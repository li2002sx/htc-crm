package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSON;
import com.htche.crm.biz.CarTreeBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.CarTree;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.CxSelect;
import com.htche.crm.util.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/cartree")
public class CarTreeController {

    private static final Logger logger = LoggerFactory.getLogger(CarTreeController.class);

    @Autowired
    CarTreeBiz carTreeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/cartree/list");
        List<CarTree> carTrees = carTreeBiz.selectAllList();
        model.addObject("carTrees", JSON.toJSONString(carTrees));
        Integer maxId = carTreeBiz.selectMaxId();
        if (maxId == null) maxId = 0;
        model.addObject("startCount", maxId + 1);

        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(CarTree carTree) {
        AjaxResult ajaxResult = new AjaxResult();
        carTree.setStatus(CommonStatus.Normal.getIndex());
        boolean flag = carTreeBiz.save(carTree);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = carTreeBiz.updateStatus(id, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    @RequestMapping("listdata")
    @ResponseBody
    public String listdata() {
        List<CxSelect.CxSelectFirst> firsts = carTreeBiz.getCarTreeList();
        String json = JSON.toJSONString(firsts);
        return json;
    }
}
