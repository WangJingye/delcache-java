<%@ page import="com.delcache.extend.UrlManager" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form class="form-horizontal col-lg-8 col-sm-8 col-md-6" id="save-form"
      action="<%= UrlManager.createUrl("system/role/edit") %>" method="post">
    <input type="hidden" name="id" value="${data.id}">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="name">角色名称</label>
        <div class="col-sm-10">
            <input type="text" id="name" name="name" class="form-control" value="${data.name}"
                   placeholder="请输入角色名称">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="remark">描述</label>
        <div class="col-sm-10">
            <textarea name="remark" id="remark" class="form-control" placeholder="请输入描述">${data.remark}</textarea>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success">保存</button>
        </div>
    </div>
</form>
<input type="hidden" id="index-url" value="<%=UrlManager.createUrl("system/role/index")%>">
<custom_script>
    <script src="/static/js/system/role.js"></script>
</custom_script>