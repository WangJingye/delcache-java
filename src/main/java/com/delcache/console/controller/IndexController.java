package com.delcache.console.controller;


import com.delcache.common.entity.Admin;
import com.delcache.common.entity.SiteInfo;
import com.delcache.extend.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class IndexController {
    public static void main(String[] args) {
        SiteInfo siteInfo = new SiteInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("web_ip", "122222");

        Class clazz = siteInfo.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> methodMap = new HashMap<>();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }
        try {
            for (Field field : fields) {
                String f = field.getName();
                String column = Util.toUnderlineString(f);
                if (!map.containsKey(column)) {
                    continue;
                }
                String setMethodName = "set" + Util.toCamelName(f);
                System.out.println(setMethodName);
                if (methodMap.containsKey(setMethodName)) {
                    Object value = map.get(column);
                    Method setMethod = methodMap.get(setMethodName);
                    setMethod.invoke(siteInfo, value);
                }
            }
        } catch (Exception e) {

        }
    }
}