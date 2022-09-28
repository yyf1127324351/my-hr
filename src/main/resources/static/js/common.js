
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


$.fn.extend({
    pagegrid : function(options) {
        var queryParamFunction = options.getQueryParamFunc;
        if(!$.isFunction(queryParamFunction) && options.searchForm) {
            queryParamFunction = function() {
                var queryParam = {};
                $.each($(options.searchForm).serializeArray(), function(i, e) {
                    if(e.value) {
                        queryParam[e.name] = e.value;
                    }
                });
                return queryParam;
            };

            $(options.searchForm).on("keypress", function(e) {
                if(e.keyCode == 13) {
                    $this.trigger("search");
                    e.preventDefault();
                    e.stopPropagation();
                }
            });
        }

        var paginationSelector = undefined;
        if(typeof(options.pagination) === 'string') {
            paginationSelector = options.pagination;
            options.pagination = false;
        }
        var $this = $(this);
        var defaultOptions = {
            method: 'POST',
            loadMsg: "数据装载中,请稍等....",
            nowrap: false,
            fitColumns: true,
            rownumbers: true,
            striped: true,
            singleSelect: true
        };
        var options = $.extend({}, defaultOptions, options);
        if(paginationSelector && $(paginationSelector).length == 1) {
            var originPageEvent = {
                onBeforeLoad: options.onBeforeLoad || $.noop,
                onLoadSuccess: options.onLoadSuccess || $.noop,
                onLoadError: options.onLoadError || $.noop,
            };
            options = $.extend(options, {
                onBeforeLoad: function (params) {
                    if($(this).datagrid('options').url){
                        $(paginationSelector).pagination("loading");
                    }
                    var result = originPageEvent.onBeforeLoad(params);
                    if(result === false) {
                        $(paginationSelector).pagination("loaded");
                    }
                    return result;
                },
                onLoadSuccess: function (data) {
                    $(paginationSelector).pagination("refresh", {
                        total: data.total
                    });
                    $(paginationSelector).pagination("loaded");
                    return originPageEvent.onLoadSuccess(data);
                },
                onLoadError: function (data) {
                    $(paginationSelector).pagination("loaded");
                    return originPageEvent.onLoadError(data);
                }
            });

            var paginationOptions = {
                total: 0,
                pageNumber: 1,
                pageSize: options.pageSize,
                pageList: options.pageList,
                onSelectPage: tableSearch,
                onRefresh: tableSearch,
                onSelectPage: tableSearch,
                loading:false
            };
            $(paginationSelector).pagination(paginationOptions);
        }

        function tableSearchParam() {
            var data = $.isFunction(queryParamFunction) ? queryParamFunction() : {};
            if(paginationSelector && $(paginationSelector).length == 1) {
                var pagination = $(paginationSelector).pagination("options");
                data = $.extend(data, {
                    page: pagination.pageNumber || 1,
                    rows: pagination.pageSize
                });
            }
            return data;
        }

        function tableSearch() {
            $this.datagrid("reload", tableSearchParam());
        }

        options.queryParams = tableSearchParam();
        $this.on("search", function() {
            if(paginationSelector && $(paginationSelector).length == 1) {
                $(paginationSelector).pagination("select", 1);
            }else {
                tableSearch();
            }
        });
        $this.on("reload", tableSearch);
        return $this.datagrid(options);
    },
    userbox: function(options) {
        var $this = this;

        if($this.selector && typeof($this.selector) !== 'string') {
            console.warn('必须使用选择器！');
            return false;
        }

        var options = $.extend(true, {}, {
            valueSelector: null,
            textSelector: $this.selector,
            valueField: 'id',
            textField: 'userName',
            values: [],
            multiple: false,
            multipleSeparator: ',',
            onSelect: $.noop,
            onClear: $.noop
        }, options);

        var id = (Math.random() * 1000000).toFixed(0);
        var inputId = 'user-input-wrapper' + id;

        var value = null || options.value;
        if(value && options.values.length == 0) {
            options.values.push(singleUserId);
        }

        var $span = $('<span class="userbox-wrapper" style="position:relative;">' +
            '<span class="userbox-remove" style="position: absolute; background: #fff; line-height: 1.3; right: 1px; top: 0.3px; cursor: pointer; width: 1.8em; text-align: center;">&times;</span>' +
            '</span>');
        $span.insertBefore($this);
        $span.find(".userbox-remove").on("click", function() {
            $this.val(null);
            $(options.valueSelector).val(null);
            $(options.textSelector).val(null);
            options.onClear.call($this[0]);
        });
        $span.append($this);
        $this.attr('readonly', 'readonly');
        $this.addClass('user-box');
        $this.css("outline", "none");
        $this.on('click', function() {
            userDialog(options);
        });
    }



});

