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
    }
    $("#picUrl_file").fileinput({
        uploadUrl: "/upload",
        maxFileSize: 2048,
        minFileCount: 1,
        maxFileCount: 1,
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
            $('#picUrl').val(result.value);
        } else {
            $alert("提示", result.error);
        }
    });//初始化

    //保存
    $('#save').click(function () {

        var moduleIds = [];
        $("input[name='moduleId']:checked").each(function () {
            moduleIds.push($(this).val());
        });

        var params = {
            templateId: parseInt($('#templateId').val()) || 0,
            carModelId: carModelId,
            title: $('#title').val().trim(),
            bgPicUrl: $('#picUrl').val(),
            moduleIds: moduleIds.join(","),
            status: 1
        };
        if (params.title.length == 0) {
            $.err("标题不能为空");
            return false;
        }

        if (moduleIds.length == 0) {
            $.err("必须选择模块");
            return false;
        }


        $.post("/cartemplate/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/cartemplate/list?modelid=" + carModelId;
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});