package com.delcache.backend.system.controller;

import com.delcache.backend.common.BaseController;
import com.delcache.common.entity.Admin;
import com.delcache.extend.Encrypt;
import com.delcache.extend.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PublicController extends BaseController {


    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model) {
        model.addAttribute("title", "后台管理系统");
        return "system/public/login";
    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new Exception("用户名密码有误");
            }
            Admin admin = (Admin) db.table(Admin.class).where("username", username).find();
            if (admin == null || !admin.getPassword().equals(Encrypt.encryptPassword(password, admin.getSalt()))) {
                throw new Exception("用户名密码有误");
            }
            if (admin.getStatus() == 0) {
                throw new Exception("您的账号已禁用，请联系管理员～");
            }
            admin.setLastLoginTime(Util.time());
            db.table(Admin.class).save(admin);
            request.getSession().setAttribute("user", admin);
            return this.success("登录成功");
        } catch (Exception e) {
            return this.error(e.getMessage());
        }
    }

    @RequestMapping(value = "logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Admin user = (Admin) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().removeAttribute("user");
        }
        response.sendRedirect("/");
    }
}
