package com.bn.yfc.userdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.fragmentmain.FragmentMain;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Permissions;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/7.
 */

public class UserData2 extends Fragment implements OnClickListener {
    private View view;
    private TextView times, title;
    private TextView order, money, store, usernum;
    private ImageView left;
    private Intent intent;
    private LinearLayout lin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userdatafragment2, null);
        initView(view);
        /*initData();*/
        return view;

    }

    public void initData(String userNumber, String driverNumber, String storeNumber, String money) {
        setViews(userNumber, driverNumber, storeNumber, money);
    }

    private void setViews(String userNumber, String driverNumber, String storeNumber, String moneys) {
        //数据加载
        usernum.setText(userNumber);
        store.setText(storeNumber);
        money.setText(moneys);
        order.setText(driverNumber);
    }

    private void initView(View view) {
        order = (TextView) view.findViewById(R.id.userdata2_orders);
        money = (TextView) view.findViewById(R.id.userdata2_money);
        usernum = (TextView) view.findViewById(R.id.userdata2_usernum);
        store = (TextView) view.findViewById(R.id.userdata2_store);
        lin = (LinearLayout) view.findViewById(R.id.userdata2_lin);
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

                //消费掉事件
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
        Tools.setLog("UserData2 onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.setLog("UserData2 onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.setLog("UserData2 onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.setLog("UserData2 onDestroyView");
    }
}
