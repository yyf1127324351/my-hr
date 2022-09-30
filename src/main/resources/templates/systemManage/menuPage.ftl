<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>菜单管理</title>

    <#include "/common/common.ftl"/>
</head>
<body class="easyui-layout">
<!-- 首页左边Div  begin-->
<div data-options="region:'west',split:true" style="width:15%">
    <ul id="menu_tree" class="easyui-tree" style="">

    </ul>
</div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" class="panel-fit">
            <form id="search_form">
                <div style="padding: 4px 5px;">
                    <div class="search-row">
                        <div class="form-group">
                            <label class="search-label">菜单名称:</label>
                            <input name="name" class="easyui-textbox" type="text" style="width: 120px;">
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
           <#--<div id="button_tab" style="">-->
                <#--<table class="button_table">-->
                    <#--<td style="height: 20px;width: 100%">-->
                        <#--<a class="easyui-linkbutton toolButton" onclick="queryList()"-->
                           <#--data-options="iconCls:'icon-search'">搜索</a>-->
                        <#--<a class="easyui-linkbutton toolButton" onclick="javascript:clearQuery()" data-options="iconCls:'icon-clear'"-->
                           <#--style="margin-top: 2px">重置</a>-->
                    <#--</td>-->
                <#--</table>-->
            <#--</div>-->
            <table id="data_table" style="height: 100%;"></table>
        </div>
    </div>
</div>

<div id="handleMenu" class="easyui-menu" style="width:120px;">
    <div id="menu_add" data-options="iconCls:'icon-add'">新增</div>
    <div id="menu_edit" data-options="iconCls:'icon-edit'">编辑</div>
    <div id="menu_del" data-options="iconCls:'icon-remove'">删除</div>
</div>

<div style="display:none">
    <div id="addEditMenuDialog" class="dialog">
        <form>
            <input type="hidden" id="id" name="id"/>
            <table style="width:95%;margin:10px 10px 0 20px;">
            <tr style="height:30px;">
                <th style="width: 70px;text-align:right;" >菜单名称：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="name" class="easyui-textbox" prompt="1/30" type="text" style="width: 400px;"data-options="validType:'length[1,30]'"/>
                </td>

            </tr>
            <tr style="height:30px;">
                <th style="width: 75px;text-align:right;">权限编码：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="code" class="easyui-textbox" prompt="1/50" type="text" style="width: 400px;"data-options="validType:'length[1,50]'"/>
                </td>
            </tr>
            <tr id="url_tr" style="height:30px;">
                <th style="width: 75px;text-align:right;">菜单地址：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="url" class="easyui-textbox" prompt="1/100" type="text" style="width: 400px;"data-options="validType:'length[1,100]'"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <th style="width: 75px;text-align:right;">类型：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input type="hidden" id="type"/>
                    <input type="hidden" id="level"/>
                    <input id="typeName" class="easyui-textbox" readonly="readonly" prompt="1/50" type="text" style="width: 400px;"data-options="validType:'length[1,50]'"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <th style="width: 75px;text-align:right;">父菜单名：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input type="hidden" id="parentId"/>
                    <input id="parentName" class="easyui-textbox" readonly="readonly" prompt="1/50" type="text" style="width: 400px;"data-options="validType:'length[1,50]'"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <th style="width: 75px;text-align:right;">排序值：<font size="3" color="red">*</font></th>
                <td style="align:center;">
                    <input id="sortNumber" type="text" class="easyui-numberbox" prompt="请输入正整数" style="width: 400px;" data-options="min:0,precision:0"/>
                </td>
            </tr>

        </table>
        </form>
    </div>
</div>



