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

        public User user;
    }

    @Data
    public static class UserPhotoList extends ApiResult {

        public List<UserPhoto> userPhotos;
    }
}
