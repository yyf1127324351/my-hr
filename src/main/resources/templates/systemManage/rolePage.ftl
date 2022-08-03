
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>角色管理页面</title>

    <#include "/common/common.ftl"/>
</head>
<body class="easyui-layout">
<div data-options="region:'north'" class="panel-fit">
    <form id="search_form" >
        <div style="padding: 4px 5px;">
            <div class="search-row">
                <div class="form-group">
                    <label class="search-label">角色编码:</label>
                    <input name="roleCode" class="easyui-textbox" type="text" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label class="search-label">角色名称:</label>
                    <input name="roleName" class="easyui-textbox" type="text" style="width: 120px;">
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
            <td style="height: 20px;width: 100%">
                <a class="easyui-linkbutton toolButton" onclick="addInfo()" data-options="iconCls:'icon-add'"style="margin-top: 2px" >新增</a>
            </td>
        </table>
    </div>
    <table id="data_table" style="height: 100%;"></table>
</div>
<div style="display:none">
    <div id="addEditRoleDialog" class="dialog">
        <input type="hidden" id="id"/>
        <table style="width:95%;margin:10px 10px 10px 10px;">
            <tr style="height:30px;">
                <th style="width: 80px;text-align:right;" >角色编码：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="roleCode" class="easyui-textbox" prompt="1/30" type="text" style="width: 300px;"data-options="validType:'length[1,30]'"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <th style="width: 80px;text-align:right;">角色名称：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="roleName" class="easyui-textbox" prompt="1/30" type="text" style="width: 300px;"data-options="validType:'length[1,30]'"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <th style="width: 80px;text-align:right;">备注：&nbsp;&nbsp;</th>
                <td style="align:center;">
                    <input id="remark" class="easyui-textbox"  type="text" style="width: 300px;"/>
                </td>
            </tr>

        </table>
    </div>
</div>
<div id="win"></div>

