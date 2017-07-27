package com.htche.crm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lishengxi on 2017/7/20.
 */
public class StringUtil {

    public static final String empty = "";
    public static final String newLine = System.getProperty("line.separator");
    public static final char UNDERLINE = '_';

    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 65 && firstChar <= 90) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 97 && firstChar <= 122) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] - 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isBlank(Object str) {
        return str == null || str instanceof String && ((String)str).isEmpty();
    }

    public static boolean notBlank(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean notBlank(String... strings) {
        if(strings == null) {
            return false;
        } else {
            String[] var1 = strings;
            int var2 = strings.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String str = var1[var3];
                if(str == null || str.isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }

    public static <T> String ArrayToString(Iterable<T> inputs, String separator) {
        if(inputs == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            Object input;
            for(Iterator var3 = inputs.iterator(); var3.hasNext(); sb.append(input.toString())) {
                input = var3.next();
                if(sb.length() > 0) {
                    sb.append(separator);
                }
            }

            return sb.toString();
        }
    }

    public static String camelToUnderline(String param) {
        if(param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if(Character.isUpperCase(c)) {
                    sb.append('_');
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underlineToCamel(String param) {
        if(param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if(c == 95) {
                    ++i;
                    if(i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underlineToCamel2(String param) {
        if(param != null && !"".equals(param.trim())) {
            StringBuilder sb = new StringBuilder(param);
            Matcher mc = Pattern.compile("_").matcher(param);
            int i = 0;

            while(mc.find()) {
                int position = mc.end() - i++;
                sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String replaceBlank(String str) {
        if(!StringUtils.isEmpty(str)) {
            str = str.trim();
            str = str.replaceAll("(\r\n|\r|\n|\n\r)", "");
        }

        return str;
    }

    public static boolean isLetter(char c) {
        short k = 128;
        return c / k == 0;
    }

    public static int length(String s) {
        if(s == null) {
            return 0;
        } else {
            char[] c = s.toCharArray();
            int len = 0;

            for(int i = 0; i < c.length; ++i) {
                ++len;
                if(!isLetter(c[i])) {
                    ++len;
                }
            }

            return len;
        }
    }
}
