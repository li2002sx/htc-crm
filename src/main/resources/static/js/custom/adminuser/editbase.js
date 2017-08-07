/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    $('#cancel').click(function () {
        history.back();
    });

    //保存
    $('#save').click(function () {
        var params = {
            companyName: $('#companyName').val().trim(),
            picUrl: $('#picUrl').val().trim()
        };
        if (params.companyName.length < 3) {
            $.err("公司名称不能为空");
            return false;
        }

        $.post("/adminuser/savebase", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.reload();
                });
            } else {
                $alert("提示", result.error);
            }
        });
    });

    //图片上传
    var picUrlArr = [];
    if ($.trim($("#picUrl").val()) != "") {
        var pic = "<img src='" + $.trim($("#realPicUrl").val()) + "' class='.file-preview' style='width:200px;height:200px;'>";
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
        initialPreview: picUrlArr,
        uploadExtraData: function (previewId, index) {   //额外参数的关键点
            var obj = {
                cate: 'admin',
                subCate: 'user'
            };
            return obj;
        }
    }).on("fileuploaded", function (event, data, previewId, index) {
        var result = data.response;
        if (result.success) {
            $("#picUrl").val(result.value);
        } else {
            $alert("提示", result.error);
        }
    });//初始化
});