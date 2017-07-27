package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.*;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.constants.SexType;
import com.htche.crm.domain.CarModel;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarModelModuleQuery;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/carmodule")
public class CarModelModuleController {

    private static final Logger logger = LoggerFactory.getLogger(CarModelModuleController.class);

    @Autowired
    CarModelModuleBiz carModelModuleBiz;

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list(
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmodelmodule/list");
        model.addObject("carModelId", modelid);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        return model;
    }

    @RequestMapping(value = "edit/{moduleId:\\d+}")
    public ModelAndView edit(
            @PathVariable int moduleId,
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmodelmodule/edit");
        CarModelModule carModule = new CarModelModule();
        carModule.setCarModelId(modelid);
        if (moduleId > 0) {
            carModule = carModelModuleBiz.selectByPrimaryKey(moduleId);
        }
        model.addObject("carModule", carModule);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        return model;
    }

    /**
     * 获取新闻数据，用于异步加载
     *
     * @param query 查询实体
     * @return
     */
    @RequestMapping(value = "/listdata")
    @ResponseBody
    public JSONObject listdata(CarModelModuleQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;


        Page<CarModelModule> carModelModulePage = carModelModuleBiz.selectAllList(query);
        //封装前台数据
        if (carModelModulePage != null && carModelModulePage.getResult() != null) {
            for (CarModelModule carModelModule : carModelModulePage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("moduleId", carModelModule.getModuleId());
                jsonItem.put("carModelId", carModelModule.getCarModelId());
                jsonItem.put("title", carModelModule.getTitle());
                jsonItem.put("fontSize", carModelModule.getFontSize());
                jsonItem.put("color", carModelModule.getColor());
                jsonItem.put("orderNo",carModelModule.getOrderNo());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carModelModulePage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarModelModule carModelModule) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelModuleBiz.save(carModelModule);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int moduleId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelModuleBiz.updateStatus(moduleId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "updateorderno", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateOrderNo(int moduleId, int orderNo) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelModuleBiz.updateOrderNo(moduleId, orderNo);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
