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
import com.zzkx.mtool.view.fragment.ShopGallaryFragment;
import com.zzkx.mtool.view.fragment.ShopCommentFragment;
import com.zzkx.mtool.view.fragment.ShopInfoFragment;
import com.zzkx.mtool.view.iview.IView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/13.
 */

public class ShopDetailActivity extends BaseActivity implements IView {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    private SparseArray<BaseFragment> mCache;

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
        TabHelper tabHelper = new TabHelper();
        tabHelper.bind(mViewPager, mTab, new int[]{R.mipmap.ic_top_shop, R.mipmap.ic_top_msg, R.mipmap.ic_top_image}
                , new int[]{R.mipmap.ic_top_shop_gray, R.mipmap.ic_top_msg_gray, R.mipmap.ic_top_image_gray});
    }

    private void initPager() {
        mCache = new SparseArray<>();
        mCache.put(0, new ShopInfoFragment());
        mCache.put(1, new ShopCommentFragment());
        mCache.put(2, new ShopGallaryFragment());

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
