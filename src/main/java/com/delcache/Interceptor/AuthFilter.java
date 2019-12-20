package com.delcache.Interceptor;

import com.delcache.backend.common.config.Config;
import com.delcache.common.entity.Admin;
import com.delcache.common.entity.Menu;
import com.delcache.common.entity.OperationLog;
import com.delcache.common.entity.RoleMenu;
import com.delcache.extend.Db;
import com.delcache.extend.Request;
import com.delcache.extend.UrlManager;
import com.delcache.extend.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by admin on 2017/4/10.
 */
public class AuthFilter implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //  后台session控制
        return AuthFilter(request, response, o);
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

    private Boolean AuthFilter(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        Admin user = (Admin) request.getSession().getAttribute("user");
        boolean checkLogin = true;
        for (Map.Entry<String, String[]> entry : Config.actionNoLoginList.entrySet()) {
            for (String action : entry.getValue()) {
                String uri = "/" + Util.stringTrim(entry.getKey() + "/" + action, "/");
                if (UrlManager.parseRequestUrl(request.getRequestURI()).equals(uri)) {
                    checkLogin = false;
                    break;
                }
            }
        }
        //不需要检测登录，那么同时就不需要验证权限
        if (!checkLogin) {
            return true;
        }
        if (null == user) {
            return this.redirectLogin(request, response);
        }
        boolean checkAuth = true;
        for (Map.Entry<String, String[]> entry : Config.actionWhiteList.entrySet()) {
            for (String action : entry.getValue()) {
                String uri = "/" + Util.stringTrim(entry.getKey() + "/" + action, "/");
                if (UrlManager.parseRequestUrl(request.getRequestURI()).equals(uri)) {
                    checkAuth = false;
                    break;
                }
            }
        }
        //不需要检测权限
        if (!checkAuth) {
            return true;
        }
        //用户被禁用
        if (user.getStatus() == 0) {
            return this.redirectNoAuth(request, response);
        }
        //验证用户权限,identity = 1 为超级管理员
        if (user.getIdentity() == 0 && !this.checkUserAuth(request)) {
            return this.redirectNoAuth(request, response);
        }
        if (UrlManager.isAjax(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String params = objectMapper.writeValueAsString(Request.getInstance(request).getParams());
            OperationLog opLog = new OperationLog();
            opLog.setCreateTime(Util.time());
            opLog.setOperatorId(user.getAdminId());
            opLog.setUrl(request.getRequestURI());
            opLog.setPost(params);
            opLog.setReferUrl(request.getHeader("Referer"));
            opLog.setIp(Util.getRemoteIp(request));
            Db.table(OperationLog.class).save(opLog);
        }
        return true;
    }

    private Boolean redirectLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        if (UrlManager.isAjax(request)) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            out.print(Util.objectToString(Util.error("未登录", 999)));
            out.close();
        } else {
            response.sendRedirect("/login.html");//todo 跳转到报错界面，然后再跳到登录界面
        }
        return false;
    }

    private Boolean redirectNoAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        if (UrlManager.isAjax(request)) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            out.print(Util.objectToString(Util.error("您暂无该权限")));
            out.close();
        } else {
            throw new Exception("您暂无该权限");
        }
        return false;
    }

    private Boolean checkUserAuth(HttpServletRequest request) {
        String uri = UrlManager.parseRequestUrl(request.getRequestURI());
        Menu currentMenu = (Menu) Db.table(Menu.class).where("url", uri).find();
        Admin user = (Admin) request.getSession().getAttribute("user");
        String sql = "select a.* from tbl_role_menu as a left join tbl_role_admin as b on b.role_id = a.role_id where b.admin_id=\"" + user.getAdminId() + "\" and a.menu_id=\"" + currentMenu.getId() + "\"";
        Map<String, Object> roleMenu = (Map<String, Object>) Db.table(Map.class).find(sql);
        if (roleMenu == null) {
            return false;
        }
        return true;
    }

    /**
     * @param request
     * @return Create Date:2013-6-5
     * @author Shine
     * Description:获取IP
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
