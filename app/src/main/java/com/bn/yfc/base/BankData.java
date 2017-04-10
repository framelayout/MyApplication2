package com.bn.yfc.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BankData implements Serializable {
    //银行卡ID
    String bankId;

    //获取银行卡ID
    public String getBankId() {
        return bankId;
    }

    //设置银行卡ID
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    //银行卡号码
    String bankNumber;

    //设置银行卡号码
    public String getBankNumber() {
        return bankNumber;
    }

    //获取银行卡号码
    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    //银行卡持有人
    String bankPos;

    //获取银行卡持有人
    public String getBankPos() {
        return bankPos;
    }

    //设置银行卡持有人
    public void setBankPos(String bankPos) {
        this.bankPos = bankPos;
    }


    //开户行类型
    String bankType;

    //设置开户行类型
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    //获取开户行类型
    public String getBankType() {
        return bankType;
    }

    //开户行地址
    String bankCard;

    //设置开户行地址
    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    //获取开户行地址
    public String getBankCard() {
        return bankCard;
    }

    String bankDefa;

    public String getBankDefa() {
        return bankDefa;
    }

    public void setBankDefa(String bankDefa) {
        this.bankDefa = bankDefa;
    }


}
