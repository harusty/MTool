package com.zzkx.mtool.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.UserStateImageFragment;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.UserDetailFragment;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/22.
 */

public class UserDetailActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    private SparseArray<BaseFragment> mCache;
    private String mId;

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {

        setMainTitleGone();
        setMainMenuEnable();
        mTab.setVisibility(View.VISIBLE);
        initPager();
        mTab.getChildAt(2).setVisibility(View.INVISIBLE);
        new TabHelper().bind(mViewPager, mTab, new int[]{R.mipmap.ic_list_red, R.mipmap.ic_top_image_red2}
                , new int[]{R.mipmap.ic_list_gray, R.mipmap.ic_top_image_gray2});
        mId = getIntent().getStringExtra(Const.ID);
    }

    private void initPager() {
        mCache = new SparseArray<>();
        UserDetailFragment userDetailFragment = new UserDetailFragment();
        userDetailFragment.setArguments(getIntent().getExtras());
        mCache.put(0, userDetailFragment);
        mCache.put(1, new UserStateImageFragment());

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
}
