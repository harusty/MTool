package com.zzkx.mtool.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.StateNotifyFragment;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/1.
 */

public class StateHelperActivity extends BaseActivity {
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private SparseArray<BaseFragment> mCache = new SparseArray();


    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }


    @Override
    public void initViews() {
        setMainMenuEnable();
        findViewById(R.id.tv_main_title).setVisibility(View.INVISIBLE);
        mTab.setVisibility(View.VISIBLE);
        mTab.getChildAt(2).setVisibility(View.GONE);

        mCache.put(0, createFragment(Const.STATE_ACTION_AT));
        mCache.put(1, createFragment(Const.STATE_ACTION_SUPPORT));

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
        new TabHelper().bind(mViewPager, mTab, new int[]{R.mipmap.ic_12, R.mipmap.ic_good_red},
                new int[]{R.mipmap.ic_11, R.mipmap.ic_10});
    }

    private StateNotifyFragment createFragment(String action) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ACTION, action);
        StateNotifyFragment fragment = new StateNotifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onReload() {

    }
}
