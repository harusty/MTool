package com.zzkx.mtool.view.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by sshss on 2017/9/4.
 */

public class ExpandableView extends LinearLayout implements View.OnClickListener {
    private boolean isExpand = true;
    private boolean isAnimating;
    //    private ValueAnimator mExAnim;
//    private ValueAnimator mPacUpAnim;
    private int mHeight;
    private int mToHeight;
    private int mMeasureMode;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    public ExpandableView(Context context) {
        this(context, null);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() > mHeight) {
            mHeight = getMeasuredHeight();
            if (!isExpand) {
                getLayoutParams().height = 0;
            }
        }
//        System.out.println("height:"+getMeasuredHeight());
//        if (isExpand) {
//            mHeight = getMeasuredHeight();
//        }

//        else{
//
//        }
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mHeight = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void toggle() {
        if (isAnimating)
            return;
        if (isExpand) {
            packUp();
        } else {
            expand();
        }
    }

    public void expand() {
        createAnimation(0, mHeight).start();
        isExpand = !isExpand;
    }

    public void packUp() {
        createAnimation(mHeight, 0).start();
        isExpand = !isExpand;

    }


    private ValueAnimator createAnimation(int from, int to) {
        ValueAnimator animation = ValueAnimator.ofInt(from, to);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        if (mUpdateListener == null)
            mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    getLayoutParams().height = (int) animation.getAnimatedValue();
                    requestLayout();
                }
            };
        animation.addUpdateListener(mUpdateListener);
        if (mAnimatorListener == null)
            mAnimatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isExpand)
                        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        animation.addListener(mAnimatorListener);
        return animation;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setTriggerView(View triggerView) {
        triggerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
