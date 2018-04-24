package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.MapConfig;
import com.zzkx.mtool.presenter.ShopListPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.RightMenuHelper;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.ShopListAdapter;
import com.zzkx.mtool.view.customview.DialogFilter;
import com.zzkx.mtool.view.customview.DialogSort;
import com.zzkx.mtool.view.customview.LoadMoreListView;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IShopListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * Created by sshss on 2017/8/22.
 */

public class ShopListActivity extends BaseActivity implements IShopListView, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.LoadMoreListener {


    @BindView(R.id.lv_list)
    LoadMoreListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwipLayout;
    @BindView(R.id.ic_left)
    View left;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindView(R.id.tv_main_title)
    TextView mTvTitle;
    @BindView(R.id.right_container)
    View mRightMenu;
    @BindView(R.id.ic_eye)
    View mEye;
    @BindView(R.id.ic_updown)
    View mSort;
    private ShopListPresenter mShopListPresenter;
    private ShopListAdapter mAdapter;
    private RightMenuHelper mRightMenuHelper;
    private DialogFilter mDialogFilter;
    private DialogSort mDialogSort;
    private ArrayList<CloudItem> mTotal;

    @Override
    public int getContentRes() {
        return R.layout.activity_shop_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mTvTitle.setText(getResources().getString(R.string.nearby_shop));
        left.setOnClickListener(this);
        mSwipLayout.setColorSchemeColors(colorPrimary);
        mSwipLayout.setOnRefreshListener(this);
        CurrentCityInfo currentCity = MyApplication.getInstance().getCurrentCity();
        LatLonPoint lat = null;
        lat = getIntent().getParcelableExtra(Const.LAT);
        if (lat == null)
            lat = MyApplication.nearbyShopLatLonPoint;
        else if (currentCity != null) {
            lat = new LatLonPoint(currentCity.latitude, currentCity.longitude);
        }

        final String keyword = getIntent().getStringExtra(Const.KEY_WORD);
        if (lat == null) {
            LocateUtil.getInstance(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        initPresenter(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), keyword);
                    } else {
                        ToastUtils.showToast("获取附近店铺失败");
                    }
                }
            }).locate();
        } else {
            initPresenter(lat, keyword);
        }
        mEye.setOnClickListener(this);
        mSort.setOnClickListener(this);
        mRightMenuHelper = new RightMenuHelper(mRightMenu);

        mDialogFilter = new DialogFilter(this, new DialogFilter.OnMenuClick() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        showProgress(true);
                        mShopListPresenter.setCurrentFilter(null);
                        break;
                    case 1:
                        showProgress(true);
                        mShopListPresenter.setCurrentFilter(new Pair<>("s_takeOut", "1"));
                        break;
                    case 2:
                        showProgress(true);
                        mShopListPresenter.setCurrentFilter(new Pair<>("s_takeIn", "1"));
                        break;
                }
                mDialogFilter.dismiss();
            }
        });
        mDialogSort = new DialogSort(this);
        mDialogSort.setOnSortListener(new DialogSort.OnSortListener() {
            @Override
            public boolean onSort(DialogSort.ViewHolder sortKey) {
                switch (sortKey.position) {
                    case Const.SORT_DISTANCE:
                        mShopListPresenter.sort(null);
                        break;
                    case Const.SORT_SALES:
                        mShopListPresenter.sort("s_lately_sales");
                        break;
                    case Const.SORT_SERV_SCORE:
                        mShopListPresenter.sort("s_service");
                        break;
                }
                return true;
            }
        });
    }

    public void initPresenter(LatLonPoint lat, String keyword) {
        mShopListPresenter = new ShopListPresenter(this, MapConfig.TABLE_ID, lat, keyword);
        mSwipLayout.setRefreshing(true);
        mListView.setOnLoadMoreListener(this);
        mSwipLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (mShopListPresenter != null)
            mShopListPresenter.onRefresh();
        else
            mSwipLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        mShopListPresenter.onLoadMore();
    }

    @Override
    public void showData(ArrayList<CloudItem> cloudItems) {
        mTotal = cloudItems;
        if (mAdapter == null) {
            mAdapter = new ShopListAdapter(this, mTotal);
            mListView.setAdapter(mAdapter);
            View view = new View(this);
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dip2PxUtils.dip2px(this, 10)));
            mListView.addHeaderView(view);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0)
                        return;
                    position -= mListView.getHeaderViewsCount();
                    if (position < mShopListPresenter.getCloudItemSize()) {
                        CloudItem cloudItem = mShopListPresenter.getCloudItem(position);
                        String url = null;
                        if (cloudItem.getCloudImage().size() > 0) {
                            url = cloudItem.getCloudImage().get(0).getUrl();
                        }
                        HashMap<String, String> customfield = cloudItem.getCustomfield();
                        startActivity(new Intent(ShopListActivity.this, ShopActivity.class)
                                .putExtra(Const.SHOP_INFO, customfield)
                                .putExtra(Const.URL, url)
                                .putExtra(Const.TITLE, cloudItem.getTitle())
                                .putExtra(Const.CUS_ADDRESS, cloudItem.getSnippet())
                                .putExtra(Const.LET_POINT, cloudItem.getLatLonPoint())
                                .putExtra(Const.ID, customfield.get(Const.CUS_SHOP_ID))
                        );
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setLoadMore(boolean b) {
        mListView.setLoading(b);
    }

    @Override
    public void setEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void showProgress(boolean toShow) {
        mSwipLayout.setRefreshing(toShow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left:
                finish();
                break;
            case R.id.ic_eye:
                mDialogFilter.toggleRightFilter(mEye);
                break;
            case R.id.ic_updown:
                mDialogSort.toggleRightFilter(mEye);
                break;
        }
    }

    @Override
    public void initNet() {
        if(mShopListPresenter == null) {
            finish();
        }else {
            mShopListPresenter.onRefresh();
        }
    }

    @Override
    public void onReload() {

    }
}
