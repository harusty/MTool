package com.zzkx.mtool.presenter;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IAddressLocateView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddressLocatePresenter extends BasePresenter<IAddressLocateView, BaseBean> implements
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener,
        PoiSearch.OnPoiSearchListener {
    private GeocodeSearch geocodeSearch;
    private AMap mAMap;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker mCenterMarker;
    private Marker mMyLocMarker;
    private LatLng mCurLat;
    private PoiSearch poiSearch;
    private String mProvinceName;
    private String mCityName;
    private String mDistrictName;

    public String getProvinceName() {
        return mProvinceName;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getDistrictName() {
        return mDistrictName;
    }

    public AddressLocatePresenter(IAddressLocateView view, AMap map) {
        super(view);
        mAMap = map;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);

        geocodeSearch = new GeocodeSearch(MyApplication.getContext());
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void onSuccessM(BaseBean bean) {

    }

    public void locate() {
        addMarkerInScreenCenter();
        getView().showLocatePb(true);
        System.out.println("start locate.....");
//        mAMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

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
    public void onLocationChanged(AMapLocation aMapLocation) {
        getView().showLocatePb(false);
        if (aMapLocation.getErrorCode() == 0) {
            getView().showProgress(true);
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mProvinceName = aMapLocation.getProvince();
            mCityName = aMapLocation.getCity();
            mDistrictName = aMapLocation.getDistrict();
//            searchNeayBy(latLng);
            mAMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            if (mMyLocMarker != null)
                mMyLocMarker.remove();
            mMyLocMarker = mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromView(View.inflate(MyApplication.getContext(), R.layout.item_my_loacteion, null)))
                    .position(latLng)
            );
            System.out.println("AMapLocation:" + aMapLocation.getAoiName() + "  " + aMapLocation.toString());
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
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            ToastUtils.showToast("AmapErr:" + errText);
        }
    }

    private void addMarkerInScreenCenter() {
        LatLng latLng = mAMap.getCameraPosition().target;
        Point screenPosition = mAMap.getProjection().toScreenLocation(latLng);
        ViewGroup markerView = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_set_locate, null);
        mCenterMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromView(markerView)));
        //设置Marker在屏幕上,不跟随地图移动
        mCenterMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

    }

    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        getView().showProgress(true);
        mCurLat = cameraPosition.target;
        searchNeayBy(mCurLat);
    }

    public void search(String keyword, boolean noBound) {
        if (mCurLat != null) {
            getView().showProgress(true);
            PoiSearch.Query query = new PoiSearch.Query(keyword, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(50);
            query.setPageNum(1);// 设置查第一页

            poiSearch = new PoiSearch(getView().getContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            if (!noBound)
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mCurLat.latitude, mCurLat.longitude), 1000, true));
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private void searchNeayBy(LatLng latLng) {
        getView().showProgress(true);
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 500, GeocodeSearch.AMAP);
//        query.setPoiType("");
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        getView().showProgress(false);
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {

            ArrayList<PoiItem> pois = poiResult.getPois();
            if (pois != null && pois.size() > 0)
                getView().showAddressInfo(pois);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        getView().showProgress(false);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
//            System.out.println(regeocodeAddress.getProvince() + "  " + regeocodeAddress.getCity() + "  " + regeocodeAddress.getDistrict());

            mProvinceName = regeocodeAddress.getProvince();
            mCityName = regeocodeAddress.getCity();
            mDistrictName = regeocodeAddress.getDistrict();
            List<PoiItem> pois = regeocodeAddress.getPois();
            if (pois != null && pois.size() > 0)
                getView().showAddressInfo(pois);
//            List<BusinessArea> businessAreas = regeocodeAddress.getBusinessAreas();
//            for (BusinessArea area : businessAreas) {
//                System.out.println("BusinessArea:" + area.getName());
//            }
//            List<Crossroad> crossroads = regeocodeAddress.getCrossroads();
//            for (Crossroad road : crossroads) {
//                System.out.println("Crossroad:" + road.getDirection() + "  " + road.getFirstRoadName());
//            }
//            List<RegeocodeRoad> roads = regeocodeAddress.getRoads();
//            for (RegeocodeRoad road : roads) {
//                System.out.println("RegeocodeRoad:" + road.getName());
//            }
//            List<PoiItem> pois = regeocodeAddress.getPois();
//            for (PoiItem item : pois) {
//                System.out.println("PoiItem:" + item.getTitle() + "  " + item.getProvinceName()+"  "+item.getCityName()+"  "+item.getCityCode());
//
//            }
//            List<AoiItem> aois = regeocodeAddress.getAois();
//            for (AoiItem item : aois) {
//                System.out.println("AoiItem:" + item.getAoiName() + "   " + item.getAoiArea());
//            }

        } else {
            ToastUtils.showToast("未找到");
        }
    }


}

