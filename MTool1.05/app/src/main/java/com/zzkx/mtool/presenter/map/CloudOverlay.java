package com.zzkx.mtool.presenter.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.imple.OnCusMarkerClickListener;
import com.zzkx.mtool.util.Dip2PxUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;

/**
 * Created by sshss on 2017/8/16.
 */

public class CloudOverlay implements AMap.OnMarkerClickListener {


    private AlphaAnimation mAddAnim;
    private AlphaAnimation mRemoveAnim;
    private Context mContext;
    private AMap mAMap;
    private LruCache<String, Bitmap> mLruCache;
    private List<CloudItem> mCloudItems;
    //    private HashMap<String, Marker> mPoiMarks;
    private List<Marker> mPoiMarks;
    private List<Cluster> mClusters;
    private int mDis;
    private OnCusMarkerClickListener mCusMarkerClickListener;
    private Marker mLastClickMarker; //必须是非分组Marker

    /**
     * Cluster
     * Marker
     * CloudItem
     * clickPosition
     * Map<title,cloudItem>
     * 显示  View和marker绑定 点击状态？
     * 点击  背景变更，头像变更
     * 通过marker.title 从 Map<title,cloudItem>取 改cloudItem 供下次加载
     * type1：没有cameraChange  记录点击的marker，reset上次点击marker，还有头像，同时改cloudItem
     * Viewpager切换：先拿到 cloudItem，需要拿到marker? 覆盖原有marker不用循环寻找，循环根据title寻找。
     */


    public CloudOverlay(Context context, AMap amap, List<CloudItem> pois, int distance) {
        mContext = context;
        mAMap = amap;
        mCloudItems = pois;
        mDis = distance;
        mAMap.setOnMarkerClickListener(this);

//        mPoiMarks = new HashMap<>();
        mPoiMarks = new ArrayList<>();
        mClusters = new ArrayList<>();

        mLruCache = new LruCache<String, Bitmap>(50) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                oldValue.getBitmap().recycle();
            }
        };

        mAddAnim = new AlphaAnimation(0, 1);
        mRemoveAnim = new AlphaAnimation(1, 0);
    }

    public void addToMap() {
        removeFromMap();
        //优化异步,分组
        for (int i = 0; i < mCloudItems.size(); i++) {
            CloudItem cloudItem = mCloudItems.get(i);
            Cluster cluster = getCluster(cloudItem);
            if (cluster == null) {
                cluster = new Cluster(converLatlng(cloudItem.getLatLonPoint()));
                cluster.setImgUrl(cloudItem.getCustomfield().get(Const.CUS_SHOP_LOGO));
                mClusters.add(cluster);
            }
            cluster.addClusterItem(cloudItem);
        }


        //添加
        for (int j = 0; j < mClusters.size(); j++) {
            Cluster cluster = mClusters.get(j);
            String title = cluster.getTitle();
            MarkerOptions options = new MarkerOptions()
                    .position(cluster.getCenterLatLng())
                    .title(title);

            //头像，标记背景
            String imgUrl = cluster.imgUrl;
            Marker marker;
            boolean resetMarker = false;
            final ViewGroup markerView;
            if (cluster.getSize() > 1) {
                markerView = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi__num, null);
                ((TextView) markerView.getChildAt(1)).setText(String.valueOf(cluster.getSize()));
                options.icon(BitmapDescriptorFactory.fromView(markerView));
                marker = mAMap.addMarker(options);
            } else {
                ViewGroup markerView2;
                //这里重新设置lastMarker
                if (mLastClickMarker != null && TextUtils.equals(title, mLastClickMarker.getTitle())) {
                    resetMarker = true;
                    markerView = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
                    markerView2 = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
                } else {
                    markerView = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi_header, null);
                    markerView2 = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi_header, null);
                }

                if (imgUrl != null) {
                    Bitmap bitmap = mLruCache.get(imgUrl);
                    if (bitmap != null) {
                        ((ImageView) markerView.getChildAt(1)).setImageBitmap(bitmap);
                        options.icon(BitmapDescriptorFactory.fromView(markerView));
                        marker = mAMap.addMarker(options);
                    } else {
                        options.icon(BitmapDescriptorFactory.fromView(markerView));
                        marker = mAMap.addMarker(options);
                        Glide.with(mContext).load(imgUrl).asBitmap().into(new HeadLoader(marker, imgUrl, markerView2));
                    }
                } else {
                    options.icon(BitmapDescriptorFactory.fromView(markerView));
                    marker = mAMap.addMarker(options);
                }
            }
            if (resetMarker)
                mLastClickMarker = marker;
            marker.setObject(cluster);
            marker.setAnimation(mAddAnim);
            marker.startAnimation();
