package com.bn.yfc.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bn.yfc.fragmentmain.FragmentMain;
import com.bn.yfc.map.ReleaseDelivery;
import com.bn.yfc.map.ReleaseDelivery1;
import com.bn.yfc.map.Shipping;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Balance;
import com.bn.yfc.user.OrdersTest;
import com.bn.yfc.user.Recharge;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/**
 * Created by Administrator on 2017/2/5.
 */

public class AliPayTools {
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016120103674872";
    // 商户收款账号
    public static final String SELLER = "mltxvj0118@sandbox.com";

    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDnXwKfzhOiw8g88UtPJQ1YodPgf2N56weaM7zQnRUrAolbat54JBZMewcchHy/DRZTnHtUVwivagl3/hfIWoS0DJRFQEk/Pp3mWfKNZpUWovOLL8rM6GAoFmkGtgEScrk4pjazaJRVz2/KUT1Zmjk54n8weVCHsB/B6KQOFu9z0wIDAQABAoGAWeTxSROxT2EQEJWTtAlqWRBvGN+Fz93AU2pcpxURH2fZ5FnuW/FxdH+scKkNKtGz5gJ7bHZwWm8xhJyFswNwYSL8wSkDNiIvP54YnSnVIycsMVojld1QfQiIhROr2Urj/yGo3xoRc7hG6/9vyz5W5Onbonw5IIu7uO0nmpjLUckCQQD6RvvnVTvEWVrQCoOwZkX1hh/r6pC4XzdlGNaIEFcfLV8wNW1J2PH8lKydWNOZzXSXHx/jszBJwbozjr4UZbbHAkEA7KlblmDQexF2tCkDV9W/pzsVmnhPFGHgH62m+7t/SaPwKniBHcqafSkw7AZwKdddK3BZNuv256V+94De4WlelQJBAIgNEl7c3JKOKGmzbYb08c7YTH4IkwdG62wPpn9sso+GMJqaUIC2aEVv5K3cXZrcLyb/imxTOkq9/ySL0hRwfn0CQAhfycTdciMYsBmZ5etiveBd/9qybSHsgLC+RU1dChkB+wqVrwPt2l+ZaIjBhZCFdV+HHzEJUnhJ6984KSrSvq0CQQC/D0E6BDGfsE8r5mux3AhPlUomkeZjP7QGyt+zuXyRGUnP0n3E/xPCbPrZ15eg3mjyw26HIK241SnmdOWyHoo3";
    public static final String RSA_PRIVATT = "MIICXQIBAAKBgQDnXwKfzhOiw8g88UtPJQ1YodPgf2N56weaM7zQnRUrAolbat54JBZMewcchHy/DRZTnHtUVwivagl3/hfIWoS0DJRFQEk/Pp3mWfKNZpUWovOLL8rM6GAoFmkGtgEScrk4pjazaJRVz2/KUT1Zmjk54n8weVCHsB/B6KQOFu9z0wIDAQABAoGAWeTxSROxT2EQEJWTtAlqWRBvGN+Fz93AU2pcpxURH2fZ5FnuW/FxdH+scKkNKtGz5gJ7bHZwWm8xhJyFswNwYSL8wSkDNiIvP54YnSnVIycsMVojld1QfQiIhROr2Urj/yGo3xoRc7hG6/9vyz5W5Onbonw5IIu7uO0nmpjLUckCQQD6RvvnVTvEWVrQCoOwZkX1hh/r6pC4XzdlGNaIEFcfLV8wNW1J2PH8lKydWNOZzXSXHx/jszBJwbozjr4UZbbHAkEA7KlblmDQexF2tCkDV9W/pzsVmnhPFGHgH62m+7t/SaPwKniBHcqafSkw7AZwKdddK3BZNuv256V+94De4WlelQJBAIgNEl7c3JKOKGmzbYb08c7YTH4IkwdG62wPpn9sso+GMJqaUIC2aEVv5K3cXZrcLyb/imxTOkq9/ySL0hRwfn0CQAhfycTdciMYsBmZ5etiveBd/9qybSHsgLC+RU1dChkB+wqVrwPt2l+ZaIjBhZCFdV+HHzEJUnhJ6984KSrSvq0CQQC/D0E6BDGfsE8r5mux3AhPlUomkeZjP7QGyt+zuXyRGUnP0n3E/xPCbPrZ15eg3mjyw26HIK241SnmdOWyHoo3";
    private static final int SDK_PAY_FLAG = 1;
    private static Activity conac;
    static Intent intent;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Tools.setLog("支付成功");
                        if (!Tools.isEmpty((String) SharedPreferencesUtils.getParam(conac.getApplication(), "ispay", ""))) {

                            Tools.setLog("支付类型是" + SharedPreferencesUtils.getParam(conac.getApplication(), "ispay", ""));
                            switch ((String) SharedPreferencesUtils.getParam(conac.getApplication(), "ispay", "")) {
                                case "0":
                                    Tools.setLog("订单支付为余额");
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                                case "1":
                                    Tools.setLog("订单支付为快递");
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                                case "2":
                                    Tools.setLog("订单支付为顺路送");
                                    ((ReleaseDelivery) conac).ifPay();
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                                case "3":
                                    Tools.setLog("订单支付为帮我买");
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                                case "4":
                                    Tools.setLog("订单支付为船运");
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                                case "5":
                                    Tools.setLog("订单支付为当面付");
                                    intent = new Intent();
                                    intent.setAction("closealipay");
                                    conac.sendBroadcast(intent);
                                    break;
                            }
                            SharedPreferencesUtils.setParam(conac.getApplication(), "ispay", "");
                        } else {
                            Tools.setLog("支付宝获取ispay为空");
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Tools.setLog("支付取消");

                    }

