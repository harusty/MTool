package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CollectionSearchBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CollectionSearchPresenter;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.MenuDetailActivity;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.adapter.CollectionSearchAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.ICollectionSearchView;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/26.
 */

public class CollectionSearchFragment extends SearchFragment implements ICollectionSearchView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CollectionSearchPresenter mCollectionSearchPresenter;
    private CollectionSearchAdapter mAdapter;
    private AMapLocation mMyLocation;
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            showProgress(false);
            if (aMapLocation.getErrorCode() == 0) {
                mMyLocation = aMapLocation;
            } else {
                ToastUtils.showToast("定位失败");
            }
        }
    };

    @Override
    public void search(String key) {
        mCollectionSearchPresenter.search(key);
        SearchHistoryCacheUtil.putKeyword(key, Const.SEARCH_TYPE_COLLECTION);
        ((SearchActivity) getActivity()).refreshHistoryLayout(key);
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mCollectionSearchPresenter = new CollectionSearchPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemViewType = mAdapter.getItemViewType(position);
                switch (itemViewType) {
                    case 1:
                        CollectionSearchBean.ShopListBean item1 = (CollectionSearchBean.ShopListBean) mAdapter.getItem(position);
                        CollectionSearchBean.MerchantRestaurantsDoBean aDo = item1.merchantRestaurantsDo;
                        if (aDo != null) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(Const.CUS_SERV_SCORE, aDo.serviceScore + "");
                            map.put(Const.CUS_USER_SCORE, aDo.priceScore + "");
                            map.put(Const.CUS_PEISONG, aDo.toHomeTip);
                            map.put(Const.CUS_RENJUN, aDo.avgConsume);
                            map.put(Const.CUS_QISONG, aDo.deliverAmount + "");
                            map.put(Const.CUS_SHOP_ID, aDo.id);
                            map.put(Const.CUS_INTRO, aDo.description);

                            startActivity(new Intent(getActivity(), ShopActivity.class)
                                    .putExtra(Const.ID, aDo.id)
                            );
                        }
                        break;
                    case 2:
                        CollectionSearchBean.GoodsListBean item2 = (CollectionSearchBean.GoodsListBean) mAdapter.getItem(position);
                        startActivity(new Intent(getActivity(), MenuDetailActivity.class)
                                .putExtra(Const.ID, item2.goodsId)
                        );
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        StateListBean.DataBean item3 = ((CollectionSearchBean.ForumPostCollectListBean) mAdapter.getItem(position)).forumPostDo;
                        startActivity(new Intent(getActivity(), StateDetailActivity.class)
                                .putExtra(Const.TYPE, itemViewType - 3)
                                .putExtra(Const.ID, item3.id)
                        );
                        break;
                }
            }
        });
    }

    @Override
    public void initNet() {
        showProgress(true);
        LocateUtil.getInstance(mAMapLocationListener).locate();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void showData(CollectionSearchBean bean) {
        mAdapter = new CollectionSearchAdapter(getActivity(), bean);
        mAdapter.setMyLocation(mMyLocation);
        mListView.setAdapter(mAdapter);
    }
}
