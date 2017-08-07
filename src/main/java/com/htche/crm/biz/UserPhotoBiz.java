package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.UserPhoto;
import com.htche.crm.mapper.UserPhotoMapper;
import com.htche.crm.model.query.UserPhotoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class UserPhotoBiz {

    @Autowired
    UserPhotoMapper userPhotoMapper;

    /**
     * 保存
     *
     * @param userPhoto
     * @return
     */
    public boolean save(UserPhoto userPhoto) {
        int effectCount = 0;
        if (userPhoto.getPhotoId() > 0) {
            effectCount = userPhotoMapper.updateByPrimaryKeySelective(userPhoto);
        } else {
            effectCount = userPhotoMapper.insertSelective(userPhoto);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<UserPhoto> selectAllList(UserPhotoQuery query) {
        Page<UserPhoto> userPhotoPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("userId", query.getUserId());
        List<UserPhoto> userPhotos = userPhotoMapper.selectAllList(map);
        return userPhotoPage;
    }

    public UserPhoto selectByPrimaryKey(int userPhotoId) {
        return userPhotoMapper.selectByPrimaryKey(userPhotoId);
    }

    public boolean updateStatus(int userPhotoId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userPhotoId", userPhotoId);
        map.put("status", status);
        return userPhotoMapper.updateStatus(map) > 0;
    }

    public boolean updateStatusByIds(String[] userPhotoIds, int status, int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userPhotoIds", userPhotoIds);
        map.put("status", status);
        map.put("userId", userId);
        return userPhotoMapper.updateStatusByIds(map) > 0;
    }

    public List<UserPhoto> selectTopNList(int userId, int top) {
        Map<String, Object> map = new HashMap<>();
        map.put("top", top);
        map.put("userId", userId);
        return userPhotoMapper.selectTopNList(map);
    }
}
