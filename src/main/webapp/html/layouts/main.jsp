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
<div class="wrap" style="padding: 0;margin: 0">
    <div class="view-content clearfix">
        <nav id="w0" class="navbar-inverse navbar-fixed-top navbar"
             style="margin: 0;position: -webkit-sticky; position:sticky;border-bottom:0">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#w0-collapse"><span
                            class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span></button>
                    <a class="navbar-brand" href="/">新昌运输系统</a></div>
                <div id="w0-collapse" class="collapse navbar-collapse">
                    <ul id="w1" class="navbar-nav navbar-left nav">
                        <li><a href="/index.php?r=system%2Fmenu%2Findex">系统管理</a></li>
                        <li class="active"><a href="/index.php?r=erp%2Fcustomer%2Findex">业务管理</a></li>
                    </ul>
                    <ul id="w2" class="navbar-nav navbar-right nav">
                        <li class="dropdown"><a class="nav-link dropdown-toggle" href="javascript:void(0)" role="button"
                                                data-toggle="dropdown"><span>系统管理员</span></a>
                            <ul class="dropdown-menu">
                                <li><a href="/index.php?r=system%2Fuser%2Fprofile">个人信息</a></li>
                                <li><a href="/index.php?r=site%2Flogout">登出</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="view-content clearfix">
            <div class="col-12 col-md-2 col-sm-2 bd-sidebar" style="padding: 0;overflow-y: auto">
                <ul class="list-group">
                    <li class="list-group-item main-item active">
                        <div><i class="glyphicon glyphicon-user"></i> 用户管理</div>
                    </li>
                    <li class="sub-item collapse in">
                        <ul class="list-sub-item">
                            <li class="list-group-item active" data-url="/index.php?r=erp%2Fcustomer%2Findex">会员列表</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fcustomer%2Fdriver">司机列表</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fcustomer%2Fstation">站点用户</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="iconfont icon-bus"></i> 车辆管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fbus%2Findex">公交车辆</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fmotor%2Findex">机动车辆</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="iconfont icon-siji-"></i> 司机管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fdriver%2Fbus">公交司机</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fdriver%2Ftransport">客运司机</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="iconfont icon-zhandianditu"></i> 站点管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fservice-station%2Findex">服务站点</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fbus-station%2Findex">公交站点</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fbus-line%2Findex">公交线路</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstation-allocation%2Findex">站点配置
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-shopping-cart"></i> 订单管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fpackage-order%2Findex">包裹数据</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fbus-order%2Findex">客运订单</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Forder%2Findex">包裹订单</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fpackage-list%2Findex">包裹条码列表</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fpackage-type%2Findex">包裹物品类型</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-credit-card"></i> 认证管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fverify%2Fcustomer">会员认证</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fverify%2Fstation">站点认证</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-list-alt"></i> 结算管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fdriver-bill%2Findex">司机结算</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstation-bill%2Findex">站点结算</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-signal"></i> 数据管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstatistics%2Fcar">车辆数据</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstatistics%2Fstation">站点数据</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstatistics%2Forder">订单数据</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fstatistics%2Fbus-line">线路数据</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-wrench"></i> 报修管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fbus-repair%2Findex">车辆报修</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fservice-station-repair%2Findex">
                                站点报修
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-book"></i> 意见管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fsuggestion%2Findex">建议列表</li>
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fcomplaint%2Findex">投诉列表</li>
                        </ul>
                    </li>
                    <li class="list-group-item main-item ">
                        <div><i class="glyphicon glyphicon-envelope"></i> 消息管理</div>
                    </li>
                    <li class="sub-item collapse ">
                        <ul class="list-sub-item">
                            <li class="list-group-item " data-url="/index.php?r=erp%2Fsystem-msg%2Findex">系统消息</li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div class="col-12 col-md-10 col-xl-10 bd-content">
                <h3>${title}</h3>
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

       

       

       

       

       
