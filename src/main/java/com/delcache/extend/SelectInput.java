package com.delcache.extend;

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
        String html = "";
        String id = name;
        if (multi) {
            name = name + "[]";
        }
        if (type.equals("radio") || type.equals("checkbox")) {
            html += "<div class=\"form-radio-group\">";
            for (Map.Entry<String, String> v : map.entrySet()) {
                String checked = checks.contains(v.getKey()) ? "checked" : "";
                html += "<label class=\"" + type + "-inline\">" +
                        "<input type=\"" + type + "\" name=\"" + name + "\" id=\"" + id + "\" value=\"" + v.getKey() + "\" " + checked + "> " +
                        v.getValue() + "</label>";
            }
            html += "</div>";
        } else if (type.equals("select") || type.equals("select2")) {
            String multiString = multi ? "multiple" : "";
            html += "<select name=\"" + name + "\" class=\"form-control" + (type.equals("select2") ? " select2" : "") + "\" id=\"" + id + "\" " + multiString + ">";
            if (!multi) {
                //单选增加请选择选项
                html += "<option value=\"\">请选择</option>";
            }
            for (Map.Entry<String, String> v : map.entrySet()) {
                String checked = checks.contains(v.getKey()) ? "selected" : "";
                html += "<option value=\"" + v.getKey() + "\" " + checked + ">" + v.getValue() + "</option>";
            }
            html += "</select>";
        }
        return html;
    }
}
