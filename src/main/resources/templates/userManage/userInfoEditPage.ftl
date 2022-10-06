<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员信息编辑</title>
    <#include "/common/common.ftl"/>
    <style>
        .basic-left{
            width:99%;
            cursor:pointer;
            text-align:center;
            line-height:35px;
            border:1px solid #DDDDDD;
        }
        .basic-on{
            background:#E2E2E2
        }
        a{
            color:#000000
        }
    </style>
</head>
<body class="easyui-layout" style="margin-top:4px;margin-left:4px;">
    <!-- 首页左边Div  begin-->
    <div data-options="region:'west',split:true"  style="width:140px;">
        <div class="basic-left basic-on" type="BaseInfo">
            <a style="width:100%">基本信息</a>
        </div>
        <div class="basic-left " type="ContractTimeInfo">
            <a style="width:100%">合同信息</a>
        </div>
    </div>

    <div data-options="region:'center',border:false" style="overflow: hidden;">
        <h3 style="margin-left: 8px;">查询日期： <span style="color: #3498DB">${queryDate}</span></h3>
        <iframe id="_iframe" name="_iframe" frameborder="0" style="width:100%;height:98%"
                src="/userInfo/goEditInfoPage?pageType=BaseInfo&id=${id}&queryDate=${queryDate}"></iframe>
    </div>
    <!-- 首页左边Div  end-->
</body>

<script type="text/javascript">
    var pageType="";
    $(function(){
        $('.basic-left').on('click',function(){
            $('.basic-on').removeClass('basic-on');
            $(this).addClass('basic-on');
            pageType = $(this).attr('type');
            $('iframe').attr('src','/userInfo/goEditInfoPage?id=${id}&queryDate=${queryDate}&pageType='+pageType);
        });
    });
</script>
</html>