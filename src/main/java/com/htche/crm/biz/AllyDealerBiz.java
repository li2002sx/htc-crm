package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.AllyDealer;
import com.htche.crm.mapper.AllyDealerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class AllyDealerBiz {

    @Autowired
    AllyDealerMapper allyDealerMapper;

    /**
     * 保存
     *
     * @param allyDealer
     * @return
     */
    public boolean save(AllyDealer allyDealer) {
        int effectCount = 0;
        if (allyDealer.getAllyDealerId() > 0) {
            effectCount = allyDealerMapper.updateByPrimaryKeySelective(allyDealer);
        } else {
            effectCount = allyDealerMapper.insertSelective(allyDealer);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @param companyName
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<AllyDealer> selectAllList(int carDealerId, String companyName, String name, int pageIndex, int pageSize) {
        Page<AllyDealer> allyDealerPage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        map.put("companyName", companyName);
        map.put("name", name);
        List<AllyDealer> allyDealers = allyDealerMapper.selectAllList(map);
        return allyDealerPage;
    }

    public AllyDealer selectByPrimaryKey(int allyDealerId) {
        return allyDealerMapper.selectByPrimaryKey(allyDealerId);
    }

    public boolean updateStatus(int allyDealerId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("allyDealerId", allyDealerId);
        map.put("status", status);
        return allyDealerMapper.updateStatus(map) > 0;
    }

    public int selectCount(int carDealerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        return allyDealerMapper.selectCount(map);
    }
}
