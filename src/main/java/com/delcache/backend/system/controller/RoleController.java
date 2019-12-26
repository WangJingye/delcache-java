package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.backend.system.service.RoleService;
import com.delcache.common.entity.*;
import com.delcache.extend.Db;
import com.delcache.extend.Request;
import com.delcache.extend.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class RoleController extends BaseController {

    private Map<String, String> statusList = new LinkedHashMap<String, String>() {
        {
            put("1", "可用");
            put("0", "禁用");
        }
    };

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "system/role/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        Map<String, Object> params = Request.getInstance(request).getParams();
        Map<String, Object> res = this.roleService.getList(params);
        model.addAttribute("list", res.get("list"));
        model.addAttribute("pagination", res.get("pagination"));
        model.addAttribute("statusList", this.statusList);
        model.addAttribute("params", params);
        return this.render("system/role/index", model);
    }

    @RequestMapping(value = "system/role/edit", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, Model model) throws Exception {
        String id = request.getParameter("id");
        model.addAttribute("title", "创建角色");
        if (Util.parseInt(id) != 0) {
            Role data = (Role) Db.table(Role.class).where("id", id).find();
            if (data == null) {
                throw new Exception("参数有误");
            }
            model.addAttribute("data", data);
            model.addAttribute("title", "编辑角色 - " + data.getName());
        }
        return this.render("system/role/edit", model);
    }

    @ResponseBody
    @RequestMapping(value = "system/role/edit", method = RequestMethod.POST)
    public Object edit(HttpServletRequest request) {
        try {
            Map<String, Object> params = Request.getInstance(request).getParams();
            Role role;
            if (Util.parseInt(params.get("id")) != 0) {
                role = (Role) Db.table(Role.class).where("id", params.get("id")).find();
            } else {
                params.remove("id");
                role = new Role();
            }
            role.load(params);
            Db.table(Role.class).save(role);
            return this.success("操作成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "system/role/set-status", method = RequestMethod.POST)
    public Object setStatus(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (Util.parseInt(id) == 0) {
                throw new Exception("参数有误");
            }
            Db.table(Role.class).where("id", id).update("status", request.getParameter("status"));
            return this.success("操作成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }

    @RequestMapping(value = "system/role/set-role-admin", method = RequestMethod.GET)
    public String setRoleAdmin(HttpServletRequest request, Model model) throws Exception {
        String id = request.getParameter("id");
        if (Util.parseInt(id) == 0) {
            throw new Exception("参数有误");
        }
        Role data = (Role) Db.table(Role.class).where("id", id).find();
        List<RoleAdmin> rows = (List<RoleAdmin>) Db.table(RoleAdmin.class).where("role_id", id).findAll();
        List<String> adminIdList = Util.arrayColumn(rows, "adminId");
        List<Admin> adminRows = (List<Admin>) Db.table(Admin.class).where("identity", 0).findAll();
        Map<String, String> adminList = Util.arrayColumn(adminRows, "realname", "adminId");
        model.addAttribute("data", data);
        model.addAttribute("adminList", adminList);
        model.addAttribute("adminIdList", adminIdList);
        return this.render("system/role/set-role-admin", model);
    }

    @ResponseBody
    @RequestMapping(value = "system/role/set-role-admin", method = RequestMethod.POST)
    public Object setRoleAdmin(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (Util.parseInt(id) == 0) {
                throw new Exception("参数有误");
            }
            Map<String, Object> params = Request.getInstance(request).getParams();
            this.roleService.setRoleAdmin(params);
            return this.success("设置成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }

    @RequestMapping(value = "system/role/set-role-menu", method = RequestMethod.GET)
    public String setRoleMenu(HttpServletRequest request, Model model) throws Exception {
        if (Util.parseInt(request.getParameter("id")) == 0) {
            throw new Exception("参数有误");
        }
        Role data = (Role) Db.table(Role.class).where("id", request.getParameter("id")).find();
        List<RoleMenu> rows = (List<RoleMenu>) Db.table(RoleMenu.class).where("role_id", request.getParameter("id")).findAll();
        List<String> roleMenuIds = Util.arrayColumn(rows, "menuId");
        List<Menu> menuList = (List<Menu>) Db.table(Menu.class).where("status", 1).order("sort desc,create_time asc").findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Menu menu : menuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("pId", menu.getParentId());
            map.put("name", menu.getName());
            if (roleMenuIds.size() > 0 && roleMenuIds.contains(String.valueOf(menu.getId()))) {
                map.put("checked", true);
            }
            result.add(map);
        }
        model.addAttribute("menuList", result);
        model.addAttribute("data", data);
        return this.render("system/role/set-role-menu", model);
    }

    @ResponseBody
    @RequestMapping(value = "system/role/set-role-menu", method = RequestMethod.POST)
    public Object setRoleMenu(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (Util.parseInt(id) == 0) {
                throw new Exception("参数有误");
            }
            Map<String, Object> params = Request.getInstance(request).getParams();
            this.roleService.setRoleMenu(params);
            return this.success("设置成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }
}
