package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarModel;
import com.htche.crm.domain.CarModelModule;
import com.htche.crm.mapper.CarModelMapper;
import com.htche.crm.model.query.CarModelModuleQuery;
import com.htche.crm.model.query.CarModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarModelBiz {

    private static final String[] modules = new String[]{"车型颜色", "外观", "内饰", "亮点", "相关"};

    @Autowired
    CarModelMapper carModelMapper;

    @Autowired
    CarModelModuleBiz carModelModuleBiz;

    /**
     * 保存
     *
     * @param carModel
     * @return
     */
    public boolean save(CarModel carModel) {
        int effectCount = 0;
        if (carModel.getCarModelId() > 0) {
            effectCount = carModelMapper.updateByPrimaryKeySelective(carModel);
//            carModelModuleBiz.batchInsertModules(modules, carModel.getCarModelId());
        } else {
            effectCount = carModelMapper.insertSelective(carModel);
            carModelModuleBiz.batchInsertModules(modules, carModel.getCarModelId());
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<CarModel> selectAllList(CarModelQuery query) {
        Page<CarModel> carModelPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("specId", query.getSpecId());
        map.put("brandId", query.getBrandId());
        map.put("carModelId", query.getCarModelId());
        map.put("modelsId", query.getModelsId());
        map.put("searchKey", query.getSearchKey());
        List<CarModel> carModels = carModelMapper.selectAllList(map);
        return carModelPage;
    }

    public CarModel selectByPrimaryKey(int carModelId) {
        return carModelMapper.selectByPrimaryKey(carModelId);
    }

    public boolean updateStatus(int carModelId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("carModelId", carModelId);
        map.put("status", status);
        return carModelMapper.updateStatus(map) > 0;
    }
}
