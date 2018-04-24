package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseListBean;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.ShopDetailBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.imple.BottomCartCtrlListener;
import com.zzkx.mtool.presenter.ShopDetailPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.customview.DialogCart;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.MainMenu;
import com.zzkx.mtool.view.customview.MtoolRatingBar;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;
import com.zzkx.mtool.view.fragment.ShopFragment;
import com.zzkx.mtool.view.iview.IShopDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/24.
 */

public class ShopActivity extends BaseActivity implements View.OnClickListener, IShopDetailView {
    @BindView(R.id.rootView)
    ViewGroup mRootView;
    @BindView(R.id.scrolable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    ViewGroup mTab;
    @BindView(R.id.cartLayout)
    ViewGroup mCartContainer;
    @BindView(R.id.transView)
    View mTransView;
    @BindView(R.id.bottom_layout_out)
    View mBottomLayoutOut;
    @BindView(R.id.bottom_layout_in)
    View mBottomLayoutIn;

    @BindView(R.id.iv_shop_logo)
    ImageView mIvShopLogo;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.tv_rating_serv)
    TextView mTvRatingServ;
    @BindView(R.id.tv_rating_user)
    TextView mTvRatingUser;
    @BindView(R.id.rating_serv)
    MtoolRatingBar mRatingServ;
    @BindView(R.id.rating_user)
    MtoolRatingBar mRatingUser;
    @BindView(R.id.tv_intro)
    TextView mShopIntro;

    private SparseArray<ShopFragment> mCache = new SparseArray<>();
    private DialogCart mDialogCartOut;
    private DialogCart mDialogCartIn;
    private MainMenu mFakeMenu;
    private int mType;
    private ViewHolder mViewHolderIn;
    private ViewHolder mViewHolderOut;
    private String mPeisongfei;
    private double mQisong;
    private String mRenjun;
    private boolean mNeedRefresh;
    private View mShopLayout;
    private String mShopId;
    private ShopDetailPresenter mShopDetailPresenter;
    public static ShopActivity sInstance;
    private DialogState mDialogState;
    private ShopDetailBean.MerchantRestaurantsDoBean mShopInfo;


