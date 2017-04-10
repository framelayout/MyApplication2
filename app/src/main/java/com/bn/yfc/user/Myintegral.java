package com.bn.yfc.user;

import android.content.Context;
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
import com.bn.yfc.fragmentmain.RefreshData;
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

public class Myintegral extends BaseActivity implements View.OnClickListener {
    //0是积分
    private String type;
    private ListView listview;
    private List<Map<String, String>> datas;
    private ListAdapter adapter;
    private ImageView left;
    private TextView title, current, accumulation;
    private int num = 13;
    private RefreshLayout refreshLayout;
    private int fisResh = 0;
    private LinearLayout noData;
    private RefreshData reData = new RefreshData(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myintegral);
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            Tools.setLog("当前状态为" + type);
        }
        initHead();
        initView();
        initData();


    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        if (type.equals("0")) {
            title.setText("我的积分");
        } else {
            title.setText("我的善心");
        }
    }

    //加载
    private void load() {
        num += 10;
        sendIntegralData();
    }

    private void Reefre() {
        num = 13;
        sendIntegralData();
    }

    private void showRe() {
        refreshLayout.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    private void showNodata() {
        noData.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    //关闭加载
    private void closeRefreshs() {
        refreshLayout.setLoading(false);
    }

    //关闭刷新
    private void closeRefresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initView() {
        current = (TextView) findViewById(R.id.myintegral_current);
        accumulation = (TextView) findViewById(R.id.myintegral_accumulation);
        if (type.equals("0")) {
            //积分
            current.setText("当前积分" + SharedPreferencesUtils.getParam(this, "current_jifen", ""));
            accumulation.setText("历史积分" + SharedPreferencesUtils.getParam(this, "jifen", ""));
        } else {
            //善心
            current.setText("当前善心" + SharedPreferencesUtils.getParam(this, "current_heart", ""));
            accumulation.setText("历史善心" + SharedPreferencesUtils.getParam(this, "heart", ""));
        }
        noData = (LinearLayout) findViewById(R.id.nodata);
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                fisResh = 1;
                Reefre();
            }
        });
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                //加载
                fisResh = 2;
                load();
            }
        });
        listview = (ListView) findViewById(R.id.myintegral_listview);
    }

    private void initData() {


        datas = new ArrayList<>();
        //积分
        sendIntegralData();
        //善心
        adapter = new ListAdapter(getApplication(), datas);
        listview.setAdapter(adapter);

    }

    private void sendIntegralData() {
        AjaxParams params = new AjaxParams();
        params.put("type", "1");
        params.put("num", String.valueOf(num));
        params.put("id", (String) SharedPreferencesUtils.getParam(this, "id", ""));
        FinalHttp fhp = new FinalHttp();
        String url = "";
        if (type.equals("0")) {
            //积分
            url = MyConfig.POSTINTEGRALRECORDURL;
        } else {
            url = MyConfig.POSTHEATRECORDURL;
        }
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                showRe();
                super.onSuccess(s);
                Tools.setLog("获取积分请求成功:\n" + s);
                switch (fisResh) {
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
                    datas.clear();
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ary = object.getJSONArray("data");
                            if (type.equals("0")) {
                                if (datas != null) {
                                    datas.clear();
                                }
                                for (int i = 0; i < ary.length(); i++) {
                                    JSONObject ob = ary.getJSONObject(i);
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("time", Tools.getDates(Long.valueOf(ob.getString("time"))));
                                    map.put("type", ob.getString("type"));
                                    if (ob.getString("type").equals("0")) {
                                        map.put("jifen", ob.getString("integral"));
                                        map.put("xiaofen", ob.getString("exc_branch"));
                                    } else {
                                        map.put("jifen", ob.getString("integral"));
                                    }
                                    //执行完毕
                                    datas.add(map);
                                }
                            } else {
                                for (int i = 0; i < ary.length(); i++) {
                                    JSONObject ob = ary.getJSONObject(i);
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("time", Tools.getDates(Long.valueOf(ob.getString("time"))));
                                    map.put("type", ob.getString("type"));
                                    if (ob.getString("type").equals("0")) {
                                        //激励到账
                                        map.put("xiaofen", ob.getString("exc_branch"));
                                        map.put("jifen", ob.getString("heart"));
                                    } else {
                                        //增加积分
                                        map.put("jifen", ob.getString("heart"));
                                    }

                                    datas.add(map);
                                }

                            }
                            if (adapter != null) {
                                adapter = null;
                            }
                            adapter = new ListAdapter(getApplication(), datas);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 110:
                            Tools.setLog("没有记录");
                            showNodata();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("获取积分请求失败:" + strMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reData.StartDataRefresh(this, (String) SharedPreferencesUtils.getParam(getApplication(), "id", ""));
    }

    class ListAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, String>> datas;
        private HolderView holderview;

        public ListAdapter(Context context, List<Map<String, String>> datas) {
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

            if (convertView == null) {
                holderview = new HolderView();
                convertView = LayoutInflater.from(context).inflate(R.layout.myintegralitem, null);
                holderview.xiaofen = (TextView) convertView.findViewById(R.id.myintegralitem_xiaofen);
                holderview.jifen = (TextView) convertView.findViewById(R.id.myintegralitem_jifen);
                holderview.showTime = (TextView) convertView.findViewById(R.id.myintegralitem_showtime);
                holderview.icon = (ImageView) convertView.findViewById(R.id.myintegralitem_icon);
                holderview.heart_icon = (ImageView) convertView.findViewById(R.id.heart_icon);
                convertView.setTag(holderview);
            } else {
                holderview = (HolderView) convertView.getTag();
            }
            if (type.equals("0")) {
                holderview.icon.setBackgroundResource(R.drawable.integral_icon);
                if (datas.get(position).get("type").equals("0")) {
                    // Tools.showToast(getApplication(), "当前善分为" + datas.get(position).get("xiaofen"));
                    holderview.xiaofen.setText("激励到账:" + datas.get(position).get("xiaofen"));
                    holderview.jifen.setText(datas.get(position).get("jifen"));
                    holderview.heart_icon.setVisibility(View.VISIBLE);
                } else {
                    holderview.heart_icon.setVisibility(View.INVISIBLE);

                    holderview.xiaofen.setText("交易金额:" + datas.get(position).get("jifen"));
                    holderview.jifen.setText("+" + datas.get(position).get("jifen"));
                }
            } else {
                holderview.icon.setBackgroundResource(R.drawable.heart_icon);
                if (datas.get(position).get("type").equals("0")) {
                    // Tools.showToast(getApplication(), "当前善分为" + datas.get(position).get("xiaofen"));
                    holderview.xiaofen.setText("激励到账:" + datas.get(position).get("xiaofen"));
                    holderview.jifen.setText(datas.get(position).get("jifen"));
                    holderview.heart_icon.setVisibility(View.VISIBLE);
                } else {
                    holderview.heart_icon.setVisibility(View.INVISIBLE);
                    holderview.xiaofen.setText("交易金额:" + Tools.toStringTwo(String.valueOf((Float.valueOf(datas.get(position).get("jifen"))) * 300)));
                    holderview.jifen.setText("+" + datas.get(position).get("jifen"));
                }
            }
            holderview.showTime.setText(datas.get(position).get("time"));
            return convertView;
        }

        class HolderView {
            TextView xiaofen, jifen, showTime;
            ImageView icon, heart_icon;
        }
    }

}
