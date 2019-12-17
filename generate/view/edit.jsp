<%@ page import="com.delcache.extend.UrlManager" %>
<%@ page import="com.delcache.common.entity.{{model}}" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form class="form-horizontal col-lg-8 col-sm-8 col-md-6" id="save-form"
      action="<%= UrlManager.createUrl("{{module}}/{{modelRoute}}/edit") %>" method="post">
    <% {{model}} data = ({{model}}) request.getAttribute("data");%>
    <input type="hidden" name="{{primaryKey}}" value="${data.{{primaryKey}}}">{{inputParams}}
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success">保存</button>
        </div>
    </div>
</form>
<input type="hidden" id="index-url" value="<%= UrlManager.createUrl("{{module}}/{{modelRoute}}/index") %>">
<custom_script>
    <script src="/static/js/{{module}}/{{modelRoute}}.js"></script>
</custom_script>