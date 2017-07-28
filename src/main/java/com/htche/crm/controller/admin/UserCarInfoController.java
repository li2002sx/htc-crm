package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.UserCarInfoBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.UserCarInfo;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.UserCarInfoQuery;
import com.htche.crm.util.ImageUtil;
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

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/usercar")
public class UserCarInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserCarInfoController.class);

    @Autowired
    UserCarInfoBiz userCarInfoBiz;


    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/usercarinfo/list");
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
    public JSONObject listdata(UserCarInfoQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Page<UserCarInfo> userPage = userCarInfoBiz.selectAllList(query);
        //封装前台数据
        if (userPage != null && userPage.getResult() != null) {
            for (UserCarInfo userCarInfo : userPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("userCarInfoId", userCarInfo.getUserCarInfoId());
                jsonItem.put("userId", userCarInfo.getUserId());
                jsonItem.put("price", userCarInfo.getPrice());
                jsonItem.put("configure", userCarInfo.getConfigure());
                jsonItem.put("frame", userCarInfo.getFrame());
                jsonItem.put("createTime", DateFormatUtils.format(userCarInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", userPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int userCarInfoId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = userCarInfoBiz.updateStatus(userCarInfoId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
