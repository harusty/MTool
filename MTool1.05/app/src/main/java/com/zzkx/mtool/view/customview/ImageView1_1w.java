package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sshss on 2017/10/10.
 */

public class ImageView1_1w extends ImageView {
    public ImageView1_1w(Context context) {
        super(context);
    }

    public ImageView1_1w(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView1_1w(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
