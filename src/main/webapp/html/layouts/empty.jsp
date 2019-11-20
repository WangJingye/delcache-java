<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <link href="/static/plugin/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/select2.min.css" rel="stylesheet">
    <link href="/static/css/ztree.css" rel="stylesheet">
    <link href="/static/css/main.css" rel="stylesheet">
    <sitemesh:write property='custom_css'/>
</head>
<body>
<sitemesh:write property='body'/>
</body>
<script src="/static/js/jquery.js"></script>
<script src="/static/js/jquery.validate.js"></script>
<script src="/static/plugin/bootstrap/bootstrap.js"></script>
<script src="/static/js/ztree.core.js"></script>
<script src="/static/js/ztree.excheck.js"></script>
<script src="/static/js/select2.min.js"></script>
<sitemesh:write property='custom_script'/>
</html>