package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */

public class AddBank extends BaseActivity implements OnClickListener {

    private Map<String, String> newMap;
    private ImageView left;
    private TextView title;
    private EditText name, type, number, address;
    private Button but;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbank);
        initHead();
        initView();
        getDatas();
    }

    private void initView() {
        but = (Button) findViewById(R.id.addbank_but);
        but.setOnClickListener(this);
        address = (EditText) findViewById(R.id.addbank_address);
        name = (EditText) findViewById(R.id.addbank_name);
        type = (EditText) findViewById(R.id.addbank_bankname);
        number = (EditText) findViewById(R.id.addbank_number);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("添加银行卡");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.addbank_but:
                String nae = name.getText().toString();
                String typ = type.getText().toString();
                String num = number.getText().toString();
                String addre = address.getText().toString();
                if (Tools.isEmpty(nae)) {
                    Tools.showToast(this, "请输入持卡人姓名");
                    break;
                }
                if (Tools.isEmpty(typ)) {
                    Tools.showToast(this, "请输入卡号");
                    break;
                }
                if (Tools.isEmpty(addre)) {
                    Tools.showToast(this, "请输入开户行地址");
                    break;
                }
                if (Tools.isEmpty(num)) {
                    Tools.showToast(this, "请输入开户行类型");
                    break;
                }
                PostData(nae, typ, num, addre);
                break;
        }
    }

    private void PostData(String addres, String nae, String typ, String num) {
        RequestParams params = new RequestParams(MyConfig.POSETADDBANKURL);
        params.addBodyParameter("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.addBodyParameter("type", "1");
        params.addBodyParameter("bank", typ);
        params.addBodyParameter("user", nae);
        params.addBodyParameter("card", num);
        params.addBodyParameter("pos", addres);
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(AddBank.this, "银行添加成功");
                            finish();
                            break;
                        case 110:
                            Tools.showToast(AddBank.this, "添加失败，请重新提交");
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("添加银行请求错误=======" + ex.getMessage());
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void getDatas() {
        AjaxParams paraams = new AjaxParams();
        paraams.put("", "");
    }
}
