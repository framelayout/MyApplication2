package com.bn.yfc.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/1/7.
 */

public class Opertion extends BaseActivity implements OnClickListener {

    private ImageView left;
    private TextView title;
    private TextView right;
    private EditText name, phone;
    private LinearLayout select;
    private TextView selectAddress;
    private Intent intent;
    private String id = null;
    private String money = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opertion);
        initHead();
        initView();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.opertion_name);
        phone = (EditText) findViewById(R.id.opertion_phone);
        selectAddress = (TextView) findViewById(R.id.opertion_selectaddress);
        select = (LinearLayout) findViewById(R.id.opertion_select);
        select.setOnClickListener(this);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.opertion_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.opertion_title);
        title.setText("加盟代理");
        right = (TextView) findViewById(R.id.opertion_complete);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.opertion_complete:
                if (id == null) {
                    Tools.showToast(getApplication(), "请选择成为代理地区");
                    break;
                }
                PostSelectAddress();
                break;
            case R.id.opertion_left:
                finish();
                break;
            case R.id.opertion_select:
                intent = new Intent(this, OperAddress.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 13) {
            if (data == null) {
                return;
            }
            id = data.getStringExtra("id");
            money = data.getStringExtra("money");
            if (id == null) {
                return;
            }
            selectAddress.setText(data.getStringExtra("address"));
            Tools.setLog("收到的参数为" + id + "|" + money + "|" + selectAddress.getText().toString());
        }
    }

    //上传选择
    private void PostSelectAddress() {
        AjaxParams params = new AjaxParams();
        params.put("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.put("cid", id);
        params.put("type", "0");
        params.put("money", money);
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTBECOMEURL, params, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("请求数据为===" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(getApplication(), "您的申请已提交,请等待客服人员与您联系");
                            finish();
                            break;
                        case 110:
                            Tools.showToast(getApplication(), "您之前已提交过代理申请,请耐心等待审核");
                            break;
                        case 112:
                            Tools.showToast(getApplication(), "您已是我们的代理商");
                            break;
                        case 111:
                            Tools.showToast(getApplication(), "代理失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.setLog("解析数据报错" + e.getMessage());
                }
            }


            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("申请代理请求报错=====" + strMsg);
                closeProgressDialog();
                Tools.showToast(Opertion.this, "网络错误");
            }
        });
    }
}
