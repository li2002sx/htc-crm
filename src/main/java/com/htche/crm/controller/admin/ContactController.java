package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.ContactBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.Contact;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.ContactQuery;
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

import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/contact")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    ContactBiz contactBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/contact/list");
        return model;
    }

    @RequestMapping(value = "edit/{contactId:\\d+}")
    public ModelAndView edit(@PathVariable int contactId) {
        ModelAndView model = new ViewResult("custom/contact/edit");
        Contact contact = new Contact();
        if (contactId > 0) {
            contact = contactBiz.selectByPrimaryKey(contactId);
        }
        model.addObject("contact", contact);
        model.addObject("businessMap", categoryBiz.getCategoryMap(4));
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(Contact contact) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = contactBiz.save(contact);
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
    public JSONObject listdata(ContactQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Map<Integer, String> businessMap = categoryBiz.getCategoryMap(4);

        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        Page<Contact> contactPage = contactBiz.selectAllList(query);
        //封装前台数据
        if (contactPage != null && contactPage.getResult() != null) {
            for (Contact contact : contactPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("contactId", contact.getContactId());
                jsonItem.put("provinceName", regionMap.get(contact.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(contact.getCityId()));
                jsonItem.put("name", contact.getName());
                jsonItem.put("phone", contact.getPhone());
                jsonItem.put("businessId", contact.getBusinessId());
                jsonItem.put("businessName", businessMap.get(contact.getBusinessId()));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", contactPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int contactId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = contactBiz.updateStatus(contactId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
