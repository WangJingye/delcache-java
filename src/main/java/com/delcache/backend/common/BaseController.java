package com.delcache.backend.common;


import com.delcache.backend.system.service.MenuService;
import com.delcache.common.entity.SiteInfo;
import com.delcache.component.Db;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController extends com.delcache.common.controller.BaseController {
    @Autowired
    MenuService menuService;

    public String render(String view, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        model.addAttribute("menus", this.menuService.getLayoutMenus());
        model.addAttribute("siteInfo", Db.table(SiteInfo.class).find());
        model.addAttribute("activeMenu", this.menuService.getActiveMenu());
        model.addAttribute("user", request.getSession().getAttribute("user"));
        return view;
    }
}
