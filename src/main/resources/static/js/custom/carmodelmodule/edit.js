/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {

    $('.my-colorpicker2').colorpicker();

    $('#cancel').click(function () {
        history.back();
    });

    //保存
    $('#save').click(function () {
        var params = {
            moduleId: parseInt($('#moduleId').val()) || 0,
            carModelId: parseInt($('#carModelId').val()) || 0,
            title: $('#title').val().trim(),
            fontSize: parseInt($('#fontSize').val().trim()) || 0,
            color: $('#color').val().trim(),
            status: 1
        };
        if (params.title.length == 0) {
            $.err("标题不能为空");
            return false;
        }

        $.post("/carmodule/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/carmodule/list?modelid=" + params.carModelId;
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});