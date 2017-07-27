package com.htche.crm.controller.rest;

import com.htche.crm.biz.*;
import com.htche.crm.domain.*;
import com.htche.crm.model.ApiResult;
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
    UserBiz userBiz;

    @Autowired
    CategoryBiz categoryBiz;

    @Autowired
    RegionBiz regionBiz;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public CarModelModel.UserCarResult getUserCarResult(
            @RequestParam(value = "carModelId", required = true) Integer carModelId
    ) {
        CarModelModel.UserCarResult userCarResult = new CarModelModel.UserCarResult();
        userCarResult.setStatus(0);
        UserCarInfo userCarInfo = userCarInfoBiz.selectByCarModelId(carModelId);
        if (userCarInfo != null) {
            userCarResult.setUserCarInfo(userCarInfo);
            int userId = userCarInfo.getUserId();
            if (userId > 0) {
                User user = userBiz.selectByPrimaryKey(userId);
                if (user != null) {
                    user.setUserPic(ImageUtil.getRealPicUrl(user.getUserPic(), false));
                    Map<Integer, String> categoryMap = categoryBiz.getCategoryMap(0);
                    Map<Integer, String> regionMap = regionBiz.getRegionMap();
                    user.setIndustryName(categoryMap.get(user.getIndustryId()));
                    user.setProvinceName(regionMap.get(user.getProvinceId()));
                    user.setCityName(regionMap.get(user.getCityId()));

                    userCarResult.setUser(user);
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

        return userCarResult;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult save(UserCarInfo userCarInfo) {
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        User user = CurrentUser.getInstance().getUser();
        if (user != null) {
            userCarInfo.setUserId(user.getUserId());
            boolean flag = userCarInfoBiz.save(userCarInfo);
            apiResult.setStatus(flag ? 1 : 0);
        } else {
            apiResult.setStatus(-1);
        }
        return apiResult;
    }
}
