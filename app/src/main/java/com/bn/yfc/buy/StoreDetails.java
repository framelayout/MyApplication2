package com.bn.yfc.buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.Arith;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.userdata.OnlinePay;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/13.
 */

public class StoreDetails extends BaseActivity implements View.OnClickListener {
    //屏幕比例 60:29
    private ViewGroup.LayoutParams para;
    private ImageView icon, onlinepay;
    private RelativeLayout introduction;
    private TextView title, name, phone, shutdowntime, address, type;
    private ImageView left;
    private Intent intent;
    private String id;
    private Map<String, String> storeInfo = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storedetails);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            Tools.setLog("接收到的id" + id);
        }
        initView();
        initHead();
        initData();
    }

    private void initData() {
        AjaxParams parmas = new AjaxParams();
        parmas.put("id", id);
        showProgressDialog();
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTGETSTOREDATAURL, parmas, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {

                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("获取商家详细信息成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject urlObject = object.getJSONObject("url");
                    String headUrl = urlObject.getString("url");
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("开始解析");
                            JSONArray ary = object.getJSONArray("data");
                            JSONObject ob1 = ary.getJSONObject(0);

                            storeInfo.put("icon", headUrl + ob1.getString("pk_storelog"));
                            storeInfo.put("name", ob1.getString("pk_companyname"));
                            storeInfo.put("phone", ob1.getString("pk_sostel"));
                            storeInfo.put("userName", ob1.getString("pk_realname"));
                            storeInfo.put("icon1", headUrl + ob1.getString("pk_storeone"));
                            storeInfo.put("icon2", headUrl + ob1.getString("pk_storetwo"));
                            storeInfo.put("icon3", headUrl + ob1.getString("pk_storethree"));
                            storeInfo.put("icon4", headUrl + ob1.getString("pk_storefour"));
                            storeInfo.put("address", ob1.getString("pk_address"));
                            storeInfo.put("type", ob1.getString("pk_industry_type"));
                            storeInfo.put("starttime", ob1.getString("pk_start_time"));
                            storeInfo.put("endtime", ob1.getString("pk_end_time"));
                            storeInfo.put("remark", ob1.getString("pk_remark"));
                            setViews();
                            break;
                        case 110:
                            Tools.showToast(getApplication(), "获取商家详细信息失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                closeProgressDialog();
                Tools.setLog("获取商家详细信息失败" + strMsg);
            }
        });
    }

    private void initHead() {
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("商家");
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.storedetails_icon);
        icon = setImageViewWith(icon);
        //  icon.setBackgroundResource(R.drawable.kuai_user);
        introduction = (RelativeLayout) findViewById(R.id.storedetails_introduction);
        introduction.setOnClickListener(this);
        onlinepay = (ImageView) findViewById(R.id.storedetails_onlinepay);
        onlinepay.setOnClickListener(this);
        name = (TextView) findViewById(R.id.storedetails_name);
        phone = (TextView) findViewById(R.id.storedetails_phone);
        shutdowntime = (TextView) findViewById(R.id.storedetails_shutdowntime);
        address = (TextView) findViewById(R.id.storedetails_address);
        type = (TextView) findViewById(R.id.storedetails_type);
    }

    private int getMultiple(int widht) {
        double aa = widht;
        double bb = 60;
        double cc = 0;
        try {
            cc = Arith.div(aa, bb, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        cc = Arith.mul(cc, 29);
        return (int) cc;
    }

    private void setViews() {
        name.setText(storeInfo.get("name"));
        phone.setText(storeInfo.get("phone"));
        shutdowntime.setText(storeInfo.get("starttime") + "-" + storeInfo.get("endtime"));
        address.setText(storeInfo.get("address"));
        type.setText(storeInfo.get("type"));
        x.image().bind(icon, storeInfo.get("icon"));
    }

    private ImageView setImageViewWith(ImageView iv) {
        para = iv.getLayoutParams();
        //设置宽高比60:29
        para.width = Tools.getWidht(this);
        para.height = getMultiple(Tools.getWidht(this));
        iv.setLayoutParams(para);
        return iv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storedetails_icon:
                //广告图
                break;
            case R.id.storedetails_introduction:
                //商家介绍
                IntentStoreInfroduction();
                break;
            case R.id.storedetails_onlinepay:
                //支付
                intent = new Intent(this, OnlinePay.class);
                Tools.setLog("当前id"+id);
                intent.putExtra("did", id);
                intent.putExtra("storename", storeInfo.get("name"));
                intent.putExtra("storeaddress", storeInfo.get("address"));
                intent.putExtra("storeuser", storeInfo.get("userName"));
                startActivity(intent);
                break;
            case R.id.headac_left:
                //
                finish();
                break;
        }
    }

    private void IntentStoreInfroduction() {
        intent = new Intent(this, StoreIntroduction.class);
        intent.putExtra("iconnum", 4);
        intent.putExtra("icon1", storeInfo.get("icon1"));
        intent.putExtra("icon2", storeInfo.get("icon2"));
        intent.putExtra("icon3", storeInfo.get("icon3"));
        intent.putExtra("icon4", storeInfo.get("icon4"));
        intent.putExtra("remark", storeInfo.get("remark"));
        startActivity(intent);
    }
}
