/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    //Date picker
    $("#buyTime").datepicker({
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
            carOwnerId: parseInt($('#carOwnerId').val()) || 0,
            provinceId: parseInt($('#provinceId').val()) || 0,
            cityId: parseInt($('#cityId').val()) || 0,
            name: $('#name').val().trim(),
            phone: $('#phone').val().trim(),
            address: $('#address').val().trim(),
            buyTime: $('#buyTime').val(),
            buyCarModels: $('#buyCarModels').val().trim(),
            cardNo: $('#cardNo').val().trim(),
            status: 1
        };
        // if (params.provinceId == 0 || params.cityId == 0) {
        //     $.err("请选择地区");
        //     return false;
        // }
        if (params.name.length < 2) {
            $.err("联系人不能为空");
            return false;
        }
        // if (params.phone.length < 3 || !params.phone.isMobile()) {
        //     $.err("联系人手机格式不正确");
        //     return false;
        // }
        // if (params.address.length < 3) {
        //     $.err("联系人地址不能为空");
        //     return false;
        // }
        // if (params.buyTime.length < 3) {
        //     $.err("购车不能为空");
        //     return false;
        // }
        // if (params.buyCarModels.length < 2) {
        //     $.err("购买车型不能为空");
        //     return false;
        // }
        if (params.cardNo.length < 3 || !params.cardNo.isCardNo()) {
            $.err("身份证号格式不正确");
            return false;
        }

        $.post("/carowner/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/carowner/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});