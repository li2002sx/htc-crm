package com.htche.crm.model.rest;

import com.htche.crm.domain.Category;
import com.htche.crm.domain.User;
import com.htche.crm.domain.UserPhoto;
import com.htche.crm.model.ApiResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by lishengxi on 2017/7/19.
 */
public class CommonModel {

    @Data
    public static class CategoryList extends ApiResult {

        private Map<Integer, String> categoryMap;
    }

    @Data
    public static class RegionList extends ApiResult {

        private List<Region> regions;
    }

    @Data
    public static class Region {
        private String name;
        private String value;
        private String parent;
    }
}
