package com.bn.yfc.userdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.fragmentmain.RefreshData;
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
 * Created by Administrator on 2017/3/14.
 */

public class OnlinePay extends BaseActivity implements View.OnClickListener {
    private TextView title, userMoney, storeName, storeAddrass, storeUserName;
    private ImageView left;
    private ImageView wxpay, alipay;
    private boolean ifspay = false;
    private EditText name;
    private EditText money;
    private Button but;
    private String did = "";
    String order = "";
    RefreshData redata = new RefreshData(this);
    private int payCode = 0;
    private String sName, sAddress, sUserName;
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
        setContentView(R.layout.onlinepay);
        if (getIntent() != null) {
            did = getIntent().getStringExtra("did");
            sName = getIntent().getStringExtra("storename");
            sAddress = getIntent().getStringExtra("storeaddress");
            sUserName = getIntent().getStringExtra("storeuser");
            Tools.setLog("获取的店铺名称" + sAddress + "商家id" + did);
        }
        initHead();
        initView();
        initReceiver();
    }

    private void initView() {
        storeName = (TextView) findViewById(R.id.onlinepay_storename);
        storeAddrass = (TextView) findViewById(R.id.onlinepay_storeaddress);
        storeUserName = (TextView) findViewById(R.id.onlinepay_storeusername);
        storeUserName.setText(sUserName);
        storeAddrass.setText(sAddress);
        storeName.setText(sName);
        but = (Button) findViewById(R.id.onlinepay_but);
        but.setOnClickListener(this);
        name = (EditText) findViewById(R.id.onlinepay_name);
        userMoney = (TextView) findViewById(R.id.onlinepay_usermoney);
        userMoney.setText((String) SharedPreferencesUtils.getParam(getApplicationContext(), "money", ""));
        money = (EditText) findViewById(R.id.onlinepay_money);
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
        alipay = (ImageView) findViewById(R.id.onlinepay_alipayicon);
        alipay.setOnClickListener(this);
        wxpay = (ImageView) findViewById(R.id.onlinepay_wxicon);
        wxpay.setOnClickListener(this);
    }


    private void initHead() {
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("在线支付");
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
    }

    private void changePay() {
        if (ifspay) {
            wxpay.setBackgroundResource(R.drawable.false_icon);
            alipay.setBackgroundResource(R.drawable.true_icon);
        } else {
            alipay.setBackgroundResource(R.drawable.false_icon);
            wxpay.setBackgroundResource(R.drawable.true_icon);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onlinepay_wxicon:
                ifspay = false;
                changePay();
                break;
            case R.id.onlinepay_alipayicon:
                ifspay = true;
                changePay();
                break;
            case R.id.headac_left:
                //返回
                finish();
                break;
            case R.id.onlinepay_but:
                sendData();
                break;
        }
    }

    private void sendData() {
        String moneys = money.getText().toString();
        String names = name.getText().toString();
        if (Tools.isEmpty(moneys)) {
            Tools.showToast(getApplication(), "请输入支付金额");
            return;
        }
       /* if (Tools.isEmpty(names)) {
            Tools.showToast(getApplication(), "请输入商品名称");
            return;
        }*/
        sendPay(moneys);
    }

    private void sendWXpay(String mo) {
        order += Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(getApplication(), "id", "");
        Tools.setLog("order" + order);
        WXShare.sendWXPay(getApplication(), "创言快递充值订单", order, mo, "face_zhifu", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""), did);
    }

    //调起支付
    private void sendPay(String moneys) {
        payCode = 1;
        SharedPreferencesUtils.setParam(getApplication(), "ispay", "5");
        //支付接口
        if (ifspay) {
            //支付宝
            sendAlipay(moneys);
        } else {
            //微信
            sendWXpay(moneys);
        }
        redata.StartDataRefresh(this, (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        order = "";
        if (payCode == 0) {
            return;
        }
        Tools.showToast(getApplication(), "已付款");
        if (SharedPreferencesUtils.getParam(getApplication(), "ispay", "").equals("13")) {

            finish();
        }

    }

    //支付宝
    private void sendAlipay(String mo) {
        Tools.setLog("支付宝支付" + mo);
        AjaxParams params = new AjaxParams();
        params.put("body", "face_zhifu");
        params.put("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("did", did);
        String order = "";
        order += Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(getApplication(), "id", "");
        params.put("order", order);
        Tools.setLog("ordernum:" + order);
        params.put("money", mo);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTALIPAYURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("面对面支付宝支付请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            sendAalipay(object);
                            break;
                        case 111:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("面对面支付支付宝请求失败" + strMsg);
            }
        });
    }

    private void sendAalipay(JSONObject object) {
        JSONObject ob1 = null;
        try {
            ob1 = object.getJSONObject("data");
            String id = ob1.getString("id");
            String ordernums = ob1.getString("ordernum");
            Tools.setLog("解析的ordernum:" + ordernums);
            String money = ob1.getString("money");
            String appID = ob1.getString("appID");
            String privateKey = ob1.getString("privateKey");
            String aliback = ob1.getString("ali_back");
            AliPayTools.AlipaySend(OnlinePay.this, privateKey, appID, money, aliback, ordernums);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
