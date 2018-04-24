package com.zzkx.mtool.view.iview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.OrderStatus;
import com.zzkx.mtool.presenter.BasePresenter;
import com.zzkx.mtool.util.ChString;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public class OrderDetailMapPresender extends BasePresenter implements CloudSearch.OnCloudSearchListener, RouteSearch.OnRouteSearchListener {
    private final RouteSearch mRouteSearch;
    private Context mContext;
    private CloudSearch mCloudSearch;
    private AMap mAMap;

    private Marker mMyLocMarker;
    private Marker mShopMarker;
    private Marker mDelivMarker;

    private LatLng mShopLat;
    private LatLng mMyLatLng;
    private LatLng mDiliverLat;

    private String mStoreId;
    private String mDeliverId;


    private NearbySearch.NearbyListener mNearbyListener = new NearbySearch.NearbyListener() {
        @Override
        public void onUserInfoCleared(int i) {

        }

        @Override
        public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
            if (resultCode == 1000) {
                if (nearbySearchResult != null
                        && nearbySearchResult.getNearbyInfoList() != null
                        && nearbySearchResult.getNearbyInfoList().size() > 0) {
                    List<NearbyInfo> nearbyInfoList = nearbySearchResult.getNearbyInfoList();
                    NearbyInfo nearbyInfo = nearbyInfoList.get(0);
                    System.out.println("周边搜索结果为size " + nearbySearchResult.getNearbyInfoList().size()
                            + " first：" + nearbyInfo.getUserID() + "  " + nearbyInfo.getDistance() + "  "
                            + nearbyInfo.getDrivingDistance() + "  " + nearbyInfo.getTimeStamp() + "  " +
                            nearbyInfo.getPoint().toString());

                    for (int i = 0; i < nearbyInfoList.size(); i++) {
                        NearbyInfo nearbyInfo1 = nearbyInfoList.get(i);
                        if (nearbyInfo1.getUserID().equals(mDeliverId)) {
                            mDiliverLat = new LatLng(nearbyInfo1.getPoint().getLatitude() + 0.01, nearbyInfo1.getPoint().getLongitude() + 0.01);
                            View view = View.inflate(mContext, R.layout.item_poi_with_info_window, null);
//                            ((ImageView) view.getChildAt(1)).setImageResource(R.mipmap.ic_launcher);
                            if (mDelivMarker != null)
                                mDelivMarker.remove();
                            mDelivMarker = mAMap.addMarker(new MarkerOptions()
                                    .position(mDiliverLat)
                            );
                            mDelivMarker.setIcon(BitmapDescriptorFactory.fromView(view));
                            setCameraPosition();
                            if (mTimeView != null && mOrderStatus == OrderStatus.PEISONGZHONG)
                                searchRouteResult(new LatLonPoint(mDiliverLat.latitude, mDiliverLat.longitude), new LatLonPoint(mMyLatLng.latitude, mMyLatLng.longitude));
                            break;
                        }
                    }
                } else {
                    ToastUtils.showToast("没有搜索到配送员,请稍后重试");
                }
            } else if (resultCode == 1802) {
                ToastUtils.showToast("连接超时，请稍后再试");
            } else {
                ToastUtils.showToast("没有搜索到配送员,请稍后重试");
                System.out.println("周边搜索出现异常，异常码为：" + resultCode);
            }
        }

        @Override
        public void onNearbyInfoUploaded(int i) {

        }
    };
    private TextView mTimeView;
    private int mOrderStatus;

    public OrderDetailMapPresender(IView view, AMap map, Context context) {
        super(view);
        mContext = MyApplication.getContext();
        mAMap = map;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setScaleControlsEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);//logo位置
        uiSettings.setZoomControlsEnabled(false);//默认缩放按钮
        uiSettings.setRotateGesturesEnabled(false);//旋转手势
        uiSettings.setTiltGesturesEnabled(false);//倾斜手势

        mCloudSearch = new CloudSearch(MyApplication.getContext());
        mCloudSearch.setOnCloudSearchListener(this);
        mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(this);
    }

    public void setTimeView(TextView time, int orderStatus) {
        mTimeView = time;
        mOrderStatus = orderStatus;
    }

    public void searchRouteResult(LatLonPoint start, LatLonPoint end) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                start, end);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }


    public void addMarkers(String storeId, String deliverId) {
        System.out.println("deliverId: " + deliverId);
        mStoreId = storeId;
        mDeliverId = deliverId;
        getView().showProgress(true);
        LocateUtil.getInstance(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    View icon = View.inflate(mContext, R.layout.item_poi, null);
                    mMyLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    mMyLocMarker = mAMap.addMarker(new MarkerOptions()
                            .position(mMyLatLng));
                    mMyLocMarker.setIcon(BitmapDescriptorFactory.fromView(icon));

                    Glide.with(mContext)
                            .load(SPUtil.getString(Const.USER_HEADER, ""))
                            .asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ViewGroup icon = (ViewGroup) View.inflate(mContext, R.layout.item_poi_header, null);
                            ((ImageView) icon.getChildAt(1)).setImageBitmap(resource);
                            mMyLocMarker.setIcon(BitmapDescriptorFactory.fromView(icon));
                        }
                    });
                    setCameraPosition();
                    if (mDeliverId != null)
                        searchDeliv();
                    if (mStoreId != null)
                        mCloudSearch.searchCloudDetailAsyn(Const.MAP_TABLE_ID, mStoreId);
                } else {
                    getView().showProgress(false);
                    ToastUtils.showToast("获取当前位置失败");
                }
            }
        }).locate();

    }

    public void searchDeliv() {
        NearbySearch mNearbySearch = NearbySearch.getInstance(mContext);
        NearbySearch.getInstance(mContext).addNearbyListener(mNearbyListener);
        //设置搜索条件
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(new LatLonPoint(mMyLatLng.latitude, mMyLatLng.longitude));
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        query.setRadius(20000);
        //设置查询的时间
        query.setTimeRange(10000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
        //调用异步查询接口
        mNearbySearch.searchNearbyInfoAsyn(query);
    }

    private void setCameraPosition() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mMyLatLng != null)
            builder.include(mMyLatLng);
        if (mShopLat != null)
            builder.include(mShopLat);
        if (mDiliverLat != null)
            builder.include(mDiliverLat);
        LatLngBounds build = builder.build();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(build, 300));
    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {

    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
        getView().showProgress(false);
        if (rCode == AMapException.CODE_AMAP_SUCCESS && item != null) {
            ViewGroup inflate = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);

            mShopLat = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
            mShopMarker = mAMap.addMarker(new MarkerOptions().position(mShopLat));
            mShopMarker.setIcon(BitmapDescriptorFactory.fromView(inflate));
            setCameraPosition();
            Glide.with(MyApplication.getContext()).load(item.getCustomfield().get(Const.CUS_SHOP_LOGO)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ViewGroup inflate = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
                    ((ImageView) inflate.getChildAt(1)).setImageBitmap(resource);
                    mShopMarker.setIcon(BitmapDescriptorFactory.fromView(inflate));
                }
            });
            if (mTimeView != null && mOrderStatus == OrderStatus.DAIQUCAN)
                searchRouteResult(new LatLonPoint(mMyLatLng.latitude, mMyLatLng.longitude), item.getLatLonPoint());
        } else {
            ToastUtils.showToast("error id!");
        }
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                List<DrivePath> paths = result.getPaths();
                if (result.getPaths().size() > 0) {
                    DrivePath drivePath = paths.get(0);
                    String friendlyTime = getFriendlyTime((int) drivePath.getDuration());
                    String friendlyLength = getFriendlyLength((int) drivePath.getDuration());
                    mTimeView.setText(friendlyTime);

                    View view = View.inflate(mContext, R.layout.item_poi_with_info_window, null);
                    TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
                    TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                    ImageView iv_header = (ImageView) view.findViewById(R.id.iv_header);
                    if (mOrderStatus == OrderStatus.DAIQUCAN) {
                        tv_type.setText("距离商家：");
                    } else {
                        tv_type.setText("距离您：");
                    }
                    tv_distance.setText(friendlyLength);
                    mDelivMarker.setIcon(BitmapDescriptorFactory.fromView(view));
                }
            }
        }
    }

    private String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
//            return dis + ChString.Kilometer;
            return dis + "KM";
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + "M";
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + "M";
    }

    private String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    @Override
    public void onSuccessM(BaseBean bean) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocateUtil.getInstance().stopLocation();
        NearbySearch.getInstance(mContext).removeNearbyListener(mNearbyListener);
        NearbySearch.getInstance(mContext).clearUserInfoAsyn();
        mTimeView = null;
        mAMap = null;
    }
}
