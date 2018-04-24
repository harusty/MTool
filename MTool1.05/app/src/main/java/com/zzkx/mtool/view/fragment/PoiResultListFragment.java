package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.SearchFoodShopPresenter;
import com.zzkx.mtool.view.activity.NearByShopActivity;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.adapter.LocateNeayByAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IShopFoodResultView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/16.
 */

public class PoiResultListFragment extends SearchFragment implements IShopFoodResultView {
    @BindView(R.id.lv_list)
    ListView mListView;
    private SearchFoodShopPresenter mPresenter;
    private LocateNeayByAdapter mAdapter;
    private ArrayList<PoiItem> mDataList;
    private SearchActivity mActivity;
    private OnPoiClickListener mOnPoiclickListener;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        if (getActivity() instanceof SearchActivity)
            mActivity = (SearchActivity) getActivity();
        mBaseView.findViewById(R.id.sr_layout).setEnabled(false);
        mPresenter = new SearchFoodShopPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiItem = mDataList.get(position);
                if (mOnPoiclickListener != null) {
                    mOnPoiclickListener.onPoiClick(poiItem);
                } else {
                    Intent intent = new Intent(getActivity(), NearByShopActivity.class)
                            .putExtra(Const.LOC_INFO, poiItem.getLatLonPoint());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void refreshLocalHistory(String keyword) {
        if (mActivity != null)
            mActivity.refreshHistoryLayout(keyword);
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onReload() {

    }

    public void search(String s1) {
        mPresenter.search(SearchActivity.TYPE_0, s1);
    }

    @Override
    public void showFoodData(CusMenuListBean bean) {

    }

    @Override
    public void showPoiData(ArrayList<PoiItem> poiItems) {
        if (mAdapter == null) {
            mDataList = poiItems;
            mAdapter = new LocateNeayByAdapter(getActivity(), mDataList);
            mListView.setAdapter(mAdapter);

        } else {
            mDataList.clear();
            mDataList.addAll(poiItems);
            mAdapter.notifyDataSetChanged();
        }
    }



    public void setOnPoiClickListener(OnPoiClickListener listener) {
        mOnPoiclickListener = listener;
    }

    public interface OnPoiClickListener {
        void onPoiClick(PoiItem poiItem);
    }
}
