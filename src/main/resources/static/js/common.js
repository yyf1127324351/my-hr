
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
            $("#template").combobox("reload");
            $('#new_columnWindow').dialog("destroy");
        },
        closable: true,
        closed: true   //已关闭
    });

    $('#new_columnWindow').dialog('open');
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