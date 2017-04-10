package com.bn.yfc.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bn.yfc.R;

/**
 * Created by Administrator on 2016/11/22.
 */

public class NoticeTest extends Activity {
    private Button buton;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "red".equals(intent.getAction())) {
                buton.setText(intent.getStringExtra("test"));
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticetest);
        buton = (Button) findViewById(R.id.notice_one);
        registerReceiver(mReceiver, new IntentFilter("red"));
    }

    public void OnClickOne(View view) {
        ShowNotice.ShowNotice(this);

    }

    public void OnClickTwo(View view) {
        ShowNotice.CleanNotice();
    }

    public void OnClickThree(View view) {

    }

    public void OnClickFour(View view) {

    }


}
