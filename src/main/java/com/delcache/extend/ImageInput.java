package com.delcache.extend;

import org.springframework.util.StringUtils;

public class ImageInput {
    public static String show(String[] images, String name, int count) {
        String html = "<div class=\"fileinput-box-list\" data-max=\"" + String.valueOf(count) + "\">";
        String newName = name;
        for (int i = 0; i < images.length; i++) {
            String image = images[i];
            if (count != 1) {
                newName = name + "[" + String.valueOf(i) + "]";
            }
            html += "<div class=\"fileinput-box\">" +
                    "<img src=\"" + image + "\">" +
                    "<input type=\"hidden\" name=\"" + newName + "\" value=\"" + image + "\">" +
                    "<div class=\"fileinput-button\">" +
                    "<div class=\"plus-symbol\" style=\"display: none\">+</div>" +
                    "<input class=\"fileinput-input\" type=\"file\" name=\"" + newName + "\">" +
                    "</div>" +
                    "<div class=\"file-remove-btn\">" +
                    "<div class=\"btn btn-sm btn-danger\" style=\"font-size: 0.5rem;\">删除</div>" +
                    "</div></div>";
        }
        if (images.length < count) {
            if (count != 1) {
                newName = name + "_add[]";
            }
            html += "<div class=\"fileinput-box\">" +
                    "<div class=\"fileinput-button\">" +
                    "<div class=\"plus-symbol\"> +</div >" +
                    "<input class=\"fileinput-input add-new\" type=\"file\" name =\"" + newName + "\" data-name=\"" + name + "_add\">" +
                    "</div></div>";
        }
        html += "</div>";
        return html;
    }

    public static String show(String value, String name, int count) {
        String[] images = StringUtils.isEmpty(value) ? new String[]{} : value.split(",");
        return ImageInput.show(images, name, count);
    }

    public static String show(String value, String name) {
        return ImageInput.show(value, name, 1);
    }
}
