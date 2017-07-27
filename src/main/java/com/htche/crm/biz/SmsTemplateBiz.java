package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.SmsTemplate;
import com.htche.crm.mapper.SmsTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class SmsTemplateBiz {

    @Autowired
    SmsTemplateMapper smsTemplateMapper;

    /**
     * 保存
     *
     * @param smsTemplate
     * @return
     */
    public boolean save(SmsTemplate smsTemplate) {
        int effectCount = 0;
        if (smsTemplate.getTemplateId() > 0) {
            effectCount = smsTemplateMapper.updateByPrimaryKeySelective(smsTemplate);
        } else {
            effectCount = smsTemplateMapper.insertSelective(smsTemplate);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<SmsTemplate> selectAllList(int pageIndex, int pageSize) {
        Page<SmsTemplate> smsTemplatePage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, String> map = new HashMap<>();
        List<SmsTemplate> smsTemplates = smsTemplateMapper.selectAllList(map);
        return smsTemplatePage;
    }

    public SmsTemplate selectByPrimaryKey(int smsTemplateId) {
        return smsTemplateMapper.selectByPrimaryKey(smsTemplateId);
    }

    public boolean updateStatus(int smsTemplateId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("templateId", smsTemplateId);
        map.put("status", status);
        return smsTemplateMapper.updateStatus(map) > 0;
    }

    public Map<Integer, String> getAllMap(int templateType) {
        Map<Integer, String> map = new HashMap<>();
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("templateType", templateType);
        List<SmsTemplate> smsTemplates = smsTemplateMapper.selectAllList(argMap);
        smsTemplates.forEach(item -> {
            map.put(item.getTemplateId(), item.getContent());
        });
        return map;
    }
}
