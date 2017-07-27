package com.htche.crm.controller.rest;

import com.htche.crm.biz.SmsBiz;
import com.htche.crm.constants.SmsType;
import com.htche.crm.model.ApiResult;
import com.htche.crm.util.RandUtil;
import com.htche.crm.util.geetest.GeetestConfig;
import com.htche.crm.util.geetest.GeetestLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lishengxi on 2017/7/27.
 */
@RestController
@RequestMapping("/rest/sms")
public class SmsRestController {

    private static final Logger logger = LoggerFactory.getLogger(SmsRestController.class);

    @Autowired
    SmsBiz smsBiz;

    @RequestMapping(value = "smscode", method = RequestMethod.POST)
    public ApiResult smsCode(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "smsType", required = true) int smsType,
            HttpServletRequest request
    ) {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        //拖动验证码验证
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

        //从session中获取userid
        String userId = (String) request.getSession().getAttribute("userId");

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userId);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        if (gtResult == 1) {
            // 验证成功
            smsBiz.sendMsgCode(mobile, smsType);
            apiResult.setStatus(1);
        } else {
            // 验证失败
            apiResult.setMessage("验证失败");
        }
        return apiResult;
    }

    private static final String _GeetestPrefix = "cheyuan";

    @RequestMapping(value = "register2", method = RequestMethod.GET)
    public String geetestStart(HttpServletRequest request, HttpServletResponse response) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String resStr = "{}";

        //自定义userId
        String userId = String.format("%s-%s", _GeetestPrefix, RandUtil.stringId(5));

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(userId);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将userid设置到session中
        request.getSession().setAttribute("userId", userId);

        resStr = gtSdk.getResponseStr();

        return resStr;
    }
}
