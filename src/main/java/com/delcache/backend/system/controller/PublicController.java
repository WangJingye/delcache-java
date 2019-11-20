package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicController extends BaseController {
    @RequestMapping(value = "system/public/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request,Model model) {
        return this.render("system/public/index", model);
    }
}
