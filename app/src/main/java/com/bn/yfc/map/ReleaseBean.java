package com.bn.yfc.map;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */

public class ReleaseBean implements Serializable {
    public String getAritcle() {
        return aritcle;
    }

    public void setAritcle(String aritcle) {
        this.aritcle = aritcle;
    }

    public String getReceiverShowTime() {
        return receiverShowTime;
    }

    public void setReceiverShowTime(String receiverShowTime) {
        this.receiverShowTime = receiverShowTime;
    }

    public String getAritcleIonc2() {
        return aritcleIonc2;
    }

    public void setAritcleIonc2(String aritcleIonc2) {
        this.aritcleIonc2 = aritcleIonc2;
    }

    public String getAritcleIonc1() {
        return aritcleIonc1;
    }

    public void setAritcleIonc1(String aritcleIonc1) {
        this.aritcleIonc1 = aritcleIonc1;
    }

    public String getAritcleIonc3() {
        return aritcleIonc3;
    }

    public void setAritcleIonc3(String aritcleIonc3) {
        this.aritcleIonc3 = aritcleIonc3;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getSendeShowTime() {
        return sendeShowTime;
    }

    public void setSendeShowTime(String sendeShowTime) {
        this.sendeShowTime = sendeShowTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderSelectAddress() {
        return senderSelectAddress;
    }

    public void setSenderSelectAddress(String senderSelectAddress) {
        this.senderSelectAddress = senderSelectAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverSelectAddress() {
        return receiverSelectAddress;
    }

    public void setReceiverSelectAddress(String receiverSelectAddress) {
        this.receiverSelectAddress = receiverSelectAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    String cartype;
    String aritcle;
    String aritcleIonc1;
    String aritcleIonc2;
    String aritcleIonc3;
    String prix;
    String remarks;


    String sendeShowTime;
    String senderName;
    String senderAddress;
    String senderPhone;
    String senderSelectAddress;


    String receiverPhone;
    String receiverAddress;
    String receiverSelectAddress;
    String receiverName;
    String receiverShowTime;
}
