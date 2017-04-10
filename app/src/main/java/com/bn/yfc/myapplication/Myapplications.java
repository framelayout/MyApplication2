package com.bn.yfc.myapplication;

import android.app.Application;
import android.content.Context;

import com.bn.yfc.R;
import com.bn.yfc.base.Tools;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/16.
 */

public class Myapplications extends Application {
    private Context context;
    private static IWXAPI iwxapi;

    @Override

    public void onCreate() {
        context = this;
        super.onCreate();
        iwxapi = WXAPIFactory.createWXAPI(this, getResources().getResourceName(R.string.WXAPPID));
        boolean aaawx = iwxapi.registerApp(getResources().getResourceName(R.string.WXAPPID));
        Tools.setLog("初始化微信" + aaawx);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        Tools.setLog("初始化极光");
        UMShareAPI.get(this);
        Tools.setLog("初始化友盟");
        x.Ext.init(this);
    }

    {
        PlatformConfig.setWeixin("wx8556f3f6937eacca", "576a6129c82e161689ba6e04dbcc7219");
    }

    // public static IWXAPI getIwxapi() {
    //   return iwxapi;
    // }
}
