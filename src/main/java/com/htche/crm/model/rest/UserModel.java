package com.htche.crm.model.rest;

import com.htche.crm.domain.User;
import com.htche.crm.domain.UserPhoto;
import com.htche.crm.model.ApiResult;
import lombok.Data;

import java.util.List;

/**
 * Created by lishengxi on 2017/7/19.
 */
public class UserModel {

    @Data
    public static class UserInfo extends ApiResult {

        private User user;
    }

    @Data
    public static class UserPhotoList extends ApiResult {

        private List<UserPhoto> userPhotos;
    }

    @Data
    public static class WxAuthInfo extends ApiResult {

        private String wxAuthInfo;
    }

    @Data
    public static class WeixinJSConfig {

        //应用ID
        private String appId;

        //随机字符串
        private String nonceStr;

        //时间戳
        private String timestamp;

        //签名
        private String signature;
    }
}
