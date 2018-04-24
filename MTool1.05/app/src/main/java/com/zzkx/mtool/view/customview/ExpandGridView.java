package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by sshss on 2017/8/23.
 */

public class ExpandGridView extends GridView {
    public ExpandGridView(Context context) {
        this(context,null);
    }

    public ExpandGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExpandGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
