package com.zzkx.mtool.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.BaseAdapter;

import com.zzkx.mtool.bean.MenuCommentListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.MenuCommentListPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.adapter.MenuCommentAdapter;
import com.zzkx.mtool.view.iview.IMenuCommentListView;

/**
 * Created by sshss on 2017/10/14.
 */

public class MenuCommentFragment extends BaseListFragment<MenuCommentListBean.DataBean> implements IMenuCommentListView {
    private MenuCommentListPresenter mPresenter;
    private MenuListBean.FoodInfoListBean mMenuBean;

    @Override
    public BaseAdapter getAdapter() {
        return new MenuCommentAdapter(getActivity(), getTotalData());
    }

    @Override
    public BaseListPresenter getPresenter() {
        mMenuBean = (MenuListBean.FoodInfoListBean) getActivity().getIntent().getSerializableExtra(Const.MENU_BEAN);
        String id;
        if (mMenuBean == null) {
            id = getActivity().getIntent().getStringExtra(Const.ID);
        } else {
            id = mMenuBean.id;
        }
        mPresenter = new MenuCommentListPresenter(this, id);
        return mPresenter;
    }

    @Override
    public void initNet() {
        mPresenter.getListData(1);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getContext(), 10));
        HeaderUtil.addHeader(getContext(), mListView, 10);
    }
}
