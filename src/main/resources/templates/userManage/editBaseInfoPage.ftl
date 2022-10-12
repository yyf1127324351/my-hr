<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>基本信息</title>
    <#include "/common/common.ftl"/>
    <script type="text/javascript" src="/static/js/commonPlugin.js?v=${.now?string('hhmmSSsss')}"></script>
    <style type="text/css">
        #user-form table { width:100%; border: 1px solid #DDDDDD; border-collapse: collapse; }
        #user-form table td { border: 1px solid #3498db; padding: 2px 5px; line-height: 1.8; }
        #user-form table td p { margin: 0; }
        /*#user-form table td input { border-style: solid; border-width: 1px; }*/
        span.important { color: red!important; }
        span.important:after { content: "*"; display: inline-block; }
        body { padding-bottom: 40px; }
        .validStyle {
            border-color: #ffa8a8;
            background-color: #fff3f3;
            color: #000;
        }
    </style>
</head>
<body>
<form id="user-form">
    <input type="hidden" name="id" id="id" value="${id}" />
    <input type="hidden" name="inductionTeacherId" id="inductionTeacherId" value="${userInfo.inductionTeacherId}"/>
    <input type="hidden" name="reportLeaderId" id="reportLeaderId" value="${userInfo.reportLeaderId}"/>
    <div class="easyui-panel" title="个人信息">
        <table class="table table-bordered  table-hover table-condensed">
            <tr>
                <td width="25%">花名</td>
                <td width="25%">工号</td>
                <td width="25%">公司邮箱</td>
                <td width="25%">公司邮箱</td>
            </tr>
            <tr>
                <td>${userInfo.userName!''}</td>
                <td>${userInfo.userNo!''}</td>
                <td>${userInfo.companyEmail!''}</td>
                <td>${userInfo.companyEmail!''}</td>
            </tr>
        </table>
    </div>

    <div class="easyui-panel" title="个人基本信息">
        <table>
            <tr>
                <td width="10%">中文名：<span style="color:red;">*</span></td>
                <td width="23%">
                    <input class="easyui-textbox" style="width: 90%" id="cnName" name="cnName" value="${userInfo.cnName!''}" />
                </td>
                <td width="10%">性别：</td>
                <td width="23%">
                    <input name="sex" class="easyui-combobox" style="width: 90%"
                           data-options="valueField:'id',required:true,panelHeight: '50',editable:false,textField:'description',data:sexJson,
        						onLoadSuccess: function () {
                      				<#if userInfo.sex??>
                      					$(this).combobox('select', ${userInfo.sex!''});
                      				</#if>
                      			}"
                    />
                </td>
                <td width="10%">出生日期：</td>
                <td width="23%"></td>
            </tr>
            <tr>
                <td>年龄：</td>
                <td class="readonly_style">
                    <input class="easyui-textbox" style="width: 90%" name="age" readonly = "readonly" value="${userInfo.age!''}" />
                </td>
                <td >国籍：</td>
                <td>
                </td>
                <td>籍贯：</td>
                <td></td>
            </tr>
        </table>
    </div>

    <div class="easyui-panel" title="员工相关">
        <table>
            <tr>
                <td width="10%">入职导师：<span style="color:red;">*</span></td>
                <td width="23%">
                    <input class="easyui-validatebox validStyle"  readonly="readonly" required="true" type="text" name="inductionTeacherName" id="inductionTeacherName"
                           value="${userInfo.inductionTeacherName!''}" style="width:86%;" />
                </td>
                <td width="10%">汇报对象：<span style="color:red;">*</span></td>
                <td width="23%">
                    <input class="easyui-validatebox validStyle"  readonly="readonly" required="true" type="text" name="reportLeaderName" id="reportLeaderName"
                           value="${userInfo.reportLeaderName!''}" style="width:86%;"/>
                </td>
                <td width="10%">直属上级：</td>
                <td width="23%"></td>
            </tr>
        </table>
    </div>
    <a class="easyui-linkbutton" id="submit-btn" data-options="iconCls:'icon-ok'" onclick="updateBaseInfo()" style="width:100px">提交</a>

</form>
</body>


<script type="text/javascript">
    $(document).ready(function () {
        $("#inductionTeacherName").userbox({
            valueSelector: '#inductionTeacherId',
        });

        $("#reportLeaderName").userbox({
            valueSelector: '#reportLeaderId',
            multiple: true
        });
    });
    
    
    function updateBaseInfo() {

        var id = $("#id").val();

        var cnName = $("#cnName").val()
        var inductionTeacherName = $("#inductionTeacherName").val();
        var reportLeaderName = $("#reportLeaderName").val();
        var inductionTeacherId = $("#inductionTeacherId").val();
        var reportLeaderId = $("#reportLeaderId").val();

        if (!cnName) {
            layer.alert("中文名不能为空", {icon: 5, title: "提示"});
            return;
        }
        if(!inductionTeacherName) {
            layer.alert("入职导师不能为空", {icon: 5, title: "提示"});
            return;
        }
        if(!reportLeaderName) {
            layer.alert("汇报对象不能为空", {icon: 5, title: "提示"});
            return;
        }

        var paramData = {};
        paramData.id = id;
        paramData.cnName = cnName;
        paramData.inductionTeacherName = inductionTeacherName;
        paramData.reportLeaderName = reportLeaderName;
        paramData.inductionTeacherId = inductionTeacherId;
        paramData.reportLeaderId = reportLeaderId;



        $.ajax({
            type : "POST",
            url : "/userInfo/updateUserInfo",
            dataType: "json",
            data : paramData,
            success : function(data) {
                if(data.code == 200){
                    //保存完成之后跳到列表
                    layer.alert(data.message, {icon: 1, title: "提示"},function () {
                        location.reload();
                    });
                }else{
                    //错误提示
                    layer.alert(data.message, {icon: 5,title: "提示"});
                }
            },
            error :function(){
                layer.alert('updateUserInfoError', {icon: 5,title: "提示"});
            }
        });
        
    }
    
</script>


</html>