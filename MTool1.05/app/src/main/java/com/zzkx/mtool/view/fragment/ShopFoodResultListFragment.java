package com.zzkx.mtool.view.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.presenter.SearchFoodShopPresenter;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.activity.SearchFoodShopActivity;
import com.zzkx.mtool.view.adapter.SearchFoodAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IShopFoodResultView;

import java.util.ArrayList;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;

/**
 * Created by sshss on 2017/9/16.
 */

public class ShopFoodResultListFragment extends SearchFragment implements IShopFoodResultView {
    @BindView(R.id.lv_list)
    ExpandableStickyListHeadersListView mListView;
    private SearchFoodShopPresenter mSearchPresenter;
    private SearchFoodAdapter mAdapter;
    private SearchFoodShopActivity mActivity;

    @Override
    public int getContentRes() {
        return R.layout.layout_sticky_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = (SearchFoodShopActivity) getActivity();
        mBaseView.findViewById(R.id.sr_layout).setEnabled(false);
        mSearchPresenter = new SearchFoodShopPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchPresenter.onItemClick(getActivity(), position);
            }
        });
    }

    @Override
    public void refreshLocalHistory(String keyword) {
        mActivity.refreshHistoryLayout(keyword);
    }


    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onReload() {

    }

    public void search(String keyword) {
        mSearchPresenter.search(SearchActivity.TYPE_1, keyword);
    }

    @Override
    public void showFoodData(CusMenuListBean bean) {
        if (mAdapter == null) {
            mAdapter = new SearchFoodAdapter(getActivity(), bean);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void showPoiData(ArrayList<PoiItem> poiItems) {

    }
}
