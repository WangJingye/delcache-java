package com.delcache.extend;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UrlManager {

    //解析注解的url
    public static String parseMethodUrl(String url) {
        return Util.stringTrim(Util.stringTrim(url, "["), "]");
    }

    //解析request的url
    public static String parseRequestUrl(String url) {
        if (url.equals("/")) {
            return "/system/menu/index";
        }
        return "/" + Util.stringTrim(Util.stringTrim(url, "/"), ".html");
    }


    public static String createUrl(String uri) {
        Map<String, String> params = new HashMap<>();
        return UrlManager.createUrl(uri, params);
    }

    public static String createUrl(String uri, Map<String, String> params) {
        if (uri.equals("/")) {
            return uri;
        }
        String[] uriArr = uri.split("\\?");
        if (uriArr.length == 2) {
            String[] uriParams = uriArr[1].split("&");
            String[] uriParamArr;
            for (String uriParam : uriParams) {
                uriParamArr = uriParam.split("=");
                if (uriParamArr.length == 2) {
                    params.put(uriParamArr[0], uriParamArr[1]);
                }
            }
        }
        String[] trimStrings = new String[]{".html", ".jsp", "/html", "/"};
        String res = uriArr[0];
        for (String trimString : trimStrings) {
            res = Util.stringTrim(res, trimString);
        }
        res += ".html";
        String resParams = "";
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (StringUtils.isEmpty(resParams)) {
                resParams = "?";
            } else {
                resParams += "&";
            }
            resParams += param.getKey() + "=" + param.getValue();
        }
        return "/" + res + resParams;
    }

    public static Boolean isAjax(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getHeader("X-Requested-With")) && request.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }
}
