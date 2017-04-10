package com.bn.yfc.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/15.
 */

public class InviteInterface extends BaseActivity implements View.OnClickListener {
    private Adapter adapter;
    private ImageView left;
    private TextView title;
    private List<Map<String, String>> datas;
    private LinearLayout hint;
    private ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inviteinterface);
        initHead();
        initData();
        initView();
    }

    private void initData() {
        datas = new ArrayList<Map<String, String>>();
        sendData();
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("已邀请用户");
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.invite_list);
        listview.setAdapter(adapter);
        hint = (LinearLayout) findViewById(R.id.inviteinterface_hint);

    }

    private void setListview(List<Map<String, String>> datas) {
        if (adapter != null) {
            adapter = null;
        }
        adapter = new Adapter(this, datas);
        listview.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
        }
    }


    private void sendData() {
        AjaxParams params = new AjaxParams();
        params.put("id", (String) SharedPreferencesUtils.getParam(this, "id", ""));
        params.put("type", "1");
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTMYSONSURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("获取邀请人列表\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ob1 = object.getJSONArray("xnb");
                            JSONObject headUrl = object.getJSONObject("url");
                            String url = headUrl.getString("url");
                            for (int i = 0; i < ob1.length(); i++) {
                                String icon = url + ob1.getJSONObject(i).getString("url");
                                String name = ob1.getJSONObject(i).getString("nickname");
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("icon", icon);
                                map.put("name", name);
                                datas.add(map);
                            }
                            setListview(datas);
                            break;
                        case 110:
                            //暂无邀请人
                            hint.setVisibility(View.VISIBLE);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("获取邀请人列表失败" + strMsg);
            }
        });

    }

    class Adapter extends BaseAdapter {

        Context context;
        List<Map<String, String>> datas;

        public Adapter(Context context, List<Map<String, String>> datas) {
            this.context = context;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.inviteitem, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.inviteitem_icon);
                holder.name = (TextView) convertView.findViewById(R.id.inviteitem_name);
                holder.in = (LinearLayout) convertView.findViewById(R.id.inviteitem_lin);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.icon.setBackgroundResource(R.drawable.user_icon_main);
            holder.name.setText(datas.get(position).get("name"));
            holder.in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.showToast(getApplication(), "这个用户是" + datas.get(position).get("name"));
                }
            });
            return convertView;
        }

        class Holder {
            LinearLayout in;
            ImageView icon;
            TextView name;
        }
    }
}
