package com.bn.yfc.userdata;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Permissions;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/7.
 */

public class UserdataFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ViewPager viewPager;
    private TextView title;
    private ImageView right;
    private Intent intent;
    private ViewPargerAdapter adapter;
    private List<Fragment> datas;
    UserData1 data1 = new UserData1();
    UserData2 data2 = new UserData2();
    private Timer tim;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                    initTimer();
                    break;
                case 1:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userdataframgnets, null);
        initHead(view);
        initView(view);
        initData();
        sendData();
        return view;
    }

    private void sendData() {
        //请求数据
        String url = MyConfig.POSTGETNUM;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                Tools.setLog("请求数据成功\n" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("成功" + object.getString("data"));
                            JSONObject ob = object.getJSONObject("data");
                            String userNumber = ob.getString("user_num");
                            String driverNumber = ob.getString("driver_num");
                            String storeNumber = ob.getString("shangjia_num");
                            String money = ob.getString("money_num");
                            data2.initData(userNumber, driverNumber, storeNumber, money);
                            String sums = ob.getString("sum");
                            String user = ob.getString("user");
                            String dirver = ob.getString("driver");
                            data1.initData(sums, user, dirver);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("请求数据失败\n" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void initData() {
        adapter = new ViewPargerAdapter(getChildFragmentManager());
        datas = new ArrayList<>();
        datas.add(data1);
        datas.add(data2);
        viewPager.setAdapter(adapter);
        initTimer();

    }

    public void cleanTimer() {
        if (tim != null) {
            tim.cancel();
            tim = null;
        }
        if (data1 != null) {

        }
        if (data2 != null) {

        }

    }

    public void initTimer() {
        if (tim != null) {
            tim.cancel();
            tim = null;
        }
        tim = new Timer();
        tim.schedule(new TimerTask(), 3000);
    }

    class TimerTask extends java.util.TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.userdatafragment_viewpager);

    }

    private void initHead(final View view) {
        title = (TextView) view.findViewById(R.id.head_title);
        title.setText("数据展示");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.setLog("当前参数为:" + viewPager.getCurrentItem());
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                }
            }
        });
        right = (ImageView) view.findViewById(R.id.head_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
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

    class ViewPargerAdapter extends FragmentPagerAdapter {

        public ViewPargerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Tools.setLog("onStart");
        if (Permissions.newsDataPermissions(getActivity())) {
            right.setBackgroundResource(R.drawable.newshave_icon);
        } else {
            right.setBackgroundResource(R.drawable.news_icon);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Tools.setLog("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.setLog("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.setLog("onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.setLog("onDestroyView");
        cleanTimer();
    }


}
