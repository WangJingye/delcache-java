<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.{{model}}" %>{{imports}}
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="btn-box clearfix">
    <a href="<%= UrlManager.createUrl("{{module}}/{{modelRoute}}/edit") %>">
        <div class="btn btn-success pull-right"><i class="glyphicon glyphicon-plus"></i> 创建</div>
    </a>
</div>
<form class="search-form" action="<%= UrlManager.createUrl("{{module}}/{{modelRoute}}/index") %>" method="get">
    <% Map<String, String> params = (Map<String, String>) request.getAttribute("params");%>{{searchPer}}
    <div class="form-content">
        <%
            Map<String, String> searchMap = new HashMap<String, String>(){
                {{{searchParams}}
                }
            };
        %>
        <span class="control-label search-label">查询条件</span>
        <div class="clearfix" style="display: inline-flex;">
            <select class="form-control search-type" name="search_type">
                <option value="">请选择</option>
                <% for (Map.Entry<String, String> entry : searchMap.entrySet()) { %>
                <option value="<%= entry.getKey() %>" <%= entry.getKey().equals(params.get("search_type")) ? "selected" : "" %>>
                    <%= entry.getValue() %>
                </option>
                <% } %>
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
        <tr>{{table-header}}
        </tr>
        </thead>
        <tbody>
        <% List<{{model}}> list = (List<{{model}}>) request.getAttribute("list");
            for ({{model}} v : list) {
        %>
        <tr>{{table-body}}
            <td>
                <a href="<%= UrlManager.createUrl("{{module}}/{{modelRoute}}/edit?id="+ String.valueOf(v.get{{u_PrimaryKey}}())) %>">
                    <div class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-pencil"></i> 编辑</div>
                </a>
                <div class="btn btn-danger btn-sm remove-btn" data-id="<%= v.get{{u_PrimaryKey}}() %>">
                    <i class="glyphicon glyphicon-trash"></i> 删除
                </div>{{statusIndex}}
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
    <script src="/static/js/{{module}}/{{modelRoute}}.js"></script>
</custom_script>