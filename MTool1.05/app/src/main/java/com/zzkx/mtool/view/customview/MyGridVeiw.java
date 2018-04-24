package com.zzkx.mtool.view.customview;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by admin on 2016/4/1.
 */
public class MyGridVeiw extends GridView {

    public MyGridVeiw(Context context) {
        super(context);
    }

    public MyGridVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridVeiw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
