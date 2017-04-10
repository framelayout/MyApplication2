package com.bn.yfc.fragmentmain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;

/**
 * Created by Administrator on 2017/4/9.
 */

public class ProtocolWeb extends BaseActivity implements View.OnClickListener {
    String url;

    private ImageView left;
    private TextView title;
    private WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolweb);
        initData();
        initHead();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }
    }

    private void initView() {
        web = (WebView) findViewById(R.id.protocolweb);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                web.loadUrl(url);
                return true;
            }
        });
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("协议");
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
