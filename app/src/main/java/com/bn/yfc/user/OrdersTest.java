package com.bn.yfc.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.base.OrderBaseBean;
import com.bn.yfc.basedialog.OrderPayDialog;
import com.bn.yfc.basedialog.PayDialog;
import com.bn.yfc.myapp.Arith;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.AliPayTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.Random;

/**
 * Created by Administrator on 2017/1/14.
 */

public class OrdersTest extends BaseActivity implements OnClickListener {

    private TextView name, status, money, prix, phone, cartype, showtime, number, sendaddress, sendshowtime, sendname, sendphone, bossname, bossaddress, bossphone, bossshowtime, tips;
    private TextView title;
    private ImageView left;
    private Button copy, sendplay, bossplay, leftbut, rightbut;
    private OrderBaseBean bean = new OrderBaseBean();
    private LinearLayout send, boss, start_lin;
    private Intent intent;
    private String numbers;
    private ViewGroup.LayoutParams para;
    private ImageView icon1, icon2, icon3;
    private String urlHead = "";
    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("closealipay")) {
                Tools.setLog("注销广播接收成功");
                RefrshData();
            }
        }
    };

    private void initReceiver() {
        IntentFilter filter = new IntentFilter("closealipay");
        registerReceiver(recevier, filter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemtest);
        initHead();
        initView();
        //setvIewStats();
        start_lin.setVisibility(View.VISIBLE);
        initData();
        initReceiver();
    }

    private void initView() {
        icon1 = (ImageView) findViewById(R.id.orderstest_icon1);
        icon1 = setImageViewWidth(icon1);
        icon1.setVisibility(View.GONE);
        icon1.setOnClickListener(this);
        icon2 = (ImageView) findViewById(R.id.orderstest_icon2);
        icon2 = setImageViewWidth(icon2);
        icon2.setVisibility(View.GONE);
        icon2.setOnClickListener(this);
        icon3 = (ImageView) findViewById(R.id.orderstest_icon3);
        icon3 = setImageViewWidth(icon3);
        icon3.setOnClickListener(this);
        icon3.setVisibility(View.GONE);
        boss = (LinearLayout) findViewById(R.id.order_boss);
        send = (LinearLayout) findViewById(R.id.order_send);
        name = (TextView) findViewById(R.id.order_name);
        status = (TextView) findViewById(R.id.order_status);
        money = (TextView) findViewById(R.id.order_money);
        prix = (TextView) findViewById(R.id.order_prix);
        phone = (TextView) findViewById(R.id.order_phone);
        cartype = (TextView) findViewById(R.id.order_cartype);
        showtime = (TextView) findViewById(R.id.order_showtime);
        number = (TextView) findViewById(R.id.order_number);
        sendname = (TextView) findViewById(R.id.order_sendname);
        sendaddress = (TextView) findViewById(R.id.order_sendaddres);
        sendphone = (TextView) findViewById(R.id.order_sendphone);
        sendshowtime = (TextView) findViewById(R.id.order_sendshowtime);
        bossname = (TextView) findViewById(R.id.order_bossname);
        bossaddress = (TextView) findViewById(R.id.order_bossaddress);
        bossphone = (TextView) findViewById(R.id.order_bossphone);
        bossshowtime = (TextView) findViewById(R.id.order_bossshowtime);
        tips = (TextView) findViewById(R.id.order_tips);
        copy = (Button) findViewById(R.id.order_copy);
        copy.setOnClickListener(this);
        sendplay = (Button) findViewById(R.id.order_sendphoneplay);
        sendplay.setOnClickListener(this);
        bossplay = (Button) findViewById(R.id.order_bossphoneplay);
        bossplay.setOnClickListener(this);
        leftbut = (Button) findViewById(R.id.order_leftbut);
        leftbut.setOnClickListener(this);
        rightbut = (Button) findViewById(R.id.order_rightbut);
        rightbut.setOnClickListener(this);
        start_lin = (LinearLayout) findViewById(R.id.start_lin);
    }

    private void initData() {
        if (getIntent() != null) {
            numbers = getIntent().getStringExtra("number");
            Tools.setLog("订单编号为" + getIntent().getStringExtra("number"));
            urlHead = getIntent().getStringExtra("url");
            postData(numbers);
        }
    }

    private void RefrshData() {
        postData(numbers);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);

    }

    private void setvIewStats() {
        Random rand = new Random();
        name.setText(bean.getName());
        switch (rand.nextInt(3)) {
            case 0:
                status.setText("快递");
                title.setText("快递");
                sendshowtime.setVisibility(View.GONE);
                bossshowtime.setVisibility(View.GONE);
                money.setText("任务金额: " + rand.nextInt(1000000) + "元");
                break;
            case 1:
                status.setText("顺路送");
                title.setText("顺路送");
                money.setText("任务金额: " + rand.nextInt(1000000) + "元");
                break;
            case 2:
                status.setText("帮我买");
                title.setText("帮我买");
                sendshowtime.setVisibility(View.GONE);
                bossshowtime.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                money.setText("商品品牌: " + "大前门");
                break;
        }


        prix.setText("商品价格" + rand.nextInt(1000000) + "元");
        phone.setText("15340554611");
        switch (rand.nextInt(10)) {
            case 0:
                cartype.setText("摩托车配送");
                break;
            case 1:
                cartype.setText("轿车配送");
                break;
            case 2:
                cartype.setText("小面包车配送");
                break;
            case 3:
                cartype.setText("中面包车配送");
                break;
            case 4:
                cartype.setText("小货车配送");
                break;
            case 5:
                cartype.setText("中货车配送");
                break;
            case 6:
                cartype.setText("大货车配送");
                break;
            case 7:
                cartype.setText("商家配送");
                break;
            case 8:
                cartype.setText("船运配送");
                break;
            case 9:
                cartype.setText("顺路送配送");
                break;

        }
        showtime.setText("订单发布日期: " + Tools.getTimerDay());
        number.setText("订单编号");
        sendname.setText("获取名称");
        String adderss = (String) SharedPreferencesUtils.getParam(getApplication(), "sheng", "") + SharedPreferencesUtils.getParam(getApplication(), "city", " ") + SharedPreferencesUtils.getParam(getApplication(), "qu", " ");
        sendaddress.setText(adderss);
        sendphone.setText("发货人电话" + "15340554611");
        sendshowtime.setText(Tools.getTimerDay());
        bossname.setText("收货人姓名");
        bossaddress.setText("收货地址: " + adderss);
        bossphone.setText("收货人电话: " + "15340554611");
        bossshowtime.setText(Tools.getTimerDay());
        tips.setText("订单备注: " + "这是一条备注信息");
    }

    private void setViewDatas() {

        name.setText(bean.getName());
        prix.setText("商品价格" + bean.getMoney() + "元");
        phone.setText("15340554610");
        switch (bean.getCartype()) {
            case "1":
                cartype.setText("摩托车派送");
                break;
            case "2":
                cartype.setText("轿车派送");
                break;
            case "3":
                cartype.setText("小面包车派送");
                break;
            case "4":
                cartype.setText("中面包车派送");
                break;
            case "5":
                cartype.setText("小货车派送");
                break;
            case "6":
                cartype.setText("中货车派送");
                break;
            case "7":
                cartype.setText("大货车派送");
                break;
            case "8":
                cartype.setText("商家派送");
                break;
            case "9":
                cartype.setText("船运派送");
                break;
            case "10":
                cartype.setText("顺路送派送");
                break;

        }
        long i = Long.parseLong(bean.getTime());
        showtime.setText("发布日期: " + Tools.getDate(i));
        number.setText("订单编号: " + bean.getOrdernum());
        sendname.setText("发货人: " + bean.getSend_name());
        sendaddress.setText(bean.getSend_pos() + "-" + bean.getSend_street());
        sendphone.setText("发货人电话: " + bean.getSend_tel());
        sendshowtime.setText("预发货时间: " + bean.getSend_time());
        Tools.setLog(bean.getBoss_name());
        bossname.setText("收货人: " + bean.getBoss_name());
        bossaddress.setText(bean.getBoss_pos() + "-" + bean.getBoss_street());
        bossphone.setText("收货人电话: " + bean.getBoss_tel());
        bossshowtime.setText("预收货时间:" + bean.getBoss_time());
        tips.setText("订单备注: " + bean.getTips());
        start_lin.setVisibility(View.GONE);
        if (bean.getPic1() != null) {
            icon1.setVisibility(View.VISIBLE);
            //设置图片
            x.image().bind(icon1, bean.getPic1());
        }
        if (bean.getPic2() != null) {
            icon2.setVisibility(View.VISIBLE);
            //设置图片.
            x.image().bind(icon2, bean.getPic2());
        }
        if (bean.getPic3() != null) {
            icon3.setVisibility(View.VISIBLE);
            x.image().bind(icon3, bean.getPic3());
            //设置图片
        }
        if (bean.getIsxs().equals("1")) {
            leftbut.setVisibility(View.INVISIBLE);
            rightbut.setText("协商中");
            return;
        }

        switch (bean.getType()) {
            case "1":
                status.setText("快递");
                title.setText("快递");
                sendshowtime.setVisibility(View.GONE);
                bossshowtime.setVisibility(View.GONE);
                money.setText("任务金额: " + bean.getSend_money() + "元");
                switch (bean.getStatus()) {
                    case "0":
                        leftbut.setText("");
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("取消订单");
                        break;
                    case "1":
                        leftbut.setText("取消订单");
                        rightbut.setText("开始派送");
                        break;
                    case "2":
                        leftbut.setText("协商订单");
                        rightbut.setText("查询快递");
                        break;
                    case "3":
                        leftbut.setText("协商订单");
                        rightbut.setText("确认收货");
                        break;
                    case "4":
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("发表评论");
                        rightbut.setFocusable(false);
                        break;
                    case "5":
                        leftbut.setVisibility(View.INVISIBLE);
                        leftbut.setText("");
                        rightbut.setText("完成订单");
                        rightbut.setFocusable(false);
                        break;
                }
                break;
            case "2":
                status.setText("顺路送");
                title.setText("顺路送");
                money.setText("任务金额: " + bean.getSend_money() + "元");
                switch (bean.getStatus()) {
                    case "0":
                        leftbut.setText("");
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("取消订单");
                        break;
                    case "1":
                        leftbut.setText("取消订单");
                        rightbut.setText("开始派送");
                        break;
                    case "2":
                        leftbut.setText("协商订单");
                        rightbut.setText("查询快递");
                        break;
                    case "3":
                        leftbut.setText("协商订单");
                        rightbut.setText("确认收货");
                        break;
                    case "4":
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("发表评论");
                        rightbut.setFocusable(false);
                        break;
                    case "5":
                        leftbut.setVisibility(View.INVISIBLE);
                        leftbut.setText("");
                        rightbut.setText("完成订单");
                        rightbut.setFocusable(false);
                        break;

                }
                break;
            case "3":
                status.setText("帮我买");
                title.setText("帮我买");
                sendshowtime.setVisibility(View.GONE);
                bossshowtime.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                money.setText("商品品牌: " + bean.getGoods_logo());
                switch (bean.getStatus()) {
                    case "0":
                        leftbut.setText("");
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("取消订单");
                        break;
                    case "1":
                        leftbut.setText("取消订单");
                        rightbut.setText("开始派送");
                        break;
                    case "2":
                        leftbut.setText("协商订单");
                        rightbut.setText("查询快递");
                        break;
                    case "3":
                        leftbut.setText("协商订单");
                        //提交支付
                        rightbut.setText("确认收货");
                        break;
                    case "4":
                        leftbut.setVisibility(View.INVISIBLE);
                        rightbut.setText("发表评论");
                        rightbut.setFocusable(false);
                        break;
                    case "5":
                        leftbut.setVisibility(View.INVISIBLE);
                        leftbut.setText("");
                        rightbut.setText("完成订单");
                        rightbut.setFocusable(false);
                        break;
                }
                break;
            case "4":
                status.setText("船运");
                title.setText("船运");
                sendshowtime.setVisibility(View.GONE);
                bossshowtime.setVisibility(View.GONE);
                showtime.setVisibility(View.GONE);
                bossname.setVisibility(View.GONE);
                bossphone.setVisibility(View.GONE);
                bossplay.setVisibility(View.GONE);
                switch (bean.getStatus()) {
                    case "0":
                        leftbut.setVisibility(View.INVISIBLE);
                        leftbut.setText("");
                        rightbut.setText("取消");
                        break;
                    case "1":
                        leftbut.setVisibility(View.VISIBLE);
                        leftbut.setText("取消派送");
                        rightbut.setText("联系派送");
                        break;
                    case "2":

                        break;

                }
                break;
        }

    }

    private void postData(String i) {
        AjaxParams params = new AjaxParams();
        params.put("id", i);
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTSHOWORDERS, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("订单详情请求成功\n" + s);
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
                            if (!ob.getString("pic1").equals("null")) {
                                bean.setPic1(urlHead + ob.getString("pic1"));
                            }
                            if (!ob.getString("pic2").equals("null")) {
                                bean.setPic2(urlHead + ob.getString("pic2"));
                            }
                            if (!ob.getString("pic3").equals("null")) {
                                bean.setPic3(urlHead + ob.getString("pic3"));
                            }
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
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.orderstest_icon1:
                intent = new Intent(this, PhotoLook.class);
                intent.putExtra("path", bean.getPic1());
                startActivity(intent);
                break;
            case R.id.orderstest_icon2:
                intent = new Intent(this, PhotoLook.class);
                intent.putExtra("path", bean.getPic2());
                startActivity(intent);
                break;
            case R.id.orderstest_icon3:
                intent = new Intent(this, PhotoLook.class);
                intent.putExtra("path", bean.getPic3());
                startActivity(intent);
                break;
            case R.id.headac_left:
                finish();
                break;
            case R.id.order_leftbut:
                ifsOrderLeftStatus();
                break;
            case R.id.order_rightbut:
                ifsOrderRightStatus();
                break;
            case R.id.order_copy:
                break;
            case R.id.order_sendphoneplay:
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getSend_tel()));
                startActivity(intent);
                break;
            case R.id.order_bossphoneplay:
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getBoss_tel()));
                startActivity(intent);
                break;
        }
    }

    //左按钮判定
    private void ifsOrderLeftStatus() {
        switch (bean.getType()) {
            case "1":
                switch (bean.getStatus()) {
                    case "0":
                        break;
                    case "1":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "2":
                        //协商订单
                        bexsOrder();
                        break;
                    case "3":
                        //协商订单
                        bexsOrder();
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                }
                break;
            case "2":
                switch (bean.getStatus()) {
                    case "0":
                        break;
                    case "1":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "2":
                        //协商订单
                        bexsOrder();
                        break;
                    case "3":
                        //协商订单
                        bexsOrder();
                        break;
                    case "4":
                        break;
                    case "5":
                        break;
                }
                break;
            case "3":
                switch (bean.getStatus()) {
                    case "0":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "1":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "2":
                        //协商订单
                        bexsOrder();
                        break;
                    case "3":
                        //协商订单
                        bexsOrder();
                        break;
                    case "4":

                        break;
                    case "5":
                        break;
                }
                break;
            case "4":
                switch (bean.getStatus()) {
                    case "0":
                        break;
                    case "1":
                        deldeteOrder();
                        break;
                }
                break;
        }

    }

    //右按钮状态判定
    private void ifsOrderRightStatus() {
        switch (bean.getType()) {
            case "1":
                switch (bean.getStatus()) {
                    case "0":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "1":
                      /*  //联系客服
                        Tools.setLog("发货人电话为: " + bean.getSend_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getSend_tel()));
                        startActivity(intent);*/
                        startGoSend();
                        break;
                    case "2":
                        Tools.setLog("快递员电话为：" + bean.getD_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getD_tel()));
                        startActivity(intent);
                        break;
                    case "3":
                        //提醒收货
                        // hintOrder();
                        //确认送达
                        reachSend();
                        break;
                    case "4":
                        //评价
                        comment();
                        break;
                    case "5":

                        break;
                }
                break;
            case "2":
                switch (bean.getStatus()) {
                    case "0":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "1":
                      /*  //联系客服
                        Tools.setLog("发货人电话为: " + bean.getSend_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getSend_tel()));
                        startActivity(intent);*/
                        startGoSend();
                        break;
                    case "2":
                        Tools.setLog("快递员电话为：" + bean.getD_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getD_tel()));
                        startActivity(intent);
                        break;
                    case "3":
                        //提醒收货
                        // hintOrder();
                        //确认送达
                        reachSend();
                        break;
                    case "4":
                        //评价
                        comment();
                        break;
                    case "5":

                        break;
                }
                break;
            case "3":
                switch (bean.getStatus()) {
                    case "0":
                        //取消订单
                        deldeteOrder();
                        break;
                    case "1":
                      /*  //联系客服
                        Tools.setLog("发货人电话为: " + bean.getSend_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getSend_tel()));
                        startActivity(intent);*/
                        appPay();
                        //开始派送，进行支付
                        break;
                    case "2":
                        Tools.setLog("快递员电话为：" + bean.getD_tel());
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getD_tel()));
                        startActivity(intent);
                        break;
                    case "3":
                        //提醒收货
                        // hintOrder();
                        //确认送达
                        reachSend();
                        //调起支付
                        //  PayDialog dialog = new PayDialog();
                        break;
                    case "4":
                        //评价
                        comment();
                        break;
                    case "5":

                        break;
                }
                break;
            case "4":
                switch (bean.getStatus()) {
                    case "0":
                        deldeteOrder();
                        break;
                    case "1":
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getD_tel()));
                        startActivity(intent);
                        break;
                }
                break;
        }

    }

    private void comment() {
        intent = new Intent();
        intent.setClass(this, Comment.class);
        intent.putExtra("number", bean.getId());
        startActivity(intent);
    }

    //确认送达
    private void reachSend() {
        AjaxParams params = new AjaxParams();
        String url = MyConfig.POSTUSERSENDURL;
        params.put("id", bean.getId());
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("订单送达请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            RefrshData();
                            Tools.setLog("确认成功");
                            //Tools.showToast(getApplication(), "成功");
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
                Tools.setLog("订单送达报错===" + strMsg);
            }
        });
    }

    //取消订单
    private void deldeteOrder() {
        AjaxParams params = new AjaxParams();
        String url = MyConfig.POSTDELETEORDERURL;
        Tools.setLog("取消订单时单号为:" + bean.getId());
        params.put("id", bean.getId());
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("取消订单请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
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
                Tools.setLog("取消订单请求报错===" + strMsg);
            }
        });
    }

    //协商订单
    public void bexsOrder() {
        AjaxParams params = new AjaxParams();
        String url = MyConfig.POSTBEXSURL;
        params.put("id", bean.getId());
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("协商订单请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            RefrshData();
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
                Tools.setLog("协商订单请求报错===" + strMsg);
            }
        });
    }

    //提醒收货
    public void hintOrder() {
        AjaxParams params = new AjaxParams();
        String url = "";
        params.put("id", bean.getId());
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("协商订单请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            RefrshData();
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
                Tools.setLog("协商订单请求报错===" + strMsg);
            }
        });
    }

    //开始派送
    private void startGoSend() {
        AjaxParams params = new AjaxParams();
        String url = MyConfig.POGOSENDURL;
        params.put("id", bean.getId());
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("开始派送订单请求成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            RefrshData();
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
                Tools.setLog("协商订单请求报错===" + strMsg);
            }
        });

    }

    private void appPay() {
        SharedPreferencesUtils.setParam(getApplication(), "ispay", "3");
        //弹起支付对话框
        OrderPayDialog orpaydiao = new OrderPayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("order_id", bean.getId());
        orpaydiao.setArguments(bundle);
        orpaydiao.show(getSupportFragmentManager(), "ordarpay");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.setLog("个人列表 onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Tools.setLog("个人列表 onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tools.setLog("个人列表刷新数据");
        RefrshData();
    }


    private ImageView setImageViewWidth(ImageView iv) {
        para = iv.getLayoutParams();
        para.width = getMultiple(Tools.getWidht(this));
        para.height = para.width;
        iv.setLayoutParams(para);
        return iv;
    }

    private int getMultiple(int widht) {
        double aa = widht;
        double bb = 3.6;
        double cc = 0;
        try {
            cc = Arith.div(aa, bb, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) cc;
    }


}

