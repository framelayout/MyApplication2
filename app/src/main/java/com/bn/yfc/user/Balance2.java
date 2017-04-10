package com.bn.yfc.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/19.
 */

public class Balance2 extends BaseActivity implements View.OnClickListener {
    private Spinner spinner;
    private List<Map<String, String>> datas;
    private SpinnerAdapter adapter;
    private Button but;
    private ImageView left;
    private TextView title, phone;
    private Intent intent;
    private EditText inMoney;
    private String bankId, bankName, openBank, bankNum;

    RefreshData refreshData = new RefreshData(Balance2.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance2);
        initHead();
        initView();
        initData();

    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("提现");
    }

    private void initView() {
        inMoney = (EditText) findViewById(R.id.balance2_money);
        inMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        inMoney.setText(s);
                        inMoney.setSelection(s.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phone = (TextView) findViewById(R.id.balance2_phone);
        phone.setText("当前账号: " + (String) SharedPreferencesUtils.getParam(getApplication(), "name", " "));
        but = (Button) findViewById(R.id.balance2_but);
        but.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.balance2_spinner);
    }


    private void initData() {
        getData();
        datas = new ArrayList<Map<String, String>>();
        adapter = new SpinnerAdapter(this, datas);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankId = datas.get(position).get("bankId");
                bankName = datas.get(position).get("bankName");
                openBank = datas.get(position).get("bankType");
                bankNum = datas.get(position).get("bankNumber");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.balance2_but:
                String oldMoney = (String) SharedPreferencesUtils.getParam(getApplication(), "money", " ");
                String inputMoney = inMoney.getText().toString();
               /*  if (Tools.isEmpty(oldMoney)) {
                    Tools.showToast(getApplication(), "余额不足，请充值");
                    return;
                }
                if ((Float.valueOf(oldMoney) >= Float.valueOf(inputMoney))) {*/
                sendMoney(inputMoney);
             /*   } else {
                    Tools.showToast(getApplication(), "余额不够支付");
                }*/
                break;

        }
    }

    class SpinnerAdapter extends BaseAdapter {
        List<Map<String, String>> datas;
        Context context;

        public SpinnerAdapter(Context context, List<Map<String, String>> datas) {
            this.context = context;
            this.datas = datas;
        }

        public void setDatas(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HolderView holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.spinneritem, null);
                holder = new HolderView();
                holder.titel = (TextView) view.findViewById(R.id.spinneritem2_title);
                holder.lin = (LinearLayout) view.findViewById(R.id.spinneritem2_lin);
                view.setTag(holder);
            } else {
                holder = (HolderView) view.getTag();
            }
            holder.titel.setText(datas.get(i).get("bankType") + "(" + datas.get(i).get("bankNumber") + ")");

            return view;
        }

        class HolderView {
            TextView titel;
            LinearLayout lin;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void finishActivity() {
        Log.d("yfc", "发送广播");
        intent = new Intent();
        intent.setAction("cleanBalance");
        this.sendBroadcast(intent);
    }

    private void setDataToView(JSONArray ary) {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        datas = new ArrayList<Map<String, String>>();
        for (int i = 0; i < ary.length(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            try {
                JSONObject ob = ary.getJSONObject(i);
                String bankId = ob.getString("id");
                String bankNumber = ob.getString("bank");
                String banAddress = ob.getString("card");
                String bankType = ob.getString("user");
                String nickName = ob.getString("pos");
                map.put("bankId", bankId);
                map.put("bankNumber", bankNumber);
                map.put("bankAddress", banAddress);
                map.put("bankType", bankType);
                map.put("bankName", nickName);
                datas.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.setDatas(datas);
        }

    }

    private void sendMoney(String bankId, String money) {
        AjaxParams params = new AjaxParams();
        params.put("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("bid", bankId);
        params.put("money", money);
        params.put("type", "1");

        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTGETMONEYSURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(getApplication(), "提现申请成功");
                            finishActivity();
                            finish();
                            break;
                        case 110:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                closeProgressDialog();
            }
        });

    }

    //获取银行卡信息
    public void getData() {
        RequestParams params = new RequestParams(MyConfig.POSTSHOWBANKURL);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
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
                            Tools.showToast(Balance2.this, "获取银行卡成功\n");
                            setDataToView(object.getJSONArray("data"));
                            break;
                        case 110:
                            Tools.showToast(Balance2.this, "获取失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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


    /*3.31*/
    private void sendMoney(String money) {

        AjaxParams params = new AjaxParams();
        //银行账号
        params.put("cardNo", bankNum);
        //市
        String citys = (String) SharedPreferencesUtils.getParam(getApplication(), "city", "");
        if (Tools.isEmpty(citys)) {
            Tools.showToast(getApplication(), "请打开定位权限");
            return;
        }
        params.put("city", citys);
        //收款人姓名
        params.put("usrName", bankName);
        //收款金额
        params.put("transAmt", /*money*/"0.01");
        //开户银行
        params.put("openBank", openBank);
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
        Tools.setLog("传递的金额为" + money + "|type:" + "1" + "|prov:" + sheng + "|city:" + citys + "|openBank:" + openBank + "|收款账号:" + bankNum + "|收款人姓名:" + bankName + "|用户id:" + SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        showProgressDialog();
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTSENDTIXURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("提交提现申请成功\n" + s);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1001:
                            Tools.showToast(getApplication(), "提现申请成功");
                            refreshData.StartDataRefresh(getApplication(), (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
                            finish();
                            break;
                        case 1002:
                            Tools.showToast(getApplication(), "提现失败");
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
                            Tools.showToast(getApplication(), "提现额度不足");
                            break;
                        case 1008:
                            Tools.showToast(getApplication(), "提现处理中");
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
                closeProgressDialog();
            }
        });

    }
}
