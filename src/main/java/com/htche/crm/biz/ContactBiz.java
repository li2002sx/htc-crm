package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.Contact;
import com.htche.crm.mapper.ContactMapper;
import com.htche.crm.model.query.ContactQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class ContactBiz {

    @Autowired
    ContactMapper contactMapper;

    /**
     * 保存
     *
     * @param contact
     * @return
     */
    public boolean save(Contact contact) {
        int effectCount = 0;
        if (contact.getContactId() > 0) {
            effectCount = contactMapper.updateByPrimaryKeySelective(contact);
        } else {
            effectCount = contactMapper.insertSelective(contact);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     * @return
     */
    public Page<Contact> selectAllList(ContactQuery query) {
        Page<Contact> contactPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("name", query.getName());
        map.put("phone", query.getPhone());
        List<Contact> contacts = contactMapper.selectAllList(map);
        return contactPage;
    }

    public Contact selectByPrimaryKey(int contactId) {
        return contactMapper.selectByPrimaryKey(contactId);
    }

    public boolean updateStatus(int contactId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("contactId", contactId);
        map.put("status", status);
        return contactMapper.updateStatus(map) > 0;
    }
}
