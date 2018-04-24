package com.zzkx.mtool.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.activity.CollectionSearchActivity;
import com.zzkx.mtool.view.activity.MessageSearchActivity;
import com.zzkx.mtool.view.activity.OrderSearchActivity;
import com.zzkx.mtool.view.activity.SearchFoodShopActivity;
import com.zzkx.mtool.view.activity.StateSearchActivity;

/**
 * Created by sshss on 2017/8/29.
 */

public class DialogSearch implements View.OnClickListener {

    private AlertDialog alertDialog;

    public DialogSearch(Context context) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
    }

    public void show() {
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_search);
        alertDialog.findViewById(R.id.tv_search_food).setOnClickListener(this);
        alertDialog.findViewById(R.id.iv_cancle).setOnClickListener(this);
        alertDialog.findViewById(R.id.state).setOnClickListener(this);
        alertDialog.findViewById(R.id.order).setOnClickListener(this);
        alertDialog.findViewById(R.id.supported).setOnClickListener(this);
        alertDialog.findViewById(R.id.collection).setOnClickListener(this);
        alertDialog.findViewById(R.id.message).setOnClickListener(this);
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//有edittext必须加这一条
        window.setWindowAnimations(R.style.up_down_anim_pop);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//布局背景透明
        WindowManager.LayoutParams attributes = window.getAttributes();//宽度要show完之后在设
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;//在这定义宽度才可以居中
        attributes.height = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER;
        alertDialog.onWindowAttributesChanged(attributes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_food:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), SearchFoodShopActivity.class));
                break;
            case R.id.iv_cancle:
                alertDialog.dismiss();
                break;
            case R.id.state:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), StateSearchActivity.class));
                break;
            case R.id.order:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), OrderSearchActivity.class));
                break;
            case R.id.supported:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), StateSearchActivity.class).putExtra(Const.FLAG, true));
                break;
            case R.id.collection:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), CollectionSearchActivity.class));
                break;
            case R.id.message:
                alertDialog.getContext().startActivity(new Intent(alertDialog.getContext(), MessageSearchActivity.class));
                break;
        }
        alertDialog.dismiss();

    }
}
