<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <title>设置展示列模板页面</title>
</head>
<body class="easyui-layout">
	<input type="hidden" id="fieldType" value="${fieldType}" />
    <input type="hidden" id="fieldTemplateUserId" value="${fieldTemplateUserId}" />

	<div region="north" border="false" style=" padding: 5px">
		<div align="center">
			<table border="0" width="100%" class="search_table">
				<tr>
					<td width="20%" align='right'>展示列模板:</td>
					<td width="15%" align='left'>
                        <input id="editTemplate" class="easyui-combobox" style="width: 180px;">
					</td>
                    <td width="23%" align='right'>
                        默认展示该模板:
                    </td>
                    <td align='left'>
                        <input type="checkbox" id="isDefaultShow" name="isDefaultShow"/>
                    </td>
					<td width="45%" align='right'>
						<a href="javascript:saveTem();" class="sel_btn ch_cls">保存</a>
						<a href="javascript:editTem();" class="sel_btn ch_cls">修改模板名称</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div style="width: 100%; height: 85%;border-top:1px dashed  #95B8E7;">
		<table>
			<tr>
				<td>
                    <table>
						<thead>
							<tr>
								<th field="rname" width="170" align="center">未展示字段</th>
							</tr>
						</thead>
						<tr>
							<td><select id="noshow_select" size="18" style="width: 248px;height: 300px;">
							</select></td>
						</tr>
					</table>
                </td>
				<td>
					<div align="center">
						<a href="javascript:right_column()" class="easyui-linkbutton" style="width:50px;">选择-></a>
					</div>
					<div align="center" style="padding-top: 10px;">
						<a href="javascript: left_column()" class="easyui-linkbutton" style="width:50px;"><-删除</a>
					</div>
                    <div align="center" style="padding-top: 10px;">
                        <a href="javascript:up_column()" class="easyui-linkbutton" style="width:50px;">上移</a>
                    </div>
                    <div align="center" style="padding-top: 10px;">
                        <a href="javascript:down_column()" class="easyui-linkbutton" style="width:50px;">下移</a>
                    </div>
				</td>
				<td>
					<table id="notr">
						<thead>
							<tr>
								<th field="rname" width="170" align="center">展示字段</th>
								<th></th>
							</tr>
						</thead>
						<tr>
							<td><select  id="show_select" size="18"  style="width: 248px;height: 300px;">
							</select></td>
						</tr>
					</table>
				</td>

			</tr>
		</table>
	</div>

    <div style="display:none">
        <div id="edit_list_name" class="dialog">
            <form>
                <table style="width:95%;margin:10px 10px 10px 10px;">
                    <tr style="height:30px;">
                        <th style="width: 80px;text-align:right;" >模板名称：<font size="3" color="red">*</font></th>
                        <td style="align:center;">
                            <input id="templateName" class="easyui-textbox" type="text" style="width: 200px;" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>






    <script type="text/javascript">
        $(document).ready(function () {

            $("#edit_list_name").dialog({
                title:'修改模板名称',
                width:'350',
                height:'150',
                close : true,
                shadow:false,
                modal:true,
                buttons:[{
                    text:'确定',
                    iconCls:'icon-ok',
                    handler:function(){
                        saveTemplateName();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#edit_list_name').dialog('close');
                    }
                }],
                onClose:function(){
                },
                closable: true,
                closed: true   //已关闭
            });

            $("#noshow_select").dblclick(function(){
                right_column();//双击选择
            });

            $("#show_select").dblclick(function(){
                left_column();//双击删除
            });

            $("#editTemplate").combobox({
                url : '/columnField/queryColumnFieldTemplateUser?fieldType=1&allFlag=0',
                valueField : 'id',
                textField : 'templateName',
                multiple: false,
                editable:false,
                onLoadSuccess : function(){
                    var fieldTemplateUserId = $("#fieldTemplateUserId").val();
                    if (fieldTemplateUserId != null && fieldTemplateUserId > 0) {
                        $("#isDefaultShow").prop("checked", true)
                        $(this).combobox("setValue", fieldTemplateUserId);
                    }else {
                        var data = $(this).combobox("getData");
                        if (data && data.length > 0) {
                            fieldTemplateUserId = data[0].id;
                            var isDefaultShow = data[0].isDefaultShow;
                            if (isDefaultShow == 1) {
                                $("#isDefaultShow").prop("checked", true)
                            }else {
                                $("#isDefaultShow").prop("checked", false)
                            }
                            $(this).combobox("setValue", fieldTemplateUserId);
                        }
                    }
                    //设置 未展示字段和已展示字段
                    initColumnFieldShow(fieldTemplateUserId)

                },
                onSelect: function (node) {


                }
            });



        });

        function initColumnFieldShow(fieldTemplateUserId){
            $.ajax({
                type : "POST",
                url : "/columnField/queryUserColumnFieldMap",
                data : {"fieldTemplateUserId":fieldTemplateUserId},
                dataType: "json",
                success : function(result) {
                    if (result.code == 200) {
                        var noshowField = result.data.noSelectFields;
                        var showField = result.data.selectFields;
                        $("#noshow_select").html("");
                        $("#show_select").html("");
                        for(var i=0;noshowField!=null&&i<noshowField.length;i++){
                            var option ="<option value='"+noshowField[i].id+"'>"+noshowField[i].name+"</option>";
                            $("#noshow_select").append(option);
                        }
                        for(var i=0;showField!=null&& i<showField.length;i++){
                            var option ="<option value='"+showField[i].id+"'>"+showField[i].name+"</option>";
                            $("#show_select").append(option);
                        }
                    }else {
                        $('#new_columnWindow').dialog('close');
                        $('#new_columnWindow').dialog("destroy");
                        layer.alert(result.message, {icon: 0});
                    }

                },
                error :function(result){
                    $('#new_columnWindow').dialog('close');
                    $('#new_columnWindow').dialog("destroy");
                    layer.alert(result.message, {icon: 5});
                }
            });
        }


        //选择
        function right_column(){
            var selected= $("#noshow_select").find("option:selected");
            if(selected.text()!=''){
                var option ="<option value='"+selected.attr("value")+"'>"+selected.text()+"</option>";
                $("#show_select").append(option);
                selected.remove();
            }else{
                layer.alert('请选择展示字段', {icon: 0, title: "提示"});
            }
        }

        //删除
        function left_column(){
            var selected= $("#show_select").find("option:selected");
            if(selected.text()!=''){
                var option ="<option value='"+selected.attr("value")+"'>"+selected.text()+"</option>";
                $("#noshow_select").append(option);
                selected.remove();
            }else{
                layer.alert('请选择未展示字段', {icon: 0, title: "提示"});
            }
        }

        //上移
        function up_column(){
            var selected= $("#show_select").find("option:selected");
            if(selected.text()==''){
                layer.alert('请选择展示字段进行上移操作', {icon: 0, title: "提示"});
                return ;
            }
            if(selected.prev('option').text()=='姓名'){
                //$.messager.alert('提示','已经到顶了','warning');
                //return ;
            }else{
                selected.insertBefore(selected.prev('option'));
            }

        }

        //下移
        function down_column(){
            var selected= $("#show_select").find("option:selected");
            if(selected.text()==''){
                layer.alert('请选择展示字段进行下移操作', {icon: 0, title: "提示"});
                return ;
            }

            //索引的长度,从1开始
            var optionLength = $('#show_select')[0].options.length;
            //选中的索引,从0开始
            var optionIndex = $('#show_select').get(0).selectedIndex;
            //如果选择的不在最下面,表示可以向下
            if(optionIndex < (optionLength-1)){
                selected.insertAfter(selected.next('option'));
            }else{
                //$.messager.alert('提示','已经到底了','warning');
            }
        }

        //保存
        function saveTem(){
            var fieldTemplateUserId = $("#editTemplate").combobox("getValue");
            var templateName = $("#editTemplate").combobox("getText");


            var isDefaultShowResult= $('#isDefaultShow').is(':checked');
            var isDefaultShow = 0;
            if (isDefaultShowResult) {
                isDefaultShow = 1;
            }


            var options = $('#show_select')[0].options;
            var columnFieldIds='';
            for(var i=0; i<options.length; i++){
                //去掉默认显示的(例如：操作，姓名等固定列)
                if(!options[i].disabled){
                    if(columnFieldIds==''){
                        //第一笔
                        columnFieldIds = options[i].value;
                    }else{
                        columnFieldIds = columnFieldIds + ',' + options[i].value;
                    }
                }
            }
            if(columnFieldIds==''){
                layer.alert('请至少选择一项！', {icon: 0, title: "提示"});
                return ;
            }else{
                $.messager.progress({title:'保存中...'});
                $.ajax({
                    type : "POST",
                    url : "/columnField/updateColumnFieldTemplateUser",
                    data : {"id":fieldTemplateUserId,"columnFieldIds":columnFieldIds,"templateName":templateName,"isDefaultShow":isDefaultShow},
                    dataType: "json",
                    success : function(result) {
                        $.messager.progress('close');
                        if(result.code == 200){
                            layer.alert(result.message, {icon: 1});
                        }else{
                            layer.alert(result.message, {icon: 5});
                        }
                    },
                    error :function(){
                        layer.alert("展示列模板保存报错了！", {icon: 5, title: "错误"});
                        $.messager.progress('close');
                    }
                });
            }
        }

        //打开编辑模板名称弹框
        function editTem(){
            var templateName = $("#editTemplate").combobox("getText");

            $("#templateName").val(templateName);
            $('#edit_list_name').dialog('open');
        }

        //保存模板名称
        function saveTemplateName(){
            var newName= $("#templateName").val();
            newName = $.trim(newName);
            if(newName==''){
                layer.alert('请输入模板新名称！', {icon: 0, title: "提示"});
                return ;
            }else{
                $("#editTemplate").combobox("setText",newName);
            }
            saveTem();
            $('#edit_list_name').dialog('close');
        }

    </script>

</body>
</html>

