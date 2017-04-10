package com.bn.yfc.user;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.R;
import com.bn.yfc.base.OrderBean;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/19.
 */

public class UserAudit extends BaseActivity implements OnClickListener {
    int typeCode = 0;
    private int index = 10;
    int ALLCODE = 0;
    int GETONCODE = 1;
    int NEGOCODE = 2;
    int CRRAYOUTCODE = 3;
    int EVALUATECODE = 4;
    private ListView listView;
    private List<OrderBean> datas;
    private ListAdapter adapter;
    private Button aliibut, getonbut, negebut, crrayoutcode, evaluate;
    private TextView title;
    private ImageView left;
    private int itemCode;
    private RefreshLayout refresh;
    private Intent intent;
    private String urlHead = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useraudti);
        initHead();
        initView();
        initData();
        setPostData(0, false);
    }

    private void setPostData(final int i, final boolean ifs) {
        AjaxParams params = new AjaxParams();
        switch (typeCode) {
            case 0:
                params.put("status", "");
                Tools.setLog("全部订单");
                if (ifs) {

                } else {
                    datas.clear();
                }
                break;
            case 1:
                params.put("status", "2");
                Tools.setLog("进行中订单");
                if (ifs) {

                } else {
                    datas.clear();
                }
                break;
            case 2:
                params.put("status", "3");
                Tools.setLog("待协商订单");
                if (ifs) {

                } else {
                    datas.clear();
                }
                break;
            case 3:
                params.put("status", "1");
                Tools.setLog("已完成订单");
                if (ifs) {

                } else {
                    datas.clear();
                }
                break;
            case 4:
                params.put("status", "4");
                Tools.setLog("待评价订单");
                if (ifs) {

                } else {
                    datas.clear();
                }
                break;
        }
        params.put("uid", (String) SharedPreferencesUtils.getParam(this, "id", " "));
        params.put("num", String.valueOf(index));
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTORDERURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgressDialog();
                Tools.setLog("获取数据为\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONObject obs = object.getJSONObject("data");
                            JSONArray ary = obs.getJSONArray("xnb");
                            JSONObject obUrl = obs.getJSONObject("url");
                            urlHead = obUrl.getString("url");
                            if (datas == null) {
                                datas = new ArrayList<OrderBean>();
                            } else {
                                if (ifs) {
                                    if (datas.size() == ary.length()) {
                                        Tools.showToast(UserAudit.this, "没有更多数据了");
                                        closeRefreshs();
                                        return;
                                    }
                                } else {
                                    datas.clear();
                                }

                            }
                            if (datas != null) {
                                datas.clear();
                            }
                            for (int i = 0; i < ary.length(); i++) {
                                OrderBean bean = new OrderBean();
                                JSONObject ob = (JSONObject) ary.get(i);
                                bean.setId(ob.getString("id"));
                                bean.setName(ob.getString("name"));
                                bean.setStatus(ob.getString("status"));
                                bean.setBoss_tel(ob.getString("boss_tel"));
                                if (Tools.isEmpty(ob.getString("send_money"))) {
                                    bean.setSend_money("0");
                                } else {
                                    bean.setSend_money(ob.getString("send_money"));
                                }

                                bean.setSend_tel(ob.getString("send_tel"));
                                bean.setMoney(ob.getString("money"));
                                //时间戳，转义
                                long a = Long.parseLong(ob.getString("time"));
                                Tools.setLog("转义前的时间戳" + a);
                                bean.setTime(Tools.getDate(a));
                                switch (ob.getString("type")) {
                                    case "1":
                                        bean.setType("1");
                                        break;
                                    case "2":
                                        bean.setType("2");
                                        break;
                                    case "3":
                                        bean.setType("3");
                                        break;
                                    case "4":
                                        bean.setType("4");
                                        break;

                                }
                                Tools.setLog("保险费");
                                if (Tools.isEmpty(ob.getString("protect_money"))) {
                                    bean.setProtect_money("0");
                                } else {
                                    bean.setProtect_money(ob.getString("protect_money"));
                                }

                                Tools.setLog("保险费完");
                                datas.add(bean);
                            }
                            if (ifs) {
                                //封装数据后定位
                                listView.setAdapter(adapter);
                                listView.setSelection(i);
                                closeRefreshs();
                            }

                            adapter.setDatas(datas);
                            closeRefresh();

                            break;
                        case 110:
                            Tools.showToast(getApplication(), "暂无更多订单");
                            if (ifs) {
                                closeRefreshs();
                            } else {
                                closeRefresh();
                            }
                            adapter.setDatas(datas);
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
                Tools.showToast(getApplication(), "网络错误");
            }
        });
    }

    private void initHead() {
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("我的订单");
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
    }

    private void closeRefreshs() {
        refresh.setLoading(false);
    }

    private void closeRefresh() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.useraudit_list);
        refresh = (RefreshLayout) findViewById(R.id.useraudit_refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        aliibut = (Button) findViewById(R.id.useraudit_all);
        aliibut.setOnClickListener(this);
        getonbut = (Button) findViewById(R.id.useraudit_geton);
        getonbut.setOnClickListener(this);
        negebut = (Button) findViewById(R.id.useraudit_nego);
        negebut.setOnClickListener(this);
        evaluate = (Button) findViewById(R.id.useraudit_evaluate);
        evaluate.setOnClickListener(this);
        crrayoutcode = (Button) findViewById(R.id.useraudit_crrayout);
        crrayoutcode.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.post(new Runnable() {
                    @Override
                    public void run() {

                        setPostData(0, false);
                    }
                });
            }
        });
        refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (datas.size() < 6) {
                            closeRefreshs();
                            return;
                        }
                        index += 5;
                        listJump(listView.getChildCount());
                    }
                }, 0);
            }
        });
    }


    private void listJump(int i) {
        setPostData(i, true);
    }

    private void initData() {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        datas = new ArrayList<OrderBean>();
        adapter = new ListAdapter(this, datas);
        listView.setAdapter(adapter);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void selectType() {
        switch (typeCode) {
            case 0:
                aliibut.setBackgroundResource(R.drawable.audit_but_onclick);
                aliibut.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                getonbut.setBackground(null);
                getonbut.setTextColor(getResources().getColor(R.color.colorBlack));
                negebut.setBackground(null);
                negebut.setTextColor(getResources().getColor(R.color.colorBlack));
                crrayoutcode.setBackground(null);
                crrayoutcode.setTextColor(getResources().getColor(R.color.colorBlack));
                evaluate.setBackground(null);
                evaluate.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case 1:
                aliibut.setBackground(null);
                aliibut.setTextColor(getResources().getColor(R.color.colorBlack));
                getonbut.setBackgroundResource(R.drawable.audit_but_onclick);
                getonbut.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                negebut.setBackground(null);
                negebut.setTextColor(getResources().getColor(R.color.colorBlack));
                crrayoutcode.setBackground(null);
                crrayoutcode.setTextColor(getResources().getColor(R.color.colorBlack));
                evaluate.setBackground(null);
                evaluate.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case 2:
                aliibut.setBackground(null);
                aliibut.setTextColor(getResources().getColor(R.color.colorBlack));
                negebut.setBackgroundResource(R.drawable.audit_but_onclick);
                negebut.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                getonbut.setBackground(null);
                getonbut.setTextColor(getResources().getColor(R.color.colorBlack));
                crrayoutcode.setBackground(null);
                crrayoutcode.setTextColor(getResources().getColor(R.color.colorBlack));
                evaluate.setBackground(null);
                evaluate.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case 3:
                crrayoutcode.setBackgroundResource(R.drawable.audit_but_onclick);
                crrayoutcode.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                getonbut.setBackground(null);
                getonbut.setTextColor(getResources().getColor(R.color.colorBlack));
                negebut.setBackground(null);
                negebut.setTextColor(getResources().getColor(R.color.colorBlack));
                aliibut.setBackground(null);
                aliibut.setTextColor(getResources().getColor(R.color.colorBlack));
                evaluate.setBackground(null);
                evaluate.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case 4:
                crrayoutcode.setBackground(null);
                crrayoutcode.setTextColor(getResources().getColor(R.color.colorBlack));
                getonbut.setBackground(null);
                getonbut.setTextColor(getResources().getColor(R.color.colorBlack));
                negebut.setBackground(null);
                negebut.setTextColor(getResources().getColor(R.color.colorBlack));
                aliibut.setBackground(null);
                aliibut.setTextColor(getResources().getColor(R.color.colorBlack));
                evaluate.setBackgroundResource(R.drawable.audit_but_onclick);
                evaluate.setTextColor(getResources().getColor(R.color.colorSkyBlue));
                break;
        }
        setPostData(0, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.useraudit_all:
                typeCode = ALLCODE;
                selectType();
                break;
            case R.id.useraudit_geton:
                typeCode = GETONCODE;
                selectType();
                break;
            case R.id.useraudit_nego:
                typeCode = NEGOCODE;
                selectType();
                break;
            case R.id.useraudit_crrayout:
                typeCode = CRRAYOUTCODE;
                selectType();
                break;
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.useraudit_evaluate:
                typeCode = EVALUATECODE;
                selectType();
                break;
        }
    }

    class ListAdapter extends BaseAdapter {
        Context context;
        List<OrderBean> datas;

        public ListAdapter(Context context, List<OrderBean> datas) {
            this.context = context;
            this.datas = datas;
        }

        public void setDatas(List<OrderBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final HolderView holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.useraudititem, null);
                holder = new HolderView();
                holder.itemlin = (LinearLayout) view.findViewById(R.id.useraudit_itemlin);
                holder.itemstatus_but = (Button) view.findViewById(R.id.useraudit_itemstatus_but);
                holder.itemtime = (TextView) view.findViewById(R.id.useraudit_itemtime);
                holder.itemname = (TextView) view.findViewById(R.id.useraudit_itemname);
                holder.itemstatus = (Button) view.findViewById(R.id.useraudit_itemstatus);
                holder.itemfreihght = (TextView) view.findViewById(R.id.useraudit_itemfreihght);
                holder.itemmoney = (TextView) view.findViewById(R.id.useraudit_itemmoney);
                holder.itemnumber = (TextView) view.findViewById(R.id.useraudit_number);
                holder.itemphone = (TextView) view.findViewById(R.id.useraudit_itemphone);
                holder.itemtype = (TextView) view.findViewById(R.id.useraudit_itemtype);

                view.setTag(holder);
            } else {
                holder = (HolderView) view.getTag();
            }
            holder.itemstatus.setVisibility(View.INVISIBLE);
            holder.itemstatus_but.setText("订单操作");
            holder.itemstatus_but.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(UserAudit.this, OrdersTest.class);
                    intent.putExtra("number", datas.get(i).getId());
                    intent.putExtra("url", urlHead);
                    startActivity(intent);
                }
            });
            holder.itemtime.setText(datas.get(i).getTime());
            holder.itemname.setText(datas.get(i).getName());
            float aa = Float.valueOf(datas.get(i).getProtect_money()) + Float.valueOf(datas.get(i).getSend_money());
            CharSequence cs = String.valueOf(aa);
            String pri = Tools.selectStringTwo(cs);
            holder.itemfreihght.setText("");
            holder.itemmoney.setText(pri + " 元");
            holder.itemnumber.setText("商品价格" + datas.get(i).getMoney() + "元");
            if (datas.get(i).getType().equals("3")) {
                holder.itemphone.setText("客户电话: " + datas.get(i).getBoss_tel());
            } else {
                holder.itemphone.setText("客户电话: " + datas.get(i).getSend_tel());
            }
            switch (datas.get(i).getType()) {
                case "1":
                    holder.itemtype.setText("快递");
                    break;
                case "2":
                    holder.itemtype.setText("顺路送");
                    break;
                case "3":
                    holder.itemtype.setText("帮我买");
                    break;
                case "4":
                    holder.itemtype.setText("船运");
                    break;
            }
            holder.itemlin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Tools.showToast(getApplication(), "跳转到详情界面");
                }
            });
            return view;
        }


        class HolderView {
            Button itemstatus_but, itemstatus;
            TextView itemtime, itemname, itemtype, itemnumber, itemphone, itemmoney, itemfreihght;
            LinearLayout itemlin;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas = null;
        }

    }
}
