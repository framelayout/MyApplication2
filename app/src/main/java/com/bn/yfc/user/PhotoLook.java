package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;

import org.xutils.x;

/**
 * Created by Administrator on 2017/2/24.
 */

public class PhotoLook extends BaseActivity {
    String path = "";

    private ImageView look;
    private ImageView left;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolook);
        initHead();
        initData();
        initView();
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("照片预览");
    }

    private void initView() {
        look = (ImageView) findViewById(R.id.photolook);
        x.image().bind(look, path);
    }

    private void initData() {
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
        }
    }
}
