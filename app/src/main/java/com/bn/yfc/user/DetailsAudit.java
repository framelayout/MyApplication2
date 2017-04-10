package com.bn.yfc.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;

/**
 * Created by Administrator on 2017/1/9.
 */

public class DetailsAudit extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsaudit);
        initData();
    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

    }
}
