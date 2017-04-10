package com.bn.yfc.base;


import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.yfc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/16.
 */

public class Tools {

    public static void setLog(String msg) {
        Log.d("yfc", msg);
    }

    //默认短提示
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //默认长提示
    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    //自定义提示
    public static void showIcToast(Context context, String msg) {
        LayoutInflater infa = LayoutInflater.from(context);
        LinearLayout linea = (LinearLayout) infa.inflate(R.layout.basetoastlinearlayout, null);
        TextView tv = (TextView) linea.findViewById(R.id.basetoast_title);
        tv.setText(msg);
        Toast st = new Toast(context);
        st.setGravity(Gravity.CENTER, 0, 0);
        st.setView(linea);
        st.show();
    }


    //判断输入的字符串参数是否为空/
    // @return boolean 空则返回true,非空则flase
    public static boolean isEmpty(String input) {
        return null == input || 0 == input.length() || 0 == input.replaceAll("\\s", "").length();
    }


    /**
     * 获取当前的时间yyyyMMddHHmmss
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取当前的时间yyyyMMdd
     */
    public static String getTimerDay() {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }

    //直接获取时间戳
    public static String getTimeStamp() {
        String currentDate = getCurrentTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime());
    }
}
