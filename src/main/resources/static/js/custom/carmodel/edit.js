/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {

    $('#models').cxSelect({
        url: '/cartree/listdata',
        //url: 'js/cityData.min.json',
        selects: ['brand', 'series', 'models'],
        nodata: 'none'
    });

    $('#cancel').click(function () {
        history.back();
    });

    //图片上传
    var picUrlArr = [];
    if ($.trim($("#picUrl").val()) != "") {
        var pic = "<img src='" + $.trim($("#realPicUrl").val()) + "' class='.file-preview' style='width:240px;height:180px;'>";
        picUrlArr.push(pic);
    }
    $("#picUrl_file").fileinput({
        uploadUrl: "/upload",
        maxFileSize: 2048,
        minFileCount: 0,
        maxFileCount: 1,
        resizeImage: true,
        language: 'zh', //设置语言
        allowedFileExtensions: ['JPG', 'JPEG', 'GIF', 'PNG'],//接收的文件后缀
        showUpload: false, //是否显示上传按钮
        showRemove: false,//
        showCaption: false,//是否显示标题
        previewFileIcon: '==i class="fa fa-file">==/i>',
        initialPreview: picUrlArr
    }).on("fileuploaded", function (event, data, previewId, index) {
        var result = data.response;
        if (result.success) {
            $("#picUrl").val(result.value);
        } else {
            $alert("提示", result.error);
        }
    });//初始化

    //保存
    $('#save').click(function () {
        var params = {
            carModelId: parseInt($('#carModelId').val()) || 0,
            specId: parseInt($('#specId').val()) || 0,
            brandId: parseInt($('#brandId').val()) || 0,
            seriesId: parseInt($('#seriesId').val()) || 0,
            modelsId: parseInt($('#modelsId').val()) || 0,
            picUrl: $('#picUrl').val().trim(),
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

        $.post("/carmodel/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/carmodel/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});