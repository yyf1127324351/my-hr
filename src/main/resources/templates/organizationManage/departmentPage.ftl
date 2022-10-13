<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门管理页面</title>
    <#include "/common/common.ftl"/>
    <script type="text/javascript" src="/static/js/common.js?v=${.now?string('hhmmSSsss')}"></script>
    <script type="text/javascript" src="/static/js/commonPlugin.js?v=${.now?string('hhmmSSsss')}"></script>


</head>
<body class="easyui-layout">

<#-- 左侧部门树 -->
<div data-options="region:'west',split:true" style="width:15%">
    <ul id="dept_tree" class="easyui-tree" style=""></ul>
</div>

<#-- 左侧部门树右键操作 -->
<div id="handleDept" class="easyui-menu" style="width:100px;">
    <div id="dept_add" data-options="iconCls:'icon-add'" >新增</div>
    <div id="dept_move" data-options="iconCls:'icon-edit'">平移</div>
    <div id="dept_del" data-options="iconCls:'icon-remove'" >失效</div>
</div>

<#-- 搜索栏 -->
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" class="panel-fit">
            <form id="search_form">
                <div style="padding: 4px 5px;">
                    <div class="search-row">
                        <div class="form-group">
                            <label class="search-label">查询日期:</label>
                            <input class="easyui-datebox" type="text" name="queryDate" id="queryDate" value="${queryDate}" style="width:100px;">
                        </div>
                        <div class="form-group">
                            <label class="search-label">状态:</label>
                            <select class="easyui-combobox" type="text" name="queryStatus" id="queryStatus" data-options="editable:false" style="width:80px;">
                                <option value=0 selected>全部</option>
                                <option value=1>有效</option>
                                <option value=2>无效</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="search-label">部门ID:</label>
                            <input name="queryId" id="queryId" class="easyui-textbox" type="text" style="width: 90px;">
                        </div>
                        <div class="form-group">
                            <label class="search-label">部门名称:</label>
                            <input name="queryName" id="queryName" class="easyui-textbox" type="text" style="width: 120px;">
                        </div>
                        <div class="form-group">
                            <a class="easyui-linkbutton" style="height: 22px" data-options="iconCls:'icon-search'" onclick="queryList()" >搜索</a>
                            <a class="easyui-linkbutton" style="height: 22px" data-options="iconCls:'icon-clear'"  onclick="javascript:clearQuery()" >重置</a>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div data-options="region:'center'">
            <table id="data_table" style="height: 100%;"></table>
        </div>
    </div>
</div>

<#-- 新增dialog -->
<div style="display:none">
    <div id="addDeptDialog" class="dialog">
        <form id="addDeptForm">
            <table style="width:95%;margin:10px 10px 10px 10px;">
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" >上级部门:</td>
                    <td style="align:center;" class="readonly_style">
                        <input type='hidden' id='addParentId' name="parentId"/>
                        <input id="addParentName" name ="parentName" class="easyui-textbox" readonly="readonly" type="text" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" ><font size="3" color="red">*</font>部门名称:</td>
                    <td style="align:center;">
                        <input id="addName" name="name" class="easyui-textbox" type="text" data-options="required:true" style="width: 210px;"/>
                    </td>
                    <td style="width: 80px;text-align:right;" >部门级别:</td>
                    <td style="align:center;" class="readonly_style">
                        <input id="addLevel" name="level" class="easyui-textbox validStyle" readonly="readonly" type="text" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;"><font size="3" color="red">*</font>生效日期:</td>
                    <td style="align:center">
                        <input  class="easyui-datebox" data-options="editable:false,required:true" name="startDate" id="addStartDate"  style="width: 210px;">
                    </td>
                    <td style="width: 80px;text-align:right;"><font size="3" color="red">*</font>失效日期:</td>
                    <td style="align:center">
                        <input  class="easyui-datebox" data-options="editable:false,required:true" name="endDate" id="addEndDate"  style="width: 210px;"  >
                    </td>
                </tr>

                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" ><font size="3" color="red">*</font>部门负责人:</td>
                    <td style="align:center;">
                        <input type="hidden" id="addLeaderId" name="leaderId"/>
                        <input id="addLeaderName" name="leaderName" class="easyui-validatebox validStyle" type="text" data-options="required:true" readonly="readonly" style="width: 202px;"/>
                    </td>
                    <td style="width: 80px;text-align:right;" ><font size="3" color="red">*</font>部门HRBP:</td>
                    <td style="align:center;">
                        <input type="hidden" id="addHrbpId" name="hrbpId" />
                        <input id="addHrbpName" name="hrbpName" class="easyui-validatebox validStyle" type="text" data-options="required:true" readonly="readonly" style="width: 202px;"/>
                    </td>
                </tr>

                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" >部门介绍:</td>
                    <td colspan='3'>
                        <textarea id="addIntroduction" name="introduction" style="height:60px;width: 530px;" cols="80" rows=4  class="textarea"  maxlength=100 ></textarea>
                    </td>
                </tr>

            </table>

        </form>
    </div>
