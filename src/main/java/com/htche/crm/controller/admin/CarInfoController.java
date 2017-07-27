package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarInfoBiz;
import com.htche.crm.biz.CarTreeBiz;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarInfo;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarInfoQuery;
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
@RequestMapping("/carinfo")
public class CarInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CarInfoController.class);

    @Autowired
    CarInfoBiz carInfoBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/carinfo/list");
        model.addObject("specMap", categoryBiz.getCategoryMap(1));
        return model;
    }

    @RequestMapping(value = "edit/{carInfoId:\\d+}")
    public ModelAndView edit(@PathVariable int carInfoId) {
        ModelAndView model = new ViewResult("custom/carinfo/edit");
        CarInfo carInfo = new CarInfo();
        if (carInfoId > 0) {
            carInfo = carInfoBiz.selectByPrimaryKey(carInfoId);
        }
        model.addObject("carInfo", carInfo);
        model.addObject("specMap", categoryBiz.getCategoryMap(1));
        model.addObject("sourceMap", categoryBiz.getCategoryMap(2));
        model.addObject("procedureMap", categoryBiz.getCategoryMap(3));
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarInfo carInfo) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carInfoBiz.save(carInfo);
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
    public JSONObject listdata(CarInfoQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
        Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        Page<CarInfo> carInfoPage = carInfoBiz.selectAllList(query);
        //封装前台数据
        if (carInfoPage != null && carInfoPage.getResult() != null) {
            for (CarInfo carInfo : carInfoPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("carInfoId", carInfo.getCarInfoId());
                jsonItem.put("brandName", carTreeMap.get(carInfo.getBrandId()));
                jsonItem.put("seriesName", carTreeMap.get(carInfo.getSeriesId()));
                jsonItem.put("modelsName", carTreeMap.get(carInfo.getModelsId()));
                jsonItem.put("specName", categoryMap.get(carInfo.getSpecId()));
                jsonItem.put("configure", carInfo.getConfigure());
                jsonItem.put("frame", carInfo.getFrame());
                jsonItem.put("outColor", carInfo.getOutColor());
                jsonItem.put("inColor", carInfo.getInColor());
                jsonItem.put("sourceName", categoryMap.get(carInfo.getSourceId()));
                jsonItem.put("procedureName", categoryMap.get(carInfo.getProcedureId()));
                jsonItem.put("provinceName", regionMap.get(carInfo.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(carInfo.getCityId()));
                jsonItem.put("price", carInfo.getPrice());
                jsonItem.put("priceRatio", carInfo.getPriceRatio());
                jsonItem.put("time", carInfo.getYear() + "." + carInfo.getMonth());

                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carInfoPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int carInfoId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carInfoBiz.updateStatus(carInfoId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
