package com.delcache.extend;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        if(UrlManager.isAjax(request)){
            return new ModelAndView(new MappingJackson2JsonView(), Util.error(ex.getMessage(), 400));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return new ModelAndView("redirect:/error.html", map);
    }
}