//            mPoiMarks.put(title, marker);
            mPoiMarks.add(marker);
        }
    }

    public void setMarkerHeader(final Marker marker, final String imgUrl, final ViewGroup view) {
        Bitmap bitmap = mLruCache.get(imgUrl);
        if (bitmap != null) {
            if (marker != null) {
                ((ImageView) view.getChildAt(1)).setImageBitmap(bitmap);
                marker.setIcon(BitmapDescriptorFactory.fromView(view));
                mLruCache.put(imgUrl, bitmap);
            }
        } else {
            Glide.with(mContext).load(imgUrl).asBitmap().into(new HeadLoader(marker, imgUrl, view));
        }
    }

    public void clear() {
        if (mCloudItems != null)
            mCloudItems.clear();
    }

    private class HeadLoader extends SimpleTarget<Bitmap> {

        private String url;
        private ViewGroup markerView;
        private Marker mMarker;

        public HeadLoader(Marker marker, String url, ViewGroup markerView) {
            this.url = url;
            this.markerView = markerView;
            mMarker = marker;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            if (mMarker != null && !mMarker.isRemoved()) {
                ((ImageView) markerView.getChildAt(1)).setImageBitmap(resource);
                mMarker.setIcon(BitmapDescriptorFactory.fromView(markerView));
                mLruCache.put(url, resource);
            }
        }
    }

    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * marker背景获取
     *
     * @return
     */
    private BitmapDescriptor getBitmapDes(int size, String title) {
        ViewGroup markerView;
        if (size > 1) {
            markerView = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi__num, null);
            ((TextView) markerView.getChildAt(1)).setText(String.valueOf(size));
        } else {
            markerView = (ViewGroup) inflate(MyApplication.getContext(), R.layout.item_poi, null);
            if (mLastClickMarker != null && TextUtils.equals(title, mLastClickMarker.getTitle()))
                ((ImageView) markerView.getChildAt(0)).setImageResource(R.mipmap.ic_poi_red);
        }
        return BitmapDescriptorFactory.fromView(markerView);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Cluster cluster = (Cluster) marker.getObject();
        if (cluster.getSize() == 1) {
            return mCusMarkerClickListener.onCusMarkerClick(marker);
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (CloudItem clusterItem : cluster.getClusterItems()) {
                LatLonPoint latLonPoint = clusterItem.getLatLonPoint();
                builder.include(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
            }
            LatLngBounds latLngBounds = builder.build();
            //300 marker显示区域padding
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 300));
            return true;
        }
    }

    /**
     * 获取所属范围内的组
     *
     * @param cloudItem
     * @return
     */
    private Cluster getCluster(CloudItem cloudItem) {
        LatLng latLng = converLatlng(cloudItem.getLatLonPoint());

        for (Cluster cluster : mClusters) {
            LatLng clusterCenterPoint = cluster.getCenterLatLng();
            int distance = getDpInstance(mAMap, latLng, clusterCenterPoint);
            int maxDis;
            if (cluster.getSize() > 1)
                maxDis = mDis / 2;
            else
                maxDis = mDis;
            if (distance < maxDis) {
                return cluster;
            }
        }

        return null;
    }

    /**
     * 两点距离(dp)
     *
     * @param map
     * @param lA
     * @param lB
     * @return
     */
    private int getDpInstance(AMap map, LatLng lA, LatLng lB) {
        Point pA = map.getProjection().toScreenLocation(lA);
        Point pB = map.getProjection().toScreenLocation(lB);
        int distance = (int) Math.sqrt(Math.pow(pB.y - pA.y, 2) + Math.pow(pB.x - pA.x, 2));

        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (distance / scale + 0.5f);
    }


    public void removeFromMap() {
        for (Marker marker : mPoiMarks) {
            marker.remove();
        }
        mClusters.clear();
        mPoiMarks.clear();

    }


    public void zoomToSpan(CurrentCityInfo myLocation) {
        if (mCloudItems != null && mCloudItems.size() > 0) {
            if (mAMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(myLocation);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, Dip2PxUtils.dip2px(MyApplication.getContext(), 30)));
        }
    }

    private LatLngBounds getLatLngBounds(CurrentCityInfo myLocation) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mCloudItems.size(); i++) {
            b.include(new LatLng(mCloudItems.get(i).getLatLonPoint().getLatitude(),
                    mCloudItems.get(i).getLatLonPoint().getLongitude()));
        }
        if (myLocation != null)
            b.include(new LatLng(myLocation.latitude, myLocation.longitude));
        return b.build();
    }

    public int getPoiIndex(String tag) {

        for (int i = 0; i < mCloudItems.size(); i++) {
            if (getTag(mCloudItems.get(i).getLatLonPoint()).equals(tag)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 保存当前点击的位置（根据title判断）同时重置上一个点击的marker
     */
    public void setLastClickPoi(Marker clickMarker) {
        String lastTitle = mLastClickMarker == null ? null : mLastClickMarker.getTitle();
        if (TextUtils.equals(clickMarker.getTitle(), lastTitle))
            return;
        View markerView = inflate(MyApplication.getContext(), R.layout.item_poi_header, null);
        View markerView2 = inflate(MyApplication.getContext(), R.layout.item_poi_header, null);
        if (mLastClickMarker != null && !mLastClickMarker.isRemoved()) {
            Cluster cluster = (Cluster) mLastClickMarker.getObject();
            if (cluster.imgUrl != null) {
                mLastClickMarker.setIcon(BitmapDescriptorFactory.fromView(markerView2));
                setMarkerHeader(mLastClickMarker, cluster.imgUrl, (ViewGroup) markerView);
            } else {
                mLastClickMarker.setIcon(BitmapDescriptorFactory.fromView(markerView));
            }
        }
        mLastClickMarker = clickMarker;
    }

    public void perfromClick(CloudItem cloudItem) {
//        Marker marker = mPoiMarks.get(cloudItem.getTitle());
//        if (marker != null && !marker.isRemoved()) {
//            ViewGroup markerView = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
//            ViewGroup markerView2 = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
//            Cluster cluster = (Cluster) marker.getObject();
//            if (cluster.getSize() == 1 && cluster.imgUrl != null) {
//                marker.setIcon(BitmapDescriptorFactory.fromView(markerView2));
//                setMarkerHeader(marker, cluster.imgUrl, markerView);
//            } else {
//                marker.setIcon(BitmapDescriptorFactory.fromView(markerView));
//            }
//            setLastClickPoi(marker);
//        }

        String lastTitle = getTag(mLastClickMarker.getPosition());
        String title = getTag(cloudItem.getLatLonPoint());
        if (TextUtils.equals(cloudItem.getTitle(), lastTitle)) {
            return;
        }
        for (int i = 0; i < mPoiMarks.size(); i++) {
            Marker marker = mPoiMarks.get(i);
            Cluster cluster = (Cluster) marker.getObject();
            if (cluster.getSize() > 1)
                continue;
            String title1 = getTag(marker.getPosition());
            if (title1 == null) {
                continue;
            }
            ViewGroup markerView;
            if (TextUtils.equals(title1, title)) {
                mLastClickMarker = marker;
                markerView = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header_selected, null);
                if (cluster.getSize() == 1 && cluster.imgUrl != null) {
                    setMarkerHeader(marker, cluster.imgUrl, markerView);
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromView(markerView));
                }

            } else if (TextUtils.equals(title1, lastTitle)) {
                markerView = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_poi_header, null);
                if (cluster.getSize() == 1 && cluster.imgUrl != null) {
                    setMarkerHeader(marker, cluster.imgUrl, markerView);
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromView(markerView));
                }
            }
        }
    }

    public String getTag(LatLonPoint latLonPoint) {
        if (latLonPoint == null)
            return null;
        else return latLonPoint.getLatitude() + "_" + latLonPoint.getLongitude();
    }

    public String getTag(LatLng position) {
        return position.latitude + "_" + position.longitude;
    }


    private Bitmap drawCircle(int radius, int color) {

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    private LatLng converLatlng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }


    private float mLastZoom = -1;

    /**
     * 只会在地图放大后缩小时重新加载
     *
     * @param cameraPosition
     */
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (mLastZoom != cameraPosition.zoom) {
            addToMap();
        }
        mLastZoom = cameraPosition.zoom;
    }

    public void setOnCusMarkerClickListener(OnCusMarkerClickListener markerClickListener) {
        mCusMarkerClickListener = markerClickListener;
    }


    public CloudItem getCloudItem(int position) {
        return mCloudItems.get(position);
    }

    public void onDestroy() {
        mLruCache.evictAll();
        mCloudItems.clear();
        mPoiMarks.clear();
        mClusters.clear();
    }


}
