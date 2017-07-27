package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarDealerBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarDealer;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarDealerQuery;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/cardealer")
public class CarDealerController {

    private static final Logger logger = LoggerFactory.getLogger(CarDealerController.class);

    @Autowired
    CarDealerBiz carDealerBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/cardealer/list");
        return model;
    }

    @RequestMapping(value = "edit/{carDealerId:\\d+}")
    public ModelAndView edit(@PathVariable int carDealerId) {
        ModelAndView model = new ViewResult("custom/cardealer/edit");
        CarDealer carDealer = new CarDealer();
        if (carDealerId > 0) {
            carDealer = carDealerBiz.selectByPrimaryKey(carDealerId);
        }
        model.addObject("carDealer", carDealer);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarDealer carDealer) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carDealerBiz.save(carDealer);
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
    public JSONObject listdata(CarDealerQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;
        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        Page<CarDealer> carDealerPage = carDealerBiz.selectAllList(query);
        //封装前台数据
        if (carDealerPage != null && carDealerPage.getResult() != null) {
            for (CarDealer carDealer : carDealerPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("carDealerId", carDealer.getCarDealerId());
                jsonItem.put("companyName", carDealer.getCompanyName());
                jsonItem.put("provinceName", regionMap.get(carDealer.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(carDealer.getCityId()));
                jsonItem.put("name", carDealer.getName());
                jsonItem.put("phone", carDealer.getPhone());
                jsonItem.put("address", carDealer.getAddress());
                jsonItem.put("business", carDealer.getBusiness());

                Date registTime = carDealer.getRegistTime();
                if (registTime != null) {
                    jsonItem.put("registTime", DateFormatUtils.format(registTime, "yyyy-MM-dd"));
                }
                Date expireTime = carDealer.getExpireTime();
                if (expireTime != null) {
                    jsonItem.put("expireTime", DateFormatUtils.format(expireTime, "yyyy-MM-dd"));
                }
                jsonItem.put("smsCount", String.format("%s/%s", carDealer.getSendSmsNum(), carDealer.getSmsNum()));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carDealerPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int carDealerId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carDealerBiz.updateStatus(carDealerId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
