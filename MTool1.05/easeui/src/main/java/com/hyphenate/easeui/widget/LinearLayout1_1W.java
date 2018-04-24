package com.hyphenate.easeui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by sshss on 2017/11/3.
 */

public class LinearLayout1_1W extends LinearLayout {
    public LinearLayout1_1W(Context context) {
        super(context);
    }

    public LinearLayout1_1W(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayout1_1W(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
