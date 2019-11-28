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
    <link href="/static/css/login.css" rel="stylesheet">
    <sitemesh:write property='custom_css'/>
</head>
<body>
<sitemesh:write property='body'/>
</body>
<script src="/static/js/jquery.js"></script>
<script src="/static/js/jquery.validate.js"></script>
<script src="/static/plugin/bootstrap/js/bootstrap.js"></script>
<script src="/static/js/popup.js"></script>
<script src="/static/js/main.js"></script>
<script src="/static/js/login.js"></script>
<sitemesh:write property='custom_script'/>
</html>