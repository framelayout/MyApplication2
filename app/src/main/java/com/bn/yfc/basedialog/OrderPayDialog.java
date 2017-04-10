package com.bn.yfc.basedialog;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bn.yfc.R;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.OrdersTest;
import com.bn.yfc.wxapi.AliPayTools;
import com.bn.yfc.wxapi.WXShare;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/23.
 */

public class OrderPayDialog extends DialogFragment implements View.OnClickListener {
    private Activity context;
    private View view;
    private EditText getMoney;
    private Button left, right;
    private String order_id;
    private ImageView wxpay, alipay;
    private boolean ifpay = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString("order_id") != null) {
            order_id = getArguments().getString("order_id");
        }
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.orderpaydialog, null);
        initView(view);
        //  Tools.showToast(getActivity(), "创建成功");

        setViews();
        return view;
    }

    private void setViews() {
        if (ifpay) {
            wxpay.setBackgroundResource(R.drawable.false_icon);
            alipay.setBackgroundResource(R.drawable.true_icon);
        } else {
            wxpay.setBackgroundResource(R.drawable.true_icon);
            alipay.setBackgroundResource(R.drawable.false_icon);
        }
    }

    private void initView(View view) {
        getMoney = (EditText) view.findViewById(R.id.orderpay_nomeynumber);
        left = (Button) view.findViewById(R.id.orderpayleft);
        right = (Button) view.findViewById(R.id.orderpayright);
        wxpay = (ImageView) view.findViewById(R.id.orderpay_wx);
        alipay = (ImageView) view.findViewById(R.id.orderpay_alipay);
        wxpay.setOnClickListener(this);
        alipay.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        int dialogWidth = (int) (width * 0.8);
        int dialogHeight = (int) (height * 0.4);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderpayleft:
                //  取消
                dismiss();
                break;
            case R.id.orderpayright:
                //支付
                String money = getMoney.getText().toString();
                if (Tools.isEmpty(money)) {
                    Tools.showToast(getActivity(), "请输入支付金额");
                    return;
                }
                Tools.setLog("传递的金额为" + money + "|" + order_id);
                sendOrader(money);
                dismiss();
                break;
            case R.id.orderpay_wx:
                ifpay = false;
                setViews();
                break;
            case R.id.orderpay_alipay:
                ifpay = true;
                setViews();
                break;
        }
    }

    private void sendOrader(String money) {
        AjaxParams params = new AjaxParams();
        params.put("id", order_id);
        params.put("money", money);
        FinalHttp fhp = new FinalHttp();
        Tools.showToast(getActivity(), "开始请求");
        fhp.post(MyConfig.POSTBWORDERURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("补充帮我买订单金额请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject ob = object.getJSONObject("data");
                            String money = ob.getString("send_money");
                            String order = ob.getString("ordernum");
                            String app_id = ob.getString("appID");
                            String privateKey = ob.getString("privateKey");
                            String notify_url = ob.getString("ali_back");
                            ifsTypePay(money, order, app_id, privateKey, notify_url);
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
                Tools.setLog("补充帮我买订单金额请求失败\n" + strMsg);
            }
        });
    }


    private void ifsTypePay(String money, String order, String app_id, String privateKey, String notify_url) {
        String orders = "";
        //orders += Tools.getTimeStamp() + Tools.getRandomString(4) + "_" + (String) SharedPreferencesUtils.getParam(context, "id", "");
        Tools.setLog("获取返回的order" + order);
        if (ifpay) {
            AliPayTools.AlipaySend(context, privateKey, app_id, money, notify_url, order);
        } else {
            WXShare.sendWXSign(context, true, "创言快递支付订单", order, money, "fufei");
        }
    }


}
