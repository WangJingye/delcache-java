<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.delcache.extend.Util" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.Menu" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="btn-box clearfix">
    <a href="<%= UrlManager.createUrl("system/menu/edit") %>">
        <div class="btn btn-success pull-right"><i class="glyphicon glyphicon-plus"></i> 创建</div>
    </a>
</div>
<form class="search-form" action="<%= UrlManager.createUrl("system/menu/index") %>" method="get">
    <% Map<String, String> params = (Map<String, String>) request.getAttribute("params");%>
    <div class="form-content">
        <span class="control-label search-label">菜单状态</span>
        <select name="status" id="status" class="form-control search-input">
            <option value="">请选择</option>
            <%
                Map<String, String> statusList = (Map<String, String>) request.getAttribute("statusList");
                for (Map.Entry<String, String> entry : statusList.entrySet()) {
            %>
            <option value="<%= entry.getKey() %>"<%= entry.getKey().equals(params.get("status"))?"selected":"" %>
            ><%=entry.getValue()%>
                    <% }%>
        </select>
    </div>
    <div class="form-content">
        <%
            Map<String, String> searchMap = new HashMap<String, String>(){
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
            <th>菜单名称</th>
            <th>菜单链接</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% List<Menu> list = (List<Menu>) request.getAttribute("list");
            for (Menu v : list) {
        %>
        <tr>
            <td><%= v.getId()%>
            </td>
            <td><%= v.getName()%>
            </td>
            <td><%= v.getUrl()%>
            </td>
            <td><%= statusList.get(String.valueOf(v.getStatus()))%>
            </td>
            <td><%= Util.stampToDate(v.getCreateTime())%>
            </td>
            <td>
                <a href="<%= UrlManager.createUrl("system/menu/edit?id="+ String.valueOf(v.getId())) %>">
                    <div class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-pencil"></i> 编辑</div>
                </a>
                <% if (v.getStatus() == 1) {%>
                <div class="btn btn-danger btn-sm set-status-btn" data-id="<%= v.getId() %>" data-status="0"
                     data-url="<%= UrlManager.createUrl("/system/menu/set-status") %>">
                    <i class="glyphicon glyphicon-ban-circle"></i> 禁用
                </div>
                <% } else { %>
                <div class="btn btn-success btn-sm set-status-btn" data-id="<%= v.getId() %>" data-status="1"
                     data-url="<%= UrlManager.createUrl("/system/menu/set-status") %>">
                    <i class="glyphicon glyphicon-ok-circle"></i> 启用
                </div>
                <% }%>
            </td>
        </tr>
        <%}%>
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
    <script src="/static/js/system/menu.js"></script>
</custom_script>