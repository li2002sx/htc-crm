package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarModelBiz;
import com.htche.crm.biz.CarModelModuleBiz;
import com.htche.crm.biz.CarModelTemplateBiz;
import com.htche.crm.biz.CarTreeBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarModel;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.domain.CarModelTemplate;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarModelModuleQuery;
import com.htche.crm.model.query.CarModelTemplateQuery;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/cartemplate")
public class CarModelTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(CarModelTemplateController.class);

    @Autowired
    CarModelTemplateBiz carModelTemplateBiz;

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @Autowired
    CarModelModuleBiz carModelModuleBiz;

    @RequestMapping(value = "list")
    public ModelAndView list(
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmodeltemplate/list");
        model.addObject("carModelId", modelid);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        return model;
    }

    @RequestMapping(value = "edit/{templateId:\\d+}")
    public ModelAndView edit(
            @PathVariable int templateId,
            @RequestParam(value = "modelid", required = true) Integer modelid) {
        ModelAndView model = new ViewResult("custom/carmodeltemplate/edit");
        CarModelTemplate carModelTemplate = new CarModelTemplate();
        if (templateId > 0) {
            carModelTemplate = carModelTemplateBiz.selectByPrimaryKey(templateId);
            String picUrl = carModelTemplate.getBgPicUrl();
            model.addObject("picUrl", picUrl);
            model.addObject("realPicUrl", ImageUtil.getRealPicUrl(picUrl, false));

            String moduleIds = carModelTemplate.getModuleIds();
            if (StringUtils.isNotEmpty(moduleIds)) {
                model.addObject("moduleIdArr", moduleIds.split(","));
            }
        }
        model.addObject("carTemplate", carModelTemplate);
        model.addObject("carModelId", modelid);

        CarModel carModel = carModelBiz.selectByPrimaryKey(modelid);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            model.addObject("modelsName", carTreeMap.get(carModel.getModelsId()));
        }

        CarModelModuleQuery carModelModuleQuery = new CarModelModuleQuery();
        carModelModuleQuery.setCarModelId(modelid);

        List<CarModelModule> carModelModules = carModelModuleBiz.selectAllList(carModelModuleQuery);
        Map<Integer, String> moduleMap = new HashMap<>();
        if (carModelModules != null) {
            carModelModules.forEach(item -> {
                moduleMap.put(item.getModuleId(), item.getTitle());
            });
        }
        model.addObject("moduleMap", moduleMap);

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
    public JSONObject listdata(CarModelTemplateQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;


        Page<CarModelTemplate> carModelTemplatePage = carModelTemplateBiz.selectAllList(query);
        //封装前台数据
        if (carModelTemplatePage != null && carModelTemplatePage.getResult() != null) {
            for (CarModelTemplate carModelTemplate : carModelTemplatePage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("templateId", carModelTemplate.getTemplateId());
                jsonItem.put("moduleIds", carModelTemplate.getModuleIds());
                jsonItem.put("title", carModelTemplate.getTitle());
                String picUrl = carModelTemplate.getBgPicUrl();
                if (StringUtils.isNotEmpty(picUrl)) {
                    jsonItem.put("picUrl", ImageUtil.getRealPicUrl(picUrl, false));
                }
                jsonItem.put("acquiesce", carModelTemplate.getAcquiesce());
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carModelTemplatePage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarModelTemplate carModelTemplate) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelTemplateBiz.save(carModelTemplate);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int templateId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelTemplateBiz.updateStatus(templateId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "acquiesce", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateAcquiesce(int templateId, int carModelId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelTemplateBiz.updateAcquiesce(templateId, carModelId);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
