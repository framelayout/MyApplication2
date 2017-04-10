package com.bn.yfc.buy;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.basedialog.BaseProgressDialog;
import com.bn.yfc.basedialog.PayDialog;
import com.bn.yfc.fragmentmain.NewsInterface;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.tools.PremissionTools;
import com.bn.yfc.tools.Tools;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.user.Login;
import com.bn.yfc.user.OrdersTest;
import com.bn.yfc.user.Permissions;
import com.bn.yfc.user.UserAudit;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/1/2.
 */

public class GoBuyFragment extends Fragment implements OnClickListener {
    private int RESULTCODE = 12;
    private View view;
    private ImageView right;
    private TextView title;
    private RelativeLayout selectAddress;
    private TextView timerYear, timerDay, hint, address;
    private EditText getDetailed, getName, getNumber, getBrand, getPrix, getMarks, getContects, getPhone;
    private Button but;
    private Intent intent;
    double lat, lon;
    BaseProgressDialog progressDialog;
    private int ifPayCode = 0;
    private int statusCode = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gobuy, null);
        initHead(view);
        initView(view);
        setView();
        progressDialog = new BaseProgressDialog(getActivity());
        return view;
    }

    private void setView() {

    }

    private void initHead(View view) {
        if (statusCode == 0) {
            title = (TextView) view.findViewById(R.id.head_title);
            title.setText("帮我买");
            right = (ImageView) view.findViewById(R.id.head_right);
            right.setVisibility(View.GONE);
            return;
        }

        right = (ImageView) view.findViewById(R.id.head_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.head_title);
        title.setText((String) SharedPreferencesUtils.getParam(getActivity(), "city", " "));
        setRightVew();
    }

    private void initView(View view) {
        selectAddress = (RelativeLayout) view.findViewById(R.id.gobuy_selectaddress);
        selectAddress.setOnClickListener(this);
        address = (TextView) view.findViewById(R.id.gobuy_address);
        getDetailed = (EditText) view.findViewById(R.id.gobuy_detailed);
        getName = (EditText) view.findViewById(R.id.gobuy_name);
        getBrand = (EditText) view.findViewById(R.id.gobuy_brand);
        getNumber = (EditText) view.findViewById(R.id.gobuy_number);
        getPrix = (EditText) view.findViewById(R.id.gobuy_prix);
        getPrix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        getPrix.setText(s);
                        getPrix.setSelection(s.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getMarks = (EditText) view.findViewById(R.id.gobuy_marks);
        getPhone = (EditText) view.findViewById(R.id.gobuy_phone);
        getContects = (EditText) view.findViewById(R.id.gobuy_contacts);
        but = (Button) view.findViewById(R.id.gobuy_but);
        but.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.head_right:
                if (Permissions.userLoginPermissions(getActivity())) {


                    intent = new Intent(getActivity(), NewsInterface.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
                break;
            case R.id.gobuy_selectaddress:
                intent = new Intent(getActivity(), SelectAddress.class);
                intent.putExtra("jump", "gobuy");
                startActivityForResult(intent, RESULTCODE);
                break;
            case R.id.gobuy_but:
                if (Permissions.userLoginPermissions(getActivity())) {
                    gotBuyIfs();
                } else {
                    intent = new Intent(getActivity(), Login.class);
                    intent.putExtra("logintype", "gobuy");
                    startActivity(intent);
                }


                break;
        }
    }

    private void showProgress() {
        progressDialog.show();
        progressDialog.startAnim();
    }

    private void closeProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void gotBuyIfs() {
        String name = getName.getText().toString();
        String number = getNumber.getText().toString();
        String brand = getBrand.getText().toString();
        String prix = getPrix.getText().toString();
        String detailed = getDetailed.getText().toString();
        String addre = address.getText().toString();
        String contects = getContects.getText().toString();
        String phone = getPhone.getText().toString();
        String marks = getMarks.getText().toString();
        if (Tools.isEmpty(name)) {
            Tools.showToast(getActivity(), "请输入商品名称");
            return;
        }
        if (Tools.isEmpty(number)) {
            Tools.showToast(getActivity(), "请输入商品数量");
            return;
        }
        if (Tools.isEmpty(brand)) {
            Tools.showToast(getActivity(), "请输入商品品牌");
            return;
        }
        if (Tools.isEmpty(prix)) {
            Tools.showToast(getActivity(), "请输入商品参考价格");
            return;
        }
        if (Tools.isEmpty(detailed)) {
            Tools.showToast(getActivity(), "请输入详细地址");
            return;
        }
        if (Tools.isEmpty(addre)) {
            Tools.showToast(getActivity(), "请选择送达地址");
            return;
        }
        if (Tools.isEmpty(contects)) {
            Tools.showToast(getActivity(), "请输入联系人姓名");
            return;
        }
        if (Tools.isEmpty(phone)) {
            Tools.showToast(getActivity(), "请输入联系方式");
            return;
        }
        if (Tools.isEmpty(marks)) {
            Tools.showToast(getActivity(), "请输入订单备注");
            return;
        }
        PosetGoBuy(name, prix, number, brand, addre, contects, phone, marks, detailed);
    }

    private void PosetGoBuy(String name, String prix, String number, String brand, String addre, String contects, String phone, String marks, String detailed) {
        AjaxParams params = new AjaxParams();
        params.put("name", name);
        params.put("money", prix);
        params.put("goods_num", number);
        params.put("goods_logo", brand);
        params.put("boss_tel", phone);
        params.put("boss_pos", addre);
        params.put("boss_name", contects);
        params.put("tips", marks);
        params.put("boss_street", detailed);
        params.put("boss_jd", String.valueOf(lon));
        params.put("boss_wd", String.valueOf(lat));
        Tools.setLog("uid ==" + SharedPreferencesUtils.getParam(getActivity(), "id", " "));
        params.put("uid", (String) SharedPreferencesUtils.getParam(getActivity(), "id", " "));
        params.put("area", (String) SharedPreferencesUtils.getParam(getActivity(), "sheng", "") + SharedPreferencesUtils.getParam(getActivity(), "city", "") + SharedPreferencesUtils.getParam(getActivity(), "qu", ""));
        params.put("type", "3");
        SharedPreferencesUtils.setParam(getActivity(), "ispay", "3");
        FinalHttp fhp = new FinalHttp();
        showProgress();
        fhp.post(MyConfig.POSTORDERADDURL, params, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Tools.setLog("发布顺路送订单错误" + strMsg);
                closeProgress();
                Tools.showToast(getActivity(), "网络错误");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeProgress();
                Tools.setLog("发布成功" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 10:
                            Tools.showToast(getActivity(), "订单发布成功!");
                            IntentMyOrders();
                            break;
                        case 110:
                            break;
                        case 11:
                            Tools.showToast(getActivity(), "发布成功");
                            cleanData();
                            IntentMyOrders();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void IntentMyOrders() {
        intent = new Intent(getActivity(), UserAudit.class);
        startActivity(intent);
        cleanData();
    }

    private void startPayDialog(String order, String money, String app_id, String privateKey) {
        PayDialog dialog = new PayDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "pay");
        bundle.putString("order", order);
        bundle.putString("money", money);
        bundle.putString("app_id", app_id);
        bundle.putString("privateKey", privateKey);
        dialog.setArguments(bundle);
        ifPayCode = 1;
        dialog.show(getChildFragmentManager(), "pay");
    }

    private void cleanData() {
        getContects.setText("");
        getPrix.setText("");
        getNumber.setText("");
        getPhone.setText("");
        getBrand.setText("");
        getDetailed.setText("");
        getMarks.setText("");
        getName.setText("");
        address.setText("");
        lat = 0;
        lon = 0;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTCODE) {
            Tools.setLog("跳转选择地址界面");
        }
        if (resultCode == RESULTCODE) {
            lat = data.getDoubleExtra("latduble", 0);
            lon = data.getDoubleExtra("longduble", 0);
            Tools.setLog("获取的经纬度" + lat + "|" + lon);
            address.setText(data.getStringExtra("address"));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Permissions.newsDataPermissions(getActivity())) {
            right.setBackgroundResource(R.drawable.newshave_icon);
        } else {
            right.setBackgroundResource(R.drawable.news_icon);
        }
        Tools.setLog("gobuy onStart");
        setRightVew();
        if (ifPayCode == 0) {
            return;
        }
        if (Tools.isEmpty((String) SharedPreferencesUtils.getParam(getActivity(), "ispay", ""))) {
            IntentMyOrders();
        }
    }

    public void startifPay() {
        IntentMyOrders();
    }

    //   IntentMyOrders();
    private void setRightVew() {
        if (Permissions.userLoginPermissions(getActivity())) {
            if (Permissions.newsDataPermissions(getActivity())) {
                right.setBackgroundResource(R.drawable.newshave_icon);
            } else {
                right.setBackgroundResource(R.drawable.news_icon);
            }
        }
    }
}
