package com.bn.yfc.basedialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bn.yfc.R;


/**
 * Created by Administrator on 2017/2/9.
 */

public class BaseProgressDialog extends Dialog {
    private ImageView loadingImg = null;
    private TextView loadingText = null;
    private AnimationDrawable animationDrawable;

    public BaseProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseProgressDialog(Context context) {
        super(context, R.style.base_progress_dialog_style);
        setContentView(R.layout.base_pd);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        loadingImg = (ImageView) findViewById(R.id.loadingImg);
        animationDrawable = (AnimationDrawable) loadingImg.getDrawable();
        loadingText = (TextView) findViewById(R.id.loadingMsg);
        loadingText.setText("加载中...");
    }

    protected BaseProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void startAnim() {
        animationDrawable.start();
    }

    public void setMessageText(String msg) {
        if (loadingText != null) {
            loadingText.setText(msg);
        }
    }
}
