package com.bn.yfc.base;

/**
 * Created by Administrator on 2016/11/16.
 */

public class TypeConversion {

    public static boolean isNumber(String str) {

        return str.matches("@\"^\\d+([.]\\d+)?$\"");
    }

}
