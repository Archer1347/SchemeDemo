package com.archer.scheme.processor;

/**
 * Created by ljq on 2020/7/14
 */
class Consts {

    static final String SEPARATOR = "$$";

    /**
     * 注解依赖的包名
     */
    static final String ANNOTATION_PACKAGE = "com.archer.scheme.annotation";
    /**
     * 路由注解类全路径
     */
    static final String ANNOTATION_SCHEME_PATH = ANNOTATION_PACKAGE + ".SchemePath";
    /**
     * 参数注解类全路径
     */
    static final String ANNOTATION_SCHEME_EXTRA = ANNOTATION_PACKAGE + ".SchemeExtra";

    /**
     * 存放生成代码的包
     */
    static final String PROVIDER_PACKAGE = "com.archer.scheme.provider";

    // ----------------- Java 参数类型 ------------------  //
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String CHAR = LANG + ".Character";
    public static final String STRING = LANG + ".String";
    public static final String PARCELABLE = "android.os.Parcelable";
    public static final String SERIALIZABLE = "java.io.Serializable";

}
