/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    $('#cancel').click(function () {
        history.back();
    });

    $('#content').artTxtCount($('#contentTip'), 64);

    //保存
    $('#save').click(function () {
        var params = {
            templateId: parseInt($('#templateId').val()) || 0,
            templateType: parseInt($('input[name="templateType"]:checked').val()) || 0,
            content: $('#content').val().trim(),
            status: 0
        };

        if (params.templateType == 0) {
            $.err("请选择一个模板类型");
            return false;
        }

        if (params.content.length < 3) {
            $.err("内容不能为空");
            return false;
        }

        $.post("/smstemplate/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/smstemplate/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});