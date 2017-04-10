package com.bn.yfc.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.R;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */

public class Problem extends BaseActivity implements OnClickListener {
    private List<Map<String, String>> datas;
    private ImageView left;
    private TextView title;
    private ListView listView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem);
        initHead();
        initData();
        initView();
        setView();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.proble_list);
        listView.setAdapter(adapter);
    }

    private void setView() {

    }

    private void getData() {
        AjaxParams par = new AjaxParams();
        par.put("type", "1");
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTGETQANDAURL, par, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("请求常用数据成功" + s);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.setLog("解析数据");
                            JSONArray ary = object.getJSONArray("data");
                            Tools.setLog("数组长度" + ary.length());
                            for (int i = 0; i < ary.length(); i++) {
                                Tools.setLog("循环1");
                                Map<String, String> map = new HashMap<String, String>();
                                Tools.setLog("循环2");
                                JSONObject ob = ary.getJSONObject(i);
                                Tools.setLog("循环3");
                                map.put("answer", ob.getString("answer"));
                                Tools.setLog("循环4");
                                map.put("quesiton", ob.getString("question"));
                                Tools.setLog("循环5");
                                datas.add(map);
                                Tools.setLog("循环6");
                            }
                            adapter.setAdapters(datas);
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
                Tools.setLog("请求常用数据错误");
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }


    private void initData() {
        datas = new ArrayList<Map<String, String>>();
        if (adapter == null) {
            adapter = new ListAdapter(Problem.this, datas);
        }
        getData();
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("常见问题");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
        }
    }


    class ListAdapter extends BaseAdapter {
        Context context;
        List<Map<String, String>> datas;

        public ListAdapter(Context context, List<Map<String, String>> datas) {
            this.context = context;
            this.datas = datas;
        }

        public void setAdapters(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();
            Tools.setLog("重载数据");
        }

        @Override
        public int getCount() {
            if (datas.size() < 0) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.problemitem, null);
                holder = new HolderView();
                holder.quesiton = (TextView) convertView.findViewById(R.id.problemitem_question);
                holder.answer = (TextView) convertView.findViewById(R.id.problemitem_ansewer);
                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }
            holder.quesiton.setText(datas.get(position).get("quesiton"));
            holder.answer.setText(datas.get(position).get("answer"));
            return convertView;
        }

        class HolderView {
            TextView quesiton;
            TextView answer;
        }


    }
}
