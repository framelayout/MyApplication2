package com.bn.yfc.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.bn.yfc.R;
import com.bn.yfc.TestPost;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.base.LatLongBean;
import com.bn.yfc.base.NoticeTest;
import com.bn.yfc.base.Tools;
import com.bn.yfc.fragmentmain.FragmentMain;
import com.bn.yfc.map.MapTest;


import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class MainActivity extends BaseActivity implements OnRouteSearchListener {
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private Button but1;
    private TextView tvone;
    private Intent intent;
    private TextView tvtwo;
    private SharedPreferences pre;
    LatLongBean fromLatLongBean;
    LatLongBean toLatLongBean;

    private void showToast() {
        Tools.showIcToast(this, "启动APP");
        Tools.setLog(Tools.getCurrentTime());
        Tools.setLog(Tools.getTimeStamp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        initData();
        initView();
        showToast();
    }

    private void initData() {

        pre = getSharedPreferences("mainAddress", MODE_PRIVATE);

    }

    private void initView() {
        but1 = (Button) findViewById(R.id.main_bu1);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), FragmentMain.class);
                //    Intent intent = new Intent(getApplication(), TestPost.class);
                startActivity(intent);
            }
        });
        tvone = (TextView) findViewById(R.id.main_address_one);
        tvtwo = (TextView) findViewById(R.id.main_address_two);
    }

    public void IntentNoctie(View v) {
        Intent intent = new Intent(this, NoticeTest.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                Tools.setLog("回调的resultCode ：" + resultCode);
                Tools.setLog("发货地址 ：" + data.getStringExtra("userAddress"));
                tvone.setText(data.getStringExtra("userAddress"));
                fromLatLongBean = (LatLongBean) data.getBundleExtra("bundle").getSerializable("Address");
                break;
            case 2:
                Tools.setLog("回调的resultCode ：" + resultCode);
                Tools.setLog("收货地址 ：" + data.getStringExtra("userAddress"));
                tvtwo.setText(data.getStringExtra("userAddress"));
                toLatLongBean = (LatLongBean) data.getBundleExtra("bundle").getSerializable("Address");
                break;
        }
        Compute();
    }

    public void Compute() {
        Tools.setLog("判断发货和收货地址是否不为空" + AddreIf());
        if (AddreIf()) {
            Tools.setLog("开始计算距离");
            SearchDistance(this, new LatLng(fromLatLongBean.getLatduble(), fromLatLongBean.getLongduble()), new LatLng(toLatLongBean.getLatduble(), toLatLongBean.getLongduble()));
            Tools.setLog("最短路径计算 ：" + AMapUtils.calculateArea(new LatLng(fromLatLongBean.getLatduble(), fromLatLongBean.getLongduble()), new LatLng(toLatLongBean.getLatduble(), toLatLongBean.getLongduble())));
        }
    }

    private boolean AddreIf() {
        String from = tvone.getText().toString();
        String to = tvtwo.getText().toString();
        if (Tools.isEmpty(from) || Tools.isEmpty(to)) {
            return false;
        } else {
            return true;
        }
    }

    public void textoneonclick(View v) {
        intent = new Intent(this, MapTest.class);
        switch (v.getId()) {

            case R.id.main_address_one:
                intent.putExtra("address", "one");
                break;
            case R.id.main_address_two:
                intent.putExtra("address", "two");
                break;
        }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void SearchDistance(Context context, LatLng startpoint, LatLng end) {
        RouteSearch routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(new FromAndTo(new LatLonPoint(startpoint.latitude, startpoint.longitude), new LatLonPoint(end.latitude, end.longitude)), 0, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        Tools.setLog("驾车路线计算结果集" + "\n code为" + i);
        List<DrivePath> drives = driveRouteResult.getPaths();
        float dis = 0;
        for (int it = 0; it < drives.size(); it++) {
            List<DriveStep> mDrvestep = drives.get(it).getSteps();
            for (int t = 0; t < mDrvestep.size(); t++) {
                dis += mDrvestep.get(t).getDistance();
            }
        }

        String mDistance = String.valueOf(dis);
        Tools.setLog("距离为：" + mDistance + "米");
        Tools.showToast(this, "距离为 " + mDistance + "米");
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
