package com.bn.yfc.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bn.yfc.basedialog.WaiterDialog;
import com.bn.yfc.buy.Exchange;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.R;
import com.bn.yfc.qrcode.QRCode;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

/**
 * Created by Administrator on 2016/12/16.
 */

public class UserInterface extends Fragment implements OnClickListener {
    private View view;
    private boolean stuate = false;
    private ImageView left, right, icon, righticon;
    private LinearLayout balance, orders, settings, waiter, opertion, problem, suggest, comment, invite, qrinterface, invitecode, standar;
    private Button login;
    private TextView title, name, jifen;
    private Intent intent;
    LocalBroadcastManager broadcastManager;
    RefreshData pefreshdata = new RefreshData(getActivity());
    /*新编功能 3.10*/
    private TextView integral, heart, heart_integral;
    private LinearLayout integral_but, heart_but, checklist_but, fraction_but, exchange_but, waiter1;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    pefreshdata.StartDataRefresh(getActivity(), SharedPreferencesUtils.getParam(getActivity(), "id", "") + "");
                    ifsUserStatus();
                    break;
            }

        }
    };

    //判断是否登录设置view
    private void ifsUserStatus() {
        if ((SharedPreferencesUtils.getParam(getActivity(), "islogin", "") != null) && SharedPreferencesUtils.getParam(getActivity(), "islogin", "").equals("1")) {
            //设置积分和其他参数
            name.setText((String) SharedPreferencesUtils.getParam(getActivity(), "nickname", " "));
            stuate = true;
            setView();
        } else {
            integral.setText("_");
            heart.setText("_");
            heart_integral.setText("_");
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.user_icon_main);
            //实际上这是一个BitmapDrawable对象
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            //可以在调用getBitmap方法，得到这个位图
            Bitmap bitmap = bitmapDrawable.getBitmap();
            icon.setImageBitmap(bitmap);
            icon.setBackgroundResource(R.drawable.user_icon_main);
            name.setText("未登录");
            stuate = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.userinterface, null);
        Tools.setWindowStatusBarColor1(getActivity(), R.color.colorwindowcolor);
        Tools.setWindowStatusBarColor(getActivity());
        initHead(view);
        initView(view);
        ifsUserStatus();
        return view;
    }

    private void setView() {
        Tools.setLog("刷新头像");
        integral.setText((String) SharedPreferencesUtils.getParam(getActivity(), "current_jifen", ""));
        heart.setText((String) SharedPreferencesUtils.getParam(getActivity(), "current_heart", ""));
        heart_integral.setText((String) SharedPreferencesUtils.getParam(getActivity(), "branch", ""));
        ImageRequest request = new ImageRequest((String) SharedPreferencesUtils.getParam(getActivity(), "pic", " "), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                icon.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyAppLication.getmQueue().add(request);
        pefreshdata.RefreshWaith(getActivity());
    }

    private void initView(View view) {
        /*新编功能*/
        waiter1 = (LinearLayout) view.findViewById(R.id.userinterface_waiter1);
        waiter1.setOnClickListener(this);
        integral = (TextView) view.findViewById(R.id.userinterface_integral);
        heart = (TextView) view.findViewById(R.id.userinterface_heart);
        heart_integral = (TextView) view.findViewById(R.id.userinterface_heart_integral);
        integral_but = (LinearLayout) view.findViewById(R.id.userinterface_integral_but);
        integral_but.setOnClickListener(this);
        heart_but = (LinearLayout) view.findViewById(R.id.userinterface_heart_but);
        heart_but.setOnClickListener(this);
        checklist_but = (LinearLayout) view.findViewById(R.id.userinterface_checklist_but);
        checklist_but.setOnClickListener(this);
        fraction_but = (LinearLayout) view.findViewById(R.id.userinterface_fraction_but);
        fraction_but.setOnClickListener(this);
        exchange_but = (LinearLayout) view.findViewById(R.id.userinterface_exchange_but);
        exchange_but.setOnClickListener(this);
        /*新编功能*/
        standar = (LinearLayout) view.findViewById(R.id.userinterface_standar);
        standar.setOnClickListener(this);
        invitecode = (LinearLayout) view.findViewById(R.id.userinterface_invitescode);
        invitecode.setOnClickListener(this);
        qrinterface = (LinearLayout) view.findViewById(R.id.userinterface_qr);
        qrinterface.setOnClickListener(this);
        invite = (LinearLayout) view.findViewById(R.id.userinterface_invite);
        invite.setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.userinterface_name);
        /***/
        icon = (ImageView) view.findViewById(R.id.userinterface_usericon);
        icon.setOnClickListener(this);
        balance = (LinearLayout) view.findViewById(R.id.userinterface_balance);
        balance.setOnClickListener(this);
        orders = (LinearLayout) view.findViewById(R.id.userinterface_orders);
        orders.setOnClickListener(this);
        settings = (LinearLayout) view.findViewById(R.id.userinterface_settings);
        settings.setOnClickListener(this);
        waiter = (LinearLayout) view.findViewById(R.id.userinterface_waiter);
        waiter.setOnClickListener(this);
        opertion = (LinearLayout) view.findViewById(R.id.userinterface_opertion);
        opertion.setOnClickListener(this);
        problem = (LinearLayout) view.findViewById(R.id.userinterface_problem);
        problem.setOnClickListener(this);
        suggest = (LinearLayout) view.findViewById(R.id.userinterface_suggest);
        suggest.setOnClickListener(this);
        comment = (LinearLayout) view.findViewById(R.id.userinterface_comment);
        comment.setOnClickListener(this);
    }

    private void initHead(View view) {
        right = (ImageView) view.findViewById(R.id.head_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        setRightVew();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinterface_waiter1:
                WaiterDialog waiterdialog = new WaiterDialog();
                Bundle bundle = new Bundle();
                bundle.putString("test", "test");
                waiterdialog.setArguments(bundle);
                waiterdialog.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.userinterface_checklist_but:
                //我的订单列表
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), UserAudit.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_fraction_but:
                //我的孝分
                intent = new Intent(getActivity(), XiaoListView.class);
                startActivity(intent);
                break;
            case R.id.userinterface_heart_but:
                //我的孝心
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Myintegral.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_exchange_but:
                //兑换
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Exchange.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_integral_but:
                //我的积分
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Myintegral.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.head_right:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), NewsInterface.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_usericon:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), UserSettings.class);
                    Tools.setLog("ifdata" + (String) SharedPreferencesUtils.getParam(getActivity().getApplication(), "ifdata", ""));
                    intent.putExtra("data", (String) SharedPreferencesUtils.getParam(getActivity().getApplication(), "ifdata", ""));
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_settings:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), SettingsInterface.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_waiter:
                WaiterDialog waiterdialog1 = new WaiterDialog();
                Bundle bundle1 = new Bundle();
                bundle1.putString("test", "test");
                waiterdialog1.setArguments(bundle1);
                waiterdialog1.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.userinterface_opertion:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Opertion.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_problem:

                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Problem.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_suggest:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Feedback.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_comment:
                break;
            case R.id.userinterface_orders:

                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), UserAudit.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }
                break;
            case R.id.userinterface_balance:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Balance.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_invite:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), InviteInterface.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_qr:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), QRCode.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_invitescode:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), InviteCode.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
            case R.id.userinterface_standar:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), Standar.class);
                    startActivity(intent);
                } else {
                    logIntent();
                }

                break;
        }
    }

    private void logIntent() {
        intent = new Intent(getActivity(), Login.class);
        intent.putExtra("logintype", "login");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setRightVew() {
        if (Permissions.userLoginPermissions(getActivity())) {
            if (Permissions.newsDataPermissions(getActivity())) {
                right.setBackgroundResource(R.drawable.newshave_icon);
            } else {
                right.setBackgroundResource(R.drawable.news_icon);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Tools.setLog("userInterface onStart");
        handler.sendEmptyMessage(0);
        setRightVew();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
