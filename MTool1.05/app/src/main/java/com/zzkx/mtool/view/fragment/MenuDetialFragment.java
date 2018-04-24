package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.MenuDetailBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.MenuOptionBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.db.MenuBean;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.CollectPresenter;
import com.zzkx.mtool.presenter.MenuDetailPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.activity.MenuDetailActivity;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.indicator.CirclePageIndicator;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.ICollectView;
import com.zzkx.mtool.view.iview.IMenuDetailVew;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/29.
 */

public class MenuDetialFragment extends BaseFragment implements IMenuDetailVew, ICollectView, ICancleCollectionView {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator mIndication;
    @BindView(R.id.option_container)
    ViewGroup mOptionContainer;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu_intro)
    TextView mTvIntro;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    private MenuDetailActivity mActivity;
    private MenuListBean.FoodInfoListBean mMenuBean;
    private CollectPresenter mCollectPresenter;
    private MenuDetailPresenter mMenuDetailPresenter;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    private DialogState mDialogState;
    private int mType;

    @Override
    public int getContentRes() {
        return R.layout.fragment_menu_detail;
    }

    @Override
    public void initViews() {
        mActivity = (MenuDetailActivity) getActivity();
        mCollectPresenter = new CollectPresenter(this);
        mMenuDetailPresenter = new MenuDetailPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        setTitleDisable();
        mType = mActivity.getIntent().getIntExtra(Const.TYPE, CartCacheUtil.TYPE_OUT);
    }

    @Override
    public void initNet() {
        mMenuDetailPresenter.getMenuDetailInfo(mActivity.getIntent().getStringExtra(Const.ID));
    }

    private void initOption() {
        mOptionContainer.removeAllViews();
        List<MenuListBean.MenuOpiton> foodSkuList = mMenuBean.foodSkus;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                OptionHolder optionHolder = (OptionHolder) tag;
                MenuListBean.MenuOpiton menuOpiton = optionHolder.menuOpiton;

                switch (v.getId()) {
                    case R.id.iv_minus:
                        handleCount(optionHolder, menuOpiton, -1);
                        break;
                    case R.id.iv_plus:
                        handleCount(optionHolder, menuOpiton, 1);
                        break;
                }
            }
        };
        if (foodSkuList != null && foodSkuList.size() > 0) {
            for (int i = 0; i < foodSkuList.size(); i++) {
                MenuListBean.MenuOpiton menuOpiton = foodSkuList.get(i);
                OptionHolder optionHolder = new OptionHolder();
                optionHolder.menuOpiton = menuOpiton;
                optionHolder.tv_name.setText(menuOpiton.spec);
                if (mMenuBean.type == CartCacheUtil.TYPE_OUT)
                    optionHolder.tv_money.setText(String.valueOf(menuOpiton.priceOut));
                else
                    optionHolder.tv_money.setText(String.valueOf(menuOpiton.priceIn));
                optionHolder.tv_count.setText(String.valueOf(menuOpiton.cusCount));
                optionHolder.iv_minus.setTag(optionHolder);
                optionHolder.iv_plus.setTag(optionHolder);
                optionHolder.iv_minus.setOnClickListener(listener);
                optionHolder.iv_plus.setOnClickListener(listener);
                mOptionContainer.addView(optionHolder.view);
            }
        } else {
            OptionHolder optionHolder = new OptionHolder();
            optionHolder.tv_name.setVisibility(View.GONE);
            if (mMenuBean.type == CartCacheUtil.TYPE_OUT)
                optionHolder.tv_money.setText(String.valueOf(mMenuBean.priceOut));
            else
                optionHolder.tv_money.setText(String.valueOf(mMenuBean.priceIn));
            optionHolder.tv_count.setText(String.valueOf(mMenuBean.cusCount));
            optionHolder.iv_minus.setOnClickListener(listener);
            optionHolder.iv_plus.setOnClickListener(listener);
            optionHolder.iv_minus.setTag(optionHolder);
            optionHolder.iv_plus.setTag(optionHolder);
            mOptionContainer.addView(optionHolder.view);
        }
    }

    private void handleCount(OptionHolder optionHolder, MenuListBean.MenuOpiton menuOpiton, int i) {
        if (menuOpiton == null) {
            mMenuBean.cusCount += i;
            if (mMenuBean.cusCount < 0) {
                mMenuBean.cusCount = 0;
                return;
            }
            optionHolder.tv_count.setText(String.valueOf(mMenuBean.cusCount));
            double offsetPrice;
            if (mMenuBean.type == CartCacheUtil.TYPE_OUT)
                offsetPrice = mMenuBean.priceOut * i;
            else
                offsetPrice = mMenuBean.priceIn * i;

            mMenuBean.cusMenuTotalPrice += offsetPrice;
            CartCacheUtil.getInstance().saveMenu(mMenuBean, mMenuBean.type);
            Intent intent = new Intent();
            intent.putExtra(Const.MENU_BEAN, mMenuBean);
            mActivity.setResult(7, intent);
        } else {
            menuOpiton.cusMenuName = mMenuBean.name;
            menuOpiton.cusCount += i;
            if (menuOpiton.cusCount < 0) {
                menuOpiton.cusCount = 0;
                return;
            }
            mMenuBean.cusCount += i;
            optionHolder.tv_count.setText(String.valueOf(menuOpiton.cusCount));
            double offsetPrice;
            if (mMenuBean.type == CartCacheUtil.TYPE_OUT)
                offsetPrice = menuOpiton.priceOut * i;
            else
                offsetPrice = menuOpiton.priceIn * i;

            mMenuBean.cusMenuTotalPrice += offsetPrice;
            Intent intent = new Intent();
            intent.putExtra(Const.MENU_BEAN, mMenuBean);
            mActivity.setResult(7, intent);
            CartCacheUtil.getInstance().saveOption(menuOpiton, mType);
        }
    }

    public void collect() {

        double price = mMenuBean.priceIn;
        if (mMenuBean.type == CartCacheUtil.TYPE_OUT)
            price = mMenuBean.priceOut;
        mCollectPresenter.collectGood(mMenuBean.id, null, 0);
    }


    @Override
    public void showMenuDetail(MenuDetailBean bean) {
        mMenuBean = bean.data;
        if (mMenuBean != null) {
            mTvTitle.setText(mMenuBean.name);
            mTvIntro.setText(mMenuBean.description);

            syncCach();
            initOption();
            initViewPager();
            mDialogState = new DialogState(getContext(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatShareBean shareBean = new ChatShareBean();
                    shareBean.id = mMenuBean.id;
                    shareBean.picUrl = mMenuBean.foodImageDos == null ? null : mMenuBean.foodImageDos.get(0).imgUrl;
                    ;
                    shareBean.type = 3;
                    shareBean.title = mMenuBean.name;
                    shareBean.content = "外送：" + mMenuBean.priceOut + "元/到店：" + mMenuBean.priceIn + "元";
                    if (v.getId() == R.id.layout_share_mtool_friend) {
                        startActivity(new Intent(getContext(), MToolShareActivity.class)
                                .putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean)));
                    } else {
                        mDialogState.onShare(v.getId(), shareBean);
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
                            mScrollView.scrollTo(0, 0);
                            break;
                        case 1:
                            mDialogState.show("", 1);
                            break;
                        case 2:
                            if (mMenuBean.collectType == 1) {
                                cancleCollect();
                            } else {
                                collect();
                            }
                            break;
                    }
                    mActivity.mMainMenu.dismiss(false);
                }
            };
            mActivity.setSecMenu(new int[]{R.mipmap.ic_backto_top, R.mipmap.ic_share, R.mipmap.ic_2}
                    , new String[]{getString(R.string.backTop), "分享单品",
                            getString(R.string.collect_single)}, listener);
            if (mMenuBean.collectType == 1) {
                mActivity.mMainMenu.changeSecMenuItem(2, R.mipmap.ic_heart, "取消收藏");
            } else {
                mActivity.mMainMenu.changeSecMenuItem(2, R.mipmap.ic_heart_gray, "收藏单品");
            }
        }
    }

    private void cancleCollect() {
        mCancleCollectionPresenter.cancleCollection(mMenuBean.id, 1);
    }

    private void syncCach() {
        List<MenuListBean.MenuOpiton> foodSkuList = mMenuBean.foodSkus;
        if (foodSkuList != null && foodSkuList.size() > 0) {
            for (int i = 0; i < foodSkuList.size(); i++) {
                MenuListBean.MenuOpiton menuOpiton = foodSkuList.get(i);
                MenuOptionBean cacheOptionBean = CartCacheUtil.getInstance().getOption(menuOpiton.id);
                if (cacheOptionBean != null) {
                    if (mType == CartCacheUtil.TYPE_OUT)
                        menuOpiton.cusCount = cacheOptionBean.outCount;
                    else
                        menuOpiton.cusCount = cacheOptionBean.inCount;
                }
                mMenuBean.cusCount += menuOpiton.cusCount;
            }
        } else {
            MenuBean cacheMenuBean = CartCacheUtil.getInstance().getMenu(mMenuBean.id);
            if (cacheMenuBean != null) {
                if (mType == CartCacheUtil.TYPE_OUT)
                    mMenuBean.cusCount = cacheMenuBean.outCount;
                else
                    mMenuBean.cusCount = cacheMenuBean.inCount;
            }
        }
    }

    @Override
    public void collectSuccess(BaseBean bean) {
        ToastUtils.showToast(bean.msg);
        if (bean.status == 1) {
            mMenuBean.collectType = 1;
            mActivity.mMainMenu.changeSecMenuItem(2, R.mipmap.ic_heart, "取消收藏");
        }
    }

    @Override
    public void showCancleCollectResult(BaseBean bean) {
        ToastUtils.showToast(bean.msg);
        if (bean.status == 1) {
            mMenuBean.collectType = 0;
            mActivity.mMainMenu.changeSecMenuItem(2, R.mipmap.ic_heart_gray, "收藏单品");
        }
    }

    private class OptionHolder {
        private View view;
        TextView tv_name;
        TextView tv_money;
        TextView tv_count;
        View iv_minus;
        View iv_plus;
        public MenuListBean.MenuOpiton menuOpiton;

        public OptionHolder() {
            view = View.inflate(getContext(), R.layout.item_menu_option, null);
            tv_name = (TextView) view.findViewById(R.id.tv_section);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            iv_minus = view.findViewById(R.id.iv_minus);
            iv_plus = view.findViewById(R.id.iv_plus);
        }
    }

    private void initViewPager() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mMenuBean.foodImageDos.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {

                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MenuListBean.FoodImage foodImage = mMenuBean.foodImageDos.get(position);
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtil.getInstance().display(imageView, foodImage.imgUrl);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mIndication.setViewPager(mViewPager);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCollectPresenter != null)
            mCollectPresenter.onDestroy();
        if (mMenuDetailPresenter != null)
            mMenuDetailPresenter.onDestroy();
        if (mCancleCollectionPresenter != null)
            mCancleCollectionPresenter.onDestroy();
    }
}
