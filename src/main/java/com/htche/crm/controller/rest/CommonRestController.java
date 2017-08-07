package com.htche.crm.controller.rest;

import com.alibaba.fastjson.JSON;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.domain.Region;
import com.htche.crm.model.CxSelect;
import com.htche.crm.model.rest.CommonModel;
import com.htche.crm.model.rest.UserRechargeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/common")
public class CommonRestController {

    private static final Logger logger = LoggerFactory.getLogger(CommonRestController.class);

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "categories", method = RequestMethod.GET)
    public CommonModel.CategoryList getCategoryList(
            @RequestParam(value = "type", required = true) int type) {
        CommonModel.CategoryList categoryList = new CommonModel.CategoryList();
        categoryList.setStatus(1);
        Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(type);
        categoryList.setCategoryMap(categoryMap);
        return categoryList;
    }

    @RequestMapping(value = "regions", method = RequestMethod.GET)
    public CommonModel.RegionList getRegionList() {
        CommonModel.RegionList regionList = new CommonModel.RegionList();
        List<CommonModel.Region> regions = new ArrayList<>();
        regionList.setStatus(1);
        List<Region> regionsByDepth = regionBiz.selectListByDepth(1);
        if (regionsByDepth != null) {
            regionsByDepth.forEach(item -> {
                CommonModel.Region region = new CommonModel.Region();
                region.setName(item.getTitle());
                region.setValue(item.getRegionId().toString());
                region.setParent(item.getPid().toString());
                regions.add(region);
            });
        }
        regionList.setRegions(regions);
        return regionList;
    }
}
