<%@ page import="java.util.List" %>
<%@ page import="com.delcache.common.entity.Menu" %>
<%@ page import="com.delcache.extend.Util" %>
<%@ page import="org.springframework.util.StringUtils" %>
<%@ page import="com.delcache.extend.UrlManager" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <link href="/static/plugin/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap4.css" rel="stylesheet">
    <link href="/static/css/select2.min.css" rel="stylesheet">
    <link href="/static/css/ztree.css" rel="stylesheet">
    <link href="/static/css/main.css" rel="stylesheet">
    <sitemesh:write property='custom_css'/>
</head>
<body>
<div class="wrap" style="padding: 0;margin: 0">
    <div class="view-content clearfix">
        <nav class="navbar-inverse navbar-fixed-top navbar"
             style="margin: 0;position: -webkit-sticky; position:sticky;border-bottom:0">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse"
                            data-target="#main-nav-collapse"><span
                            class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span></button>
                    <a class="navbar-brand" href="/">后台管理系统</a></div>
                <div id="main-nav-collapse" class="collapse navbar-collapse">
                    <ul class="navbar-nav navbar-left nav">
                        <%
                            List<Menu> menus = (List<Menu>) request.getAttribute("menus");
                            List<Menu> activeMenu = (List<Menu>) request.getAttribute("activeMenu");
                            List<String> activeMenuIds = Util.arrayColumn(activeMenu, "id");
                            for (Menu menu : menus) {
                                List<Menu> firstList = (List<Menu>) menu.extra;
                                if (firstList.size() > 0) {
                        %>
                        <li class="<%= activeMenuIds.contains(String.valueOf(menu.getId()))?"active":""%>">
                            <a <% if (!menu.getUrl().isEmpty()) {%>
                                    href="<%= UrlManager.createUrl(menu.getUrl()) %>"<%}%>>
                                <%= menu.getName() %>
                            </a>
                        </li>
                        <% }%>
                        <% }%>
                    </ul>
                    <ul class="navbar-nav navbar-right nav">
                        <li class="dropdown"><a class="nav-link dropdown-toggle" href="javascript:void(0)" role="button"
                                                data-toggle="dropdown"><span>${user.realname}</span></a>
                            <ul class="dropdown-menu">
                                <li><a href="<%= UrlManager.createUrl("system/admin/profile") %>">个人信息</a></li>
                                <li><a href="<%= UrlManager.createUrl("logout") %>">登出</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="view-content clearfix">
            <div class="col-lg-2 col-md-2 col-sm-2 col-xl-12 bd-sidebar" style="padding: 0;overflow-y: auto">
                <ul class="list-group">
                    <%
                        for (Menu menu : menus) {
                            if (!activeMenuIds.contains(String.valueOf(menu.getId()))) {
                                continue;
                            }
                            List<Menu> firstList = (List<Menu>) menu.extra;
                            for (Menu first : firstList) {
                                List<Menu> secondList = (List<Menu>) first.extra;
                    %>
                    <li class="list-group-item main-item <%= activeMenuIds.contains(String.valueOf(first.getId()))?"active":""%>">
                        <div><i class="glyphicon glyphicon-user"></i><%=first.getName()%>
                        </div>
                    </li>
                    <% if (secondList.size() > 0) {%>
                    <li class="sub-item collapse <%= activeMenuIds.contains(String.valueOf(first.getId()))?"in":""%>">
                        <ul class="list-sub-item">
                            <% for (Menu second : secondList) {%>
                            <li class="list-group-item <%= activeMenuIds.contains(String.valueOf(second.getId()))?"active":""%>"
                                data-url="<%=UrlManager.createUrl(second.getUrl())%>"><%=second.getName()%>
                            </li>
                            <%}%>
                        </ul>
                    </li>
                    <% }
                    }
                    }%>
                </ul>
            </div>
            <div class="col-lg-10 col-md-10 col-sm-10 col-xl-12 bd-content">
                <ul class="breadcrumb">
                    <li><a href="/">首页</a></li>
                    <% for (int i = activeMenu.size() - 1; i >= 0; i--) {%>
                    <li class="active"><%=activeMenu.get(i).getName()%>
                    </li>
                    <% }%>
                </ul>
                <%
                    String title = (String) request.getAttribute("title");
                    if (StringUtils.isEmpty(title)) {
                        title = activeMenu.get(0).getName();
                    }
                %>
                <h3><%= title%>
                </h3>
                <hr>
                <sitemesh:write property='body'/>
            </div>
        </div>
    </div>
</div>
</body>
<script src="/static/js/jquery.js"></script>
<script src="/static/js/jquery.validate.js"></script>
<script src="/static/plugin/bootstrap/js/bootstrap.js"></script>
<script src="/static/js/ztree.core.js"></script>
<script src="/static/js/ztree.excheck.js"></script>
<script src="/static/js/select2.min.js"></script>
<script src="/static/js/popup.js"></script>
<script src="/static/js/main.js"></script>
<sitemesh:write property='custom_script'/>
</html>

       

       

       

       

       
