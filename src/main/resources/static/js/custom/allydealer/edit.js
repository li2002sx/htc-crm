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
            allyDealerId: parseInt($('#allyDealerId').val()) || 0,
            companyName: $('#companyName').val().trim(),
            provinceId: parseInt($('#provinceId').val()) || 0,
            cityId: parseInt($('#cityId').val()) || 0,
            name: $('#name').val().trim(),
            phone: $('#phone').val().trim(),
            address: $('#address').val().trim(),
            business: $('#business').val().trim(),
            status: 1
        };
        if (params.companyName.length < 3) {
            $.err("企业名称不能为空");
            return false;
        }
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
        if (params.address.length < 3) {
            $.err("联系人地址不能为空");
            return false;
        }
        if (params.business.length < 2) {
            $.err("主营业务不能为空");
            return false;
        }

        $.post("/allydealer/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/allydealer/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});