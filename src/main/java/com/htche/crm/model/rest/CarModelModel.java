package com.htche.crm.model.rest;

import com.htche.crm.domain.*;
import com.htche.crm.model.ApiResult;
import lombok.Data;

import java.util.List;

/**
 * Created by lishengxi on 2017/7/19.
 */
public class CarModelModel {

    @Data
    public static class CarModelList extends ApiResult {

        private List<CarModel> carModelList;
    }

    @Data
    public static class CarModelInfo extends ApiResult {

        private CarModel carModel;

        private CarModelTemplate carModelTemplate;

        private List<CarModelModule> carModelModules;
    }

    @Data
    public static class UserCarList extends ApiResult {

        private List<UserCarInfo> userCarInfos;
    }

    @Data
    public static class UserCarResult extends ApiResult {

        private CarModel carModel;

        private UserCarInfo userCarInfo;

        private User user;

        private List<UserPhoto> userPhotos;
    }
}
