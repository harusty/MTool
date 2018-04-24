package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by sshss on 2017/12/13.
 */

public class RelativeLayout1_1w extends RelativeLayout{
    public RelativeLayout1_1w(@NonNull Context context) {
        super(context);
    }

    public RelativeLayout1_1w(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayout1_1w(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
