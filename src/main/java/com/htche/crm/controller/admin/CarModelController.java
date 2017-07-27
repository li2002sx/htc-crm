package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CarModelBiz;
import com.htche.crm.biz.CarTreeBiz;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarModel;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.CarModelQuery;
import com.htche.crm.util.ImageUtil;
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
@RequestMapping("/carmodel")
public class CarModelController {

    private static final Logger logger = LoggerFactory.getLogger(CarModelController.class);

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/carmodel/list");
        model.addObject("specMap", categoryBiz.getCategoryMap(1));
        return model;
    }

    @RequestMapping(value = "edit/{carModelId:\\d+}")
    public ModelAndView edit(@PathVariable int carModelId) {
        ModelAndView model = new ViewResult("custom/carmodel/edit");
        CarModel carModel = new CarModel();
        if (carModelId > 0) {
            carModel = carModelBiz.selectByPrimaryKey(carModelId);
            String picUrl = carModel.getPicUrl();
            model.addObject("picUrl", picUrl);
            model.addObject("realPicUrl", ImageUtil.getRealPicUrl(picUrl, false));
        }
        model.addObject("carModel", carModel);
        model.addObject("specMap", categoryBiz.getCategoryMap(1));
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarModel carModel) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelBiz.save(carModel);
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
    public JSONObject listdata(CarModelQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
        Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);

        Page<CarModel> carModelPage = carModelBiz.selectAllList(query);
        //封装前台数据
        if (carModelPage != null && carModelPage.getResult() != null) {
            for (CarModel carModel : carModelPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("carModelId", carModel.getCarModelId());
                jsonItem.put("brandName", carTreeMap.get(carModel.getBrandId()));
                jsonItem.put("seriesName", carTreeMap.get(carModel.getSeriesId()));
                jsonItem.put("modelsName", carTreeMap.get(carModel.getModelsId()));
                jsonItem.put("specName", categoryMap.get(carModel.getSpecId()));

                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", carModelPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int carModelId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carModelBiz.updateStatus(carModelId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
