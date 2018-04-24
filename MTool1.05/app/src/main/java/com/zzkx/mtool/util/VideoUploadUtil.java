package com.zzkx.mtool.util;

import android.os.Handler;
import android.os.Message;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.VideoUpBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;

import java.io.File;

/**
 * Created by sshss on 2017/11/28.
 */

public class VideoUploadUtil implements IPresenter {
    private static VideoUploadUtil sVideoUploadUtil;
    private final HttpModel mHttpModel;
    private final VODUploadClientImpl uploader;
    private OnUploadListener mListener;
    public VideoUpBean mBean;

    public String getVideoId() {
        if (mBean != null)
            return mBean.data.videoId;
        else return null;
    }

    private enum UploadError {
        //凭证获取失败
        NET_ERROR,
        //上传中失败
        UPLOAD_ERROR,
        //凭证失效
        UPLOAD_EXPIRED
    }


    private UploadError mError;
    private VODUploadCallback callback = new VODUploadCallback() {

        @Override
        public void onUploadSucceed(UploadFileInfo uploadFileInfo) {
            if (mListener != null)
                mListener.onUploadSuccess();


        }

        @Override
        public void onUploadFailed(UploadFileInfo uploadFileInfo, String s, String s1) {
            if (mListener != null)
                mListener.onUploadFaild();

            mError = UploadError.UPLOAD_ERROR;
        }

        @Override
        public void onUploadProgress(UploadFileInfo uploadFileInfo, long uploadedSize, long totalSize) {
            if (mListener != null)
                mListener.onProgress(uploadedSize, totalSize);
        }

        @Override
        public void onUploadTokenExpired() {
            //上传凭证失效
            mError = UploadError.UPLOAD_EXPIRED;
        }

        @Override
        public void onUploadRetry(String s, String s1) {
            System.out.println(s + "   " + s1);
        }

        @Override
        public void onUploadRetryResume() {

        }

        @Override
        public void onUploadStarted(UploadFileInfo uploadFileInfo) {
            if (mBean != null)
                uploader.setUploadAuthAndAddress(uploadFileInfo, mBean.data.uploadAuth, mBean.data.uploadAddress);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBean = (VideoUpBean) msg.obj;
            startUpload();
        }
    };

    private void startUpload() {
        VideoUpBean.Data data = mBean.data;
        if (data != null) {
            uploader.clearFiles();
            uploader.addFile(mBean.path, new VodInfo());
            uploader.start();
        } else {
            if (mListener != null)
                mListener.onUploadFaild();
            mError = UploadError.NET_ERROR;
        }
    }

    private String mVideoPath;


    private VideoUploadUtil() {
        mHttpModel = new HttpModel(this);
        uploader = new VODUploadClientImpl(MyApplication.getContext());
        uploader.init(callback);
    }

    public static VideoUploadUtil getInstance() {
        if (sVideoUploadUtil == null)
            sVideoUploadUtil = new VideoUploadUtil();
        return sVideoUploadUtil;
    }

    public void upload(String videoPath) {
        mVideoPath = videoPath;
        if (mBean == null) {
            RequestBean requestBean = new RequestBean();
            String name = new File(videoPath).getName();
            requestBean.fileName = name;
            requestBean.title = name;
            mHttpModel.request(API.VIDEO_UPLOAD_ADD, requestBean, videoPath);
        } else {
            startUpload();
        }
        if (mListener != null)
            mListener.onStart();
    }

    @Override
    public void onSuccess(String json, String url, Object tag) {
        VideoUpBean videoUpBean = Json_U.fromJson(json, VideoUpBean.class);
        videoUpBean.path = tag.toString();
        Message obtain = Message.obtain();
        obtain.obj = videoUpBean;
        mHandler.sendMessage(obtain);
    }

    @Override
    public void onConnectFaild(ErrorBean bean) {
        if (mListener != null)
            mListener.onUploadFaild();
        mError = UploadError.NET_ERROR;
    }

    @Override
    public void onResponseError(ErrorBean bean) {
        if (mListener != null)
            mListener.onUploadFaild();
        mError = UploadError.NET_ERROR;
    }

    public VideoUploadUtil setOnUploadListener(OnUploadListener listener) {
        mListener = listener;
        return this;
    }

    public void reUpLoad() {
        if (mError == UploadError.NET_ERROR) {
            upload(mVideoPath);
        } else if (mError == UploadError.UPLOAD_ERROR) {
            uploader.start();
        } else if (mError == UploadError.UPLOAD_EXPIRED) {
            upload(mVideoPath);
        }
    }

    public interface OnUploadListener {
        void onUploadFaild();

        void onUploadSuccess();

        void onProgress(long uploadedSize, long totalSize);

        void onStart();
    }

    public void stop() {
        if (uploader != null) {
            uploader.stop();
        }
    }
}
