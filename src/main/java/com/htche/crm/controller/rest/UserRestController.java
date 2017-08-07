package com.htche.crm.controller.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.wxpay.sdk.WXPayUtil;
import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.biz.UserBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.constants.SexType;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.User;
import com.htche.crm.domain.UserCarInfo;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.ApiResult;
import com.htche.crm.model.query.UserQuery;
import com.htche.crm.model.rest.CarModelModel;
import com.htche.crm.model.rest.UserModel;
import com.htche.crm.util.*;
import com.htche.crm.util.pay.WeixinPayConfig;
import com.htche.crm.util.pay.WeixinUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/user")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    UserBiz userBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    public UserModel.UserInfo regist(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "code", required = true) String code) {
        UserModel.UserInfo userInfo = new UserModel.UserInfo();
        userInfo.setStatus(0);
        User user = userBiz.regist(mobile, password, code);
        if (user != null) {
            userInfo.setStatus(1);
            userInfo.setUser(user);
        }
        return userInfo;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public UserModel.UserInfo login(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "password", required = true) String password) {
        UserModel.UserInfo userInfo = new UserModel.UserInfo();
        userInfo.setStatus(0);
        User user = userBiz.login(mobile, password);
        if (user != null) {
            userInfo.setStatus(1);
            userInfo.setUser(user);
        }
        return userInfo;
    }

    @RequestMapping(value = "findpass", method = RequestMethod.POST)
    public UserModel.UserInfo findPass(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "code", required = true) String code) {
        UserModel.UserInfo userInfo = new UserModel.UserInfo();
        userInfo.setStatus(0);
        User user = userBiz.findPass(mobile, password, code);
        if (user != null) {
            userInfo.setStatus(1);
            userInfo.setUser(user);
        }
        return userInfo;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult save(User userInfo) {
        ApiResult apiResult = new ApiResult();
        String userPic = userInfo.getUserPic();
        if (userPic.startsWith("data:image")) {
            AjaxResult ajaxResult = ImageUtil.saveBase64ToPic(userPic, "user", "avatar");
            if (ajaxResult.isSuccess()) {
                userPic = ajaxResult.getValue().toString();
            } else {
                userPic = null;
            }
        } else {
            userPic = null;
        }
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            userInfo.setUserId(user.getUserId());
            userInfo.setUserPic(userPic);
            int effectCount = userBiz.updateInfoByUserId(userInfo);
            apiResult.setStatus(effectCount);
        } else {
            apiResult.setStatus(-1);
        }
        return apiResult;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public UserModel.UserInfo getUserInfo() {
        UserModel.UserInfo userInfo = new UserModel.UserInfo();
        userInfo.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            user = userBiz.selectByPrimaryKey(user.getUserId());
            user.setUserPic(ImageUtil.getRealPicUrl(user.getUserPic(), false));
            Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
            Map<Integer, String> regionMap = regionBiz.getRegionMap();
            user.setIndustryName(categoryMap.get(user.getIndustryId()));
            user.setProvinceName(regionMap.get(user.getProvinceId()));
            user.setCityName(regionMap.get(user.getCityId()));

            userInfo.setUser(user);
            userInfo.setStatus(1);
        } else {
            userInfo.setStatus(-1);
        }
        return userInfo;
    }

    @RequestMapping(value = "vip", method = RequestMethod.GET)
    public ApiResult vip() {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            user = userBiz.selectByPrimaryKey(user.getUserId());
            Date expireTime = user.getExpireTime();
            if (expireTime == null) {
                apiResult.setMessage("您还没有开通VIP会员");
            } else if (expireTime.getTime() < new Date().getTime()) {
                apiResult.setMessage("您的VIP会员已经过期");
            } else {
                apiResult.setStatus(1);
            }
        } else {
            apiResult.setStatus(-1);
        }
        return apiResult;
    }

    /**
     * 如果微信打开直接获取微信OPENID,用于支付
     *
     * @return
     */
    @RequestMapping(value = "wxauth", method = RequestMethod.GET)
    public UserModel.WxAuthInfo getWxAuthInfo(String code) {
        UserModel.WxAuthInfo wxAuthInfo = new UserModel.WxAuthInfo();
        wxAuthInfo.setStatus(0);
        if (StringUtils.isNotEmpty(code)) {
            StringBuilder sb = new StringBuilder();
            sb.append(WeixinUtil._WxAuthAccessTokenUrl + "?grant_type=authorization_code");
            sb.append("&appid=" + WeixinUtil._AppIdPub);
            sb.append("&secret=" + WeixinUtil._AppSecretPub);
            sb.append("&code=" + code);

            String json = HttpUtil.get(sb.toString());
            wxAuthInfo.setWxAuthInfo(json);
            wxAuthInfo.setStatus(1);
        }
        return wxAuthInfo;
    }

    @RequestMapping(value = "wxshareconf", method = RequestMethod.GET)
    public UserModel.WeixinJSConfig weixinJSConfig(
            @RequestParam(value = "url", required = true) String url) {
        String jsTicket = WeixinUtil.getWxJSTicket();

        Map<String, Object> map = new HashMap<>();
        String rndStr = RandUtil.stringId(10);
        long utmMill = new Date().getTime() / 1000L;
        map.put("noncestr", rndStr);
        map.put("jsapi_ticket", jsTicket);
        map.put("timestamp", utmMill);
        map.put("url", url);

        String sign = WeixinUtil.wxShareSign(map);
        UserModel.WeixinJSConfig weixinJSConfig = new UserModel.WeixinJSConfig();
        weixinJSConfig.setAppId(WeixinUtil._AppIdPub);
        weixinJSConfig.setNonceStr(rndStr);
        weixinJSConfig.setTimestamp(String.valueOf(utmMill));
        weixinJSConfig.setSignature(sign);
        return weixinJSConfig;
    }
}
