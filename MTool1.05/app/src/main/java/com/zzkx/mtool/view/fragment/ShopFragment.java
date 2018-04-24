package com.zzkx.mtool.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.imple.BottomCartCtrlListener;
import com.zzkx.mtool.presenter.ShopPresenter;
import com.zzkx.mtool.util.AnimationExecutor;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.adapter.MenuAdapter;
import com.zzkx.mtool.view.customview.DialogMenuOption;
import com.zzkx.mtool.view.iview.IShopView;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by sshss on 2017/8/24.
 */

public class ShopFragment extends BaseFragment implements IShopView {
    @BindView(R.id.lv_list)
    ExpandableStickyListHeadersListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwlayout;
    private ShopPresenter mShopPresenter;
    private MenuAdapter menuAdapter;
    private DialogMenuOption mDialogMenuOption;
    private String mShopId;
    private int mType;

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_sticky_list;
    }


    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void notifyDataSetChanged() {
        menuAdapter.notifyDataSetChanged();
    }

    @Override
    public void showOption(MenuListBean.FoodInfoListBean bean, MenuListBean.DataBean dataBean) {
        if (mDialogMenuOption != null)
            mDialogMenuOption.show(bean, dataBean);
    }

    @Override
    public void showCartInfo(int count, double price, int type) {
        if (getActivity() != null)
            ((ShopActivity) getActivity()).showCartInfo(count, price, type);
    }

    @Override
    public void setBottomCtrlListener(BottomCartCtrlListener listener, int type) {
        if (getActivity() != null)
            ((ShopActivity) getActivity()).setBottomListener(listener, type);
    }

    @Override
    public void initViews() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mShopId = arguments.getString(Const.CUS_SHOP_ID);
            mType = arguments.getInt(Const.TYPE);
        }

        mShopPresenter = new ShopPresenter(this);
        mSwlayout.setEnabled(false);
        setTitleDisable();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mShopPresenter.onItemClick(ShopFragment.this,position);
//                mListView.setSelection(0);

            }
        });
        mListView.setAnimExecutor(new AnimationExecutor());
        mListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (mListView.isHeaderCollapsed(headerId)) {
                    mListView.expand(headerId);
                } else {
                    mListView.collapse(headerId);
                }
            }
        });
        mDialogMenuOption = new DialogMenuOption(getActivity(), mShopPresenter.getOptionConfrimListener(), mType);
    }
    public void refreshCache() {
        initNet();
    }
    @Override
    public void initNet() {
        //init cache
        mShopPresenter.getFoodMenu(mShopId, mType);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void shoMenulist(CusMenuListBean bean, View.OnClickListener optionCtrlListener) {
        if (menuAdapter == null) {
            System.out.println("mType:"+mType);
            menuAdapter = new MenuAdapter(getActivity(), bean, optionCtrlListener,mType);
            mListView.setAdapter(menuAdapter);
        } else {
            menuAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShopPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReload() {

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mShopPresenter.onDestroy();
    }


    public void backToTop() {
        mListView.setSelection(0);
    }

    public int getType() {
        return mType;
    }
}
