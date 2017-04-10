package com.bn.yfc.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/16.
 */

public class Recover extends BaseActivity implements OnClickListener {

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
                        codebut.setText(timers + "可获取");
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
                    codebut.setClickable(true);
                    codebut.setBackgroundResource(R.drawable.verification_normal);
                    codebut.setText("获取验证码");
                    timers = 60;
                    break;
            }
        }
    };

    private Button codebut, but;
    private EditText user, pass, code;
    ImageView left, userClean, passClean;
    TextView title;
    private long timerNumberOutCode;
    private String timerNumber;
    private String phoneCode;
    private String oldPhone;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover);
        initHead();
        initView();
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("修改密码");

    }

    private void initView() {
        code = (EditText) findViewById(R.id.recover_code);
        codebut = (Button) findViewById(R.id.recover_code_but);
        codebut.setOnClickListener(this);
        user = (EditText) findViewById(R.id.recover_user);
        userClean = (ImageView) findViewById(R.id.recover_user_clean);
        userClean.setOnClickListener(this);
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i > 0) {
                    userClean.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass = (EditText) findViewById(R.id.recover_pass);
        passClean = (ImageView) findViewById(R.id.recover_pass_clean);
        passClean.setOnClickListener(this);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i > 0) {
                    passClean.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        but = (Button) findViewById(R.id.recover_but);
        but.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registered_pass_clean:
                pass.setText("");
                pass.setHint("请输入密码");
                passClean.setVisibility(View.INVISIBLE);
                break;
            case R.id.registered_user_clean:
                user.setText("");
                user.setHint("请输入账户");
                userClean.setVisibility(View.INVISIBLE);
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.recover_code_but:
                String phones = user.getText().toString();
                if (Tools.isEmpty(phones)) {
                    Tools.showToast(this, "请输入手机号码");
                } else {
                    getCode(phones);
                    codebut.setClickable(false);
                    codebut.setBackgroundResource(R.drawable.verification_onclick);
                    countdown();
                }

                break;
            case R.id.recover_but:
                String phons = user.getText().toString();
                String passs = pass.getText().toString();
                String codes = code.getText().toString();
                if (Tools.isEmpty(phons)) {
                    Tools.showToast(this, "请输入电话号码");
                    break;
                }
                if (Tools.isEmpty(passs)) {
                    Tools.showToast(this, "请输入新密码");
                    break;
                }
                if (!oldPhone.equals(phons)) {
                    Tools.showToast(this, "请获取手机号验证码");
                    break;
                }
                if (phoneCode.equals(codes)) {
                    RecoverPost(phons, Tools.md5(passs));
                } else {
                    Tools.showToast(this, "验证码不正确");
                }

                break;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    private void RecoverPost(String phone, String pass) {
        RequestParams params = new RequestParams(MyConfig.POSTFINDBANKURL);
        params.addBodyParameter("user", phone);
        params.addBodyParameter("password", pass);
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
                if (result != null) {
                    Tools.setLog("请求成功" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        switch (object.getInt("code")) {
                            case 1:
                                Tools.showToast(Recover.this, "修改成功,请使用新账号登录");
                                finish();
                                break;
                            case 103:
                                break;
                            case 110:
                                Tools.showToast(Recover.this, "修改失败,账号不存在");
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

    private void getCode(String phone) {
        RequestParams params = new RequestParams(MyConfig.POSTSHOTMESURL);
        params.addBodyParameter("user", phone);
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
                if (result != null) {
                    Tools.setLog("请求成功" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        switch (object.getInt("code")) {
                            case 1:
                                JSONObject ob = object.getJSONObject("data");

                                Tools.showToast(Recover.this, "发送成功!");
                                phoneCode = object.getString("shotmes");
                                timerNumber = ob.getString("time");
                                oldPhone = ob.getString("user");
                                timerNumberOutCode = Long.parseLong(timerNumber) + 1800L;
                                Tools.setLog("获取到的验证码为：" + phoneCode + "\n" + "获取到的时间戳为：" + timerNumber + "\n" + "当前时间戳为" + Tools.getTimeStamp() + "过时时间戳" + timerNumberOutCode);
                                break;
                            case 103:
                                break;
                            case 110:
                                Tools.showToast(Recover.this, "验证码获取失败，请稍后再次获取");
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
}
