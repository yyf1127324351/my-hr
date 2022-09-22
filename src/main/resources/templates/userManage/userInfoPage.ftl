<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>员工信息管理</title>
    <#include "/common/common.ftl"/>
    <script type="text/javascript" src="/static/common/sys/common.js?v=${.now?string('hhmmSSsss')}"></script>
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


</body>

<script type="text/javascript">

    $(document).ready(function () {

        document.onkeydown = function (e) {
            if (e.keyCode == '13') {
                queryList();
            }
        };

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
                        var html = '<a class="sel_btn ch_cls" href="javascript:editUserRole()" style="text-decoration:none;">编辑</a>';
                        return html;
                    }
                },
                {title: '工号', field: 'id', width: 100, align: 'center'},
                {title: '花名', field: 'userName', width: 150, align: 'center'},
                {title: '账号', field: 'loginName', width: 150, align: 'center'}
            ]],
            columns:columns

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