<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.Admin" %>
<%@ page import="com.delcache.common.entity.SiteInfo" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="btn-box clearfix">
    <a href="<%= UrlManager.createUrl("system/admin/edit") %>">
        <div class="btn btn-success pull-right"><i class="glyphicon glyphicon-plus"></i> 创建</div>
    </a>
</div>
<form class="search-form" action="<%= UrlManager.createUrl("system/admin/index") %>" method="get">
    <% Map<String, String> params = (Map<String, String>) request.getAttribute("params");%>
    <div class="form-content">
        <span class="control-label search-label">用户状态</span><%
        Map<String, String> statusList = (Map<String, String>) request.getAttribute("statusList");
        for (Map.Entry<String, String> entry : statusList.entrySet()) {
    %>
        <select name="status" id="status" class="form-control search-input">
            <option value="">请选择</option>

            <option value="<%= entry.getKey() %>" <%= entry.getKey().equals(params.get("status")) ? "selected" : "" %>>
                <%= entry.getValue() %>
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
            <th>用户ID</th>
            <th>用户名称</th>
            <th>真实名称</th>
            <th>邮箱</th>
            <th>手机号</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            Admin admin = (Admin) request.getSession().getAttribute("user");
            SiteInfo siteInfo = (SiteInfo) request.getAttribute("siteInfo");
            List<Admin> list = (List<Admin>) request.getAttribute("list");
            for (Admin v : list) {
        %>
        <tr>
            <td><%= v.getAdminId() %>
            </td>
            <td><%= v.getUsername() %>
            </td>
            <td><%= v.getRealname() %>
            </td>
            <td><%= v.getEmail() %>
            </td>
            <td><%= v.getMobile() %>
            </td>
            <td><%= statusList.get(String.valueOf(v.getStatus()))%>
            </td>
            </td>
            <td>
                <a href="<%= UrlManager.createUrl("system/admin/edit?admin_id="+ String.valueOf(v.getAdminId())) %>">
                    <div class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-pencil"></i> 编辑</div>
                </a>
                <% if (v.getStatus() == 1) {%>
                <div class="btn btn-danger btn-sm set-status-btn" data-id="<%= v.getAdminId() %>" data-status="0"
                     data-url="<%= UrlManager.createUrl("/system/admin/set-status") %>">
                    <i class="glyphicon glyphicon-ban-circle"></i> 禁用
                </div>
                <% } else { %>
                <div class="btn btn-success btn-sm set-status-btn" data-id="<%= v.getAdminId() %>" data-status="1"
                     data-url="<%= UrlManager.createUrl("/system/admin/set-status") %>">
                    <i class="glyphicon glyphicon-ok-circle"></i> 启用
                </div>
                <% }%>
                <% if (v.getIdentity() == 0 && admin.getAdminId() != v.getAdminId()) {%>
                <div class="btn btn-outline-danger btn-sm reset-password-btn" data-id="<%= v.getAdminId() %>"
                     data-url="<%= UrlManager.createUrl("/system/admin/reset-password") %>"
                     data-default="<%= siteInfo.getDefaultPassword() %>"><i
                        class="glyphicon glyphicon-repeat"></i>
                    重置密码
                </div>
                <% } %>
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
    <script src="/static/js/system/admin.js"></script>
</custom_script>