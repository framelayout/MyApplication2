package com.bn.yfc.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Administrator on 2017/3/16.
 */

public class XiaoListView extends BaseActivity implements View.OnClickListener {
    private LinearLayout excitation, recommend, exlin, relin;
    private ListView listview;
    private List<Map<String, String>> datas;
    private ImageView left;
    private TextView title, branch;
    private ListViewAdapter adapter;
    int typeCode = 0;
    private RefreshLayout refresh;
    private int reCode = 0;
    private int num = 10;
    private LinearLayout noData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiaolistview);
        initHead();
        initView();
        initData();
    }

    private void initData() {

        ifsTypeInitData();

    }

    private void ifsTypeInitData() {
        //0=激励善分1=推荐善分
        sendData();
    }

    //加载关闭
    private void closeRefreshs() {
        refresh.setLoading(false);
    }

    //刷新关闭
    private void closeRefresh() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        branch = (TextView) findViewById(R.id.branch);
        branch.setText((String) SharedPreferencesUtils.getParam(this, "branch", ""));
        noData = (LinearLayout) findViewById(R.id.nodata);
        refresh = (RefreshLayout) findViewById(R.id.refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                //加载
                reCode = 2;
                num += 10;
                ifsTypeInitData();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                reCode = 1;
                ifsTypeInitData();
            }
        });
        listview = (ListView) findViewById(R.id.xiaolistview_list);
        excitation = (LinearLayout) findViewById(R.id.xiaolistview_excitation);
        recommend = (LinearLayout) findViewById(R.id.xiaolistview_recommend);
        exlin = (LinearLayout) findViewById(R.id.xiaolistview_exlin);
        relin = (LinearLayout) findViewById(R.id.xiaolistview_relin);
        exlin.setOnClickListener(this);
        relin.setOnClickListener(this);
    }

    private void initHead() {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        datas = new ArrayList<>();
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("我的善分");
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
    }

    private void IfsLinBackColor() {
        switch (typeCode) {
            case 0:
                excitation.setBackgroundResource(R.color.colorRedColors);
                recommend.setBackgroundResource(R.color.colorWhite);
                break;
            case 1:
                excitation.setBackgroundResource(R.color.colorWhite);
                recommend.setBackgroundResource(R.color.colorRedColors);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.xiaolistview_exlin:
                typeCode = 0;
                IfsLinBackColor();
                num = 10;
                initData();
                break;
            case R.id.xiaolistview_relin:
                Tools.setLog("改变为推荐善分");
                typeCode = 1;
                num = 10;
                IfsLinBackColor();
                initData();
                break;
        }
    }

    private void showRe() {
        refresh.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    private void showNodata() {
        noData.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
    }

    //推荐善分
    private void sendData() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("id", (String) SharedPreferencesUtils.getParam(this, "id", ""));
        params.put("type", "1");
        params.put("num", String.valueOf(num));
        FinalHttp fhp = new FinalHttp();
        String url = "";
        if (typeCode == 0) {
            url = MyConfig.POSTBRABANCHURL;
        } else {
            url = MyConfig.POSTEXCTITATIONURL;
        }
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("获取善分数据成功\n" + s);
                closeProgressDialog();
                showRe();
                switch (reCode) {
                    case 0:
                        break;
                    case 1:
                        closeRefresh();
                        break;
                    case 2:
                        closeRefreshs();
                        break;
                }
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {

                        case 1:

                            JSONArray ary = object.getJSONArray("data");
                            if (datas != null) {
                                datas.clear();
                            }
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject ob1 = ary.getJSONObject(i);
                                Map<String, String> map = new HashMap<String, String>();
                                Tools.setLog("当前codetype为:" + typeCode);
                                if (typeCode == 0) {
                                    map.put("fraction0", ob1.getString("integral"));
                                    map.put("heart", ob1.getString("heart"));
                                    map.put("time0", Tools.getDates(Long.valueOf(ob1.getString("time"))));
                                } else {
                                    map.put("time1", /*Tools.getDates(Long.valueOf(ob1.getString("true_time")))*/Tools.getCurrentTime());
                                    map.put("identity", ob1.getString("send_name"));
                                    map.put("money", ob1.getString("send_money"));
                                }
                                datas.add(map);
                            }
                            if (adapter != null) {
                                adapter = null;
                            }
                            adapter = new ListViewAdapter(datas, getApplication());
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 110:
                            showNodata();
                            closeProgressDialog();
                            Tools.showToast(getApplication(), "暂无数据");
                            if (adapter != null) {
                                adapter = null;
                            }
                            adapter = new ListViewAdapter(datas, getApplication());
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 112:
                            showNodata();
                            closeProgressDialog();
                            Tools.showToast(getApplication(), "没有邀请人");
                            if (adapter != null) {
                                adapter = null;
                            }
                            adapter = new ListViewAdapter(datas, getApplication());
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                switch (reCode) {
                    case 0:
                        break;
                    case 1:
                        closeRefresh();
                        break;
                    case 2:
                        closeRefreshs();
                        break;
                }
                Tools.setLog("获取善分数据失败" + strMsg);
            }
        });
    }

    class ListViewAdapter extends BaseAdapter {

        List<Map<String, String>> datas;
        Context context;

        public ListViewAdapter(List<Map<String, String>> datas, Context context) {

            this.context = context;
            this.datas = datas;
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

            if (typeCode == 0) {
                convertView = excitationView(position, convertView);
            } else {
                convertView = recommendView(position, convertView);
            }
            return convertView;
        }

        //激励
        private View excitationView(int position, View convertView) {
            HolderView0 holderView;
            if (convertView == null) {
                holderView = new HolderView0();
                convertView = LayoutInflater.from(context).inflate(R.layout.excitationitem, null);
                holderView.fraction = (TextView) convertView.findViewById(R.id.excitationitem_fraction);
                holderView.heart = (TextView) convertView.findViewById(R.id.excitationitem_heart);
                holderView.showtime = (TextView) convertView.findViewById(R.id.excitationitem_showtime);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView0) convertView.getTag();
            }
            holderView.showtime.setText(datas.get(position).get("time0"));
            holderView.heart.setText(datas.get(position).get("heart"));
            holderView.fraction.setText(datas.get(position).get("fraction0"));
            return convertView;
        }

        //推荐
        private View recommendView(int position, View convertView) {
            HolderView1 holderView;
            if (convertView == null) {
                holderView = new HolderView1();
                convertView = LayoutInflater.from(context).inflate(R.layout.recommenditem, null);
                holderView.fraction = (TextView) convertView.findViewById(R.id.recommenditem_fraction);
                holderView.identity = (TextView) convertView.findViewById(R.id.recommenditem_identity);
                holderView.money = (TextView) convertView.findViewById(R.id.recommenditem_money);
                holderView.showtime = (TextView) convertView.findViewById(R.id.recommenditem_showtime);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView1) convertView.getTag();
            }
            holderView.showtime.setText(datas.get(position).get("time1"));
            holderView.money.setText(datas.get(position).get("money") + "元");
            holderView.identity.setText(datas.get(position).get("identity"));
            holderView.fraction.setText(Float.valueOf(datas.get(position).get("money")) * 0.006 + "");
            return convertView;
        }

        class HolderView1 {
            TextView showtime;
            TextView identity;
            TextView money;
            TextView fraction;
        }

        class HolderView0 {
            TextView showtime;
            TextView heart;
            TextView fraction;
        }
    }
}
