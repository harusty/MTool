package com.zzkx.mtool.presenter.map;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zzkx.mtool.MyApplication;

/**
 * Created by sshss on 2017/8/24.
 */

public class MToolMarker extends SimpleTarget<Bitmap> {
    private  ViewGroup mMarkerView;
    private Marker mMarker;

    public MToolMarker(Marker marker, String imgUrl, ViewGroup markerView) {
        mMarker = marker;
        Glide.with(MyApplication.getContext()).load(imgUrl).asBitmap().into(this);
        mMarkerView = markerView;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        if (mMarker != null && !mMarker.isRemoved()) {
            ((ImageView) mMarkerView.getChildAt(1)).setImageBitmap(resource);
            mMarker.setIcon(BitmapDescriptorFactory.fromView(mMarkerView));
        }
    }
}
