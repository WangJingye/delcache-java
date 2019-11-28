package com.delcache.backend.common;


import com.delcache.backend.system.service.MenuService;
import com.delcache.common.entity.SiteInfo;
import com.delcache.extend.UrlManager;
import com.delcache.extend.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

public class BaseController extends com.delcache.common.controller.BaseController {
    @Autowired
    MenuService menuService;


    public String render(String view, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        model.addAttribute("menus", this.menuService.getLayoutMenus());
        model.addAttribute("siteInfo", db.table(SiteInfo.class).find());
        model.addAttribute("activeMenu", this.menuService.getActiveMenu());
        model.addAttribute("user", request.getSession().getAttribute("user"));
        return view;
    }

    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String enctype = request.getContentType();
        //先解析普通请求
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            //data数据不解析
            if (key.equals("data")) {
                continue;
            }
            String[] valueTmp = request.getParameterValues(key);
            String value = "";
            if (valueTmp != null && valueTmp.length > 0) {
                value = String.join(",", valueTmp);
            }
            //key的最后2个字符是[]
            if (key.indexOf("]") == key.indexOf("[") + 1 && key.indexOf("]") + 1 == key.length()) {
                //默认去掉最后的一个中框号
                key = key.substring(0, key.length() - 2);
            }
            result.put(key, value);
            //对于多维数组类型的参数无法处理
//            Pair<String, Object> pair = this.parseParams(key, value, result);
//            result.put(pair.getKey(), pair.getValue());
        }
        //form-data post形式提交数据
        if (!StringUtils.isEmpty(enctype) && enctype.contains("multipart/form-data") && request.getMethod().equals("POST")) {
            //获取普通参数
            try {
                String str = request.getParameter("data");
                if (!StringUtils.isEmpty(str)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    result = objectMapper.readValue(str, result.getClass());
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    //
    public Map<String, Object> getParams() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return this.getParams(request);
    }

    public Map<String, Object> parseFile(HttpServletRequest request, String path) {
        String basePath = request.getSession().getServletContext().getRealPath("/");
        File filepath = new File(basePath + path);
        if (!filepath.exists()) {
            filepath.mkdirs();
        }
        Map<String, Object> result = new HashMap<>();
        try {
            //获取文件参数
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file = multiRequest.getFile(iter.next().toString());
                    if (file != null) {
                        //todo 存在安全隐患，文件类型需要根据文件头来判断
                        String[] tmp = file.getOriginalFilename().split("\\.");
                        String ext = tmp[tmp.length - 1];
                        String filename = DigestUtils.md5DigestAsHex(file.getInputStream()) + "." + ext;
                        filename = path + "/" + filename;
                        //上传
                        file.transferTo(new File(basePath + filename));
                        Pair<String, Object> res = this.parseParams(file.getName(), "/" + filename, result);
                        result.put(res.getKey(), res.getValue());
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }


    public Pair<String, Object> parseParams(String key, Object value, Map<String, Object> result) {
        int index = key.indexOf("[");
        //name不是数组形式，直接返回初始key
        if (Util.stringSearch(key, "[") != Util.stringSearch(key, "]") || index <= 0 || key.lastIndexOf("]") + 1 != key.length()) {
            return new Pair<>(key, bindMap(result.get(key), value));
        }
        //正确的key
        String currentKey = key.substring(0, index);
        Map<String, Object> map;
        String[] keys = key.substring(index).split("]\\[");
        for (int i = keys.length - 1; i >= 0; i--) {
            String newKey = Util.stringTrim(Util.stringTrim(keys[i], "["), "]");
            if (StringUtils.isEmpty(newKey)) {
                newKey = "0";
            }
            map = new HashMap<>();
            map.put(newKey, value);
            value = map;
        }
        return new Pair<>(currentKey, bindMap(result.get(currentKey), value));
    }

    protected Object bindMap(Object map1, Object map2) {
        if (map1 == null) {
            return map2;
        }
        if (map2 == null) {
            return map1;
        }
        String class1 = map1.getClass().getName();
        String class2 = map2.getClass().getName();
        if (class1.equals("java.lang.String")) {
            return map1;
        } else if (class2.equals("java.lang.String")) {
            return map2;
        } else if (class1.equals("java.util.HashMap") && class2.equals(class1)) {
            Map<String, Object> m1 = (Map<String, Object>) map1;
            for (Map.Entry<String, Object> m2 : ((Map<String, Object>) map2).entrySet()) {
                String key = m2.getKey();
                m1.put(key, bindMap(m1.get(key), m2.getValue()));
            }
            return m1;
        } else {
            return map1;
        }
    }

}
