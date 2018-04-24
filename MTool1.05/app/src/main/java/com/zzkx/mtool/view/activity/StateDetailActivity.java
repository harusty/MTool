package com.zzkx.mtool.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.StateCommentFragment;
import com.zzkx.mtool.view.fragment.StateDetailFragment;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateDetailActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    private SparseArray<BaseFragment> mCache;
    private StateDetailFragment mStateDetailFragment;

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitleGone();
        setMainMenuEnable();
        mTab.setVisibility(View.VISIBLE);
        mTab.getChildAt(2).setVisibility(View.INVISIBLE);
        initPager();
        TabHelper tabHelper = new TabHelper();
        tabHelper.bind(mViewPager, mTab, new int[]{R.mipmap.ic_top_1, R.mipmap.ic_top_msg}
                , new int[]{R.mipmap.ic_top_1_gray, R.mipmap.ic_top_msg_gray});
        if (getIntent().getBooleanExtra(Const.TO_REPLY, false)) {
            mViewPager.setCurrentItem(1);
        }


    }
    public void secMenuDismiss(){
        mMainMenu.dismiss(false);
    }
    private void initPager() {
        mCache = new SparseArray<>();
        mStateDetailFragment = new StateDetailFragment();
        mStateDetailFragment.setArguments(getIntent().getExtras());
        StateCommentFragment commentFragment = new StateCommentFragment();
        commentFragment.setArguments(getIntent().getExtras());
        mCache.put(0, mStateDetailFragment);
        mCache.put(1, commentFragment);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCache.get(position);
            }

            @Override
            public int getCount() {
                return mCache.size();
            }
        });
    }

    @Override
    public void onReload() {

    }

    public void setCollected(int isCollected) {
        View child = mMainMenu.getSecMenuItem(1);
        ImageView imageView = (ImageView) child.findViewById(R.id.image);
        TextView textView = (TextView) child.findViewById(R.id.text);
        if (isCollected == 1) {
            imageView.setImageResource(R.mipmap.ic_star_empty_red);
            textView.setText("取消收藏");
        } else {
            imageView.setImageResource(R.mipmap.ic_star_gray_empty);
            textView.setText("收        藏");
        }
    }
}
