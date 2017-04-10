package com.bn.yfc.myapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bn.yfc.myapplication.ConfigInfo;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Login;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MyAppLication extends Application {
    private static RequestQueue mQueue;
    private static Stack<Activity> activityMea = new Stack<Activity>();
    private static IWXAPI iwxapi;
    // private TagAliasCallback tags = null;

    @Override

    public void onCreate() {
        super.onCreate();
        initTimer();
    }

    //开启线程初始化，减少启动时间
    private void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Tools.setLog("启动线程");
                init();
            }
        }, 0);
    }

    public void init() {
        x.Ext.init(this);
        x.Ext.setDebug(false);
        boolean aaawx = reWX(this);
        Tools.setLog("初始化微信" + aaawx);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        //JPushInterface.set
        // 初始化 JPush
        Tools.ReigeJPush(this);
        Tools.setLog("极光获取状态为:" + JPushInterface.getConnectionState(this) + "|极光是否出现异常: " + JPushInterface.isPushStopped(this) + "|注册的别名" + SharedPreferencesUtils.getParam(this, "invite", ""));
        Tools.setLog("初始化极光");
        UMShareAPI.get(this);
        Tools.setLog("初始化友盟");
        mQueue = Volley.newRequestQueue(this);
        Volley.newRequestQueue(this);

    }

    private TagAliasCallback setTagAliasCallback() {
        String s = null;
        TagAliasCallback tagaliascallback = new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                switch (i) {
                    case 0:
                        Tools.setLog("\n别名设置成功\n");
                        break;
                    case 6002:
                        Tools.setLog("\n网络不好，别名设置失败\n");
                        break;
                }
            }
        };
        return tagaliascallback;
    }

    public static RequestQueue getmQueue() {
        if (mQueue != null) {
            return mQueue;
        }
        return null;
    }

    //进栈
    public static void addActivityStack(Activity a) {
        if (activityMea == null) {
            activityMea = new Stack<Activity>();
        }
        activityMea.add(a);
    }

    //清除
    public static void removActivity(Activity a) {
        if (a != null) {
            activityMea.remove(a);
            a.finish();
            a = null;
        }
    }

    //退出所有
    public static void allExit() {
        for (Activity activity : activityMea) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static boolean reWX(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, ConfigInfo.WXAPPID, true);
        boolean ifswx = iwxapi.registerApp(ConfigInfo.WXAPPID);
        return ifswx;
    }

    public static IWXAPI getIwxapi(Context context) {
        if (iwxapi.isWXAppInstalled()) {

        } else {
            reWX(context);
        }

        return iwxapi;
    }

}
