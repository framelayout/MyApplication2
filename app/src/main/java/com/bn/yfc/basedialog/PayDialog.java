package com.bn.yfc.basedialog;

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
import android.widget.LinearLayout;

import com.bn.yfc.R;
import com.bn.yfc.map.ReleaseDelivery;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.AliPayTools;
import com.bn.yfc.wxapi.WXShare;

/**
 * Created by Administrator on 2017/2/8.
 */

public class PayDialog extends DialogFragment implements View.OnClickListener {

    private View view;
    private LinearLayout alipay, wx;
    private Intent intent;
    private String type;
    private String order;
    private String money;
    private String app_id;
    private String privateKey;
    private String payType;
    private String outURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        if (type.equals("pay")) {
            order = getArguments().getString("order");
            money = getArguments().getString("money");
            app_id = getArguments().getString("app_id");
            privateKey = getArguments().getString("privateKey");
            payType = getArguments().getString("paytype");
            outURL = getArguments().getString("outurl");
        } else {
            order = getArguments().getString("order");
            money = getArguments().getString("money");
            app_id = getArguments().getString("app_id");
            privateKey = getArguments().getString("privateKey");
            payType = getArguments().getString("paytype");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.paydialog, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        wx = (LinearLayout) view.findViewById(R.id.paydialog_wx);
        wx.setOnClickListener(this);
        alipay = (LinearLayout) view.findViewById(R.id.paydialog_alipy);
        alipay.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        int dialogWidth = (int) (width * 0.9);
        int dialogHeight = (int) (height * 0.4);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paydialog_wx:
                Tools.setLog("WXpayOrder" + order);
                WXShare.sendWXSign(getActivity(), true, "创言网支付订单", order, money, "fufei");
                dismiss();
                break;
            case R.id.paydialog_alipy:
                AliPayTools.AlipaySend(getActivity(), privateKey, app_id, money, outURL, order);
                dismiss();
                break;
        }
    }
}
