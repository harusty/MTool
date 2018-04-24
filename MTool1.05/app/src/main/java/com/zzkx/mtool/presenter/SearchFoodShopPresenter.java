package com.zzkx.mtool.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.iview.IShopFoodResultView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public class SearchFoodShopPresenter extends BasePresenter<IShopFoodResultView, MenuListBean> implements AMapLocationListener, PoiSearch.OnPoiSearchListener {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mMyLocation;
    private String mKeyWord;
    private int mType;
    private CusMenuListBean mCusMenuListBean;
    private PoiSearch.Query mQuery;
    private PoiSearch mPoiSearch;

    public SearchFoodShopPresenter(IShopFoodResultView view) {
        super(view);
    }


    public void search(int type, String keyword) {
        if (TextUtils.equals(mKeyWord, keyword))
            return;
        getHttpModel().cancleAll();
        mType = type;
        mKeyWord = keyword;
        if (mMyLocation != null) {
            search(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()));
        } else if (MyApplication.getInstance().getCurrentCity() != null){
            CurrentCityInfo currentCity = MyApplication.getInstance().getCurrentCity();
            search(new LatLng(currentCity.latitude,currentCity.longitude));
        }else{
            locate();
        }
    }

    private void search(LatLng latLng) {
        switch (mType) {
            case SearchActivity.TYPE_1:
                searchFood(latLng);
                break;
            case SearchActivity.TYPE_0:
                searchPoi(latLng);
                break;
        }
    }

    private void searchFood(LatLng latLng) {
        System.out.println("searchMain food");
        RequestBean requestBean = new RequestBean();
        requestBean.merchantRestaurants = new RequestBean.MerchantRestaurants();
        requestBean.merchantRestaurants.name = mKeyWord;
        requestBean.merchantRestaurants.latitude = latLng.latitude;
        requestBean.merchantRestaurants.longitude = latLng.longitude;
        getHttpModel().request(API.SEARCH_NEARYBY_SHOP_FOOD, requestBean);
    }

    private void searchPoi(LatLng latLng) {

        if (latLng != null) {
            mQuery = new PoiSearch.Query(mKeyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            mQuery.setPageSize(200);// 设置每页最多返回多少条poiitem
            mQuery.setPageNum(1);// 设置查第一页

            mPoiSearch = new PoiSearch(MyApplication.getContext(), mQuery);
            mPoiSearch.setOnPoiSearchListener(this);
            mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 500, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            mPoiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private void locate() {
        getView().showProgress(true);
        System.out.println("start locate.....");

        if (mlocationClient == null) {
            mLocationOption = new AMapLocationClientOption();
            mlocationClient = new AMapLocationClient(MyApplication.getContext());
            mlocationClient.setLocationListener(this);
            mLocationOption.setOnceLocation(true);

            mLocationOption.setLocationCacheEnable(false);
            mlocationClient.setLocationOption(mLocationOption);
        }

        mlocationClient.startLocation();
    }


    @Override
    public void onSuccessM(MenuListBean bean) {
        historyCache();
        if (mCusMenuListBean.headerList.size() == 0)
            getView().showEmpty();
        else {
            getView().showProgress(false);
            getView().showFoodData(mCusMenuListBean);
        }
    }

    @Override
    public void onSuccessWorkThread(MenuListBean bean) {
        mCusMenuListBean = new CusMenuListBean();
        if (bean != null) {
            List<MenuListBean.DataBean> listData = bean.data;
            mCusMenuListBean.headerList = listData;
            for (int i = 0; i < listData.size(); i++) {
                MenuListBean.DataBean dataBean = listData.get(i);
                List<MenuListBean.FoodInfoListBean> foodInfoList = dataBean.foodInfoList;
                if (mCusMenuListBean.headerIndices.size() == 0) {
                    mCusMenuListBean.headerIndices.add(0);
                    mCusMenuListBean.headerIndices.add(foodInfoList.size());
                } else if (i < listData.size() - 1) {
                    Integer value = mCusMenuListBean.headerIndices.get(i - 1);
                    mCusMenuListBean.headerIndices.add(foodInfoList.size() + value);
                }
                if (foodInfoList != null && foodInfoList.size() > 0) {
                    for (int j = 0; j < foodInfoList.size(); j++) {
                        MenuListBean.FoodInfoListBean foodBean = foodInfoList.get(j);
                        foodBean.cusGroupPotision = i;
                        if (j == 0)
                            foodBean.cusParentBean = dataBean;
                    }
                    mCusMenuListBean.menuList.addAll(foodInfoList);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        getView().showProgress(false);
        if (aMapLocation.getErrorCode() == 0) {
            mMyLocation = aMapLocation;
            search(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()));
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            ToastUtils.showToast("AmapErr:" + errText);
            getView().showProgress(false);
        }
        mMyLocation = aMapLocation;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        historyCache();
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
                    ArrayList<PoiItem> poiItems = result.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        getView().showPoiData(poiItems);
                    } else {
                        ToastUtils.showToast("没有搜索到结果");
                        getView().showEmpty();
                    }
                }
            } else {
                ToastUtils.showToast("没有搜索到结果");
                getView().showEmpty();
            }
        } else {
            ToastUtils.showToast("没有搜索到结果");
            getView().showEmpty();
        }
    }

    private void historyCache() {
        SearchHistoryCacheUtil.putKeyword(mKeyWord, Const.SEARCH_TYPE_FOODSHOP);
        getView().refreshLocalHistory(mKeyWord);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void onItemClick(Activity activity, int itemPosition) {
        MenuListBean.FoodInfoListBean foodInfoListBean = mCusMenuListBean.menuList.get(itemPosition);
        int cusGroupPotision = foodInfoListBean.cusGroupPotision;
        MenuListBean.DataBean dataBean = mCusMenuListBean.headerList.get(cusGroupPotision);
        if (dataBean != null) {
            HashMap<String, String> customfield = new HashMap<>();
            customfield.put(Const.CUS_SERV_SCORE, dataBean.serviceScore + "");
            customfield.put(Const.CUS_USER_SCORE, dataBean.priceScore + "");
            customfield.put(Const.CUS_PEISONG, dataBean.startingPrice);
            customfield.put(Const.CUS_RENJUN, dataBean.avgConsume);
            customfield.put(Const.CUS_QISONG, dataBean.deliverAmount + "");
            customfield.put(Const.CUS_INTRO, dataBean.description);
            customfield.put(Const.CUS_PHONE, dataBean.distance);
            customfield.put(Const.CUS_SHOP_ID, dataBean.id);
            activity.startActivity(new Intent(activity, ShopActivity.class)
                    .putExtra(Const.ID, dataBean.id));
        }
    }

}
