package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.SmsTemplateBiz;
import com.htche.crm.constants.TemplateType;
import com.htche.crm.domain.SmsTemplate;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.SmsTemplateQuery;
import com.htche.crm.util.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/smstemplate")
public class SmsTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(SmsTemplateController.class);

    @Autowired
    SmsTemplateBiz smsTemplateBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/smstemplate/list");
        return model;
    }

    @RequestMapping(value = "edit/{smsTemplateId:\\d+}")
    public ModelAndView edit(@PathVariable int smsTemplateId) {
        ModelAndView model = new ViewResult("custom/smstemplate/edit");
        //枚举
        Map<Integer, String> templateTypeMap = new HashMap<>();
        for (TemplateType type : TemplateType.values()) {
            templateTypeMap.put(type.getIndex(), type.getName());
        }

        model.addObject("templateTypeMap", templateTypeMap);

        SmsTemplate smsTemplate = new SmsTemplate();
        if (smsTemplateId > 0) {
            smsTemplate = smsTemplateBiz.selectByPrimaryKey(smsTemplateId);
        }
        model.addObject("smsTemplate", smsTemplate);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(SmsTemplate smsTemplate) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = smsTemplateBiz.save(smsTemplate);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    /**
     * 获取新闻数据，用于异步加载
     *
     * @param query 查询实体
     * @return
     */
    @RequestMapping(value = "/listdata")
    @ResponseBody
    public JSONObject listdata(SmsTemplateQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Page<SmsTemplate> smsTemplatePage = smsTemplateBiz.selectAllList(query.getPageIndex(), query.getPageSize());
        //封装前台数据
        if (smsTemplatePage != null && smsTemplatePage.getResult() != null) {
            for (SmsTemplate smsTemplate : smsTemplatePage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("templateId", smsTemplate.getTemplateId());
                jsonItem.put("templateTypeName", TemplateType.getName(smsTemplate.getTemplateType()));
                jsonItem.put("status", smsTemplate.getStatus());
                jsonItem.put("content", smsTemplate.getContent());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", smsTemplatePage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "updatestatus", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateStatus(int smsTemplateId, int status) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = smsTemplateBiz.updateStatus(smsTemplateId, status);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