    @Subscribe
    public void onPayResult(BusPayBean payBean) {
        mNeedRefresh = payBean.mCode == BusPayBean.SUCESS;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtil.getBoolean(Const.LEFT_MODE, false)) {
            findViewById(R.id.ic_left).setVisibility(View.GONE);
            findViewById(R.id.ic_left2).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ic_left).setVisibility(View.VISIBLE);
            findViewById(R.id.ic_left2).setVisibility(View.GONE);
        }
        //支付成功刷新数据与缓存同步
        if (mNeedRefresh) {
            ((ShopFragment) mCache.get(0)).refreshCache();
            ((ShopFragment) mCache.get(1)).refreshCache();
        }
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_shop;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        sInstance = null;
    }

    private int[] mRes = new int[]{R.mipmap.ic_bike, R.mipmap.ic_shop};
    private int[] mResSelected = new int[]{R.mipmap.ic_bike_selected, R.mipmap.ic_shop_selected};

    @Override
    public void showData(ShopDetailBean bean) {
        ShopDetailBean.DataBean data = bean.data;
        if (data != null) {
            mShopInfo = data.merchantRestaurantsDo;
            if (mShopInfo != null) {
                mTvShopName.setText(mShopInfo.name);
                GlideUtil.getInstance().display(mIvShopLogo, mShopInfo.logoUrl);
                mRatingServ.setCount(mShopInfo.serviceScore);
                mTvRatingServ.setText(mShopInfo.serviceScore + "");
                mRatingUser.setCount(mShopInfo.priceScore);
                mTvRatingUser.setText(mShopInfo.priceScore + "");
                mPeisongfei = mShopInfo.toHomeTip;
                mRenjun = mShopInfo.avgConsume;
                mQisong = mShopInfo.deliverAmount;
                mShopIntro.setText(mShopInfo.description);
                if (mShopInfo.takeInService == 0 && mShopInfo.takeOutService == 0) {
                    ToastUtils.showToast("???????????????");
                    return;
                }
                if (mShopInfo.takeInService == 1 && mShopInfo.takeOutService == 1) {
                    mCache.put(0, createFragment(0));
                    mCache.put(1, createFragment(1));
                } else if (mShopInfo.takeInService == 1) {
                    mCache.put(0, createFragment(1));
                    mRes = new int[]{R.mipmap.ic_shop};
                    mResSelected = new int[]{R.mipmap.ic_shop_selected};
                } else if (mShopInfo.takeOutService == 1) {
                    mRes = new int[]{R.mipmap.ic_bike};
                    mResSelected = new int[]{R.mipmap.ic_bike_selected};
                    mCache.put(0, createFragment(0));
                }
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mCache.get(0));
                initViewPager();

                new TabHelper().bind(mViewPager, mTab, mResSelected, mRes);
            }
        }
    }

    @Override
    public void initViews() {

        sInstance = this;
        EventBus.getDefault().register(this);
        setTitleDisable();
        setMainMenuEnable();
        initSecondMenu();
        mShopId = getIntent().getStringExtra(Const.ID);
        mShopDetailPresenter = new ShopDetailPresenter(this, mShopId);

        //fakeMenu
        mFakeMenu = new MainMenu(this);
        mRootView.addView(mFakeMenu, 2);

        findViewById(R.id.tv_main_title).setVisibility(View.INVISIBLE);
        findViewById(R.id.ic_left).setOnClickListener(this);
        mTab = (ViewGroup) mRootView.findViewById(R.id.shop_top_tab);
        mTab.setVisibility(View.VISIBLE);

        mShopLayout = findViewById(R.id.layout_shop_info);
        mShopLayout.setOnClickListener(this);
        mTab.getChildAt(2).setVisibility(View.INVISIBLE);
        findViewById(R.id.ic_left2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initBottomViews();
        initBottomDialog();

    }


    private void initBottomViews() {
        mViewHolderIn = new ViewHolder(mBottomLayoutIn);
        mViewHolderIn.iv_edit.setOnClickListener(this);
        mViewHolderIn.iv_edit.setTag(CartCacheUtil.TYPE_IN);
        mViewHolderIn.iv_cart.setOnClickListener(this);
        mViewHolderOut = new ViewHolder(mBottomLayoutOut);
        mViewHolderOut.iv_edit.setOnClickListener(this);
        mViewHolderOut.iv_cart.setOnClickListener(this);
        mViewHolderOut.tv_min_comsum.setText("起送价：" + mQisong + "元");

        mViewHolderOut.iv_edit.setTag(CartCacheUtil.TYPE_OUT);
        mViewHolderIn.tv_min_comsum.setVisibility(View.INVISIBLE);
    }

    private class ViewHolder {
        TextView tv_cart_info;
        TextView tv_min_comsum;
        View layout_cart;
        View iv_edit;
        View iv_cart;

        public ViewHolder(View bottomLayoutIn) {
            tv_cart_info = (TextView) bottomLayoutIn.findViewById(R.id.tv_cart_info);
            tv_min_comsum = (TextView) bottomLayoutIn.findViewById(R.id.tv_min_comsum);
            layout_cart = bottomLayoutIn.findViewById(R.id.layout_cart);
            iv_edit = bottomLayoutIn.findViewById(R.id.iv_edit);
            iv_cart = bottomLayoutIn.findViewById(R.id.iv_cart);
        }
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(2);
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
            }

            @Override
            public void onPageSelected(int position) {
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mCache.get(position));
//                mType = position;
                mType = mCache.get(position).getType();
                if (mType == CartCacheUtil.TYPE_OUT) {
                    mBottomLayoutIn.setVisibility(View.INVISIBLE);
                    mBottomLayoutOut.setVisibility(View.VISIBLE);
                } else {
                    mBottomLayoutIn.setVisibility(View.VISIBLE);
                    mBottomLayoutOut.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int index = getIntent().getIntExtra(Const.INDEX, 0);
        mViewPager.setCurrentItem(index, true);
    }

    private ShopFragment createFragment(int type) {
        ShopFragment shopFragment1 = new ShopFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Const.CUS_SHOP_ID, mShopId);
        bundle1.putInt(Const.TYPE, type);
        shopFragment1.setArguments(bundle1);
        return shopFragment1;
    }

    private void initBottomDialog() {
        mDialogCartOut = new DialogCart(this, mCartContainer, mTransView, mMainMenu, mFakeMenu, mShopId);
        mDialogCartIn = new DialogCart(this, mCartContainer, mTransView, mMainMenu, mFakeMenu, mShopId);
    }

    /**
     * 这里不能用mType!!!
     * 执行完才算把两个对话框初始化完成。
     *
     * @param listener
     */
    public void setBottomListener(BottomCartCtrlListener listener, int type) {
        if (type == CartCacheUtil.TYPE_OUT) {
            mDialogCartOut.setContrlListener(listener, 0);
        } else if (type == CartCacheUtil.TYPE_IN) {
            mDialogCartIn.setContrlListener(listener, 1);
        }
    }


    private void initSecondMenu() {
        mDialogState = new DialogState(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopInfo != null) {
                    ChatShareBean shareBean = new ChatShareBean();
                    shareBean.id = mShopId;
                    shareBean.picUrl = mShopInfo.logoUrl;
                    shareBean.type = 2;
                    shareBean.title = mShopInfo.name;
                    shareBean.content = mShopInfo.description;
                    if (v.getId() == R.id.layout_share_mtool_friend) {
                        startActivity(new Intent(ShopActivity.this, MToolShareActivity.class)
                                .putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean)));
                    } else {
                        mDialogState.onShare(v.getId(), shareBean);
                    }
                }
                mDialogState.dismiss();

            }
        });
        mDialogState.hideActionMenu();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                switch (position) {
                    case 0:
                        bactoTop();
                        break;
                    case 1:
                        mShopLayout.performClick();
                        break;
                    case 2:
                        mDialogState.show("", 2);
                        break;
                    case 3:
                        break;
                }
                mMainMenu.dismiss(false);
            }
        };
        setSecMenu(new int[]{R.mipmap.ic_backto_top, R.mipmap.ic_shop_light,
                        R.mipmap.ic_share, R.mipmap.ic_feedback}
                , new String[]{getString(R.string.backTop), getString(R.string.shop_detail)
                        , getString(R.string.share_shop), getString(R.string.feedbac_err)}, listener);
    }

    private void bactoTop() {
        ShopFragment fragment = mCache.get(mViewPager.getCurrentItem());
        fragment.backToTop();
        mScrollableLayout.scrollTo(0, 0);
    }


    private void initTab() {

    }


    public void showCartInfo(int count, double price, int type) {
        if (count == 0) {
            if (type == CartCacheUtil.TYPE_OUT) {
                mViewHolderOut.tv_cart_info.setText("配送费：" + mPeisongfei + "元");
                mViewHolderOut.tv_min_comsum.setText("起送价：" + mQisong + "元");
                if (mDialogCartOut != null)
                    mDialogCartOut.dismiss();
                mViewHolderOut.layout_cart.setVisibility(View.INVISIBLE);
            } else {
                mViewHolderIn.tv_cart_info.setText("人均消费：" + mRenjun + "元");
                if (mDialogCartIn != null)
                    mDialogCartIn.dismiss();
                mViewHolderIn.layout_cart.setVisibility(View.INVISIBLE);
            }
        } else {
            String info = "数量：" + count + " ￥" + price;
            if (type == CartCacheUtil.TYPE_OUT) {
                if (price < mQisong) {
                    mViewHolderOut.tv_min_comsum.setText("还差：" + (mQisong - price) + "元");
                    mViewHolderOut.layout_cart.setVisibility(View.INVISIBLE);
                } else {
                    info += "+" + mPeisongfei + "元";
                    mViewHolderOut.layout_cart.setVisibility(View.VISIBLE);
                }
                mViewHolderOut.tv_cart_info.setText(info);
            } else {
                mViewHolderIn.tv_cart_info.setText(info);
                mViewHolderIn.layout_cart.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initNet() {
        mShopDetailPresenter.getListData(1);
    }

    @Override
    public void onReload() {

    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left:
                finish();
                break;
            case R.id.iv_edit:
                int tag = (int) v.getTag();
                if (tag == CartCacheUtil.TYPE_OUT) {
                    mDialogCartOut.toggle();
                } else {
                    mDialogCartIn.toggle();
                }
                break;
            case R.id.iv_cart:
                if (SPUtil.getBoolean(Const.IS_LOGIN, false)) {
                    startActivityForResult(new Intent(this, CartActivity.class), 9);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.layout_shop_info:
                Intent intent = getIntent().setClass(this, ShopDetailActivity.class).putExtra(Const.ID, mShopId);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            for (int i = 0; i < mCache.size(); i++) {
                mCache.get(i).initNet();
            }
        }
    }

    @Override
    public void showRefreshComplete() {

    }

    @Override
    public void showList(BaseListBean baseListBean) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showReload() {

    }
}
