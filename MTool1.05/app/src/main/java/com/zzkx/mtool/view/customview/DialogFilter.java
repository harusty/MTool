package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.Dip2PxUtils;

/**
 * Created by sshss on 2017/9/13.
 */

public class DialogFilter {
    private OnMenuClick mOnMenuClick;
    private Context mContext;
    private ViewGroup mMainContainer;
    private ViewGroup mSubContainer;
    private PopupWindow mRightFilterView;


    public DialogFilter(Context context, OnMenuClick onMenuClick) {
        mOnMenuClick = onMenuClick;
        mContext = context;
        mRightFilterView = new PopupWindow(context, null, 0, R.style.AppTheme);
        mRightFilterView.setFocusable(true);
        mRightFilterView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = View.inflate(context, R.layout.popup_filter, null);
        mRightFilterView.setContentView(view);
        mRightFilterView.setWidth(Dip2PxUtils.dip2px(context, 277));
        mRightFilterView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mRightFilterView.setAnimationStyle(R.style.RightRangeSilde);
        mMainContainer = (ViewGroup) view.findViewById(R.id.main_container);
        mSubContainer = (ViewGroup) view.findViewById(R.id.sub_container);
        view.findViewById(R.id.ic_cacle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRightFilterView.dismiss();
            }
        });
        initChild();
    }
    public void dismiss(){
        mRightFilterView.dismiss();
    }
    private int[] mMainRes = new int[]{R.mipmap.ic_filter_food, R.mipmap.ic_filter_entertain, R.mipmap.ic_filter_hotel};
    private int[] mMainResUnSelect = new int[]{R.mipmap.ic_filter_food_gray, R.mipmap.ic_filter_entertain_gray, R.mipmap.ic_filter_hotel_gray};
    private int[] mSubRes = new int[]{R.mipmap.ic_filter_all, R.mipmap.ic_filter_waimai, R.mipmap.ic_filter_to_shop};
    private int[] mSubResUnSelect = new int[]{R.mipmap.ic_filter_all_gray, R.mipmap.ic_filter_waimai_gray, R.mipmap.ic_filter_to_shop_gray};

    private ViewHolder mLastMainHolder;
    private ViewHolder mLastSubHolder;
    private ViewHolder curSubHolder;

    private void initChild() {
        View.OnClickListener mainClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if (holder.position == mLastMainHolder.position)
                    return;
                if (holder.position == 0)
                    mSubContainer.setVisibility(View.VISIBLE);
                else
                    mSubContainer.setVisibility(View.GONE);
                holder.name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.icon.setImageResource(mMainRes[holder.position]);

                mLastMainHolder.name.setTextColor(mContext.getResources().getColor(R.color.textHintColor));
                mLastMainHolder.icon.setImageResource(mMainResUnSelect[mLastMainHolder.position]);
                mLastMainHolder = holder;


            }
        };
        for (int i = 0; i < mMainContainer.getChildCount(); i++) {
            ViewGroup child = (ViewGroup) mMainContainer.getChildAt(i);
            ViewHolder viewHolder = new ViewHolder(child, i);
            child.setTag(viewHolder);
            child.setOnClickListener(mainClickListener);
            if (i == 0)
                mLastMainHolder = viewHolder;
        }


        View.OnClickListener subClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if (holder.position == mLastSubHolder.position)
                    return;
                holder.name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.icon.setImageResource(mSubRes[holder.position]);

                mLastSubHolder.name.setTextColor(mContext.getResources().getColor(R.color.textHintColor));
                mLastSubHolder.icon.setImageResource(mSubResUnSelect[mLastSubHolder.position]);
                mLastSubHolder = holder;
                mOnMenuClick.onClick(holder.position);
            }
        };
        for (int i = 0; i < mSubContainer.getChildCount(); i++) {
            ViewGroup child = (ViewGroup) mSubContainer.getChildAt(i);
            ViewHolder viewHolder = new ViewHolder(child, i);
            child.setTag(viewHolder);
            child.setOnClickListener(subClickListener);
            if (i == 0)
                mLastSubHolder = viewHolder;
        }
    }

    public void toggleRightFilter(View view) {
        if (mRightFilterView.isShowing()) {
            mRightFilterView.dismiss();
        } else {
            mRightFilterView.showAsDropDown(view, 0, -view.getMeasuredHeight());
        }
    }

    private class ViewHolder {
        public TextView name;
        public ImageView icon;
        public int position;

        public ViewHolder(ViewGroup child, int position) {
            this.position = position;
            name = (TextView) child.getChildAt(1);
            icon = (ImageView) child.getChildAt(0);
        }
    }


    public interface OnMenuClick {
        void onClick(int position);
    }
}
