package com.bn.yfc.buy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseSearchEditText;
import com.bn.yfc.base.RefreshLayout;
import com.bn.yfc.base.SearchStart;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.user.Permissions;

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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Administrator on 2017/3/10.
 */

public class Chains1 extends Fragment implements View.OnClickListener {
    private View view;
    private BaseSearchEditText search;
    private RefreshLayout refresh;
    private ListView listview;
    private ListAdapter adapter;
    private List<Map<String, String>> datas;
    private LinearLayout select;
    private Intent intent;
    private TextView seachText;
    private int num = 10;
    private int reCode = 0;
    private LinearLayout noData;
    private List<Map<String, String>> datasa = new ArrayList<>();
    private ListView listviews;
    private PopListViewAdapter adapters;
    private PopupWindow popuiwindow;
    private TextView selectaddress;
    private int seachCode = 0;
    private String seachName = "";
    private String selecQu = "";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Tools.setLog("回调重装");
                    adapters.PopListViewData(datasa);
                    if (popuiwindow != null) {
                        Tools.setLog("pop不为空");
                    }
                    popuiwindow.showAsDropDown(select);
                    break;
            }
        }
    };

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chains, null);
        context = getActivity();
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        datas = new ArrayList<>();
        sendStoreData(selecQu);
    }

    //获取区
    private void sendQU(final PopupWindow popuiwindows) {
        AjaxParams params = new AjaxParams();
        String sheng = (String) SharedPreferencesUtils.getParam(getActivity(), "sheng", "");
        if (Tools.isEmpty(sheng)) {
            Tools.showToast(getActivity(), "请获取定位权限");
            return;
        }
        sheng = sheng.substring(0, sheng.length() - 1);
        params.put("sheng", sheng);
        String city = (String) SharedPreferencesUtils.getParam(getActivity(), "city", "");
        city = city.substring(0, city.length() - 1);
        params.put("shi", city);
        FinalHttp fhp = new FinalHttp();
        fhp.post(MyConfig.POSTQUSURL, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Tools.setLog("获取区数据成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {

                        case 1:
                            JSONObject obs = object.getJSONObject("data");
                            JSONArray ary = obs.getJSONArray("name");
                            if (datasa != null) {
                                datasa.clear();
                            }
                            for (int i = 0; i < ary.length(); i++) {
                                Map<String, String> data = new HashMap<String, String>();
                                data.put("city", ary.getString(i));
                                datasa.add(data);
                            }
                            Tools.setLog("区数据解析成功" + datasa.get(0).get("city"));
                            Tools.setLog("区数据解析成功1");
                            handler.sendEmptyMessage(0);
                            break;
                        case 111:
                            Tools.showToast(getActivity(), "当前地区没有数据");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("获取区数据失败" + strMsg);
            }
        });
    }

    //获取商户数据
    private void sendStoreData(String qus) {
        String url = "";
        AjaxParams params = new AjaxParams();
        String sheng = (String) SharedPreferencesUtils.getParam(getActivity(), "sheng", "");
        if (Tools.isEmpty(sheng)) {
            Tools.showToast(getActivity(), "请获取定位权限");
            return;
        }
        sheng = sheng.substring(0, sheng.length() - 1);
        String city = (String) SharedPreferencesUtils.getParam(getActivity(), "city", "");
        city = city.substring(0, city.length() - 1);
        if (Tools.isEmpty(qus)) {
            String qu = (String) SharedPreferencesUtils.getParam(getActivity(), "qu", "");
            qus = qu.substring(0, qu.length() - 1);
        } else {
            String qu = qus;
            qus = qu.substring(0, qus.length() - 1);
        }
        if (seachCode == 0) {
            url = MyConfig.POSTSEASTOREDATAURL;

        } else {
            url = MyConfig.POSTSEACHSTORENAMEURL;
            params.put("name", seachName);
        }
        params.put("sheng", sheng);
        params.put("shi", city);
        params.put("qu", qus);
        params.put("num", String.valueOf(num));
        Tools.setLog("省=" + sheng + "|市=" + city + "|区=" + qus);
        FinalHttp fhp = new FinalHttp();
        fhp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {

                super.onSuccess(s);
                showRe();
                switch (reCode) {
                    case 0:
                        Tools.setLog("啥也不做");
                        break;
                    case 1:
                        closeRefresh();
                        break;
                    case 2:
                        closeRefreshs();
                        break;
                }

                Tools.setLog("请求商户列表成功\n" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            JSONArray ary = object.getJSONArray("data");
                            JSONObject urlObject = object.getJSONObject("url");
                            String urlHead = urlObject.getString("url");
                            if (datas != null) {
                                datas.clear();
                            }
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject ob1 = ary.getJSONObject(i);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("address", ob1.getString("pk_qu"));
                                map.put("name", ob1.getString("pk_companyname"));
                                map.put("icon", urlHead + ob1.getString("pk_storelog"));
                                map.put("id", ob1.getString("id"));
                                datas.add(map);
                            }
                            Tools.setLog("解析成功");
                            if (adapter != null) {
                                adapter = null;
                            }
                            adapter = new ListAdapter(getActivity(), datas);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 110:
                            showNoData();
                            Tools.showToast(context, "没有该地区的商户");
                            break;
                        case 111:
                            showNoData();
                            Tools.showToast(context, "没有该地区的商户");

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("请求商户列表失败\n" + strMsg);
                showNoData();
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
            }
        });
    }

    private void showNoData() {
        noData.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
    }

    private void showRe() {
        refresh.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }

    private void initView(View v) {
        initPopWindow();
        selectaddress = (TextView) v.findViewById(R.id.chains_selectaddress);
        noData = (LinearLayout) view.findViewById(R.id.nodata);
        refresh = (RefreshLayout) v.findViewById(R.id.chains_refresh);
        setRefresh();
        seachText = (TextView) v.findViewById(R.id.chains_search_text);
        seachText.setOnClickListener(this);
        select = (LinearLayout) v.findViewById(R.id.chains_select);
        select.setOnClickListener(this);
        listview = (ListView) v.findViewById(R.id.chains_listview);
        search = (BaseSearchEditText) v.findViewById(R.id.chains_search);
        search.setSeachStartLinstener(new SearchStart() {
            @Override
            public void SearchStart(boolean starts) {
                if (starts) {
                    Tools.showToast(getActivity(), "开始搜索，内容为" + search.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } else {

                }
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Tools.setLog("判断文字beforeTextChanged" + s.toString() + "|" + start + "|" + count + "|" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0 || count > 0) {
                    Tools.setLog("显示");
                    seachText.setVisibility(View.VISIBLE);
                } else {
                    Tools.setLog("隐藏");
                    seachText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Permissions.userLoginPermissions(getActivity())) {
                    intent = new Intent(getActivity(), StoreDetails.class);
                    intent.putExtra("id", datas.get(position).get("id"));
                    startActivity(intent);
                } else {
                    Tools.showToast(getActivity(), "请先登录");
                }

            }
        });
    }

    private void setRefresh() {
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reCode = 1;
                sendStoreData(selecQu);
            }
        });
        refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                //加载
                reCode = 2;
                if (datas.size() < 6) {
                    closeRefreshs();
                    return;
                }
                sendStoreData(selecQu);
            }
        });
    }

    //关闭加载
    private void closeRefreshs() {

        refresh.setLoading(false);
    }

    //关闭刷新
    private void closeRefresh() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        Tools.setLog("fan", "store onStart");

        //是否登录
        if (Permissions.userLoginPermissions(getActivity())) {
            //是否有新闻
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chains_select:
                Tools.setLog("设置筛选列表");
                showPopupWindow();
                break;
            case R.id.chains_search_text:
                //搜索
                seachName = search.getText().toString();
                if (Tools.isEmpty(seachName)) {
                    return;
                }
                seachCode = 1;
                sendStoreData(selecQu);
                break;
        }
    }

    class ListAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, String>> datas;

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
            HolderView view;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chains_item, null);
                view = new HolderView();
                view.addres = (TextView) convertView.findViewById(R.id.chains_address);
                view.name = (TextView) convertView.findViewById(R.id.chains_name);
                view.icon = (ImageView) convertView.findViewById(R.id.chains_icon);
                convertView.setTag(view);
            } else {
                view = (HolderView) convertView.getTag();
            }
            view.addres.setText(datas.get(position).get("address"));
            view.name.setText(datas.get(position).get("name"));
            x.image().bind(view.icon, datas.get(position).get("icon"));
            return convertView;
        }

        class HolderView {
            TextView name, addres;
            ImageView icon;
        }

    }


    private void initPopWindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chains_popwin, null);
        listviews = (ListView) view.findViewById(R.id.chains_poplistview);
        popuiwindow = new PopupWindow(view, MATCH_PARENT, WRAP_CONTENT);
        popuiwindow.setFocusable(true);
        popuiwindow.setBackgroundDrawable(new BitmapDrawable());
        popuiwindow.setContentView(view);
        if (datasa != null) {
            datasa.clear();
        }
        adapters = new PopListViewAdapter(datasa, getActivity());
        listviews.setAdapter(adapters);
        listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView city = (TextView) view.findViewById(R.id.chains_popwinsitem_city);
                selecQu = city.getText().toString();
                Tools.showToast(getActivity(), selecQu);
                selectaddress.setText(selecQu);
                seachCode = 0;
                sendStoreData(selecQu);
                popuiwindow.dismiss();
            }
        });
    }

    /*showwindow*/
    private void showPopupWindow() {
        sendQU(popuiwindow);
    }

    class PopListViewAdapter extends BaseAdapter {
        List<Map<String, String>> popData;
        Context context;

        public void PopListViewData(List<Map<String, String>> popData) {
            this.popData = popData;

            notifyDataSetChanged();
        }


        public PopListViewAdapter(List<Map<String, String>> popData, Context context) {
            this.popData = popData;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (popData.size() > 0) {
                return popData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return popData.get(position);
        }

        @Override
        public long getItemId(int position)

        {
            Tools.setLog("执行了getItemId");
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Tools.setLog("执行getView方法");
            HolderView holderView;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chains_popwinsitem, null);
                holderView = new HolderView();
                holderView.city = (TextView) convertView.findViewById(R.id.chains_popwinsitem_city);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }

            holderView.city.setText(popData.get(position).get("city"));
            return convertView;
        }

        class HolderView {
            TextView city;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.setLog("fan", "store onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.setLog("fan", "storeResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.setLog("fan", "onDetroyView");
        if (datas != null) {
            datas.clear();
        }
    }
}
