package com.archer.scheme.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Utils for type exchange
 *
 * @author zhilong <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/2/21 下午1:06
 */
public class TypeUtils {

    private Types types;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

    public TypeUtils(Types types, Elements elements) {
        this.types = types;

        parcelableType = elements.getTypeElement(Consts.PARCELABLE).asType();
        serializableType = elements.getTypeElement(Consts.SERIALIZABLE).asType();
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case Consts.BYTE:
                return TypeKind.BYTE.ordinal();
            case Consts.SHORT:
                return TypeKind.SHORT.ordinal();
            case Consts.INTEGER:
                return TypeKind.INT.ordinal();
            case Consts.LONG:
                return TypeKind.LONG.ordinal();
            case Consts.FLOAT:
                return TypeKind.FLOAT.ordinal();
            case Consts.DOUBEL:
                return TypeKind.DOUBLE.ordinal();
            case Consts.BOOLEAN:
                return TypeKind.BOOLEAN.ordinal();
            case Consts.CHAR:
                return TypeKind.CHAR.ordinal();
            case Consts.STRING:
                return TypeKind.STRING.ordinal();
            default:
                // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {
                    // PARCELABLE
                    return TypeKind.PARCELABLE.ordinal();
                } else if (types.isSubtype(typeMirror, serializableType)) {
                    // SERIALIZABLE
                    return TypeKind.SERIALIZABLE.ordinal();
                } else {
                    return TypeKind.OBJECT.ordinal();
                }
        }
    }
}
