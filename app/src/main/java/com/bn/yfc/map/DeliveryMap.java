package com.bn.yfc.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bn.yfc.R;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Login;
import com.bn.yfc.user.Permissions;
import com.bn.yfc.user.TestPay;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bn.yfc.base.Tools.*;

/**
 * Created by Administrator on 2017/1/2.
 */

public class DeliveryMap extends SupportMapFragment implements OnClickListener, LocationSource, AMapLocationListener {
    private View view;
    private TextureMapView mapView;
    private AMap amap;
    private Intent intent;
    private DeliveryBean bean;
    private List<DeliveryBean> datas;
    private HorizontalListView listview;
    private ListAdapter adapter;
    private TextView title;
    private Button getb1, getb2, getb3, getb4, getb5, getb6, getb7, getb8, getb9, getb10;
    private LinearLayout b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
    private ImageView left, right;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker marker;
    private double latduble;
    private double longduble;
    private int posNumber = 1;
    private GridAdapter adap;
    private LinearLayout expand;
    private HorizontalScrollView hoscrview;
    private LinearLayout test;
    private TextView texttest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.deliverymaptest, null);
        initData(view);
      /*  intent = new Intent(getActivity(), TestLinearListViewActivity.class);
        startActivity(intent);*/
        initHead(view);
        initView(view);
        initCarType(view);
        // generateBitmap();
        initMap(view, savedInstanceState);
        return view;
    }

    private void initCarType(View view) {
        Tools.setLog("设置Lin布局");
        hoscrview = (HorizontalScrollView) view.findViewById(R.id.delivermapview_holistview);
        test = (LinearLayout) view.findViewById(R.id.deliverymaptest_test);
        test.setClickable(true);
        getb1 = (Button) view.findViewById(R.id.deliverymap_butb1);
        b1 = (LinearLayout) view.findViewById(R.id.deliverymap_but1);
        b1 = Tools.setLinearLayouWidth(getActivity(), b1, 5.5);
        getb1.setOnClickListener(this);
        getb2 = (Button) view.findViewById(R.id.deliverymap_butb2);
        b2 = (LinearLayout) view.findViewById(R.id.deliverymap_but2);
        b2 = Tools.setLinearLayouWidth(getActivity(), b2, 5.5);
        getb2.setOnClickListener(this);
        getb3 = (Button) view.findViewById(R.id.deliverymap_butb3);
        b3 = (LinearLayout) view.findViewById(R.id.deliverymap_but3);
        b3 = Tools.setLinearLayouWidth(getActivity(), b3, 5.5);
        getb3.setOnClickListener(this);
        getb4 = (Button) view.findViewById(R.id.deliverymap_butb4);
        b4 = (LinearLayout) view.findViewById(R.id.deliverymap_but4);
        b4 = Tools.setLinearLayouWidth(getActivity(), b4, 5.5);
        getb4.setOnClickListener(this);
        getb5 = (Button) view.findViewById(R.id.deliverymap_butb5);
        b5 = (LinearLayout) view.findViewById(R.id.deliverymap_but5);
        b5 = Tools.setLinearLayouWidth(getActivity(), b5, 5.5);
        getb5.setOnClickListener(this);
        getb6 = (Button) view.findViewById(R.id.deliverymap_butb6);
        b6 = (LinearLayout) view.findViewById(R.id.deliverymap_but6);
        b6 = Tools.setLinearLayouWidth(getActivity(), b6, 5.5);
        getb6.setOnClickListener(this);
        getb7 = (Button) view.findViewById(R.id.deliverymap_butb7);
        b7 = (LinearLayout) view.findViewById(R.id.deliverymap_but7);
        b7 = Tools.setLinearLayouWidth(getActivity(), b7, 5.5);
        getb7.setOnClickListener(this);
        getb8 = (Button) view.findViewById(R.id.deliverymap_butb8);
        b8 = (LinearLayout) view.findViewById(R.id.deliverymap_but8);
        b8 = Tools.setLinearLayouWidth(getActivity(), b8, 5.5);
        getb8.setOnClickListener(this);
        getb9 = (Button) view.findViewById(R.id.deliverymap_butb9);
        b9 = (LinearLayout) view.findViewById(R.id.deliverymap_but9);
        b9 = Tools.setLinearLayouWidth(getActivity(), b9, 5.5);
        getb9.setOnClickListener(this);
        getb10 = (Button) view.findViewById(R.id.deliverymap_butb10);
        b10 = (LinearLayout) view.findViewById(R.id.deliverymap_but10);
        b10 = Tools.setLinearLayouWidth(getActivity(), b10, 5.5);
        getb10.setOnClickListener(this);
    }

    private void initMap(View view, Bundle savedInstanceState) {
        mapView = (TextureMapView) view.findViewById(R.id.deliverymap_mapview);
        mapView.onCreate(savedInstanceState);
        Tools.setLog("Map  onCreate");
        amap = mapView.getMap();
        initAmap();
    }


    private void initAmap() {
        setLog(" init  Amap");
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
                setLog("手指点击坐标为:" + latLng.latitude + ";" + latLng.longitude);
                latduble = latLng.latitude;
                longduble = latLng.longitude;
                animaMap(latLng);
               /* setSerchAddress(latLng);*/
                setMarker(latLng, "" + SharedPreferencesUtils.getParam(getActivity(), "city", " "));

            }

        });
        amap.setLocationSource(this);
        amap.getUiSettings().setMyLocationButtonEnabled(true);    // 设置定位按钮
        amap.setMyLocationEnabled(true);       // true ：定位层可触发定位 ，false ：隐藏定位层不可触发定位
    }

    private void initHead(View view) {
        left = (ImageView) view.findViewById(R.id.head_left);
        left.setBackgroundResource(R.drawable.release_icon);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        right = (ImageView) view.findViewById(R.id.head_right);
        right.setVisibility(View.VISIBLE);
        right.setBackgroundResource(R.drawable.news_icon);
        right.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.head_title);
        title.setText("发布订单");
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  intent = new Intent(getActivity(), TestPay.class);
                startActivity(intent);*/
            }
        });
        setRightVew();
    }

    private void initView(View view) {
        expand = (LinearLayout) view.findViewById(R.id.deliverymap_expand);
        expand.setOnClickListener(this);
    }

    private void initData(View view) {
        datas = new ArrayList<DeliveryBean>();
        datas.add(new DeliveryBean("摩托车", R.drawable.motorcycle));
        datas.add(new DeliveryBean("三轮车", R.drawable.pedicab_icon));
        datas.add(new DeliveryBean("顺路送", R.drawable.transport_icon));
        datas.add(new DeliveryBean("轿车", R.drawable.car));
        datas.add(new DeliveryBean("小面包车", R.drawable.thevan_small));
        datas.add(new DeliveryBean("中面包车", R.drawable.thevan_in));
        datas.add(new DeliveryBean("小货车", R.drawable.truck_smalll));
        datas.add(new DeliveryBean("中货车", R.drawable.truck_in));
        datas.add(new DeliveryBean("大货车", R.drawable.truck_big));
        datas.add(new DeliveryBean("船运", R.drawable.ferry));
        adapter = new ListAdapter(getActivity(), datas);
        adap = new GridAdapter(getActivity(), datas);
    }

    private void startIntent(String cartype) {
        if (cartype.equals("船运")) {
            intent = new Intent(getActivity(), Shipping.class);
            startActivity(intent);
            return;
        }
        intent = new Intent(getActivity(), ReleaseDelivery1.class);
        if (cartype.equals("顺路送")) {
            intent.putExtra("type", "3");
        } else {
            intent.putExtra("type", String.valueOf(posNumber));
        }
        intent.putExtra("cartype", cartype);
        startActivity(intent);
    }

    private void login() {
        intent = new Intent(getActivity(), Login.class);
        intent.putExtra("logintype", "delivery");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deliverymap_expand:
                showCartypePopWindow(view);
                break;
            case R.id.head_left:
                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent("摩托车");
                } else {
                    login();
                }

                break;
            case R.id.head_right:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), NewsInterface.class);
                    startActivity(intent);
                } else {
                    login();
                }

                break;
            case R.id.deliverymap_butb1:
                posNumber = 1;
                setLinColor();
                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb1.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb2:
                posNumber = 2;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb2.getText().toString());
                } else {
                    login();
                }

                break;
            case R.id.deliverymap_butb3:
                posNumber = 3;
                setLinColor();
                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb3.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb4:
                posNumber = 4;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb4.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb5:
                posNumber = 5;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb5.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb6:
                posNumber = 6;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb6.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb7:
                posNumber = 7;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb7.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb8:
                posNumber = 8;
                setLinColor();

                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb8.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb9:
                posNumber = 9;
                setLinColor();
                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb9.getText().toString());
                } else {
                    login();
                }
                break;
            case R.id.deliverymap_butb10:
                posNumber = 10;
                setLinColor();
                if (Permissions.userLoginPermissions(getActivity())) {
                    startIntent(getb10.getText().toString());
                } else {
                    login();
                }
                break;
        }
    }

    private void setLinColor() {
        Tools.setLog("修改车型背景");
        getb1.setBackgroundResource(R.drawable.butbackwith);
        getb2.setBackgroundResource(R.drawable.butbackwith);
        getb3.setBackgroundResource(R.drawable.butbackwith);
        getb4.setBackgroundResource(R.drawable.butbackwith);
        getb5.setBackgroundResource(R.drawable.butbackwith);
        getb6.setBackgroundResource(R.drawable.butbackwith);
        getb7.setBackgroundResource(R.drawable.butbackwith);
        getb8.setBackgroundResource(R.drawable.butbackwith);
        getb9.setBackgroundResource(R.drawable.butbackwith);
        getb10.setBackgroundResource(R.drawable.butbackwith);
        switch (posNumber) {
            case 1:
                getb1.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 2:
                getb2.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 3:
                getb3.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 4:
                getb4.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 5:
                getb5.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 6:
                getb6.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 7:
                getb7.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 8:
                getb8.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 9:
                getb9.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
            case 10:
                getb10.setBackgroundResource(R.drawable.audit_but_onclick);
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        setLog("定位成功");
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged(aMapLocation);
            SharedPreferencesUtils.setParam(getActivity(), "city", aMapLocation.getCity());
            setLog(aMapLocation.getCity() + ", " +
                    aMapLocation.getAddress() + ", " +
                    aMapLocation.getLatitude() + ", " +
                    aMapLocation.getLongitude() + "\n");
            latduble = aMapLocation.getLatitude();
            longduble = aMapLocation.getLongitude();
            animaMap(new LatLng(latduble, longduble));
            setMarker(new LatLng(latduble, longduble), aMapLocation.getAddress());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        setLog("定位开始");
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
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

    //移动地图视角
    private void animaMap(LatLng lats) {
        amap.animateCamera(CameraUpdateFactory.newLatLngZoom(lats, 18), new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                com.bn.yfc.base.Tools.setLog("视角偏移开始");
            }

            @Override
            public void onCancel() {
                com.bn.yfc.base.Tools.setLog("视角偏移取消");
            }
        });
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

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        setLog("定位结束");
    }

    class ListAdapter extends BaseAdapter {
        List<DeliveryBean> datas;
        Context context;

        public ListAdapter(Context context, List<DeliveryBean> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas.size() < 0) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.delivermaplistitem, null);
                holder = new HolderView();
                holder.lin = (LinearLayout) convertView.findViewById(R.id.delivermap_lin);

                holder.title = (TextView) convertView.findViewById(R.id.deliverlistitem_title);
                convertView.setTag(holder);
            }
            holder = (HolderView) convertView.getTag();
            // holder.lin = Tools.setLinViewWidth(getActivity(), holder.lin, 5);
            holder.title.setText(datas.get(position).getType());
            holder.title.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tools.showToast(getActivity(), "选择了" + datas.get(position).getType());
                }
            });
            return convertView;
        }

        class HolderView {
            LinearLayout lin;
            TextView title;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deactivate();
        mapView.setVisibility(View.INVISIBLE);
        mapView.onDestroy();
        Tools.setLog("Map onDsestroyView");
        if (amap != null) {
            amap.clear();
            amap = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void SetMapVisi() {
        Tools.setLog("设置map隐藏界面");
        mapView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Tools.setLog("Map onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView.getVisibility() == View.VISIBLE) {
            Tools.setLog("并没有隐藏地图");
        } else {
            mapView.setVisibility(View.VISIBLE);
            Tools.setLog("显示隐藏地图");
        }
        mapView.onResume();
        Tools.setLog("Map onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        Tools.setLog("Map onSaveInstanceState");
    }

    /***
     * pop
     */
    private void showCartypePopWindow(View view) {
        final View popView = LayoutInflater.from(getActivity()).inflate(R.layout.cartypepop, null);
        final PopupWindow gpopupWindow = new PopupWindow(popView, MATCH_PARENT, /*WRAP_CONTENT*/MATCH_PARENT);
        GridView gridview = (GridView) popView.findViewById(R.id.cartype_pop);
        ImageView close = (ImageView) popView.findViewById(R.id.cartype_close);
        LinearLayout glin = (LinearLayout) popView.findViewById(R.id.carytpepop_lin);

        if (adap != null) {
            gridview.setAdapter(adap);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpopupWindow.dismiss();
            }
        });
        glin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpopupWindow.dismiss();
            }
        });
        gpopupWindow.setContentView(popView);
        gpopupWindow.setFocusable(false);
        gpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        gpopupWindow.showAtLocation(view.findViewById(R.id.deliverymap_linshowpop), Gravity.TOP, 0, 0);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Permissions.userLoginPermissions(getActivity())) {
                    TextView titles = (TextView) view.findViewById(R.id.cartypepop_title);
                    posNumber = position + 1;
                    startIntent(titles.getText().toString());
                    gpopupWindow.dismiss();
                } else {
                    intent = new Intent(getActivity(), Login.class);
                    intent.putExtra("logintype", "delivery");
                    startActivity(intent);
                }
            }
        });
    }

    class GridAdapter extends BaseAdapter {
        Context context;
        List<DeliveryBean> datas;

        public GridAdapter(Context context, List<DeliveryBean> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas.size() < 0) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GrHolderView holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.cartypepopitem, null);
                holder = new GrHolderView();
                holder.icon = (ImageView) convertView.findViewById(R.id.cartypepop_icon);
                holder.title = (TextView) convertView.findViewById(R.id.cartypepop_title);
                convertView.setTag(holder);
            } else {
                holder = (GrHolderView) convertView.getTag();
            }
            holder.title.setText(datas.get(position).getType());
            holder.icon.setBackgroundResource(datas.get(position).getIcon());
            return convertView;
        }

        class GrHolderView {
            ImageView icon;
            TextView title;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setRightVew();
    }

    private void setRightVew() {
        if (Permissions.userLoginPermissions(getActivity())) {
            if (Permissions.newsDataPermissions(getActivity())) {
                right.setBackgroundResource(R.drawable.newshave_icon);
            } else {
                right.setBackgroundResource(R.drawable.news_icon);
            }
        }
    }

}
