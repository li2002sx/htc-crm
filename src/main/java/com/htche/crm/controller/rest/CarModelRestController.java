package com.htche.crm.controller.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.*;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarModel;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.domain.CarModelTemplate;
import com.htche.crm.domain.CarModuleInfo;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarModelModuleQuery;
import com.htche.crm.model.query.CarModelQuery;
import com.htche.crm.model.query.CarModelTemplateQuery;
import com.htche.crm.model.rest.CarModelModel;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/carmodel")
public class CarModelRestController {

    private static final Logger logger = LoggerFactory.getLogger(CarModelRestController.class);

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @Autowired
    CarModelModuleBiz carModelModuleBiz;

    @Autowired
    CarModuleInfoBiz carModuleInfoBiz;

    @Autowired
    CarModelTemplateBiz carModelTemplateBiz;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public CarModelModel.CarModelList getCarModelList(
            @RequestParam(value = "searchKey", required = false) String searchKey,
            @RequestParam(value = "modelsId", required = false) Integer modelsId,
            @RequestParam(value = "pageIndex", required = true) Integer pageIndex
    ) {
        CarModelModel.CarModelList carModelList = new CarModelModel.CarModelList();
        carModelList.setStatus(1);
        CarModelQuery carModelQuery = new CarModelQuery();
        carModelQuery.setSearchKey(searchKey);
        carModelQuery.setModelsId(modelsId);
        carModelQuery.setPageIndex(pageIndex);
        carModelQuery.setPageSize(10);
        List<CarModel> carModels = carModelBiz.selectAllList(carModelQuery);
        if (carModels != null && carModels.size() > 0) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
            carModels.forEach(item -> {
                item.setPicUrl(ImageUtil.getRealPicUrl(item.getPicUrl(), false));
                item.setSpecName(categoryMap.get(item.getSpecId()));
                item.setModelsName(carTreeMap.get(item.getModelsId()));
            });
        }
        carModelList.setCarModelList(carModels);
        return carModelList;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public CarModelModel.CarModelInfo getCarModelInfo(
            @RequestParam(value = "carModelId", required = true) Integer carModelId) {
        CarModelModel.CarModelInfo carModelInfo = new CarModelModel.CarModelInfo();
        carModelInfo.setStatus(0);
        CarModel carModel = carModelBiz.selectByPrimaryKey(carModelId);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
            carModel.setPicUrl(ImageUtil.getRealPicUrl(carModel.getPicUrl(), false));
            carModel.setSpecName(categoryMap.get(carModel.getSpecId()));
            carModel.setModelsName(carTreeMap.get(carModel.getModelsId()));
            carModelInfo.setCarModel(carModel);
            //获取模板
            CarModelTemplateQuery carModelTemplateQuery = new CarModelTemplateQuery();
            carModelTemplateQuery.setCarModelId(carModelId);
            List<CarModelTemplate> carModelTemplates = carModelTemplateBiz.selectAllList(carModelTemplateQuery);
            if (carModelTemplates != null && carModelTemplates.size() > 0) {
                CarModelTemplate carModelTemplate = carModelTemplates.get(0);
                carModelTemplate.setBgPicUrl(ImageUtil.getRealPicUrl(carModelTemplate.getBgPicUrl(), false));
                carModelInfo.setCarModelTemplate(carModelTemplate);

                //获取模块
                String moduleIds = carModelTemplate.getModuleIds();
                if (StringUtils.isNotBlank(moduleIds)) {
                    List<String> moduleIdList = Arrays.asList(moduleIds.split(","));
                    CarModelModuleQuery carModelModuleQuery = new CarModelModuleQuery();
                    carModelModuleQuery.setCarModelId(carModelId);
                    carModelModuleQuery.setModuleIds(moduleIdList);
                    List<CarModelModule> carModelModules = carModelModuleBiz.selectAllList(carModelModuleQuery);
                    if (carModelModules != null && carModelModules.size() > 0) {
                        List<CarModuleInfo> carModuleInfos = carModuleInfoBiz.selectListByModuleIds(moduleIdList);
                        if (carModuleInfos != null & carModuleInfos.size() > 0) {
                            carModelModules.forEach(module -> {
                                if (module.getFontSize() == 0) {
                                    module.setFontSize(30);
                                }
                                List<CarModuleInfo> infos = carModuleInfos.stream().filter(info -> info.getModuleId() == module.getModuleId()).collect(Collectors.toList());
                                if (infos != null && infos.size() > 0) {
                                    infos.forEach(info -> {
                                        info.setPicUrl(ImageUtil.getRealPicUrl(info.getPicUrl(), false));
                                    });
                                    module.setCarModuleInfos(infos);
                                }
                            });
                            carModelInfo.setCarModelModules(carModelModules);
                            carModelInfo.setStatus(1);
                        } else {
                            carModelInfo.setMessage("没有找到模块信息列表");
                        }
                    } else {
                        carModelInfo.setMessage("没有找到模块列表");
                    }
                } else {
                    carModelInfo.setMessage("模板不包含任何模块");
                }
            } else {
                carModelInfo.setMessage("没有找到模板");
            }
        } else {
            carModelInfo.setMessage("没有找到车型");
        }
        return carModelInfo;
    }
}
