<%@ page import="com.delcache.component.UrlManager" %>
<%@ page import="com.delcache.component.SelectInput" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    Map<String, String> adminList = (Map<String, String>) request.getAttribute("adminList");
    List<String> adminIdList = (List<String>) request.getAttribute("adminIdList");
%>
<form id="save-role-admin-form" class="form-horizontal col-lg-8 col-sm-8 col-md-6"
      action="<%= UrlManager.createUrl("system/role/set-role-admin")%>" method="post">
    <input type="hidden" name="id" value="${data.id}">
    <div class="form-group">
        <label class="col-sm-2 control-label">角色名称</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" readonly value="${data.name}">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">所选用户</label>
        <div class="col-sm-10">
            <%= SelectInput.show(adminList, adminIdList, "admin_id", "select2") %>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-primary" type="submit" value="保存"/>
        </div>
    </div>
</form>
<custom_script>
    <script src="/static/js/system/role.js"></script>
</custom_script>