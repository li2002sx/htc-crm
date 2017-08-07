package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.constants.BusinessType;
import com.htche.crm.constants.PayStatus;
import com.htche.crm.constants.RechargeType;
import com.htche.crm.domain.RechargeProduct;
import com.htche.crm.domain.UserRechargeRecord;
import com.htche.crm.domain.pay.BusinessOrderParam;
import com.htche.crm.domain.pay.UnifiedOrderResult;
import com.htche.crm.mapper.UserRechargeRecordMapper;
import com.htche.crm.model.query.UserRechargeRecordQuery;
import com.htche.crm.model.rest.UserRechargeModel;
import com.htche.crm.util.AmountUtil;
import com.htche.crm.util.RandUtil;
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
public class UserRechargeRecordBiz {

    @Autowired
    UserRechargeRecordMapper userRechargeRecordMapper;

    @Autowired
    RechargeProductBiz rechargeProductBiz;

    @Autowired
    RechargeRecordBiz rechargeRecordBiz;

    /**
     * 用户充值
     *
     * @param userId
     * @return
     */
    public UserRechargeModel.UseRechargeResult userRecharge(int userId, int productId
            , String openId, String ip) {
        UserRechargeModel.UseRechargeResult useRechargeResult = new UserRechargeModel.UseRechargeResult();

        RechargeProduct rechargeProduct = rechargeProductBiz.selectByPrimaryKey(productId);
        if (rechargeProduct != null) {
            int amount = rechargeProduct.getPrice();
            UserRechargeRecord userRechargeRecord = new UserRechargeRecord();
            userRechargeRecord.setUserId(userId);
            String orderNumber = RandUtil.createOrderNumber(BusinessType.Virtual);
            userRechargeRecord.setOrderNumber(orderNumber);
            userRechargeRecord.setAmount(amount);
            userRechargeRecord.setStatus(PayStatus.Init.getIndex());
            userRechargeRecord.setCreateTime(new Date());
            int effectCount = userRechargeRecordMapper.insertSelective(userRechargeRecord);
            if (effectCount >= 0) {
                BusinessOrderParam businessOrderParam = new BusinessOrderParam();
                businessOrderParam.setAmount(amount);
                businessOrderParam.setBusinessType(BusinessType.Virtual);
                businessOrderParam.setOpenId(openId);
                businessOrderParam.setIp(ip);
                businessOrderParam.setRechargeType(RechargeType.Wx);
                businessOrderParam.setRemark(String.format("%f元金额充值", AmountUtil.centToYuan(amount)));
                businessOrderParam.setOrderNumber(orderNumber);
                UnifiedOrderResult unifiedOrderResult = rechargeRecordBiz.unifiedOrder(businessOrderParam);
                useRechargeResult.setUnifiedOrderResult(unifiedOrderResult);
            } else {
                throw new RuntimeException("创建用户流水失败");
            }
        } else {
            throw new RuntimeException("没有找到对应的产品信息");
        }
        return useRechargeResult;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<UserRechargeRecord> selectAllList(UserRechargeRecordQuery query) {
        Page<UserRechargeRecord> userRechargeRecordPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("userId", query.getUserId());
        List<UserRechargeRecord> userRechargeRecords = userRechargeRecordMapper.selectAllList(map);
        return userRechargeRecordPage;
    }

    public UserRechargeRecord selectByPrimaryKey(int userRechargeRecordId) {
        return userRechargeRecordMapper.selectByPrimaryKey(userRechargeRecordId);
    }

    public boolean updateStatus(int userRechargeRecordId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userRechargeRecordId", userRechargeRecordId);
        map.put("status", status);
        return userRechargeRecordMapper.updateStatus(map) > 0;
    }

    public UserRechargeRecord selectByOrderNumber(String orderNumber) {
        return userRechargeRecordMapper.selectByOrderNumber(orderNumber);
    }

    public int updateStatusByOrderNumber(String orderNumber, int status, Date finishTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNumber", orderNumber);
        map.put("status", status);
        map.put("finishTime", finishTime);
        return userRechargeRecordMapper.updateStatusByOrderNumber(map);
    }
}
