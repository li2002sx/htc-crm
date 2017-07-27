package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarModelBiz;
import com.htche.crm.biz.CarModelModuleBiz;
import com.htche.crm.biz.CarModuleInfoBiz;
import com.htche.crm.biz.CarTreeBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarModel;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.domain.CarModuleInfo;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarModuleInfoQuery;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/moduleinfo")
public class CarModuleInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CarModuleInfoController.class);

    @Autowired
    CarModuleInfoBiz carModuleInfoBiz;

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    CarModelModuleBiz carModelModuleBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list(
            @RequestParam(value = "moduleid", required = true) Integer moduleid,
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmoduleinfo/list");
        model.addObject("moduleId", moduleid);
        model.addObject("carModelId", modelid);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        CarModelModule carModelModule = carModelModuleBiz.selectByPrimaryKey(moduleid);
        model.addObject("carModelModule", carModelModule);

        return model;
    }

    @RequestMapping(value = "edit/{moduleInfoId:\\d+}")
    public ModelAndView edit(
            @PathVariable int moduleInfoId,
            @RequestParam(value = "moduleid", required = true) Integer moduleid,
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmoduleinfo/edit");
        CarModuleInfo carModuleInfo = new CarModuleInfo();
        carModuleInfo.setModuleId(moduleid);
        if (moduleInfoId > 0) {
            carModuleInfo = carModuleInfoBiz.selectByPrimaryKey(moduleInfoId);
            String picUrl = carModuleInfo.getPicUrl();
            model.addObject("picUrl", picUrl);
            model.addObject("realPicUrl", ImageUtil.getRealPicUrl(picUrl, false));

            String picUrl2 = carModuleInfo.getPicUrl2();
            model.addObject("picUrl2", picUrl2);
            model.addObject("realPicUrl2", ImageUtil.getRealPicUrl(picUrl2, false));
        }
        model.addObject("carModuleInfo", carModuleInfo);
        model.addObject("carModelId", modelid);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        CarModelModule carModelModule = carModelModuleBiz.selectByPrimaryKey(moduleid);
        model.addObject("carModelModule", carModelModule);

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
    public JSONObject listdata(CarModuleInfoQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;


        Page<CarModuleInfo> carModuleInfoPage = carModuleInfoBiz.selectAllList(query);
        //封装前台数据
        if (carModuleInfoPage != null && carModuleInfoPage.getResult() != null) {
            for (CarModuleInfo carModuleInfo : carModuleInfoPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("moduleInfoId", carModuleInfo.getModuleInfoId());
                jsonItem.put("moduleId", carModuleInfo.getModuleId());
                jsonItem.put("title", carModuleInfo.getTitle());
                String picUrl = carModuleInfo.getPicUrl();
                if (StringUtils.isNotEmpty(picUrl)) {
                    jsonItem.put("picUrl", ImageUtil.getRealPicUrl(picUrl, false));
                }
                jsonItem.put("orderNo", carModuleInfo.getOrderNo());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carModuleInfoPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarModuleInfo carModuleInfo) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModuleInfoBiz.save(carModuleInfo);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int moduleInfoId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModuleInfoBiz.updateStatus(moduleInfoId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "updateorderno", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateOrderNo(int moduleInfoId, int orderNo) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModuleInfoBiz.updateOrderNo(moduleInfoId, orderNo);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
