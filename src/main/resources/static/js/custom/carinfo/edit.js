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

    $('#models').cxSelect({
        url: '/cartree/listdata',
        //url: 'js/cityData.min.json',
        selects: ['brand', 'series', 'models'],
        nodata: 'none'
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
            carInfoId: parseInt($('#carInfoId').val()) || 0,
            specId: parseInt($('#specId').val()) || 0,
            brandId: parseInt($('#brandId').val()) || 0,
            seriesId: parseInt($('#seriesId').val()) || 0,
            modelsId: parseInt($('#modelsId').val()) || 0,
            configure: $('#configure').val().trim(),
            frame: $('#frame').val().trim(),
            outColor: $('#outColor').val().trim(),
            inColor: $('#inColor').val().trim(),
            sourceId: parseInt($('#sourceId').val()) || 0,
            procedureId: parseInt($('#procedureId').val()) || 0,
            provinceId: parseInt($('#provinceId').val()) || 0,
            cityId: parseInt($('#cityId').val()) || 0,
            price: parseFloat($('#price').val()) || 0,
            year: parseInt($('#year').val()) || 0,
            month: parseInt($('#month').val()) || 0,
            priceRatio: parseInt($('#priceRatio').val()) || 0,
            status: 1
        };
        if (params.specId == 0) {
            $.err("请选择规格");
            return false;
        }
        if (params.brandId == 0 || params.seriesId == 0 || params.modelsId == 0) {
            $.err("请选择品牌，车系，车型");
            return false;
        }
        if (params.provinceId == 0 || params.cityId == 0) {
            $.err("请选择地区");
            return false;
        }

        $.post("/carinfo/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/carinfo/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});