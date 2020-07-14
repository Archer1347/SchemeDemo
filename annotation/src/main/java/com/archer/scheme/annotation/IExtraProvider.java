package com.archer.scheme.annotation;

/**
 * uri参数解析及activity参数注入
 * Created by ljq on 2020/7/14
 */
public interface IExtraProvider {

    /**
     * Desc: 解析uri参数，并放入intent
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     *
     * @param uriObject Uri
     * @param intentObject Intent
     */
    void convertParameterType(Object uriObject, Object intentObject);

    /**
     * Desc: 注入activity参数
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     */
    void inject(Object object);
}
