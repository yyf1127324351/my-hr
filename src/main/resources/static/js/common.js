var sexJson = [ {
    id : 1,
    description : '男'
}, {
    id : 2,
    description : '女'
} ];



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



Date.prototype.format = function(format){
    var b = formatDate(this,format);
    if(dayLightSaving(b)){
        var adjust = new Date(this.getTime()+3600000);
        b = formatDate(adjust,format);
    }
    return b;
}

//中国夏令时处理
function dayLightSaving(dateStr){
    if((dateStr>='1986-05-03'&&dateStr<='1986-09-14')||
        (dateStr>='1987-04-11'&&dateStr<='1987-09-13')||
        (dateStr>='1988-04-09'&&dateStr<='1988-09-11')||
        (dateStr>='1989-04-15'&&dateStr<='1989-09-17')||
        (dateStr>='1990-04-14'&&dateStr<='1990-09-16')||
        (dateStr>='1991-04-13'&&dateStr<='1991-09-15')){
        return true;
    }
    return false;
}

function formatDate(date,format){
    var o = {
        "M+" : date.getMonth()+1,
        "d+" : date.getDate(),
        "h+" : date.getHours(),
        "m+" : date.getMinutes(),
        "s+" : date.getSeconds(),
        "q+" : Math.floor((date.getMonth()+3)/3),
        "S" : date.getMilliseconds()
    }
    if(/(y+)/.test(format)){
        format=format.replace(RegExp.$1,(date.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(format)) {
            format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
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