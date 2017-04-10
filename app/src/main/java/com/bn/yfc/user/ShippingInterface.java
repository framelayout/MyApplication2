package com.bn.yfc.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.base.OrderBaseBean;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ShippingInterface extends BaseActivity implements OnClickListener {
    private TextView tips, number, name, type, sendPhone, sendName, sendAddress, bossAddress, times, starus;
    private ImageView left;
    private TextView title;
    private String numbers;
    private OrderBaseBean bean;
    private Button leftBut, rightBut;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shippinginterface);
        iniHead();
        initView();
        initData();
    }

    private void initData() {
        bean = new OrderBaseBean();
        if (getIntent() != null) {
            numbers = getIntent().getStringExtra("number");
            Tools.setLog("订单编号为" + getIntent().getStringExtra("number"));
            sendData(numbers);
        }
    }

    private void sendData(String i) {
        AjaxParams params = new AjaxParams();
        params.put("id", i);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTSHOWORDERS, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("订单详情请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject ob = object.getJSONObject("data");
                            bean.setOrdernum(ob.getString("ordernum"));
                            bean.setType(ob.getString("type"));
                            bean.setArea(ob.getString("area"));
                            bean.setBoss_jd(ob.getString("boss_jd"));
                            bean.setBoss_name(ob.getString("boss_name"));
                            Tools.setLog(bean.getBoss_name());
                            bean.setBoss_pos(ob.getString("boss_pos"));
                            bean.setBoss_street(ob.getString("boss_street"));
                            bean.setBoss_tel(ob.getString("boss_tel"));
                            bean.setBoss_time(ob.getString("boss_time"));
                            bean.setBoss_wd(ob.getString("boss_wd"));
                            bean.setCartype(ob.getString("cartype"));
                            bean.setComment(ob.getString("comment"));
                            bean.setD_tel(ob.getString("d_tel"));
                            bean.setDid(ob.getString("did"));
                            bean.setDis(ob.getString("dis"));
                            bean.setId(ob.getString("id"));
                            bean.setGoods_logo(ob.getString("goods_logo"));
                            bean.setGoods_num(ob.getString("goods_num"));
                            bean.setIsexit(ob.getString("isexit"));
                            bean.setMoney(ob.getString("money"));
                            bean.setName(ob.getString("name"));
                            bean.setPay_num(ob.getString("pay_num"));
                            bean.setPic1(ob.getString("pic1"));
                            bean.setPic2(ob.getString("pic2"));
                            bean.setPic3(ob.getString("pic3"));
                            bean.setProtect_money(ob.getString("protect_money"));
                            bean.setReply(ob.getString("reply"));
                            bean.setSend_jd(ob.getString("send_jd"));
                            bean.setSend_wd(ob.getString("send_wd"));
                            bean.setSend_money(ob.getString("send_money"));
                            bean.setSend_tel(ob.getString("send_tel"));
                            bean.setSend_name(ob.getString("send_name"));
                            bean.setSend_pos(ob.getString("send_pos"));
                            bean.setSend_street(ob.getString("send_street"));
                            Tools.setLog("showtime" + ob.getString("send_time"));
                            bean.setSend_time(ob.getString("send_time"));
                            bean.setSheng(ob.getString("sheng"));
                            bean.setShi(ob.getString("shi"));
                            bean.setStatus(ob.getString("status"));
                            bean.setTime(ob.getString("time"));
                            bean.setTips(ob.getString("tips"));
                            bean.setUid(ob.getString("uid"));
                            bean.setIsxs(ob.getString("isxs"));
                            setViewDatas();
                            break;
                        case 110:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("订单详情请求错误" + strMsg);
            }
        });
    }

    private void setViewDatas() {
        name.setText("货物: " + bean.getName());
        sendName.setText("联系人: " + bean.getSend_name());
        sendAddress.setText(bean.getSend_pos() + "\n详细地址: " + bean.getSend_street());
        bossAddress.setText(bean.getBoss_pos() + "\n详细地址: " + bean.getBoss_street());
        tips.setText("订单详情: " + bean.getTips());
        sendPhone.setText("联系方式: " + bean.getSend_tel());
        long i = Long.parseLong(bean.getTime());
        times.setText("发布日期: " + Tools.getDate(i));
        number.setText("订单编号: " + bean.getOrdernum());
        switch (bean.getType()) {
            case "4":
                type.setText("船运订单");
                title.setText("船运订单");
                break;
        }
        switch (bean.getStatus()) {
            case "0":
                starus.setText("未接单 ");
                break;
            case "1":
                starus.setText("已接单");
                break;
        }
        switch (bean.getStatus()) {
            case "0":
                leftBut.setVisibility(View.INVISIBLE);
                rightBut.setVisibility(View.VISIBLE);
                rightBut.setText("接取订单");
                break;
            case "1":
                leftBut.setVisibility(View.VISIBLE);
                leftBut.setText("取消订单");
                rightBut.setVisibility(View.VISIBLE);
                rightBut.setText("联系客户");
                break;
        }
    }

    private void initView() {
        name = (TextView) findViewById(R.id.shipping_name);
        sendName = (TextView) findViewById(R.id.shipping_sendname);
        sendPhone = (TextView) findViewById(R.id.shipping_sendphone);
        sendAddress = (TextView) findViewById(R.id.shipping_sendaddress);
        bossAddress = (TextView) findViewById(R.id.shipping_bossaddress);
        tips = (TextView) findViewById(R.id.shipping_tips);
        times = (TextView) findViewById(R.id.shipping_time);
        type = (TextView) findViewById(R.id.shipping_type);
        number = (TextView) findViewById(R.id.shipping_number);
        starus = (TextView) findViewById(R.id.shipping_status);
        leftBut = (Button) findViewById(R.id.shipping_leftbut);
        leftBut.setOnClickListener(this);
        rightBut = (Button) findViewById(R.id.shipping_rightbut);
        rightBut.setOnClickListener(this);
    }


    private void iniHead() {

        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.shipping_leftbut:
                ifsLeftStatus();
                break;
            case R.id.shipping_rightbut:
                ifsRightStatus();
                break;
        }
    }

    private void ifsLeftStatus() {
        switch (bean.getStatus()) {
            case "0":
                break;
            case "1":
                //取消

                break;
        }


    }

    private void ifsRightStatus() {
        switch (bean.getStatus()) {
            case "0":
                //接单
                startSend();
                break;
            case "1":
                //联系人
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getSend_tel()));
                startActivity(intent);
                break;
        }
    }


    //接单请求
    private void startSend() {
        AjaxParams params = new AjaxParams();
        String url = "";
        params.put("id", bean.getId());
        params.put("did", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("d_tel", (String) SharedPreferencesUtils.getParam(getApplication(), "name", ""));
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(url, params, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("接单请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            intent = new Intent(ShippingInterface.this, UserAudit.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 110:
                            Tools.showToast(getApplication(), object.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("接单请求报错===" + strMsg);
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }
}
