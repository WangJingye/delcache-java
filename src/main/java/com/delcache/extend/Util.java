package com.delcache.extend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", message);
        result.put("data", data);
        return result;
    }

    public static Map<String, Object> success(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", message);
        result.put("data", null);
        return result;
    }

    public static Map<String, Object> error(String message) {
        return Util.error(message, 400);
    }

    public static Map<String, Object> error(String message, int code) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", null);
        return result;
    }

    public static Map<String, Object> error(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("message", message);
        result.put("data", data);
        return result;
    }

    public static String stringTrim(String str, String element) {
        int elementLength = element.length();
        boolean beginIndexFlag = (str.indexOf(element) == 0);
        boolean endIndexFlag = (str.lastIndexOf(element) + elementLength == str.length());
        while (beginIndexFlag || endIndexFlag) {
            int start = str.indexOf(element);
            if (start == 0) {
                str = str.substring(elementLength);
            }
            int end = str.lastIndexOf(element);

            if (end != -1 && end + elementLength == str.length()) {
                str = str.substring(0, end);
            }
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) != -1 && str.lastIndexOf(element) + elementLength == str.length());
        }
        return str;
    }

    public static int stringSearch(String str, String search) {
        int n = 0;//计数器
        int index = str.indexOf(search);
        while (index != -1) {
            n++;
            index = str.indexOf(search, index + 1);
        }

        return n;
    }

    public static List<String> arrayColumn(List list, String value) {
        List<String> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };
        for (Object obj : list) {
            Map<String, Object> map = objectMapper.convertValue(obj, typeRef);
            Object newValue = map.get(value);
            if (StringUtils.isEmpty(map.get(value))) {
                newValue = "";
            }
            result.add(newValue.toString());
        }
        return result;
    }

    public static Map<String, String> arrayColumn(List list, String value, String key) {
        Map<String, String> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };
        for (Object obj : list) {
            Map<String, Object> map = objectMapper.convertValue(obj, typeRef);
            Object newKey = map.get(key);
            Object newValue = map.get(value);
            if (StringUtils.isEmpty(newKey)) {
                newKey = "";
            }
            if (StringUtils.isEmpty(newKey)) {
                newValue = "";
            }
            result.put(newKey.toString(), newValue.toString());
        }
        return result;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Object s, String format) {
        long lt = Long.parseLong(s.toString()) * 1000;
        if (lt == 0) {
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Object s) {
        long lt = Long.parseLong(s.toString()) * 1000;
        if (lt == 0) {
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * String左对齐
     */
    public static String strPad(Object src, int len, String ch, int padType) {
        if (StringUtils.isEmpty(src)) {
            src = "";
        }
        String res = src.toString();
        int diff = len - res.length();
        if (diff <= 0) {
            return res;
        }
        int size = (int) Math.floor((double) diff / ch.length());
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < size; i++) {
            pad.append(ch);
        }

        //剩余要补的长度
        int leave = diff - (size * ch.length());
        if (leave > 0) {
            pad.append(ch, 0, leave);
        }
        if (padType == 1) {
            res = pad.append(res).toString();
        } else {
            res = res + pad.toString();
        }
        return res;
    }

    public static String objectToString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }

    public static Object stringToObject(String str, Class clazz) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static int parseInt(Object str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        int res;
        try {
            res = Integer.parseInt(str.toString());
        } catch (NumberFormatException e) {
            res = 0;
        }
        return res;
    }

    public static double parseFloat(Object str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        double res;
        try {
            res = Double.parseDouble(str.toString());
        } catch (NumberFormatException e) {
            res = 0;
        }
        return res;
    }

    public static int time() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