function userDialog(options) {
    if(window.userDialogId) {
        console.warn("已弹出一个用户选择框！");
        return;
    }

    options = $.extend(true, {}, {
        valueSelector: null,
        textSelector: null,
        valueField: 'id',
        textField: 'userName',
        values: [],
        multiple: false,
        multipleSepartor: ',',
        onSelect: $.noop
    }, options);

    if(!options.defaultParams) {
        options.defaultParams = {
            'status': '1'
        };
    }

    if(options.valueSelector) {
        var val = $(options.valueSelector).val();
        if(!options.multiple) {
            if($.isNumeric(val) && options.values.length == 0) {
                options.values = [ val.trim() ];
            }
        }else if(val && options.values.length == 0) {
            options.values = $.map(val.split(options.multipleSeparator), function(e) { return e * 1; });
            // options.values = "";
        }
    }

    var mapping = {};
    if(options.valueSelector) {
        mapping[options.valueSelector] = options.valueField;
    }
    if(options.textSelector) {
        mapping[options.textSelector] = options.textField;
    }

    var id = (Math.random() * 10000000).toFixed(0);
    var dialogId = "user-dialog-" + id;
    var formId = "user-form-" + id;
    var tableId = "user-table-" + id;
    var pageId = "user-pagination-" + id;
    var selectUserListId = "user-select-list-" + id;

    var southHeight = options.multiple ? 100 : 35;

    var defaultInput = [];
    $.each(options.defaultParams, function(key, value) {
        defaultInput.push('<input type="hidden" name="' + key + '" value="' + value + '"/>')
    });
    window.userDialogId = dialogId;
    $("body").append('<div id="' +  dialogId + '" class="user-dialog">' +
        '<div class="easyui-layout" style="width:100%;height:100%;">' +
        '<div region="north" border="false" class="panel-fit" style="height: auto;padding:5px 0 0;">' +
        '<form id="' + formId + '">' + defaultInput.join("") +
        '   <div class="search-row">' +
        '       <div class="form-group"><label class="search-label">中英文名：</label><input id="userbox-name-'+ id +'" name="userName" class="easyui-textbox" type="text" style="width: 380px;" /></div>' +
        '       <div class="form-group"><a class="easyui-linkbutton search-btn" data-options="iconCls:\'fa fa-search\'" style="height:22px;width: 80px;">搜索</a></div>' +
        '   </div>' +
        '</form>' +
        '</div>' +
        '<div region="south" border="false" style="height:'+ southHeight +'px;padding:3px;overflow: hidden;">' +
        '<div id="' + pageId +'"style="border: 1px solid #ddd;" class="easyui-pagination"></div>' +
        '<div class="clearfix panel-body">' +
        '   <div id="' + selectUserListId +'" style="height:60px;width:425px;float: left;overflow: auto;"></div>' +
        '   <a class="easyui-linkbutton ok-btn" data-options="iconCls:\'icon-ok\'" style="padding: 5px; margin: 15px;float: right;">确定</a>' +
        '</div>' +
        '</div>' +
        '<div region="center" border="false" style="padding:5px 5px;user-select: none;"><table id="' + tableId +'" style="height: 100%;"></table></div>' +
        '</div></div>');
    if(options.values.length > 0) {
        debugger;
        var data = $.extend({}, options.defaultParams);
        switch(options.valueField) {
            case 'id':
                data['ids'] = options.values;
                break;
            case 'userNo':
                data['userNos'] = options.values;
                break;
            default:
                console.warn('暂时只支持id和staffNo两种valueField');
        }
        $.ajax({
            url: "/userInfo/queryUserSearch",
            type: 'POST',
            data: data,
            dataType: 'json',
            async: true,
            success: function (ret) {
                $.each(ret.rows, function() {
                    var item = $('<div class="user-item" style="display: inline-block;padding: 4px; background: #AACCFF;margin:2px; border-radius: 3px;">' + this.userName +
                        ' <a href="javascript:void(0);" class="user-item-del" style="text-decoration: none;">&times;</a>' +
                        '</div>');
                    item.data("id", this.id);
                    item.data("user", this);
                    $("#" + selectUserListId).append(item);
                });
            }
        });
    }

    $("#" + dialogId).find(".easyui-textbox").textbox();
    $("#" + dialogId).find(".easyui-linkbutton").linkbutton();
    $("#" + dialogId).find(".search-btn").on("click", function() {
        $("#" + tableId).trigger("search");
    });

    $("#" + dialogId).find(".ok-btn").on("click", function() {
        var users = [];
        $("#" + selectUserListId + " .user-item").each(function() {
            users.push($(this).data("user"));
        });

        if(options.onSelect) {
            options.onSelect.call($("#" + dialogId)[0], users);
        }

        $.each(mapping, function(key, value) {
            var v = $.map(users, function(e) {
                return e[value];
            }).join(options.multipleSepartor);
            $(key).val(v);
        });
        $("#" + dialogId).dialog("close");
    });

    $("#" + dialogId).find(".easyui-layout").layout();
    $("#" + dialogId).dialog({
        title:'人员选择' + (options.multiple ? "" : "&nbsp;(双击选中)"),
        width: 550,
        height: (options.multiple ? 470 : 420),
        iconCls:'icon-man',
        shadow:false,
        modal:true,
        onOpen: function() {
            $("#userbox-name-" + id).textbox('textbox').focus();
            var isFirst = true;
            $("#" + tableId).pagegrid({
                url: '/userInfo/queryUserSearch',
                searchForm: "#" + formId,
                pagination: "#" + pageId,
                pageSize: 30,
                singleSelect: !options.multiple,
                idField: "id",
                columns:[[
                    {field:'userName',title:'姓名',width: 150},
                    {field:'departmentName',title:'员工所在部门',width:350}
                ]],
                onBeforeLoad: function(params) {
                    if(params["userName"]) {

                    }else {
                        if(isFirst) {
                            isFirst = false;
                        }else {
                            $.messager.alert("提示", "中英文名不能为空！");
                            $("#" + tableId).datagrid("loadData", { total: 0, rows: []});
                        }
                        return false;
                    }
                },
                onSelect: function(rowIndex, rowData) {
                    var exists = false;
                    $("#" + selectUserListId).find(".user-item").each(function() {
                        var userId = $(this).data("id");
                        if(userId == rowData.id) {
                            exists = true;
                            return false;
                        }
                    });
                    if(!exists) {
                        var item = $('<div class="user-item" style="display: inline-block;padding: 4px; background: #AACCFF;margin:2px; border-radius: 3px;">' + rowData.userName +
                            ' <a href="javascript:void(0);" class="user-item-del" style="text-decoration: none;">&times;</a>' +
                            '</div>');
                        item.data("id", rowData.id);
                        item.data("user", rowData);
                        $("#" + selectUserListId).append(item);
                    }
                },
                onUnselect: function(rowIndex, rowData) {
                    $("#" + selectUserListId).find(".user-item").each(function() {
                        var userId = $(this).data("id");
                        if(userId == rowData.id) {
                            $(this).remove();
                        }
                    });
                },
                onLoadSuccess: function(ret) {
                    var rows = $("#" + tableId).datagrid("getRows");
                    $("#" + selectUserListId).find(".user-item").each(function() {
                        var userId = $(this).data("id");
                        $.each(rows, function() {
                            if(this.id == userId) {
                                var rowIndex = $("#" + tableId).datagrid("getRowIndex", this);
                                if(rowIndex >= 0) {
                                    $("#" + tableId).datagrid("selectRow", rowIndex);
                                }
                            }
                        });
                    });
                },
                onDblClickRow: function(rowIndex, rowData) {
                    if(!options.multiple) {
                        if(options.onSelect) {
                            options.onSelect.call($("#" + dialogId)[0], rowData);
                        }

                        $.each(mapping, function(key, value) {
                            $(key).val(rowData[value]);
                        });
                        $("#" + dialogId).dialog("close");
                    }
                }
            });
        },
        onClose:function() {
            window.userDialogId = null;
            $("#" + dialogId).remove();
        }
    });

    $("#"+ selectUserListId).on("click", ".user-item-del", function() {
        var item = $(this).closest(".user-item");
        var id = item.data("id");
        var rows = $("#" + tableId).datagrid("getSelections");
        $.each(rows, function() {
            if(this.id == id) {
                var rowIndex = $("#" + tableId).datagrid("getRowIndex", this);
                if(rowIndex >= 0) {
                    $("#" + tableId).datagrid("unselectRow", rowIndex);
                }
            }
        });
        item.remove();
    });
}