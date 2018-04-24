package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2018/1/11.
 */

public class DialogMoreMenu extends SimpleDialog {

    private OnMenuClickListener mListener;
    private View.OnClickListener mOnClickListener;
    private ViewGroup mMenuContainer;

    public DialogMoreMenu(Context context, OnMenuClickListener listener) {
        super(context, R.layout.dialog_more_menu);
        mListener = listener;
        getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMenuContainer = (ViewGroup) findViewById(R.id.layout_menu_container);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mListener.onMenuClick(position,mListPosition);
            }
        };
    }

    private int mListPosition;
    public void show(int listPosition) {
        super.show();
        mListPosition = listPosition;
    }
    public void addMenu(int imageRes, String name) {
        View item = View.inflate(mContext, R.layout.item_more_menu, null);
        ((ImageView) item.findViewById(R.id.iv_icon)).setImageResource(imageRes);
        ((TextView) item.findViewById(R.id.tv_name)).setText(name);
        item.setOnClickListener(mOnClickListener);
        item.setTag(mMenuContainer.getChildCount());
        mMenuContainer.addView(item);
    }

    public void setMenu(int imageRes, String name, int position) {
        if (mMenuContainer != null && position < mMenuContainer.getChildCount()) {
            View item = mMenuContainer.getChildAt(position);
            ((ImageView) item.findViewById(R.id.iv_icon)).setImageResource(imageRes);
            ((TextView) item.findViewById(R.id.tv_name)).setText(name);
        }
    }

    public void setVisiblity(int position,int visiblity){
        if (mMenuContainer != null && position < mMenuContainer.getChildCount()) {
            mMenuContainer.getChildAt(position).setVisibility(visiblity);
        }
    }


    public void show(OnMenuClickListener listener) {
        mListener = listener;
        super.show();
    }

    public interface OnMenuClickListener {
        void onMenuClick(int menuPosition, int listPosition);
    }
}
