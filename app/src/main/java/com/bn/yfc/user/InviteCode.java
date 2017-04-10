package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.wxapi.WXShare;


/**
 * Created by Administrator on 2017/1/18.
 */

public class InviteCode extends BaseActivity implements OnClickListener {


    private ImageView left, right;
    private TextView title;
    private TextView code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitecode);
        initHead();
        code = (TextView) findViewById(R.id.invitecode_code);
        code.setText((String) SharedPreferencesUtils.getParam(getApplication(), "invite", ""));

    }

    private void sendData() {


    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("我的邀请码");
        right = (ImageView) findViewById(R.id.headac_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        right.setBackgroundResource(R.drawable.shares_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.headac_right:
                WXShare.ToWXText(this, code.getText().toString());
                break;
        }
    }
}
