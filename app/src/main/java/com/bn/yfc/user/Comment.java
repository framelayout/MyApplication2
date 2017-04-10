package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/27.
 */

public class Comment extends BaseActivity implements View.OnClickListener {
    private EditText editText;
    private Button but;
    private ImageView left;
    private TextView title;
    private RatingBar rabar;
    private String OrderNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        initHead();
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            OrderNumber = getIntent().getStringExtra("number");
            Tools.setLog("订单号为" + OrderNumber);
        }
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("评价");
    }

    private void initView() {
        but = (Button) findViewById(R.id.comment_but);
        but.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.comment_edittext);
        rabar = (RatingBar) findViewById(R.id.comment_bar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.comment_but:
                sendPost();
                break;
        }
    }

    private void sendPost() {
        int numbers = rabar.getNumStars();
        String contexts = editText.getText().toString();
        if (Tools.isEmpty(contexts)) {
            Tools.showToast(this, "请输入对订单的评价");
            return;
        }
        if (!(numbers > 0)) {
            Tools.showToast(this, "请输入对订单的评分");
            return;
        }
        AjaxParams paras = new AjaxParams();
        paras.put("stars", String.valueOf(numbers));
        paras.put("id", OrderNumber);
        paras.put("comment", contexts);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTCOMMENTURL, paras, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("用户评论发送成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            finish();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("用户评论发送失败" + strMsg);
            }
        });
    }
}
