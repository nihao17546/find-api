package com.appcnd.find.api.util;

/**
 * @author nihao 2018/11/23
 */
public class Strings {

    public static boolean equals(String s1, String s2){
        if (s1 != null) {
            return s1.equals(s2);
        }
        else if (s2 != null) {
            return false;
        }
        return true;
    }

}
