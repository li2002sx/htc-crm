package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarModelTemplate;
import com.htche.crm.mapper.CarModelTemplateMapper;
import com.htche.crm.model.query.CarModelTemplateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarModelTemplateBiz {

    @Autowired
    CarModelTemplateMapper carModelTemplateMapper;

    /**
     * 保存
     *
     * @param carModelTemplate
     * @return
     */
    public boolean save(CarModelTemplate carModelTemplate) {
        int effectCount = 0;
        if (carModelTemplate.getTemplateId() > 0) {
            effectCount = carModelTemplateMapper.updateByPrimaryKeySelective(carModelTemplate);
        } else {
            effectCount = carModelTemplateMapper.insertSelective(carModelTemplate);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<CarModelTemplate> selectAllList(CarModelTemplateQuery query) {
        Page<CarModelTemplate> carModelTemplatePage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("carModelId", query.getCarModelId());
        List<CarModelTemplate> carModelTemplates = carModelTemplateMapper.selectAllList(map);
        return carModelTemplatePage;
    }

    public CarModelTemplate selectByPrimaryKey(int carModelTemplateId) {
        return carModelTemplateMapper.selectByPrimaryKey(carModelTemplateId);
    }

    public boolean updateStatus(int templateId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("templateId", templateId);
        map.put("status", status);
        return carModelTemplateMapper.updateStatus(map) > 0;
    }

    public boolean updateAcquiesce(int templateId, int carModelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("templateId", templateId);
        map.put("carModelId", carModelId);
        return carModelTemplateMapper.updateAcquiesce(map) > 0;
    }
}
