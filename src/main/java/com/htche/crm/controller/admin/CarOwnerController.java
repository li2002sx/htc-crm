package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarOwnerBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.CarOwner;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarOwnerQuery;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/carowner")
public class CarOwnerController {

    private static final Logger logger = LoggerFactory.getLogger(CarOwnerController.class);

    @Autowired
    CarOwnerBiz carOwnerBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/carowner/list");
        return model;
    }

    @RequestMapping(value = "edit/{carOwnerId:\\d+}")
    public ModelAndView edit(@PathVariable int carOwnerId) {
        ModelAndView model = new ViewResult("custom/carowner/edit");
        CarOwner carOwner = new CarOwner();
        if (carOwnerId > 0) {
            carOwner = carOwnerBiz.selectByPrimaryKey(carOwnerId);
        }
        model.addObject("carOwner", carOwner);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarOwner carOwner) throws ParseException {
        AjaxResult ajaxResult = new AjaxResult();
        String cardNo = carOwner.getCardNo();
        String birthStr = String.format("%s-%s-%s", cardNo.substring(6, 10), cardNo.substring(10, 12), cardNo.substring(12, 14));
        Date birthDay = DateUtils.parseDate(birthStr, "yyyy-MM-dd");
        carOwner.setBirthday(birthDay);
        if (carOwner.getCarOwnerId() == 0) {
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            carOwner.setCarDealerId(adminUser.getRoleId());
        }
        boolean flag = carOwnerBiz.save(carOwner);
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
    public JSONObject listdata(CarOwnerQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();

        Page<CarOwner> carOwnerPage = carOwnerBiz.selectAllList(adminUser.getRoleId(), query.getName(), query.getPageIndex(), query.getPageSize());
        //封装前台数据
        if (carOwnerPage != null && carOwnerPage.getResult() != null) {
            for (CarOwner carOwner : carOwnerPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("carOwnerId", carOwner.getCarOwnerId());
                jsonItem.put("provinceName", regionMap.get(carOwner.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(carOwner.getCityId()));
                jsonItem.put("name", carOwner.getName());
                jsonItem.put("phone", carOwner.getPhone());
                jsonItem.put("address", carOwner.getAddress());
                jsonItem.put("cardNo", carOwner.getCardNo());

                Date buyTime = carOwner.getBuyTime();
                if (buyTime != null) {
                    jsonItem.put("buyTime", DateFormatUtils.format(buyTime, "yyyy-MM-dd"));
                }
                jsonItem.put("buyCarModels", carOwner.getBuyCarModels());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carOwnerPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int carOwnerId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carOwnerBiz.updateStatus(carOwnerId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
