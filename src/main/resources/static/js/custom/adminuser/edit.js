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
            adminUserId: parseInt($('#adminUserId').val()) || 0,
            userName: $('#userName').val().trim(),
            roleId: parseInt($('#roleId').val()) || 0,
            remark: $('#remark').val().trim(),
            status: 1
        };
        if (params.userName.length < 3) {
            $.err("名称不能为空");
            return false;
        }

        $.post("/adminuser/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！账户密码为:" + result.value + "，请手工记录", function () {
                    location.href = "/adminuser/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});