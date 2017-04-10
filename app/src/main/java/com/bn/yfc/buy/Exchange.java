package com.bn.yfc.buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.basedialog.BaseSelectBank;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/14.
 */

public class Exchange extends BaseActivity implements View.OnClickListener {

    private ImageView left;
    private TextView title, right, xiaofen, bankName, bankNum, hint1, hint2, hint3, hint4, hint5;
    private Button but;
    private LinearLayout banks;
    private EditText getMoney;
    private Intent intent;
    private String bankId, bankNames, openBanks, bankNums;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange);
        initHead();
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
        but = (Button) findViewById(R.id.exchange_but);
        but.setOnClickListener(this);
        xiaofen = (TextView) findViewById(R.id.exchange_xiaofen);
        xiaofen.setText((String) SharedPreferencesUtils.getParam(getApplication(), "branch", ""));
        hint1 = (TextView) findViewById(R.id.exchange_hint1);
        hint2 = (TextView) findViewById(R.id.exchange_hint2);
        hint3 = (TextView) findViewById(R.id.exchange_hint3);
        hint4 = (TextView) findViewById(R.id.exchange_hint4);
        hint5 = (TextView) findViewById(R.id.exchange_hint5);
        bankName = (TextView) findViewById(R.id.exchange_bankname);
        bankNum = (TextView) findViewById(R.id.exchange_banknum);
        banks = (LinearLayout) findViewById(R.id.exchange_bank);
        banks.setOnClickListener(this);
        getMoney = (EditText) findViewById(R.id.exchange_money);
        getMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        getMoney.setText(s);
                        getMoney.setSelection(s.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("兑换");
        right = (TextView) findViewById(R.id.headac_right);
        right.setOnClickListener(this);
    }

    public void BankInfo(String num, String s, String userNames) {
        Tools.setLog("获取到银行卡为" + num);
        bankName.setText(s);
        bankNum.setText(num);
        bankNums = num;
        openBanks = s;
        bankNames = userNames;
    }


    private void sendMoneyExchange() {
        AjaxParams params = new AjaxParams();
        if (Tools.isEmpty(bankNums)) {
            Tools.showToast(getApplication(), "请选择银行卡");
            return;
        }
        //银行账号
        params.put("cardNo", bankNums);
        //市
        String citys = (String) SharedPreferencesUtils.getParam(getApplication(), "city", "");
        if (Tools.isEmpty(citys)) {
            Tools.showToast(getApplication(), "请打开定位权限");
            return;
        }
        params.put("city", citys);
        //收款人姓名
        params.put("usrName", bankNames);
        //兑换金额
        String moneys = getMoney.getText().toString();
        if (Tools.isEmpty(moneys)) {
            Tools.showToast(getApplication(), "请输入兑换孝分");
            return;
        }
        //收款金额
        params.put("transAmt", /*money*/"0.01");
        //开户银行
        params.put("openBank", openBanks);
        //用户id
        params.put("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        //省S
        String sheng = (String) SharedPreferencesUtils.getParam(getApplication(), "sheng", "");
        if (Tools.isEmpty(sheng)) {
            Tools.showToast(getApplication(), "请打开定位权限");
            return;
        }
        params.put("prov", sheng);
        //1用户2司机
        params.put("type", "1");
        Tools.setLog("传递的金额为" + "" + "|type:" + "1" + "|prov:" + sheng + "|city:" + citys + "|openBank:" + "" + "|收款账号:" + "" + "|收款人姓名:" + "" + "|用户id:" + SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        showProgressDialog();
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTSENDMONEYURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("兑换申请请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1001:
                            Tools.showToast(getApplication(), "兑换申请成功");
                            finish();
                            break;
                        case 1002:
                            Tools.showToast(getApplication(), "兑换失败");
                            break;
                        case 1003:
                            Tools.showToast(getApplication(), "请输入整百金额");
                            break;
                        case 1004:
                            Tools.showToast(getApplication(), "金额格式出错");
                            break;
                        case 1005:
                            Tools.showToast(getApplication(), "金额为空");
                            break;
                        case 1006:
                            break;
                        case 1007:
                            Tools.showToast(getApplication(), "兑换额度不足");
                            break;
                        case 1008:
                            Tools.showToast(getApplication(), "兑换处理中");
                            break;
                        case 1009:
                            Tools.showToast(getApplication(), "网络错误");
                            break;
                        case 0:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("兑换申请请求失败 " + strMsg);
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.headac_right:
                intent = new Intent(this, ExchangeList.class);
                startActivity(intent);
                break;
            case R.id.exchange_but:
                sendMoneyExchange();
                break;
            case R.id.exchange_bank:
                showBankDialog();
                break;
        }
    }

    private void showBankDialog() {
        BaseSelectBank bank = new BaseSelectBank();
        bank.show(getSupportFragmentManager(), "addbank");
    }
}
