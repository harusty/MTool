package com.zzkx.mtool.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.MenuCommentFragment;
import com.zzkx.mtool.view.fragment.MenuDetialFragment;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/29.
 */

public class MenuDetailActivity extends BaseActivity {
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private SparseArray<BaseFragment> mCache = new SparseArray();
    private MenuDetialFragment mMenuDetialFragment;

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        findViewById(R.id.tv_main_title).setVisibility(View.INVISIBLE);
        mTab.setVisibility(View.VISIBLE);
        mMenuDetialFragment = new MenuDetialFragment();
        mCache.put(0, mMenuDetialFragment);
        mCache.put(1, new MenuCommentFragment());
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
        initTab();
    }

    private int[] mRes = new int[]{R.mipmap.ic_menu_detail, R.mipmap.ic_conment};
    private int[] mResSelected = new int[]{R.mipmap.ic_menu_select, R.mipmap.ic_comment_select};

    private void initTab() {
        mTab.getChildAt(2).setVisibility(View.INVISIBLE);
        new TabHelper().bind(mViewPager, mTab, mResSelected, mRes);
    }

    @Override
    public void onReload() {

    }
}
