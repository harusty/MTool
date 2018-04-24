package com.zzkx.mtool.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CartPresenter;
import com.zzkx.mtool.util.AnimationExecutor;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.CartActivity;
import com.zzkx.mtool.view.adapter.CartAdapter;
import com.zzkx.mtool.view.customview.DialogMenuOption;
import com.zzkx.mtool.view.iview.ICartView;

import java.util.List;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by sshss on 2017/9/2.
 */

public class CartFragment extends BaseFragment implements ICartView {
    @BindView(R.id.lv_list)
    ExpandableStickyListHeadersListView mListView;
    private CartPresenter mCartPresenter;
    private CartAdapter mCartAdapter;
    private DialogMenuOption mDialogMenuOption;
    private CartActivity mActivity;
    private int mType = -1;

    @Override
    public int getContentRes() {
        return R.layout.layout_sticky_list;
    }

    @Override
    public void initViews() {
        mActivity = (CartActivity) getActivity();
        setTitleDisable();
        mBaseView.findViewById(R.id.sr_layout).setEnabled(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(Const.TYPE, 0);
            mCartPresenter = new CartPresenter(this, mType);
            mDialogMenuOption = new DialogMenuOption(getActivity(), mCartPresenter.getOptionConfrimListener(), mType, true);

        } else {
            ToastUtils.showToast("arguments null!");
        }
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
    }

    @Override
    public void initNet() {
        if (mCartPresenter != null)
            mCartPresenter.getListData();
    }

    @Override
    public void showCartInfo(CusMenuListBean bean, View.OnClickListener optionCtrlListener) {
        if (mType == CartCacheUtil.TYPE_OUT)
            mActivity.mOutCusListBean = bean;
        else
            mActivity.mInCusListBean = bean;
        if (mCartAdapter == null) {
            mCartAdapter = new CartAdapter(getActivity(), bean, optionCtrlListener, mType);
            mListView.setAdapter(mCartAdapter);
        } else {
            mCartAdapter.notifyDataSetChanged();
        }
    }

    public void setDataLoded() {
        mActivity.setDataLoaded(mType);
    }

    @Override
    public void notifyDataSetChanged() {
        if (mCartAdapter != null)
            mCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void showOption(MenuListBean.FoodInfoListBean bean, MenuListBean.DataBean dataBean, boolean noOpton) {
        if (mDialogMenuOption != null)
            mDialogMenuOption.show(bean, dataBean, noOpton);
    }

    @Override
    public void showTopOrderInfo(double totalPrice, int mecuNum, int type) {
        ((CartContainerFragment) getParentFragment()).showTopOrder(totalPrice, mecuNum, type);
    }

    @Override
    public void recordData(List<MenuListBean.DataBean> headerList, int type) {
        if (type == CartCacheUtil.TYPE_OUT) {
            mActivity.mShopsTypeOut = headerList;
        } else {
            mActivity.mShopsTypeIn = headerList;
        }
    }

    @Override
    public void onReload() {
        mCartPresenter.getListData();
    }

    public View getScrollableView() {
        return mListView;
    }


    public void resetTopOrderInfo() {
        mCartPresenter.initTopOrderInfo();
    }
}
