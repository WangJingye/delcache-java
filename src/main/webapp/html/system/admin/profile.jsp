<%@ page import="com.delcache.extend.UrlManager" %>
<%@ page import="com.delcache.extend.ImageInput" %>
<%@ page import="com.delcache.common.entity.Admin" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<% Admin user = (Admin) request.getAttribute("user");%>
<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#nav-profile" role="tab" data-toggle="tab">个人信息</a></li>
    <li role="presentation"><a href="#nav-password" role="tab" data-toggle="tab">修改密码</a></li>
</ul>
<div class="tab-content bd-content">
    <div role="tabpanel" class="tab-pane active" id="nav-profile">
        <form id="change-user-info-form" class="form-horizontal col-12 col-sm-8 col-md-6"
              action="<%= UrlManager.createUrl("/system/admin/change-profile") %>" method="post">
            <div class="form-group">
                <label class="col-sm-4 control-label" for="username">用户名</label>
                <div class="col-sm-8">
                    <input type="text" id="username" readonly class="form-control" value="${user.username}"
                           placeholder="请输入用户名">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="realname">真实姓名</label>
                <div class="col-sm-8">
                    <input type="text" id="realname" name="realname" class="form-control" value="${user.realname}"
                           placeholder="请输入真实姓名">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="email">邮箱地址</label>
                <div class="col-sm-8">
                    <input type="email" name="email" id="email" class="form-control" value="${user.email}"
                           placeholder="请输入邮箱">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="mobile">联系电话</label>
                <div class="col-sm-8">
                    <input type="tel" name="mobile" id="mobile" class="form-control" value="${user.mobile}"
                           placeholder="请输入联系电话">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">个人头像</label>
                <div class="col-sm-8">
                    <%= ImageInput.show(user.getAvatar(), "avatar",9)%>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-8">
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>
            </div>
        </form>
    </div>
    <div role="tabpanel" class="tab-pane" id="nav-password">
        <form id="change-password-form" class="form-horizontal col-12 col-sm-8 col-md-6"
              action="<%= UrlManager.createUrl("/system/admin/change-password")%>" method="post">
            <div class="form-group">
                <label class="col-sm-4 control-label" for="password">当前登录密码</label>
                <div class="col-sm-8">
                    <input type="password" id="password" name="password" class="form-control" placeholder="请输入当前登录密码">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="newPassword">新登录密码</label>
                <div class="col-sm-8">
                    <input type="password" name="newPassword" id="newPassword" class="form-control"
                           placeholder="请输入新登录密码">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="rePassword">确认新登录密码</label>
                <div class="col-sm-8">
                    <input type="password" name="rePassword" id="rePassword"  class="form-control" placeholder="请输入确认密码">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-8">
                    <button type="submit" class="btn btn-primary">保存</button>
                </div>
            </div>
        </form>

    </div>
</div>
<custom_script>
    <script src="/static/js/system/admin.js"></script>
</custom_script>