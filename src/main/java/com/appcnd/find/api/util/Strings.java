package com.appcnd.find.api.util;

import com.appcnd.find.api.pojo.StaticConstant;

/**
 * @author nihao 2018/11/23
 */
public class Strings {

    public static boolean equals(String s1, String s2) {
        if (s1 != null) {
            return s1.equals(s2);
        }
        else if (s2 != null) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().equals("");
    }

    public static String compileUrl(String url) {
        if (isEmpty(url)) {
            return url;
        }
        return url.replace("${img}", StaticConstant.IMG_PREFIX)
                .replace("${mydata}", StaticConstant.MYDATA_PREFIX)
                .replace("${fdfs}", StaticConstant.FDFS_PREFIX)
                .replace("${activity}", StaticConstant.ACTIVITY_PREFIX);
    }

}
