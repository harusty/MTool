package com.zzkx.mtool.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.UserStateImageFragment;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.MyStateFragement;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/11.
 */

public class MyStateActivity extends BaseActivity {
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
        mTab.getChildAt(2).setVisibility(View.INVISIBLE);
        mCache.put(0, new MyStateFragement());
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
        TabHelper tabHelper = new TabHelper();
        tabHelper.bind(mViewPager, mTab, new int[]{R.mipmap.ic_list_red, R.mipmap.ic_top_image_red2}
                , new int[]{R.mipmap.ic_list_gray, R.mipmap.ic_top_image_gray2});
    }

    @Override
    public void onReload() {

    }
}
