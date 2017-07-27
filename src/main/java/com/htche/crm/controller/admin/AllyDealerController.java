package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.AllyDealerBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.AllyDealer;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.AllyDealerQuery;
import com.htche.crm.util.CurrentUser;
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
@RequestMapping("/allydealer")
public class AllyDealerController {

    private static final Logger logger = LoggerFactory.getLogger(AllyDealerController.class);

    @Autowired
    AllyDealerBiz allyDealerBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/allydealer/list");
        return model;
    }

    @RequestMapping(value = "edit/{allyDealerId:\\d+}")
    public ModelAndView edit(@PathVariable int allyDealerId) {
        ModelAndView model = new ViewResult("custom/allydealer/edit");
        AllyDealer allyDealer = new AllyDealer();
        if (allyDealerId > 0) {
            allyDealer = allyDealerBiz.selectByPrimaryKey(allyDealerId);
        }
        model.addObject("allyDealer", allyDealer);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(AllyDealer allyDealer) {
        AjaxResult ajaxResult = new AjaxResult();
        if (allyDealer.getAllyDealerId() == 0) {
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            allyDealer.setCarDealerId(adminUser.getRoleId());
        }
        boolean flag = allyDealerBiz.save(allyDealer);
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
    public JSONObject listdata(AllyDealerQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();

        Page<AllyDealer> allyDealerPage = allyDealerBiz.selectAllList(adminUser.getRoleId(), query.getCompanyName(), query.getName(), query.getPageIndex(), query.getPageSize());
        //封装前台数据
        if (allyDealerPage != null && allyDealerPage.getResult() != null) {
            for (AllyDealer allyDealer : allyDealerPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("allyDealerId", allyDealer.getAllyDealerId());
                jsonItem.put("companyName", allyDealer.getCompanyName());
                jsonItem.put("provinceName", regionMap.get(allyDealer.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(allyDealer.getCityId()));
                jsonItem.put("name", allyDealer.getName());
                jsonItem.put("phone", allyDealer.getPhone());
                jsonItem.put("address", allyDealer.getAddress());
                jsonItem.put("business", allyDealer.getBusiness());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", allyDealerPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int allyDealerId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = allyDealerBiz.updateStatus(allyDealerId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
