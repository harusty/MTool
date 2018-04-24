package com.hyphenate.easeui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

import com.hyphenate.easeui.R;


/**
 * Created by sshss on 2017/9/4.
 */

public class RectChekBox extends ImageView implements Checkable, View.OnClickListener {
    private int checkedRes = R.drawable.ic_rect_check_red;
    private int unCheckedRes = R.drawable.ic_rect_check_gray;
    private boolean mChecked;
    private OnCheckChangeListener mListener;
    private boolean mTouchable = true;

    public RectChekBox(Context context) {
        this(context, null);
    }

    public RectChekBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectChekBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(unCheckedRes);
        this.setOnClickListener(this);
    }

    public void setBoxRes(int check, int unCheckedRes) {
        checkedRes = check;
        this.unCheckedRes = unCheckedRes;
        setImageResource(unCheckedRes);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mChecked) {
            setImageResource(checkedRes);
        } else {
            setImageResource(unCheckedRes);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        if (mChecked) {
            setImageResource(checkedRes);
        } else {
            setImageResource(unCheckedRes);
        }
    }

    @Override
    public void onClick(View v) {
        if (!mTouchable)
            return;
        toggle();
        if (mListener != null)
            mListener.onChange(this, mChecked);
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        mListener = listener;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchable)
            return super.onTouchEvent(event);
        else
            return false;
    }

    public static interface OnCheckChangeListener {
        void onChange(RectChekBox chekBox, boolean b);
    }
}
