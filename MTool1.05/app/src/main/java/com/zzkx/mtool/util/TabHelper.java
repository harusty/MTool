package com.zzkx.mtool.util;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sshss on 2017/9/8.
 */

public class TabHelper {
    ViewGroup mTab;
    int mClickPosition = 0;
    int[] mRes;
    int[] mResSelected;
    private ViewPager mViewPager;
    private boolean extraFlag;

    public void setExtraFlag(boolean flag) {
        extraFlag = flag;
    }

    public void bind(ViewPager viewPager, ViewGroup topTab, int[] focusedRes, int[] res) {
        mRes = res;
        mResSelected = focusedRes;
        mTab = topTab;
        mViewPager = viewPager;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == mClickPosition || extraFlag)
                    return;
                getImageView(position).setImageResource(mResSelected[position]);
                getImageView(mClickPosition).setImageResource(mRes[mClickPosition]);
                mClickPosition = position;
                mViewPager.setCurrentItem(position);
            }
        };
        for (int i = 0; i < focusedRes.length; i++) {
            ViewGroup child = (ViewGroup) topTab.getChildAt(i);
            child.setTag(i);
            ImageView childAt = (ImageView) child.getChildAt(0);
            if (i == 0)
                childAt.setImageResource(focusedRes[0]);
            else
                childAt.setImageResource(res[i]);
            child.setOnClickListener(listener);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTab(int poi) {
        getImageView(mClickPosition).setImageResource(mRes[mClickPosition]);
        getImageView(poi).setImageResource(mResSelected[poi]);
        mClickPosition = poi;
    }

    private ImageView getImageView(int position) {
        return ((ImageView) ((ViewGroup) mTab.getChildAt(position)).getChildAt(0));
    }

    private TextView getTextView(int position) {
        return ((TextView) ((ViewGroup) mTab.getChildAt(position)).getChildAt(1));
    }
}
