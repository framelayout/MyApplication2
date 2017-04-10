package com.bn.yfc.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/21.
 */

public class LatLongBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private double latduble;
    private double longduble;
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLatduble(double latduble) {
        this.latduble = latduble;
    }

    public void setLongduble(double longduble) {
        this.longduble = longduble;
    }

    public double getLatduble() {
        return latduble;
    }

    public double getLongduble() {
        return longduble;
    }
}
