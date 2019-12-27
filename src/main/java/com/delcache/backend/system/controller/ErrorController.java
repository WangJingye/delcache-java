package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.component.UrlManager;
import com.delcache.component.Util;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController extends BaseController {

    @RequestMapping(value = "error")
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);
        response.setContentType("text/html");
        String message = (String) request.getAttribute("message");
        if (StringUtils.isEmpty(message)) {
            message = "您所请求的地址不存在";
        }
        String url = (String) request.getAttribute("redirect");
        if (StringUtils.isEmpty(url)) {
            url = "/";
        }
        response.setContentType("application/json");
        if (UrlManager.isAjax(request)) {
            return new ModelAndView(new MappingJackson2JsonView(), Util.error(message, 400));
        }
        response.setContentType("text/html");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/error/index");
        mv.addObject("message", message);
        mv.addObject("url", url);
        return mv;
    }

}
