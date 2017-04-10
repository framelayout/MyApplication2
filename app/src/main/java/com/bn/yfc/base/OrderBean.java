package com.bn.yfc.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/12.
 */

public class OrderBean implements Serializable {

    //订单表id
    private String id;
    //订单号
    private String ordernum;
    //商品名字
    private String name;
    //生成订单时间戳
    private String time;
    //车辆类型(1:摩托, 2:轿车, 3:小面包车, 4:中面包车, 5:小货车, 6:中货车, 7:大货车, 8:商家, 9:船, 10:顺路送)
    private String cartype;
    //发货人电话
    private String send_tel;
    //发货人名字
    private String send_name;
    //运费
    private String send_money;
    //商品价格
    private String money;
    //距离
    private String dis;
    //发起该订单的用户id
    private String uid;
    //接到该订单的司机id
    private String did;
    //状态(0:未接单, 1:已接单未取货, 2:运送中, 3:待用户确认货物是否送达, 4:待评价, 5:完成已评价)
    private String status;
    //地区搜索索引(暂未使用)
    private String sheng;
    //地区搜索索引(暂未使用)
    private String shi;
    //发货人详细地址
    private String send_street;
    //发货人地址
    private String send_pos;
    //预发货时间
    private String send_time;
    //类型(1:普通快递, 2:顺路送)
    private String type;
    //订单评论
    private String comment;
    // 回复评论(客服回复)
    private String reply;
    //收货人名字
    private String boss_name;
    //收货人电话
    private String boss_tel;
    //收货人地址
    private String boss_pos;
    //收货人详细地址
    private String boss_street;
    //预收货时间
    private String boss_time;
    //付款编号
    private String pay_num;
    //第一张图片
    private String pic1;
    //第二张图片
    private String pic2;
    //第三张图片
    private String pic3;
    //订单备注(注意事项)
    private String tips;
    //发货人经度
    private String send_jd;
    //发货人纬度
    private String send_wd;
    //是否购买保险(0默认, 1不购买)
    private String isprotect;
    //保险费
    private String protect_money;
    //收货人经度
    private String boss_jd;
    //收货人纬度
    private String boss_wd;
    // 是否取消订单(0默认, 1取消)
    private String isexit;
    //是否处于协商状态(0默认, 1协商)
    private String isxs;

    //获取是否保险
    public String getIsprotect() {
        return isprotect;
    }

    //设置是否保险费
    public void setIsprotect(String isprotect) {
        this.isprotect = isprotect;
    }

    //获取订单表id
    public String getId() {
        return id;
    }

    //设置订单表id
    public void setId(String id) {
        this.id = id;
    }

    // 获取订单号
    public String getOrdernum() {
        return ordernum;
    }

    //设置订单号
    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    //获取商品名称
    public String getName() {
        return name;
    }

    //设置商品名称
    public void setName(String name) {
        this.name = name;
    }

    //获取订单生成时间
    public String getTime() {
        return time;
    }

    //设置订单生成时间
    public void setTime(String time) {
        this.time = time;
    }

    //获取订单车辆类型
    public String getCartype() {
        return cartype;
    }

    //设置订单车辆类型
    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    //获取发货人电话
    public String getSend_tel() {
        return send_tel;
    }

    //设置发货人电话
    public void setSend_tel(String send_tel) {
        this.send_tel = send_tel;
    }

    //获取收货人姓名
    public String getSend_name() {
        return send_name;
    }

    //设置收货人姓名
    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    //获取运费
    public String getSend_money() {
        return send_money;
    }

    //设置运费
    public void setSend_money(String send_money) {
        this.send_money = send_money;
    }

    //获取商品价格
    public String getMoney() {
        return money;
    }

    //设置商品价格
    public void setMoney(String money) {
        this.money = money;
    }

    //获取订单距离
    public String getDis() {
        return dis;
    }

    //设置订单距离
    public void setDis(String dis) {
        this.dis = dis;
    }

    //获取发起该订单的用户id
    public String getUid() {
        return uid;
    }

    //设置发起该订单的用户id
    public void setUid(String uid) {
        this.uid = uid;
    }

    //获取接到该订单的司机id
    public String getDid() {
        return did;
    }

    //设置接到该订单的司机id
    public void setDid(String did) {
        this.did = did;
    }

    //获取状态(0:未接单, 1:已接单未取货, 2:运送中, 3:待用户确认货物是否送达, 4:待评价, 5:完成已评价)
    public String getStatus() {
        return status;
    }

