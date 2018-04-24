package com.zzkx.mtool.presenter.map;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class Cluster {


    private LatLng mCenterLatLng;
    private List<CloudItem> mClusterItems;
    private Marker mMarker;
    public String imgUrl;


    public Cluster(LatLng latLng) {

        mCenterLatLng = latLng;
        mClusterItems = new ArrayList<>();
    }

    public void addClusterItem(CloudItem clusterItem) {
        mClusterItems.add(clusterItem);
        if (getSize() == 2) {
            LatLonPoint latLonPoint = clusterItem.getLatLonPoint();
            double latOffset = latLonPoint.getLatitude() - mCenterLatLng.latitude;
            double lonOffset = latLonPoint.getLongitude() - mCenterLatLng.longitude;
            mCenterLatLng = new LatLng(mCenterLatLng.latitude + latOffset, mCenterLatLng.longitude + lonOffset);
        }
    }

    public int getClusterCount() {
        return mClusterItems.size();
    }


    public LatLng getCenterLatLng() {
        return mCenterLatLng;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public List<CloudItem> getClusterItems() {
        return mClusterItems;
    }

    public int getSize() {
        return mClusterItems.size();
    }

    public String getTitle() {
        if (mClusterItems.size() > 1) {
            return null;
        } else {
            LatLonPoint latLonPoint = mClusterItems.get(0).getLatLonPoint();
            return latLonPoint.getLatitude() + "_" + latLonPoint.getLongitude();
        }
    }

    public void setImgUrl(String url) {
        imgUrl = url;
    }
}
