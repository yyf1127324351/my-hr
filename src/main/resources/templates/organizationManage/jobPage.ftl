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
                    <label class="search-label">岗位ID:</label>
                    <input name="jobId" class="easyui-textbox" type="text" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label class="search-label">岗位名称:</label>
                    <input name="jobName" class="easyui-textbox" type="text" style="width: 120px;">
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
                    <a class="easyui-linkbutton toolButton" data-options="iconCls:'fa fa-plus'" onclick="openAddDialog()">新增</a>
                </td>
                <td style="height: 20px;">
                    <a class="easyui-linkbutton toolButton" data-options="iconCls:'fa fa-plus'" onclick="openEditDialog()">编辑</a>
                </td>
            </tr>
        </table>
    </div>
    <table id="data_table" style="height: 100%;"></table>
</div>



</body>

<script type="text/javascript">
    $(document).ready(function () {
        document.onkeydown = function (e) {
            if (e.keyCode == '13') {
                queryList();
            }
        };

        $("#data_table").datagrid({
            queryParams: getFormData("searchForm"), //参数
            url: '/userAccount/getUserAccountPageList',
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
            onLoadSuccess: function (data) {
            },
            frozenColumns: [[
                {title: '操作', field: 'id', width: 80, align: 'center',fixed: false,
                    formatter: function (val, row) {
                        var html = '<a class="sel_btn ch_cls" href="javascript:editUserRole()" style="text-decoration:none;">配置角色</a>';
                        return html;
                    }
                },
                {title: '工号', field: 'userId', width: 100, align: 'center'},
                {title: '花名', field: 'userName', width: 150, align: 'center'},
                {title: '账号', field: 'loginName', width: 150, align: 'center'}
            ]],
            columns: [[

                {title: '角色', field: 'userRoleNames', width: 130, align: 'center'}
            ]]


        });

    });
</script>

</html>