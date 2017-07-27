package com.htche.crm.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TODO: just quick workaround, to enhance soon
 */
public class DateUtil {

    public static Date strToDate(String str) {
        try {
            String e = String.format("%s-%s-%s %s:%s:%s", new Object[]{str.substring(0, 4), str.substring(4, 6), str.substring(6, 8), str.substring(8, 10), str.substring(10, 12), str.substring(12, 14)});
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(e);
            return date;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }
}
