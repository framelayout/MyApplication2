package com.bn.yfc.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyAppLication;
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
 * Created by yfc on 2016/12/19.
 */

public class SettingsInterface extends BaseActivity implements OnClickListener {

    private ImageView left;
    private TextView title;
    private RelativeLayout phone, pass, outlog, cleanram;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsinterface);
        inithead();
        initView();
    }

    private void inithead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("系统设置");
    }


    private void initView() {
        phone = (RelativeLayout) findViewById(R.id.settings_phone);
        phone.setOnClickListener(this);
        pass = (RelativeLayout) findViewById(R.id.settings_pass);
        pass.setOnClickListener(this);
        outlog = (RelativeLayout) findViewById(R.id.settings_outlog);
        outlog.setOnClickListener(this);
        cleanram = (RelativeLayout) findViewById(R.id.settings_clean);
        cleanram.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.settings_phone:
                intent = new Intent(this, ReceoverPhone.class);
                startActivity(intent);
                break;
            case R.id.settings_pass:
                intent = new Intent(this, Recover.class);
                startActivity(intent);
                break;
            case R.id.settings_outlog:
                LogOut();
                break;
            case R.id.settings_clean:
                break;
        }
    }

    private void LogOut() {
        RequestParams params = new RequestParams(MyConfig.POSTLOGOUTURL);
        params.addBodyParameter("type", "1");

        params.addBodyParameter("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", "String"));
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                closeProgressDialog();
                Tools.setLog("退出请求成功：" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(SettingsInterface.this, "退出成功");
                            SharedPreferencesUtils.setParam(getApplication(), "islogin", "0");
                            finish();
                            break;
                        case 106:
                            Tools.showToast(SettingsInterface.this, "退出失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Tools.setLog("退出错误码：" + ex.getMessage());
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
}