</div>


<#-- 失效dialog -->
<div style="display:none">
    <div id="expireDeptDialog" class="dialog">
        <form id="expireDeptForm">
            <input type="hidden" id="expirePkid" name="pkid"/>
            <input type="hidden" id="expireId" name="id"/>
            <input type="hidden" id="expireParentEndDate"/>
            <table style="width:95%;margin:10px 10px 10px 10px;">
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" >部门名称:</td>
                    <td style="align:center;" class="readonly_style">
                        <input id="expireName" name="name" class="easyui-textbox" type="text" readonly="readonly" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;">部门级别:</td>
                    <td style="align:center;" class="readonly_style">
                        <input id="expireLevel" name="level" class="easyui-textbox" readonly="readonly" type="text" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;">生效日期:</td>
                    <td style="align:center" class="readonly_style">
                        <input id="expireStartDate" name="startDate" class="easyui-textbox" type="text" readonly="readonly" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;" >原失效日期:</td>
                    <td style="align:center" class="readonly_style">
                        <input id="expireOldEndDate" name="oldEndDate" class="easyui-textbox" type="text" readonly="readonly" style="width: 210px;"/>
                    </td>
                </tr>
                <tr style="height:30px;">
                    <td style="width: 80px;text-align:right;"><font size="3" color="red">*</font>新失效日期:</td>
                    <td style="align:center">
                        <input  class="easyui-datebox" data-options="editable:false,required:true" name="endDate" id="expireEndDate"  style="width: 210px;"  >
                    </td>
                </tr>

            </table>

        </form>
    </div>
</div>

</body>

