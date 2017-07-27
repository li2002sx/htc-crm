package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.constants.SmsType;
import com.htche.crm.constants.TemplateType;
import com.htche.crm.domain.*;
import com.htche.crm.mapper.SmsMapper;
import com.htche.crm.model.SmsHuyiResult;
import com.htche.crm.model.query.SmsHuyi;
import com.htche.crm.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class SmsBiz {

    private static final Logger logger = LoggerFactory.getLogger(SmsBiz.class);

    @Autowired
    SmsMapper smsMapper;

    @Autowired
    CarDealerBiz carDealerBiz;

    @Autowired
    CarOwnerBiz carOwnerBiz;

    @Autowired
    SmsTemplateBiz smsTemplateBiz;

    @Autowired
    SmsConfBiz smsConfBiz;

    @Autowired
    UserBiz userBiz;

    /**
     * 保存
     *
     * @param sms
     * @return
     */
    public boolean save(Sms sms) {
        int effectCount = 0;
        effectCount = smsMapper.insertSelective(sms);
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<Sms> selectAllList(int pageIndex, int pageSize) {
        Page<Sms> smsPage = PageHelper.startPage(pageIndex, pageSize);
        Map<String, String> map = new HashMap<>();
        List<Sms> smses = smsMapper.selectAllList(map);
        return smsPage;
    }

    public Sms selectByPrimaryKey(int smsId) {
        return smsMapper.selectByPrimaryKey(smsId);
    }

    public List<Sms> selectCurDateSmsByMobile(String mobile, int templateId) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("templateId", templateId);
        List<Sms> smses = smsMapper.selectCurDateSmsByMobile(map);
        return smses;
    }

    private final static String _AppId = "cf_mtgg";
    private final static String _AppKey = "d551472451952c45d8d06e8350ac802d";

    //    @Scheduled(cron = "0/20 * *  * * ? ")
    @Scheduled(cron = "0 0 8,9,10,11 * * ?")
    public void sendSms() {
        logger.info("开始执行:{}", LocalDateTime.now());
        //获取当天需要发送短信的车商
        List<CarOwner> carOwners = carOwnerBiz.selectDayBirthdayList();
        if (carOwners != null && carOwners.size() > 0) {
            //获取所有的appId和appKey
            Map<Integer, SmsHuyi> smsHuyiMap = carDealerBiz.getSmsHuyiMap();
            //获取所有的短信模板
            Map<Integer, String> smsTemplateMap = smsTemplateBiz.getAllMap(TemplateType.Wish.getIndex());
            //获取所有的短信配置
            Map<Integer, Integer> templateIdMap = smsConfBiz.getTemplateIdMap();

            carOwners.forEach(owner -> {
                int carOwnerId = owner.getCarOwnerId();
                String name = owner.getName();
                logger.info("找到今天生日的客户：{},{}", carOwnerId, name);
                String mobile = owner.getPhone();
                if (ValidateUtil.isMobile(mobile)) {
                    int carDealerId = owner.getCarDealerId();
                    SmsHuyi smsHuyi = smsHuyiMap.get(carDealerId);
                    int templateId = templateIdMap.get(carDealerId);
                    String content = smsTemplateMap.get(templateId);
                    content = content.replace("{name}", name);
                    SmsHuyiResult result = SmsHuyiUtil.sendSms(mobile, content, smsHuyi.getAppId(), smsHuyi.getAppKey());
                    if (result != null) {
                        String code = result.getCode();
                        if (code.equals("2")) {//成功
                            carDealerBiz.updateSendSmsNum(carDealerId);
                            carOwnerBiz.updateDayBirthday(carOwnerId);
                            logger.info("发送短信成功：{},{}", carOwnerId, name);
                        }
                        this.saveSms(mobile, carOwnerId, templateId, content, result);
                    }
                } else {
                    logger.info("手机号码错误：{},{},{}", carOwnerId, name, mobile);
                }
            });
        }
    }


    public void sendSms(int templateId, String mobiles, HttpServletResponse response) {
        SmsTemplate smsTemplate = smsTemplateBiz.selectByPrimaryKey(templateId);
        if (smsTemplate != null && !StringUtils.isEmpty(smsTemplate.getContent())) {
            String content = smsTemplate.getContent();
            AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
            int roleId = adminUser.getRoleId();
            CarDealer carDealer = carDealerBiz.selectByPrimaryKey(roleId);
            if (carDealer != null) {
                int carDealerId = carDealer.getCarDealerId();
                if (!StringUtils.isEmpty(carDealer.getAppId()) && !StringUtils.isEmpty(carDealer.getAppKey())) {
                    String[] mobileArr = mobiles.split(",");
                    int smsSuccCount = 0;
                    for (String mobile : mobileArr) {
                        if (ValidateUtil.isMobile(mobile)) {
                            List<Sms> smss = selectCurDateSmsByMobile(mobile, templateId);
                            if (smss != null && smss.size() > 0) {
                                smsSuccCount++;
                                CookieUtil.setCookie(response, "sms_succ_count", String.valueOf(smsSuccCount));
                            } else {
                                SmsHuyiResult result = SmsHuyiUtil.sendSms(mobile, content, carDealer.getAppId(), carDealer.getAppKey());
//                            SmsHuyiResult result = new SmsHuyiResult();
//                            result.setCode("2");
                                if (result != null) {
                                    String code = result.getCode();
                                    if (code.equals("2")) {//成功
                                        carDealerBiz.updateSendSmsNum(carDealerId);
                                        smsSuccCount++;
                                        CookieUtil.setCookie(response, "sms_succ_count", String.valueOf(smsSuccCount));
//                                    logger.info("发送短信成功：{}", mobile);
                                    }
                                    this.saveSms(mobile, 0, templateId, content, result);
                                }
                            }
                        } else {
                            logger.info("手机号码错误Ally：{}", mobile);
                        }
                    }
                }
            }
        }
    }

    private void saveSms(String mobile, int carOwnerId, int templateId
            , String content, SmsHuyiResult result) {
        Sms sms = new Sms();
        sms.setCarOwnerId(carOwnerId);
        sms.setTemplateId(templateId);
        sms.setMobile(mobile);
        sms.setContent(content);
        sms.setMsg(result.getMsg());
        sms.setCode(result.getCode());
        sms.setHuyiSmsId(result.getSmsId());
        sms.setCreateTime(new Date());
        this.save(sms);
    }

//    @Scheduled(fixedRate = 1000*2)
//    @Scheduled(cron = "0/5 * *  * * ? ")
//    public void print() {
//        System.out.println("Annotation：print run---" + new Date());
//    }

    public Sms selectByMobileAndTempId(String mobile, int smsType) {
        Map map = new HashMap();
        map.put("mobile", mobile);
        map.put("templateId", smsType);
        return smsMapper.selectByMobileAndTempId(map);
    }


    /**
     * 发送对应类型的验证码
     *
     * @param mobile
     * @param smsType
     * @return
     */
    public void sendMsgCode(String mobile, int smsType) {
        boolean canSend = false;
        if (ValidateUtil.isMobile(mobile)) {
            User user = userBiz.selectByMobile(mobile);
            if (user != null && smsType == SmsType.Regist.getIndex()) {
                throw new RuntimeException("该手机号已经被注册");
            } else if (user == null && smsType == SmsType.FindPass.getIndex()) {
                throw new RuntimeException("该手机号还未注册");
            } else {
                Sms sms = this.selectByMobileAndTempId(mobile, smsType);
                if (sms != null) {
                    long diff = new Date().getTime() - sms.getCreateTime().getTime();
                    if (diff < 1 * 60 * 1000) { //小于1分钟
                        throw new RuntimeException("操作频繁");
                    } else {
                        canSend = true;
                    }
                } else {
                    canSend = true;
                }
                if (canSend) {
                    String msgCode = RandUtil.stringId(4);
                    String content = String.format("您的验证码是:%s，值得您信赖的车源平台", msgCode);
                    SmsHuyiResult result = SmsHuyiUtil.sendSms(mobile, content, _AppId, _AppKey);
                    if (result != null) {
                        String code = result.getCode();
                        if (code.equals("2")) {//成功
                            this.saveSms(mobile, 0, smsType, msgCode, result);
                        } else {
                            throw new RuntimeException(result.getMsg());
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("手机号码格式不正确");
        }
    }

    /**
     * 获取对应的验证码
     *
     * @param mobile
     * @param smsType
     * @return
     */
    public Sms getMsgCode(String mobile, int smsType) {
        Sms sms = null;
        if (ValidateUtil.isMobile(mobile)) {
            sms = this.selectByMobileAndTempId(mobile, smsType);
            if (sms != null) {
                long diff = new Date().getTime() - sms.getCreateTime().getTime();
                if (diff > 10 * 60 * 1000) { //小于10分钟
                    throw new RuntimeException("验证码已超时");
                }
            }
        } else {
            throw new RuntimeException("手机号码格式不正确");
        }
        return sms;
    }
}
