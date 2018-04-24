package com.zzkx.mtool.util;

import android.widget.Toast;

import com.zzkx.mtool.MyApplication;


/**
 * Created by l.k.x on 2015/10/21.
 */
public class ToastUtils {
    private static Toast toast;
    private static Toast toast2;
//    private static WindowManager windowManager;
    //    private static Handler handler;
//    private boolean isShow = false;

    /**
     * 可以连续弹，不用等上一个显示
     *
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);//将文本设置为toast
        toast.show();
    }


//    public static void showCusToast(String text) {
//        if (toast2 == null) {
//            toast2 = new Toast(MyApplication.getContext());
//        }
//        View inflate = View.inflate(MyApplication.getContext(), R.layout.custoast, null);
//        TextView tv = (TextView) inflate.findViewById(R.id.tv);
//        tv.setText(text);
//        toast2.setDuration(Toast.LENGTH_SHORT);
//        toast2.setView(inflate);
//        toast2.setGravity(Gravity.CENTER, 0, 0);
//        toast2.show();
//    }

//    public static void showCusToast_color(String text,int color) {
//        if (toast2 == null) {
//            toast2 = new Toast(MyApplication.getContext());
//        }
//        View inflate = View.inflate(MyApplication.getContext(), R.layout.custoast, null);
//        TextView tv = (TextView) inflate.findViewById(R.id.tv);
//        tv.setText(text);
//        tv.setTextColor(MyApplication.getContext().getResources().getColor(color));
//        toast2.setDuration(Toast.LENGTH_SHORT);
//        toast2.setView(inflate);
//        toast2.setGravity(Gravity.CENTER, 0, 0);
//        toast2.show();
//    }

//    public static void showCusToast_color(long time, long timedelay, String des) {
//        final View inflate = View.inflate(MyApplication.getContext(), R.layout.custoast_color, null);
//        TextView tv = (TextView) inflate.findViewById(R.id.tv);
//        tv.setText(des);
//        Context context = MyApplication.getContext().getApplicationContext();
//        String packageName = context.getPackageName();
//        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.format = PixelFormat.TRANSLUCENT;
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;
//        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//        params.packageName = packageName;
//
//
//    }
}
