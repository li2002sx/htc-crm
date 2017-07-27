package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.NoticeBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.Notice;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.NoticeQuery;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ViewResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    NoticeBiz noticeBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/notice/list");
        return model;
    }

    @RequestMapping(value = "edit/{noticeId:\\d+}")
    public ModelAndView edit(@PathVariable int noticeId) {
        ModelAndView model = new ViewResult("custom/notice/edit");
        Notice notice = new Notice();
        if (noticeId > 0) {
            notice = noticeBiz.selectByPrimaryKey(noticeId);
        }
        model.addObject("notice", notice);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(Notice notice) {
        AjaxResult ajaxResult = new AjaxResult();
        if (notice.getNoticeId() == 0) {
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            notice.setCarDealerId(adminUser.getRoleId());
            notice.setCreateTime(new Date());
        }
        boolean flag = noticeBiz.save(notice);
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    /**
     * 获取新闻数据，用于异步加载
     *
     * @param query 查询实体
     * @return
     */
    @RequestMapping(value = "/listdata")
    @ResponseBody
    public JSONObject listdata(NoticeQuery query) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();

        Page<Notice> noticePage = noticeBiz.selectAllList(adminUser.getRoleId(), query.getPageIndex(), query.getPageSize());
        //封装前台数据
        if (noticePage != null && noticePage.getResult() != null) {
            for (Notice notice : noticePage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("noticeId", notice.getNoticeId());
                jsonItem.put("title", notice.getTitle());
                jsonItem.put("createTime", DateFormatUtils.format(notice.getCreateTime(), "yyyy-MM-dd"));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", noticePage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int noticeId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = noticeBiz.updateStatus(noticeId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }
}
