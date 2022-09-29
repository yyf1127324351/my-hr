<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>员工信息管理</title>
    <#include "/common/common.ftl"/>
    <script type="text/javascript" src="/static/js/common.js?v=${.now?string('hhmmSSsss')}"></script>
    <script type="text/javascript" src="/static/js/commonPlugin.js?v=${.now?string('hhmmSSsss')}"></script>
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
                    <a class="sel_btn ch_cls" onclick="openAddDialog()">新增</a>
                </td>
                <td style="height: 20px;">
                    <a class="sel_btn ch_cls" onclick="exportJob()">导出</a>
                </td>
            </tr>
        </table>
    </div>
    <table id="data_table" style="height: 100%;"></table>
</div>


<div style="display:none">
    <div id="addEditDialog" class="dialog">
        <form id="addEditForm">
            <input type="hidden" id="id" name = "id"/>
            <input type="hidden" name="jobNameId" id="jobNameId"/>
            <table style="width:95%;margin:10px 10px 10px 10px;">
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" >岗位名称:<font size="3" color="red">*</font></td>
                    <td style="align:center;">
                        <input class="easyui-validatebox input_width200 validatebox-text validatebox-invalid"  readonly="readonly" required="true" name="jobName" id="jobName" type="text" style="width: 200px;" />
                    </td>
                    <td style="width: 80px;text-align:right;">编制人数:<font size="3" color="red">*</font></td>
                    <td style="align:center;">
                        <input name="headcount" id="headcount" class="easyui-numberbox" type="text"data-options="min:0,max:99999,precision:0,required:true" style="width: 210px;" />
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;">生效日期:<font size="3" color="red">*</font></td>
                    <td style="align:center">
                        <input  class="easyui-datebox" data-options="editable:false,required:true" name="startDate" id="startDate"  style="width: 210px;">
                    </td>
                    <td style="width: 80px;text-align:right;">失效日期:<font size="3" color="red">*</font></td>
                    <td style="align:center">
                        <input  class="easyui-datebox" data-options="editable:false,required:true" name="endDate" id="endDate"  style="width: 210px;"  >
                    </td>
                </tr>


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

        $("#jobName").jobNameBox({
            valueSelector: '#jobNameId',
        });


        $("#data_table").datagrid({
            queryParams: getFormData("searchForm"), //参数
            url: '/job/queryJobPageList',
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
                        var html = '<a class="sel_btn ch_cls" href="javascript:editUserRole()" style="text-decoration:none;">编辑</a>';
                        return html;
                    }
                },
                {title: '岗位ID', field: 'jobId', width: 100, align: 'center'},
                {title: '岗位名称', field: 'jobName', width: 200, align: 'center'}
            ]],
            columns: [[
                {title: '编制人数', field: 'headcount', width: 80, align: 'center'},
                {title: '生效日期', field: 'startDate', width: 150, align: 'center'},
                {title: '失效日期', field: 'endDate', width: 150, align: 'center'},
                {title: '岗位名称ID', field: 'jobNameId', width: 150, align: 'center'}
            ]]


        });


        $("#addEditDialog").dialog({
            title:'新增',
            width:'700',
            height:'250',
            resizable : false,
            left: '20%',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'提交',
                iconCls:'icon-ok',
                handler:function(){
                    saveOrUpdateJob();
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

    function queryList() {
        var data = getFormData("search_form");
        $('#data_table').datagrid({url: '/job/queryJobPageList', queryParams: data});
    }

    function clearQuery() {
        $('#search_form').form('clear');
        queryList();
    }

    //打开新增岗位 窗口
    function openAddDialog() {
        $('#addEditDialog').dialog('setTitle','新增岗位');
        $('#addEditDialog').dialog('open');
        $('#endDate').datebox('setValue','9999-12-31');
        $("#headcount").numberbox('setValue',1);
    }

    function saveOrUpdateJob() {
        var id =  $('#id').val();
        if (!id) {
            id = null;
        }

        var jobNameId =  $('#jobNameId').val();
        var jobName =  $('#jobName').val();
        var startDate =  $('#startDate').datebox('getValue');
        var endDate =  $('#endDate').datebox('getValue');
        var headcount = $('#headcount').numberbox('getValue').replace(/\s+/g,"");

        if (jobName == '' || jobName == null) {
            layer.alert("岗位名称不能为空", {icon: 5, title: "提示"});
            return;
        }
        if (headcount == null || headcount == ''){
            layer.alert("岗位编制人数不能为空", {icon: 5, title: "提示"});
            return;
        }
        if (startDate == '' || startDate == null) {
            layer.alert("生效时间不能为空", {icon: 5, title: "提示"});
            return;
        }
        if (endDate == '' || endDate == null) {
            layer.alert("失效时间不能为空", {icon: 5, title: "提示"});
            return;
        }


        $.ajax({
            type : "POST",
            url : "/job/saveOrUpdateJob",
            dataType: "json",
            data : {
                "id":id,
                "jobNameId":jobNameId,
                "jobName":jobName,
                "headcount":headcount,
                "startDate":startDate,
                "endDate":endDate
            },
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

</script>

</html>