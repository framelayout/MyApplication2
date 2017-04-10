package com.bn.yfc.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class DeliveryData implements Serializable {
    private String isAudit;

    //获取审核状态
    public String getIsAudit() {
        return isAudit;
    }

    //设置审核状态
    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    private String deName;
    private String deAge;
    private String deAddress;
    private String deContact;
    private String dePositive;
    private String deNegative;
    private String deIcon;
    private String deBank;
    private String deDrive;
    private String deDriving;
    private String deCarType;
    private String dePhone;

    public String getDePhone() {
        return dePhone;
    }

    public void setDePhone(String dePhone) {
        this.dePhone = dePhone;
    }

    //获取车辆类型
    public String getDeCarType() {
        return deCarType;
    }

    //设置车辆类型
    public void setDeCarType(String carType) {
        this.deCarType = carType;
    }

    //获取审核人姓名
    public String getDeName() {
        return deName;
    }

    //设置审核人姓名
    public void setDeName(String deName) {
        this.deName = deName;
    }

    //获取审核人性别
    public String getDeAge() {
        return deAge;
    }

    //设置审核人性别
    public void setDeAge(String deAge) {
        this.deAge = deAge;
    }

    //获取审核人地址
    public String getDeAddress() {
        return deAddress;
    }

    //设置审核人地址
    public void setDeAddress(String deAddress) {
        this.deAddress = deAddress;
    }


    // 获取紧急联系方式
    public String getDeContact() {
        return deContact;
    }

    //设置紧急联系电话
    public void setDeContact(String deContact) {
        this.deContact = deContact;
    }

    //获取手持身份证正面照片
    public String getDePositive() {
        return dePositive;
    }

    //设置手持身份证正面照片
    public void setDePositive(String dePositive) {
        this.dePositive = dePositive;
    }

    //获取手持身份证反面照片
    public String getDeNegative() {
        return deNegative;
    }

    //设置手持身份证反面照片
    public void setDeNegative(String deNegative) {
        this.deNegative = deNegative;
    }

    //获取审核人头像
    public String getDeIcon() {
        return deIcon;
    }

    //设置审核人头像
    public void setDeIcon(String deIcon) {
        this.deIcon = deIcon;
    }

    //获取手持银行卡
    public String getDeBank() {
        return deBank;
    }

    //设置手持银行卡照片
    public void setDeBank(String deBank) {
        this.deBank = deBank;
    }

    //获取手持驾照照片
    public String getDeDrive() {
        return deDrive;
    }

    //设置手持驾照照片
    public void setDeDrive(String deDrive) {
        this.deDrive = deDrive;
    }

    //获取手持机动车行驶证照片
    public String getDeDriving() {
        return deDriving;
    }

    //设置手持机动车行驶证照片
    public void setDeDriving(String deDriving) {
        this.deDriving = deDriving;
    }


}
