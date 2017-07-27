/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    $('#cancel').click(function () {
        history.back();
    });

    // $('#content').artTxtCount($('#cotentTip'), 300);

    //保存
    $('#save').click(function () {
        var params = {
            smsId: parseInt($('#smsId').val()) || 0,
            templateId: parseInt($('#templateId').val().trim()) || 0,
            sendHour: parseInt($('input[name="sendHour"]:checked').val()) || 0,
            sendMinute: 0,
            status: 1
        };
        if (params.templateId == 0) {
            $.err("请选择一个短信模板");
            return false;
        }

        if (params.sendHour == 0) {
            $.err("请选择一个发送时间");
            return false;
        }

        $.post("/smsconf/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.reload();
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});