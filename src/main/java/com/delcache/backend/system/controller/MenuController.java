package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.backend.system.service.MenuService;
import com.delcache.common.entity.Menu;
import com.delcache.extend.Db;
import com.delcache.extend.Request;
import com.delcache.extend.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class MenuController extends BaseController {

    private Map<String, String> statusList = new LinkedHashMap<String, String>() {
        {
            put("1", "可用");
            put("0", "禁用");
        }
    };

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "system/menu/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        Map<String, Object> params = Request.getInstance(request).getParams();
        Map<String, Object> res = this.menuService.getList(params);
        model.addAttribute("list", res.get("list"));
        model.addAttribute("pagination", res.get("pagination"));
        model.addAttribute("statusList", this.statusList);
        model.addAttribute("params", params);
        return this.render("system/menu/index", model);
    }

    @RequestMapping(value = "system/menu/edit", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, Model model) throws Exception {
        String id = request.getParameter("id");
        model.addAttribute("title", "创建菜单");
        if (!StringUtils.isEmpty(id)) {
            Menu data = (Menu) Db.table(Menu.class).where("id", id).find();
            if (data == null) {
                throw new Exception("参数有误");
            }
            model.addAttribute("data", data);
            model.addAttribute("title", "编辑菜单 - " + data.getName());
        }
        List<String> urlList = this.menuService.getAllMethodList(id);
        model.addAttribute("urlList", urlList);
        Map<String, String> parentList = this.menuService.getChildMenus(0, 0);
        model.addAttribute("parentList", parentList);
        return this.render("system/menu/edit", model);
    }

    @ResponseBody
    @RequestMapping(value = "system/menu/edit", method = RequestMethod.POST)
    public Object edit(HttpServletRequest request) {
        try {
            Map<String, Object> params = Request.getInstance(request).getParams();
            Menu menu;
            if (!StringUtils.isEmpty(params.get("id"))) {
                menu = (Menu) Db.table(Menu.class).where("id", params.get("id")).find();
                if (menu == null) {
                    throw new Exception("参数有误");
                }
            } else {
                params.remove("id");
                menu = new Menu();
            }
            menu.load(params);
            Db.table(Menu.class).save(menu);
            return this.success("操作成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "system/menu/set-status", method = RequestMethod.POST)
    public Object setStatus(HttpServletRequest request) {
        try {
            Map<String, Object> params = Request.getInstance(request).getParams();
            if (Util.parseInt(params.get("id")) == 0) {
                throw new Exception("参数有误");
            }
            Db.table(Menu.class).where("id", params.get("id")).update("status", params.get("status"));
            return this.success("操作成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }
}
