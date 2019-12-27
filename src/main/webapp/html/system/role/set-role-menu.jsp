<%@ page import="com.delcache.component.UrlManager" %>
<%@ page import="com.delcache.component.Util" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    List<Map<String, Object>> menuList = (List<Map<String, Object>>) request.getAttribute("menuList");
%>
<form id="save-role-menu-form" class="form-horizontal col-lg-8 col-sm-8 col-md-6"
      action="<%= UrlManager.createUrl("system/role/set-role-menu")%>" method="post">
    <input type="hidden" name="id" value="${data.id}">
    <div class="form-group">
        <label class="col-sm-2 control-label">角色名称</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" readonly value="${data.name}">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">所选权限</label>
        <div class="col-sm-10">
            <ul id="menuTree" class="ztree"></ul>
            <input type="hidden" name="menu_ids">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-primary" type="submit" value="保存"/>
        </div>
    </div>
</form>
<custom_script>
    <script>
        var menuList = <%= Util.objectToString(menuList) %>;
    </script>
    <script src="/static/js/system/role.js"></script>
</custom_script>