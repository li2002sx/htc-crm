package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.biz.UserBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.constants.SexType;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.User;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.UserQuery;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserBiz userBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/user/list");
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
    public JSONObject listdata(UserQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
        Map<Integer, String> regionMap = regionBiz.getRegionMap();

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();

        Page<User> userPage = userBiz.selectAllList(query);
        //封装前台数据
        if (userPage != null && userPage.getResult() != null) {
            for (User user : userPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("userId", user.getUserId());
                jsonItem.put("realName", user.getRealName());
                jsonItem.put("mobile", user.getMobile());
                jsonItem.put("sex", SexType.getName(user.getSex()));
                jsonItem.put("industryName", categoryMap.get(user.getIndustryId()));
                jsonItem.put("company", user.getCompany());
                jsonItem.put("position", user.getPosition());
                jsonItem.put("wxCode", user.getWxCode());
                jsonItem.put("provinceName", regionMap.get(user.getProvinceId()));
                jsonItem.put("cityName", regionMap.get(user.getCityId()));
                Date expireTime = user.getExpireTime();
                if (expireTime != null) {
                    jsonItem.put("expireTime", DateFormatUtils.format(expireTime, "yyyy-MM-dd"));
                }
                jsonItem.put("createTime", DateFormatUtils.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", userPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int userId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = userBiz.updateStatus(userId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
