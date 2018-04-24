package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.zzkx.mtool.R;

import static android.R.attr.checked;

/**
 * Created by sshss on 2017/9/4.
 */

public class CartChekBox extends ImageView implements Checkable {
    private int checkedRes = R.mipmap.ic_check_red;
    private int unCheckedRes = R.mipmap.ic_check_gray;
    private boolean mChecked;

    public CartChekBox(Context context) {
        this(context, null);
    }

    public CartChekBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartChekBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
}
