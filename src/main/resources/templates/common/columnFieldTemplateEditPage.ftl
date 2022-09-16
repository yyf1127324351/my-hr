<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <title>设置展示列模板页面</title>
</head>
<body class="easyui-layout">
	<input type="hidden" id="fieldType" value="${fieldType}" />
    <input type="hidden" id="fieldTemplateUserId" value="${fieldTemplateUserId}" />

	<input type="hidden" id="field_save"/>
	<div region="north" border="false" style=" padding: 5px">
		<div class="" align="center">
			<table border="0" width="100%" class="search_table">
				<tr>
					<td width="20%" align='right'>展示列模板：</td>
					<td width="20%" align='left'>
                        <input id="editTemplate" class="easyui-combobox" style="width: 200px;">
					</td>
					<td width="60%" align='left'>
						<a href="javascript:saveTem();"style="width: 100px;" class="easyui-linkbutton">保存</a>
						<a href="javascript:editTem();" style="width: 120px;" class="easyui-linkbutton">修改模板名称</a>
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
		<div style="display:none">
		   <div id="edit_list_name" class="dialog" title="修改模板名称" style="width:220px; height:130px;">
				<table style="width:auto;margin:10px 10px 0 20px;">
					<tr style="height:30px;">
						<td>名称</td>
						<td style="align:center"><input class="easyui-textbox"  id="template_name"  type="text" style="width: 200px;" maxlength=20></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

    <script type="text/javascript">
        $(document).ready(function () {

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
                    debugger;
                    var fieldTemplateUserId = $("#fieldTemplateUserId").val();
                    if (fieldTemplateUserId != null && fieldTemplateUserId > 0) {
                        $(this).combobox("setValue", fieldTemplateUserId);
                    }else {
                        var data = $(this).combobox("getData");
                        if (data && data.length > 0) {
                            fieldTemplateUserId = data[0].id;
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
                            var option ="<option value='"+noshowField[i].field+"'>"+noshowField[i].name+"</option>";
                            $("#noshow_select").append(option);
                        }
                        for(var i=0;showField!=null&& i<showField.length;i++){
                            var option ="<option value='"+showField[i].field+"'>"+showField[i].name+"</option>";
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



    </script>

</body>
</html>

