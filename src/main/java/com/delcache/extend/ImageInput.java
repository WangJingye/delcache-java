package com.delcache.extend;

import org.springframework.util.StringUtils;

public class ImageInput {
    public static String show(String[] images, String name, int count) {
        StringBuilder html = new StringBuilder("<div class=\"fileinput-box-list\" data-max=\"" + String.valueOf(count) + "\">");
        String newName = name;
        for (int i = 0; i < images.length; i++) {
            String image = images[i];
            if (count != 1) {
                newName = name + "[" + String.valueOf(i) + "]";
            }
            html.append("<div class=\"fileinput-box\">")
                    .append("<img src=\"").append(image).append("\">")
                    .append("<input type=\"hidden\" name=\"").append(newName).append("\" value=\"").append(image).append("\">")
                    .append("<div class=\"fileinput-button\">")
                    .append("<div class=\"plus-symbol\" style=\"display: none\">+</div>")
                    .append("<input class=\"fileinput-input\" type=\"file\" name=\"").append(newName).append("\">")
                    .append("</div>")
                    .append("<div class=\"file-remove-btn\">")
                    .append("<div class=\"btn btn-sm btn-danger\" style=\"font-size: 0.5rem;\">删除</div>")
                    .append("</div></div>");
        }
        if (images.length < count) {
            if (count != 1) {
                newName = name + "_add[]";
            }
            html.append("<div class=\"fileinput-box\">")
                    .append("<div class=\"fileinput-button\">")
                    .append("<div class=\"plus-symbol\">+</div >")
                    .append("<input class=\"fileinput-input add-new\" type=\"file\" name =\"").append(newName).append("\" data-name=\"").append(name).append("_add\">")
                    .append("</div></div>");
        }
        html.append("</div>");
        return html.toString();
    }

    public static String show(String value, String name, int count) {
        String[] images = StringUtils.isEmpty(value) ? new String[]{} : value.split(",");
        return ImageInput.show(images, name, count);
    }

    public static String show(String value, String name) {
        return ImageInput.show(value, name, 1);
    }
}
