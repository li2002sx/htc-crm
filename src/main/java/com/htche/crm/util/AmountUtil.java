package com.htche.crm.util;

import com.htche.crm.constants.BusinessType;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jankie on 2017/5/23.
 */
public class AmountUtil {

    public static final int CentYuanBase = 100;

    public static int yuanToCent(double yuan) {
        return (int) (yuan * 100.0D);
    }

    public static double centToYuan(double cent) {
        return cent / 100.0D;
    }
}
