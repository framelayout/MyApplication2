package com.bn.yfc.map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.base.LatLongBean;
import com.bn.yfc.base.Tools;
import com.bn.yfc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MapTest extends BaseActivity implements LocationSource, AMapLocationListener, OnGeocodeSearchListener {
    private MapView mMapView;
    private AMap amap;
    private AutoCompleteTextView autoText;
    private LatLng lat;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker marker;
    private String address;
    private String type;
    private Intent intent;
    private double latduble;
    private double longduble;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptest);
        initData();
        initMap(savedInstanceState);
        initView();
        Tools.setLog("判断是否编译");
    }

    private void initData() {
        type = getIntent().getStringExtra("address");
        Tools.setLog("由" + type + "传递");
    }

    public void complete(View v) {
        if (v.getId() == R.id.maptest_but) {
            Tools.setLog("点击finish");
            intent = new Intent();
            Bundle bundle = new Bundle();
            LatLongBean bean = new LatLongBean();
            Tools.setLog("传递回Mian的数据 :" + latduble + "," + longduble + " ;" + autoText.getText().toString());
            bean.setAddress(autoText.getText().toString());
            bean.setLatduble(latduble);
            bean.setLongduble(longduble);
            bundle.putSerializable("Address", bean);
            intent.putExtra("bundle", bundle);
            intent.putExtra("userAddress", autoText.getText().toString());
            switch (type) {
                case "one":
                    setResult(1, intent);
                    break;
                case "two":
                    setResult(2, intent);
                    Tools.setLog("设置为2");
                    break;
            }

            this.finish();
            Tools.setLog("已经finish");
        }

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

    private void initView() {
        autoText = (AutoCompleteTextView) findViewById(R.id.maptest_autotext);
        autoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Tools.setLog("之前输入值变更 ：" + charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Tools.setLog("text更改变化 ：" + charSequence.toString() + ";" + i + ";" + i1 + ";" + i2);
                startInput(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Tools.setLog("之后输入值变更 ：" + editable.toString());
            }
        });
        autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView it = (TextView) view.findViewById(R.id.list_item_text);
                Tools.showToast(MapTest.this, it.getText().toString());
                statInpitAddress(it.getText().toString());
            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mapviewtest);
        mMapView.onCreate(savedInstanceState);
        amap = mMapView.getMap();
        initAMap(amap);
    }

    private void initAMap(final AMap aMap) {
        Tools.setLog(" init  Amap");
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.toum));
        myLocationStyle.strokeColor(0x00ffffff);
        myLocationStyle.radiusFillColor(0x00e4e4e4);
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);        //设置地图模式
        // 定位回调
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Tools.setLog("手指点击坐标为:" + latLng.latitude + ";" + latLng.longitude);
                latduble = latLng.latitude;
                longduble = latLng.longitude;
                animaMap(latLng);
                setSerchAddress(latLng);
                setMarker(latLng, autoText.getText().toString());

            }

        });
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);    // 设置定位按钮
        aMap.setMyLocationEnabled(true);       // true ：定位层可触发定位 ，false ：隐藏定位层不可触发定位
    }

    private void setSerchAddress(LatLng latLng) {
        Tools.setLog("设置经纬度转地址");
        GeocodeSearch gsearch = new GeocodeSearch(MapTest.this);
        gsearch.setOnGeocodeSearchListener(this);
        LatLonPoint llpo = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(llpo, 1000, GeocodeSearch.AMAP);
        gsearch.getFromLocationAsyn(query);
    }

    //地址转经纬度
    private void statInpitAddress(String AddressKey) {
        Tools.setLog("设置地址转经纬度");
        GeocodeSearch gsearch = new GeocodeSearch(MapTest.this);
        gsearch.setOnGeocodeSearchListener(this);
        GeocodeQuery query = new GeocodeQuery(AddressKey, city);
        gsearch.getFromLocationNameAsyn(query);
    }

    //移动地图视角
    private void animaMap(LatLng lats) {
        amap.animateCamera(CameraUpdateFactory.newLatLngZoom(lats, 18), new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Tools.setLog("视角偏移开始");
            }

            @Override
            public void onCancel() {
                Tools.setLog("视角偏移取消");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.setLog("MAP onDestroy");
        mMapView.onDestroy();
        deactivate();
        cleanMap();
    }

    private void cleanMap() {
        if (marker != null) {
            marker.destroy();
            marker = null;
        }
        if (amap != null) {
            amap = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }


    private void startInput(String searchKey) {
        Tools.setLog("获取地图提示信息");
        InputtipsQuery query = new InputtipsQuery(searchKey, "重庆");
        Inputtips inps = new Inputtips(this, query);
        inps.setInputtipsListener(new InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                List<String> data = new ArrayList<String>();
                for (int is = 0; is < list.size(); is++) {
                    Tools.setLog("获取的提示信息 ：" + list.get(is).getName());
                    data.add(list.get(is).getName());
                }
                setAdpater(data);
            }
        });
        inps.requestInputtipsAsyn();
    }


    private void setAdpater(List<String> datas) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, datas);
        autoText.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Tools.setLog("经纬度转地址回调码 ：" + i);
        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
        Tools.setLog("当前位置是: " + address.getProvince() + ";" + address.getCity() + "; " + address.getFormatAddress() + "; " + address.getStreetNumber().getStreet() + "/" + address.getBuilding() + "; " + address.getDistrict() + "; " + address.getTownship());
        autoText.setText(address.getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Tools.setLog("地址转经纬度回调码 ：" + i);
        if (i == 1000) {
            List<GeocodeAddress> addresss = geocodeResult.getGeocodeAddressList();
            latduble = addresss.get(0).getLatLonPoint().getLatitude();
            longduble = addresss.get(0).getLatLonPoint().getLongitude();
            Tools.setLog("地址转经纬度获取值:" + latduble + ";" + longduble + "\n结果集长度" + addresss.size());
            animaMap(new LatLng(latduble, longduble));
            setMarker(new LatLng(latduble, longduble), autoText.getText().toString());
        } else {
            Tools.showToast(this, "查找失败");
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Tools.setLog("定位成功");
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
            autoText.setText(aMapLocation.getAddress());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Tools.setLog("定位开始");
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(MapTest.this);
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
}
