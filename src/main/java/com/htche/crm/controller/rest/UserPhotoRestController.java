package com.htche.crm.controller.rest;

import com.htche.crm.biz.CategoryBiz;
import com.htche.crm.biz.RegionBiz;
import com.htche.crm.biz.UserBiz;
import com.htche.crm.biz.UserPhotoBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.User;
import com.htche.crm.domain.UserPhoto;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.ApiResult;
import com.htche.crm.model.query.UserPhotoQuery;
import com.htche.crm.model.rest.UserModel;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/userphoto")
public class UserPhotoRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserPhotoRestController.class);

    @Autowired
    UserPhotoBiz userPhotoBiz;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public UserModel.UserPhotoList getPhotoList(
            @RequestParam(value = "userId", required = true) Integer userId) {
        UserModel.UserPhotoList userPhotoList = new UserModel.UserPhotoList();
        UserPhotoQuery userPhotoQuery = new UserPhotoQuery();
        userPhotoQuery.setUserId(userId);
        List<UserPhoto> userPhotos = userPhotoBiz.selectAllList(userPhotoQuery);
        if (userPhotos != null && userPhotos.size() > 0) {
            userPhotos.forEach(item -> {
                item.setPicUrl(ImageUtil.getRealPicUrl(item.getPicUrl(), false));
            });
        }
        userPhotoList.setUserPhotos(userPhotos);
        userPhotoList.setStatus(1);
        return userPhotoList;
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ApiResult upload(
            @RequestPart(required = false, value = "file") MultipartFile file,
            @RequestParam(value = "cate", required = true) String cate,
            @RequestParam(value = "subCate", required = false) String subCate) {

        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);

        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            AjaxResult result = ImageUtil.upload(file, cate, subCate);
            if (result.isSuccess()) {
                UserPhoto userPhoto = new UserPhoto();
                userPhoto.setUserId(user.getUserId());
                userPhoto.setStatus(1);
                userPhoto.setPicUrl(result.getValue().toString());
                userPhoto.setCreateTime(new Date());
                boolean flag = userPhotoBiz.save(userPhoto);
                apiResult.setStatus(flag ? 1 : 0);
            } else {
                apiResult.setMessage(result.getError());
            }
        } else {
            user.setStatus(-1);
        }
        return apiResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ApiResult delete(
            @RequestParam(value = "userPhotoId", required = true) int userPhotoId) {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            boolean flag = userPhotoBiz.updateStatusByUserId(userPhotoId, CommonStatus.Deleted.getIndex(), user.getUserId());
            apiResult.setStatus(flag ? 1 : 0);
        } else {
            user.setStatus(-1);
        }
        return apiResult;
    }
}