<script type="text/javascript">
    $(document).ready(function () {
        document.onkeydown = function (e) {
            if (e.keyCode == '13') {
                queryList();
            }
        };


        $('#menu_tree').tree({
            url: "/menu/getAllMenuTree",
            cascadeCheck: false,
            onClick: function (node) {
                //点击查询
                var finalMenuIds = getMenuIds(node);
                treeClickQueryList(finalMenuIds);
            },
            onBeforeExpand: function (node) {
            },
            //右键组织crud
            onContextMenu : function(e, node) {
                $(this).tree('select',node.target);
                e.preventDefault();

                var menuId = node.id;
                if (menuId == 0) {
                    $('#menu_edit').hide();
                    $('#menu_del').hide();
                }else {
                    $('#menu_edit').show();
                    $('#menu_del').show();
                }
                var type = node.type;
                //如果类型是按钮，则隐藏新增功能
                if(type == 1) {
                    $('#menu_add').hide();
                }else {
                    $('#menu_add').show();
                }
                $('#handleMenu').menu('show', {
                    left : e.pageX,
                    top : e.pageY,

                    onClick: function(item) {
                        if(item.text == '新增') {
                            if(type == 1) {
                                layer.alert('末级菜单下不能新增！', {icon: 0, title: "提示"});
                                return;
                            }
                            addMenu(node);
                        } else if(item.text == '编辑') {
                            updateMenu(node);
                        } else if(item.text == '删除') {
                            if(node.hasChild == 1) {
                                layer.confirm('该菜单下包含子菜单，确认全部删除吗？', {icon: 3},function () {
                                    deleteMenu(node);
                                });
                            }else {
                                layer.confirm('确认删除？', {icon: 3},function () {
                                    deleteMenu(node);
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

        $("#addEditMenuDialog").dialog({
            width:'550',
            height:'300',
            close : true,
            shadow:false,
            modal:true,
            buttons:[{
                text:'提交',
                iconCls:'icon-ok',
                handler:function(){
                    addUpdateMenu();
                }
            },{
                text:'取消',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addEditMenuDialog').dialog('close');
                }
            }],
            onClose:function(){
                //清空所有数据
                $('#id').val('');
                $('#name').textbox('clear');
                $('#code').textbox('clear');
                $('#url').textbox('clear');
                $('#typeName').textbox('clear');
                $('#parentName').textbox('clear');
                $('#sortNumber').numberbox('clear');
                $('#type').val('');
                $('#level').val('');
                $('#parentId').val('');

            },
            closable: true,
            closed: true   //已关闭
        });

        $("#data_table").datagrid({
            queryParams: getFormData("searchForm"), //参数
            url: '/menu/getMenuPageList',
            method: 'post',
            loadMsg: "数据装载中,请稍等....",
            nowrap: true, //单元格内容是否可换行
            fitColumns: false, //自适应网格宽度
            showFooter: false, //是否显示最后一行，统计使用
            pagination: true,
            checkbox: true,
            singleSelect: true,
            rownumbers: true,
            pageSize: 50,
            pageList: [50, 100, 200],
            toolbar: '#button_tab',
            onLoadSuccess: function (data) {
            },
            onClickRow:function (rowIndex, rowData) {
                var menuId = rowData.id;
                var node = $('#menu_tree').tree('find', menuId);
                $('#menu_tree').tree('expandTo', node.target);
                $('#menu_tree').tree('select', node.target);
            },
            frozenColumns: [[
                // {title: '操作', field: 'id', width: 100, align: 'center',
                //     formatter: function (val, row) {
                //         var id = row.id;
                //         var html = "";
                //         html = html + '<a class="sel_btn " onclick="editMenu()" style="text-decoration:none;">编辑</a>';
                //         return html;
                //     }
                // },
                {title: '菜单名称', field: 'name', width: 230, align: 'center'}
            ]],
            columns: [[
                {title: '权限编码', field: 'code', width: 280, align: 'center'},
                {title: '类型', field: 'type', width: 50, align: 'center', sortable: true,
                    formatter: function (val,row) {
                        var level = row.level;
                        if (level == 1) {
                            return "菜单栏";
                        } else if (level == 2){
                            return "菜单";
                        } else {
                            return "按钮";
                        }
                    }
                },
                {title: '菜单ID', field: 'id', width: 80, align: 'center'},
                {title: '父菜单ID', field: 'parentId', width: 80, align: 'center'},
                {title: '级别', field: 'level', width: 50, align: 'center', sortable: true},
                {title: '菜单地址', field: 'url', width: 400, align: 'left', sortable: true},
                {title: '是否有子节点', field: 'hasChild', width: 100, align: 'center',
                    formatter: function (val) {
                        if (val == 0) {
                            return "无";
                        } else {
                            return "有";
                        }
                    }
                },
                {title: '排序值', field: 'sortNumber', width: 50, align: 'center', sortable: true}
            ]]


        });


    });

    function getMenuIds(node) {
        var finalMenuIds = node.id; //顶层id
        var childrenList = node.children; //第一层子节点
        if (null != childrenList && childrenList.length > 0) {
            for (var i = 0; i < childrenList.length; i++) {
                var menu = childrenList[i];
                finalMenuIds = finalMenuIds + "," + menu.id;
                var childrenList2 = menu.children;
                if (null != childrenList2 && childrenList2.length > 0) {
                    for (var j = 0; j < childrenList2.length; j++) {
                        var menu2 = childrenList2[j];
                        finalMenuIds = finalMenuIds + "," + menu2.id;
                        var childrenList3 = menu2.children;
                        if (null != childrenList3 && childrenList3.length > 0) {
                            for (var k = 0; k < childrenList3.length; k++) {
                                var menu3 = childrenList3[k];
                                finalMenuIds = finalMenuIds + "," + menu3.id;
                            }
                        }
                    }
                }
            }

        }
        return finalMenuIds;
    }

    function clearQuery() {
        $('#search_form').form('clear');
        $('#menu_tree').tree('reload');
        queryList();
    }

    function queryList() {
        var data = getFormData("search_form");
        $('#menu_tree').tree('reload');
        $('#data_table').datagrid({url: '/menu/getMenuPageList', queryParams: data});
    }

    function treeClickQueryList(menuIds) {
        var data = getFormData("search_form");
        data.menuIds = menuIds;
        $('#data_table').datagrid({url: '/menu/getMenuPageList', queryParams: data});
    }

    function addMenu(node) {
        $('#addEditMenuDialog').dialog('setTitle','新增');
        $('#addEditMenuDialog').dialog('open');
        var id = node.id;
        var level = node.level;
        if (level == 0){
            $('#type').val(0);
            $('#typeName').textbox('setText','菜单栏');
            $('#url_tr').hide();

        }else if (level == 1){
            $('#type').val(0);
            $('#typeName').textbox('setText','菜单');
            $('#url_tr').show();
        }else {
            $('#type').val(1);
            $('#typeName').textbox('setText','按钮');
            $('#url_tr').hide();
        }
        $('#typeName').textbox('textbox').css('background','#ccc');
        var nextLevel = level + 1;
        $('#level').val(nextLevel);
        var menuName = node.name;
        $('#parentId').val(id);
        $('#parentName').textbox('setText',menuName);
        $('#parentName').textbox('textbox').css('background','#ccc');


    }

    function updateMenu(node) {
        $('#addEditMenuDialog').dialog('setTitle', '编辑');
        $('#addEditMenuDialog').dialog('open');
        $('#id').val(node.id);
        var level = node.level;
        if (level == 1) {
            $('#typeName').textbox('setText', '菜单栏');
            $('#url_tr').hide();
        } else if (level == 2) {
            $('#typeName').textbox('setText', '菜单');
            $('#url_tr').show();
        } else {
            $('#typeName').textbox('setText', '按钮');
            $('#url_tr').hide();
        }
        $('#typeName').textbox('textbox').css('background','#ccc');
        $('#parentName').textbox('textbox').css('background','#ccc');
        $('#name').textbox('setText',node.name);
        $('#code').textbox('setText',node.code);
        $('#url').textbox('setText',node.url);
        $('#sortNumber').numberbox('setValue',node.sortNumber);
        var parent = $('#menu_tree').tree('getParent', node.target);
        $('#parentId').val(parent.id);
        $('#parentName').textbox('setText',parent.name);

    }


    function addUpdateMenu() {
        var id = $('#id').val();
        var name = $('#name').textbox('getText').replace(/\s+/g, "");
        var code = $('#code').textbox('getText').replace(/\s+/g, "");
        var url = $('#url').textbox('getText').replace(/\s+/g, "");
        var sortNumber = $('#sortNumber').val();
        var type = $('#type').val();
        var level = $('#level').val();
        var parentId =$('#parentId').val();
        if (null == name || '' == name) {
            layer.alert('菜单名称不能为空！', {icon: 0, title: "提示"});
            return;
        }
        if (null == code || '' == code) {
            layer.alert('菜单编码不能为空！', {icon: 0, title: "提示"});
            return;
        }
        if (level == 2 && (null == url || '' == url)) {
            layer.alert('菜单地址不能为空！', {icon: 0, title: "提示"});
            return;
        }
        if (null == sortNumber || '' == sortNumber) {
            layer.alert('排序值不能为空！', {icon: 0, title: "提示"});
            return;
        }
        var paramData = {};
        paramData.id= id;
        paramData.name= name;
        paramData.code= code;
        paramData.url= url;
        paramData.sortNumber= sortNumber;
        paramData.type= type;
        paramData.level= level;
        paramData.parentId= parentId;

        $.messager.progress();	//防止重复提交
        $.ajax({
            type : "POST",
            url : "/menu/addUpdateMenu",
            data :paramData,
            dataType: "json",
            success : function(result) {
                $.messager.progress('close');
                if(result.code == 200){
                    layer.alert('操作成功', {icon: 6, title: "提示"});
                    queryList();
                    $("#addEditMenuDialog").dialog('close');
                    $('#menu_tree').tree('reload');
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

    function deleteMenu(node) {
        //判断该菜单的父菜单有几个子菜单，如果有仅一个子菜单（即被删除的），则该父菜单需要更新为无子菜单。
        var finalMenuIds = getMenuIds(node);
        var id = node.id;
        var parentId = node.parentId;
        $.messager.progress();	//防止重复提交
        $.ajax({
            type : "POST",
            url : "/menu/deleteMenu",
            data : {
                "id": id,
                "parentId":parentId,
                "menuIds":finalMenuIds
            },
            dataType: "json",
            success : function(result) {
                $.messager.progress('close');
                if(result.code == 200){
                    layer.alert('操作成功', {icon: 6, title: "提示"});
                    queryList();
                    $("#addEditMenuDialog").dialog('close');
                    $('#menu_tree').tree('reload');
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



</script>

</body>
</html>
