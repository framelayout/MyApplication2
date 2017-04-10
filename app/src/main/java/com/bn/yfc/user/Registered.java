package com.bn.yfc.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.bn.yfc.myapplication.ConfigInfo;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.core.AsyncTask;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/16.
 */

public class Registered extends BaseActivity implements OnClickListener {
    int timers = 60;
    private Bitmap iconBit = null;
    private ImageView textic;
    private File file;
    private Timer timerA, timerB;
    public Handler heandler = new Handler() {
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
                case 13:
                    if (iconBit == null) {

                        break;
                    }
                    testic();
                    file = Tools.saveBitmapFile(iconBit);
                    if (file.exists()) {
                        Tools.setLog("有图像" + "|" + file.getPath());
                    } else {
                        Tools.setLog("没有图像");
                    }

                    break;
            }
        }
    };

    private Button codebut, but;
    private EditText user, pass, code;
    private ImageView left, userClean, passClean;
    private TextView title;
    private long timerNumberOutCode;
    private String timerNumber;
    private String phoneCode;
    private String jumpType;
    private String nickname, oldPhone;
    private String id;
    private String uid;
    private String userIcon;
    private Uri outFileIcon;

    private static int RECODE = 13;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        initData();
        initHead();
        initView();
        setView();
    }

    private void RegisteredWX() {
        AjaxParams params = new AjaxParams();
        params.put("id", id);
        params.put("nickname", nickname);
        if (file != null) {
            try {
                params.put("pic", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Tools.setLog("文件发生异常" + e.getMessage());
            }
        }
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTWXDATAURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("微信上传成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(Registered.this, "提交成功");
                            finish();
                            break;
                        case 10:
                            Tools.showToast(Registered.this, "账号绑定成功");
                            finish();
                            break;
                        case 11:
                            Tools.showToast(Registered.this, "注册成功");
                            finish();
                            break;
                        case 111:
                            // Tools.showToast(Registered.this, "手机号注册失败");
                            finish();
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("微信资料上传失败" + strMsg);
            }
        });
    }

    private void testic() {

        textic.setImageBitmap(iconBit);
    }

    private void setView() {
        if (jumpType != null && jumpType.equals("wx")) {
            title.setText("完善个人信息");
            but.setText("提交");
        }

    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }
        Tools.setLog("设置对应参数");
        jumpType = getIntent().getStringExtra("type");
        if (jumpType == null) {
            Tools.setLog("为空");
            return;
        }
        userIcon = getIntent().getStringExtra("usericon");

        Tools.setLog("获取微信头像地址为" + userIcon);
        nickname = getIntent().getStringExtra("nickname");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("wxid");
        new Tank().execute(userIcon);

    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("注册");

    }

    private void initView() {
        textic = (ImageView) findViewById(R.id.textic);
        code = (EditText) findViewById(R.id.registered_code);
        codebut = (Button) findViewById(R.id.registered_code_but);
        codebut.setOnClickListener(this);
        user = (EditText) findViewById(R.id.registered_user);
        userClean = (ImageView) findViewById(R.id.registered_user_clean);
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
        pass = (EditText) findViewById(R.id.registered_pass);
        passClean = (ImageView) findViewById(R.id.registered_pass_clean);
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
        but = (Button) findViewById(R.id.registered_but);
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

    class Tank extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            heandler.sendEmptyMessage(13);
        }

        @Override
        protected Void doInBackground(String... strings) {
            iconBit = Tools.GetImageInputStream((String) strings[0]);
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headac_left:
                if (jumpType != null && jumpType.equals("wx")) {
                    setResult(ConfigInfo.RESULT_OK_CODE);
                    finish();
                    break;
                }
                this.finish();
                break;
            case R.id.registered_code_but:
                String pho = user.getText().toString();
                if (Tools.isEmpty(pho)
                        ) {
                    Tools.showToast(this, "请输入电话号码");
                } else {
                    codebut.setClickable(false);
                    codebut.setBackgroundResource(R.drawable.verification_onclick);
                    countdown();
                    PostCode(pho);
                }

                break;
            case R.id.registered_pass_clean:
                pass.setText("");
                pass.setHint("请输入密码");
                passClean.setVisibility(View.INVISIBLE);
                break;
            case R.id.registered_user_clean:
                user.setText("");
                user.setHint("请输入账户");
                userClean.setVisibility(View.INVISIBLE);
                break;
            case R.id.registered_but:
                String pas = pass.getText().toString();
                String use = user.getText().toString();
                if (Tools.isEmpty(use)) {
                    Tools.showToast(this, "请输入账号");
                } else {
                    if (Tools.isEmpty(pas)) {
                        Tools.showToast(this, "请输入密码");
                    } else {
                        if (code.getText().toString().equals(phoneCode)) {
                            if (timerNumberOutCode > Long.parseLong(Tools.getTimeStamp())) {
                                if (use.equals(oldPhone)) {
                                    if (jumpType != null && jumpType.equals("wx")) {
                                        PostWXRegistered(use, Tools.md5(pas));
                                    } else {
                                        PostRegistered(use, Tools.md5(pas), phoneCode);
                                    }
                                } else {
                                    Tools.showToast(this, "请获取手机号验证码");
                                }

                            } else {
                                Tools.showToast(this, "验证码超时");
                            }
                        } else {
                            Tools.showToast(this, "验证码错误");
                        }
                    }
                }
                break;
        }
    }

    private void PostCode(String phone2) {
        Tools.setLog("验证码请求：" + "开始" + "=-===============================");
        RequestParams params = new RequestParams(MyConfig.POSTSHOTMESURL);
        params.addBodyParameter("user", phone2);
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
                                Tools.showToast(Registered.this, "发送成功!");
                                phoneCode = object.getString("shotmes");
                                timerNumber = ob.getString("time");
                                oldPhone = ob.getString("user");
                                timerNumberOutCode = Long.parseLong(timerNumber) + 1800L;
                                Tools.setLog("获取到的验证码为：" + phoneCode + "\n" + "获取到的时间戳为：" + timerNumber + "\n" + "当前时间戳为" + Tools.getTimeStamp() + "过时时间戳" + timerNumberOutCode);
                                break;
                            case 103:
                                break;
                            case 110:
                                Tools.showToast(Registered.this, "验证码获取失败，请稍后再次获取");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("验证码请求错误：" + ex.getMessage() + "=-===============================");
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
        Tools.setLog("验证码请求：" + "结束" + "=-===============================");
    }

    private void PostWXRegistered(String phone, String pass) {
        AjaxParams params = new AjaxParams();
        params.put("user", phone);
        params.put("password", pass);
        params.put("wxid", uid);
        params.put("id", id);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTWXADDURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("微信注册请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            RegisteredWX();
                            break;
                        case 11:
                            RegisteredWX();
                            break;
                        case 110:
                            Tools.setLog("");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Tools.showToast(getApplication(), "测试微信图片缓存");

            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("微信注册请求失败" + strMsg);
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }

    private void PostRegistered(String use, String pas, String code) {
        RequestParams params = new RequestParams(MyConfig.POSTREGISTERURL);
        params.addBodyParameter("user", use);
        params.addBodyParameter("password", pas);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("city", (String) SharedPreferencesUtils.getParam(this, "sheng", "") + SharedPreferencesUtils.getParam(this, "city", "") + SharedPreferencesUtils.getParam(this, "qu", ""));
        params.addBodyParameter("code", phoneCode);
        params.addBodyParameter("phonetype", "A");
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
                    Tools.setLog("注册请求返回值" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        switch (object.getInt("code")) {
                            case 1:
                                Tools.showToast(Registered.this, "注册成功,请直接登录账号");
                                finish();
                                break;
                            case 110:
                                Tools.showToast(Registered.this, "注册失败");
                                break;
                            case 103:
                                Tools.showToast(Registered.this, "用户已存在，请直接登录");
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("注册请求错误" + ex.getMessage());
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
}
