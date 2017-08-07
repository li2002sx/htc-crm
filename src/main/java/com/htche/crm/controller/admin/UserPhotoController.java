package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.UserPhotoBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.UserPhoto;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.UserPhotoQuery;
import com.htche.crm.util.ImageUtil;
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
@RequestMapping("/userphoto")
public class UserPhotoController {

    private static final Logger logger = LoggerFactory.getLogger(UserPhotoController.class);

    @Autowired
    UserPhotoBiz userPhotoBiz;


    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "userid", required = false) Integer userid) {
        ModelAndView model = new ViewResult("custom/userphoto/list");
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
    public JSONObject listdata(UserPhotoQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Page<UserPhoto> userPage = userPhotoBiz.selectAllList(query);
        //封装前台数据
        if (userPage != null && userPage.getResult() != null) {
            for (UserPhoto userPhoto : userPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("userId", userPhoto.getUserId());
                String picUrl = userPhoto.getPicUrl();
                jsonItem.put("realPicUrl", ImageUtil.getRealPicUrl(picUrl, false));
                jsonItem.put("createTime", DateFormatUtils.format(userPhoto.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
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
        boolean flag = userPhotoBiz.updateStatus(userId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
