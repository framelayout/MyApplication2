package com.bn.yfc.fragmentmain;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
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
 * Created by Administrator on 2017/1/6.
 */

public class NewsInterface extends BaseActivity implements View.OnClickListener {
    private RelativeLayout systemNews, otherNews;
    private ImageView left, sysicon, othericon;
    private TextView title;
    private ListView sysList, otherList;
    private LinearLayout systemlin, otherlin;
    private SysTemListAdapter sysAdapter;
    private SysTemListAdapter otherAdapter;
    private List<Map<String, String>> sysListData;
    private List<Map<String, String>> otherListData;
    private boolean ifsSystem = false;
    private boolean ifsOther = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsinterface);
        SharedPreferencesUtils.setParam(getApplication(), "news", "0");
        initHead();
        initData();
        initView();

    }

    private void initData() {
        //清理右上角图标
        SharedPreferencesUtils.setParam(getApplication(), "news", "0");
        sysListData = new ArrayList<Map<String, String>>();
        otherListData = new ArrayList<Map<String, String>>();
        sendData();
    }

    private void sendData() {
        AjaxParams params = new AjaxParams();
        params.put("uid", (String) SharedPreferencesUtils.getParam(NewsInterface.this, "id", ""));
        params.put("type", "1");
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTURLMESGURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("请求消息中心成功:\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray otherAry = object.getJSONArray("order");
                            JSONArray orderAry = object.getJSONArray("other");
                            if (otherAry.length() > 0) {
                                for (int i = 0; i < otherAry.length(); i++) {
                                    Map<String, String> ortherMap = new HashMap<String, String>();
                                    JSONObject ary = otherAry.getJSONObject(i);
                                    String title = ary.getString("message");
                                    String time = Tools.getDates(Long.valueOf(ary.getString("time")));
                                    ortherMap.put("title", title);
                                    ortherMap.put("time", time);
                                    otherListData.add(ortherMap);
                                }
                            }
                            if (orderAry.length() > 0) {
                                for (int i = 0; i < orderAry.length(); i++) {
                                    Map<String, String> orderMap = new HashMap<String, String>();
                                    JSONObject ary = orderAry.getJSONObject(i);
                                    String title = ary.getString("message");
                                    String time = Tools.getDates(Long.valueOf(ary.getString("time")));
                                    orderMap.put("title", title);
                                    orderMap.put("time", time);
                                    sysListData.add(orderMap);
                                }

                            }
                            setViewData(sysListData, otherListData);

                            break;

                        case 2:
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
                Tools.setLog("消息请求失败:" + strMsg);
            }
        });
    }


    private void setViewData(List<Map<String, String>> systemData, List<Map<String, String>> otherListData) {
        if (otherAdapter != null) {
            otherAdapter = null;
        }
        if (sysAdapter != null) {
            sysAdapter = null;
        }
        otherAdapter = new SysTemListAdapter(NewsInterface.this, otherListData);
        sysAdapter = new SysTemListAdapter(NewsInterface.this, sysListData);
        otherList.setAdapter(otherAdapter);
        sysList.setAdapter(sysAdapter);
    }

    private void initView() {
        systemNews = (RelativeLayout) findViewById(R.id.news_system);
        systemNews.setOnClickListener(this);
        otherNews = (RelativeLayout) findViewById(R.id.news_other);
        otherNews.setOnClickListener(this);
        systemlin = (LinearLayout) findViewById(R.id.news_systemlin);
        otherlin = (LinearLayout) findViewById(R.id.news_otherlin);
        sysicon = (ImageView) findViewById(R.id.news_sysicon);
        othericon = (ImageView) findViewById(R.id.news_othericon);
        sysList = (ListView) findViewById(R.id.news_systemlist);
        otherList = (ListView) findViewById(R.id.news_otherlist);
        sysList.setAdapter(sysAdapter);
        otherList.setAdapter(otherAdapter);
        sysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        otherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("消息中心");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.news_system:
                if (ifsSystem) {
                    systemlin.setVisibility(View.GONE);
                    ifsSystem = false;
                    sysicon.setBackgroundResource(R.drawable.right_reicon);
                } else {
                    sysicon.setBackgroundResource(R.drawable.bottom_icon);
                    systemlin.setVisibility(View.VISIBLE);
                    ifsSystem = true;
                }
                break;
            case R.id.news_other:
                if (ifsOther) {
                    otherlin.setVisibility(View.GONE);
                    othericon.setBackgroundResource(R.drawable.right_reicon);
                    ifsOther = false;
                } else {
                    otherlin.setVisibility(View.VISIBLE);
                    othericon.setBackgroundResource(R.drawable.bottom_icon);
                    ifsOther = true;
                }
                break;
        }
    }

    class SysTemListAdapter extends BaseAdapter {
        List<Map<String, String>> sysdata;
        Context conetext;

        public SysTemListAdapter(Context conetext, List<Map<String, String>> sysdata) {
            this.conetext = conetext;
            this.sysdata = sysdata;
        }

        @Override
        public int getCount() {
            if (sysdata.size() < 0) {
                return 0;
            }
            return sysdata.size();
        }

        @Override
        public Object getItem(int position) {
            return sysdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(conetext).inflate(R.layout.newsystemitem, null);
                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.newssysitemtit);
                holder.times = (TextView) convertView.findViewById(R.id.newssysitemtime);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.title.setText(sysdata.get(position).get("title"));
            holder.times.setText(sysdata.get(position).get("time"));
            return convertView;
        }

        class Holder {
            TextView title;
            TextView times;
        }
    }
}
