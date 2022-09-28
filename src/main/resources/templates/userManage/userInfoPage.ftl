<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>员工信息管理</title>
    <#include "/common/common.ftl"/>
    <script type="text/javascript" src="/static/js/common.js?v=${.now?string('hhmmSSsss')}"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'north'" class="panel-fit">
    <form id="search_form" >
        <div style="padding: 4px 5px;">
            <div class="search-row">
                <div class="form-group">
                    <label class="search-label">工号:</label>
                    <input name="userId" class="easyui-textbox" type="text" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label class="search-label">花名:</label>
                    <input name="userName" class="easyui-textbox" type="text" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label class="search-label">账号:</label>
                    <input name="loginName" class="easyui-textbox" type="text" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label class="search-label">展示列模板:</label>
                    <input id="template" class="easyui-combobox" style="width: 120px;">
                </div>


                <div class="form-group">
                    <a class="easyui-linkbutton" style="height: 22px" data-options="iconCls:'icon-search'" onclick="queryList()" >搜索</a>
                    <a class="easyui-linkbutton" style="height: 22px" data-options="iconCls:'icon-clear'"  onclick="javascript:clearQuery()" >重置</a>
                </div>
            </div>
        </div>
    </form>
</div>


<div data-options="region:'center'" >
    <div id="button_tab" style="">
        <table class="button_table">
            <tr>
                <td style="height: 20px;">
                    <a class="easyui-linkbutton toolButton" data-options="iconCls:'fa fa-plus'" onclick="addInfo()">新增</a>
                </td>

                <td style="height: 20px;">
                    <a class="easyui-linkbutton toolButton" data-options="iconCls:'fa fa-pencil-square-o'" onclick="showColumnWindow(1,'template')">设置列模版</a>
                </td>
            </tr>

        </table>
    </div>
    <table id="data_table" style="height: 100%;"></table>
</div>
<#--设置列模板div-->
<div id="columnWindow"></div>


<div style="display:none">
    <div id="addEditDialog" class="dialog">
        <form>
            <input type="hidden" name="id" id="id" />
            <input type="hidden" name="inductionTeacherId" id="inductionTeacherId"/>
            <input type="hidden" name="reportLeaderId" id="reportLeaderId"/>
            <table style="width:95%;margin:10px 10px 10px 10px;">
                <tbody>
                <tr>
                    <th><span style="color:red;">*</span>入职导师：</th>
                    <td>
                        <input type="text" name="inductionTeacherName" id="inductionTeacherName" style="width:350px;" />
                    </td>
                </tr>
                <tr>
                    <th><span style="color:red;">*</span>汇报对象：</th>
                    <td>
                        <input type="text" name="reportLeaderName" id="reportLeaderName" style="width:350px;"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>


</body>

