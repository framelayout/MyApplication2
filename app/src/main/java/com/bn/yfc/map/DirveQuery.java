package com.bn.yfc.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.WalkRouteResult;
import com.bn.yfc.tools.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class DirveQuery implements RouteSearch.OnRouteSearchListener {
    static RouteSearch search;
    static RouteSearch.DriveRouteQuery driveuqery;
    static DriveRouteResult result = new DriveRouteResult();
    static List<DrivePath> resuList = new ArrayList<DrivePath>();
    static float dis = 0;
    float aaa = 0;
    Activity ax = new Activity();

    public float ReturnDrive(Context context, LatLonPoint from, LatLonPoint to, Activity ac) throws AMapException {
        ax = ac;
        search = new RouteSearch(context);
        search.setRouteSearchListener(this);
        //速度优先
        driveuqery = new RouteSearch.DriveRouteQuery(new FromAndTo(from, to), RouteSearch.DrivingShortDistance, null, null, " ");
        search.calculateDriveRouteAsyn(driveuqery);
        return aaa;
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        Tools.setLog("接受回调信息");
        if (driveRouteResult != null) {

            Tools.setLog("计算距离异步调用 返回结果不为空");
            List<DrivePath> da = new ArrayList<DrivePath>();
            da = driveRouteResult.getPaths();
            if (da != null) {
                Tools.setLog("驾车线路规划结果集不为空" + da.size());
            }
            for (DrivePath path : da) {
                if (path != null) {
                    Tools.setLog("规划集取值不为空" + da.size());
                    List<DriveStep> setp = new ArrayList<DriveStep>();
                    setp = path.getSteps();
                    Tools.setLog("距离取值不为空" + setp.size());
                    for (DriveStep se : setp) {
                        if (se != null) {
                            aaa += se.getDistance();
                            Tools.setLog("距离取值===" + se.getDistance() + se.getAction() + "|" + se.getOrientation() + "|" + se.getRoad());
                        }
                    }
                }
            }
            ((ReleaseDelivery) ax).ReceiveDic(aaa);
            Tools.setLog("最终距离为===" + aaa);
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
