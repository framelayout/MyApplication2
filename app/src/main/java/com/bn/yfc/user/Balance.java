package com.bn.yfc.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/12/19.
 */

public class Balance extends BaseActivity implements OnClickListener {
    private ImageView left;
    private TextView title, money;
    private LinearLayout tix, cho;
    private Intent intent;
    //刷新对象
    RefreshData redata = new RefreshData(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance);
        initHead();
        initView();
        initData();
    }

    private void setView(String mone) {
        money.setText(mone);
    }


    private void initData() {
        RequestParams params = new RequestParams(MyConfig.POSTGETMONEYURL);
        params.addBodyParameter("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.addBodyParameter("type", "1");
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                closeProgressDialog();
                if (result == null) {
                    Tools.setLog("余额请求数据为空");
                    return;
                }
                Tools.setLog("余额请求成功=======================" + "\n" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("获取成功");
                            JSONObject ob = object.getJSONObject("data");
                            String mon = ob.getString("money");
                            SharedPreferencesUtils.setParam(getApplication(), "money", mon);
                            setView(mon);
                            break;
                        case 108:
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("获取余额请求报错======" + ex.getMessage());
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initView() {
        money = (TextView) findViewById(R.id.balance_money);
        tix = (LinearLayout) findViewById(R.id.balance_tix);
        tix.setOnClickListener(this);
        cho = (LinearLayout) findViewById(R.id.balance_cho);
        cho.setOnClickListener(this);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("账户余额");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.balance_tix:
                intent = new Intent(this, Balance2.class);
                startActivity(intent);
                break;
            case R.id.balance_cho:
                intent = new Intent(this, Recharge.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Tools.setLog("回调");
        //刷新数据
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (redata != null) {
            redata = null;
        }
    }
}
