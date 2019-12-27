<%@ page import="com.delcache.component.UrlManager" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<% String url = request.getAttribute("url").toString();%>
<meta name="decorator" content="/html/layouts/empty.jsp">
<div id="exception_contain">
    <div id="showInfo">
        <h1>Oops!</h1>
        <p>请求失败!</p>
        <p style="color: #FF6666;">
            ${message}
        </p>
        <p id="page-jump">页面将在<span id="time_span">5</span>秒后跳转到<a id="jump_url"
                                                                   href="<%= UrlManager.createUrl(url)%>">主页!</a>
        </p>
    </div>
    <div id="img">
        <img src="/static/image/error_page.gif">
    </div>
</div>

<style>
    * {
        padding: 0;
        margin: 0;
    }

    body {
        background-color: #f5f5f5;
    }

    #exception_contain {
        width: 550px;
        margin: 150px auto;
    }

    #showInfo {
        width: 330px;
        float: left;
        display: inline-block;
    }

    #showInfo h1 {
        font-family: Circular, "Helvetica Neue", Helvetica, Arial, sans-serif;
        margin-top: 40px;
        color: #565a5c;
        font-size: 3em;
    }

    #showInfo p {
        margin: 20px auto auto 10px;
        color: #565a5c;
        line-height: 20px;
    }

    #showInfo a {
        text-decoration: none;
        color: #555;
    }

    #showInfo a:hover {
        text-decoration: underline;
    }

    #page-jump, #page-jump a {
        line-height: 40px;
        font-size: 0.8em;
    }

    #img {
        display: inline-block;
        float: right;
    }

    #img img {
        max-width: 220px;
    }
</style>
<custom_script>
<script>
    window.onload = function () {
        var jump_url = document.getElementById('jump_url').href;
        var timer = setInterval(function () {
            var time_span = document.getElementById('time_span');
            var time_num = time_span.innerHTML;
            time_span.innerHTML = time_num - 1;
        }, 1000);

        setTimeout(function () {
            clearInterval(timer);
            window.location = jump_url;
        }, 5000);
    }
</script>
</custom_script>