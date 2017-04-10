package com.bn.yfc.buy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.Arith;
import com.bn.yfc.tools.Tools;

import org.xutils.x;

/**
 * Created by Administrator on 2017/3/14.
 * 商家介绍
 */

public class StoreIntroduction extends BaseActivity implements View.OnClickListener {
    private ImageView left;
    private TextView title;
    private ImageView icon1, icon2, icon3, icon4;
    private TextView introdiction;
    private ViewGroup.LayoutParams para;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storeintroduction);

        initHead();
        initView();
        initData();
    }

    private void initData() {
        if (getIntent() != null) {
            introdiction.setText(getIntent().getStringExtra("remark"));
            x.image().bind(icon1, getIntent().getStringExtra("icon1"));
            x.image().bind(icon2, getIntent().getStringExtra("icon2"));
            x.image().bind(icon3, getIntent().getStringExtra("icon3"));
            x.image().bind(icon4, getIntent().getStringExtra("icon4"));
        }
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("商家介绍");
    }

    private void initView() {
        introdiction = (TextView) findViewById(R.id.storeintorduction_text);
        icon1 = (ImageView) findViewById(R.id.storeintr_icon1);
        icon1 = setImageViewWith(icon1);
        setImageBace(icon1, "");
        Tools.setLog("icon1=" + icon1.getWidth());
        icon2 = (ImageView) findViewById(R.id.storeintr_icon2);
        icon2 = setImageViewWith(icon2);
        setImageBace(icon2, "");
        icon3 = (ImageView) findViewById(R.id.storeintr_icon3);
        icon3 = setImageViewWith(icon3);
        setImageBace(icon3, "");
        icon4 = (ImageView) findViewById(R.id.storeintr_icon4);
        icon4 = setImageViewWith(icon4);
        setImageBace(icon4, "");
    }

    private void setImageBace(ImageView ic, String url) {
        x.image().bind(ic, "http://s2.cn.bing.net/th?id=OJ.bNnS04UnaWa0Bw&pid=MSNJVFeeds");
    }

    private int getMultiple(int widht) {
        double aa = widht;
        Tools.setLog("当前屏幕宽度为" + aa);
        double bb = 2.1;
        double cc = 0;
        try {
            cc = Arith.div(aa, bb, 1);
            Tools.setLog("cc的值是" + cc);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) cc;
    }

    private ImageView setImageViewWith(ImageView iam) {

        para = iam.getLayoutParams();
        para.width = getMultiple(Tools.getWidht(this));
        para.height = para.width;
        iam.setLayoutParams(para);
        return iam;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
        }
    }
}
