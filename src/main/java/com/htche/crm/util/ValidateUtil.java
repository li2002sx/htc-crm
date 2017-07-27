package com.htche.crm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jankie on 16/5/24.
 */
public class ValidateUtil {

    public static boolean isMobile(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Pattern p = Pattern.compile("^(1[0-9])\\d{9}$");
            Matcher m = p.matcher(input);
            return m.find();
        }
        return false;
    }

    public static boolean isNum(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Pattern p = Pattern.compile("^\\d+$");
            Matcher m = p.matcher(input);
            return m.find();
        }
        return false;
    }

    /**
     * 是否是身份证验证
     *
     * @param input
     * @return
     */
    public static boolean isIdNum(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
            Matcher m = p.matcher(input);
            return m.find();
        }
        return false;
    }

    public static boolean isLettleAndNum(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Pattern p = Pattern.compile("^[A-Za-z0-9\\-]+$");
            Matcher m = p.matcher(input);
            return m.find();
        }
        return false;
    }

    public static boolean isAddTime(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Pattern p = Pattern.compile("^\\d+[D|M|Y]$");
            Matcher m = p.matcher(input);
            return m.find();
        }
        return false;
    }
}
