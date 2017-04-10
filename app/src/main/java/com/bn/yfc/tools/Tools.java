package com.bn.yfc.tools;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bn.yfc.R;
import com.bn.yfc.myapp.Arith;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/11/16.
 */

public class Tools {


    //极光推送别名设置回调
    private static TagAliasCallback setTagAliasCallback() {
        String s = null;
        TagAliasCallback tagaliascallback = new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                switch (i) {
                    case 0:
                        com.bn.yfc.base.Tools.setLog("\n别名设置成功\n");
                        break;
                    case 6002:
                        com.bn.yfc.base.Tools.setLog("\n网络不好，别名设置失败\n");
                        break;
                }
            }
        };
        return tagaliascallback;
    }

    public static void ReigeJPush(Context context) {
        JPushInterface.setAlias(context, (String) SharedPreferencesUtils.getParam(context, "invite", ""), setTagAliasCallback());
    }

    public static void setLog(String msg) {
        Log.d("yfc", msg);

    }

    public static void setLog(String tag, String msg) {
        Log.d(tag, msg);
    }

    /**
     * 获取当前的时间yyyyMMdd
     */
    public static String getTimerDay() {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
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
     * 获取当前的时间HHmmss
     */
    public static String getTimeHM() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    /**
     * 获取当前的时间yyyyMMddHHmmss
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getCurrentTimes() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    //返回存储卡
    public static String returnSD() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    //照片存储地址
    public static String phonePath() {
        return returnSD() + "/cykds/" + "Android" + getTimeStamp() + ".jpg";
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
        String sss = String.valueOf(date.getTime());
        sss = sss.substring(0, sss.length() - 3);
        return sss;
    }

    //MD5加密
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String getDatess(long i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(i);
        //mss即是从服务器获取的时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sf.format(calendar.getTime());
        //date即是已转换好的时间。
        return date;
    }

    public static String getDates(long i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(i * 1000);
        //mss即是从服务器获取的时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sf.format(calendar.getTime());
        //date即是已转换好的时间。
        return date;
    }

    //时间戳转data
    public static String getDate(long i) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(i * 1000);
        //mss即是从服务器获取的时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sf.format(calendar.getTime());
        //date即是已转换好的时间。
        return date;
    }

    //获取屏幕宽度
    public static int getWidht(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }

    //获取屏幕宽度
    public static int getHeight(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        return height;
    }

    /*****/


    // 方法三
    public static void copyFile3(String srcPath, String destPath) throws IOException {
        // 打开输入流
        FileInputStream fis = new FileInputStream(srcPath);
        // 打开输出流
        FileOutputStream fos = new FileOutputStream(destPath);
        // 读取和写入信息
        int len = 0;
        // 创建一个字节数组，当做缓冲区
        byte[] b = new byte[1024];
        while ((len = fis.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        // 关闭流  先开后关  后开先关
        fos.close(); // 后开先关
        fis.close(); // 先开后关

    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);
        String MESSAGE;
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            MESSAGE = "源文件：" + srcFileName + "不存在！";
            Tools.setLog(MESSAGE);
            return false;
        } else if (!srcFile.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
            Tools.setLog(MESSAGE);
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(descFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(descFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }
        Tools.setLog("复制文件运行了一半了");
        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static int getMultiple(int widht, double be) {
        double aa = widht;
        double bb = be;
        double cc = 0;
        try {
            cc = Arith.div(aa, bb, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) cc;
    }

    public static ImageView setImageViewWidth(Activity contex, ImageView iv, double be) {
        LayoutParams para;
        para = iv.getLayoutParams();
      /*  Tools.setLog("Imagview 宽 ====" + iv.getWidth());
        Tools.setLog("Imagview 高 ====" + iv.getHeight());*/
        para.width = getMultiple(Tools.getWidht(contex), be);
        para.height = para.width;
      /*  Tools.setLog("Imagview 宽 ====" + para.width);
        Tools.setLog("Imagview 高 ====" + para.height);*/
        iv.setLayoutParams(para);
        return iv;
    }

    public static LinearLayout setLinearLayouWidth(Activity contex, LinearLayout iv, double be) {
        LayoutParams para;
        para = iv.getLayoutParams();
        Tools.setLog("Imagview 宽 ====" + iv.getWidth());
        Tools.setLog("Imagview 高 ====" + iv.getHeight());
        para.width = getMultiple(Tools.getWidht(contex), be);
        //para.height = para.width;
        Tools.setLog("Imagview 宽 ====" + para.width);
        Tools.setLog("Imagview 高 ====" + para.height);
        iv.setLayoutParams(para);
        return iv;
    }

    public static Button setButtonViewWidth(Activity contex, Button iv, double be) {
        LayoutParams para;
        para = iv.getLayoutParams();
        Tools.setLog("Imagview 宽 ====" + iv.getWidth());
        Tools.setLog("Imagview 高 ====" + iv.getHeight());
        para.width = getMultiple(Tools.getWidht(contex), be);
        //para.height = para.width;
        Tools.setLog("Imagview 宽 ====" + para.width);
        Tools.setLog("Imagview 高 ====" + para.height);
        iv.setLayoutParams(para);
        return iv;
    }

    public static LinearLayout setLinViewWidth(Activity contex, LinearLayout iv, double be) {
        if (iv == null) {
            Tools.setLog("iv 是空");
        } else {
            Tools.setLog("iv 不是空");
        }
        iv.setLayoutParams(new LinearLayout.LayoutParams(getMultiple(Tools.getWidht(contex), be), LayoutParams.WRAP_CONTENT));
        return iv;
    }


    //状态栏
    public static void setWindowStatusBarColor(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorSkyBlue));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //动态设置状态栏颜色
    public static void setWindowStatusBarColor1(Activity activity, int colo) {
        Tools.setLog("动态设置标题栏");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colo));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成二维码
    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @return void
     */
    public static Uri SavaImage(Bitmap bitmap) {
        String path = Tools.phonePath();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(new File(path));
        return uri;
    }

    // Bitmap对象保存味图片文件
    public static File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Tools.phonePath());//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //字符串转义为数组
    public static String[] convertStrToArray2(String str) {
        StringTokenizer st = new StringTokenizer(str, ",");
        String[] strArray = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            strArray[i++] = st.nextToken();
        }
        return strArray;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String toStringTwo(String s) {
        String reS = "";
        if (s.contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                reS = s.substring(0, s.indexOf(".") + 3);
                return reS;
            }
        }

        return s;
    }

    //接取小数点后两位字符
    public static String selectStringTwo(CharSequence s) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                return s.toString();
            }
        }
        return s.toString();
    }
}
