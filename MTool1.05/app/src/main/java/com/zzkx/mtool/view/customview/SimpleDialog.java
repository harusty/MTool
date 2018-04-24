package com.zzkx.mtool.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sshss on 2017/9/29.
 */

public class SimpleDialog {

    protected View mDialogView;
    protected Window window;
    protected AlertDialog alertDialog;
    public Context mContext;


    public SimpleDialog(Context context, int res) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        window = alertDialog.getWindow();
        mDialogView = View.inflate(context, res, null);
    }

    public View findViewById(int id) {
        return mDialogView.findViewById(id);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        alertDialog.setOnDismissListener(dismissListener);
    }

    public void show() {
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setContentView(mDialogView);

        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//有edittext必须加这一条
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//布局背景透明
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;//在这定义宽度才可以居中
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;
        alertDialog.onWindowAttributesChanged(attributes);
    }

    public Window getDialogWindow() {
        return window;
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public View getView() {
        return mDialogView;
    }

    public AlertDialog getDialog() {
        return alertDialog;
    }


}