                    break;
            }
        }
    };

    public static void AlipaySend(final Activity context, String privateKey, String app_id, String money, String notify_url, String out_trade_no) {
        conac = context;
        Tools.showToast(context, "开始请求支付宝");
        String payInfo = "";
        String body = null;
        String subject = "";
        switch ((String) SharedPreferencesUtils.getParam(context.getApplication(), "ispay", "")) {
            case "0":
                body = "cz";
                subject = "充值";
                break;
            case "1":
                body = "ddzf";
                subject = "发布快递";
                break;
            case "2":
                body = "ddzf";
                subject = "发布快递";
                break;
            case "3":
                body = "ddzf";
                subject = "发布快递";
                break;
            case "4":
                body = "ddzf";
                subject = "发布快递";
                break;
            case "5":
                body = "face_zhifu";
                subject = "当面支付";
                break;
        }
        Tools.setLog("支付宝支付money为" + money);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(app_id, subject, body, money, notify_url, out_trade_no, false);
        //请求支付参数
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //加签后参数
        String sign = OrderInfoUtil2_0.getSign(params, privateKey/*RSA_PRIVATE*/, false);
        //拼接完参数
        final String orderInfo = orderParam + "&" + sign;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask paytask = new PayTask(context);
                Map<String, String> msg = paytask.payV2(orderInfo, true);
                Message mes = new Message();
                mes.obj = msg;
                Bundle bundle = new Bundle();
                bundle.putSerializable("test", "测试");
                mes.setData(bundle);
                mes.what = 0;
                handler.handleMessage(mes);
            }
        };
        Thread payThread = new Thread(runnable);
        payThread.start();
    }

    public static void startPay(final Activity context, final String ordersInfo) {

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = combinationOrderInfo(APPID);
        String payInfos = "";
     /*   try {
            payInfos = URLEncoder.encode(payInfo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        //最终支付参数
        final String finalPayInfos = ordersInfo;
        Tools.setLog("拼接完的参数为\n" + finalPayInfos);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask paytask = new PayTask(context);
                String msg = paytask.pay(finalPayInfos/*ordersInfo*/, true);
                Message mes = new Message();
                mes.obj = msg;
                mes.what = 0;
                handler.handleMessage(mes);
            }
        };
        Thread payThread = new Thread(runnable);
        payThread.start();
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE, false);
    }

    private static String combinationOrderInfo(String app_id) {
        String orderInfo = "app_id=" + app_id;
        orderInfo += "&biz_content=" + businessOrderInfo("测试商品", "CYKD测试商品", Tools.getTimeStamp() + Tools.getRandomString(4), "0.01");
        orderInfo += "&charset=utf-8";
    /*    orderInfo += "&format=json";*/
        orderInfo += "&method=alipay.trade.app.pay";
        orderInfo += "&notify_url=http://120.77.87.193/index.php/client/Alipay/back";
        orderInfo += "&sign_type=RSA";
        orderInfo += "&timestamp=" + Tools.getCurrentTime();
        orderInfo += "&version=" + "1.0";
        Tools.setLog("待签参数====\n" + orderInfo);
        String payUrl = "";
        try {
            payUrl = URLEncoder.encode(orderInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String signs = "";
        signs = sign(orderInfo);
        payUrl += "&sign=" + signs;
        Tools.setLog("签名为===\n" + signs);
        Tools.setLog("拼接签名后参数为:\n" + orderInfo);
        //return TestOrderInfo("");
        return orderInfo;
    }

    // body         :商品描述
    // subject      :订单标题
    // out_trade_no :服务端订单唯一编号
    //prix         :商品价格
    //单独转义参数
    private static String businessOrderInfo(String body, String subject, String out_trade_no, String prix) {
        String yi = "\"";
        String business = "{\"body\":" + "\"" + body + "\"";
        business += ",\"subject\":" + "\"" + subject + "\"";
        business += ",\"out_trade_no\":" + "\"" + out_trade_no + "\"";
        business += ",\"timeout_express\":" + "\"" + "2m" + "\"";
        business += ",\"total_amount\":" + "\"" + prix + "\"";
        //收款账户  默认为商户签约账户
        /*business+=",seller_id:QUICK_MSECURITY_PAY";*/
        business += ",\"product_code\":\"QUICK_MSECURITY_PAY\"";
        //商品类型:0虚拟|1实物
        /*business+=",goods_type:0";*/
        //回传参数 用以用户回调地址验证
        /*business += ",passback_param:";*/
        //业务拓展参数
        /*business += ",promo_params:";*/
        //可用去掉
        /*business += ",extend_params:";*/
        business += "}";
        Tools.setLog("商品参数==\n" + business);
        String businesss = "";
      /*  try {
            businesss = URLEncoder.encode(business, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Tools.setLog("商品参数转义==\n" + businesss);*/
        return business;
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
