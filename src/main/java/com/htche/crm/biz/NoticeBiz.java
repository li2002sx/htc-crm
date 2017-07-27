package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.Notice;
import com.htche.crm.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class NoticeBiz {

    @Autowired
    NoticeMapper noticeMapper;

    /**
     * 保存
     *
     * @param notice
     * @return
     */
    public boolean save(Notice notice) {
        int effectCount = 0;
        if (notice.getNoticeId() > 0) {
            effectCount = noticeMapper.updateByPrimaryKeySelective(notice);
        } else {
            effectCount = noticeMapper.insertSelective(notice);
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
    public Page<Notice> selectAllList(int carDealerId, int pageIndex, int pageSize) {
        Page<Notice> noticePage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("carDealerId", carDealerId);
        List<Notice> notices = noticeMapper.selectAllList(map);
        return noticePage;
    }

    public Notice selectByPrimaryKey(int noticeId) {
        return noticeMapper.selectByPrimaryKey(noticeId);
    }

    public boolean updateStatus(int noticeId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("noticeId", noticeId);
        map.put("status", status);
        return noticeMapper.updateStatus(map) > 0;
    }
}
