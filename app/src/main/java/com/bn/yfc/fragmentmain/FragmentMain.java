package com.bn.yfc.fragmentmain;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.R;
import com.bn.yfc.buy.Chain;
import com.bn.yfc.buy.Chains1;
import com.bn.yfc.buy.GoBuyFragment;
import com.bn.yfc.map.DeliveryMap;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.UserInterface;
import com.bn.yfc.userdata.UserData2;
import com.bn.yfc.userdata.UserdataFragment;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2016/12/16.
 */

public class FragmentMain extends BaseActivity implements OnBottomItemClickListener {
    private ButonFragment butFragment = new ButonFragment();
    private GoBuyFragment audit = new GoBuyFragment();
    private DeliveryMap orders = new DeliveryMap();
    private UserInterface user = new UserInterface();
    private UserdataFragment userdata = new UserdataFragment();
    //   private Chain chain = new Chain();
    private Chains1 chain = new Chains1();
    private LinearLayout lin;
    private View framgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.fragmentmain);
        lin = (LinearLayout) findViewById(R.id.fragmentmain_lin);
        framgent = findViewById(R.id.fragmentmain_framgent);
        initBut();
    }

    private void initBut() {
        butFragment = (ButonFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentmain_but);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, orders).commit();
        butFragment.setOnBottomItemClickListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count - 1 >= 0) {
                    FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
                    String name = entry.getName();

                    setItemChecked(name);
                }
            }
        });


    }

    public void cleanViewPager() {
        userdata.cleanTimer();
    }

    public void initViewPager() {
        userdata.initTimer();
    }

    public void setFragmentVisi() {
        framgent.setVisibility(View.VISIBLE);
        lin.setVisibility(View.GONE);
    }

    public void setFragmentGong() {
        lin.setVisibility(View.VISIBLE);
        framgent.setVisibility(View.GONE);
    }

    public void ifPay() {
        if (audit != null) {
            audit.startifPay();
        }
    }

    public void setItemChecked(String name) {
        int position = ButonFragment.ORDERS;
        if (name.equals("orders")) {
            position = ButonFragment.ORDERS;
        } else if (name.equals("audit")) {
            position = ButonFragment.AUDIT;
        } else if (name.equals("uri")) {
            position = ButonFragment.URLS;
        } else if (name.equals("user")) {
            position = ButonFragment.USER;
        } else if (name.equals("userdata")) {
            position = ButonFragment.USERDATA;
        }
        butFragment.setSelected(position);
    }

    @Override
    public void onBottomItemClick(int position) {
        switch (position) {
            case ButonFragment.ORDERS:
                if (!orders.isAdded()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, orders).commit();
                }

                break;
            case ButonFragment.AUDIT:
                orders.SetMapVisi();
                if (!audit.isAdded()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, audit).commit();
                }
                break;
            case ButonFragment.URLS:
                orders.SetMapVisi();
                if (!chain.isAdded()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, chain).commit();
                }
                break;
            case ButonFragment.USER:
                orders.SetMapVisi();
                if (!user.isAdded()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, user).commit();
                }
                break;
            case ButonFragment.USERDATA:
                orders.SetMapVisi();
                if (!userdata.isAdded()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentmain_framgent, userdata).commit();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean returnif = false;
    private Timer timer;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Tools.showToast(this, "再按一次退出程序");
            if (returnif) {
                MyAppLication.allExit();
            } else {
                returnif = true;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        returnif = false;
                    }
                }, 3000);
            }
        } else {

        }
        return false;

    }

}
