package com.njcwking.box.crop.utils;

import java.util.regex.Pattern;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/13
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */
public class ValidationUtils {
    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
