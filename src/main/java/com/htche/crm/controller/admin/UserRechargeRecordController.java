package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.UserRechargeRecordBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.constants.PayStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.UserRechargeRecord;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.UserRechargeRecordQuery;
import com.htche.crm.util.AmountUtil;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/userrecharge")
public class UserRechargeRecordController {

    private static final Logger logger = LoggerFactory.getLogger(UserRechargeRecordController.class);

    @Autowired
    UserRechargeRecordBiz userRechargeRecordBiz;

    @RequestMapping(value = "list")
    public ModelAndView list(
            @RequestParam(value = "userid", required = false) Integer userid) {
        ModelAndView model = new ViewResult("custom/userrechargerecord/list");
        model.addObject("userId", userid);
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
    public JSONObject listdata(UserRechargeRecordQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();

        Page<UserRechargeRecord> userRechargeRecordPage = userRechargeRecordBiz.selectAllList(query);
        //封装前台数据
        if (userRechargeRecordPage != null && userRechargeRecordPage.getResult() != null) {
            for (UserRechargeRecord userRechargeRecord : userRechargeRecordPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("userRechargeId", userRechargeRecord.getUserRechargeId());
                jsonItem.put("userId", userRechargeRecord.getUserId());
                jsonItem.put("orderNumber", userRechargeRecord.getOrderNumber());
                jsonItem.put("amount", AmountUtil.centToYuan(userRechargeRecord.getAmount()));
                jsonItem.put("statusName", PayStatus.getName(userRechargeRecord.getStatus()));
                jsonItem.put("createTime", DateFormatUtils.format(userRechargeRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                Date finishTime = userRechargeRecord.getFinishTime();
                if (finishTime != null) {
                    jsonItem.put("finishTime", DateFormatUtils.format(finishTime, "yyyy-MM-dd HH:mm:ss"));
                }
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", userRechargeRecordPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }
}
