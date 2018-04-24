package com.zzkx.mtool.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.Pair;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IShopListView;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/8/22.
 */

public class ShopListPresenter extends BasePresenter<IShopListView, BaseBean> implements CloudSearch.OnCloudSearchListener {

    private FormatHandler mFormatHandler;
    private CloudSearch mCloudSearch;
    private String mTabId;
    private CloudSearch.Query mQuery;
    private LatLng mCenterPoint;
    private String mKeyword = "";
    private ArrayList<CloudItem> mCloudItems = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isRefresh = true;
    private HandlerThread mDatFormatThread = new HandlerThread("formatData");
    private CloudSearch.Sortingrules mSortingRule;
    private Pair<String, String> mCurrentFilter;

    public void setCurrentFilter(Pair<String, String> currentFilter) {
        mCurrentFilter = currentFilter;
        pageNum = 1;
        isRefresh = true;
        searchCloud();
    }

    private class FormatHandler extends Handler {

        public FormatHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ArrayList<CloudItem> clouds = (ArrayList<CloudItem>) msg.obj;
//            if(clouds != null){
//                for(int i = 0 ;i<clouds.size();i++){
//
//                }
//            }

        }
    }


    public ShopListPresenter(IShopListView view, String tabId, LatLonPoint lat, String keyword) {
        super(view);
        mDatFormatThread.start();
        mFormatHandler = new FormatHandler(mDatFormatThread.getLooper());
        mTabId = tabId;
        mKeyword = keyword;
        mCenterPoint = new LatLng(lat.getLatitude(), lat.getLongitude());
        mCloudSearch = new CloudSearch(MyApplication.getContext());
        mCloudSearch.setOnCloudSearchListener(this);

    }

    public void sort(String sortKey) {
        getView().showProgress(true);
        /**
         * key - 排序索引的key。
         isAscending - 是否按升序排列返回。true 为升序，false为降序。
         */
        if (sortKey == null)
            mSortingRule = new CloudSearch.Sortingrules(CloudSearch.Sortingrules.DISTANCE);
        else
            mSortingRule = new CloudSearch.Sortingrules(sortKey, false);
        onRefresh();
    }

    public void searchCloud() {
        if (mCenterPoint == null)
            return;
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(
                mCenterPoint.latitude, mCenterPoint.longitude), Const.SEARCH_RADIUS);
        try {
            mQuery = new CloudSearch.Query(mTabId, mKeyword, bound);
            mQuery.setPageSize(pageSize);
            mQuery.setPageNum(pageNum);
            if (mCurrentFilter != null) {
                mQuery.addFilterString(mCurrentFilter.first, mCurrentFilter.second);
            }
            //按距离排序
            if (mSortingRule == null)
                mSortingRule = new CloudSearch.Sortingrules(CloudSearch.Sortingrules.DISTANCE);
            mQuery.setSortingrules(mSortingRule);
            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
            getView().showProgress(false);
        }
    }


    public void onRefresh() {
        isRefresh = true;
        pageNum = 1;
        searchCloud();
    }

    public void onLoadMore() {
        isRefresh = false;
        searchCloud();
    }

    @Override
    public void onCloudSearched(CloudResult result, int rCode) {
        if (isRefresh)
            mCloudItems.clear();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
//                    Message obtain = Message.obtain();
//                    obtain.obj = result.getClouds();
//                    mFormatHandler.sendMessage(obtain);
                    ArrayList<CloudItem> clouds = result.getClouds();
                    handleData(clouds);
                } else {
                    if (isRefresh) {
                        getView().showProgress(false);
                        getView().setEmpty();
                    } else {
                        getView().setLoadMore(false);
                    }
                    ToastUtils.showToast("QueryError!!");
                }
            } else {
                if (isRefresh) {
                    getView().showProgress(false);
                    getView().setEmpty();
                } else {
                    getView().setLoadMore(false);
                }
            }
        } else {
            getView().showProgress(false);
            getView().setLoadMore(false);
            ToastUtils.showToast("Error:" + rCode);
        }
    }

    private void handleData(ArrayList<CloudItem> clouds) {
        if (clouds != null && clouds.size() > 0) {
            pageNum++;
            mCloudItems.addAll(clouds);
            getView().showData(mCloudItems);
            if (isRefresh) {
                getView().showProgress(false);
                getView().setLoadMore(mCloudItems.size() > pageSize);
            } else {
                getView().setLoadMore(true);
            }

        } else {
            getView().showData(mCloudItems);
            if (isRefresh) {
                getView().showProgress(false);
                getView().setEmpty();
            } else {
                getView().setLoadMore(false);
            }

        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

    }

    @Override
    public void onSuccessM(BaseBean bean) {

    }


    public CloudItem getCloudItem(int position) {

        return mCloudItems.get(position);
    }

    public int getCloudItemSize() {

        return mCloudItems.size();
    }

}
