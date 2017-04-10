package com.bn.yfc.user;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.fragmentmain.RefreshData;
import com.bn.yfc.myapp.MyAppLication;
import com.bn.yfc.myapp.MyConfig;
import com.bn.yfc.R;
import com.bn.yfc.qrcode.QRCode;
import com.bn.yfc.tools.SharedPreferencesUtils;
import com.bn.yfc.tools.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;


/**
 * Created by Administrator on 2016/12/18.
 */

public class UserSettings extends BaseActivity implements OnClickListener {
    private Button but;
    private boolean sexCode = false;
    private EditText getname, geteya;
    private ImageView boy, gril, left, icon;
    private LinearLayout setPhone, bank, qrcode;
    private TextView title;
    private EditText phone;
    private Uri outUri;
    private Intent intent;
    private boolean playOne = false;
    private String types;
    RefreshData redata = new RefreshData(UserSettings.this);
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings);
        redata.StartDataRefresh(this, (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        initData();
        initView();
        initHead();
        setView();
    }


    //设置uri
    private void initData() {
        if (getIntent() != null) {
            types = getIntent().getStringExtra("data");
        }
        Tools.setLog("type" + types);
        outUri = Uri.fromFile(new File(Tools.phonePath()));

    }

    //从本地获取用户数据
    private void setView() {
        if (types.equals("106")) {
            //未完善资料无法显示银行卡信息
            bank.setVisibility(View.INVISIBLE);
            return;
        }
        //完善资料后用户信息无法修改
        getname.setFocusable(true);
        geteya.setFocusable(true);
        boy.setClickable(true);
        gril.setClickable(true);

        phone.setCursorVisible(true);
        phone.setFocusable(true);
        but.setVisibility(View.VISIBLE);
        bank.setVisibility(View.VISIBLE);
        //显示头像
        ImageRequest request = new ImageRequest((String) SharedPreferencesUtils.getParam(getApplication(), "pic", " "), new Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                icon.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyAppLication.getmQueue().add(request);
        getname.setText((String) SharedPreferencesUtils.getParam(getApplication(), "nickname", " "));
        geteya.setText((String) SharedPreferencesUtils.getParam(getApplication(), "age", " "));
        phone.setText((String) SharedPreferencesUtils.getParam(getApplication(), "tel", " "));
    }

    private void initHead() {
        left = (ImageView) findViewById(R.id.headac_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.headac_title);
        title.setText("我的资料");
    }

    private void initView() {

        timer = new Timer();
        icon = (ImageView) findViewById(R.id.usersettings_icon);
        icon.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.usersettings_phone);
        setPhone = (LinearLayout) findViewById(R.id.usersettings_setphone);
        setPhone.setOnClickListener(this);
        qrcode = (LinearLayout) findViewById(R.id.usersettings_qrcode);
        qrcode.setOnClickListener(this);
        bank = (LinearLayout) findViewById(R.id.usersettings_bank);
        bank.setOnClickListener(this);
        boy = (ImageView) findViewById(R.id.usersettings_sex_boy);
        boy.setOnClickListener(this);
        gril = (ImageView) findViewById(R.id.usersettings_sex_gril);
        gril.setOnClickListener(this);
        but = (Button) findViewById(R.id.usersettings_but);
        but.setOnClickListener(this);
        getname = (EditText) findViewById(R.id.usersettings_getname);
        geteya = (EditText) findViewById(R.id.usersettings_eya);
        //设置性别选项
        if (SharedPreferencesUtils.getParam(getApplication(), "sex", "0").equals("1")) {
            setGirl();
        } else {
            setBoy();
        }
    }


    private void setGirl() {
        boy.setBackgroundResource(R.drawable.withdraw_normal);
        gril.setBackgroundResource(R.drawable.withdraw_onclick);
        sexCode = false;
    }

    private void setBoy() {
        boy.setBackgroundResource(R.drawable.withdraw_onclick);
        gril.setBackgroundResource(R.drawable.withdraw_normal);
        sexCode = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headac_left:
                this.finish();
                break;
            case R.id.usersettings_setphone:
                intent = new Intent(this, ReceoverPhone.class);
                startActivity(intent);
                break;
            case R.id.usersettings_sex_boy:
                setBoy();
                break;
            case R.id.usersettings_sex_gril:
                setGirl();
                break;
            case R.id.usersettings_but:
                String names = getname.getText().toString();
                String ages = geteya.getText().toString();
                String phones = phone.getText().toString();
                String sexs = " ";
                if (sexCode) {
                    sexs = "2";
                } else {
                    sexs = "1";
                }
                if (Tools.isEmpty(names)) {
                    Tools.showToast(this, "请输入真实姓名");
                    break;
                }
                if (Tools.isEmpty(ages)) {
                    Tools.showToast(this, "请输入真实年龄");
                    break;
                }
                if (Tools.isEmpty(phones)) {
                    Tools.showToast(this, "请设置联系方式");
                    break;
                }
                if (new File(outUri.getPath()).exists()) {

                } else {
                    Tools.showToast(this, "请上传真实照片");
                    break;
                }
                postData(names, ages, sexs, phones);
                break;
            case R.id.usersettings_icon:
                showPopupWindows();
                break;
            case R.id.usersettings_bank:
                intent = new Intent(this, MyBank.class);
                startActivity(intent);
                break;
            case R.id.usersettings_qrcode:
                intent = new Intent(this, QRCode.class);
                startActivity(intent);
                break;
        }
    }

    //照片剪辑
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    //拍照
    private void playPhone() {
        Tools.setLog("拍摄图片");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(intent, 0);//拍照返回
    }

    //选择
    private void selectPhone() {
        Tools.setLog("选择图片");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (playOne) {
                    if (outUri != null) {
                        Bitmap bit = decodeUriAsBitmap(outUri);
                        icon.setImageBitmap(bit);
                    }
                    playOne = false;
                    return;
                }
                playOne = true;
                if (outUri != null) {
                    if (data != null) {
                        Tools.setLog("data不为空");
                        Uri selectedImage = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        String imagePath = c.getString(columnIndex);
                        c.close();
                        boolean fileBoolean = false;
                        Tools.setLog("获取资源库中的文件地址" + imagePath);
                        fileBoolean = Tools.copyFile(imagePath, outUri.getPath(), true);
                        Tools.setLog("判断copy文件是否存在" + new File(outUri.getPath()).exists() + "/" + fileBoolean);
                        Tools.setLog("当前url值为" + outUri.getPath());
                        cropImageUri(outUri, 500, 500, 123);
                        break;
                    } else {
                        Tools.setLog("data为空");
                        cropImageUri(outUri, 500, 500, 123);
                    }
                }
                break;

            case RESULT_CANCELED:
                playOne = false;
                break;
            case 1:
                break;
            case 200:

                break;
        }
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

    //显示popupwindows
    private void showPopupWindows() {
        View popView = LayoutInflater.from(this).inflate(R.layout.popupwindows, null);
        final PopupWindow popupWindow = new PopupWindow(popView, MATCH_PARENT, WRAP_CONTENT);
        popupWindow.setContentView(popView);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(findViewById(R.id.usersettings_showpop), Gravity.BOTTOM, 0, 0);
        Button play = (Button) popView.findViewById(R.id.pop_playphone);
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playPhone();
                popupWindow.dismiss();
            }
        });
        Button select = (Button) popView.findViewById(R.id.popselect_phone);
        select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhone();
                popupWindow.dismiss();
            }
        });
        Button cancel = (Button) popView.findViewById(R.id.pop_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void postData(String name, String age, String sex, final String phones) {
        AjaxParams params = new AjaxParams();
        params.put("id", (String) SharedPreferencesUtils.getParam(getApplication(), "id", " "));
        params.put("type", "1");
        params.put("sex", sex);
        params.put("realname", name);
        params.put("age", age);
        params.put("tel", phones);

        try {
            String filePath = outUri.getPath();
            params.put("pic", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FinalHttp fhp = new FinalHttp();
        showProgressDialog();
        fhp.post(MyConfig.POSTOERSNALURL, params, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                closeProgressDialog();
                Tools.showToast(getApplication(), "网络错误");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject object = new JSONObject(s);
                    switch (object.getInt("code")) {
                        case 1:
                            SharedPreferencesUtils.setParam(getApplication(), "tel", phones);
                            SharedPreferencesUtils.setParam(getApplication(), "ifdata", "1");
                            Tools.showToast(UserSettings.this, "上传成功");
                            Tools.setLog("返回的URL = " + outUri.getPath());
                            SharedPreferencesUtils.setParam(getApplication(), "iconifs", "1");
                            SharedPreferencesUtils.setParam(getApplication(), "settingsIcon", outUri.getPath());
                            redata.StartDataRefresh(getApplication(), (String) SharedPreferencesUtils.getParam(UserSettings.this, "id", ""));
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    closeProgressDialog();
                                    finish();
                                }
                            }, 1000);
                            break;
                        case 111:
                            Tools.showToast(UserSettings.this, "图片上传失败，请重新选择图上传");
                            break;
                        case 109:
                            Tools.showToast(UserSettings.this, "个人信息保存失败，重新提交");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}



