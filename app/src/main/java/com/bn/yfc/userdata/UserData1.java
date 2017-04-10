package com.bn.yfc.userdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.fragmentmain.FragmentMain;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Permissions;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/7.
 */

public class UserData1 extends Fragment implements View.OnClickListener {
    private View view;
    private TextView times, title;
    private TextView pastMoney, rebateMoney, orderMoney, storeMoney;
    private ImageView left;
    private Intent intent;
    private LinearLayout lin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userdatafragment, null);
        initView(view);
        return view;

    }

    public void initData(String psat, String rebate, String store) {
        //请求数据
        setViews(psat, rebate, store);
    }

    private void setViews(String psat, String rebate, String store) {
        //数据加载
        pastMoney.setText(psat);
        rebateMoney.setText(rebate);
        storeMoney.setText(store);
    }


    private void initView(View view) {
        times = (TextView) view.findViewById(R.id.userdata_time);
        long aa = System.currentTimeMillis();
        long bb = new Date().getTime();
        Tools.setLog("当前时间戳为" + aa + "|" + bb);
        aa = aa - 86400000;
        times.setText(Tools.getDatess(aa));
        pastMoney = (TextView) view.findViewById(R.id.userdata_pastmoney);
        rebateMoney = (TextView) view.findViewById(R.id.userdata_rebatemoney);
        storeMoney = (TextView) view.findViewById(R.id.userdata_storemoney);
        lin = (LinearLayout) view.findViewById(R.id.userdata1_lin);
        lin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((FragmentMain) (getActivity())).initViewPager();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        ((FragmentMain) (getActivity())).cleanViewPager();
                        break;
                }
                return true;
            }
        });
    }

    private void initHead(View v) {
        left = (ImageView) v.findViewById(R.id.head_right);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) v.findViewById(R.id.head_title);
        title.setText("数据展示");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), NewsInterface.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Tools.setLog("UserData1 onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.setLog("UserData1 onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.setLog("UserData1 onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.setLog("UserData1 onDestroyView");
    }
}
