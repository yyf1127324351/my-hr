
function showColumnWindow(fieldType,templateComboboxId) {
    $("#columnWindow").append("<div id='new_columnWindow'></div>");//创建一个临时层，关闭销毁。

    var fieldTemplateUserId = 0;
    var fieldTemplateText = $("#template").combobox("getText");
    if (fieldTemplateText != '' && fieldTemplateText != "通用模板") {
        fieldTemplateUserId = $("#template").combobox("getValue");
    }

    $('#new_columnWindow').dialog({
        title:'设置展示列模板',
        width:'590px',
        height:'415px',
        resizable : true,
        iconCls:'icon-edit',
        href:'/columnField/goColumnFieldTemplateEditPage?fieldType='+fieldType+'&fieldTemplateUserId='+fieldTemplateUserId,
        shadow:false,
        top: '10%',
        left: '20%',
        modal:true,
        onClose:function(){
            if($("#filed_save").val()!=''){
                //修改过模板--需要刷新页面
                //window.parent.window.openTab(name,url);
                //刷新列表和模板下拉框
                $.ajax({
                    url: '/filed/queryListtemplate',
                    data:{'table':table},
                    type: 'post',
                    dataType: "json",
                    success: function (returnValue) {//异步获取要动态生成的列 别名，宽度也可以
                        var arr = returnValue.columns;
                        for(var i=0;arr!=null&&i<arr.length;i++){
                            $("#z_"+arr[i].code).text(arr[i].name);
                        }
                    }
                });
                if($("#"+templateComboboxId).hasClass("easyui-combobox")) {
                    $("#"+templateComboboxId).combobox("reload");
                }else {
                    loadColumn($("#"+id).find("option:selected").attr("value"));
                }
            }
            $('#new_columnWindow').dialog("destroy");
        },
        closable: true,
        closed: true   //已关闭
    });

    $('#new_columnWindow').dialog('open');
}


//
// $(function() {
//     // datagrid数据表格ID
//     var datagridId = 'data_table';
//     // 第一次加载时自动变化大小
//     //$('#' + datagridId).resizeDataGrid(0, 0, 0, 0);
//     // 当窗口大小发生变化时，调整DataGrid的大小
//     $(window).resize(function() {
//         $('#' + datagridId).resizeDialog(0, 0, 0, 0);
//     });
// });
//
// $.fn.extend({
//     resizeDialog : function(heightMargin, widthMargin, minHeight, minWidth) {
//         var height = $(document.body).height() - heightMargin;
//         var width = $(document.body).width() - widthMargin;
//         height = height < minHeight ? minHeight : height;
//         width = width < minWidth ? minWidth : width;
//         try{
//             $(this).datagrid('resize', {
//                 width : width
//             });
//         }catch(e){};
//     }
// });


