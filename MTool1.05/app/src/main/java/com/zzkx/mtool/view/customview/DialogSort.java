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

/**
 * Created by sshss on 2017/9/13.
 */

public class DialogSort {
    private Context mContext;
    private ViewGroup mMainContainer;
    private PopupWindow mRightFilterView;
    private OnSortListener mSortListener;


    public DialogSort(Context context) {
        this(context, null, null, null);
    }

    public DialogSort(Context context, int[] res, int[] resUnselect, String[] titles) {
        mContext = context;
        mRightFilterView = new PopupWindow(context, null, 0, R.style.AppTheme);
        mRightFilterView.setFocusable(true);
        mRightFilterView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = View.inflate(context, R.layout.popup_sort, null);
        mRightFilterView.setContentView(view);
        mRightFilterView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mRightFilterView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mRightFilterView.setAnimationStyle(R.style.RightRangeSilde);
        mMainContainer = (ViewGroup) view.findViewById(R.id.main_container);
        view.findViewById(R.id.ic_cacle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRightFilterView.dismiss();
            }
        });
        if (res != null && resUnselect != null) {
            mMainRes = res;
            mMainResUnSelect = resUnselect;
            mTitles = titles;
        }
        initChild();
    }

    public void setOnSortListener(OnSortListener clickListener) {
        mSortListener = clickListener;
    }

    private int[] mMainRes = new int[]{R.mipmap.ic_sort_distance, R.mipmap.ic_sort_comment, R.mipmap.ic_sort_sales};
    private int[] mMainResUnSelect = new int[]{R.mipmap.ic_sort_distance_gray, R.mipmap.ic_sort_conmment_gray, R.mipmap.ic_sort_sales_gray};
    private String[] mTitles = new String[]{"按距离排序", "按评分排序", "按销量排序"};
    private ViewHolder mLastMainHolder;

    private void initChild() {
        View.OnClickListener mainClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                boolean toHandle = false;
                if (mSortListener != null) {
                    toHandle = mSortListener.onSort(holder);
                }

                if (toHandle) {
                    handleSelect(holder);
                }
            }
        };
        for (int i = 0; i < mMainRes.length; i++) {
            View child = View.inflate(mContext, R.layout.item_dialog_sort, null);
            mMainContainer.addView(child);
            ViewHolder viewHolder = new ViewHolder(child, i);
            if (i == 0) {
                viewHolder.icon.setImageResource(mMainRes[i]);
            } else {
                viewHolder.icon.setImageResource(mMainResUnSelect[i]);
            }
            viewHolder.name.setText(mTitles[i]);
            child.setTag(viewHolder);
            child.setOnClickListener(mainClickListener);
            if (i == 0)
                mLastMainHolder = viewHolder;

        }
    }

    public void handleSelect(ViewHolder holder) {
        if (holder.position == mLastMainHolder.position)
            return;
        //                holder.searchName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        holder.icon.setImageResource(mMainRes[holder.position]);
//                mLastMainHolder.searchName.setTextColor(mContext.getResources().getColor(R.color.textTmp2));
        mLastMainHolder.icon.setImageResource(mMainResUnSelect[mLastMainHolder.position]);
        mLastMainHolder = holder;
        mRightFilterView.dismiss();
    }

    public void toggleRightFilter(View view) {
        if (mRightFilterView.isShowing()) {
            mRightFilterView.dismiss();
        } else {
            mRightFilterView.showAsDropDown(view, 0, -view.getMeasuredHeight());
        }
    }

    public void dismiss() {
        mRightFilterView.dismiss();
    }

    public static class ViewHolder {
        public TextView name;
        public ImageView icon;
        public int position;

        public ViewHolder(View child, int position) {
            this.position = position;
            name = (TextView) child.findViewById(R.id.tv_title);
            icon = (ImageView) child.findViewById(R.id.iv_logo);
        }
    }

    public static interface OnSortListener {
        boolean onSort(ViewHolder sortKey);
    }
}