    //设置状态(0:未接单, 1:已接单未取货, 2:运送中, 3:待用户确认货物是否送达, 4:待评价, 5:完成已评价)
    public void setStatus(String status) {
        this.status = status;
    }

    //
    public String getSheng() {
        return sheng;
    }

    //
    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    //获取发货人详细地址
    public String getSend_street() {
        return send_street;
    }

    //设置发货人详细地址
    public void setSend_street(String send_street) {
        this.send_street = send_street;
    }

    //获取发货人地址
    public String getSend_pos() {
        return send_pos;
    }

    //设置发货人地址
    public void setSend_pos(String send_pos) {
        this.send_pos = send_pos;
    }

    //获取预发货时间
    public String getSend_time() {
        return send_time;
    }

    //设置预发货时间
    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    //获取订单类型
    public String getType() {
        return type;
    }

    //设置订单类型
    public void setType(String type) {
        this.type = type;
    }

    //获取订单评论
    public String getComment() {
        return comment;
    }

    //设置评论
    public void setComment(String comment) {
        this.comment = comment;
    }

    //获取回复评论(客服回复)
    public String getReply() {
        return reply;
    }

    //设置回复评论(客服回复)
    public void setReply(String reply) {
        this.reply = reply;
    }

    //获取收货人名字
    public String getBoss_name() {
        return boss_name;
    }

    //设置收货人名字
    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    //获取收货人电话
    public String getBoss_tel() {
        return boss_tel;
    }

    //设置收货人电话
    public void setBoss_tel(String boss_tel) {
        this.boss_tel = boss_tel;
    }

    //获取收货人地址
    public String getBoss_pos() {
        return boss_pos;
    }

    //设置收货人地址
    public void setBoss_pos(String boss_pos) {
        this.boss_pos = boss_pos;
    }

    //获取收货人详细地址
    public String getBoss_street() {
        return boss_street;
    }

    //设置收货人详细地址
    public void setBoss_street(String boss_street) {
        this.boss_street = boss_street;
    }

    //获取预收货时间
    public String getBoss_time() {
        return boss_time;
    }

    //设置预收货时间
    public void setBoss_time(String boss_time) {
        this.boss_time = boss_time;
    }

    //获取支付编号
    public String getPay_num() {
        return pay_num;
    }

    //设置支付编号
    public void setPay_num(String pay_num) {
        this.pay_num = pay_num;
    }

    //获取货物图片1
    public String getPic1() {
        return pic1;
    }

    //设置货物图片1
    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    //获取货物图片2
    public String getPic2() {
        return pic2;
    }

    //设置货物图片2
    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    //货哦去货物图片3
    public String getPic3() {
        return pic3;
    }

    //设置货物图片3
    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    //获取订单备注(注意事项)
    public String getTips() {
        return tips;
    }

    //设置订单备注(注意事项)
    public void setTips(String tips) {
        this.tips = tips;
    }

    //获取发货人经度
    public String getSend_jd() {
        return send_jd;
    }

    //设置发货人经度
    public void setSend_jd(String send_jd) {
        this.send_jd = send_jd;
    }

    //获取发货人纬度
    public String getSend_wd() {
        return send_wd;
    }

    //设置发货人纬度
    public void setSend_wd(String send_wd) {
        this.send_wd = send_wd;
    }

    //获取保险费
    public String getProtect_money() {
        return protect_money;
    }

    //设置保险费
    public void setProtect_money(String protect_money) {
        this.protect_money = protect_money;
    }

    //获取收货人经度
    public String getBoss_jd() {
        return boss_jd;
    }

    //设置收货人经度
    public void setBoss_jd(String boss_jd) {
        this.boss_jd = boss_jd;
    }

    //获取收货人纬度
    public String getBoss_wd() {
        return boss_wd;
    }

    //设置收货人纬度
    public void setBoss_wd(String boss_wd) {
        this.boss_wd = boss_wd;
    }

    //获取是否取消订单(0默认, 1取消)
    public String getIsexit() {
        return isexit;
    }

    //设置是否取消订单(0默认, 1取消)
    public void setIsexit(String isexit) {
        this.isexit = isexit;
    }

    //获取是否处于协商状态(0默认, 1协商)
    public String getIsxs() {
        return isxs;
    }//设置是否处于协商状态(0默认, 1协商)

    public void setIsxs(String isxs) {
        this.isxs = isxs;
    }


}
