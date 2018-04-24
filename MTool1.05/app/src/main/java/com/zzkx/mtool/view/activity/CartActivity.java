package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.CustomViewPager;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.CartContainerFragment;
import com.zzkx.mtool.view.fragment.OrderConfrimFragment;
import com.zzkx.mtool.view.fragment.PayFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/2.
 */

public class CartActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTopTab;
    private SparseArray<BaseFragment> mCache;

    public double mTotalPrice = 0;
    public double mTypeInPrice = 0;
    public double mTypeOutPrice = 0;
    public double mPeisong = 0;

    public int mTypeInNum = 0;
    public int mTypeOutNum = 0;

    //两种订单总和
    public List<MenuListBean.DataBean> mShopsTypeOut = new ArrayList<>();
    public List<MenuListBean.DataBean> mShopsTypeIn = new ArrayList<>();

    public CusMenuListBean mOutCusListBean;
    public CusMenuListBean mInCusListBean;

    public boolean mTypeOutLoaded;
    public boolean mTypeInLoaded;
    private CartContainerFragment mCartContainerFragment;
    private TabHelper mTabHelper;


    public void setDataLoaded(int type) {
        if (type == CartCacheUtil.TYPE_IN)
            mTypeInLoaded = true;
        else
            mTypeOutLoaded = true;
        setViewPagerScrollEnable();
    }

    public void setViewPagerScrollEnable() {
        boolean flag = mTypeInLoaded && mTypeOutLoaded && mTotalPrice - mPeisong > 0;
        mViewPager.setTouchEnable(flag);
        mTabHelper.setExtraFlag(!flag);
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_cart;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mCartContainerFragment.initNet();
    }

    @Override
    public void initViews() {
        EventBus.getDefault().register(this);
        setMainMenuEnable();
        setMainTitleGone();
        setSecMenu(new int[]{R.mipmap.ic_text_gray, R.mipmap.ic_tras_can},
                new String[]{"提交订单", "删除已选"},
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mViewPager.isTouchable()) {
                            ToastUtils.showToast("正在加载数据，请稍后再试");
                            return;
                        }
                        int position = (int) v.getTag();
                        switch (position) {
                            case 0:
                                mViewPager.setCurrentItem(1, true);
                                break;
                            case 1:
                                removeSelectMenus();
                                break;
                        }
                        mMainMenu.dismiss(false);
                    }
                });
        mTopTab.setVisibility(View.VISIBLE);
        mCache = new SparseArray<>();
        mCartContainerFragment = new CartContainerFragment();
        mCache.put(0, mCartContainerFragment);
        mCache.put(1, new OrderConfrimFragment());
        mCache.put(2, new PayFragment());

        initViewPager();
        mTopTab.setEnabled(false);
    }

    private void removeSelectMenus() {
        showProgress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int type = mCartContainerFragment.getCurPageType();
                CusMenuListBean cusMenuListBean;
                List<MenuListBean.FoodInfoListBean> foodInfoList;
                if (type == CartCacheUtil.TYPE_OUT)
                    cusMenuListBean = mOutCusListBean;
                else
                    cusMenuListBean = mInCusListBean;

                foodInfoList = cusMenuListBean.menuList;
                if (foodInfoList == null || foodInfoList.size() == 0)
                    return;
                Iterator<MenuListBean.FoodInfoListBean> iterator = foodInfoList.iterator();
                while (iterator.hasNext()) {
                    MenuListBean.FoodInfoListBean foodBean = iterator.next();
                    if (foodBean.cusIsChecked) {

                        MenuListBean.DataBean dataBean = mShopsTypeOut.get(foodBean.cusGroupPotision);
                        dataBean.cusSelectCount--;
                        dataBean.cusOriginSelectCount--;
                        dataBean.cusShopOrderPrice -= foodBean.cusMenuTotalPrice;
                        cusMenuListBean.totalMenuNum -= foodBean.cusCount;
                        cusMenuListBean.cusTotalPrice -= foodBean.cusMenuTotalPrice;

                        iterator.remove();
//                        CartCacheUtil.removeMenu(foodBean.restaurantsId, foodBean.id, CartCacheUtil.TYPE_OUT);
                        foodBean.cusIsChecked = false;
                    }
                }
//                CartCacheUtil.cacheMenus(CartCacheUtil.TYPE_OUT);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(Const.RESULT_SUCESS_CODE);
                        mCartContainerFragment.notifyDatasetChanged();
                        mCartContainerFragment.refreshTopOrderInfo();
                        showProgress(false);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPayResult(BusPayBean payBean) {
        ((PayFragment) mCache.get(2)).dismissDialog();
        ((OrderConfrimFragment) mCache.get(1)).showParentProgress(false);
        switch (payBean.mCode) {
            case BusPayBean.SUCESS:
                startActivity(new Intent(this, HistorOrderListActivity.class));
                clearCartCache();
                finish();
                break;
            case BusPayBean.FAILD:
                mViewPager.setCurrentItem(1);
                break;
            case BusPayBean.CANCLE:
                mViewPager.setCurrentItem(1);
                break;
        }
    }


    //清空已支付成功的缓存
    private void clearCartCache() {
        for (MenuListBean.DataBean shopBean : mShopsTypeOut) {
            CartCacheUtil.getInstance().clearShopMenus(shopBean.id, CartCacheUtil.TYPE_OUT);
        }
        for (MenuListBean.DataBean shopBean : mShopsTypeIn) {
            CartCacheUtil.getInstance().clearShopMenus(shopBean.id, CartCacheUtil.TYPE_IN);
        }

    }

    private int[] mRes = new int[]{R.mipmap.ic_cart_gray_2, R.mipmap.ic_text_gray, R.mipmap.ic_wallete_gray};
    private int[] mResSelected = new int[]{R.mipmap.ic_cart_red2, R.mipmap.ic_text_red, R.mipmap.ic_wallete_red};
    private int mClickPosition = 0;

    private int curPage;

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(3);

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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != curPage && positionOffset == 0 && positionOffsetPixels == 0) {
                    curPage = position;
                    mCache.get(position).onPageSelected();
                    if (curPage == 0) {
                        mMainMenu.showMianSecMenu();
                    } else {
                        mMainMenu.hideMainSecMenu();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabHelper = new TabHelper();
        mTabHelper.bind(mViewPager, mTopTab, mResSelected, mRes);
    }


    public CustomViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                toSubmit();
                break;
        }
    }


    public void toSubmit() {
        ((OrderConfrimFragment) mCache.get(1)).submitOrder();
    }

    public void resetFragmentSate(boolean show) {
        mViewPager.setCurrentItem(1);
        mViewPager.setTouchEnable(!show);
        mCache.get(2).showProgress(false);
    }
}
