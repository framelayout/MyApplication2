package com.bn.yfc.buy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.InviteInterface;
import com.bn.yfc.user.Login;
import com.bn.yfc.user.Permissions;
import com.bn.yfc.wxapi.WXShare;

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
 * Created by Administrator on 2017/1/16.
 */

public class Chain extends Fragment implements OnClickListener {

    private View view;
    private ImageView left, right;
    private TextView title;
    private ListView listview;
    private List<Map<String, String>> datas;
    private Adapter adapter;
    private RefreshData redata = new RefreshData(getActivity());
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chain, null);
        initData();
        initHead(view);
        iniView(view);
        sendData();
        return view;
    }

    private void initData() {
        datas = new ArrayList<>();
        adapter = new Adapter(getActivity(), datas);

    }

    private void iniView(View view) {
        listview = (ListView) view.findViewById(R.id.chain_listview);
        listview.setAdapter(adapter);
    }

    private void initHead(View view) {
        left = (ImageView) view.findViewById(R.id.head_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.head_title);
        title.setText("合作商家");
        right = (ImageView) view.findViewById(R.id.head_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        setRightVew();
    }

    private void setDatas(List<Map<String, String>> datas) {
        adapter.setDataAdapter(datas);
        if (adapter != null) {
            adapter = null;
        }
        adapter = new Adapter(getActivity(), datas);
        listview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                break;
            case R.id.head_right:
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), NewsInterface.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setRightVew();
    }

    private void setRightVew() {
        if (Permissions.userLoginPermissions(getActivity())) {
            if (Permissions.newsDataPermissions(getActivity())) {
                right.setBackgroundResource(R.drawable.newshave_icon);
            } else {
                right.setBackgroundResource(R.drawable.news_icon);
            }
        }
    }

    private void sendData() {
        AjaxParams params = new AjaxParams();
        String url = MyConfig.POSTGETFIRESNDURL;
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("请求合作商数据成功==" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ary = object.getJSONArray("data");
                            JSONObject ob = object.getJSONObject("url");
                            String iconUrl = ob.getString("url");
                            Tools.setLog("数据大小为" + ary.length());
                            for (int i = 0; i < ary.length(); i++) {
                                Map<String, String> map = new ArrayMap<String, String>();
                                JSONObject ob1 = ary.getJSONObject(i);
                                map.put("icon", iconUrl + ob1.getString("logo"));
                                Tools.setLog("icon --" + map.get("icon"));
                                map.put("name", ob1.getString("name"));
                                Tools.setLog("name --" + map.get("name"));
                                map.put("url", ob1.getString("url"));
                                Tools.setLog("url --" + map.get("url"));
                                datas.add(map);
                            }
                            Tools.setLog("判断循环是否有效");
                            setDatas(datas);
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
                Tools.setLog("请求合作商数据失败" + strMsg);
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

        public void setDataAdapter(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.chain_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.chainitem_icon);
                holder.name = (TextView) convertView.findViewById(R.id.chainitem_name);
                holder.in = (LinearLayout) convertView.findViewById(R.id.chainitem_lin);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            x.image().bind(holder.icon, datas.get(position).get("icon"));
            holder.icon.setBackgroundResource(R.drawable.umeng_socialize_menu_default);
            holder.name.setText(datas.get(position).get("name"));
            holder.in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(datas.get(position).get("url"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (datas != null) {
            datas.clear();
            datas = null;
        }
    }
}

