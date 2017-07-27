package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.CarOwner;
import com.htche.crm.mapper.CarOwnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class CarOwnerBiz {

    @Autowired
    CarOwnerMapper carOwnerMapper;

    /**
     * 保存
     *
     * @param carOwner
     * @return
     */
    public boolean save(CarOwner carOwner) {
        int effectCount = 0;
        if (carOwner.getCarOwnerId() > 0) {
            effectCount = carOwnerMapper.updateByPrimaryKeySelective(carOwner);
        } else {
            effectCount = carOwnerMapper.insertSelective(carOwner);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<CarOwner> selectAllList(int carDealerId, String name, int pageIndex, int pageSize) {
        Page<CarOwner> carOwnerPage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        map.put("name", name);
        List<CarOwner> carOwners = carOwnerMapper.selectAllList(map);
        return carOwnerPage;
    }

    public CarOwner selectByPrimaryKey(int carOwnerId) {
        return carOwnerMapper.selectByPrimaryKey(carOwnerId);
    }

    public boolean updateStatus(int carOwnerId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("carOwnerId", carOwnerId);
        map.put("status", status);
        return carOwnerMapper.updateStatus(map) > 0;
    }

    public int selectCount(int carDealerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        return carOwnerMapper.selectCount(map);
    }

    public int selectMonthBirthdayCount(int carDealerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        return carOwnerMapper.selectMonthBirthdayCount(map);
    }

    public int selectDayBirthdayCount(int carDealerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        return carOwnerMapper.selectDayBirthdayCount(map);
    }

    /**
     * 获取当天过生日的用户
     *
     * @return
     */
    public List<CarOwner> selectDayBirthdayList() {
        Map<String, Object> map = new HashMap<>();
        return carOwnerMapper.selectDayBirthdayList(map);
    }

    public boolean updateDayBirthday(int carOwnerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carOwnerId", carOwnerId);
        map.put("smsTime", new Date());
        return carOwnerMapper.updateDayBirthday(map) > 0;
    }
}
