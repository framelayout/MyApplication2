package com.bn.yfc.map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bn.yfc.R;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.buy.SelectAddress;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ReleaseDelivery1 extends BaseActivity implements OnClickListener {
    private int RESULTCODE = 12;
    private ImageView left;
    private TextView title;
    private EditText getName, getPirx, getRemarks, getPhone, getShowTime, getDetailed, getSenderName;
    private TextView address;
    private RelativeLayout selectAddress;
    private ImageView icon1, icon2, icon3, icon1clean, icon2clean, icon3clean;
    private Button but;
    private Intent intent;
    private String type;
    private String phoType;
    private ReleaseBean bean;
    private String carType;
    private double latduble;
    private double longduble;
    private LinearLayout setTime;
    private Bitmap bitsa;
    //拍照
    /****/
    RefreshData redata = new RefreshData(ReleaseDelivery1.this);
    private Uri outUri;
    private Map<String, Uri> uriMap;
    private static final int camera_request_code = 22;
    private TimeSelector timerselector;
    private int textIndex = 0;
    private boolean playOne = false;

    //初始化outurl
    private void startUrl() {
        outUri = Uri.fromFile(new File(Tools.phonePath()));
    }

    //拍照
    private void playPhone() {
        startUrl();
        Log.d("yfc", "拍照");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(intent, camera_request_code);//拍照返回
    }

    //选择
    private void selectPhone() {
        startUrl();
        Log.d("yfc", "选择");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, camera_request_code);
    }

    //显示popupwindows
    private void showPopupWindows() {
        View popView = LayoutInflater.from(this).inflate(R.layout.popupwindows, null);
        final PopupWindow popupWindow = new PopupWindow(popView, MATCH_PARENT, WRAP_CONTENT);
        popupWindow.setContentView(popView);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(findViewById(R.id.releasedeliverry1_showpop), Gravity.BOTTOM, 0, 0);
        Button play = (Button) popView.findViewById(R.id.pop_playphone);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPhone();
                popupWindow.dismiss();
            }
        });
        Button select = (Button) popView.findViewById(R.id.popselect_phone);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhone();
                popupWindow.dismiss();
            }
        });
        Button cancel = (Button) popView.findViewById(R.id.pop_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    //照片剪辑
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    //Uri转Btimap
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    //拍照
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.releasedelivery1);
        bitsa = BitmapFactory.decodeResource(getResources(), R.drawable.add_icon);
        initTimer();
        initData();
        initHead();
        initView();
    }

    private void initTimer() {
        timerselector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                getShowTime.setText(time);
            }
        }, Tools.getCurrentTimes(), "2099-12-30 00:00");
        timerselector.setMode(TimeSelector.MODE.YMDHM);
    }

    private void initData() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            carType = getIntent().getStringExtra("cartype");
            Tools.setLog("请求保险数据");
            redata.RefreshProtect(ReleaseDelivery1.this);
            redata.RefreshTolls(ReleaseDelivery1.this, type);
            Tools.setLog("carType" + type);
        }
        uriMap = new HashMap<>();
        bean = new ReleaseBean();
    }

    private void initView() {
        icon1 = (ImageView) findViewById(R.id.releasedelivery1_article_icon1);
        icon1.setOnClickListener(this);
        icon1 = Tools.setImageViewWidth(this, icon1, 4.6);
        icon1.setImageBitmap(bitsa);
        icon1clean = (ImageView) findViewById(R.id.releasedelivery1_article_icon1_clean);
        icon1clean.setOnClickListener(this);
        icon2 = (ImageView) findViewById(R.id.releasedelivery1_article_icon2);
        icon2.setOnClickListener(this);
        icon2 = Tools.setImageViewWidth(this, icon2, 4.6);
        icon2.setImageBitmap(bitsa);
        icon2clean = (ImageView) findViewById(R.id.releasedelivery1_article_icon2_clean);
        icon2clean.setOnClickListener(this);
        icon3 = (ImageView) findViewById(R.id.releasedelivery1_article_icon3);
        icon3.setOnClickListener(this);
        icon3 = Tools.setImageViewWidth(this, icon3, 4.6);
        icon3.setImageBitmap(bitsa);
        icon3clean = (ImageView) findViewById(R.id.releasedelivery1_article_icon3_clean);
        icon3clean.setOnClickListener(this);
        address = (TextView) findViewById(R.id.releasedelivery1_address);
        selectAddress = (RelativeLayout) findViewById(R.id.releasedelivery1_selecaddress);
        selectAddress.setOnClickListener(this);
        but = (Button) findViewById(R.id.releasedelivery1_but);
        but.setOnClickListener(this);
        getName = (EditText) findViewById(R.id.releasedelivery1_article);
        getPirx = (EditText) findViewById(R.id.releasedelivery1_prix);
        //保留两位小数
        getPirx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        getPirx.setText(s);
                        getPirx.setSelection(s.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getDetailed = (EditText) findViewById(R.id.releasedelivery1_detailed);
        getRemarks = (EditText) findViewById(R.id.releasedelivery1_remarks);
        getShowTime = (EditText) findViewById(R.id.releasedelivery1_showtime);
        getPhone = (EditText) findViewById(R.id.releasedelivery1_phone);
        getSenderName = (EditText) findViewById(R.id.releasedelivery1_sendername);
        setTime = (LinearLayout) findViewById(R.id.releasedelivery1_settime);
        Tools.setLog("接受到订单类型为" + type);

        if (!type.equals("3")) {
            setTime.setVisibility(View.GONE);
        }
        getShowTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    Tools.setLog("获取焦点");
                    timerselector.show();
                } else {
                    //失去焦点
                    Tools.setLog("失去焦点");
                }
            }
        });
        getShowTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Tools.setLog("当前文字" + s.toString() + "start=" + start + "|count=" + count + "|after=" + after);
                if (textIndex == start) {
                    Tools.setLog("不变");
                } else {
                    Tools.setLog("变化了");
                    timerselector.show();
                    textIndex = start;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Tools.setLog("第二个方法文字" + s.toString() + "start=" + start + "|count=" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setOnClickListener(this);
        left.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.headac_title);
        switch (type) {
            default:
                title.setText("发布快递");
                break;
            case "3":
                title.setText("发布顺路送");
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                finish();
                break;
            case R.id.releasedelivery1_but:
                ifsData();
                break;
            case R.id.releasedelivery1_article_icon1:
                phoType = "icon1";
                showPopupWindows();
                break;
            case R.id.releasedelivery1_article_icon1_clean:
                Tools.setLog("点击清除 icon1");
                if (uriMap.get("icon1") != null) {
                    uriMap.remove("icon1");
                }
                icon1clean.setVisibility(View.INVISIBLE);
                icon1.setImageBitmap(bitsa);
                break;
            case R.id.releasedelivery1_article_icon2:
                phoType = "icon2";
                showPopupWindows();
                break;
            case R.id.releasedelivery1_article_icon2_clean:
                if (uriMap.get("icon2") != null) {
                    uriMap.remove("icon2");
                }
                icon2clean.setVisibility(View.INVISIBLE);
                icon2.setImageBitmap(bitsa);
                break;
            case R.id.releasedelivery1_article_icon3:
                phoType = "icon3";
                showPopupWindows();
                break;
            case R.id.releasedelivery1_article_icon3_clean:
                if (uriMap.get("icon3") != null) {
                    uriMap.remove("icon3");
                }
                icon3clean.setVisibility(View.INVISIBLE);
                icon3.setImageBitmap(bitsa);
                break;
            case R.id.releasedelivery1_selecaddress:
                intent = new Intent(this, SelectAddress.class);
                intent.putExtra("jump", "ReleaseDelivery");
                startActivityForResult(intent, RESULTCODE);
                break;

        }


    }

    private void ifsData() {
        String name = getName.getText().toString();
        String prix = getPirx.getText().toString();
        String remarks = getRemarks.getText().toString();
        String phone = getPhone.getText().toString();
        String tim = getShowTime.getText().toString();
        String addres = address.getText().toString();
        String detailed = getDetailed.getText().toString();
        String senderName = getSenderName.getText().toString();
        if (Tools.isEmpty(name)) {
            Tools.showToast(this, "请输入物品名称");
            return;
        }
        bean.setAritcle(name);
        if (Tools.isEmpty(prix)) {
            Tools.showToast(this, "请输入快递价值");
            return;
        }
        bean.setPrix(prix);
        if (Tools.isEmpty(remarks)) {
            Tools.showToast(this, "请输入快递备注");
            return;
        }
        bean.setRemarks(remarks);

        if (Tools.isEmpty(senderName)) {
            Tools.showToast(this, "请输入发货人姓名");
            return;
        }
        bean.setSenderName(senderName);
        if (Tools.isEmpty(phone)) {
            Tools.showToast(this, "请输入发货人联系方式");
            return;
        }
        bean.setSenderPhone(phone);
        if (type.equals("3")) {
            if (Tools.isEmpty(tim)) {
                Tools.showToast(this, "请输入预计发货时间");
                return;
            }
            bean.setSendeShowTime(tim);

        } else {
            Tools.setLog("不是顺路送");
        }
        if (Tools.isEmpty(detailed)) {
            Tools.showToast(this, "请选择发货地址");
            return;
        }
        bean.setSenderSelectAddress(detailed);

        if (Tools.isEmpty(addres)) {
            Tools.showToast(this, "请输入发货详细地址");

            return;
        }
        bean.setSenderAddress(addres);
        int ifURl = 0;
        if (uriMap.get("icon1") != null) {
            bean.setAritcleIonc1(uriMap.get("icon1").getPath());
            Tools.setLog("照片1地址为" + uriMap.get("icon1").getPath());
            ifURl++;

        }
        if (uriMap.get("icon2") != null) {
            bean.setAritcleIonc2(uriMap.get("icon2").getPath());
            ifURl++;

        }
        if (uriMap.get("icon3") != null) {
            bean.setAritcleIonc3(uriMap.get("icon3").getPath());
            ifURl++;

        }
        if (ifURl <= 0) {
            Tools.showToast(ReleaseDelivery1.this, "请上传一张照片");
            return;
        }
        intent = new Intent(this, ReleaseDelivery.class);
        if (type == null) {
            intent.putExtra("type", type);
        } else {
            intent.putExtra("type", type);
        }

        intent.putExtra("prix", prix);
        intent.putExtra("cartype", carType);
        intent.putExtra("data", bean);
        intent.putExtra("latduble", latduble);
        intent.putExtra("longduble", longduble);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 12:
                if (data != null) {
                    address.setVisibility(View.VISIBLE);
                    address.setText(data.getStringExtra("address"));
                    latduble = data.getDoubleExtra("latduble", 0);
                    longduble = data.getDoubleExtra("longduble", 0);
                    Tools.setLog("获取的地址为:" + data.getStringExtra("address") + latduble + "," + longduble);
                }
                break;
            case 13:
                Tools.setLog("触发了");
                break;
        }
        if (resultCode == 13) {
            finish();
        }
        if (playOne) {
            addUri();
            playOne = false;
            return;
        }
        playOne = true;
        switch (resultCode) {
            case RESULT_OK:
                //拍照获取
                if (outUri != null) {
                    if (data != null) {
                        if (data.getData() != null) {
                            Uri selectedImage = data.getData();
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePathColumns[0]);
                            String imagePath = c.getString(columnIndex);
                            Tools.setLog("判断文件是否存在==" + new File(imagePath).exists());
                            c.close();
                            boolean sss = false;
                            sss = Tools.copyFile(imagePath, outUri.getPath(), true);
                            Tools.setLog("判断copy文件是否存在" + new File(outUri.getPath()).exists() + "/" + sss);
                            cropImageUri(outUri, 500, 500, 199);
                        }
                        break;
                    }
                    Tools.setLog("outUri没有值");
                    cropImageUri(outUri, 500, 500, 199);
                }
                break;
            case RESULT_CANCELED:
                playOne = false;
                break;
            case 199:

                break;
        }
    }

    private void addUri() {
        Tools.setLog("stausUri>" + phoType + " outUri> " + outUri);
        if (outUri != null) {
            Bitmap bit = decodeUriAsBitmap(outUri);
            switch (phoType) {
                case "icon1":
                    Tools.setLog("icon1");
                    icon1clean.setVisibility(View.VISIBLE);
                    icon1.setImageBitmap(bit);
                    break;
                case "icon2":
                    Tools.setLog("icon2");
                    icon2clean.setVisibility(View.VISIBLE);
                    icon2.setImageBitmap(bit);
                    break;
                case "icon3":
                    Tools.setLog("icon3");
                    icon3clean.setVisibility(View.VISIBLE);
                    icon3.setImageBitmap(bit);
                    break;
            }
            Uri phoneUri = Uri.parse(outUri.getPath());
            Log.e("yfc", "stausUri>" + phoType + " outUri> " + outUri);
            uriMap.put(phoType, phoneUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uriMap != null) {
            uriMap.clear();
            uriMap = null;
        }
    }
}
