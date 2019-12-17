<%@ page import="com.delcache.extend.SelectInput" %>
<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.Menu" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.delcache.extend.UrlManager" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form class="form-horizontal col-lg-8 col-sm-8 col-md-6" id="save-form"
      action="<%= UrlManager.createUrl("system/menu/edit") %>" method="post">
    <%
        List<String> urlList = (List<String>)request.getAttribute("urlList");
        Map<String,String> parentList = (Map<String,String>)request.getAttribute("parentList");
        Menu data=(Menu) request.getAttribute("data");%>
    <input type="hidden" name="id" value="${data.id}">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="parent_id">父级功能</label>
        <div class="col-sm-10">
            <%= SelectInput.show(parentList, data != null ? String.valueOf(data.getParentId()) : "", "parent_id", "select2") %>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="url">链接地址</label>
        <div class="col-sm-10">
            <%= SelectInput.show(urlList, data != null ? data.getUrl() : "", "url", "select") %>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="name">标题</label>
        <div class="col-sm-10">
            <input type="text" id="name" name="name" class="form-control" value="${data.name}"
                   placeholder="请输入标题">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="sort">排序</label>
        <div class="col-sm-10">
            <input type="text" id="sort" name="sort" class="form-control" value="<%= data != null ?
            data.getSort() : "0" %>"
                   placeholder="请输入排序数字">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="icon">图标样式</label>
        <div class="col-sm-10">
            <input type="text" id="icon" name="icon" class="form-control"
                   value="<%= data != null ? data.getIcon() : "glyphicon glyphicon-bookmark" %>" placeholder="请输入图标样式">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="remark">菜单描述</label>
        <div class="col-sm-10">
            <textarea name="remark" id="remark" class="form-control" placeholder="请输入菜单描述">${data.remark}</textarea>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success">保存</button>
        </div>
    </div>
</form>
<input type="hidden" id="index-url" value="<%= UrlManager.createUrl("system/menu/index") %>">
<custom_script>
    <script src="/static/js/system/menu.js"></script>
</custom_script>