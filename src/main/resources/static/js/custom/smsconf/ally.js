/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    $('#cancel').click(function () {
        history.back();
    });

    // $('#content').artTxtCount($('#cotentTip'), 300);

    $.removeCookie('sms_succ_count');

    var effectiveMobiles = [];

    var effective = 0, invalid = 0;
    $('#mobiles').bind('keyup change blur', function () {
        var mobiles = $('#mobiles').val();
        var mobileArr = mobiles.split(' ');
        for (var i = 0; i < mobileArr.length; i++) {
            var mobile = mobileArr[i].trim();
            if (mobile.length > 0) {
                if (mobile.isMobile()) {
                    if (effectiveMobiles.indexOf(mobile) == -1) {
                        effective++;
                        effectiveMobiles.push(mobile);
                    }
                } else {
                    invalid++;
                }
            }
        }
        $('#effective,#effective2').html(effective);
        $('#invalid').html(invalid);
        effective = 0, invalid = 0;
    });

    //保存
    $('#save').click(function () {
        $('#save').attr('disabled', 'disabled');
        var params = {
            templateId: parseInt($('#templateId').val().trim()) || 0,
            mobiles: effectiveMobiles.join(",")
        };

        if (params.templateId == 0) {
            $.err("请选择一个短信模板");
            return false;
        }

        if (effectiveMobiles.length == 0) {
            $.err("手机号码不能为空");
            return false;
        }

        $.post("/smsconf/sendsms", params);
        var interval = window.setInterval(function () {
            var smsSuccCount = parseInt($.cookie('sms_succ_count')) || 0;
            if (smsSuccCount == effectiveMobiles.length) {
                // $('#save').removeAttr('disabled');
                $.removeCookie('sms_succ_count');
                clearInterval(interval);
            }
            $('#smsSuccCount').html(smsSuccCount);
            $('#progressBar').width(((smsSuccCount * 100) / effectiveMobiles.length).toFixed(0) + "%");
        }, 100);
    })
});