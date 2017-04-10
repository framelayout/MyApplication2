package com.bn.yfc.base;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.bn.yfc.map.MapTest;
import com.bn.yfc.R;
import com.bn.yfc.user.UserLogin;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ShowNotice {
    private static NotificationManager manager;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void ShowNotice(Context context) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.testic);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.testic));
        builder.setContentText("测试通知栏");
        builder.setContentTitle("测试通知栏标题");
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(context, UserLogin.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        manager.notify(0, builder.build());
    }

    public static void CleanNotice() {
        if (manager != null) {
            manager.cancel(0);
        }


    }
}
