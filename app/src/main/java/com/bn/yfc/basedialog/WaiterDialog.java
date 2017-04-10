package com.bn.yfc.basedialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.R;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;


/**
 * Created by Administrator on 2016/12/18.
 */

public class WaiterDialog extends DialogFragment implements View.OnClickListener {
    private TextView phone;
    private Button cancel;
    private Button play;
    private RefreshData pefreshdata = new RefreshData(getActivity());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.waiterdialog, null);
        pefreshdata.RefreshWaith(getActivity());
        initView(view);
        return view;

    }

    private void initView(View view) {
        phone = (TextView) view.findViewById(R.id.waiterdialog_phone);
        phone.setText((String) SharedPreferencesUtils.getParam(getActivity(), "waith", " "));
        cancel = (Button) view.findViewById(R.id.waiterdialog_cancel);
        cancel.setOnClickListener(this);
        play = (Button) view.findViewById(R.id.waiterdialog_play);
        play.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        int dialogWidth = (int) (width * 0.9);
        int dialogHeight = (int) (height * 0.4);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.waiterdialog_cancel:
                dismiss();
                break;
            case R.id.waiterdialog_play:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + SharedPreferencesUtils.getParam(getActivity(), "waith", " ")));
                startActivity(intent);
                dismiss();
                break;
        }
    }
}
