package com.bn.yfc.map;

/**
 * Created by Administrator on 2017/1/2.
 */

public class DeliveryBean {
    public DeliveryBean(String type, int icon) {
        this.type = type;
        this.icon = icon;
    }

    //车辆类型
    String type;
    //车辆图片
    int icon;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setAll(String type, int icon) {
        this.type = type;
        this.icon = icon;
    }
}
