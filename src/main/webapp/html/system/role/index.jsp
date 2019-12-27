<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.Role" %>
<%@ page import="com.delcache.component.Util" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="btn-box clearfix">
    <a href="<%= UrlManager.createUrl("system/role/edit") %>">
        <div class="btn btn-success pull-right"><i class="glyphicon glyphicon-plus"></i> 创建</div>
    </a>
</div>
<form class="search-form" action="<%= UrlManager.createUrl("system/role/index") %>" method="get">
    <% Map<String, String> params = (Map<String, String>) request.getAttribute("params");%>
    <div class="form-content">
        <span class="control-label search-label">用户状态</span>
        <select name="status" id="status" class="form-control search-input">
            <option value="">请选择</option>
            <%
                Map<String, String> statusList = (Map<String, String>) request.getAttribute("statusList");
                for (Map.Entry<String, String> entry : statusList.entrySet()) {
            %>
            <option value="<%= entry.getKey() %>" <%= entry.getKey().equals(params.get("status")) ? "selected" : "" %>>
                <%=entry.getValue()%>
            </option>
            <% }%>
        </select>
    </div>
    <div class="form-content">
        <%
            Map<String, String> searchMap = new HashMap<String, String>() {
                {
                    put("id", "ID");
                    put("name", "菜单名称");
                    put("url", "URL");
                }
            };
        %>
        <span class="control-label search-label">查询条件</span>
        <div class="clearfix" style="display: inline-flex;">
            <select class="form-control search-type" name="search_type">
                <option value="">请选择</option>
                <% for (Map.Entry<String, String> entry : searchMap.entrySet()) {%>
                <option value="<%=entry.getKey()%>"<%=entry.getKey().equals(params.get("search_type")) ? "selected" : ""%>
                ><%=entry.getValue()%>
                </option>
                <% }%>
            </select>
            <input type="text" class="form-control search-value" name="search_value" placeholder="关键词"
                   value="<%= params.get("search_value") == null ? "" : params.get("search_value") %>">
            <div class="btn btn-primary search-btn text-nowrap"><i class="glyphicon glyphicon-search"></i> 搜索</div>
        </div>
    </div>
</form>
<div class="table-responsive">
    <table class="table table-striped table-bordered list-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>角色名称</th>
            <th>描述</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Role> list = (List<Role>) request.getAttribute("list");
            for (Role v : list) {
        %>
        <tr>
            <td><%= v.getId() %>
            </td>
            <td><%= v.getName() %>
            </td>
            <td><%= v.getRemark() %>
            </td>
            <td><%= statusList.get(String.valueOf(v.getStatus()))%>
            </td>
            <td><%= Util.stampToDate(v.getCreateTime()) %>
            </td>
            <td>
                <a href="<%= UrlManager.createUrl("system/role/edit?id="+ String.valueOf(v.getId())) %>">
                    <div class="btn btn-outline-primary btn-sm"><i class="glyphicon glyphicon-pencil"></i> 编辑</div>
                </a>
                <% if (v.getStatus() == 1) {%>
                <a href="<%= UrlManager.createUrl("system/role/set-role-menu?id="+ v.getId()) %>">
                    <div class="btn btn-outline-primary btn-sm"><i class="glyphicon glyphicon-align-justify"></i>
                        设置角色权限
                    </div>
                </a>
                <a href="<%= UrlManager.createUrl("system/role/set-role-admin?id="+v.getId()) %>">
                    <div class="btn btn-outline-primary btn-sm"><i class="glyphicon glyphicon-user"></i> 设置角色用户
                    </div>
                </a>
                <div class="btn btn-danger btn-sm set-status-btn" data-id="<%= v.getId() %>" data-status="0"
                     data-url="<%= UrlManager.createUrl("/system/role/set-status") %>">
                    <i class="glyphicon glyphicon-ban-circle"></i> 禁用
                </div>
                <% } else { %>
                <div class="btn btn-success btn-sm set-status-btn" data-id="<%= v.getId() %>" data-status="1"
                     data-url="<%= UrlManager.createUrl("/system/role/set-status") %>">
                    <i class="glyphicon glyphicon-ok-circle"></i> 启用
                </div>
                <% }%>
            </td>
        </tr>
        <% }%>
        <% if (list.size() == 0) {%>
        <tr>
            <td colspan="18" class="list-table-nodata">暂无相关数据</td>
        </tr>
        <% }%>
        </tbody>
    </table>
</div>
<%@ include file="/html/layouts/pagination.jsp" %>
<custom_script>
    <script src="/static/js/system/role.js"></script>
</custom_script>