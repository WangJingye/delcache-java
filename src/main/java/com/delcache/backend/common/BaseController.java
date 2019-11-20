package com.delcache.backend.common;


import org.springframework.ui.Model;

public class BaseController extends com.delcache.common.controller.BaseController {
    public String render(String view,Model model){

        return view;
    }
}
