package com.scheme.annotation;

/**
 * activity路由
 * Created by ljq on 2020/7/14
 */
public interface ISchemeProvider {

    /**
     * Desc: 根据uri路径查找activity
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     *
     * @param schemePath 路由路径
     */
    Class findClass(String schemePath);
}
