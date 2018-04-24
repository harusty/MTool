package com.zzkx.mtool.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.util.ScreenUtils;

/**
 * Created by sshss on 2017/8/23.
 */

public class DialogInitialNab {
    private View view;
    private AlertDialog alertDialog;
    private GridView mGridView;

    public DialogInitialNab(final Context context) {

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(true);

        view = View.inflate(context, R.layout.popup_initial, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 27;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(context, R.layout.item_popup_initial_nav, null);
                TextView name = (TextView) convertView.findViewById(R.id.tv_section);
                char i;
                if (position == 0)
                    i = '#';
                else {
                    i = (char) ('A' + position - 1);
                }
                String s = String.valueOf(i);
                name.setText(s);
                convertView.setTag(s);
                return convertView;
            }
        });
        this.view = view;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mGridView.setOnItemClickListener(itemClickListener);
    }

    public void show(View view) {
        int[] ints = new int[2];
        view.getLocationOnScreen(ints);
        alertDialog.getWindow().getAttributes().y = ints[1] + ScreenUtils.getStatusBarHeight(MyApplication.getContext());
        alertDialog.show();
        alertDialog.setContentView(this.view);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setDimAmount(0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;//在这定义宽度才可以居中
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.TOP;
        alertDialog.onWindowAttributesChanged(attributes);
    }

    public boolean isShowing() {
        return alertDialog.isShowing();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
}