<script type="text/javascript">

    $(document).ready(function () {

        document.onkeydown = function (e) {
            if (e.keyCode == '13') {
                queryList();
            }
        };

        $("#inductionTeacherName").userbox({
            valueSelector: '#inductionTeacherId',
        });

        $("#reportLeaderName").userbox({
            valueSelector: '#reportLeaderId',
            multiple: true
        });

        $("#template").combobox({
            url : '/columnField/queryColumnFieldTemplateUser?fieldType=1',
            valueField : 'id',
            textField : 'templateName',
            multiple: false,
            editable:false,
            onLoadSuccess : function(){
                var data = $(this).combobox("getData");
                if (data && data.length > 0) {
                    $(this).combobox("setValue", data[0].id);
                }
                //重新加载页面展示列
                loadColumn(data[0].id);
            },
            onSelect: function (node) {
                //重新加载页面展示列
                loadColumn(node.id);
            }
            // onShowPanel:function(){
            //     $(this).combobox('reload','/columnField/queryColumnFieldTemplateUser?fieldType=1');
            // }

        });


        //动态展示列
        var columns;
        $("#data_table").datagrid({
            url: '/userInfo/getUserInfoPageList',
            method: 'post',
            loadMsg: "数据装载中,请稍等....",
            nowrap: true, //单元格内容是否可换行
            fitColumns: true, //自适应网格宽度
            showFooter: false, //是否显示最后一行，统计使用
            pagination: true,
            singleSelect: true,
            rownumbers: true,
            pageSize: 50,
            pageList: [50, 100, 200],
            toolbar: '#button_tab',
            onBeforeLoad: function (param) {
                //设置参数
                // param.id=id;
            },
            onLoadSuccess: function (data) {
            },
            frozenColumns: [[
                {title: '操作', field: 'a', width: 80, align: 'center',fixed: false,
                    formatter: function (val, row) {
                        var html = '<a class="sel_btn ch_cls" href="javascript:edit()" style="text-decoration:none;">编辑</a>';
                        return html;
                    }
                },
                {title: '工号', field: 'id', width: 100, align: 'center'},
                {title: '花名', field: 'userName', width: 150, align: 'center'},
                {title: '账号', field: 'loginName', width: 150, align: 'center'}
            ]],
            columns:columns

        });


        $("#addEditDialog").dialog({
            resizable : false,
            width:'500',
            height:'160',
            // width:'38%',
            // height:'32%',
            // top: '10%',
            left: '30%',
            // iconCls:'icon-edit',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    saveOrUpdate();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addEditDialog').dialog('close');
                }
            }],
            onClose:function(){
                $("#addEditDialog form").form("reset");
            },
            closable: true,
            closed: true   //已关闭
        });

    });

    function clearQuery() {
        $('#search_form').form('clear');
        $("#template").combobox('reload','/columnField/queryColumnFieldTemplateUser?fieldType=1');
        queryList();
    }

    function queryList() {
        var data = getFormData("search_form");
        $('#data_table').datagrid({url: '/userInfo/getUserInfoPageList', queryParams: data});
    }

    //编辑
    function edit() {

        var row = $("#data_table").datagrid('getSelected');

        $('#addEditDialog').dialog('setTitle','编辑');
        $('#addEditDialog').dialog('open');
        $("#id").val(row.id);
        $("#inductionTeacherId").val(row.inductionTeacherId);
        $("#reportLeaderId").val(row.reportLeaderId);
        $("#inductionTeacherName").val(row.inductionTeacherName);
        $("#reportLeaderName").val(row.reportLeaderName);
    }


    function saveOrUpdate() {
        var id = $("#id").val();
        var inductionTeacherName = $("#inductionTeacherName").val();
        var reportLeaderName = $("#reportLeaderName").val();
        var inductionTeacherId = $("#inductionTeacherId").val();
        var reportLeaderId = $("#reportLeaderId").val();
        if(!inductionTeacherName) {
            layer.alert("入职导师不能为空", {icon: 5, title: "提示"});
            return;
        }else if(!reportLeaderName) {
            layer.alert("汇报对象不能为空", {icon: 5, title: "提示"});
            return;
        }

        $.ajax({
            type : "POST",
            url : "/userInfo/saveOrUpdateUserInfo",
            dataType: "json",
            data : $("#addEditDialog form").serializeArray(),
            success : function(data) {
                if(data.code == 200){
                    //保存完成之后跳到列表
                    layer.alert(data.message, {icon: 1, title: "提示"});
                    $('#addEditDialog').dialog('close');
                    queryList();
                }else{
                    //错误提示
                    layer.alert(data.message, {icon: 5,title: "提示"});
                }
            },
            error :function(){
                layer.alert('saveOrUpdateUserInfoError', {icon: 5,title: "提示"});
            }
        });
    }



    function handleColumnField(column,item){
        column.push({ "field": item.field, "title": item.name, "width": 100, "align": 'center'});
    }
    function loadColumn(templateId){
        var columns;
        $.ajax({
            url: '/columnField/queryUserColumnField',
            data:{'fieldTemplateUserId':templateId},
            type: 'post',
            dataType: "json",
            async: false,
            success: function (result) {//异步获取要动态生成的列 别名，宽度也可以
                var column = new Array();

                //处理后台查出的需要展示的列
                $.each(result, function (i, item) {
                    handleColumnField(column,item);
                });
                columns=  new Array(column);
            }

        });
        $("#data_table").datagrid({columns:columns});
    }



    function getFormData(form) {
        var array = $("#" + form).serializeArray();
        var data = {};
        $.each(array, function () {
            var item = this;
            if (data[item["name"]]) {
                data[item["name"]] = data[item["name"]] + "," + item["value"];
            } else {
                data[item["name"]] = item["value"];
            }
        });
        return data;
    }

</script>
</html>