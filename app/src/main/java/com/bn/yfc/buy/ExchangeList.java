package com.bn.yfc.buy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.base.RefreshLayout;
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
 * Created by Administrator on 2017/3/15.
 */

public class ExchangeList extends BaseActivity implements View.OnClickListener {

    private ImageView left;
    private TextView title;
    private ListView listview;
    private List<Map<String, String>> datas;
    private ListAdapter adapter;
    private int number = 13;
    private RefreshLayout relalyou;
    private int reCode = 0;
//determine_icon

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchangelist);
        initHead();
        initView();
        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        adapter = new ListAdapter(this, datas);
        listview.setAdapter(adapter);
        sendData();
    }


    private void sendData() {
        AjaxParams params = new AjaxParams();
        params.put("uid", (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
        params.put("type", "1");
        params.put("num", String.valueOf(number));
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTSHOWSTOREURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                switch (reCode) {
                    case 1:
                        closeRefresh();
                        break;
                    case 2:
                        closeRefreshs();
                        break;
                }
                Tools.setLog("兑换记录请求成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ary = object.getJSONArray("data");
                            if (datas != null) {
                                datas.clear();
                            }
                            for (int i = 0; i < ary.length(); i++) {
                                Map<String, String> map = new HashMap<String, String>();
                                JSONObject ob1 = ary.getJSONObject(i);
                                map.put("xiaofen", ob1.getString("sort"));
                                map.put("time", Tools.getDates(Long.valueOf(ob1.getString("time"))));
                                map.put("state", ob1.getString("state"));
                                datas.add(map);
                            }
                            adapter.addData(datas);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("兑换记录请求失败" + strMsg);
                switch (reCode) {
                    case 1:
                        closeRefresh();
                        break;
                    case 2:
                        closeRefreshs();
                        break;
                }
            }
        });

    }

    private void closeRefreshs() {
        relalyou.setLoading(false);
    }

    private void closeRefresh() {
        relalyou.post(new Runnable() {
            @Override
            public void run() {
                relalyou.setRefreshing(false);
            }
        });
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.exchangelistview_listview);
        relalyou = (RefreshLayout) findViewById(R.id.refresh);
        relalyou.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        relalyou.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                reCode = 1;
                sendData();

            }
        });
        relalyou.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                //加载
                if (datas.size() < 13) {
                    closeRefreshs();
                    return;
                }
                reCode = 2;
                number += 10;
                sendData();
            }
        });
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("兑换历史");
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
        HolderView holder;

        public ListAdapter(Context context, List<Map<String, String>> datas) {
            this.context = context;
            this.datas = datas;

        }

        public void addData(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (datas.size() > 0) {
                return datas.size();
            }
            return 0;
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
            if (convertView == null) {
                holder = new HolderView();
                convertView = LayoutInflater.from(context).inflate(R.layout.exchanglistitem, null);
                holder.showtim = (TextView) convertView.findViewById(R.id.exchanglistitem_showtime);
                holder.xiaofen = (TextView) convertView.findViewById(R.id.exchanglistitem_xiaofen);
                holder.status = (TextView) convertView.findViewById(R.id.exchanglistitem_status);
                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }
            if (datas.size() < 0) {
            } else {
                holder.showtim.setText(datas.get(position).get("time"));
                holder.xiaofen.setText(datas.get(position).get("xiaofen"));
                if (datas.get(position).get("state").equals("1")) {
                    holder.status.setText("已打款");
                } else {
                    holder.status.setText("未打款");
                }

            }
            return convertView;
        }

        class HolderView {
            TextView showtim, xiaofen, status;
            ImageView icon;
        }
    }
}
