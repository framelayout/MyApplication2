package com.bn.yfc.wxapi;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/2/5.
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = MyAppLication.getIwxapi(WXPayEntryActivity.this);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Tools.setLog("onPayFinish,errCode=" + baseResp.errCode);
        switch (baseResp.errCode) {
            case 0:
                if (!Tools.isEmpty((String) SharedPreferencesUtils.getParam(getApplication(), "ispay", ""))) {
                    Tools.setLog("支付类型是" + SharedPreferencesUtils.getParam(getApplication(), "ispay", ""));
                    switch ((String) SharedPreferencesUtils.getParam(getApplication(), "ispay", "")) {
                        case "0":
                            Tools.setLog("订单支付为余额");
                            break;
                        case "1":
                            Tools.setLog("订单支付为快递");
                            break;
                        case "2":
                            Tools.setLog("订单支付为顺路送");
                            break;
                        case "3":
                            Tools.setLog("订单支付为帮我买");
                            break;
                        case "4":
                            Tools.setLog("订单支付为船运");
                            break;
                        case "5":
                            Tools.setLog("订单为面对面支付");
                            break;
                    }
                    SharedPreferencesUtils.setParam(getApplication(), "ispay", "13");
                    finish();
                }
                break;
            case -2:
               // Tools.showToast(getApplication(), "已取消支付");
                Tools.setLog("微信支付|" + SharedPreferencesUtils.getParam(getApplication(), "ispay", "13"));
                finish();
                break;

        }


    }
}
