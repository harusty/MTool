package com.zzkx.mtool.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.aries.ui.view.radius.RadiusTextView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.MapConfig;
import com.zzkx.mtool.imple.OnCusMarkerClickListener;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;
import com.zzkx.mtool.presenter.map.CloudOverlay;
import com.zzkx.mtool.presenter.map.Cluster;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.customview.MtoolRatingBar;
import com.zzkx.mtool.view.iview.IMainView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sshss on 2017/8/22.
 */

public class MainMapPresenter implements IPresenter,
        CloudSearch.OnCloudSearchListener, OnCusMarkerClickListener, AMapLocationListener {

    private MapView mMapView;
    private IMainView mMainView;
    private AMap mAMap;
    private int mSerachRadius = 5000;//搜索范围5公里
    private CloudSearch.Query mQuery;
    private CloudSearch mCloudSearch;
    private ArrayList<CloudItem> mCloudItems;
    private CloudOverlay mPoiCloudOverlay;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private String mKeyWord;
    private Marker mMyMarker;
    private View.OnClickListener mPagerClickListener;
    private LatLonPoint mLatLonPoint;


    public MainMapPresenter(IMainView mainView, MapView amp) {
        mMainView = mainView;
        mMapView = amp;
        mAMap = amp.getMap();
        mViewPager = mMainView.getViewPager();
        UiSettings uiSettings = mAMap.getUiSettings();
//        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);//logo位置
        uiSettings.setZoomControlsEnabled(false);//默认缩放按钮
        uiSettings.setRotateGesturesEnabled(false);//旋转手势
        uiSettings.setTiltGesturesEnabled(false);//倾斜手势


        /**
         *自定义系统定位蓝点
         */
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location));
        mAMap.setMyLocationStyle(myLocationStyle);
        // 自定义精度范围
        myLocationStyle.strokeColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
        myLocationStyle.strokeWidth(5);
        myLocationStyle.radiusFillColor(0x5AFF305E);

    }

    public void locate() {
        System.out.println("start locate.....");
        mMainView.showLocatePb(true);
//        mAMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        LocateUtil.getInstance(this).locate();
    }

    /**
     * 定位回调
     *
     * @param amapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        System.out.println("end locate.....");
        mMainView.showLocatePb(false);
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //手动镜头移动到定位位置
                CurrentCityInfo currentCity = MyApplication.getInstance().getCurrentCity();
                if (currentCity == null) {
                    currentCity = new CurrentCityInfo();
                    MyApplication.getInstance().setCurrentCityInfo(currentCity);
                }
                currentCity.latitude = amapLocation.getLatitude();
                currentCity.longitude = amapLocation.getLongitude();
                currentCity.city = amapLocation.getCity();
                currentCity.cityCode = amapLocation.getCityCode();
                MyApplication.nearbyShopLatLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                addMyMarker(amapLocation.getLatitude(), amapLocation.getLongitude(), true);
                mMainView.setCurCity(amapLocation.getCity());
                //设置空适配器，不然点击poi默认弹出infoWindow
                mAMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                searchByBound(mMainView.getKeyWord(), amapLocation.getLatitude(), amapLocation.getLongitude(), null);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                ToastUtils.showToast("定位失败:" + errText);
            }
        }
    }

    public void addMyMarker(double latitude, double longitude, boolean onCameraChange) {
        LatLng latLng = new LatLng(latitude, longitude);
        if (onCameraChange) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
        if (mMyMarker != null)
            mMyMarker.remove();
        mMyMarker = mAMap.addMarker(new MarkerOptions().position(latLng)
                .icon((BitmapDescriptorFactory.fromView(View.inflate(MyApplication.getContext(), R.layout.item_my_loacteion, null)))));
    }

    public void searchByBound(String key, String value) {
        CurrentCityInfo currentCity = MyApplication.getInstance().getCurrentCity();
        if (currentCity != null) {
            Pair<String, String> pair = null;
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value))
                pair = new Pair<>(key, value);
            searchByBound("", currentCity.latitude, currentCity.longitude, pair);
        }
    }

    public void searchByBound(String keyWord, double latitude, double longitude, Pair<String, String> pair) {
        mLatLonPoint = new LatLonPoint(latitude, longitude);
        mKeyWord = keyWord;
        mMainView.showProgress(true);
        //初始化云图搜索
        if (mCloudSearch == null) {
            mCloudSearch = new CloudSearch(MyApplication.getContext());
            mCloudSearch.setOnCloudSearchListener(this);
        }
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(
                latitude, longitude), mSerachRadius);
        try {
            mQuery = new CloudSearch.Query(MapConfig.TABLE_ID, keyWord, bound);
            mQuery.setPageSize(1000);//最大每页1000条
            CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules(CloudSearch.Sortingrules.DISTANCE);
            mQuery.setSortingrules(sorting);
            if (pair != null)
                mQuery.addFilterString(pair.first, pair.second);
            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
            mMainView.showProgress(false);
        }
    }

    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (mPoiCloudOverlay != null)
            mPoiCloudOverlay.onCameraChangeFinish(cameraPosition);
    }

    @Override
    public void onCloudSearched(CloudResult result, int rCode) {
        boolean haveResult = true;
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
                    mCloudItems = result.getClouds();
                    if (mCloudItems != null && mCloudItems.size() > 0) {
                        mAMap.clear();
                        if (mPoiCloudOverlay != null) {
                            mPoiCloudOverlay = null;
                        }

                        //及时刷新适配器不然报错
                        if (mPagerAdapter != null) {
                            mPagerAdapter.notifyDataSetChanged();
                            mPagerAdapter = null;
                            mViewPager.setVisibility(View.INVISIBLE);
                        }

                        mPoiCloudOverlay = new CloudOverlay(MyApplication.getContext(), mAMap, mCloudItems, 50);
                        mPoiCloudOverlay.removeFromMap();
                        mPoiCloudOverlay.addToMap();
                        mPoiCloudOverlay.zoomToSpan(MyApplication.getInstance().getCurrentCity());
                        mPoiCloudOverlay.setOnCusMarkerClickListener(this);
                        haveResult = false;
                    } else {
                        ToastUtils.showToast("没有搜索结果");
                    }
                }
            } else {
                ToastUtils.showToast("没有搜索结果");
            }
        } else {
            ToastUtils.showToast("ErrorCode:" + rCode);
        }
        if (mLatLonPoint != null)
            addMyMarker(mLatLonPoint.getLatitude(), mLatLonPoint.getLongitude(), haveResult);
        mMainView.showProgress(false);

    }

    private boolean clickSet;

    @Override
    public boolean onCusMarkerClick(Marker marker) {
        Cluster cluster = (Cluster) marker.getObject();
        ViewGroup view = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
        if (cluster.getSize() == 1 && !TextUtils.isEmpty(cluster.imgUrl)) {
            mPoiCloudOverlay.setMarkerHeader(marker, cluster.imgUrl, view);
        } else {
            marker.setIcon(BitmapDescriptorFactory.fromView(view));
        }
        clickSet = true;
        String tag = mPoiCloudOverlay.getTag(marker.getPosition());
        setViewPager(mPoiCloudOverlay.getPoiIndex(tag));
        clickSet = false;
        mPoiCloudOverlay.setLastClickPoi(marker);
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
        return true;
    }

    private void setViewPager(final int poiIndex) {
        mViewPager.setVisibility(View.VISIBLE);
        if (mPagerClickListener == null)
            mPagerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    CloudItem cloudItem = mCloudItems.get(position);
                    String url = null;
                    if (cloudItem.getCloudImage().size() > 0) {
                        url = cloudItem.getCloudImage().get(0).getUrl();
                    }
                    HashMap<String, String> customfield = cloudItem.getCustomfield();
                    Intent intent = new Intent(MyApplication.getContext(), ShopActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Const.ID, customfield.get(Const.CUS_SHOP_ID));
                    MyApplication.getContext().startActivity(intent);
                }
            };
        if (mPagerAdapter == null) {
            mPagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return mCloudItems.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View item = View.inflate(MyApplication.getContext(), R.layout.item_shop, null);
                    CloudItem cloudItem = mCloudItems.get(position);
                    TextView title = (TextView) item.findViewById(R.id.title);
                    TextView tv_dis = (TextView) item.findViewById(R.id.tv_dis);
                    ImageView iv_image = (ImageView) item.findViewById(R.id.iv_image);
                    TextView tv_intro = (TextView) item.findViewById(R.id.tv_intro);
                    TextView tv_info = (TextView) item.findViewById(R.id.tv_info);
                    MtoolRatingBar rating = (MtoolRatingBar) item.findViewById(R.id.rating);
                    ViewGroup typeLayout = (ViewGroup) item.findViewById(R.id.layout_type);

                    String s_deposit = cloudItem.getCustomfield().get(Const.CUS_BAOZHENG);//保证金
                    String s_toStore_money = cloudItem.getCustomfield().get(Const.CUS_RENJUN);//人均消费
                    String s_toHome_money = cloudItem.getCustomfield().get(Const.CUS_QISONG);//起送价格
                    String s_toHome_tip = cloudItem.getCustomfield().get(Const.CUS_PEISONG);//配送费
                    String s_type = cloudItem.getCustomfield().get(Const.CUS_TYPE);//类型
                    String s_service = cloudItem.getCustomfield().get(Const.CUS_SERV_SCORE);//服务分
                    String s_intro = cloudItem.getCustomfield().get(Const.CUS_INTRO);//简介
                    title.setText(cloudItem.getTitle());
                    double dis = ((double) cloudItem.getDistance()) / 1000.0;
                    tv_dis.setText(dis + "km");
                    if (!TextUtils.isEmpty(s_service)) {
                        rating.setCount(Integer.parseInt(s_service));
                    }
                    typeLayout.removeAllViews();
                    TextView tag = (TextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
                    tag.setText("保" + s_deposit);
                    typeLayout.addView(tag);
                    RadiusTextView typeTag = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
                    if ("餐饮".equals(s_type)) {
                        typeTag.setText("餐饮");
                        typeTag.getDelegate().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.ligthBlue));
                        typeLayout.addView(typeTag);
                    } else if ("酒店".equals(s_type)) {
                        typeTag.setText("酒店");
                        typeTag.getDelegate().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.darkBlue));
                        typeLayout.addView(typeTag);
                    } else if ("娱乐".equals(s_type)) {
                        typeTag.setText("娱乐");
                        typeTag.getDelegate().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.dartYellow));
                        typeLayout.addView(typeTag);
                    }

                    tv_info.setText("到店人均：" + s_toStore_money + "元\\外送：" + s_toHome_money + "元起\\配送费：" + s_toHome_tip + "元");
                    tv_intro.setText(s_intro);

                    GlideUtil.getInstance().display(iv_image, cloudItem.getCustomfield().get(Const.CUS_SHOP_LOGO));
                    container.addView(item);
                    item.setTag(position);
                    item.setOnClickListener(mPagerClickListener);
                    return item;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            };
            mViewPager.setAdapter(mPagerAdapter);
        }

        if (mPageChangeListener == null) {
            mPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    CloudItem cloudItem = mPoiCloudOverlay.getCloudItem(position);
                    if (cloudItem != null && !clickSet) {
                        LatLonPoint latLonPoint = cloudItem.getLatLonPoint();
                        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())));
                        mPoiCloudOverlay.perfromClick(cloudItem);
                    } else {
                        clickSet = false;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
            mViewPager.addOnPageChangeListener(mPageChangeListener);
        }
        if (poiIndex > -1)
            mViewPager.setCurrentItem(poiIndex, false);

    }

    public String getKeyWord() {
        return mKeyWord;
    }

    public boolean getDataHave() {
        return mCloudItems != null && mCloudItems.size() > 0;
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

    }


    public void onResume() {
        mMapView.onResume();
    }

    public void onPause() {
        mMapView.onPause();
    }

    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        mMapView.onDestroy();
        if (mPoiCloudOverlay != null)
            mPoiCloudOverlay.onDestroy();
    }

    @Override
    public void onSuccess(String json, String url, Object tag) {

    }

    @Override
    public void onConnectFaild(ErrorBean bean) {

    }

    @Override
    public void onResponseError(ErrorBean bean) {

    }


    public void clear() {
        if (mAMap != null)
            mAMap.clear();
        if (mPoiCloudOverlay != null)
            mPoiCloudOverlay.clear();
    }
}
