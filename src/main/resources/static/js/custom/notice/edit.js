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
            noticeId: parseInt($('#noticeId').val()) || 0,
            title: $('#title').val().trim(),
            content: $('#content').val().trim(),
            status: 1
        };
        if (params.title.length < 3) {
            $.err("标题不能为空");
            return false;
        }

        $.post("/notice/save", params, function (result) {
            if (result.success) {
                $alert("提示", "保存成功！", function () {
                    location.href = "/notice/list";
                });
            } else {
                $alert("提示", result.error);
            }
        });
    })
});