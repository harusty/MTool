package com.zzkx.mtool.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zzkx.mtool.MyApplication;

import java.io.File;

/**
 * Created by L.K.X on 2016/9/8.
 */
public class GlideUtil {
    private GlideUtil() {
    }

    private static GlideUtil bitmapUtils;

    public static GlideUtil getInstance() {
        if (bitmapUtils == null) {
            synchronized (GlideUtil.class) {
                if (bitmapUtils == null) {
                    bitmapUtils = new GlideUtil();
                }
            }
        }
        return bitmapUtils;
    }

    //    public void display(Fragment fragment, ImageView imageView, String reqUrl) {
//        Glide.with(fragment)
//                .load(reqUrl)
//                .into(imageView);
//    }
//
//    public void display(Activity activity, ImageView imageView, String reqUrl) {
//        Glide.with(activity)
//                .load(reqUrl)
//                .into(imageView);
//    }
//
//    public void display(Context context, ImageView imageView, String reqUrl) {
//        Glide.with(context)
//                .load(reqUrl)
//                .into(imageView);
//    }
    public void display(ImageView imageView, String url) {
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
    public void display(ImageView imageView, File file) {
        Glide.with(MyApplication.getContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
    public void display(ImageView imageView, Uri uri) {
        Glide.with(MyApplication.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public void display(ImageView imageView, int resId) {
        Glide.with(MyApplication.getContext())
                .load(resId)
                .into(imageView);
    }

    public void displayGif(ImageView imageView, String url) {
        Glide.with(MyApplication.getContext())
                .load(url)
                .asGif()
                .into(imageView);
    }

    /**
     * 注意不能用到复用的listview中
     *
     * @param imageView
     * @param url
     */
    public void displayCircle(final ImageView imageView, String url) {
        Glide.with(MyApplication.getContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 注意不能用到复用的listview中
     *
     * @param imageView
     * @param resId
     */
    public void displayCircle(final ImageView imageView, int resId) {
        Glide.with(MyApplication.getContext()).load(resId).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 圆角
     *
     * @param imageView
     * @param url
     */
    public void displayCircleRec(final ImageView imageView, String url) {
        Glide.with(MyApplication.getContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(Dip2PxUtils.dip2px(imageView.getContext(), 3));
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 圆角
     *
     * @param imageView
     * @param resId
     */
    public void displayCircleRec(final ImageView imageView, int resId) {
        Glide.with(MyApplication.getContext()).load(resId).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(Dip2PxUtils.dip2px(imageView.getContext(), 8));
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
