package com.htche.crm.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * TODO: just quick workaround, to enhance soon
 */
public class DateFormatter implements Formatter<Date> {

    @Override
    public Date parse(String s, Locale locale) throws ParseException {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        // TODO: format to configurable
        s = s.trim();
        return DateUtils.parseDate(s, "yyyy-MM-dd");
    }

    @Override
    public String print(Date date, Locale locale) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }
}
