package com.bn.yfc.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.basedialog.PayDialog;
import com.bn.yfc.buy.SelectAddress;
import com.bn.yfc.fragmentmain.ProtocolWeb;
import com.bn.yfc.myapp.Arith;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.WXShare;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ReleaseDelivery extends BaseActivity implements OnClickListener {
    private double Rlatduble;
    private float dics = 0;
    private double Rlongduble;
    private double Slatduble;
    private double Slongduble;
    private Intent intent;
    //人随货走 0为未勾选，1为勾选
    private ImageView left;
    private TextView title;
    private String type;
    private boolean ifsInsurance = true;
    private int RUTEUNCODE = 13;
    private int RUTEUNADDRESSCODE = 12;
    private LinearLayout insurance, carlin, timelin;
    private RelativeLayout selectAddress;
    private EditText getName, getPhone, getDetailed, getShudownTimer;
    private ReleaseBean bean = new ReleaseBean();
    private TextView getPrix, getDistance, address;
    private Button but;
    private TextView carType, protect;
    private String cartt, prixs;
    private int openOne = 0;
    private boolean ifsPerson = true;
    private ImageView insuranceIcon, person_icon;
    private String protects, payMoney;
    private TimeSelector timerselector;
    private RelativeLayout person;
    private int textIndex = 0;
    //判断是否为第一次进入页面
    private int ifsPayCode = 0;
    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("closealipay")) {
                Tools.setLog("注销广播接收成功");
                //finish();
                finishData();
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
        setContentView(R.layout.releasedelivery2);
        initTimeSelector();
        initData();
        initHead();
        initView();
        setView();
        initReceiver();
    }

    private void initTimeSelector() {
        timerselector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                getShudownTimer.setText(time);
            }
        }, Tools.getCurrentTimes(), "2099-12-30 00:00");
        timerselector.setMode(TimeSelector.MODE.YMDHM);
    }

    private void setView() {
        carType.setText(cartt);
    }

    private void initView() {
        person = (RelativeLayout) findViewById(R.id.releasedelivery1_person);
        person.setOnClickListener(this);
        person_icon = (ImageView) findViewById(R.id.releasedelivery1_person_icon);
        person_icon.setOnClickListener(this);
        getShudownTimer = (EditText) findViewById(R.id.releasedelivery_shutdowntime);
        carlin = (LinearLayout) findViewById(R.id.releasedelivery_carlin);
        timelin = (LinearLayout) findViewById(R.id.releasedelivery_timelin);
        getName = (EditText) findViewById(R.id.releasedelivery_name);
        getPhone = (EditText) findViewById(R.id.releasedelivery_phone);
        getDetailed = (EditText) findViewById(R.id.releasedelivery_detailed);
        address = (TextView) findViewById(R.id.releasedelivery_address);
        getPrix = (TextView) findViewById(R.id.releasedelivery_prix);
        getDistance = (TextView) findViewById(R.id.releasedelivery_distance);
        carType = (TextView) findViewById(R.id.releasedelivery_cartype);
        carType.setOnClickListener(this);
        selectAddress = (RelativeLayout) findViewById(R.id.releasedelivery_selecaddress);
        selectAddress.setOnClickListener(this);
        insurance = (LinearLayout) findViewById(R.id.releasedelivery_insurance);
        insurance.setOnClickListener(this);
        insuranceIcon = (ImageView) findViewById(R.id.releasedelivery_insurance_icon);
        insuranceIcon.setOnClickListener(this);
        but = (Button) findViewById(R.id.releasedelivery_but);
        but.setOnClickListener(this);
        address = (TextView) findViewById(R.id.releasedelivery_address);
        protect = (TextView) findViewById(R.id.releasedelivery_protect);
        getShudownTimer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    timerselector.show();
                } else {
                    //失去焦点
                }
            }
        });
        getShudownTimer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (textIndex == start) {

                } else {
                    timerselector.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!type.equals("3")) {
            timelin.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            bean = (ReleaseBean) getIntent().getSerializableExtra("data");
            cartt = getIntent().getStringExtra("cartype");
            Slatduble = getIntent().getDoubleExtra("latduble", 0);
            Slongduble = getIntent().getDoubleExtra("longduble", 0);
            Tools.setLog("商品价格" + getIntent().getStringExtra("prix"));
            prixs = getIntent().getStringExtra("prix");
            Tools.setLog("判断传递分发货地址经纬度：" + Slatduble + "," + Slongduble);
            if (SharedPreferencesUtils.getParam(ReleaseDelivery.this, "protect", "") != null) {
                //  protects = (String) SharedPreferencesUtils.getParam(ReleaseDelivery.this, "protect", "");
                protects = "0";
                Tools.setLog("获取成功~" + protect);
            } else {
                //判断，如果没有获取到收费规则，可以继续进行请求
                Tools.setLog("获取失败~" + SharedPreferencesUtils.getParam(ReleaseDelivery.this, "protect", ""));
            }
        }
    }


    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        if (type.equals("3")) {
            title.setText("发布顺路送");
        } else {
            title.setText("发布快递");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RUTEUNADDRESSCODE) {
            if (data != null) {
                address.setVisibility(View.VISIBLE);
                address.setText(data.getStringExtra("address"));
                Rlatduble = data.getDoubleExtra("latduble", 0);
                Rlongduble = data.getDoubleExtra("longduble", 0);
                Tools.setLog("收货地址获取经纬度为===\n" + Rlatduble + "," + Rlongduble);
                ComputeDitace();
            }

        }
    }

    public void ReceiveDic(float dic) {
        Tools.showToast(this, "计算距离为" + dic);
        DecimalFormat df = new DecimalFormat("0.00");
        dics = Float.valueOf(df.format(dic / 1000));
        //计算路费
        getDistance.setText("距离为 " + dics + " 公里");
        setRecevidicMoney();
    }

    //设置运费显示
    private void setRecevidicMoney() {
        float pri = 0;
        float frist = Float.valueOf((String) SharedPreferencesUtils.getParam(getApplication(), "frist", ""));
        Tools.setLog("frist=" + frist);
        int frist_out = Integer.valueOf((String) SharedPreferencesUtils.getParam(getApplication(), "frist_out", ""));
        Tools.setLog("frist_out= " + frist_out);
        int every = Integer.valueOf((String) SharedPreferencesUtils.getParam(getApplication(), "every", ""));
        Tools.setLog("every = " + every);
        if (dics > frist_out) {
            float x = dics - frist_out;
            pri += frist;
            pri += every * x;
            Tools.setLog("超出距离变化后计算金额" + pri + "元");
        } else {
            pri = frist;
            Tools.setLog("距离变化后计算金额" + pri + "元");
        }
        // compumt();
        payMoney = String.valueOf(pri);
        //计算保险费用
        CharSequence cse = String.valueOf(pri);
        getPrix.setText(Tools.selectStringTwo(cse) + "元");


    }

    //计算保险费用
    private float compumt() {
        float di = 0;
        float pi = Float.valueOf(bean.getPrix());
        Tools.setLog("商品价格" + pi);
        float lv = Float.valueOf((String) SharedPreferencesUtils.getParam(getApplication(), "protect", ""));
        Tools.setLog("保险费用率" + lv);
        di = pi * lv;
        if (ifsInsurance) {
            CharSequence cse = String.valueOf(di);
            protect.setText("保险费: " + Tools.selectStringTwo(cse) + "元");
        } else {
            protect.setText("无");
        }
        //保存两位小数
        protects = Tools.selectStringTwo(String.valueOf(di));
        return di;
    }

    //计算距离
    private void ComputeDitace() {
        DirveQuery dri = new DirveQuery();
        float aaa = 0;
        try {
            aaa = dri.ReturnDrive(this, new LatLonPoint(Rlatduble, Rlongduble), new LatLonPoint(Slatduble, Slongduble), this);
        } catch (AMapException e) {
            e.printStackTrace();
            Tools.setLog("出现异常" + e.getErrorCode() + "|" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.releasedelivery_selecaddress:
                intent = new Intent(this, SelectAddress.class);
                startActivityForResult(intent, RUTEUNADDRESSCODE);
                break;
            case R.id.releasedelivery_insurance:
                sendProtocol();
                break;
            case R.id.releasedelivery_insurance_icon:
                if (ifsInsurance) {
                    ifsInsurance = false;
                    insuranceIcon.setBackgroundResource(R.drawable.check_normal);
                } else {
                    ifsInsurance = true;
                    insuranceIcon.setBackgroundResource(R.drawable.check_onclick);
                }
                // setRecevidicMoney();
                break;
            case R.id.releasedelivery_but:
                ifsDataPost();
                break;
            case R.id.releasedelivery_cartype:

                break;
            case R.id.releasedelivery1_person:

                break;
            case R.id.releasedelivery1_person_icon:
                if (ifsPerson) {
                    ifsPerson = false;
                    person_icon.setBackgroundResource(R.drawable.check_normal);
                } else {
                    ifsPerson = true;
                    person_icon.setBackgroundResource(R.drawable.check_onclick);
                }

                break;
        }
    }

    private void sendProtocol() {
        intent = new Intent(this, ProtocolWeb.class);
        intent.putExtra("url", "http://www.baidu.com");
        startActivity(intent);
    }

    private void ifsDataPost() {
        String postType = null;
        String name = getName.getText().toString();
        String prix = getPrix.getText().toString();
        String phone = getPhone.getText().toString();
        String detailde = getDetailed.getText().toString();
        String addre = address.getText().toString();
        String distance = getDistance.getText().toString();
        String caryTypes = carType.getText().toString();
        String shutdown = getShudownTimer.getText().toString();

        if (type.equals("3")) {
            if (Tools.isEmpty(shutdown)) {
                Tools.showToast(this, "请输入预计送达时间");
                return;
            }
            bean.setCartype("3");
            bean.setReceiverShowTime(shutdown);
        } else {
            switch (caryTypes) {
                case "摩托车":
                    bean.setCartype("1");
                    break;
                case "三轮车":
                    bean.setCartype("2");
                    break;
                case "顺路送":
                    bean.setCartype("3");
                    break;
                case "轿车":
                    bean.setCartype("4");
                    break;
                case "小面包车":
                    bean.setCartype("5");
                    break;
                case "中面包车":
                    bean.setCartype("6");
                    break;
                case "小货车":
                    bean.setCartype("7");
                    break;
                case "中货车":
                    bean.setCartype("8");
                    break;
                case "大货车":
                    bean.setCartype("9");
                    break;
                case "船运":
                    bean.setCartype("10");
                    break;
            }
        }
        if (Tools.isEmpty(name)) {
            Tools.showToast(this, "请输入收货人姓名");
            return;
        }
        bean.setReceiverName(name);

        if (Tools.isEmpty(phone)) {
            Tools.showToast(this, "请输入收货人联系电话");
            return;
        }
        bean.setReceiverPhone(phone);

        if (Tools.isEmpty(detailde)) {
            Tools.showToast(this, "请输入详细收货地址");
            return;
        }
        bean.setReceiverAddress(detailde);
        if (Tools.isEmpty(addre)) {
            Tools.showToast(this, "请选择收货地址");
            return;
        }
        bean.setReceiverSelectAddress(addre);

        try {

            setDataPost(postType, prix, distance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Tools.setLog("捕获上传照片异常" + e.getMessage());
        }

    }


    private void setDataPost(String postType, String prix, String distance) throws FileNotFoundException {
        int ifpic = 0;
        Tools.setLog("开始发布订单");
        AjaxParams parames = new AjaxParams();
        Tools.setLog("开始发布订单1");
        if (bean.aritcleIonc1 != null) {
            String url1 = bean.getAritcleIonc1();
            if (openOne == 1) {
                String urlnew1 = Tools.phonePath();
                boolean files = Tools.copyFile(url1, urlnew1, true);
                if (files) {
                    Tools.setLog("转换成功,newFile:" + urlnew1 + "|判断新的照片是否存在" + new File(urlnew1).exists());
                    parames.put("pic3", new File(urlnew1));
                } else {
                    Tools.setLog("没有得到最新的照片:" + urlnew1 + "|判断以前的照片是否存在" + new File(url1).exists());
                }
            } else {
                parames.put("pic1", new File(bean.getAritcleIonc1()));
                ifpic = 1;
                Tools.setLog("上传照片1是否存在" + new File(bean.getAritcleIonc1()).exists() + "|" + bean.getAritcleIonc1());
            }
        }
        Tools.setLog("开始发布订单2");
        if (bean.aritcleIonc2 != null) {
            String url2 = bean.getAritcleIonc2();
            if (openOne == 1) {
                String urlnew2 = Tools.phonePath();
                boolean files = Tools.copyFile(url2, urlnew2, true);
                if (files) {
                    Tools.setLog("转换成功,newFile:" + urlnew2 + "|判断新的照片是否存在" + new File(urlnew2).exists());
                    parames.put("pic3", new File(urlnew2));
                } else {
                    Tools.setLog("没有得到最新的照片:" + urlnew2 + "|判断以前的照片是否存在" + new File(url2).exists());
                }
            } else {
                parames.put("pic2", new File(bean.getAritcleIonc2()));
                ifpic = 1;
                Tools.setLog("上传照片2是否存在" + new File(bean.getAritcleIonc2()).exists() + "|" + bean.getAritcleIonc1());
            }
        }
        Tools.setLog("开始发布订单3");
        if (bean.aritcleIonc3 != null) {
            String url3 = bean.getAritcleIonc3();
            if (openOne == 1) {
                String urlnew3 = Tools.phonePath();
                boolean files = Tools.copyFile(url3, urlnew3, true);
                if (files) {
                    Tools.setLog("转换成功,newFile:" + urlnew3 + "|判断新的照片是否存在" + new File(urlnew3).exists());
                    parames.put("pic3", new File(urlnew3));
                } else {
                    Tools.setLog("没有得到最新的照片:" + urlnew3 + "|判断以前的照片是否存在" + new File(url3).exists());
                }

            } else {
                parames.put("pic3", new File(bean.getAritcleIonc3()));
            }

            ifpic = 1;
            Tools.setLog("上传照片3是否存在" + new File(bean.getAritcleIonc3()).exists() + "|" + bean.getAritcleIonc1());
        }
        parames.put("ispic", String.valueOf(ifpic));
        parames.put("boss_jd", String.valueOf(Rlongduble));
        parames.put("boss_wd", String.valueOf(Rlatduble));
        parames.put("boss_pos", bean.getReceiverSelectAddress());
        parames.put("boss_street", bean.getReceiverAddress());
        parames.put("boss_name", bean.getReceiverName());
        parames.put("boss_tel", bean.getReceiverPhone());
        parames.put("boss_time", bean.getReceiverShowTime());
        parames.put("send_jd", String.valueOf(Slongduble));
        parames.put("send_wd", String.valueOf(Slatduble));
        parames.put("send_name", bean.getSenderName());
        parames.put("send_tel", bean.getSenderPhone());
        parames.put("send_time", bean.getSendeShowTime());
        parames.put("send_pos", bean.getSenderSelectAddress());
        parames.put("send_street", bean.getSenderAddress());
        if (type.equals("3")) {
            parames.put("type", "2");
            SharedPreferencesUtils.setParam(this, "ispay", "2");
        } else {
            SharedPreferencesUtils.setParam(this, "ispay", "1");
            parames.put("type", "1");
        }

        if (ifsPerson) {
            parames.put("tips", "人随货走," + bean.getRemarks());
        } else {
            parames.put("tips", bean.getRemarks());
        }
        parames.put("name", bean.getAritcle());

        parames.put("money", bean.getPrix());
        parames.put("send_money", payMoney);

        if (ifsInsurance) {
            parames.put("isprotect", "1");
        } else {
            Tools.showToast(getApplication(), "请勾选发布协议");
            return;
        }
        if (!type.equals("3")) {
            parames.put("cartype", bean.getCartype());
        } else {

        }
        parames.put("uid", (String) SharedPreferencesUtils.getParam(this, "id", " "));
        parames.put("protect_money", protects);
        parames.put("dis", String.valueOf(dics));
        parames.put("area", (String) SharedPreferencesUtils.getParam(getApplication(), "sheng", "") + SharedPreferencesUtils.getParam(getApplication(), "city", "") + SharedPreferencesUtils.getParam(getApplication(), "qu", ""));
        Tools.setLog("生成订单时商品价格:" + bean.getPrix() + "|保险费用:" + protects + "|配送费用: " + payMoney);
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        openOne = 1;
        fhp.post(MyConfig.POSTORDERADDURL, parames, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("发布订单请求失败:" + strMsg);
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("接受发布订单的返回值====" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(getApplication(), "发布成功");
                            JSONObject ob = object.getJSONObject("data");
                            String money = ob.getString("money");
                            String order = ob.getString("ordernum");
                            String app_id = ob.getString("appID");
                            String privateKey = ob.getString("privateKey");
                            String outUrl = ob.getString("ali_back");
                            Tools.setLog("");
                            payType(order, money, app_id, privateKey, outUrl);
                            break;
                        case 110:
                            Tools.showToast(getApplication(), "发布失败");
                            break;
                        case 111:
                            Tools.showToast(getApplication(), "图片上传失败，请稍后再试");
                            break;
                        case 10:
                            JSONObject ob1 = object.getJSONObject("data");
                            String money1 = ob1.getString("money");
                            String order1 = ob1.getString("ordernum");
                            String app_id1 = ob1.getString("appID");
                            String privateKey1 = ob1.getString("privateKey");
                            String outUrl1 = ob1.getString("ali_back");
                            payType(order1, money1, app_id1, privateKey1, outUrl1);
                            Tools.showToast(getApplication(), "发布成功");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Tools.setLog("开始发布订单29");
    }

    private void finishData() {
        setResult(13);
        finish();
    }

    public void cleanFinish() {
        finishData();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Tools.setLog("判断是否支付" + SharedPreferencesUtils.getParam(getApplication(), "ispay", "") + "");
        if (ifsPayCode == 0) {
            return;
        }
        if (((String) SharedPreferencesUtils.getParam(getApplication(), "ispay", "")).equals("13")) {
            ifPay();
        }
    }

    public void ifPay() {
        Tools.setLog("验证订单发布成功");
        Tools.setLog("订单发布成功");
        finishData();
    }

    public void payType(String order, String money, String app_id, String privateKey, String outurl) {
        ifsPayCode = 1;
        PayDialog dialog = new PayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "pay");
        bundle.putString("order", order);
        bundle.putString("money", money);
        bundle.putString("app_id", app_id);
        bundle.putString("privateKey", privateKey);
        bundle.putString("outurl", outurl);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "pay");
    }
}
