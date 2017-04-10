package com.bn.yfc.fragmentmain;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bn.yfc.R;


/**
 * Created by Administrator on 2016/12/16.
 */

public class ButonFragment extends Fragment implements OnClickListener {
    private LinearLayout ordersInterface, auditInterface, userInterface, urlInterface, userData;
    private View view;
    public static final int ORDERS = 0;
    public static final int AUDIT = 1;
    public static final int URLS = 2;
    public static final int USER = 3;
    public static final int USERDATA = 4;

    private ImageView ordersicon;
    private ImageView auditicon;
    private ImageView urlicon;
    private ImageView usericon;
    private ImageView userdataicon;
    // 监听对象
    private OnBottomItemClickListener bottonItemClickListener;
    private TextView roderstext, audittext, usertext, urltext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buttonfragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
        urltext = (TextView) view.findViewById(R.id.butonfragment_urltext);
        roderstext = (TextView) view.findViewById(R.id.butonfragment_orderstext);
        audittext = (TextView) view.findViewById(R.id.butonfragemnt_audittext);
        usertext = (TextView) view.findViewById(R.id.butonfragment_usertext);
        urlicon = (ImageView) view.findViewById(R.id.butonfragment_urlicon);
        userdataicon = (ImageView) view.findViewById(R.id.butonfragment_userdataicon);
        ordersicon = (ImageView) view.findViewById(R.id.butonfragment_ordersicon);
        auditicon = (ImageView) view.findViewById(R.id.butonfragment_auditicon);
        usericon = (ImageView) view.findViewById(R.id.butonfragemnt_usericon);
        urlInterface = (LinearLayout) view.findViewById(R.id.butonfragment_urllin);
        ordersInterface = (LinearLayout) view.findViewById(R.id.butonfragment_orderslin);
        auditInterface = (LinearLayout) view.findViewById(R.id.butonfragment_auditlin);
        userInterface = (LinearLayout) view.findViewById(R.id.butonfragment_userlin);
        userData = (LinearLayout) view.findViewById(R.id.butonfragment_userdata);
        userData.setOnClickListener(this);
        ordersInterface.setOnClickListener(this);
        auditInterface.setOnClickListener(this);
        userInterface.setOnClickListener(this);
        urlInterface.setOnClickListener(this);
    }

    // 设置监听事件
    public void setOnBottomItemClickListener(OnBottomItemClickListener bottomItemClickListener) {
        this.bottonItemClickListener = bottomItemClickListener;
    }

    public void setSelected(int position) {
        switch (position) {
            case ORDERS:
                ordersicon.setBackgroundResource(R.drawable.orders_onclick);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                roderstext.setTextColor(0xff78a5ff);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                break;
            case AUDIT:
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_onclick);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xff78a5ff);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                break;
            case URLS:
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.user_normal);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_onclick);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xff78a5ff);
                break;
            case USER:
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_onclick);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xff78a5ff);
                urltext.setTextColor(0xFF9b9b9b);
                break;
            case USERDATA:
                userdataicon.setBackgroundResource(R.drawable.userdata_normal);
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_onclick);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        View view = null;
        int position = 0;
        switch (v.getId()) {
            case R.id.butonfragment_orderslin:
                ordersicon.setBackgroundResource(R.drawable.orders_onclick);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_normal);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                roderstext.setTextColor(0xff78a5ff);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                position = ORDERS;
                break;
            case R.id.butonfragment_auditlin:
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                auditicon.setBackgroundResource(R.drawable.audit_onclick);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xff78a5ff);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                position = AUDIT;
                break;
            case R.id.butonfragment_urllin:
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_onclick);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xff78a5ff);
                position = URLS;
                break;
            case R.id.butonfragment_userlin:
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_onclick);
                userdataicon.setBackgroundResource(R.drawable.userdata_select);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xff78a5ff);
                urltext.setTextColor(0xFF9b9b9b);
                position = USER;
                break;
            case R.id.butonfragment_userdata:
                userdataicon.setBackgroundResource(R.drawable.userdata_normal);
                ordersicon.setBackgroundResource(R.drawable.orders_normal);
                auditicon.setBackgroundResource(R.drawable.audit_normal);
                usericon.setBackgroundResource(R.drawable.user_normal);
                urlicon.setBackgroundResource(R.drawable.urlsj_icon_normal);
                roderstext.setTextColor(0xFF9b9b9b);
                audittext.setTextColor(0xFF9b9b9b);
                usertext.setTextColor(0xFF9b9b9b);
                urltext.setTextColor(0xFF9b9b9b);
                position = USERDATA;
                break;
        }
        if (bottonItemClickListener != null) {
            bottonItemClickListener.onBottomItemClick(position);
        }

    }

}
