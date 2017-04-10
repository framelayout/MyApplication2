package com.bn.yfc.buy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.map.MapTest;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bn.yfc.tools.Tools.*;

/**
 * Created by Administrator on 2017/1/2.
 */

public class SelectAddress extends BaseActivity implements OnClickListener, LocationSource, AMapLocationListener, OnGeocodeSearchListener {
    String JumpType;
    boolean oper = true;
    private Intent intent;
    private MapView mapview;
    private AMap amap;
    private AutoCompleteTextView selt;
    private Button but;
    /***/
    private double latduble;
    private double longduble;
    private Marker marker;
    private LatLng lat;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private String city;
    private ImageView left;
    private TextView title;
    /***/
    List<String> data = new ArrayList<String>();
    List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectaddress);
        initHead();
        initData();
        initView(savedInstanceState);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("选择地址");
    }

    private void initView(Bundle savedInstanceState) {
        but = (Button) findViewById(R.id.selectaddress_but);
        but.setOnClickListener(this);
        selt = (AutoCompleteTextView) findViewById(R.id.selectaddress_selt);
        mapview = (MapView) findViewById(R.id.selectaddress_mapview);
        mapview.onCreate(savedInstanceState);
        amap = mapview.getMap();
        initAMap(amap);
        selt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oper = false;
                startInput(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        selt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                oper = true;
                TextView it = (TextView) view.findViewById(R.id.list_item_text);
                Tools.showToast(SelectAddress.this, it.getText().toString());
                LatLonPoint point = (LatLonPoint) listData.get(i).get("LatLonPoint");
                LatLng latin = new LatLng(point.getLatitude(), point.getLongitude());
                latduble = point.getLatitude();
                longduble = point.getLongitude();
                animaMap(latin);
                setMarker(latin, it.getText().toString());
            }
        });
    }

    private void initAMap(AMap amap) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.toum));
        myLocationStyle.strokeColor(0x00ffffff);
        myLocationStyle.radiusFillColor(0x00e4e4e4);
        myLocationStyle.strokeWidth(1.0f);
        amap.setMyLocationStyle(myLocationStyle);
        amap.setMapType(AMap.MAP_TYPE_NORMAL);        //设置地图模式
        // 定位回调
        amap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Tools.setLog("手指点击坐标为:" + latLng.latitude + ";" + latLng.longitude);
                latduble = latLng.latitude;
                longduble = latLng.longitude;
                animaMap(latLng);
                oper = true;
                latlongTurnAddrress(latLng);
                setMarker(latLng, selt.getText().toString());

            }

        });
        amap.setLocationSource(this);
        amap.getUiSettings().setMyLocationButtonEnabled(true);    // 设置定位按钮
        amap.setMyLocationEnabled(true);       // true ：定位层可触发定位 ，false ：隐藏定位层不可触发定位
    }


    private void initData() {
        if (getIntent() == null) {
            return;
        }
        JumpType = getIntent().getStringExtra("jump");
        setLog("jumpType ==" + JumpType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    /***
     * 地图业务区
     */
    private void setAdpater(List<String> datass) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, datass);
        selt.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void startInput(String searchKey) {
        //Tools.setLog("获取地图提示信息");
        InputtipsQuery query = new InputtipsQuery(searchKey, (String) SharedPreferencesUtils.getParam(getApplication(), "city", " "));
        Inputtips inps = new Inputtips(this, query);
        inps.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                if (listData != null) {
                    listData.clear();
                    listData = null;
                }
                if (data != null) {
                    data.clear();
                    data = null;
                }
                data = new ArrayList<String>();
                listData = new ArrayList<Map<String, Object>>();
                for (int is = 0; is < list.size(); is++) {
                    // Tools.setLog("获取的提示信息 ：" + list.get(is).getName());
                    data.add(list.get(is).getName());
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("LatLonPoint", list.get(is).getPoint());
                    listData.add(maps);
                    // Tools.setLog("给出选择项的经纬度:" + list.get(is).getPoint().getLatitude() + "," + list.get(is).getPoint().getLongitude());
                }
                setAdpater(data);
            }
        });
        inps.requestInputtipsAsyn();
    }

    private void setMarker(LatLng latlng, String s) {
        if (marker == null) {
            marker = amap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.address_icon)).title("当前位置").snippet(s));
        } else {
            amap.clear();
            marker.destroy();
            marker = null;
            marker = amap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.address_icon)).title("当前位置").snippet(s));
        }

    }

    //地址转经纬度
    private void addressTurnLatLong(String AddressKey) {
        // Tools.setLog("设置地址转经纬度");
        GeocodeSearch gsearch = new GeocodeSearch(SelectAddress.this);
        gsearch.setOnGeocodeSearchListener(this);
        //(String) SharedPreferencesUtils.getParam(getApplication(), "city", " ")
        GeocodeQuery query = new GeocodeQuery(AddressKey, (String) SharedPreferencesUtils.getParam(getApplication(), "city", " "));
        gsearch.getFromLocationNameAsyn(query);
    }

    //经纬度转地址
    private void latlongTurnAddrress(LatLng latLng) {
        // Tools.setLog("设置经纬度转地址");
        GeocodeSearch gsearch = new GeocodeSearch(SelectAddress.this);
        gsearch.setOnGeocodeSearchListener(this);
        LatLonPoint llpo = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(llpo, 1000, GeocodeSearch.AMAP);
        gsearch.getFromLocationAsyn(query);
    }

    //移动地图视角
    private void animaMap(LatLng lats) {
        amap.animateCamera(CameraUpdateFactory.newLatLngZoom(lats, 18), new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                setLog("视角偏移开始");
            }

            @Override
            public void onCancel() {
                setLog("视角偏移取消");
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //  setLog("定位成功");
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged(aMapLocation);
            city = aMapLocation.getCity();
            Tools.setLog(aMapLocation.getCity() + ", " +
                    aMapLocation.getAddress() + ", " +
                    aMapLocation.getLatitude() + ", " +
                    aMapLocation.getLongitude() + "\n");
            latduble = aMapLocation.getLatitude();
            longduble = aMapLocation.getLongitude();
            animaMap(new LatLng(latduble, longduble));
            setMarker(new LatLng(latduble, longduble), aMapLocation.getAddress());
            SharedPreferencesUtils.setParam(getApplication(), "sheng", aMapLocation.getProvince());
            SharedPreferencesUtils.setParam(getApplication(), "qu", aMapLocation.getDistrict());
            SharedPreferencesUtils.setParam(getApplication(), "city", aMapLocation.getCity());
            SharedPreferencesUtils.setParam(getApplication(), "jd", aMapLocation.getLongitude());
            SharedPreferencesUtils.setParam(getApplication(), "wd", aMapLocation.getLatitude());
            selt.setText(aMapLocation.getStreet());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        // Tools.setLog("定位开始");
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(SelectAddress.this);
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
        //Tools.setLog("定位结束");
    }

    //经纬度转地址回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //Tools.setLog("经纬度转地址回调码 ：" + i);
        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
        //  Tools.setLog("当前位置是: " + address.getProvince() + ";" + address.getCity() + "; " + address.getFormatAddress() + "; " + address.getStreetNumber().getStreet() + "/" + address.getBuilding() + "; " + address.getDistrict() + "; " + address.getTownship());
        selt.setText(address.getFormatAddress());
    }

    //地址转经纬度回调
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //Tools.setLog("地址转经纬度回调码 ：" + i);
        if (i == 1000) {
            List<GeocodeAddress> addresss = geocodeResult.getGeocodeAddressList();
            latduble = addresss.get(0).getLatLonPoint().getLatitude();
            longduble = addresss.get(0).getLatLonPoint().getLongitude();
            for (GeocodeAddress gad : addresss) {
                //    Tools.setLog("获取得到的值为" + "\n" + "经纬度:" + latduble + "," + longduble + "\n" + "返回值经纬度:" + gad.getLatLonPoint().getLatitude() + "," + gad.getLatLonPoint().getLongitude() + "\n" + "提示地址为:" + gad.getCity() + "|" + gad.getFormatAddress());
            }
            oper = true;
            //Tools.setLog("地址转经纬度获取值:" + latduble + ";" + longduble + "\n结果集长度" + addresss.size() + "\n" + addresss.get(0).getCity() + "|" + addresss.get(0).getFormatAddress());
            animaMap(new LatLng(latduble, longduble));
            setMarker(new LatLng(latduble, longduble), selt.getText().toString());
        } else {
            Tools.showToast(this, "查找失败");
        }
    }

    /***
     * 地图业务区结束
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.selectaddress_but:
                String selectAd = selt.getText().toString();
                if (Tools.isEmpty(selectAd)) {
                    Tools.showToast(this, "请选择街道地址");
                    break;
                }
                if (oper) {
                    jumpReturn(selectAd);
                } else {
                    addressTurnLatLong(selt.getText().toString());
                }

                break;
        }
    }

    private void jumpReturn(String address) {
        intent = new Intent();
        intent.putExtra("address", address);
        intent.putExtra("latduble", latduble);
        intent.putExtra("longduble", longduble);
        setResult(12, intent);
        finish();
    }
}
