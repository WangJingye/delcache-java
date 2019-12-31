<%@ page import="com.delcache.component.UrlManager" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<meta name="decorator" content="/html/layouts/login.jsp">
<div class="container">
    <div class="login-head"><h2>${title}</h2></div>
    <div class="login-box">
        <div class="login-title">用户登录</div>
        <form id="login-form" action="<%= UrlManager.createUrl("login") %>" method="post">
            <div class="form-group">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <input type="text" class="form-control" name="username" placeholder="用户名">
                </div>
            </div>
            <div class="form-group">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <input type="password" class="form-control" name="password" placeholder="密码">
                </div>
            </div>
            <div class="form-group captcha-box">
                <input type="text" class="form-control captcha" name="captcha" placeholder="验证码" maxlength="4">
                <img src="<%= UrlManager.createUrl("captcha") %>"
                     data-src="<%= UrlManager.createUrl("captcha") %>">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary" style="width: 100%">登录</button>
            </div>
        </form>
    </div>
</div>
<input type="hidden" id="index-url" value="<%= UrlManager.createUrl("/") %>">