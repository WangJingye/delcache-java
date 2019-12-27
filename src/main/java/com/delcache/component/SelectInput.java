package com.delcache.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectInput {

    /**
     * 单选
     *
     * @param map
     * @param check
     * @param name
     * @param type
     * @return
     */
    public static String show(Map<String, String> map, String check, String name, String type) {
        List<String> checks = new ArrayList<>();
        checks.add(check);
        return SelectInput.show(map, checks, name, type, false);
    }

    /**
     * 单选
     *
     * @param list
     * @param check
     * @param name
     * @param type
     * @return
     */
    public static String show(List<String> list, String check, String name, String type) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String v : list) {
            map.put(v, v);
        }
        List<String> checks = new ArrayList<>();
        checks.add(check);
        return SelectInput.show(map, checks, name, type, false);
    }

    /**
     * 多选
     *
     * @param list
     * @param checks
     * @param name
     * @param type
     * @return
     */
    public static String show(List<String> list, List<String> checks, String name, String type) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String v : list) {
            map.put(v, v);
        }
        return SelectInput.show(map, checks, name, type, true);
    }

    /**
     * 多选
     *
     * @param map
     * @param checks
     * @param name
     * @param type
     * @return
     */
    public static String show(Map<String, String> map, List<String> checks, String name, String type) {
        return SelectInput.show(map, checks, name, type, true);

    }

    protected static String show(Map<String, String> map, List<String> checks, String name, String type, Boolean multi) {
        StringBuilder html = new StringBuilder();
        String id = name;
        if (multi) {
            name = name + "[]";
        }
        if (type.equals("radio") || type.equals("checkbox")) {
            html.append("<div class=\"form-radio-group\">");
            for (Map.Entry<String, String> v : map.entrySet()) {
                String checked = checks.contains(v.getKey()) ? "checked" : "";
                html.append("<label class=\"").append(type).append("-inline\">" )
                        .append("<input type=\"").append(type).append("\" name=\"").append(name).append("\" id=\"").append(id).append("\" value=\"").append(v.getKey()).append("\" ").append(checked).append("> ")
                        .append(v.getValue()).append("</label>");
            }
            html.append("</div>");
        } else if (type.equals("select") || type.equals("select2")) {
            String multiString = multi ? "multiple" : "";
            html.append("<select name=\"").append(name).append("\" class=\"form-control").append((type.equals("select2") ? " select2" : "")).append("\" id=\"").append(id).append("\" ").append(multiString).append(">");
            if (!multi) {
                //单选增加请选择选项
                html.append("<option value=\"\">请选择</option>");
            }
            for (Map.Entry<String, String> v : map.entrySet()) {
                String checked = checks.contains(v.getKey()) ? "selected" : "";
                html.append("<option value=\"").append(v.getKey()).append("\" ").append(checked).append(">").append(v.getValue()).append("</option>");
            }
            html.append("</select>");
        }
        return html.toString();
    }
}
