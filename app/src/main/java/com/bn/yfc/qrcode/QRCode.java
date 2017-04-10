package com.bn.yfc.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.InviteInterface;
import com.bn.yfc.wxapi.WXShare;

/**
 * Created by Administrator on 2017/1/7.
 */

public class QRCode extends BaseActivity implements OnClickListener {

    private ImageView left, right;
    private TextView title, code;
    private ImageView show;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        initHead();
        initView();
        showCode();
    }

    private void initView() {
        code = (TextView) findViewById(R.id.qrcode_code);
        code.setOnClickListener(this);
        show = (ImageView) findViewById(R.id.qrcode_show);
        show.setOnClickListener(this);
        code.setText((String) SharedPreferencesUtils.getParam(this, "invite", ""));
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setOnClickListener(this);
        left.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("邀请码");
        right = (ImageView) findViewById(R.id.headac_right);
        right.setVisibility(View.VISIBLE);
        right.setBackgroundResource(R.drawable.mypen_icon);
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.headac_right:
                intent = new Intent(QRCode.this, InviteInterface.class);
                startActivity(intent);
                break;
            case R.id.headac_left:
                finish();

                break;
            case R.id.qrcode_show:
                //分享

                break;
            case R.id.qrcode_code:


                break;

        }
    }

    public void wxtoapp(View v) {
        String url = "http://120.77.87.193/index.php/Admin/invite?";
        Bitmap bit = Tools.generateBitmap(url + "type=1&" + "uid=" + SharedPreferencesUtils.getParam(getApplication(), "id", ""), 600, 600);
        WXShare.ToWXHtml(this, url, "邀请码:" + (String) SharedPreferencesUtils.getParam(getApplication(), "invite", ""), bit, 0);
    }

    public void wxtopen(View v) {
        String url = "http://120.77.87.193/index.php/Admin/invite?";
        Bitmap bit = Tools.generateBitmap(url + "type=1&" + "uid=" + SharedPreferencesUtils.getParam(getApplication(), "id", ""), 600, 600);
        WXShare.ToWXHtml(this, url, "邀请码:" + (String) SharedPreferencesUtils.getParam(getApplication(), "invite", ""), bit, 1);
    }

    private void showCode() {
        String url = (String) SharedPreferencesUtils.getParam(QRCode.this, "qrurl", "");
        url = "http://120.77.87.193/index.php/Admin/invite?";
        Bitmap bit = Tools.generateBitmap(url + "type=1&" + "uid=" + SharedPreferencesUtils.getParam(getApplication(), "id", ""), 600, 600);
        show.setImageBitmap(bit);
    }
}
