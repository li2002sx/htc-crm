package com.htche.crm.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.htche.crm.biz.AdminUserBiz;
import com.htche.crm.biz.CarDealerBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.CarDealer;
import com.htche.crm.model.AjaxResult;
import com.htche.crm.model.query.AdminUserQuery;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.ViewResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import java.util.Map;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/adminuser")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    AdminUserBiz adminUserBiz;

    @Autowired
    CarDealerBiz carDealerBiz;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView model = new ViewResult("custom/adminuser/list");
        return model;
    }

    @RequestMapping(value = "edit/{adminUserId:\\d+}")
    public ModelAndView edit(@PathVariable int adminUserId) {
        ModelAndView model = new ViewResult("custom/adminuser/edit");
        AdminUser adminUser = new AdminUser();
        if (adminUserId > 0) {
            adminUser = adminUserBiz.selectByPrimaryKey(adminUserId);
        }
        model.addObject("adminUser", adminUser);
        Map<Integer, String> carDealerMap = carDealerBiz.getAllMap();
        model.addObject("carDealerMap", carDealerMap);
        return model;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(AdminUser adminUser) {
        AjaxResult ajaxResult = new AjaxResult();
        String random = "";
        if (adminUser.getAdminUserId() == 0) {
            //随机6位数字密码
            random = RandomStringUtils.randomNumeric(6);
            String password = DigestUtils.md5Hex(random);
            adminUser.setPassword(password);
        }
        adminUser.setCreateTime(new Date());
        boolean flag = adminUserBiz.save(adminUser);
        ajaxResult.setSuccess(flag);
        ajaxResult.setValue(random);
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
    public JSONObject listdata(AdminUserQuery query) {

        Map<Integer, String> carDealerMap = carDealerBiz.getAllMap();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonItem = null;

        Page<AdminUser> adminUserPage = adminUserBiz.selectAllList(query.getUserName(), query.getPageIndex(), query.getPageSize());
        //封装前台数据
        if (adminUserPage != null && adminUserPage.getResult() != null) {
            for (AdminUser adminUser : adminUserPage.getResult()) {
                jsonItem = new JSONObject();
                jsonItem.put("adminUserId", adminUser.getAdminUserId());
                jsonItem.put("userName", adminUser.getUserName());
                jsonItem.put("roleName", carDealerMap.get(adminUser.getRoleId()));
                Date lastLoginTime = adminUser.getLastLoginTime();
                if (lastLoginTime != null) {
                    jsonItem.put("lastLoginTime", DateFormatUtils.format(lastLoginTime, "yyyy-MM-dd HH:mm:ss"));
                }
                jsonItem.put("remark", adminUser.getRemark());
                jsonItem.put("createTime", DateFormatUtils.format(adminUser.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                jsonArray.add(jsonItem);
            }
        }
        jsonObject.put("total", adminUserPage.getTotal());
        jsonObject.put("rows", jsonArray);
        return jsonObject;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(int adminUserId) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean flag = adminUserBiz.updateStatus(adminUserId, CommonStatus.Deleted.getIndex());
        ajaxResult.setSuccess(flag);
        return ajaxResult;
    }

    /**
     * 修改用户密码
     *
     * @return
     */
    @RequestMapping(value = "updatepassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updatePassword(String oldPwd, String newPwd) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            adminUser = adminUserBiz.selectByPrimaryKey(adminUser.getAdminUserId());
            if (DigestUtils.md5Hex(oldPwd).equals(adminUser.getPassword())) {
                String newPass = DigestUtils.md5Hex(newPwd);
                boolean flag = adminUserBiz.updatePassword(adminUser.getAdminUserId(), newPass);
                ajaxResult.setSuccess(flag);
            } else {
                ajaxResult.setError("原始密码错误");
            }
        } catch (Exception ex) {
            ajaxResult.setError(ex.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 修改用户密码
     *
     * @return
     */
    @RequestMapping(value = "resetpassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult resetPassword(int adminUserId) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            //随机6位数字密码
            String random = RandomStringUtils.randomNumeric(6);
            String password = DigestUtils.md5Hex(random);
            boolean flag = adminUserBiz.updatePassword(adminUserId, password);
            ajaxResult.setSuccess(flag);
            ajaxResult.setValue(random);
        } catch (Exception ex) {
            ajaxResult.setError(ex.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping(value = "editbase")
    public ModelAndView editbase() {
        ModelAndView model = new ViewResult("custom/adminuser/editbase");
        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
        adminUser = adminUserBiz.selectByPrimaryKey(adminUser.getAdminUserId());
        String picUrl = adminUser.getUserPic();
        model.addObject("picUrl", picUrl);
        model.addObject("realPicUrl", ImageUtil.getRealPicUrl(picUrl, false));
        int roleId = adminUser.getRoleId();
        CarDealer carDealer = carDealerBiz.selectByPrimaryKey(roleId);
        if (carDealer != null) {
            model.addObject("companyName", carDealer.getCompanyName());
        }
        return model;
    }

    @RequestMapping(value = "savebase", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult saveBase(String companyName, String picUrl) {
        AjaxResult ajaxResult = new AjaxResult();
        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
        int adminUserId = adminUser.getAdminUserId();
        int roleId = adminUser.getRoleId();
        adminUserBiz.updatePicUrl(adminUserId, picUrl);
        carDealerBiz.updateCompanyName(roleId, companyName);

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        adminUser.setCompanyName(companyName);
        adminUser.setUserPic(ImageUtil.getRealPicUrl(picUrl, true));
        session.setAttribute("user", adminUser);

        ajaxResult.setSuccess(true);
        return ajaxResult;
    }
}
