package com.bn.yfc.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.bn.yfc.wxapi.AliPayTools;
import com.bn.yfc.wxapi.WXShare;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/12/16.
 */

public class Login extends BaseActivity implements OnClickListener {
    String logintype = null;
    private Button login;
    private EditText user, pass;
    private ImageView userclean, passclean, returnicon;
    private TextView registered, recover;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initData();
        initView();

    }

    private void initData() {

        if (getIntent() != null) {
            logintype = getIntent().getStringExtra("logintype");
            Log.d("yfc", "Login getIntent ====" + logintype);
        }

    }

    private void initView() {
        returnicon = (ImageView) findViewById(R.id.login_return);
        returnicon.setOnClickListener(this);
        login = (Button) findViewById(R.id.login_but);
        login.setOnClickListener(this);
        userclean = (ImageView) findViewById(R.id.login_user_clean);
        userclean.setOnClickListener(this);
        passclean = (ImageView) findViewById(R.id.login_pass_clean);
        passclean.setOnClickListener(this);
        registered = (TextView) findViewById(R.id.login_registered);
        registered.setOnClickListener(this);
        recover = (TextView) findViewById(R.id.login_recover);
        recover.setOnClickListener(this);
        user = (EditText) findViewById(R.id.login_user);
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i > 0) {
                    userclean.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pass = (EditText) findViewById(R.id.login_pass);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i > 0) {
                    passclean.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_but:
                String use = user.getText().toString();
                String pas = pass.getText().toString();
                if (Tools.isEmpty(use)) {
                    Tools.showToast(this, "请输入账号");
                    break;
                }
                if (Tools.isEmpty(pas)) {
                    Tools.showToast(this, "请输入密码");
                    break;
                }
                LoginPost(use, Tools.md5(pas));
                //   test(use);
                break;
            case R.id.login_user_clean:
                user.setText("");
                user.setHint("请输入账号");
                userclean.setVisibility(View.INVISIBLE);
                break;
            case R.id.login_pass_clean:
                pass.setText("");
                pass.setHint("请输入密码");
                passclean.setVisibility(View.INVISIBLE);
                break;
            case R.id.login_registered:
                intent = new Intent(this, Registered.class);
                startActivity(intent);
                break;
            case R.id.login_recover:
                intent = new Intent(this, Recover.class);
                startActivity(intent);
                break;
            case R.id.login_return:
                this.finish();
                break;
        }
    }


    private void LoginPost(String use, String pas) {
        RequestParams params = new RequestParams(MyConfig.POSTLOGINURL);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("user", use);
        params.addBodyParameter("nowplace", (String) SharedPreferencesUtils.getParam(getApplication(), "district", " "));
        params.addBodyParameter("jd", (String) SharedPreferencesUtils.getParam(getApplication(), "jd", "106.551018"));
        params.addBodyParameter("wd", (String) SharedPreferencesUtils.getParam(getApplication(), "wd", "29.439868"));
        params.addBodyParameter("password", pas);
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Tools.ReigeJPush(Login.this);
                closeProgressDialog();
                if (result != null) {
                    try {
                        Tools.setLog("登录请求数据为：\n" + result);
                        JSONObject object = new JSONObject(result);
                        switch (object.getInt("code")) {
                            case 1:
                                Tools.showToast(Login.this, "登录成功");
                                Tools.setLog("用户信息为" + object.getString("data"));
                                JSONObject ob2 = (JSONObject) object.get("data");
                                if (ob2 != null) {
                                    Tools.setLog("解析数据");
                                    //积分
                                    SharedPreferencesUtils.setParam(getApplication(), "jifen", ob2.getString("jifen"));
                                    //当前积分
                                    SharedPreferencesUtils.setParam(getApplication(), "current_jifen", ob2.getString("current_jifen"));
                                    //孝心
                                    SharedPreferencesUtils.setParam(getApplication(), "heart", ob2.getString("heart"));
                                    //当前孝心
                                    SharedPreferencesUtils.setParam(getApplication(), "current_heart", ob2.getString("current_heart"));
                                    //孝分
                                    SharedPreferencesUtils.setParam(getApplication(), "branch", ob2.getString("branch"));
                                    //用户ID
                                    SharedPreferencesUtils.setParam(getApplication(), "id", ob2.getString("id"));
                                    //用户账号
                                    SharedPreferencesUtils.setParam(getApplication(), "name", ob2.getString("name"));
                                    //用户真实姓名
                                    SharedPreferencesUtils.setParam(getApplication(), "nickname", ob2.getString("nickname"));
                                    //年龄
                                    SharedPreferencesUtils.setParam(getApplication(), "age", ob2.getString("age"));

                                    //用户性别
                                    if (ob2.getString("sex").equals(0)) {
                                        SharedPreferencesUtils.setParam(getApplication(), "sex", "2");
                                    } else {
                                        SharedPreferencesUtils.setParam(getApplication(), "sex", ob2.getString("sex"));
                                    }
                                    //用户余额
                                    SharedPreferencesUtils.setParam(getApplication(), "money", ob2.getString("money"));
                                    //账户登录状态
                                    SharedPreferencesUtils.setParam(getApplication(), "islogin", "1");
                                    //邀请人
                                    SharedPreferencesUtils.setParam(getApplication(), "invite", ob2.getString("invite"));
                                    /*Tools.setLog("login" + 11);
                                    //更新地理位置
                                    SharedPreferencesUtils.setParam(getApplication(), "nowplace", ob2s.getString("nowplace"));*/
                                    //电弧
                                    SharedPreferencesUtils.setParam(getApplication(), "tel", ob2.getString("tel"));
                                    SharedPreferencesUtils.setParam(getApplication(), "wxid", ob2.getString("wxid"));
                                    JSONObject obs = ob2.getJSONObject("url");
                                    String urlhead = obs.getString("url");
                                    String icurl = urlhead + ob2.getString("pic");
                                    //用户头像
                                    SharedPreferencesUtils.setParam(getApplication(), "pic", icurl);
                                    //用户是否完善资料
                                    SharedPreferencesUtils.setParam(getApplication(), "ifdata", "1");
                                    Tools.setLog("获取值为:" + SharedPreferencesUtils.getParam(Login.this, "id", " ") + "/" + SharedPreferencesUtils.getParam(Login.this, "name", " ") + "/" + SharedPreferencesUtils.getParam(Login.this, "money", " "));
                                }
                                switch (logintype) {
                                    case "login1":
                                        intent = new Intent();
                                        intent.setAction("login1");
                                        sendBroadcast(intent);
                                        Tools.setLog("发送login1广播");
                                        break;
                                    case "login":
                                        intent = new Intent();
                                        intent.setAction("login");
                                        sendBroadcast(intent);
                                        Tools.setLog("发送login广播");
                                        break;
                                }
                                finish();
                                break;
                            case 106:
                                Tools.setLog("Login 完善信息");
                                Tools.showToast(Login.this, "登录成功");
                                JSONObject ob2s = (JSONObject) object.get("data");
                                if (ob2s != null) {
                                    int i = 1;
                                    Tools.setLog("解析数据");
                                    //积分
                                    SharedPreferencesUtils.setParam(getApplication(), "jifen", ob2s.getString("jifen"));
                                    //当前积分
                                    SharedPreferencesUtils.setParam(getApplication(), "current_jifen", ob2s.getString("current_jifen"));
                                    //孝心
                                    SharedPreferencesUtils.setParam(getApplication(), "heart", ob2s.getString("heart"));
                                    //当前孝心
                                    SharedPreferencesUtils.setParam(getApplication(), "current_heart", ob2s.getString("current_heart"));
                                    //孝分
                                    SharedPreferencesUtils.setParam(getApplication(), "branch", ob2s.getString("branch"));
                                    //用户ID
                                    SharedPreferencesUtils.setParam(getApplication(), "id", ob2s.getString("id"));
                                    //用户账号
                                    SharedPreferencesUtils.setParam(getApplication(), "name", ob2s.getString("name"));
                                    //用户真实姓名
                                    SharedPreferencesUtils.setParam(getApplication(), "nickname", ob2s.getString("nickname"));
                                    //年龄
                                    SharedPreferencesUtils.setParam(getApplication(), "age", ob2s.getString("age"));
                                    //用户性别
                                    if (ob2s.getString("sex").equals(0)) {
                                        SharedPreferencesUtils.setParam(getApplication(), "sex", "2");
                                    } else {
                                        SharedPreferencesUtils.setParam(getApplication(), "sex", ob2s.getString("sex"));
                                    }
                                    //用户余额
                                    SharedPreferencesUtils.setParam(getApplication(), "money", ob2s.getString("money"));
                                    //账户登录状态
                                    SharedPreferencesUtils.setParam(getApplication(), "islogin", "1");
                                    //邀请码
                                    SharedPreferencesUtils.setParam(getApplication(), "invite", ob2s.getString("invite"));

                                    //电话
                                    SharedPreferencesUtils.setParam(getApplication(), "tel", ob2s.getString("tel"));
                                    SharedPreferencesUtils.setParam(getApplication(), "wxid", ob2s.getString("wxid"));
                                    JSONObject obs = ob2s.getJSONObject("url");
                                    String urlhead = obs.getString("url");
                                    String icurl = urlhead + ob2s.getString("pic");
                                    //用户头像
                                    SharedPreferencesUtils.setParam(getApplication(), "pic", icurl);
                                    //用户是否完善资料
                                    SharedPreferencesUtils.setParam(getApplication(), "ifdata", "106");
                                    Tools.setLog("获取值为:" + SharedPreferencesUtils.getParam(Login.this, "id", " ") + "/" + SharedPreferencesUtils.getParam(Login.this, "name", " ") + "/" + SharedPreferencesUtils.getParam(Login.this, "money", " "));
                                }
                                switch (logintype) {
                                    case "login1":
                                        intent = new Intent();
                                        intent.setAction("login1");
                                        sendBroadcast(intent);
                                        Tools.setLog("发送login1广播");
                                        break;
                                    case "login":
                                        intent = new Intent();
                                        intent.setAction("login");
                                        sendBroadcast(intent);
                                        Tools.setLog("发送login广播");
                                        break;
                                }
                                intent = new Intent(Login.this, UserSettings.class);
                                intent.putExtra("data", "106");
                                startActivity(intent);
                                finish();
                                break;
                            case 107:
                                Tools.showToast(Login.this, "请注册账号");
                                break;
                            case 105:
                                Tools.showToast(Login.this, "账号或密码错误");
                                break;
                            case 102:
                                Tools.showToast(Login.this, "账号已登录");
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("登录请求报错：" + ex.getMessage());
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


    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPreferencesUtils.getParam(this, "islogin", "").equals("1")) {
            this.finish();
        } else {
            //啥也不做
        }
    }

    public void LoginWx(View v) {
        WXShare.WXLogin(this);
    }
}