<script type="text/javascript">
    $(document).ready(function () {
        // document.onkeydown = function (e) {
        //     if (e.keyCode == '13') {
        //         queryList();
        //     }
        // };

        $("#addLeaderName").userbox({
            valueSelector: '#addLeaderId'
        });

        $("#addHrbpName").userbox({
            valueSelector: '#addHrbpId'
        });

        var queryDate = $("#queryDate").datebox('getValue');
        $('#dept_tree').tree({
            url: "/department/loadDepartmentTree",
            queryParams : {queryDate:queryDate},
            cascadeCheck: false,
            onClick: function (node) {
                //点击查询
                deptTreeClickQueryList(node.id);
            },
            onBeforeExpand: function (node) {
            },
            //右键组织crud
            onContextMenu : function(e, node) {
                $(this).tree('select',node.target);
                e.preventDefault();

                var level = node.level;
                //如果该节点是3级部门，则隐藏新增功能
                if(level >= 3) {
                    $('#dept_add').hide();
                }else {
                    $('#dept_add').show();
                }
                //如果该节点是顶级部门，则隐藏平移，和失效功能
                if (level <= 0) {
                    $('#dept_del').hide();
                    $('#dept_move').hide();
                }else {
                    $('#dept_del').show();
                    $('#dept_move').show();
                }

                var nowQueryDate = $("#queryDate").datebox('getValue');
                var nowDate = new Date().format("yyyy-MM-dd");
                if (nowQueryDate != nowDate) {
                    layer.alert('请选择当前日期并查询最新的部门架构后,再进行操作！', {icon: 0, title: "提示"});
                    return false;
                }

                $('#handleDept').menu('show', {
                    left : e.pageX,
                    top : e.pageY,

                    onClick: function(item) {
                        if(item.text == '新增') {
                            if(level > 2) {
                                layer.alert('3级部门下无法创建子部门！', {icon: 0, title: "提示"});
                                return;
                            }
                            openAddDeptDialog(node);
                        } else if(item.text == '平移') {



                        } else if(item.text == '失效') {
                            if(node.hasChild == 1) {
                                layer.confirm('该部门下有子部门，确认全部失效吗？', {icon: 3},function (index) {
                                    layer.close(index);
                                    openExpireDeptDialog(node);

                                });
                            }else {
                                layer.confirm('确认失效该部门吗？', {icon: 3},function (index) {
                                    openExpireDeptDialog(node);
                                    layer.close(index);
                                });
                            }

                        }
                    }

                });

            },
            onExpand: function (node) {
                // node.id = node.id.split("_")[0];
            },
            onLoadSuccess: function (node) {
                // $(".tree-icon,.tree-file").removeClass("tree-icon tree-file"); //去掉 子图标
                $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed"); //去掉父图标
            }
        });



        $("#data_table").datagrid({
            url: '/department/queryDepartmentPageList',
            method: 'post',
            loadMsg: "数据装载中,请稍等....",
            nowrap: true, //单元格内容是否可换行
            fitColumns: true, //自适应网格宽度
            showFooter: false, //是否显示最后一行，统计使用
            pagination: true,
            checkbox: true,
            resizable:true,
            singleSelect: true,
            rownumbers: true,
            pageSize: 50,
            pageList: [50, 100, 200],
            toolbar: '#button_tab',
            onBeforeLoad: function (param) {
                param.queryDate = queryDate;
                var status =$("#queryStatus").combobox('getValue');
                param.status = status;
            },
            onLoadSuccess: function (data) {

            },
            onClickRow:function (rowIndex, rowData) {
                var deptId = rowData.id;
                var node = $('#dept_tree').tree('find', deptId);
                $('#dept_tree').tree('expandTo', node.target);
                $('#dept_tree').tree('select', node.target);
            },
            // frozenColumns: [[
            //
            // ]],
            columns: [[
                {title: '操作', field: 'pkid', align: 'center',fixed: true,
                    formatter: function (val, row) {
                        var html = "";
                        var isValid = row.isValid;
                        if (isValid == 0) {
                        }else {
                            html = html + '<a class="sel_btn a_margin" href="javascript:openEditDialog()" >修改</a>';
                            html = html + '<a class="sel_btn a_margin" href="javascript:openChangeDialog()">变更</a>';
                        }
                        return html;
                    }
                },
                {title: '部门ID', field: 'id', width: 100, align: 'center'},
                {title: '部门名称', field: 'name', width: 200, align: 'center'},
                {title: '部门级别', field: 'level', width: 80, align: 'center'},
                {title: '生效日期', field: 'startDate', width: 150, align: 'center'},
                {title: '失效日期', field: 'endDate', width: 150, align: 'center'}


            ]]


        });


        $("#addDeptDialog").dialog({
            width:'700',
            height:'300',
            resizable : false,
            left: '20%',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'提交',
                iconCls:'icon-ok',
                handler:function(){
                    addDepartment();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addDeptDialog').dialog('close');
                }
            }],
            onClose:function(){
                $("#addDeptDialog form").form("reset");
            },
            closable: true,
            closed: true   //已关闭
        });

        $("#expireDeptDialog").dialog({
            width:'390',
            height:'260',
            resizable : false,
            left: '25%',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'提交',
                iconCls:'icon-ok',
                handler:function(){
                    expireDepartment();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#expireDeptDialog').dialog('close');
                }
            }],
            onClose:function(){
                $("#expireDeptDialog form").form("reset");
            },
            closable: true,
            closed: true   //已关闭
        });

    });

    /*点击部门对应部门，进行搜索*/
    function deptTreeClickQueryList(id) {
        var queryDate = $("#queryDate").datebox('getValue');
        if (!queryDate) {
            layer.alert("查询日期不能为空", {icon: 5, title: "提示"});
        }
        var queryStatus =$("#queryStatus").combobox('getValue');
        var queryParams = {};
        queryParams.id = id;
        queryParams.queryDate = queryDate;
        queryParams.status = queryStatus;

        $('#data_table').datagrid({url: '/department/queryDepartmentPageList', queryParams: queryParams});
    }

    /*搜索*/
    function queryList() {
        var queryParams = {};
        var queryDate = $("#queryDate").datebox('getValue');
        if (!queryDate) {
            layer.alert("查询日期不能为空", {icon: 5, title: "提示"});
        }
        queryParams.queryDate = queryDate;
        $('#dept_tree').tree("options").queryParams = queryParams;
        $('#dept_tree').tree('reload');

        var queryStatus = $("#queryStatus").combobox('getValue');
        var queryName = $("#queryName").val().replace(/\s+/g, "");
        var queryId = $("#queryId").val().replace(/\s+/g, "");
        queryParams.status = queryStatus;
        queryParams.name = queryName;
        queryParams.queryId = queryId;
        $('#data_table').datagrid({url: '/department/queryDepartmentPageList', queryParams: queryParams});
    }

    /*清空搜索条件*/
    function clearQuery() {
        $('#search_form').form('clear');
        var queryDate = new Date().format("yyyy-MM-dd");
        $("#queryDate").datebox('setValue',queryDate);
        $("#queryStatus").combobox('setValue',0);
        $('#dept_tree').tree('reload');
        queryList();
    }

    /*打开 新增部门窗口*/
    function openAddDeptDialog(node) {
        $('#addDeptDialog').dialog('setTitle','新增部门');
        $('#addDeptDialog').dialog('open');

        var parentId = node.id;
        var parentName = node.name;
        var level = node.level + 1;

        $('#addParentId').val(parentId);
        $('#addParentName').textbox().textbox('setValue',parentName);
        $('#addLevel').textbox().textbox('setValue',level);

        $('#addEndDate').datebox('setValue','9999-12-31');

    }

    /*新增部门*/
    function addDepartment() {
        var name = $('#addName').textbox('getValue').replace(/\s+/g, "");
        if (!name) {
            layer.alert("部门名称不能为空", {icon: 5, title: "提示"});
            return;
        }

        var startDate =  $('#addStartDate').datebox('getValue');
        var endDate =  $('#addEndDate').datebox('getValue');
        if (!startDate) {
            layer.alert("生效时间不能为空", {icon: 5, title: "提示"});
            return;
        }
        if (!endDate) {
            layer.alert("失效时间不能为空", {icon: 5, title: "提示"});
            return;
        }
        var leaderName = $("#addLeaderName").val();
        var hrbpName = $("#addHrbpName").val();
        if(!leaderName) {
            layer.alert("部门负责人不能为空", {icon: 5, title: "提示"});
            return;
        }
        if(!hrbpName) {
            layer.alert("部门HRBP不能为空", {icon: 5, title: "提示"});
            return;
        }

        var data = getFormData("addDeptForm");
        data.name = name;

        layer.confirm("确认新增该部门吗？", {icon: 3}, function () {
            $.ajax({
                type : "POST",
                url : "/department/addDepartment",
                dataType: "json",
                data : data,
                success : function(result) {
                    if(result.code == 200){
                        //保存完成之后跳到列表
                        queryList();
                        $('#addDeptDialog').dialog('close');
                        layer.alert(result.message, {icon: 1, title: "提示"},function (index) {
                            //部门树定位到 新增的部门
                            var dept = result.data;
                            var node = $('#dept_tree').tree('find', dept.id);
                            $('#dept_tree').tree('expandTo', node.target);
                            $('#dept_tree').tree('select', node.target);
                            layer.close(index);
                        });
                    }else{
                        //错误提示
                        layer.alert(result.message, {icon: 5,title: "提示"});
                    }
                },
                error :function(){
                    layer.alert('addDepartmentError', {icon: 5,title: "提示"});
                }
            });
        });
    }

    /*打开 失效部门窗口*/
    function openExpireDeptDialog(node){
        $('#expireDeptDialog').dialog('setTitle','失效部门');
        $('#expireDeptDialog').dialog('open');

        var parentNode = $('#dept_tree').tree('find', node.parentId);

        $('#expireParentEndDate').val(parentNode.endDate);

        var level = node.level + 1;
        $('#expirePkid').val(node.pkid);
        $('#expireId').val(node.id);
        $('#expireName').textbox().textbox('setValue',node.name);
        $('#expireLevel').textbox().textbox('setValue',level);
        $('#expireStartDate').textbox().textbox('setValue',node.startDate);
        $('#expireOldEndDate').textbox().textbox('setValue',node.endDate);

    }

    function expireDepartment() {

        var startDate =  $('#expireStartDate').textbox('getValue');
        var endDate =  $('#expireEndDate').textbox('getValue');
        if (!endDate) {
            layer.alert("新失效日期不能为空", {icon: 5, title: "提示"});
            return;
        }
        var startDateNum = startDate.replaceAll('-', '');
        var endDateNum = endDate.replaceAll('-', '');
        if (endDateNum <= startDateNum) {
            layer.alert("新失效日期必须晚于生效日期", {icon: 5, title: "提示"});
            return;
        }

        var parentEndDate = $('#expireParentEndDate').val();
        var parentEndDateNum = parentEndDate.replaceAll('-', '');
        if (endDateNum > parentEndDateNum) {
            layer.alert("新失效日期不能晚于父部门的失效日期<br/>["+parentEndDate+"]", {icon: 5, title: "提示"});
            return;
        }


        var data = getFormData("expireDeptForm");

        layer.confirm("确认对该部门以及其子部门进行失效吗？", {icon: 3}, function () {
            $.ajax({
                type : "POST",
                url : "/department/expireDepartment",
                dataType: "json",
                data : data,
                success : function(result) {
                    if(result.code == 200){
                        queryList();
                        $('#expireDeptDialog').dialog('close');
                        layer.alert(result.message, {icon: 1, title: "提示"});
                    }else{
                        //错误提示
                        layer.alert(result.message, {icon: 5,title: "提示"});
                    }
                },
                error :function(){
                    layer.alert('expireDepartmentError', {icon: 5,title: "提示"});
                }
            });
        });
    }
</script>

</html>