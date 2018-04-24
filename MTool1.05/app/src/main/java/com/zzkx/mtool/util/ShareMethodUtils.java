package com.zzkx.mtool.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.SSO_UserBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by L.K.X on 2016/2/27.
 */
public class ShareMethodUtils {
    public static void login_wechat(final OnShareListener listener) {
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        if (platform.isAuthValid())
            platform.removeAccount(true);

        platform.SSOSetting(false);  //设置false表示使用SSO授权方式
        platform.setPlatformActionListener(new BasePlatformActionListener(listener));
        platform.showUser(null);
    }

    public static void login_qq(final OnShareListener listener) {
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        if (platform.isAuthValid())
            platform.removeAccount(true);

        platform.SSOSetting(false);
        platform.setPlatformActionListener(new BasePlatformActionListener(listener));
        platform.showUser(null);
    }

    public static void login_weibo(OnShareListener listener) {
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (platform.isAuthValid())
            platform.removeAccount(true);

        platform.SSOSetting(false);
        platform.setPlatformActionListener(new BasePlatformActionListener(listener));
        platform.showUser(null);
    }

    public static void logout_wechat(final OnShareListener listener) {
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.removeAccount(true);

//        platform.setPlatformActionListener(new BasePlatformActionListener(listener));
//        platform.showUser(null);//授权并获取用户信息
    }

    public static void showQQ_url(String imageUrl, String title, String content, String shareUrl) {
        QQ.ShareParams shareParams = new QQ.ShareParams();
        if (TextUtils.isEmpty(imageUrl))
            shareParams.setImagePath(getLogoPath());//不支持setImageData(bitmap)
        else
            shareParams.setImageUrl(imageUrl);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.setPlatformActionListener(new BasePlatformActionListener(new OnShareListener() {
            @Override
            public void onComplete(SSO_UserBean bean) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError() {
                ToastUtils.showToast("分享错误");
            }

            @Override
            public void onCancel() {

            }
        }));
        platform.share(shareParams);
    }


    /**
     * 推荐使用
     * QQ空间本身不支持分享本地图片，因此如果想分享本地图片，图片会先上传到ShareSDK的文件服务器，
     * 得到连接以后才分享此链接.由于本地图片更耗流量，因此imageUrl优先级高于imagePath.
     *
     * @param imageUrl
     * @param title
     * @param content
     * @param shareUrl
     */
    public static void showQQZone_Url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        if (TextUtils.isEmpty(imageUrl))
            shareParams.setImagePath(getLogoPath());//不支持setImageData(bitmap)
        else
            shareParams.setImageUrl(imageUrl);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setTitleUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        platform.setPlatformActionListener(new BasePlatformActionListener(new OnShareListener() {
            @Override
            public void onComplete(SSO_UserBean bean) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError() {
                ToastUtils.showToast("分享错误");
            }

            @Override
            public void onCancel() {

            }
        }));
        platform.share(shareParams);
    }


    public static void showWeiXin_url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(content);

        if (TextUtils.isEmpty(imageUrl))
            shareParams.setImageData(getBitmap());
        else
            shareParams.setImageUrl(imageUrl);
        shareParams.setUrl(shareUrl);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(new BasePlatformActionListener(new OnShareListener() {
            @Override
            public void onComplete(SSO_UserBean bean) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError() {
                ToastUtils.showToast("分享错误");
            }

            @Override
            public void onCancel() {

            }
        }));
        platform.share(shareParams);
    }

    private static String getLogoPath() {
        String assetsCacheFile = getAssetsCacheFile(MyApplication.getContext(), "ic_logo.png");
        return assetsCacheFile;
    }

    private static Bitmap getBitmap() {
        Bitmap bmp = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.ic_logo);
        return bmp;
    }

    public static String getAssetsCacheFile(Context context, String fileName) {
        File cacheFile = new File(context.getCacheDir(), fileName);
        if (!cacheFile.exists()) {
            try {
                InputStream inputStream = context.getAssets().open(fileName);
                try {
                    FileOutputStream outputStream = new FileOutputStream(cacheFile);
                    try {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buf)) > 0) {
                            outputStream.write(buf, 0, len);
                        }
                    } finally {
                        outputStream.close();
                    }
                } finally {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cacheFile.getAbsolutePath();
    }

    public static void showWeiXinZone_url(String imageUrl, String title, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setUrl(shareUrl);
        if (TextUtils.isEmpty(imageUrl))
            shareParams.setImageData(getBitmap());
        else
            shareParams.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.setPlatformActionListener(new BasePlatformActionListener(new OnShareListener() {
            @Override
            public void onComplete(SSO_UserBean bean) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError() {
                ToastUtils.showToast("分享错误");
            }

            @Override
            public void onCancel() {

            }
        }));
        platform.share(shareParams);
    }

    /**
     * @param title
     * @param content
     * @param shareUrl http://bbs.mob.com/forum.php?mod=viewthread&tid=24689&page=1&extra=#pid61902
     */
    public static void showSina_url(String imageUrl, String title, String content, String shareUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setUrl(shareUrl);
        if (TextUtils.isEmpty(imageUrl))
            shareParams.setImageData(getBitmap());
        else
            shareParams.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.setPlatformActionListener(new BasePlatformActionListener(new OnShareListener() {
            @Override
            public void onComplete(SSO_UserBean bean) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError() {
                ToastUtils.showToast("分享错误");
            }

            @Override
            public void onCancel() {

            }
        }));
        platform.share(shareParams);
    }


    private static class BasePlatformActionListener implements PlatformActionListener {

        private OnShareListener mListener;

        public BasePlatformActionListener(OnShareListener listener) {
            mListener = listener;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            System.out.println("onComplete");
            SSO_UserBean sso_userBean = null;
            if (i == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                sso_userBean = new SSO_UserBean();
                sso_userBean.token = platDB.getToken();// 获取授权token,令牌
                sso_userBean.gender = platDB.getUserGender();//性别
                sso_userBean.userIcon = platDB.getUserIcon();//头像链接
                sso_userBean.id = platDB.getUserId();// 获取用户在此平台的ID
                sso_userBean.name = platDB.getUserName();// 获取用户昵称
            }
            mListener.onComplete(sso_userBean);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            System.out.println("onError code:" + i);
            System.out.println("onError msg:" + throwable.getMessage());
            mListener.onError();
        }

        @Override
        public void onCancel(Platform platform, int i) {
            System.out.println("onCancel:" + i);
            mListener.onCancel();
        }
    }


    public interface OnShareListener {
        void onComplete(SSO_UserBean bean);

        void onError();

        void onCancel();
    }
}
