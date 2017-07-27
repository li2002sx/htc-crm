/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {

    $('#cancel').click(function () {
        history.back();
    });

    //图片上传
    var picUrlArr = [];
    if ($.trim($("#picUrl").val()) != "") {
        var pic = "<img src='" + $.trim($("#realPicUrl").val()) + "' class='.file-preview' style='width:200px;height:150px;'>";
        picUrlArr.push(pic);
        var realPicUrl2 = $.trim($("#realPicUrl2").val());
        if (realPicUrl2.length > 0) {
            pic = "<img src='" + realPicUrl2 + "' class='.file-preview' style='width:200px;height:150px;'>";
            picUrlArr.push(pic);
        }
    }
    $("#picUrl_file").fileinput({
        uploadUrl: "/upload",
        maxFileSize: 2048,
        minFileCount: 1,
        maxFileCount: 2,
        resizeImage: true,
        language: 'zh', //设置语言
        allowedFileExtensions: ['JPG', 'JPEG', 'GIF', 'PNG'],//接收的文件后缀
        showUpload: true, //是否显示上传按钮
        showRemove: true,//
        showCaption: true,//是否显示标题
        previewFileIcon: '==i class="fa fa-file">==/i>',
        initialPreview: picUrlArr,
        uploadExtraData: function (previewId, index) {   //额外参数的关键点
            var obj = {
                cate: 'template',
                subCate: carModelId
            };
            return obj;
        }
    }).on("fileuploaded", function (event, data, previewId, index) {
        var result = data.response;
        if (result.success) {
            if (index == 0) {
                $('#picUrl').val(result.value);
            } else {
                $('#picUrl2').val(result.value);
            }
        } else {
            $alert("提示", result.error);
        }
    });//初始化

    //保存
    $('#save').click(function () {
        var params = {
            moduleInfoId: parseInt($('#moduleInfoId').val()) || 0,
            moduleId: parseInt($('#moduleId').val()) || 0,
            title: $('#title').val().trim(),
            picUrl: $('#picUrl').val(),
            picUrl2: $('#picUrl2').val(),
            url: $('#url').val().trim(),
            orderNo: parseInt($('#orderNo').val()) || 0,
            status: 1
        };
        if (params.title.length == 0) {
            $.err("标题不能为空");
            return false;
        }

        $.post("/moduleinfo/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/moduleinfo/list?moduleid=" + params.moduleId + "&modelid=" + carModelId;
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});