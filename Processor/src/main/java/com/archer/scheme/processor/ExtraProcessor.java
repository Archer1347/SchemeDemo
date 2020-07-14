package com.archer.scheme.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.archer.scheme.annotation.IExtraProvider;
import com.archer.scheme.annotation.SchemeExtra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * 参数注解处理
 * Created by ljq on 2020/7/14
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({Consts.ANNOTATION_SCHEME_EXTRA})
@AutoService(Processor.class)
public class ExtraProcessor extends AbstractProcessor {

    /**
     * 存放activity下的参数map
     */
    private Map<TypeElement, List<Element>> activityAndField = new HashMap<>();
    private Filer mFiler;
    private Messager messager;
    private TypeUtils typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        typeUtils = new TypeUtils(processingEnv.getTypeUtils(), processingEnvironment.getElementUtils());
        log("ExtraProcessor =>>>>>>>>>>>>>  init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> schemePath = roundEnvironment.getElementsAnnotatedWith(SchemeExtra.class);
        log("ExtraProcessor =>>>>>>>>>>>>>  process");
        if (schemePath != null && schemePath.size() > 0) {
            createExtraMap(schemePath);
            generate();
        }
        return true;
    }

    private void generate() {
        for (Map.Entry<TypeElement, List<Element>> entry : activityAndField.entrySet()) {
            List<Element> childs = entry.getValue();
            TypeElement parent = entry.getKey();
            String className = parent.getSimpleName() + Consts.SEPARATOR + "Extra";
            TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(IExtraProvider.class);

            MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("convertParameterType")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override.class)
                    .addParameter(Object.class, "uriObject")
                    .addParameter(Object.class, "intentObject")
                    .addCode("android.net.Uri uri = (android.net.Uri) uriObject;\n")
                    .addCode("android.content.Intent intent = (android.content.Intent) intentObject;\n");

            MethodSpec.Builder injectMethodBuild = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override.class)
                    .addParameter(Object.class, "object");
            injectMethodBuild.addCode(parent + " act = (" + parent + ")object;\n");

            log("ExtraProcessor =>>>>>>>>>>>>>  parent = " + parent.getSimpleName());
            for (Element element : childs) {
                String fieldName = element.getSimpleName().toString();
                log("ExtraProcessor =>>>>>>>>>>>>>  fieldName = " + fieldName);
                log("ExtraProcessor =>>>>>>>>>>>>>  type = " + typeUtils.typeExchange(element));

                methodBuild.addCode("String " + fieldName + " = uri.getQueryParameter(\"" + fieldName + "\");\n");
                methodBuild.addCode("if (" + fieldName + " != null) {\n");
                methodBuild.addCode("    try {\n");
                String intentStatement = buildIntentStatement(typeUtils.typeExchange(element), fieldName);
                methodBuild.addStatement("        " + intentStatement, fieldName);
                methodBuild.addCode("    } catch (NumberFormatException e) {\n" +
                        "        e.printStackTrace();\n" +
                        "    }\n");
                methodBuild.addCode("}\n");

                String methodValue = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String statement = "act.set" + methodValue + "(act.getIntent().";
                String originalValue = "act.get" + methodValue + "()";
                statement = buildStatement(originalValue, statement, typeUtils.typeExchange(element)) + ")";
                injectMethodBuild.addStatement(statement, fieldName);
            }
            builder.addMethod(methodBuild.build());
            builder.addMethod(injectMethodBuild.build());
            JavaFile file = JavaFile.builder(Consts.PROVIDER_PACKAGE, builder.build()).build();
            try {
                file.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String buildIntentStatement(int type, String fieldValue) {
        switch (TypeKind.values()[type]) {
            case BOOLEAN:
                return "intent.putExtra($S, Boolean.valueOf(" + fieldValue + "))";
            case BYTE:
                return "intent.putExtra($S, Byte.valueOf(" + fieldValue + "))";
            case SHORT:
                return "intent.putExtra($S, Short.valueOf(" + fieldValue + "))";
            case INT:
                return "intent.putExtra($S, Integer.valueOf(" + fieldValue + "))";
            case LONG:
                return "intent.putExtra($S, Long.valueOf(" + fieldValue + "))";
            case CHAR:
                return "intent.putExtra($S, Character.valueOf(" + fieldValue + "))";
            case FLOAT:
                return "intent.putExtra($S, Float.valueOf(" + fieldValue + "))";
            case DOUBLE:
                return "intent.putExtra($S, Double.valueOf(" + fieldValue + "))";
            case STRING:
                return "intent.putExtra($S, " + fieldValue + ")";
            default:
                return null;
        }
    }

    private String buildStatement(String originalValue, String statement, int type) {
        switch (TypeKind.values()[type]) {
            case BOOLEAN:
                statement += "getBooleanExtra($S, " + originalValue + ")";
                break;
            case BYTE:
                statement += "getByteExtra($S, " + originalValue + ")";
                break;
            case SHORT:
                statement += "getShortExtra($S, " + originalValue + ")";
                break;
            case INT:
                statement += "getIntExtra($S, " + originalValue + ")";
                break;
            case LONG:
                statement += "getLongExtra($S, " + originalValue + ")";
                break;
            case CHAR:
                statement += "getCharExtra($S, " + originalValue + ")";
                break;
            case FLOAT:
                statement += "getFloatExtra($S, " + originalValue + ")";
                break;
            case DOUBLE:
                statement += "getDoubleExtra($S, " + originalValue + ")";
                break;
            case STRING:
                statement += "getStringExtra($S)";
                break;
            default:
                break;
        }
        return statement;
    }

    private void createExtraMap(Set<? extends Element> elements) {
        for (Element element : elements) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            if (activityAndField.containsKey(enclosingElement)) {
                activityAndField.get(enclosingElement).add(element);
            } else {
                List<Element> list = new ArrayList<>();
                list.add(element);
                activityAndField.put(enclosingElement, list);
            }
        }
    }

    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg + "\n");
    }
}
