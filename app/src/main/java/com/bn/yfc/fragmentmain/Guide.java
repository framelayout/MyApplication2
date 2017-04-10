package com.bn.yfc.fragmentmain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.R;
import com.bn.yfc.base.Test;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Guide extends BaseActivity implements LocationSource, AMapLocationListener {
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Timer timer;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        initData();
        startLocation();
    }

    private void startLocation() {

        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //高精度
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //单次定位
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mlocationClient.setLocationListener(this);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void initData() {
        if (SharedPreferencesUtils.getParam(getApplication(), "islogin", "") == null) {
            SharedPreferencesUtils.setParam(getApplication(), "islogin", "0");
        }
        SharedPreferencesUtils.setParam(getApplication(), "news", "0");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                IntentView();
            }
        }, 1000);


    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        Tools.setLog("定位成功");
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            SharedPreferencesUtils.setParam(getApplication(), "city", aMapLocation.getCity());
            SharedPreferencesUtils.setParam(getApplication(), "jd", aMapLocation.getLongitude());
            SharedPreferencesUtils.setParam(getApplication(), "wd", aMapLocation.getLatitude());
            SharedPreferencesUtils.setParam(getApplication(), "sheng", aMapLocation.getProvince());
            SharedPreferencesUtils.setParam(getApplication(), "qu", aMapLocation.getDistrict());
            Tools.setLog(aMapLocation.getCity() + ", " +
                    aMapLocation.getAddress() + ", " +
                    aMapLocation.getDistrict() + "," +
                    aMapLocation.getStreet() + "," +
                    aMapLocation.getLatitude() + ", " + aMapLocation.getLongitude() + "\n" + SharedPreferencesUtils.getParam(getApplication(), "district", " "));
        } else {
            //Tools.showToast(this, "请打开定位权限");
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Tools.setLog("定位开始");
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(Guide.this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        deactivate();
    }


    private void IntentView() {
        intent = new Intent(Guide.this, /*TestRefresh.class);//*/FragmentMain.class);
        startActivity(intent);
        finish();
    }

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
