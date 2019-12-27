package com.delcache.component;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (UrlManager.isAjax(request)) {
            return new ModelAndView(new MappingJackson2JsonView(), Util.error(ex.getMessage(), 400));
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("url", "/");
        modelAndView.setViewName("system/error/index");
        return modelAndView;
    }
}
