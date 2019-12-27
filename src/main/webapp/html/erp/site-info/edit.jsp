<%@ page import="com.delcache.component.UrlManager" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<form class="form-horizontal col-lg-8 col-sm-8 col-md-6" id="save-form"
      action="<%= UrlManager.createUrl("erp/site-info/edit") %>" method="post">
    <div class="form-group">
        <label class="col-sm-3 control-label">网站标题</label>
        <div class="col-sm-9">
            <input type="text" name="title" class="form-control" value="${data.title}" placeholder="请输入网站标题">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">手机号</label>
        <div class="col-sm-9">
            <input type="text" name="mobile" class="form-control" value="${data.mobile}" placeholder="请输入手机号">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">默认密码</label>
        <div class="col-sm-9">
            <input type="password" name="default_password" class="form-control" value="${data.defaultPassword}"
                   placeholder="请输入默认密码">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">网站HOST</label>
        <div class="col-sm-9">
            <input type="text" name="web_host" class="form-control" value="${data.webHost}" placeholder="请输入网站HOST">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">网站IP地址</label>
        <div class="col-sm-9">
            <input type="text" name="web_ip" class="form-control" value="${data.webIp}" placeholder="请输入网站IP地址">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">微信APPID</label>
        <div class="col-sm-9">
            <input type="text" name="wechat_app_id" class="form-control" value="${data.wechatAppId}"
                   placeholder="请输入微信APPID">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">微信密钥 </label>
        <div class="col-sm-9">
            <input type="text" name="wechat_app_secret" class="form-control" value="${data.wechatAppSecret}"
                   placeholder="请输入微信密钥	">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">微信支付商户ID</label>
        <div class="col-sm-9">
            <input type="text" name="wechat_mch_id" class="form-control" value="${data.wechatMchId}"
                   placeholder="请输入微信支付商户ID">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">微信支付商户密钥</label>
        <div class="col-sm-9">
            <input type="text" name="wechat_pay_key" class="form-control" value="${data.wechatPayKey}"
                   placeholder="请输入微信支付商户密钥">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-success">保存</button>
        </div>
    </div>
</form>
<custom_script>
    <script src="/static/js/erp/site-info.js"></script>
</custom_script>