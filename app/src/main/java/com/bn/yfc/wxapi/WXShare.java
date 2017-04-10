package com.bn.yfc.wxapi;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.myapplication.ConfigInfo;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.SignUtils;
import com.bn.yfc.tools.Tools;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/24.
 */

public class WXShare {
    //快捷登录
    public static void WXLogin(Context context) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "heppy newyear";
        MyAppLication.getIwxapi(context).sendReq(req);
    }

    //文字分享
    public static void ToWXText(Context context, String s) {
        WXTextObject text = new WXTextObject(s);
        WXMediaMessage mesg = new WXMediaMessage();
        mesg.description = "邀请码";
        mesg.mediaObject = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = 0;
        req.message = mesg;
        boolean xx = MyAppLication.getIwxapi(context).sendReq(req);
        Tools.setLog("微信分享启动完毕" + xx);
    }

    //网页链接分享
    public static void ToWXHtml(Context context, String htmlUrl, String invtel, Bitmap ic, int s) {

        WXWebpageObject weObject = new WXWebpageObject();
        weObject.webpageUrl = htmlUrl;
        WXMediaMessage mesg = new WXMediaMessage(weObject);
        mesg.title = "创言快递";
        mesg.description = invtel;
        mesg.thumbData = Util.bmpToByteArray(ic, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (s > 0) {
            req.scene = 1;
        } else {
            req.scene = 0;
        }

        req.transaction = buildTransaction("webpage");
        req.message = mesg;
        //测试
        IWXAPI api = null;
        api = MyAppLication.getIwxapi(context);
        if (api != null) {
            Tools.setLog("api不等于空");
            if (req != null) {
                Tools.setLog("req不等于null");
            }
        }

        boolean xx = false;
        xx = req.checkArgs();
        Tools.setLog("微信网页分享测试" + xx);
        api.sendReq(req);

    }

    //微信支付
    public static void toWXPay(Context context, String prepayid, String appid, String partnerid, String timeStamp, String packageValue, String noncestr, String sign) {
        PayReq req = new PayReq();
        req.prepayId = prepayid;
        req.appId = appid;
        req.partnerId = partnerid;
        req.timeStamp = timeStamp;
        req.packageValue = packageValue;
        req.nonceStr = noncestr;
        req.sign = sign;
        Tools.setLog("微信加密sign为1" + sign);
        boolean sss = MyAppLication.getIwxapi(context).sendReq(req);
        Tools.setLog("支付调起" + sss);
    }


    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //面对面微信支付
    public static void sendWXPay(final Context context, String body, String order, String money, String type, String uid, String did) {
        Tools.setLog("开始测试请求|order:" + order + "|body:" + body + "|money:" + money + "|type:" + type + "|uid:" + uid + "|did:" + did);
        AjaxParams par = new AjaxParams();
        par.put("uid", uid);
        par.put("did", did);
        par.put("body", body);
        par.put("order", order);
        par.put("money", money);
        par.put("detail", (String) SharedPreferencesUtils.getParam(context, "invite", ""));
        par.put("attach", type);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTWXPAYURL, par, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("微信支付请求服务器成功 ===\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String appid = object.getString("appid");
                    String noncestr = object.getString("noncestr");
                    String packages = object.getString("package");
                    String partnerid = object.getString("partnerid");
                    String prepayid = object.getString("prepayid");
                    String timestamp = object.getString("timestamp");
                    String sign = object.getString("sign");
                    toWXPay(context, prepayid, appid, partnerid, timestamp, packages, noncestr, sign);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("微信支付请求服务器数据失败===" + strMsg);
            }
        });
    }

    public static void sendWXSign(final Context context, boolean prixif, String body, String order, String money, String type) {
        Tools.setLog("开始测试请求|order:" + order + "|body:" + body + "|money:" + money);
        AjaxParams par = new AjaxParams();
        if (prixif) {
            par.put("body", body);
            par.put("order", order);
            par.put("money", money);
            par.put("detail", (String) SharedPreferencesUtils.getParam(context, "invite", ""));
            par.put("attach", type);
        }
        Tools.setLog("支付生成body:" + body + "|");
        FinalHttp fhp = new FinalHttp();
        fhp.post("http://120.77.87.193/index.php/client/Wxapp/app_pay", par, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("微信支付请求服务器成功 ===" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String appid = object.getString("appid");
                    String noncestr = object.getString("noncestr");
                    String packages = object.getString("package");
                    String partnerid = object.getString("partnerid");
                    String prepayid = object.getString("prepayid");
                    String timestamp = object.getString("timestamp");
                    String sign = object.getString("sign");
                    toWXPay(context, prepayid, appid, partnerid, timestamp, packages, noncestr, sign);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("微信支付请求服务器数据失败===" + strMsg);
            }
        });

    }
}
