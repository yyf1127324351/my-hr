<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <title>HR系统</title>
    <link rel="icon" type="image/x-ico" href="/static/common/favicon.ico" mce_href="/static/common/favicon.ico"/>
    <script type="text/javascript" src="/static/common/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="/static/common/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/common/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/common/js/layer.min.js"></script>
    <script type="text/javascript" src="/static/common/js/jquery.base64.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/common/easyUI/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/common/css/supper/superBlue.css"/>
    <link rel="stylesheet" type="text/css" href="/static/common/fonts/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="/static/common/fonts/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/common/easyUI/themes/icon.css">

    <script type="text/javascript" src="/static/common/js/jquery-form.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/index.js"></script>
    <style type="text/css">
        #tabs .tabs-panels .panel-body {
            overflow: hidden;
        }

        .set-div {
            height: 30px !important;
        }

        .dialog-button {
            top: 0px !important;
        }
    </style>
</head>
<body id="main" class="easyui-layout">
<div data-options="region:'north',border:false" class="super-north" style="height: 50px;">
    <!--顶部-->
    <div class="super-navigation">
        <div class="super-navigation-title" style="text-align: center;">HR系统</div>
        <div class="super-navigation-main">
            <div class="super-setting-left">
                <ul>
                    <li><i class="fa fa-commenting-o"></i></li>
                    <li><i class="fa fa-envelope-o"></i></li>
                    <li><i class="fa fa-bell-o"></i></li>
                </ul>
            </div>
            <div class="super-setting-right">
                <ul>
                    <li class="user" style="text-align: center">
                        <span style="float: none">${loginName}</span>
                    </li>
                    <li>
                        <div style="height: 50px" class="super-setting-icon">
                            <i class="fa fa-gears" style="padding-top: 40%"></i>
                        </div>
                        <div id="setDiv" class="easyui-menu">
                            <input hidden id="userId" value="${userId}"/>
                            <div onclick="resetPassword();" class="set-div" style="height: 30px;">修改密码</div>
                            <div class="menu-sep"></div>
                            <div onclick="logout();" class="set-div" style="height: 30px;">退出</div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div id="easyui-layout-west" data-options="region:'west',title:'菜单',border:false">
    <!--左侧导航-->
    <div class="easyui-accordion" data-options="border:false,fit:true,selected:true">

        <c:forEach items="${level1List}" var="item">
            <div title="${item.name}" data-options="iconCls:'fa fa-navicon'">

                <c:forEach items="${item.children}" var="item2">
                    <ul class="level2">
                        <li data-url='${item2.url}'>${item2.name}</li>
                    </ul>
                </c:forEach>
            </div>
        </c:forEach>
    </div>

</div>

<div data-options="region:'center'" style="padding-top: 2px;">
    <!--主要内容-->
    <div id="tabs" class="easyui-tabs" data-options="border:false,fit:true">
    </div>
</div>

<div style="display:none">
    <div id="resetPasswordDialog" class="dialog">
        <table style="width:90%;margin:10px 10px 0 20px;">
            <tr style="height:25px;">
                <th><span style="font-size: 10pt;">原密码:<font size="2" color="red">*</font></span></th>
                <td style="align:center;">
                    <input class="easyui-textbox password-input" type="text" required="required" data-options="prompt:'请输入原密码',
                    events:{focus: function(){ easyuitextfocus($(this)); },blur: function(){ easyuitextblur($(this)); }}"
                           id="oldPassword" style="width: 180px;height: 25px;">
                </td>
            </tr>
            <tr style="height:25px;">
                <th><span style="font-size: 10pt;">新密码:<font size="2" color="red">*</font></span></th>
                <td style="align:center;">
                    <input class="easyui-textbox password-input" type="text" required="required" data-options="prompt:'请输入新密码',
                    events:{focus: function(){ easyuitextfocus($(this)); },blur: function(){ easyuitextblur($(this)); }}"
                           id="newPassword" style="width: 180px;height: 25px;">
                </td>
            </tr>
            <tr style="height:25px;">
                <th><span style="font-size: 10pt;">新密码:<font size="2" color="red">*</font></span></th>
                <td style="align:center;">
                    <input class="easyui-textbox password-input" type="text" required="required" data-options="prompt:'请再次输入新密码',
                    events:{focus: function(){ easyuitextfocus($(this)); },blur: function(){ easyuitextblur($(this)); }}"
                           id="newPassword2" style="width: 180px;height: 25px;">
                </td>
            </tr>
        </table>
    </div>
</div>

</body>
</html>

<script type="text/javascript">

</script>