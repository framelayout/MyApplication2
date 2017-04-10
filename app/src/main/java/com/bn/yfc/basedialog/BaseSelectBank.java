package com.bn.yfc.basedialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.buy.Exchange;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.AddBank;
import com.bn.yfc.user.Balance2;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BaseSelectBank extends DialogFragment implements View.OnClickListener {

    private View view;
    private ListView listview;
    private TextView addbank;
    private ListViewAdapter adapter;
    private List<Map<String, String>> datas;
    private Context context;
    private Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.selectbankdialog, null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        datas = new ArrayList<>();
        adapter = new ListViewAdapter(context, datas);
        listview.setAdapter(adapter);
        sendListData();
    }

    private void sendListData() {
        //请求数据
        Tools.setLog("请求银行卡数据");
        RequestParams params = new RequestParams(MyConfig.POSTSHOWBANKURL);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("uid", (String) SharedPreferencesUtils.getParam(getActivity(), "id", " "));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    switch (object.getInt("code")) {
                        case 1:
                            Tools.showToast(getActivity(), "获取银行卡成功\n");
                            setDataToView(object.getJSONArray("data"));
                            break;
                        case 110:
                            Tools.showToast(getActivity(), "获取失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Tools.showToast(getActivity(), "网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setDataToView(JSONArray ary) {
        if (datas != null) {
            datas.clear();
            datas = null;
        }
        datas = new ArrayList<Map<String, String>>();
        for (int i = 0; i < ary.length(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            try {
                JSONObject ob = ary.getJSONObject(i);
                String bankId = ob.getString("id");
                String bankNumber = ob.getString("bank");
                String banAddress = ob.getString("card");
                String bankType = ob.getString("user");
                String nickName = ob.getString("pos");
                map.put("bankId", bankId);
                map.put("bankNumber", bankNumber);
                map.put("bankAddress", banAddress);
                map.put("bankType", bankType);
                map.put("bankName", nickName);
                map.put("status", "1");
                datas.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.setDatas(datas);
        }

    }

    private void initStatus() {
        int c = datas.size();
        for (int i = 0; i < c; i++) {
            datas.get(i).put("status", "1");
        }

    }

    private void initView(View view) {
        addbank = (TextView) view.findViewById(R.id.selectbank_addbank);
        listview = (ListView) view.findViewById(R.id.selectbank_listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initStatus();
                datas.get(position).put("status", "0");
                adapter.setImagBack(position);
                Tools.showToast(context, "选中的银行卡卡号:" + datas.get(position).get("bankNumber"));
                selectBankInfo(datas.get(position).get("bankNumber"), datas.get(position).get("bankType"), datas.get(position).get("bankName"));

                dismiss();
            }
        });
        addbank.setOnClickListener(this);
    }

    private void selectBankInfo(String num, String type, String userName) {
        ((Exchange) context).BankInfo(num, type, userName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectbank_addbank:
                intent = new Intent(getActivity(), AddBank.class);
                startActivity(intent);
                dismiss();
                break;
        }
    }

    class ListViewAdapter extends BaseAdapter {
        List<Map<String, String>> datas;
        Context context;
        HolderView holderView;

        public void setDatas(List<Map<String, String>> datas) {
            this.datas = datas;
            notifyDataSetChanged();

        }

        public ListViewAdapter(Context context, List<Map<String, String>> datas) {
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

        public void setImagBack(int postion) {
            if (datas.get(postion).get("status").equals("0")) {
                holderView.status.setVisibility(View.VISIBLE);
            } else {
                holderView.status.setVisibility(View.INVISIBLE);
            }
            notifyDataSetChanged();
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
                holderView = new HolderView();
                convertView = LayoutInflater.from(context).inflate(R.layout.selectbankitem, null);
                holderView.bankInfo = (TextView) convertView.findViewById(R.id.selectbank_bankinfo);
                holderView.status = (ImageView) convertView.findViewById(R.id.selectbank_status);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }
            if (datas.size() < 0) {

            } else {
                if (datas.get(position).get("status").equals("0")) {
                    holderView.status.setVisibility(View.VISIBLE);
                } else {
                    holderView.status.setVisibility(View.INVISIBLE);
                }
                holderView.bankInfo.setText(datas.get(position).get("bankType") + datas.get(position).get("bankNumber"));
            }
            return convertView;
        }

        class HolderView {
            TextView bankInfo;
            ImageView status;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        int dialogWidth = (int) (width * 0.8);
        int dialogHeight = (int) (height * 0.6);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
}
