package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.Dip2PxUtils;

/**
 * Created by sshss on 2017/8/24.
 */

public class MtoolRatingBar extends LinearLayout implements View.OnTouchListener {
    private int mStarMargin;
    private int mStarSize;
    private boolean mTouchable;
    private int mRating;

    public MtoolRatingBar(Context context) {
        this(context, null);
    }

    public MtoolRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MtoolRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        mStarSize = Dip2PxUtils.dip2px(context, 12);
        mStarMargin = Dip2PxUtils.dip2px(context, 3);
        setCount(5);
        setOnTouchListener(this);
    }

    public void setTouchable() {
        mTouchable = true;
    }

    public void setCount(int count) {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(mStarSize, mStarSize);
            if (i > 0)
                layoutParams.setMargins(mStarMargin, 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.mipmap.ic_star);
            addView(imageView);
        }
        int grayCount = 5 - count;
        if (grayCount == 0)
            return;
        for (int i = 0; i < grayCount; i++) {
            ImageView imageView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(mStarSize, mStarSize);
            if (grayCount == 5 && i > 0 || grayCount < 5) {
                layoutParams.setMargins(mStarMargin, 0, 0, 0);
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.mipmap.ic_star_gray);
            addView(imageView);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mTouchable)
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                caculateCount(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                 mRating = caculateCount(event);
                break;
        }
        return true;
    }

    private int caculateCount(MotionEvent event) {
        float x = (int) event.getX();
        int count = 0;
        if (x < 0) {
            count = 0;
        } else if (x > getMeasuredWidth()) {
            count = 5;
        } else {
            count = Math.round(5 * (x / getMeasuredWidth()));
        }
        if(count == 0)
            count = 1;
        setCount(count);
        return count;
    }

    public int getRating(){
        return mRating;
    }

    public void setStarOption(int size, int margin) {
        mStarSize = size;
        mStarMargin = margin;
        setCount(5);
    }
}
