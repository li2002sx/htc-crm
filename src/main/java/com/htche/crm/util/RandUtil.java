package com.htche.crm.util;

import com.htche.crm.constants.BusinessType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jankie on 2017/5/23.
 */
public class RandUtil {

    /**
     * 随机N位
     * @param num
     * @return
     */
    public static final String stringId(int num) {
        String randomStr = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, num - 1)));//随机四位
        return randomStr;
    }

    public static final String stringId18() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateStr = dateFormat.format(new Date());
        String randomStr = stringId(3);//随机三位
        return dateStr + randomStr;
    }

    public static final String getUUID() {
        UUID uuid = UUID.randomUUID();
        return  uuid.toString();
    }
    /**
     * 根据业务类型生成订单号
     *
     * @param businessType
     * @return
     */
    public static String createOrderNumber(BusinessType businessType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateStr = dateFormat.format(new Date());
        String randomStr = stringId(3);//随机3位
        return String.format("%s%02d%s", dateStr, businessType.getIndex(), randomStr);
    }
}
