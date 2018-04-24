package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/9/22.
 */

public class CustomSwitch extends FrameLayout implements View.OnClickListener {
    private View mViewOn;
    private View mViewOff;
    private boolean isOn = false;
    private OnSwitchChangeListener listener;

    public CustomSwitch(@NonNull Context context) {
        this(context, null);
    }

    public CustomSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwitch(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewOff = View.inflate(context, R.layout.view_switch_off, null);
        mViewOn = View.inflate(context, R.layout.view_switch_on, null);
        addView(mViewOff);
        addView(mViewOn);
        mViewOn.setVisibility(INVISIBLE);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setSwitch(!isOn);
    }


    public void setOnSwitchChangeListener(OnSwitchChangeListener listener) {
        this.listener = listener;
    }

    public boolean isSwitchOn() {
        return isOn;
    }

    public void setSwitch(boolean open,boolean noListener) {
        if (!open) {
            mViewOff.setVisibility(VISIBLE);
            mViewOn.setVisibility(INVISIBLE);
        } else {
            mViewOff.setVisibility(INVISIBLE);
            mViewOn.setVisibility(VISIBLE);
        }
        isOn = open;
        if (listener != null && !noListener)
            listener.onChange(this,isOn);
    }
    public void setSwitch(boolean open) {
        setSwitch(open,false);
    }



    public static interface OnSwitchChangeListener {
        void onChange(CustomSwitch customSwitch,boolean change);
    }
}
