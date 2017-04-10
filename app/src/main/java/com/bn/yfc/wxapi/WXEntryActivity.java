package com.bn.yfc.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.myapplication.ConfigInfo;
import com.bn.yfc.tools.HttpRequest;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Recover;
import com.bn.yfc.user.Registered;
import com.bn.yfc.user.SettingsInterface;
import com.bn.yfc.user.UserSettings;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.RequestTracker;
import org.xutils.x;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/11/24.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Intent intent;
    private int reCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = MyAppLication.getIwxapi(this);
        api.handleIntent(getIntent(), this);
        //setContentView(R.layout.wx);
        showProgressDialog();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Tools.setLog("微信回调了 1");
    }


    public void onResp(BaseResp resp) {
        Tools.setLog("微信回调了 2");
        Tools.setLog("微信回调了方法" + resp.errCode + "type:" + resp.getType());

        switch (resp.getType()) {
            case 1:
                //快捷登录
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                        String code = sendResp.code;
                        Tools.setLog("code\t" + code);
                        getWXToken(code);
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        Tools.showToast(getApplication(), "取消成功");
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        break;
                    case BaseResp.ErrCode.ERR_COMM:
                        Log.e("yfc", "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等");
                        break;
                    default:

                        break;
                }
                break;
            case 2:
                closeProgressDialog();
                finish();
                break;
        }

        Tools.setLog("测试回调执行");
    }

    private void getWXToken(String code) {
        Tools.setLog("获取WXtoken开始");
        String xu = "wx070d525202bba218";
        String secert = "112abff069dd9f1fb999fdecf8f5dd45";
        Tools.setLog("测试wx快捷登录Token请求" + "获取code：" + code + "|\n" + "appid: " + xu + "|\n" + "secert: " + secert);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + xu + "&secret=" + secert + "&code=" + code + "&grant_type=authorization_code";
        Tools.setLog("获取WXtoken结束");
        FinalHttp fhp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("appid", xu);
        params.put("secret", secert);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Tools.setLog("开始请求");
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("请求错误" + strMsg);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("请求成功" + s);
                String token = "";
                String openid = "";
                try {
                    JSONObject object = new JSONObject(s);
                    token = object.getString("access_token");
                    openid = object.getString("openid");
                    getUserIonf(openid, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
    private void getUserIonf(String openid, String token) {
        FinalHttp fpt = new FinalHttp();

        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid;
        fpt.get(url, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("微信用户数据请求错误" + strMsg);

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("用户信息为++++++++++" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String uid = object.getString("unionid");
                    String icUrl = object.getString("headimgurl");
                    String nickname = object.getString("nickname");
                    Tools.setLog("获取用户信息为: " + uid + "|" + "获取微信头像为: " + icUrl);
                    LoginXW(icUrl, uid, nickname);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.setLog("微信登录解析错误" + e.getMessage());
                }

            }
        });
    }


    private void LoginXW(final String name, final String uid, final String icUrl) {
        AjaxParams params = new AjaxParams();
        params.put("wxid", uid);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTWXLOGINURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("微信Uid====" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("跳转登录");
                            //new RefreshData(WXEntryActivity.this).StartDataRefresh(WXEntryActivity.this, " ");
                            JSONObject ob = object.getJSONObject("data");
                            SharedPreferencesUtils.setParam(getApplication(), "tel", ob.getString("tel"));
                            //用户ID
                            SharedPreferencesUtils.setParam(getApplication(), "id", ob.getString("id"));
                            //用户账号
                            SharedPreferencesUtils.setParam(getApplication(), "name", ob.getString("name"));
                            //用户余额
                            SharedPreferencesUtils.setParam(getApplication(), "money", ob.getString("money"));
                        /*    //更新地理位置
                            SharedPreferencesUtils.setParam(context, "nowplace", ob.getString("nowplace"));*/
                            //电弧
                            //邀请人
                            SharedPreferencesUtils.setParam(getApplication(), "invite", ob.getString("invite"));
                            String urlhead = ob.getString("url");
                            String icurl = urlhead + ob.getString("pic");
                            //用户头像
                            SharedPreferencesUtils.setParam(getApplication(), "pic", icurl);
                            Tools.setLog("余额请求刷新头像地址：\n" + SharedPreferencesUtils.getParam(getApplication(), "pic", ""));
                            Tools.setLog("刷新数据获得的头像为" + urlhead + ob.getString("pic"));
                            //用户真实姓名
                            SharedPreferencesUtils.setParam(getApplication(), "nickname", ob.getString("nickname"));
                            //年龄
                            SharedPreferencesUtils.setParam(getApplication(), "age", ob.getString("age"));
                           /* //用户是否完善资料
                            SharedPreferencesUtils.setParam(context, "ifdata", "106");*/
                            //用户性别
                            if (ob.getString("sex").equals(0)) {
                                SharedPreferencesUtils.setParam(getApplication(), "sex", "2");
                            } else {
                                SharedPreferencesUtils.setParam(getApplication(), "sex", ob.getString("sex"));
                            }
                            //用户当前状态
                            SharedPreferencesUtils.setParam(getApplication(), "islogin", ob.getString("islogin"));
                            Tools.setLog("获取值为:" + SharedPreferencesUtils.getParam(getApplication(), "id", " ") + "/" + SharedPreferencesUtils.getParam(getApplication(), "name", " ") + "/" + SharedPreferencesUtils.getParam(getApplication(), "money", " "));
                            closeProgressDialog();
                            finish();
                            break;
                        case 101:
                            reCode = 1;
                            intent = new Intent(WXEntryActivity.this, Registered.class);
                            intent.putExtra("type", "wx");
                            intent.putExtra("usericon", icUrl);
                            intent.putExtra("nickname", name);
                            intent.putExtra("id", object.getString("data"));
                            intent.putExtra("wxid", uid);
                            startActivityForResult(intent, 0);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("传递失败");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigInfo.RESULT_OK_CODE) {
            finish();
        }
    }

    private void getWXToken1(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ConfigInfo.WXAPPID + "&secret=" + ConfigInfo.WXSECRET + "&code=" + code + "&grant_type=authorization_code";
        StringRequest request = new StringRequest(Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Tools.setLog("volley获取Token++++++++++++" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tools.setLog("获取Token错误++++++++++++" + error.getMessage());
            }
        });
        MyAppLication.getmQueue().add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (reCode == 0) {

        } else {
            finish();
        }
    }
}
