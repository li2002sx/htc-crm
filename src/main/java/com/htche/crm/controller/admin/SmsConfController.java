package com.htche.crm.controller.admin;

import com.htche.crm.biz.SmsBiz;
import com.htche.crm.biz.SmsConfBiz;
import com.htche.crm.biz.SmsTemplateBiz;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.SmsConf;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.util.CookieUtil;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/smsconf")
public class SmsConfController {

    private static final Logger logger = LoggerFactory.getLogger(SmsConfController.class);

    @Autowired
    SmsConfBiz smsConfBiz;

    @Autowired
    SmsTemplateBiz smsTemplateBiz;

    @Autowired
    SmsBiz smsBiz;

    @RequestMapping(value = "edit")
    public ModelAndView edit() {
        ModelAndView model = new ViewResult("custom/smsconf/edit");
        SmsConf sms = new SmsConf();
        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
        int carDealerId = adminUser.getRoleId();
        sms = smsConfBiz.selectByCarDealerId(carDealerId);
        model.addObject("smsConf", sms);

        //模板Map
        Map<Integer, String> templateMap = smsTemplateBiz.getAllMap(1);
        model.addObject("templateMap", templateMap);

        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(SmsConf sms) {
        AjaxResult ajaxResult = new AjaxResult();
        if (sms.getSmsId() == 0) {
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            sms.setCarDealerId(adminUser.getRoleId());
        }
        boolean flag = smsConfBiz.save(sms);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "ally")
    public ModelAndView ally(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = new ViewResult("custom/smsconf/ally");
        CookieUtil.removeCookie(request, response, "sms_succ_count");
        //模板Map
        Map<Integer, String> templateMap = smsTemplateBiz.getAllMap(2);
        model.addObject("templateMap", templateMap);
        return model;
    }

    @RequestMapping(value = "sendsms", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult sendSms(int templateId, String mobiles, HttpServletResponse response) {
        AjaxResult ajaxResult = new AjaxResult();
        smsBiz.sendSms(templateId, mobiles, response);
        ajaxResult.setSuccess(true);
        return ajaxResult;
    }
}
