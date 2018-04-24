package com.zzkx.mtool.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/8/24.
 */

public class RightMenuHelper {

    private Animation mToleftAnim;
    private Animation mToRightAnim;
    private View mRightContainer;

    public RightMenuHelper(View rightContainer) {
        mRightContainer = rightContainer;
        mToleftAnim = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.to_left_reset);
        mToRightAnim = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.to_right);
        mToleftAnim.setFillAfter(true);
        mToRightAnim.setFillAfter(true);
    }

    public void show() {
        mRightContainer.startAnimation(mToleftAnim);
        mToleftAnim.start();
    }

    public void dismiss() {
        mRightContainer.startAnimation(mToRightAnim);
        mToRightAnim.start();
    }
}
