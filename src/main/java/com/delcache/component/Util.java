package com.delcache.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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

    /**
     * 将对象的大写转换为下划线加小写，例如：userName-->user_name
     *
     * @param param
     * @return
     */
    public static String toUnderlineString(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));  //统一都转小写
        }
        return sb.toString();

    }

    public static String toCamelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (StringUtils.isEmpty(name)) {
            // 没必要转换
            return "";
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }

            // 驼峰片段，首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1));
        }
        return result.toString();
    }

    public static String sendRequest(String url, Map<String, String> data, String method, Map<String, String> headers) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response;
            if ("POST".equals(method)) {
                HttpPost request = new HttpPost(url);
                if (headers != null) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        request.setHeader(header.getKey(), header.getValue());
                    }
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(convertMap2NameValuePairs(data), "UTF-8");
                request.setEntity(entity);
                response = client.execute(request);
            } else {
                HttpGet request = new HttpGet(url);
                if (headers != null) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        request.setHeader(header.getKey(), header.getValue());
                    }
                }
                response = client.execute(request);
            }

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            return null;
        }
    }

    public static String sendRequest(String url, String data, String method, Map<String, String> headers) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response;
            if ("POST".equals(method)) {
                HttpPost request = new HttpPost(url);
                if (headers != null) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        request.setHeader(header.getKey(), header.getValue());
                    }
                }
                StringEntity stringEntity = new StringEntity(data, "UTF-8");
                request.setEntity(stringEntity);
                response = client.execute(request);
            } else {
                HttpGet request = new HttpGet(url);
                if (headers != null) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        request.setHeader(header.getKey(), header.getValue());
                    }
                }
                response = client.execute(request);
            }

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            return null;
        }
    }

    public static List<NameValuePair> convertMap2NameValuePairs(Map<String, String> data) {
        List<NameValuePair> nvpList = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            NameValuePair n = new BasicNameValuePair(entry.getKey(), entry.getValue());
            nvpList.add(n);
        }
        return nvpList;
    }

    public static Map<String, Object> xmlToMap(String xml) {
        try {
            Document doc = DocumentHelper.parseText(xml);//将xml转为dom对象
            Element root = doc.getRootElement();//获取根节点
            return Dom2Map(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map Dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = Dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName()
                                .equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName()
                                .equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName()
                                .equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName()
                                .equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }
}
