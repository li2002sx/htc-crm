package com.htche.crm.biz;

import com.htche.crm.domain.SmsConf;
import com.htche.crm.domain.SmsTemplate;
import com.htche.crm.mapper.SmsConfMapper;
import com.htche.crm.model.SmsHuyiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class SmsConfBiz {

    @Autowired
    SmsConfMapper smsMapper;

    /**
     * 保存
     *
     * @param sms
     * @return
     */
    public boolean save(SmsConf sms) {
        int effectCount = 0;
        if (sms.getSmsId() > 0) {
            effectCount = smsMapper.updateByPrimaryKeySelective(sms);
        } else {
            effectCount = smsMapper.insertSelective(sms);
        }
        return effectCount > 0;
    }

    public SmsConf selectByCarDealerId(int carDealerId) {
        return smsMapper.selectByCarDealerId(carDealerId);
    }

    public Map<Integer, Integer> getTemplateIdMap() {
        Map<Integer, Integer> map = new HashMap<>();
        Map<String, Object> argMap = new HashMap<>();
        List<SmsConf> smsConfs = smsMapper.selectAllList(argMap);
        smsConfs.forEach(item -> {
            map.put(item.getCarDealerId(), item.getTemplateId());
        });
        return map;
    }
}
