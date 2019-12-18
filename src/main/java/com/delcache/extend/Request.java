package com.delcache.extend;


import javafx.util.Pair;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

public class Request {

    public Map<String, List<String>> parameters = new HashMap<>();

    private HttpServletRequest httpServletRequest;

    public static Request getInstance(HttpServletRequest httpServletRequest) {
        Request request = new Request();
        request.httpServletRequest = httpServletRequest;
        Enumeration enumeration = httpServletRequest.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            int index = key.indexOf("[");
            String newKey = key;
            if (index != -1) {
                newKey = key.substring(0, index);
            }
            if (newKey.isEmpty()) {
                continue;
            }
            List<String> oldValue = request.parameters.get(newKey);
            if (oldValue == null) {
                oldValue = new ArrayList<>();
            }
            oldValue.add(key);
            request.parameters.put(newKey, oldValue);
        }
        return request;
    }

    public Map<Integer, String> parseFileByName(String name, String savePath) throws Exception {
        String basePath = this.httpServletRequest.getSession().getServletContext().getRealPath("/");
        Request.getInstance(this.httpServletRequest).parameters.get(name);
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) this.httpServletRequest;
        Iterator iter = multiRequest.getFileNames();
        Map<Integer, String> result = new TreeMap<>();
        int startKey = 10000;
        while (iter.hasNext()) {
            //一次遍历所有文件
            List<MultipartFile> files = multiRequest.getFiles(iter.next().toString());
            for (MultipartFile file : files) {
                String inputName = file.getName();
                if (!inputName.equals(name) && !inputName.startsWith(name + "[")) {
                    continue;
                }
                if (file.isEmpty()) {
                    continue;
                }
                int key = this.getKeyAndName(inputName).getKey();
                if (key == -1) {
                    key = startKey;
                    startKey++;
                }
                String[] tmp = file.getOriginalFilename().split("\\.");
                String ext = tmp[tmp.length - 1];
                //todo 存在安全隐患，文件类型需要根据文件头来判断
                String filename = DigestUtils.md5DigestAsHex(file.getInputStream()) + "." + ext;
                filename = savePath + "/" + filename;
                //文件保存
                file.transferTo(new File(basePath + filename));
                result.put(key, "/" + filename);
            }
        }
        return result;
    }

    //只支持一维数组和字符串 file[]和file形式文件上传
    public String parseFileAndParams(String name, String savePath) {
        //todo 替换原来表单提交的图片地址
        String basePath = this.httpServletRequest.getSession().getServletContext().getRealPath("/");
        File filepath = new File(basePath + savePath);
        if (!filepath.exists()) {
            filepath.mkdirs();
        }
        String addName = name + "_add";
        try {
            Map<Integer, String> result = new TreeMap<>();
            //获取文件参数
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(this.httpServletRequest.getSession().getServletContext());
            if (multipartResolver.isMultipart(this.httpServletRequest)) {
                //将request变成多部分request
                result.putAll(this.parseFileByName(name, savePath));
                Map<Integer, String> addResult = this.parseFileByName(addName, savePath);
                for (Map.Entry<Integer, String> add : addResult.entrySet()) {
                    result.put(add.getKey(), add.getValue());
                }
            }
            List<String> keys = this.parameters.get(name);
            int i = 0;
            for (String key : keys) {
                int k = this.getKeyAndName(key).getKey();
                if (k == -1) {
                    k = i;
                    i++;
                }
                result.put(k, this.httpServletRequest.getParameter(key));
            }
            return String.join(",", result.values());
        } catch (Exception e) {
            return "";
        }
    }

    private Pair<Integer, String> getKeyAndName(String inputName) {
        int index = inputName.indexOf("[");

        if (index == -1) {
            return new Pair<>(-1, inputName);
        }
        String name = inputName.substring(0, index);

        int lastIndex = inputName.lastIndexOf("]");
        String stringKey = inputName.substring(index + 1, lastIndex);
        int key = -1;
        if (!stringKey.isEmpty()) {
            try {
                key = Integer.parseInt(stringKey);
            } catch (Exception e) {

            }
        }
        return new Pair<>(key, name);
    }

    //对于多维数组类型的参数无法处理
    public Map<String, Object> getParams() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : this.parameters.entrySet()) {
            for (String key : entry.getValue()) {
                String[] valueTmp = this.httpServletRequest.getParameterValues(key);
                String value = "";
                if (valueTmp != null && valueTmp.length > 0) {
                    value = String.join(",", valueTmp);
                }
                result.put(key, value);
            }
        }
        if (!StringUtils.isEmpty(result.get("search_type")) && !StringUtils.isEmpty(result.get("search_value"))) {
            result.put(result.get("search_type").toString(), result.get("search_value"));
        }
        return result;
    }
}
