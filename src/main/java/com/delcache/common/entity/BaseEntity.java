package com.delcache.common.entity;

import com.delcache.component.DbIgnore;
import com.delcache.component.Util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BaseEntity {

    @DbIgnore
    public Object extra;

    public void load(Map<String, Object> map) {
        Class clazz = this.getClass();
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
                if (methodMap.containsKey(setMethodName)) {
                    Object value = map.get(column);
                    Method setMethod = methodMap.get(setMethodName);
                    setMethod.invoke(this, value);
                }
            }
        } catch (Exception e) {

        }
    }
}
