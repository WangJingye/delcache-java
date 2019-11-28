package com.delcache.common.entity;

import com.delcache.extend.Util;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseEntity {
    @Transient
    public Object extra;

    public void load(Map<String, Object> map) {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> nMap = new HashMap<>();
        List<Field> existFields=new ArrayList<>();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            String key = column.name();
            if (!map.containsKey(key)) {
                continue;
            }
            Object value = map.get(key);
            Class fieldClazz = field.getType();
            String type = fieldClazz.getName();
            if (type.equals("java.lang.Integer")
                    || type.equals("java.lang.Float")
                    || type.equals("java.lang.double")
                    || type.equals("java.lang.Long")
            ) {
                if (StringUtils.isEmpty(value)) {
                    value = "0";
                }
            }
            existFields.add(field);
            nMap.put(field.getName(), value);
        }

        String str = Util.objectToString(nMap);
        Object newEntity = Util.stringToObject(str, this.getClass());
        for (Field field : existFields) {
            Object newValue = new Object();
            try{
                field.setAccessible(true);
                newValue = field.get(newEntity);
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method setMethod = pd.getWriteMethod();
                setMethod.invoke(this, newValue);
            }catch (Exception e){

            }

        }
    }
}
