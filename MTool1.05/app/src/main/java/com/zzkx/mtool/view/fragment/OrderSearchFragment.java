package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.HistoryOrderBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.OrderSearchPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.view.activity.OrderDetailActivity;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.adapter.HistoryOrderAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IOrderSearchView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/25.
 */

public class OrderSearchFragment extends SearchFragment implements IOrderSearchView{

    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private OrderSearchPresenter mOrderSearchPresenter;
    private HistoryOrderAdapter mAdapter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mOrderSearchPresenter = new OrderSearchPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryOrderBean.DataBean dataBean = mAdapter.getItem(position);
                HistoryOrderBean.DiningType orderDining = dataBean.orderDining;
                switch (orderDining.diningType) {
                    case CartCacheUtil.TYPE_OUT:
                        getContext().startActivity(new Intent(getView().getContext(), OrderDetailActivity.class)
                                .putExtra(Const.TYPE, CartCacheUtil.TYPE_OUT)
                                .putExtra(Const.ID, dataBean.id));
                        break;
                    case CartCacheUtil.TYPE_IN:
                        getContext().startActivity(new Intent(getView().getContext(), OrderDetailActivity.class)
                                .putExtra(Const.TYPE, CartCacheUtil.TYPE_IN)
                                .putExtra(Const.ID, dataBean.id)
                        );
                        break;

                }
            }
        });
    }

    @Override
    public void onReload() {

    }

    @Override
    public void search(String key) {
        mOrderSearchPresenter.search(key);
        SearchHistoryCacheUtil.putKeyword(key, Const.SEARCH_TYPE_ORDER);
        ((SearchActivity)getActivity()).refreshHistoryLayout(key);
    }

    @Override
    public void showData(HistoryOrderBean bean) {
        List<HistoryOrderBean.DataBean> data = bean.data;
        if(data != null){
            mAdapter = new HistoryOrderAdapter(getActivity(), data);
            mListView.setAdapter(mAdapter);
        }else{
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }
}
