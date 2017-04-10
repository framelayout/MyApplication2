package com.bn.yfc.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.AliPayTools;
import com.bn.yfc.wxapi.WXShare;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Recharge extends BaseActivity implements OnClickListener {
    private ImageView left, wxpay_icon, alipay_icon;
    private TextView title, user;
    private EditText money;
    private Button payBut;
    private boolean isPay = false;
    private int PayCode = 0;

    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("closealipay")) {
                Tools.setLog("注销广播接收成功");
                finish();
            }
        }
    };

    private void initReceiver() {
        IntentFilter filter = new IntentFilter("closealipay");
        registerReceiver(recevier, filter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);
        initHead();
        initView();
        setPayView();
        initReceiver();
    }


    private void initView() {
        wxpay_icon = (ImageView) findViewById(R.id.wxpay_icon);
        wxpay_icon.setOnClickListener(this);
        alipay_icon = (ImageView) findViewById(R.id.alipay_icon);
        alipay_icon.setOnClickListener(this);
        payBut = (Button) findViewById(R.id.recharge_but);
        payBut.setOnClickListener(this);
        money = (EditText) findViewById(R.id.recharge_money);
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        money.setText(s);
                        money.setSelection(s.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        user = (TextView) findViewById(R.id.recharge_phone);
        user.setText("当前账户:  " + SharedPreferencesUtils.getParam(getApplication(), "name", " "));
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("充值");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.recharge_but:
                PayCode = 1;
                String mo = money.getText().toString();
                if (Tools.isEmpty(mo) && mo.equals("")) {
                    Tools.showToast(this, "请输入充值金额");
                } else {
                    SharedPreferencesUtils.setParam(getApplication(), "ispay", "0");
                    Tools.setLog("支付方式|" + SharedPreferencesUtils.getParam(getApplication(), "ispay", ""));
                    if (isPay) {
                        //Tools.setLog("支付宝支付充值");
                        // AliPayTools.AlipaySend(Recharge.this, "", "", "", "", "");
                        sendczData(mo);
                    } else {
                        String order = "";
                        order += Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(getApplication(), "id", "");
                        Tools.setLog("order" + order);
                        WXShare.sendWXSign(getApplication(), true, "创言网充值订单", order, mo, "chongzhi");
                        Tools.setLog("微信支付充值");
                    }

                }
                break;
            case R.id.wxpay_icon:
                SharedPreferencesUtils.setParam(this, "ispay", "0");
                isPay = false;
                setPayView();
                break;
            case R.id.alipay_icon:
                SharedPreferencesUtils.setParam(this, "ispay", "0");
                isPay = true;
                setPayView();
                break;
        }
    }

    private void setPayView() {
        if (isPay) {
            alipay_icon.setBackgroundResource(R.drawable.true_icon);
            wxpay_icon.setBackgroundResource(R.drawable.false_icon);
        } else {
            alipay_icon.setBackgroundResource(R.drawable.false_icon);
            wxpay_icon.setBackgroundResource(R.drawable.true_icon);
        }
    }

    public void ifPay() {
        //刷新
        Tools.setLog("回调刷新");
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PayCode == 0) {
            return;
        }
        if (SharedPreferencesUtils.getParam(getApplication(), "ispay", "").equals("13")) {
            Tools.showToast(getApplication(), "支付成功");
            finish();
        } else {
            Tools.showToast(getApplication(), "支付失败");
        }
    }

    private void sendWXpay(String mo) {
        String order = "";
        order += Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(getApplication(), "id", "");
        WXShare.sendWXSign(getApplication(), true, "创言快递支付订单", order, mo, "chongzhi");
    }

    private void sendAlipay(String key, String appid, String money, String notify_url, String ordermun) {
        Tools.setLog("调起支付宝支付");
        AliPayTools.AlipaySend(Recharge.this, key, appid, money, notify_url, ordermun);
    }

    //时间戳+随机四个字符串+uid
    private void sendczData(final String money) {
        AjaxParams params = new AjaxParams();
        params.put("money", money);
        params.put("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("type", "2");
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTCHONGZHIRUL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("充值请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject ob1 = object.getJSONObject("data");
                            String mo = ob1.getString("money");
                            String ordermun = Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(getApplication(), "id", "");
                            String appID = ob1.getString("appID");
                            String privateKey = ob1.getString("privateKey");
                            String aliyapUrl = ob1.getString("ali_back");
                            sendAlipay(privateKey, appID, mo, aliyapUrl, ordermun);
                            break;
                        case 0:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("充值申请失败" + strMsg);
            }
        });
    }
}
