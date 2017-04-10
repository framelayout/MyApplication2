package com.bn.yfc.user;

import android.os.Bundle;
import android.view.View;

import com.bn.yfc.base.Tools;
import com.bn.yfc.base.BaseActivity;
import com.bn.yfc.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2016/11/22.
 */

public class UserLogin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
    }

    public void LoginWX(View v) {
        UMImage image = new UMImage(UserLogin.this, "http://s1.dwstatic.com/group1/M00/97/25/ad65c6fd684c552ef49e60637c3b2f89.jpg");
        new ShareAction(UserLogin.this).setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(image)
                .withText("test one Studio")
                .setCallback(shaerlistener)
                .share();
    }

    UMShareListener shaerlistener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Tools.showToast(UserLogin.this, "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Tools.showToast(UserLogin.this, "分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Tools.showToast(UserLogin.this, "分享取消");
        }
    };

    public void IVmageiewToWX(View v) {
        //      WXShare.ToWXText("Studio test");

    }
}
