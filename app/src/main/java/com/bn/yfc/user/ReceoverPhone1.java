package com.bn.yfc.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/21.
 */

public class ReceoverPhone1 extends BaseActivity implements OnClickListener {
    int timers = 60;
    private Timer timerA, timerB;
    Handler heandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (timerA != null) {
                        timerA.cancel();
                        timerA = null;
                    }
                    if (timerB != null) {
                        timerB.cancel();
                        timerB = null;
                    }
                    if (timers > 0) {
                        timers--;
                        code_but.setText(timers + "可获取");
                        timerB = new Timer();
                        timerB.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                heandler.sendEmptyMessage(0);
                            }
                        }, 1000);

                    } else {
                        heandler.sendEmptyMessage(1);
                    }
                    break;
                case 1:
                    if (timerA != null) {
                        timerA.cancel();
                        timerA = null;
                    }
                    if (timerB != null) {
                        timerB.cancel();
                        timerB = null;
                    }
                    code_but.setClickable(true);
                    code_but.setBackgroundResource(R.drawable.verification_normal);
                    code_but.setText("获取验证码");
                    timers = 60;
                    break;
            }
        }
    };
    private long timerNumberOutCode;
    private String timerNumber;
    private String phoneCode;
    private String oldPhone;
    private EditText phone, code;
    private ImageView left;
    private TextView title;
    private Button code_but, but;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receoverphone1);
        initData();
        initHead();
        initView();
    }

    private void initData() {
        getIntent();

    }

    private void initView() {
        phone = (EditText) findViewById(R.id.receoverphone1_phone);
        code = (EditText) findViewById(R.id.receoverphone1_code);
        code_but = (Button) findViewById(R.id.receoverphone1_code_but);
        code_but.setOnClickListener(this);
        but = (Button) findViewById(R.id.receoverphone1_but);
        but.setOnClickListener(this);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("设置新账号");
    }

    String phones = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receoverphone1_code_but:
                phones = phone.getText().toString();
                if (Tools.isEmpty(phones)) {
                    Tools.showToast(this, "请输入手机号码");
                } else {
                    getCode(phones);
                    code_but.setClickable(false);
                    code_but.setBackgroundResource(R.drawable.verification_onclick);
                    countdown();
                }

                break;
            case R.id.receoverphone1_but:

                if (Long.parseLong(Tools.getTimeStamp()) < timerNumberOutCode) {
                    String ifCode = code.getText().toString();
                    if (ifCode.equals(phoneCode)) {
                        String pho = phone.getText().toString();
                        if (Tools.isEmpty(pho)) {
                            Tools.showToast(this, "请输入新手机号码");
                            break;
                        }
                        if (pho.equals(oldPhone)) {
                            setNewPhone(pho);
                        } else {
                            Tools.showToast(this, "请为手机号重新获取验证码");
                        }
                    } else {
                        Tools.showToast(this, "验证码错误，请重新获取验证码");
                    }
                } else {
                    Tools.showToast(this, "验证码超时，请重新获取验证码");
                }

                break;
            case R.id.headac_left:
                this.finish();
                break;
        }
    }

    private void getCode(String phone) {
        RequestParams params = new RequestParams(MyConfig.POSTSHOTMESURL);
        params.addBodyParameter("user", phone);
        params.addBodyParameter("type", "2");
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                closeProgressDialog();
                if (result != null) {
                    Tools.setLog("请求成功" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        switch (object.getInt("code")) {
                            case 1:
                                JSONObject ob = object.getJSONObject("data");
                                Tools.showToast(ReceoverPhone1.this, "发送成功!");
                                phoneCode = object.getString("shotmes");
                                timerNumber = ob.getString("time");
                                oldPhone = ob.getString("user");
                                timerNumberOutCode = Long.parseLong(timerNumber) + 1800L;
                                Tools.setLog("获取到的验证码为：" + phoneCode + "\n" + "获取到的时间戳为：" + timerNumber + "\n" + "当前时间戳为" + Tools.getTimeStamp() + "过时时间戳" + timerNumberOutCode);
                                break;
                            case 103:
                                break;
                            case 110:
                                Tools.showToast(ReceoverPhone1.this, "验证码获取失败，请稍后再次获取");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("请求错误" + ex.getMessage());
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


    //设置定时器
    private void countdown() {
        timerA = new Timer();
        timerA.schedule(new TimerTask() {
            @Override
            public void run() {
                heandler.sendEmptyMessage(0);
            }
        }, 0);

    }

    private void clean() {

        if (timerA != null) {
            timerA.cancel();
            timerA = null;
        }
        if (timerB != null) {
            timerB.cancel();
            timerB = null;
        }
    }

    private void setNewPhone(final String phone) {
        AjaxParams params = new AjaxParams();
        params.put("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.put("type", "1");
        params.put("user", phone);
        showProgressDialog();
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTCHANGEUSRE, params, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                showProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(s);
                    Tools.setLog("重新设置登录账户成功=====" + s);
                    switch (object.getInt("code")) {
                        case 1:
                            Intent intent = new Intent();
                            intent.putExtra("succees", true);
                            intent.putExtra("phone", phones);
                            setResult(Activity.RESULT_OK, intent);
                            SharedPreferencesUtils.setParam(getApplication(), "name", phone);
                            Tools.showToast(getApplication(), "设置成功，请使用新账号登录");
                            finish();
                            break;
                        case 110:
                            Tools.showToast(getApplication(), "设置失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }


}
