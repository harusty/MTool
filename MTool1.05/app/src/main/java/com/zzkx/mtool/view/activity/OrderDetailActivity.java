package com.zzkx.mtool.view.activity;

import android.support.v4.app.Fragment;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.OrderDetailPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.fragment.InOrderDetailFragment;
import com.zzkx.mtool.view.fragment.OutOrderDetailFragment;
import com.zzkx.mtool.view.iview.IOrderDetailView;

/**
 * Created by sshss on 2017/9/13.
 */

public class OrderDetailActivity extends BaseActivity implements IOrderDetailView {
    private OrderDetailPresenter mPresenter;
    private IOrderDetailView mFragment;

    @Override
    public int getContentRes() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("订单详情");
        int type = getIntent().getIntExtra(Const.TYPE, CartCacheUtil.TYPE_OUT);
        mFragment = null;
        switch (type) {
            case CartCacheUtil.TYPE_IN:
                mFragment = new InOrderDetailFragment();
                break;
            case CartCacheUtil.TYPE_OUT:
                mFragment = new OutOrderDetailFragment();
                break;
        }
        ((Fragment) mFragment).setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fr_container, ((Fragment) mFragment)).commit();
        mPresenter = new OrderDetailPresenter(this,this);
    }

    @Override
    public void initNet() {
        mPresenter.getOrderInfo(getIntent().getStringExtra(Const.ID));
    }

    @Override
    public void showProgress(boolean toShow) {
        super.showProgress(toShow);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showData(OrderDetailBean bean) {
        mFragment.showData(bean);
    }
}
