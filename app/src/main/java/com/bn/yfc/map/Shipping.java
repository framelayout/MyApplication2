package com.bn.yfc.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.basedialog.PayDialog;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.OperAddress;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Shipping extends BaseActivity implements OnClickListener {

    private ImageView left;
    private EditText getName, getPhone, getDesc, getFromAddress, getToAddress, getGoods;
    private TextView title, selectFromAddress, selectToAddress;
    private RelativeLayout fromSelect, toSelect;
    private String type = "from";
    private Intent intent;
    private Button but;
    private int ispayCode = 0;
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
        setContentView(R.layout.shipping);
        initHead();
        initView();
        initReceiver();
    }

    private void initView() {
        getGoods = (EditText) findViewById(R.id.shipping_getgoods);
        getName = (EditText) findViewById(R.id.shipping_getname);
        getPhone = (EditText) findViewById(R.id.shipping_getphone);
        getDesc = (EditText) findViewById(R.id.shipping_getdesc);
        getFromAddress = (EditText) findViewById(R.id.shipping_fromaddress);
        getToAddress = (EditText) findViewById(R.id.shipping_toaddress);
        selectFromAddress = (TextView) findViewById(R.id.shipping_selectfromaddress);
        selectToAddress = (TextView) findViewById(R.id.shipping_selecttoaddress);
        fromSelect = (RelativeLayout) findViewById(R.id.shipping_selectfrom);
        fromSelect.setOnClickListener(this);
        toSelect = (RelativeLayout) findViewById(R.id.shipping_selectto);
        toSelect.setOnClickListener(this);
        but = (Button) findViewById(R.id.shipping_but);
        but.setOnClickListener(this);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("船运订单");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.shipping_but:
                ifsData();
                break;
            case R.id.shipping_selectfrom:
                type = "from";
                selectAddress();
                break;
            case R.id.shipping_selectto:
                type = "to";
                selectAddress();
                break;
        }
    }

    private void selectAddress() {
        switch (type) {
            case "from":
                intent = new Intent(this, OperAddress.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 0);
                break;
            case "to":
                intent = new Intent(this, OperAddress.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 13:
                Tools.setLog("返回了地址" + data.getStringExtra("address"));
                switch (type) {
                    case "from":
                        String ad = data.getStringExtra("address");
                        selectFromAddress.setText(ad);
                        break;
                    case "to":
                        String ab = data.getStringExtra("address");
                        selectToAddress.setText(ab);
                        break;
                }
        }

    }

    private void ifsData() {
        String name = getName.getText().toString();
        String phone = getPhone.getText().toString();
        String desc = getDesc.getText().toString();
        String seletFrom = selectFromAddress.getText().toString();
        String selectTo = selectToAddress.getText().toString();
        String fromAddress = getFromAddress.getText().toString();
        String toAddress = getToAddress.getText().toString();
        String goods = getGoods.getText().toString();
        if (Tools.isEmpty(name)) {
            Tools.showToast(this, "请输入联系人姓名");
            return;

        }
        if (Tools.isEmpty(phone)) {
            Tools.showToast(this, "请输入联系人电话");
            return;

        }
        if (Tools.isEmpty(goods)) {
            Tools.showToast(this, "请输入货物名称");
            return;
        }
        if (Tools.isEmpty(desc)) {
            Tools.showToast(this, "请输入船运订单详细描述");
            return;

        }
        if (Tools.isEmpty(seletFrom)) {
            Tools.showToast(this, "请选择发货区域");
            return;

        }
        if (Tools.isEmpty(fromAddress)) {
            Tools.showToast(this, "请输入详细发货地址");
            return;

        }
        if (Tools.isEmpty(selectTo)) {
            Tools.showToast(this, "请选择收货区域");
            return;

        }
        if (Tools.isEmpty(toAddress)) {
            Tools.showToast(this, "请输入详细收货地址");
            return;
        }
        sendPostData(name, phone, desc, seletFrom, fromAddress, selectTo, toAddress, goods);
    }

    private void sendPostData(String name, String phone, String desc, String seletFrom, String fromAddress, String selectTo, String toAddress, String goods) {
        AjaxParams params = new AjaxParams();
        ispayCode = 1;
        Tools.setLog((String) SharedPreferencesUtils.getParam(getApplication(), "sheng", " ") + SharedPreferencesUtils.getParam(getApplication(), "city", " ") + SharedPreferencesUtils.getParam(getApplication(), "qu", " "));
        params.put("area", (String) SharedPreferencesUtils.getParam(getApplication(), "sheng", " ") + SharedPreferencesUtils.getParam(getApplication(), "city", " ") + SharedPreferencesUtils.getParam(getApplication(), "qu", " "));
        params.put("cartype", "9");
        Tools.setLog((String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("send_name", name);
        params.put("send_money", "0.01");
        params.put("send_tel", phone);
        params.put("send_pos", seletFrom);
        params.put("send_street", fromAddress);
        params.put("boss_pos", selectTo);
        params.put("boss_street", toAddress);
        params.put("name", goods);
        params.put("type", "4");
        params.put("ispic", "0");
        params.put("tips", desc);
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTORDERADDURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("船运订单请求成功===" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 10:
                            JSONObject ob = object.getJSONObject("data");
                            String moneyss = ob.getString("money");
                            String order = ob.getString("ordernum");
                            String app_id = ob.getString("appID");
                            String privateKey = ob.getString("privateKey");
                            String outRrl = ob.getString("ali_back");
                            SharedPreferencesUtils.getParam(getApplication(), "ispay", "4");
                            startPayDialog(order, moneyss, app_id, privateKey, outRrl);
                            //调起支付对话框
                            //   Tools.showToast(getApplication(), "订单发布成功!");
                            break;
                        case 110:
                            Tools.showToast(Shipping.this, "订单发布失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
                Tools.setLog("船运订单请求失败" + strMsg);
            }
        });


    }

    private void startPayDialog(String order, String money, String app_id, String privateKey, String outurl) {
        SharedPreferencesUtils.setParam(getApplication(), "ispay", "4");
        Tools.setLog("船运订单order:" + order + "|money:" + money + "|app_id:" + app_id + "|privateKey:" + privateKey);
        PayDialog dialog = new PayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "pay");
        bundle.putString("order", order);
        bundle.putString("money", money);
        bundle.putString("app_id", app_id);
        bundle.putString("privateKey", privateKey);
        bundle.putString("outurl", outurl);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "pay");
    }
    //


    @Override
    protected void onStart() {
        super.onStart();
        //刷新界面
        if (ispayCode == 0) {
            return;
        }
        Tools.setLog("onStart");
        if (((String) SharedPreferencesUtils.getParam(getApplication(), "ispay", "")).equals("13")) {
            finish();
        }
    }
}
