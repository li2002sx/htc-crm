/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    //Date picker
    $("#registTime,#expireTime").datepicker({
        //minView: "month", //选择日期后，不会再跳转去选择时分秒
        format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式
        language: 'zh-CN', //汉化
        autoclose: true //选择日期后自动关闭
    });

    $('#region').cxSelect({
        url: '/region/listdata',
        //url: 'js/cityData.min.json',
        selects: ['province', 'city'],
        nodata: 'none'
    });

    $('#cancel').click(function () {
        history.back();
    });

    //保存
    $('#save').click(function () {
        var params = {
            contactId: parseInt($('#contactId').val()) || 0,
            provinceId: parseInt($('#provinceId').val()) || 0,
            cityId: parseInt($('#cityId').val()) || 0,
            name: $('#name').val().trim(),
            phone: $('#phone').val().trim(),
            businessId: parseInt($('#businessId').val()) || 0,
            status: 1
        };
        if (params.provinceId == 0 || params.cityId == 0) {
            $.err("请选择地区");
            return false;
        }
        if (params.name.length < 2) {
            $.err("联系人不能为空");
            return false;
        }
        if (params.phone.length < 3) {
            $.err("联系人电话不能为空");
            return false;
        }
        if (params.businessId == 0) {
            $.err("主营业务不能为空");
            return false;
        }

        $.post("/contact/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/contact/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});