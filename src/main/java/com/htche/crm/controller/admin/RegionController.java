package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSON;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.model.CxSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/region")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping("listdata")
    @ResponseBody
    public String listdata() {
        List<CxSelect.CxSelectFirst> firsts = regionBiz.getRegionList();
        String json = JSON.toJSONString(firsts);
        return json;
    }
}
