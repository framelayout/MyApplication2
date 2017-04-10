package com.bn.yfc.user;

import android.content.Context;

import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;


/**
 * Created by Administrator on 2017/2/4.
 * 权限判定
 */

public class Permissions {

    /**
     * 用户登录权限判定
     */
    public static boolean userLoginPermissions(Context context) {
        if (SharedPreferencesUtils.getParam(context, "islogin", "").equals("1")) {
            Tools.setLog("当权用户是否登录==" + SharedPreferencesUtils.getParam(context, "islogin", ""));
            return true;
        }
        return false;
    }

    //true有新消息
    public static boolean newsDataPermissions(Context context) {
        if (SharedPreferencesUtils.getParam(context, "news", "").equals("1")) {
            return true;
        }
        return false;
    }

    //判断完善资料
    public static boolean dataPermissins(Context context) {
        if (SharedPreferencesUtils.getParam(context, "isdata", "").equals("1")) {
            return true;
        }
        return false;
    }


}
