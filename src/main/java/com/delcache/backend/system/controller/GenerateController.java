package com.delcache.backend.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GenerateController {
    @RequestMapping(value = "generate/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "system/generate/index";
    }
}
