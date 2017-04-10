package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.AliPayTools;
import com.bn.yfc.wxapi.WXShare;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/2/14.
 */

public class TestPay extends BaseActivity {
    private EditText pay;
    private Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testpay);
        Tools.setWindowStatusBarColor(this);
        sendData();
        pay = (EditText) findViewById(R.id.testpay);
    }

    private void sendData() {
        AjaxParams params = new AjaxParams();
        FinalHttp fhp = new FinalHttp();
        String url = "http://120.77.87.193/index.php/Client/Alipay/alipay_app";
        String urls = "http://120.77.87.193/index.php/Client/Alipay/test";
        fhp.post(urls, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                String ss = null;
                Tools.setLog("请求成功\n" + s);
                try {
                    JSONObject ob = new JSONObject(s);
                    ss = ob.getString("requestUrl");
                    String app_id = ob.getString("app_id");
                    Tools.setLog("app_id=" + app_id);
                    String biz_content = ob.getString("biz_content");
                    Tools.setLog("biz_content=" + biz_content);
                    String charset = ob.getString("charset");
                    Tools.setLog("charset=" + charset);
                    String method = ob.getString("method");
                    Tools.setLog("method=" + method);
                    String notify_url = ob.getString("notify_url");
                    Tools.setLog("notify_url=" + notify_url);
                    String sign_type = ob.getString("sign_type");
                    Tools.setLog("sign_type=" + sign_type);
                    String timestamp = ob.getString("timestamp");
                    Tools.setLog("timestamp=" + /*timestamp*/Tools.getCurrentTime());
                    String sign = ob.getString("sign");
                    String version = ob.getString("version");
                    String daiSign = "";
                    daiSign += "app_id=" + app_id + "&";
                    daiSign += "biz_content=" + biz_content + "&";
                    daiSign += "charset=" + charset + "&";
                 /*   daiSign += "format=" + "json" + "&";*/
                    daiSign += "method=" + method + "&";
                    daiSign += "notify_url=" + notify_url + "&";
                    daiSign += "sign_type=" + sign_type + "&";
                    daiSign += "timestamp=" + timestamp + "&";
                    daiSign += "version=" + version;
                    Tools.setLog("待签名内容\n" + daiSign);
                    String sion = AliPayTools.sign(daiSign);
                    Tools.setLog("返回签名为\n" + sion);
                    daiSign += "&sign=" + sion;
                    Tools.setLog("加签之后的字符\n" + daiSign);
                    String urlPy = "";
                    urlPy = URLEncoder.encode(daiSign, "utf-8");
                    pay.setText(ss/*urlPy*/);
                    Tools.setLog("获取的签名参数\n" + sion);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("请求错误" + strMsg);
            }
        });
    }


    public void TestPay(View v) {
        String payinfo = pay.getText().toString();
        Tools.setLog("支付宝支付参数:\n" + payinfo);
        if (Tools.isEmpty(payinfo)) {
            Tools.showToast(this, "请输入参数");
        }
        AliPayTools.startPay(this, payinfo);
        //AliPayTools.AlipaySend(this, "", "", "", "", "");
    }
}
