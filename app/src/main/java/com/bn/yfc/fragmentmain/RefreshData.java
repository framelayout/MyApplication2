package com.bn.yfc.fragmentmain;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.myapp.MyConfig;
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

/**
 * Created by Administrator on 2016/12/30.
 */

public class RefreshData implements AMapLocationListener, LocationSource {
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Context context;

    public RefreshData(Context context) {
        this.context = context;
    }

    public void StartDataRefresh(Context context, String id) {
        Tools.setLog("动态请求");
        RefreshDataStart(context, id);
    }

    //刷新数据
    private void RefreshDataStart(final Context context, String id) {
        RequestParams params = new RequestParams(MyConfig.POSTGETMONEYURL);
        params.addBodyParameter("id", (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "id", " "));
        params.addBodyParameter("type", "1");
        Tools.setLog("获取金额" + (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "id", " "));
        x.http().post(params, new Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Tools.setLog("请求余额成功" + "\n" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("解析用户数据，保存入本地");
                            JSONObject ob = object.getJSONObject("data");
                            SharedPreferencesUtils.setParam(context, "tel", ob.getString("tel"));
                            //用户ID
                            SharedPreferencesUtils.setParam(context, "id", ob.getString("id"));
                            //用户账号
                            SharedPreferencesUtils.setParam(context, "name", ob.getString("name"));
                            //用户余额
                            SharedPreferencesUtils.setParam(context, "money", ob.getString("money"));
                        /*    //更新地理位置
                            SharedPreferencesUtils.setParam(context, "nowplace", ob.getString("nowplace"));*/
                            //电弧
                            //邀请人
                            SharedPreferencesUtils.setParam(context, "invite", ob.getString("invite"));
                            JSONObject obss = ob.getJSONObject("url");
                            String urlhead = obss.getString("url");
                            String icurl = urlhead + ob.getString("pic");
                            //用户头像
                            SharedPreferencesUtils.setParam(context, "pic", icurl);
                            Tools.setLog("余额请求刷新头像地址：\n" + SharedPreferencesUtils.getParam(context, "pic", ""));
                            Tools.setLog("刷新数据获得的头像为" + urlhead + ob.getString("pic"));
                            //用户真实姓名
                            SharedPreferencesUtils.setParam(context, "nickname", ob.getString("nickname"));

                            SharedPreferencesUtils.setParam(context, "age", ob.getString("age"));
                            SharedPreferencesUtils.setParam(context, "jifen", ob.getString("jifen"));
                            //当前积分
                            SharedPreferencesUtils.setParam(context, "current_jifen", ob.getString("current_jifen"));
                            SharedPreferencesUtils.setParam(context, "pk_quota", ob.getString("pk_quota"));
                            SharedPreferencesUtils.setParam(context, "kdy_quota", ob.getString("kdy_quota"));
                            //孝心
                            SharedPreferencesUtils.setParam(context, "heart", ob.getString("heart"));
                            //当前孝心
                            SharedPreferencesUtils.setParam(context, "current_heart", ob.getString("current_heart"));
                            //孝分
                            SharedPreferencesUtils.setParam(context, "branch", ob.getString("branch"));
                            //年龄
                           /* //用户是否完善资料
                            SharedPreferencesUtils.setParam(context, "ifdata", "106");*/
                            //用户性别
                            if (ob.getString("sex").equals(0)) {
                                SharedPreferencesUtils.setParam(context, "sex", "2");
                            } else {
                                SharedPreferencesUtils.setParam(context, "sex", ob.getString("sex"));
                            }
                            //用户当前状态
                            SharedPreferencesUtils.setParam(context, "islogin", ob.getString("islogin"));

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

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private void LocationStart() {
        Tools.setLog("定位开始");
        startLocation();
    }

    private void startLocation() {
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //高精度
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //单次定位
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mlocationClient.setLocationListener(this);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        Tools.setLog("定位成功");
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            Tools.showToast(context, "当前位置在" + aMapLocation.getCity());
            SharedPreferencesUtils.setParam(context, "city", aMapLocation.getCity());
            SharedPreferencesUtils.setParam(context, "district", aMapLocation.getDistrict());
            SharedPreferencesUtils.setParam(context, "jd", aMapLocation.getLongitude());
            SharedPreferencesUtils.setParam(context, "wd", aMapLocation.getLatitude());
            Tools.setLog(aMapLocation.getCity() + ", " +
                    aMapLocation.getAddress() + ", " +
                    aMapLocation.getDistrict() + "," +
                    aMapLocation.getStreet() + "," +
                    aMapLocation.getLatitude() + ", " + aMapLocation.getLongitude() + "\n" + SharedPreferencesUtils.getParam(context, "district", " "));
        } else {
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //mLocationOption.setInterval(5000);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        Tools.setLog("定位结束");
    }


    public void RefreshWaith(final Context cont) {
        Tools.setLog("开始请求客服电话");
        RequestParams params = new RequestParams(MyConfig.POSTGETPHONENUMURL);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Tools.setLog("获取客服电话=======================" + result + "\n");
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject ob = object.getJSONObject("data");
                            SharedPreferencesUtils.setParam(cont, "waith", ob.getString("phone"));
                            break;
                        case 110:
                            Tools.setLog("出错了");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.setLog("客服电话请求异常" + e.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("请求客服电话报错为====" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
        Tools.setLog("客服电话请求结束");
    }


    public void WXToken(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Tools.setLog("发送请求");
        MyAppLication.getmQueue().add(request);
    }


    public void FreightCompute(Context context, String cartype) {


    }

    //路费规则请求
    public void RefreshTolls(final Context context, String type) {
        Tools.setLog("运费规则type" + type);
        AjaxParams params = new AjaxParams();
        params.put("cartype", type);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTSENDURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("请求路费计算规则成功==" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject ob = object.getJSONObject("data");
                            String frist = ob.getString("first");
                            String frist_out = ob.getString("first_int");
                            String every = ob.getString("every");
                            SharedPreferencesUtils.setParam(context, "frist", frist);
                            SharedPreferencesUtils.setParam(context, "frist_out", frist_out);
                            SharedPreferencesUtils.setParam(context, "every", every);
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
                Tools.setLog("请求路费计算规则失败==" + strMsg);
            }
        });
    }

    public void RefreshProtect(final Context context) {
        AjaxParams params = new AjaxParams();
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTPROTECTURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("获取保险费用成功:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            SharedPreferencesUtils.setParam(context, "protect", object.getString("data"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("保险费用请求失败" + strMsg);
            }
        });

    }
}
