/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    $("#updatePass,#updatePass2").click(function () {
        var sb = new StringBuilder();
        sb.append('<div class="form-horizontal">');
        sb.append('    <div class="form-group">');
        sb.append('        <label class="col-sm-3 control-label">请输入原密码：</label>');
        sb.append('        <div class="col-sm-5">');
        sb.append('            <input class="form-control" id="oldPassword" type="password">');
        sb.append('        </div>');
        sb.append('    </div>');
        sb.append('    <div class="form-group">');
        sb.append('        <label class="col-sm-3 control-label">请输入新密码：</label>');
        sb.append('        <div class="col-sm-5">');
        sb.append('            <input class="form-control" id="newPassword" type="password">');
        sb.append('        </div>');
        sb.append('    </div>');
        sb.append('    <div class="form-group">');
        sb.append('        <label class="col-sm-3 control-label">请确认新密码：</label>');
        sb.append('        <div class="col-sm-5">');
        sb.append('            <input class="form-control" id="rePassword" type="password" >');
        sb.append('        </div>');
        sb.append('    </div>');
        sb.append('</div>');

        $confirm('修改密码', sb.toString(), function (dlg) {
            var rePassword = $("#rePassword").val();
            var params = {
                oldPwd: $("#oldPassword").val(),
                newPwd: $("#newPassword").val(),

            };
            if (params.newPwd == "" || rePassword == "") {
                $.err('密码不能为空');
                return false;
            }
            if (params.newPwd != rePassword) {
                $.err('两次输入的密码不一致！');
                return false;
            }
            if ((params.newPwd.length < 6 ) || ( rePassword.length < 6)) {
                $.err('密码长度至少六位，请重新设置密码！');
                return false;
            }
            $.post("/adminuser/updatepassword", params, function (result) {
                if (result.success) {
                    dlg.close();
                    $alert("提示", "修改成功！");
                } else {
                    $alert("提示", result.error);
                }
            });
        });
    });
});