package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.extend.UrlManager;
import com.delcache.extend.Util;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController extends BaseController {


    @RequestMapping(value = "404")
    public Object error404(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);
        response.setContentType("text/html");
        if (UrlManager.isAjax(request)) {
            return new ModelAndView(new MappingJackson2JsonView(), Util.error("您所请求的页面不存在", 404));
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/error/404");
        mv.addObject("message", "您所请求的页面不存在");
        return mv;
    }

    @RequestMapping(value = "error")
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        String message = request.getParameter("message");
        if (StringUtils.isEmpty(message)) {
            message = "未知错误";
        }
        response.setContentType("application/json");
        if (UrlManager.isAjax(request)) {
            return new ModelAndView(new MappingJackson2JsonView(), Util.error(message, 400));
        }
        response.setContentType("text/html");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/error/404");
        mv.addObject("message", message);
        return mv;
    }

}
