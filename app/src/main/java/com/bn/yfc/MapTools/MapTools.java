package com.bn.yfc.MapTools;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.bn.yfc.base.Tools;

import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by Administrator on 2016/11/21.
 */

public class MapTools {
    private static String addres;
    private static LatLng latLng;
    private static String mDistance;

    private void showToast() {
        Tools.setLog("MAP 工具类启动");
    }

    public static String ComputeDistance(Context context, String from, String to) {
        SearchDistance(context, InpitAddress(context, from), InpitAddress(context, to));
        return mDistance;
    }

    public static void SearchDistance(Context context, LatLng startpoint, LatLng end) {
        RouteSearch routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(new OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                Tools.setLog("驾车路线计算结果集");

                List<DrivePath> drives = driveRouteResult.getPaths();
                float dis = 0;
                for (int it = 0; it < drives.size(); it++) {
                    List<DriveStep> mDrvestep = drives.get(it).getSteps();
                    for (int t = 0; t < mDrvestep.size(); t++) {
                        dis += mDrvestep.get(t).getDistance();
                    }
                }
                mDistance = String.valueOf(dis);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        DriveRouteQuery query = new DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(startpoint.latitude, startpoint.longitude), new LatLonPoint(end.latitude, end.longitude)), 0, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);

    }

    public static LatLng OutLatLongDue(Context context, String addr) {
        InpitAddress(context, addr);
        return latLng;
    }

    public static String OutAdress(Context context, LatLng lng) {
        InpitLatLong(context, lng);
        return addres;
    }

    private static void InpitLatLong(Context context, LatLng latLng) {
        Tools.setLog("设置经纬度转地址");
        GeocodeSearch gsearch = new GeocodeSearch(context);
        gsearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                Tools.setLog("MAP工具类经纬度转城市地图回调码 ：" + i);
                RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
                Tools.setLog("当前位置是: " + address.getCity() + "; " + address.getFormatAddress() + "; " + address.getStreetNumber().getStreet() + "/" + address.getBuilding() + "; " + address.getDistrict() + "; " + address.getTownship());
                addres = address.getFormatAddress();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint llpo = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(llpo, 1000, GeocodeSearch.GPS);
        gsearch.getFromLocationAsyn(query);
    }

    //地址转经纬度
    private static LatLng InpitAddress(Context context, String AddressKey) {
        Tools.setLog("设置地址转经纬度");
        GeocodeSearch gsearch = new GeocodeSearch(context);
        gsearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                Tools.setLog("MAP工具类城市转编码经纬度回调码 ：" + i);
                List<GeocodeAddress> addresss = geocodeResult.getGeocodeAddressList();
                Tools.setLog(addresss.get(0).getLatLonPoint().getLatitude() + ";" + addresss.get(0).getLatLonPoint().getLongitude());
                latLng = new LatLng(addresss.get(0).getLatLonPoint().getLatitude(), addresss.get(0).getLatLonPoint().getLongitude());
            }
        });
        GeocodeQuery query = new GeocodeQuery(AddressKey, "重庆");
        gsearch.getFromLocationNameAsyn(query);
        return latLng;
    }

}
