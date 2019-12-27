<%@ page import="com.delcache.component.UrlManager" %>
<%@ page import="com.delcache.common.entity.Admin" %>
<%@ page import="com.delcache.component.ImageInput" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form class="form-horizontal col-lg-8 col-sm-8 col-md-6" id="save-form"
      action="<%= UrlManager.createUrl("system/admin/edit") %>" method="post">
    <% Admin data = (Admin) request.getAttribute("data");%>
    <input type="hidden" name="admin_id" value="${data.adminId}">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="username">用户名</label>
        <div class="col-sm-10">
            <input type="text" id="username" name="username" class="form-control" value="${data.username}"
                   placeholder="请输入用户名">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="realname">真实姓名</label>
        <div class="col-sm-10">
            <input type="text" id="realname" name="realname" class="form-control" value="${data.realname}"
                   placeholder="请输入用户名">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="email">邮箱</label>
        <div class="col-sm-10">
            <input type="email" id="email" name="email" class="form-control" value="${data.email}"
                   placeholder="请输入邮箱">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="mobile">手机号</label>
        <div class="col-sm-10">
            <input type="tel" id="mobile" name="mobile" class="form-control" value="${data.mobile}"
                   placeholder="请输入手机号">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="mobile">头像</label>
        <div class="col-sm-10">
            <%= ImageInput.show(data != null ? data.getAvatar() : "", "avatar")%>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success">保存</button>
        </div>
    </div>
</form>
<input type="hidden" id="index-url" value="<%=UrlManager.createUrl("system/admin/index")%>">
<custom_script>
    <script src="/static/js/system/admin.js"></script>
</custom_script>