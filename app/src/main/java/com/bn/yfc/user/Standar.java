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

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Standar extends BaseActivity implements OnClickListener {
    private ImageView left;
    private TextView title;
    private ListView listview;
    private List<Map<String, String>> datas;
    private Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standar);
        initHead();
        initView();
        initData();

    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("运费规则");
    }

    private void initData() {
        datas = new ArrayList<Map<String, String>>();
        sendData();

    }

    private void sendData() {
        AjaxParams parms = new AjaxParams();
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTRULESURL, parms, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("运费规则请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject urlObject = object.getJSONObject("url");
                            String headUrl = urlObject.getString("url");
                            JSONArray ary = object.getJSONArray("data");
                            for (int i = 0; i < ary.length(); i++) {
                                Map<String, String> map = new HashMap<String, String>();
                                JSONObject ob = ary.getJSONObject(i);
                                String title = ob.getString("title");
                                String context = ob.getString("content1");
                                String icon = ob.getString("pic1");
                                Tools.setLog("拼接照片地址\n" + headUrl + icon);
                                String context2 = ob.getString("content2");
                                String icon2 = ob.getString("pic2");
                                String context3 = ob.getString("content3");
                                String icon3 = ob.getString("pic3");
                                Tools.setLog("map title\n" + title);
                                map.put("title", title);
                                if (icon.equals("") && Tools.isEmpty(icon)) {
                                } else {
                                    map.put("icon", headUrl + icon);
                                }
                                if (icon2.equals("") && Tools.isEmpty(icon2)) {
                                } else {
                                    map.put("icon2", headUrl + icon2);
                                }
                                if (icon3.equals("") && Tools.isEmpty(icon3)) {
                                } else {
                                    map.put("icon3", headUrl + icon3);
                                }
                                if (context.equals("") && Tools.isEmpty(context)) {
                                } else {
                                    map.put("context", context);
                                }
                                if (context2.equals("") && Tools.isEmpty(context2)) {
                                } else {
                                    map.put("context2", context2);
                                }
                                if (context3.equals("") && Tools.isEmpty(context3)) {
                                } else {
                                    map.put("context3", context3);
                                }
                                datas.add(map);
                            }
                            setListData(datas);

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("运费规则请求失败" + strMsg);
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }

    private void setListData(List<Map<String, String>> datas) {
        adapter = new Adapter(getApplication(), datas);
        listview.setAdapter(adapter);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.standar_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
        }
    }

    class Adapter extends BaseAdapter {

        List<Map<String, String>> datas;
        Context conetxt;

        public Adapter(Context context, List<Map<String, String>> datas) {
            this.conetxt = context;
            this.datas = datas;
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
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(conetxt).inflate(R.layout.standaritem, null);
                holder.titles = (TextView) convertView.findViewById(R.id.standaritem_title);
                holder.contexts = (TextView) convertView.findViewById(R.id.standaritem_context1);
                holder.icon = (ImageView) convertView.findViewById(R.id.standaritem_icon1);
                holder.contexts2 = (TextView) convertView.findViewById(R.id.standaritem_context2);
                holder.contexts3 = (TextView) convertView.findViewById(R.id.standaritem_context3);
                holder.icon2 = (ImageView) convertView.findViewById(R.id.standaritem_icon2);
                holder.icon3 = (ImageView) convertView.findViewById(R.id.standaritem_icon3);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.titles.setText(datas.get(position).get("title"));
            holder.contexts.setText(datas.get(position).get("context"));
            x.image().bind(holder.icon, datas.get(position).get("icon"));
            if (datas.get(position).get("context2").equals(" ") && Tools.isEmpty(datas.get(position).get("context2"))) {

            } else {
                holder.contexts2.setText(datas.get(position).get("context2"));

            }
            if (datas.get(position).get("icon2").equals("") && Tools.isEmpty(datas.get(position).get("icon2"))) {

            } else {
                x.image().bind(holder.icon2, datas.get(position).get("icon2"));
            }
            if (datas.get(position).get("context3").equals("") && Tools.isEmpty(datas.get(position).get("context3"))) {

            } else {
                holder.contexts3.setText(datas.get(position).get("context3"));
            }
            if (datas.get(position).get("icon3").equals("") && Tools.isEmpty(datas.get(position).get("icon3"))) {
            } else {
                x.image().bind(holder.icon3, datas.get(position).get("icon3"));
            }

            return convertView;
        }

        class Holder {
            TextView titles;
            TextView contexts, contexts2, contexts3;
            ImageView icon, icon2, icon3;
        }
    }
}
