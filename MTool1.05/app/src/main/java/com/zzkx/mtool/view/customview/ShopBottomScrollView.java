package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.zzkx.mtool.util.Dip2PxUtils;

/**
 * Created by sshss on 2017/8/31.
 */

public class ShopBottomScrollView extends ScrollView {
    private int mMaxHeight;

    public ShopBottomScrollView(Context context) {
        this(context, null);
    }

    public ShopBottomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopBottomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaxHeight = Dip2PxUtils.dip2px(context, 65 * 4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = getMeasuredHeight();
        if (heightSize > mMaxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
