package com.bn.yfc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.wxapi.WXShare;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/12/31.
 */

public class TestPost extends BaseActivity implements OnClickListener {
    private Button but;
    private TextView hint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testpost);
        initView();
    }

    private void initView() {
        but = (Button) findViewById(R.id.testpost_but1);
        but.setOnClickListener(this);
        hint = (TextView) findViewById(R.id.testpost_hint);
    }

    private void testPostData() {
        RequestParams params = new RequestParams(MyConfig.POSTGETPHONENUMURL);
        x.http().post(params, new Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Tools.setLog("测试客服请求成功" + result);
                hint.setText(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("测试客服请求报错 ======" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testpost_but1:
                testPostData();
                WXShare.WXLogin(this);
                //  WXShare.ToWXText(this,"分享");
                break;
            case 2:
                break;
        }
    }
}
