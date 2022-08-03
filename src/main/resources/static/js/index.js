/*easyui样式初始化*/
$.fn.tabs.defaults.tabHeight = 32; //tab标签条高度
$.fn.linkbutton.defaults.height = 32; //按钮高度
$.fn.menu.defaults.itemHeight = 28; //menu高度

$.fn.validatebox.defaults.height = 32;
$.fn.textbox.defaults.height = 32;
$.fn.textbox.defaults.iconWidth = 24;
$.fn.datebox.defaults.height = 32;
$.fn.numberspinner.defaults.height = 32;
$.fn.timespinner.defaults.height = 32;
$.fn.numberbox.defaults.height = 32;
$.fn.combobox.defaults.height = 32;
// $.fn.passwordbox.defaults.height = 32;
$.fn.filebox.defaults.height = 32;

$.fn.menu.defaults.noline = true
$.fn.progressbar.defaults.height = 18; //进度条

$(function() {
    /*左侧导航分类选中样式*/
    $(".panel-body .accordion-body>ul>li").on('click', function() {
        $('.level2>li').removeClass('super-accordion-selected');
        // $(this).siblings().removeClass('super-accordion-selected');
        $(this).addClass('super-accordion-selected');

        //新增一个选项卡
        var tabUrl = $(this).data('url');
        var tabTitle = $(this).text();
        //tab是否存在
        if($("#tabs").tabs('exists', tabTitle)) {
            $("#tabs").tabs('select', tabTitle);
        } else {
            openTab(tabTitle,tabUrl);
        }
    });

    /*设置按钮的下拉菜单*/
    $('.super-setting-icon').on('click', function() {
        $('#setDiv').menu('show', {
            top: 50,
            left: document.body.scrollWidth
        });
    });


});
$.parser.onComplete = function() {
    $("#index").css('opacity', 1);
}


function openTab(text,url){
    if($('#tabs').tabs('exists',text)){
        $('#tabs').tabs('select',text);
        var tab = $('#tabs').tabs('getSelected');
        var url1 = $(tab.panel('options').content).attr('src');
        if(url!=""&&url!=null){
            url1=url;
        }
        $('#tabs').tabs('update',{
            tab:tab,
            height: '100%',
            options : {
                content : '<iframe scrolling="auto" id="functionIframe" frameborder="0" src="'+url1+'" style="width:100%;height:99%;"></iframe>'
            }
        });
    }else{
        $('#tabs').tabs('add',{
            title : text,
            closable : true,
            height: '100%',
            content : '<iframe scrolling="auto" id="functionIframe" frameborder="0" src="'+url+'" style="width:100%;height:99%;"></iframe>'
        })
    }
}
function closeTab(text){
    if($('#tabs').tabs('exists',text)){
        $('#tabs').tabs('close',text);
    }
}