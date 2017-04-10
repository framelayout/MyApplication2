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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/12/30.
 */

public class Feedback extends BaseActivity implements OnClickListener {
    private ImageView left;
    private TextView title;
    private EditText edittext;
    private Button but;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        initHead();
        initView();
    }

    private void initView() {
        edittext = (EditText) findViewById(R.id.feedback_edittext);
        but = (Button) findViewById(R.id.feedback_but);
        but.setOnClickListener(this);
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("意见反馈");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.feedback_but:
                String str = edittext.getText().toString();
                if (Tools.isEmpty(str)) {
                    Tools.showToast(this, "请输入宝贵的意见");
                    break;
                }
                postData(str);

                break;
        }
    }

    private void postData(String str) {
        RequestParams params = new RequestParams(MyConfig.POSTSENGBACKURL);
        params.addBodyParameter("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.addBodyParameter("type", "1");
        params.addBodyParameter("content", str);
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Tools.setLog("反馈意见获取成功==============" + result);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);

                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(getApplication(), "提交成功");
                            finish();
                            break;
                        case 110:
                            Tools.showToast(getApplication(), "提交失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("反馈建议请求报错" + ex.getMessage());
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
