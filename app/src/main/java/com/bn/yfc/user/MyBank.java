package com.bn.yfc.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bn.yfc.base.BankData;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MyBank extends BaseActivity implements OnClickListener {

    private ImageView left, right;
    private TextView title;
    private List<BankData> datas;
    private ListAdapter adapter;
    private ListView listview;
    private Intent intent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.setLog("onDestroy");
        cleanData();

    }

    private void cleanData() {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.setLog("onResume");
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Tools.setLog("onPause");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.setLog("onCreate");
        setContentView(R.layout.mybank);
        initHead();
        initView();
        initData();
    }

    private void RefreshData() {
        Tools.setLog("刷新银行datas");
        if (datas == null) {
            datas = new ArrayList<BankData>();
        }
        if (adapter != null) {
            adapter.addData(datas);
        } else {
            adapter = new ListAdapter(this, datas);
            listview.setAdapter(adapter);

        }

    }

    private void initView() {
        listview = (ListView) findViewById(R.id.mybank_list);

    }

    private void initData() {
        getData();
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("银行卡管理");
        right = (ImageView) findViewById(R.id.headac_right);
        right.setBackgroundResource(R.drawable.add_bank);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.headac_title:
                break;
            case R.id.headac_right:
                intent = new Intent(this, AddBank.class);
                startActivity(intent);
                break;
        }
    }

    private void deleteBank(String bankId) {
        RequestParams params = new RequestParams(MyConfig.POSTDELETEBANKURL);
        params.addBodyParameter("id", bankId);
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
                            Tools.showToast(getApplication(), "删除成功");
                            getData();
                            break;
                        case 110:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("删除银行卡请求错误我： ===================" + ex.getMessage());
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

    private void getData() {
        Tools.setLog("开始获取数据======================");
        Tools.setLog("Uid = " + (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        RequestParams params = new RequestParams(MyConfig.POSTSHOWBANKURL);
        params.addBodyParameter("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.addBodyParameter("type", "1");
        showProgressDialog();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                closeProgressDialog();
                if (result != null) {
                    Tools.setLog("获取成功============" + result);
                }
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("获取成功");
                            JSONArray ary = object.getJSONArray("data");
                            if (ary == null) {
                                Tools.setLog("银行卡数组为空");
                                break;
                            }
                            if (datas != null) {
                                datas.clear();
                                datas = null;
                            }
                            datas = new ArrayList<BankData>();
                            for (int i = 0; i < ary.length(); i++) {
                                BankData data = new BankData();
                                JSONObject ob1 = (JSONObject) ary.get(i);
                                data.setBankId(ob1.getString("id"));
                                data.setBankNumber(ob1.getString("bank"));
                                data.setBankType(ob1.getString("user"));
                                data.setBankCard(ob1.getString("card"));
                                data.setBankPos(ob1.getString("pos"));
                                datas.add(data);
                            }
                            RefreshData();
                            break;
                        case 110:
                            if (datas != null) {
                                datas.clear();
                                datas = null;
                            }
                            RefreshData();
                            Tools.setLog("获取失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.setLog("获取用户银行卡数据错误======================" + ex.getMessage());
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


    class ListAdapter extends BaseAdapter {
        List<BankData> datas;
        Context context;

        public ListAdapter(Context context, List<BankData> datas) {
            this.context = context;
            this.datas = datas;
        }

        public void addData(List<BankData> datas) {
            this.datas = datas;
            notifyDataSetInvalidated();
            Tools.setLog("adapter 刷新数据");
        }

        @Override
        public int getCount() {
            if (datas.size() < 1) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.mybankitem, null);
                holder.but = (Button) view.findViewById(R.id.mybankitem_delete);
                holder.number = (TextView) view.findViewById(R.id.mybankitem_number);
                holder.title = (TextView) view.findViewById(R.id.mybankitem_bank);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.title.setText(datas.get(position).getBankType());
            holder.number.setText(datas.get(position).getBankNumber());
            holder.but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBank(datas.get(position).getBankId());
                }
            });
            return view;
        }

        class ViewHolder {
            TextView title;
            TextView number;
            Button but;
        }
    }
}
