package com.bn.yfc.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bn.yfc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/14.
 */

public class Test extends BaseActivity {
    ListView listview;
    Adapter adapter;
    List<OrderBean> datas;
    RefreshLayout refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initData();
        initView();

    }

    private void initData() {
        datas = new ArrayList<OrderBean>();
        for (int i = 0; i < 16; i++) {
            OrderBean bean = new OrderBean();
            bean.setType("开始旋转");
            datas.add(bean);
        }
        adapter = new Adapter(this, datas);
    }

    private void initView() {

        listview = (ListView) findViewById(R.id.test_list);
        listview.setAdapter(adapter);
        refresh = (RefreshLayout) findViewById(R.id.refretest);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorBlue), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.post(new Runnable() {
                    @Override
                    public void run() {


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

                    }
                }, 0);
            }
        });
    }

    class Adapter extends BaseAdapter {

        Context context;
        List<OrderBean> datas;

        public Adapter(Context context, List<OrderBean> datas) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holde;
            if (convertView == null) {
                holde = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.testpost, null);
                holde.text = (TextView) convertView.findViewById(R.id.testpost_hint);
                convertView.setTag(holde);
            } else {
                holde = (Holder) convertView.getTag();
            }
            holde.text.setText(datas.get(position).getType());
            return convertView;
        }

        class Holder {
            TextView text;
        }
    }
}