<script type="text/javascript">
    var roleAuth = {};

    $(document).ready(function () {
        document.onkeydown = function (e) {
            if (e.keyCode == '13') {
                queryList();
            }
        };

        $("#addEditRoleDialog").dialog({
            width:'440',
            height:'195',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'提交',
                iconCls:'icon-ok',
                handler:function(){
                    addUpdateRole();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addEditRoleDialog').dialog('close');
                }
            }],
            onClose:function(){
                //清空所有数据
                $('#id').val('');
                $('#roleCode').textbox('clear');
                $('#roleName').textbox('clear');
                $('#remark').textbox('clear');
            },
            closable: true,
            closed: true   //已关闭
        });

        $("#data_table").datagrid({
            queryParams: getFormData("searchForm"), //参数
            url: '/role/getRolePageList',
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
                {title: '操作', field: 'id', width: 180, align: 'center',
                    formatter: function (val, row) {
                        var html = "";
                        var status = row.status;
                        var id = row.id;
                        if (status == 1){
                            html = html + '<a class="sel_btn ch_cls" href="javascript:offUseRole(' + id + ')" style="text-decoration:none;">停用</a>&nbsp;';
                            html = html + '<a class="sel_btn ch_cls" href="javascript:editInfo(' + id + ')" style="text-decoration:none;">编辑</a>&nbsp;';
                            html = html + '<a class="sel_btn ch_cls" href="javascript:authEdit(' + id + ')" style="text-decoration:none;">分配权限</a>';
                        }else {
                            html = html + '<a class="sel_btn ch_cls" href="javascript:onUseRole(' + id + ')" style="text-decoration:none;">启用</a>';
                        }
                        return html;
                    }
                },
                {title: '状态', field: 'status', width: 100, align: 'center',
                    formatter: function (val, row, index) {
                        var status = row.status;
                        if (status == 1) {
                            return "使用中";
                        }else {
                            return "<font color='#216DDD'>已停用</font>";
                        }

                    }
                },
                {title: '角色编码', field: 'roleCode', width: 150, align: 'center'},
                {title: '角色名称', field: 'roleName', width: 220, align: 'center'}

            ]],
            columns: [[
                {title: '备注', field: 'remark', width: 130, align: 'center'}
            ]]


        });

    });

    function clearQuery() {
        $('#search_form').form('clear');
        queryList();
    }

    function queryList() {
        var data = getFormData("search_form");
        $('#data_table').datagrid({url: '/role/getRolePageList', queryParams: data});
    }
    function addInfo() {
        $('#addEditRoleDialog').dialog('setTitle','新增角色');
        $('#addEditRoleDialog').dialog('open');
    }
    function editInfo(id) {
        $('#addEditRoleDialog').dialog('setTitle','编辑角色');
        $('#addEditRoleDialog').dialog('open');
        var rowDate = $("#data_table").datagrid('getSelected');
        $('#id').val(id);
        $('#roleCode').textbox('setText',rowDate.roleCode);
        $('#roleName').textbox('setText',rowDate.roleName);
        $('#remark').textbox('setText',rowDate.remark);
    }

    function addUpdateRole() {
        var url = '/role/addRole';
        var id = $('#id').val();
        if (null != id && '' != id) {
            url = '/role/updateRole';
        }
        var roleCode = $('#roleCode').textbox('getText').replace(/\s+/g, "");
        var roleName = $('#roleName').textbox('getText').replace(/\s+/g, "");
        var remark = $('#remark').textbox('getText').replace(/\s+/g, "");
        if (null == roleCode || '' == roleCode) {
            layer.alert('角色编码不能为空！', {icon: 0, title: "提示"});
            return;
        }
        if (roleCode.length > 30){
            layer.alert('角色编码不能超过30个字符！', {icon: 0, title: "提示"});
            return;
        }
        if (null == roleName || '' == roleName) {
            layer.alert('角色名称不能为空！', {icon: 0, title: "提示"});
            return;
        }
        if (roleName.length > 20){
            layer.alert('角色名称不能超过20个字符！', {icon: 0, title: "提示"});
            return;
        }
        $.messager.progress();	//防止重复提交
        $.ajax({
            type : "POST",
            url : url,
            data : {
                "id": id,
                "roleCode":roleCode,
                "roleName":roleName,
                "remark":remark
            },
            dataType: "json",
            success : function(result) {
                $.messager.progress('close');
                if(result.code == 200){
                    queryList();
                    $('#addEditRoleDialog').dialog('close');
                }else {
                    layer.alert(result.message, {icon: 5, title: "提示"});
                }
            },
            error :function(){
                $.messager.progress('close');
                layer.alert('系统异常', {icon: 5, title: "提示"});
            }
        });
    }
    function offUseRole(id) {
        updateRoleStatus(id,2);
    }
    function onUseRole(id) {
        updateRoleStatus(id,1);
    }
    function updateRoleStatus(id,status){
        $.messager.progress();	//防止重复提交
        $.ajax({
            type : "POST",
            url : '/role/updateRole',
            data : {
                "id": id,
                "status":status
            },
            dataType: "json",
            success : function(result) {
                $.messager.progress('close');
                if(result.code == 200){
                    queryList();
                }else {
                    layer.alert(result.message, {icon: 5, title: "提示"});
                }
            },
            error :function(){
                $.messager.progress('close');
                layer.alert('系统异常', {icon: 5, title: "提示"});
            }
        });
    }

    function authEdit(id) {
        $("#win").append("<div id='new_win' class='easyui-dialog'>" +
                "<div style='width:250px;float:left;'>" +
                "<div style='color:red;margin-top: 10px; margin-left: 10px;font-weight:bold;'>1、菜单权限</div>" +
                "<ul id='menu_manage_tree' class='easyui-tree' style='margin-left: 10px;'></ul>" +
                "</div>" +
                "<div style='width:250px;float:left;'>" +
                "<div style='color:red;margin-top: 10px; margin-left: 10px;font-weight:bold;'>2、部门权限</div>" +
                "<ul id='dept_manage_tree' class='easyui-tree' style='margin-left: 10px;'></ul>" +
                "</div>" +
                "<div style='width:250px;float:left;'>" +
                "<div style='color:red;margin-top: 10px; margin-left: 10px;font-weight:bold;'>3、区域权限</div>" +
                "<ul id='area_manage_tree' class='easyui-tree' style='margin-left: 10px;'></ul>" +
                "</div>" +
                "</div>");// 创建一个临时层，关闭销毁。
        $('#new_win').dialog({
            title:'角色权限分配',
            width:'800',
            height:'90%',
            iconCls:'icon-add',
            shadow:false,
            modal:true,
            onOpen:function(){
                showAuthTree(id);
            },//回调onload函数
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    saveAuthTree(id);
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#new_win').dialog('close');
                    roleAuth = {};
                }
            }],
            onClose:function(){
                $('#new_win').dialog("destroy");
                roleAuth = {};
            },
            closable: true,
            closed: true   //已关闭
        });

        $('#new_win').dialog('open');
    }
    //生成权限树
    function showAuthTree(id) {
        $.ajax({
            type : "POST",
            url : '/role/getAuthTree',
            data : {"roleId":id},
            dataType: "json",
            success : function(result) {
                $.messager.progress('close');
                if(result.code == 200){
                    $('#menu_manage_tree').tree({
                        data: result.data.menuTreeData,
                        animate: true,
                        cascadeCheck :false,
                        checkbox: true,
                        lines: true,
                        onLoadSuccess: function(node, data) {
                            cleatTreeIcon();
                            var nodes = $(this).tree('getChecked');
                            roleAuth.menuAuthBefore = _.pluck(nodes, 'id');
                        },
                        onCheck: function(node, checked) {
                            var checkOrNo = checked ? 'tree-checkbox1' : 'tree-checkbox0';
                            $(node.target).parent('li').find('span.tree-checkbox').removeClass('tree-checkbox0 tree-checkbox1').addClass(checkOrNo);
                        }
                    });
                    $('#area_manage_tree').tree({
                        data: result.data.areaTreeData,
                        animate: true,
                        cascadeCheck: false,
                        checkbox: true,
                        lines: true,
                        onLoadSuccess: function (node, data) {
                            cleatTreeIcon();
                            var nodes = $(this).tree('getChecked');
                            roleAuth.areaAuthBefore = _.pluck(nodes, 'id');
                        },
                        onCheck: function(node, checked) {
                            var checkOrNo = checked ? 'tree-checkbox1' : 'tree-checkbox0';
                            $(node.target).parent('li').find('span.tree-checkbox').removeClass('tree-checkbox0 tree-checkbox1').addClass(checkOrNo);
                            if (checkOrNo == 'tree-checkbox0'){
                                //如果勾掉子节点，则将父节点也勾掉
                                $($($("#area_manage_tree").children('li').get(0)).children('div').get(0)).find('span.tree-checkbox').removeClass('tree-checkbox0 tree-checkbox1').addClass(checkOrNo);
                            }else {
                                //如果本次勾选的是子节点，那么 判断同级别子节点是否全部选中，如果全部选中，则选中父节点
                                var checkedList =  _.pluck($('#area_manage_tree').tree('getChecked'), 'id');
                                if (0 == checkedList[0]){
                                    checkedList = checkedList.splice(1);
                                }
                                var areaTreeList = result.data.areaTreeData;
                                var childrenList = areaTreeList[0].children;
                                if (checkedList.length == childrenList.length) {
                                    $($($("#area_manage_tree").children('li').get(0)).children('div').get(0)).find('span.tree-checkbox').removeClass('tree-checkbox0 tree-checkbox1').addClass(checkOrNo);
                                }

                            }

                        }
                    });

                }else {
                    layer.alert(result.message, {icon: 5, title: "提示"});
                }
            },
            error :function(){
                $.messager.progress('close');
                layer.alert('系统异常', {icon: 5, title: "提示"});
            }
        });
    }

    function saveAuthTree(id) {
        debugger;
        //菜单
        roleAuth.menuAuthAfter = _.pluck($('#menu_manage_tree').tree('getChecked'), 'id');
        var menuAuthAdd = _.difference(roleAuth.menuAuthAfter, roleAuth.menuAuthBefore);
        var menuAuthDelete = _.difference(roleAuth.menuAuthBefore, roleAuth.menuAuthAfter);

        //地区
        roleAuth.areaAuthAfter = _.pluck($('#area_manage_tree').tree('getChecked'), 'id');
        var areaAuthAdd = _.difference( roleAuth.areaAuthAfter, roleAuth.areaAuthBefore);
        var areaAuthDelete = _.difference(roleAuth.areaAuthBefore, roleAuth.areaAuthAfter);
        if(_.isEqual(roleAuth.menuAuthAfter, roleAuth.menuAuthBefore)
                && _.isEqual(roleAuth.areaAuthAfter, roleAuth.areaAuthBefore)){
            layer.alert('没有修改的内容！', {icon: 0, title: "提示"});
            return false;
        }
        if (0 == areaAuthAdd[0]){
            areaAuthAdd = areaAuthAdd.splice(1);
        }
        if (0 == areaAuthDelete[0]){
            areaAuthDelete = areaAuthDelete.splice(1);
        }

        $.messager.progress();	//防止重复提交
        $.ajax({
            type: "POST",
            url: '/role/saveAuthTree',
            data: {
                'menuAuthAdd': menuAuthAdd,
                'menuAuthDelete':menuAuthDelete,
                'areaAuthAdd':areaAuthAdd,
                'areaAuthDelete':areaAuthDelete,
                'roleId':id
            },
            dataType: "json",
            traditional:true,
            success: function (result) {
                $.messager.progress('close');
                if(result.code == 200){
                    $('#new_win').dialog('close'); roleAuth = {};
                    layer.alert(result.message, {icon: 6, title: "提示"});
                }else {
                    layer.alert(result.message, {icon: 5, title: "提示"});
                }
            },
            error :function(){
                $.messager.progress('close');
                layer.alert('系统异常', {icon: 5, title: "提示"});
            }
        });

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

    function cleatTreeIcon(){
        $(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
        $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed");
    }
</script>

</body>
</html>
