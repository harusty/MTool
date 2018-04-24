package com.zzkx.mtool.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.activity.CartActivity;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/2.
 */

public class CartContainerFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.scrolable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.tab_strip)
    PagerSlidingTabStrip mTabStrip;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_order_info)
    TextView mTvOrderInfo;
    private SparseArray<CartFragment> mCache;
    private String[] mTitles;
    private ViewPager mParentViewPager;
    private CartActivity mActivity;

    @Override
    public int getContentRes() {
        return R.layout.fragment_cart_contailner;
    }

    @Override
    public void initViews() {
        mActivity = (CartActivity) getActivity();
        mTitles = getResources().getStringArray(R.array.cartTabs);
        setTitleDisable();
        mCache = new SparseArray<>();
        mCache.put(0, createFragment(0));
        mCache.put(1, createFragment(1));
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mCache.get(0));
        mParentViewPager = ((CartActivity) getActivity()).getViewPager();
        mBaseView.findViewById(R.id.tv_submit).setOnClickListener(this);
        initViewPager();
    }

    private double mTotalPrice = 0;
    private double mTypeInPrice = 0;
    private double mTypeOutPrice = 0;
    private int mTypeInNum = 0;
    private int mTypeOutNum = 0;


    public void showTopOrder(double totalPrice, int menuNum, int type) {
        double peisong = 0;
        if (mActivity.mOutCusListBean != null)
            peisong = mActivity.mOutCusListBean.totalPeisong;
        if (type == CartCacheUtil.TYPE_OUT) {
            mActivity.mTotalPrice = mTotalPrice = totalPrice + mTypeInPrice;
            mActivity.mTypeOutPrice = mTypeOutPrice = totalPrice;
            mActivity.mTypeOutNum = mTypeOutNum = menuNum;
            mActivity.mPeisong = peisong;
//            if (mTypeOutPrice == peisong) {
//                mTypeOutPrice = 0;
//                mTotalPrice -= mActivity.mOutCusListBean.totalPeisong;
//            }
        } else {
            mActivity.mTotalPrice = mTotalPrice = totalPrice + mTypeOutPrice;
            mActivity.mTypeInPrice = mTypeInPrice = totalPrice;
            mActivity.mTypeInNum = mTypeInNum = menuNum;
        }
//        if (mTypeOutNum == 0) {
//            mTypeOutPrice = 0;
//        }
        mTvTotalPrice.setText(String.valueOf(mTotalPrice));

        mTvOrderInfo.setText("外送：" + mTypeOutPrice + "（" + mTypeOutNum + "份）/到店：" + mTypeInPrice + "（" + mTypeInNum + "份）");
        mActivity.setViewPagerScrollEnable();
    }

    private CartFragment createFragment(int type) {
        CartFragment cartFragment = new CartFragment();
        Bundle bundle = new Bundle();
//        String shopId = getIntent().getStringExtra(Const.CUS_SHOP_ID);
//        bundle.putString(Const.CUS_SHOP_ID, shopId);
        bundle.putInt(Const.TYPE, type);
        cartFragment.setArguments(bundle);
        return cartFragment;
    }

    @Override
    public void initNet() {
        for (int i = 0; i < mCache.size(); i++) {
            mCache.get(i).initNet();
        }
    }

    private void initViewPager() {
        mViewPager.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCache.get(position);
            }

            @Override
            public int getCount() {
                return mCache.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mTabStrip.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    mTabStrip.setIndicatorColor(getResources().getColor(R.color.blue));
                else
                    mTabStrip.setIndicatorColor(getResources().getColor(R.color.ligthBlue));
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mCache.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onReload() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                mParentViewPager.setCurrentItem(1, true);
                break;
        }
    }


    public void notifyDatasetChanged() {
        mCache.get(0).notifyDataSetChanged();
        mCache.get(1).notifyDataSetChanged();
    }

    public int getCurPageType() {
        return mViewPager.getCurrentItem();
    }

    public void refreshTopOrderInfo() {
        mCache.get(mViewPager.getCurrentItem()).resetTopOrderInfo();
    }
}
