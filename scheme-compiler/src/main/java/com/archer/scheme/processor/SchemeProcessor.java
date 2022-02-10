package com.archer.scheme.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.scheme.annotation.ISchemeProvider;
import com.scheme.annotation.SchemePath;

import java.io.IOException;
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
 * 路由注解处理
 * Created by ljq on 2020/7/14
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({Consts.ANNOTATION_SCHEME_PATH})
@AutoService(Processor.class)
public class SchemeProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        log("ExtraProcessor =>>>>>>>>>>>>>  init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> schemePath = roundEnvironment.getElementsAnnotatedWith(SchemePath.class);
        log("ExtraProcessor =>>>>>>>>>>>>>  process");
        if (schemePath != null && schemePath.size() > 0) {
            generate(schemePath);
        }
        return true;
    }

    private void generate(Set<? extends Element> schemePaths) {
        TypeSpec.Builder builder = null;
        MethodSpec.Builder methodBuild = null;
        String classNameStr;
        boolean hasCreateClass = false;
        for (Element element : schemePaths) {
            SchemePath annotation = element.getAnnotation(SchemePath.class);
            String path = annotation.value();
            classNameStr = path.substring(0, path.indexOf("/"));
            if (!hasCreateClass) {
                builder = TypeSpec.classBuilder(classNameStr + Consts.SEPARATOR + "Scheme")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addSuperinterface(ISchemeProvider.class);
                hasCreateClass = true;
                methodBuild = MethodSpec.methodBuilder("findClass")
                        .returns(Class.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Override.class)
                        .addParameter(String.class, "schemePath")
                        .addCode(" switch (schemePath) {\n");
            }
            TypeElement enclosingElement = (TypeElement) element;
            ClassName className = ClassName.get(enclosingElement);
            methodBuild.addCode("  case \"" + path + "\":\n" +
                    "      return " + className + ".class;\n");
        }
        if (builder != null) {
            methodBuild.addCode("}\n" +
                    " return null;\n");
            builder.addMethod(methodBuild.build());
            JavaFile file = JavaFile.builder(Consts.PROVIDER_PACKAGE, builder.build()).build();
            try {
                file.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
