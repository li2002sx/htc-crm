package com.htche.crm.controller.rest;

import com.htche.crm.biz.RechargeProductBiz;
import com.htche.crm.biz.RechargeRecordBiz;
import com.htche.crm.biz.UserRechargeRecordBiz;
import com.htche.crm.domain.RechargeProduct;
import com.htche.crm.domain.User;
import com.htche.crm.domain.UserRechargeRecord;
import com.htche.crm.domain.pay.UnifiedOrderResult;
import com.htche.crm.model.ApiResult;
import com.htche.crm.model.rest.UserRechargeModel;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jankie on 16/5/1.
 */
@RestController
@RequestMapping("/rest/userrecharge")
public class UserRechargeRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRechargeRestController.class);

    @Autowired
    RechargeProductBiz rechargeProductBiz;

    @Autowired
    UserRechargeRecordBiz userRechargeRecordBiz;

    @Autowired
    RechargeRecordBiz rechargeRecordBiz;

    @RequestMapping(value = "products", method = RequestMethod.GET)
    public UserRechargeModel.RechargeProductList getRechargeProductList() {
        UserRechargeModel.RechargeProductList rechargeProductList = new UserRechargeModel.RechargeProductList();
        rechargeProductList.setStatus(1);
        List<RechargeProduct> rechargeProducts = rechargeProductBiz.selectAllList();
        rechargeProductList.setRechargeProductList(rechargeProducts);
        return rechargeProductList;
    }

    @RequestMapping(value = "userrecharge", method = RequestMethod.GET)
    public UserRechargeModel.UseRechargeResult userRecharge(
            @RequestParam(value = "productId", required = true) int productId,
            @RequestParam(value = "openId", required = false) String openId) {
        UserRechargeModel.UseRechargeResult useRechargeResult = new UserRechargeModel.UseRechargeResult();
        useRechargeResult.setStatus(0);
        CurrentUser currentUser = CurrentUser.getInstance();
        HttpServletRequest request = currentUser.getRequest();
        User user = currentUser.getUser();
        if (user != null) {
            int userId = user.getUserId();
            String ip = HttpUtil.getIp(request);
            useRechargeResult = userRechargeRecordBiz.userRecharge(userId, productId, openId, ip);
            if (useRechargeResult != null && useRechargeResult.getUnifiedOrderResult() != null) {
                useRechargeResult.setStatus(1);
            }
        } else {
            useRechargeResult.setMessage("没有找到对应的用户");
        }
        return useRechargeResult;
    }

    @RequestMapping(value = "wxorderquery", method = RequestMethod.GET)
    public ApiResult wxOrderQuery(
            @RequestParam(value = "prepayId", required = false) String prepayId) {
        ApiResult apiResult = new ApiResult();
        boolean paySucc = rechargeRecordBiz.wxOrderQuery(prepayId);
        apiResult.setStatus(paySucc ? 1 : 0);
        return apiResult;
    }

    @RequestMapping(value = "wxnotify", method = RequestMethod.POST)
    public UserRechargeModel.WxNotifyResult wxNotify() {
        UserRechargeModel.WxNotifyResult wxNotifyResult = new UserRechargeModel.WxNotifyResult();
        wxNotifyResult.setReturnCode("FAIL");
        try {
            CurrentUser currentUser = CurrentUser.getInstance();
            HttpServletRequest request = currentUser.getRequest();
            String xmlContent = HttpUtil.getContent(request.getInputStream(), "utf-8");
            logger.info("微信通知结果:{}", xmlContent);
            boolean paySucc = rechargeRecordBiz.wxNotify(xmlContent);
            if (paySucc) {
                wxNotifyResult.setReturnCode("SUCCESS");
                wxNotifyResult.setReturnMsg("OK");
            } else {
                wxNotifyResult.setReturnMsg("通知失败");
            }
        } catch (Exception ex) {
            wxNotifyResult.setReturnMsg(ex.getMessage());
        }
        return wxNotifyResult;
    }
}
