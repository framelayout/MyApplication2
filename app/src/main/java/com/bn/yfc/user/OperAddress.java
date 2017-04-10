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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/8.
 */

public class OperAddress extends BaseActivity implements OnClickListener {
    private String selecKey = "capital";
    private List<Map<String, String>> capitalData = new ArrayList<>();
    private List<Map<String, String>> cityData = new ArrayList<>();
    private List<Map<String, String>> districtData = new ArrayList<>();
    private ListView showList;
    private TextView complete;
    private Button left;
    private TextView title;
    private ListAdapter adapter;
    private String capita = null;
    private String city = null;
    private String district = null;
    private String capitaId = null;
    private String cityId = null;
    private String districtId = null;
    private EditText address;
    private String moneys;
    private String selectAddress = null;
    String getID = null;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operaddress);
        initData();
        initHead();
        initView();
        getData(selecKey, "");
    }

    private void initData() {
        if (getIntent().getStringExtra("type") != null) {
            if (getIntent().getStringExtra("type").equals("1")) {
                type = "1";
            }
        }

    }

    private void initView() {
        address = (EditText) findViewById(R.id.operaddress_selectAddress);
        showList = (ListView) findViewById(R.id.operaddress_list);
        if (adapter == null) {
            adapter = new ListAdapter(this, capitalData);
        }
        showList.setAdapter(adapter);
    }

    private void initHead() {
        left = (Button) findViewById(R.id.opertionaddress_left);
        left.setOnClickListener(this);
        complete = (TextView) findViewById(R.id.opertionaddress_complete);
        complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.opertionaddress_left:
                if (selecKey.equals("capital")) {
                    finish();
                } else if (selecKey.equals("city")) {
                    selecKey = "capital";
                    address.setText("您的选择为: " + capita);
                    getData(selecKey, capitaId);
                } else if (selecKey.equals("district")) {
                    address.setText("您的选择为: " + capita + "-" + city);
                    selecKey = "city";
                    getData(selecKey, capitaId);
                }
                setLeftView();
                break;
            case R.id.opertionaddress_complete:
                String id = null;
                id = getID;
                Intent intent = new Intent();
                intent.putExtra("money", moneys);
                intent.putExtra("address", selectAddress);
                intent.putExtra("id", id);
                setResult(13, intent);
                finish();
                break;
        }
    }

    private void setLeftView() {
        if (selecKey.equals("capital")) {
            left.setBackgroundResource(R.drawable.return_icons);
            left.setText("");
        } else if (selecKey.equals("city")) {
            left.setBackgroundResource(R.color.colorTransparemt);
            left.setText("上一步");
        } else if (selecKey.equals("district")) {
            left.setBackgroundResource(R.color.colorTransparemt);
            left.setText("上一步");
        }
    }


    //设置list显示
    private void setListData(List<Map<String, String>> datas) {
        if (adapter == null) {
            adapter = new ListAdapter(this, datas);
        }
        adapter.setAdapters(datas);
    }

    class ListAdapter extends BaseAdapter {

        List<Map<String, String>> datas;
        Context context;

        public void setAdapters(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public ListAdapter(Context context, List<Map<String, String>> datas) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.operitem, null);
                holder.prix = (TextView) convertView.findViewById(R.id.operitem_prix);
                holder.titles = (TextView) convertView.findViewById(R.id.operitem_title);
                holder.sele = (RelativeLayout) convertView.findViewById(R.id.operitem_select);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.titles.setText(datas.get(position).get("name"));
            if (type != null && type.equals("1")) {
                holder.prix.setText("");
            } else {
                holder.prix.setText(datas.get(position).get("money"));
            }
            holder.sele.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    moneys = datas.get(position).get("money");
                    switch (selecKey) {
                        case "capital":
                            capitaId = datas.get(position).get("id");
                            capita = datas.get(position).get("name");
                            city = "";
                            district = "";
                            selectAddress = capita;
                            address.setText("您的选择为: " + selectAddress);
                            selecKey = "city";
                            getID = capitaId;
                            break;
                        case "city":
                            cityId = datas.get(position).get("id");
                            city = datas.get(position).get("name");
                            district = "";
                            selectAddress = capita + "-" + city;
                            address.setText("您的选择为: " + selectAddress);
                            selecKey = "district";
                            getID = cityId;
                            break;
                        case "district":
                            districtId = datas.get(position).get("id");
                            district = datas.get(position).get("name");
                            selectAddress = capita + "-" + city + "-" + district;
                            address.setText("您的选择为: " + selectAddress);
                            getID = districtId;
                            break;
                    }
                    getData(selecKey, getID);
                    setLeftView();
                }
            });

            return convertView;
        }

        class Holder {
            TextView titles;
            TextView prix;
            RelativeLayout sele;
        }
    }

    private void getData(String key, String id) {
        AjaxParams params = new AjaxParams();
        String url = "";
        switch (key) {
            case "capital":
                url = MyConfig.POSTSHENGURL;
                break;
            case "city":
                url = MyConfig.POSTSHIURL;
                params.put("id", id);
                break;
            case "district":
                url = MyConfig.POSTQUURL;
                params.put("id", id);
                break;
        }
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("请求失败====" + strMsg);
                closeProgressDialog();
                Tools.showToast(OperAddress.this, "网络错误");
            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                closeProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ary = object.getJSONArray("data");
                            switch (selecKey) {
                                case "capital":
                                    if (capitalData != null) {
                                        capitalData.clear();
                                        capitalData = null;
                                    }
                                    capitalData = comData(ary,
                                            capitalData);
                                    setListData(capitalData);
                                    break;
                                case "city":
                                    if (capitalData != null) {
                                        capitalData.clear();
                                        capitalData = null;
                                    }
                                    capitalData = comData(ary,
                                            capitalData);
                                    setListData(capitalData);
                                    break;
                                case "district":
                                    if (capitalData != null) {
                                        capitalData.clear();
                                        capitalData = null;
                                    }
                                    capitalData = comData(ary,
                                            capitalData);
                                    setListData(capitalData);
                                    break;
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private List<Map<String, String>> comData(JSONArray ary, List<Map<String, String>> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        for (int i = 0; i < ary.length(); i++) {
            JSONObject ob = null;
            try {
                ob = (JSONObject) ary.get(i);
                Map<String, String> dat = new HashMap<>();
                dat.put("name", ob.getString("name"));
                dat.put("id", ob.getString("id"));
                dat.put("pid", ob.getString("pid"));
                dat.put("money", ob.getString("money"));
                dat.put("status", ob.getString("status"));
                datas.add(dat);
            } catch (JSONException e) {
                e.printStackTrace();
                Tools.setLog("设置数据源时产生异常" + e.getMessage());
            }
        }
        return datas;
    }
}
