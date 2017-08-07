package com.htche.crm.controller.rest;

import com.htche.crm.biz.*;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.*;
import com.htche.crm.model.ApiResult;
import com.htche.crm.model.query.UserCarInfoQuery;
import com.htche.crm.model.rest.CarModelModel;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/usercar")
public class UserCarInfoRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserCarInfoRestController.class);

    @Autowired
    UserCarInfoBiz userCarInfoBiz;

    @Autowired
    CarModelBiz carModelBiz;

    @Autowired
    UserBiz userBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @Autowired
    CarTreeBiz carTreeBiz;

    @Autowired
    UserPhotoBiz userPhotoBiz;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public CarModelModel.UserCarList getUserCarList(
            @RequestParam(value = "pageIndex", required = true) Integer pageIndex) {
        CarModelModel.UserCarList userCarList = new CarModelModel.UserCarList();
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            UserCarInfoQuery query = new UserCarInfoQuery();
            query.setUserId(user.getUserId());
            query.setPageIndex(pageIndex);
            query.setPageSize(10);
            List<UserCarInfo> userCarInfos = userCarInfoBiz.selectAllList(query);
            if (userCarInfos != null && userCarInfos.size() > 0) {
                Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
                Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
                userCarInfos.forEach(item -> {
                    item.getCarModel().setPicUrl(ImageUtil.getRealPicUrl(item.getCarModel().getPicUrl(), false));
                    item.getCarModel().setSpecName(categoryMap.get(item.getCarModel().getSpecId()));
                    item.getCarModel().setModelsName(carTreeMap.get(item.getCarModel().getModelsId()));
                });
            }
            userCarList.setStatus(1);
            userCarList.setUserCarInfos(userCarInfos);
        } else {
            userCarList.setStatus(-1);
        }
        return userCarList;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public CarModelModel.UserCarResult getUserCarResult(
            @RequestParam(value = "carModelId", required = true) Integer carModelId,
            @RequestParam(value = "userCarInfoId", required = true) Integer userCarInfoId
    ) {
        CarModelModel.UserCarResult userCarResult = new CarModelModel.UserCarResult();
        userCarResult.setStatus(0);
        CarModel carModel = carModelBiz.selectByPrimaryKey(carModelId);
        if (carModel != null) {
            Map<Integer, String> carTreeMap = carTreeBiz.getCarTreeMap();
            Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
            carModel.setPicUrl(ImageUtil.getRealPicUrl(carModel.getPicUrl(), false));
            carModel.setSpecName(categoryMap.get(carModel.getSpecId()));
            carModel.setModelsName(carTreeMap.get(carModel.getModelsId()));

            userCarResult.setCarModel(carModel);
            UserCarInfo userCarInfo = new UserCarInfo();
            userCarResult.setUserCarInfo(userCarInfo);

            userCarResult.setStatus(1);
            if (userCarInfoId != null && userCarInfoId > 0) {
                userCarResult.setStatus(0);
                userCarInfo = userCarInfoBiz.selectByPrimaryKey(userCarInfoId);
                if (userCarInfo != null) {
                    userCarResult.setUserCarInfo(userCarInfo);
                    int userId = userCarInfo.getUserId();
                    if (userId > 0) {
                        User user = userBiz.selectByPrimaryKey(userId);
                        if (user != null) {
                            user.setUserPic(ImageUtil.getRealPicUrl(user.getUserPic(), false));
                            Map<Integer, String> regionMap = regionBiz.getRegionMap();
                            user.setIndustryName(categoryMap.get(user.getIndustryId()));
                            user.setProvinceName(regionMap.get(user.getProvinceId()));
                            user.setCityName(regionMap.get(user.getCityId()));
                            userCarResult.setUser(user);

                            List<UserPhoto> userPhotos = userPhotoBiz.selectTopNList(userId, 4);
                            if (userPhotos != null && userPhotos.size() > 0) {
                                userPhotos.forEach(item -> {
                                    item.setPicUrl(ImageUtil.getRealPicUrl(item.getPicUrl(), false));
                                });
                            }
                            userCarResult.setUserPhotos(userPhotos);
                            userCarResult.setStatus(1);

                        } else {
                            userCarResult.setMessage("没有找到用户信息");
                        }
                    } else {
                        userCarResult.setMessage("用户ID为空");
                    }
                } else {
                    userCarResult.setMessage("没有找到用户车源信息");
                }
            }
        } else {
            userCarResult.setMessage("没有找到车型信息");
        }
        return userCarResult;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult save(UserCarInfo userCarInfo) {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            userCarInfo.setUserId(user.getUserId());
            userCarInfo.setStatus(CommonStatus.Normal.getIndex());
            if (userCarInfo.getUserCarInfoId() == 0) {
                userCarInfo.setCreateTime(new Date());
            }
            boolean flag = userCarInfoBiz.save(userCarInfo);
            apiResult.setStatus(flag ? 1 : 0);
        } else {
            apiResult.setStatus(-1);
        }
        return apiResult;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ApiResult delete(String userCarInfoIds) {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            boolean flag = userCarInfoBiz.updateStatusByIds(userCarInfoIds.split(","), CommonStatus.Deleted.getIndex(), user.getUserId());
            apiResult.setStatus(flag ? 1 : 0);
        } else {
            apiResult.setStatus(-1);
        }
        return apiResult;
    }
}
