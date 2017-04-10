package com.bn.yfc.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.bn.yfc.basedialog.BaseProgressDialog;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.tools.*;


/**
 * Created by Administrator on 2016/12/16.
 */

public class BaseActivity extends FragmentActivity {


    private BaseProgressDialog progressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        com.bn.yfc.tools.Tools.setWindowStatusBarColor(this);
        MyAppLication.addActivityStack(this);
        progressDialog = new BaseProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyAppLication.removActivity(this);
    }

    /**
     * 设置进度对话框消息
     *
     * @param message
     */
    public void setProgressDialogMessage(String message) {
        progressDialog.setMessageText(message);
    }

    /**
     * 显示进度对话框
     */
    public void showProgressDialog() {
        progressDialog.show();
        progressDialog.startAnim();
    }

    /**
     * 关闭进度对话框
     */
    public void closeProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
