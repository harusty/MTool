package com.zzkx.mtool.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;

import java.util.ArrayList;

/**
 * Created by sshss on 2018/1/31.
 */

public class ImageContainerHelper {
    private static final int INDEX = R.id.indicator;
    private VideoUploadUtil mUploadUtil;
    private HttpModel mHttpModel;
    private ActionListener mActionListener;
    private Provider mProVider;
    private int mImageSize;
    private View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag(INDEX);
            if (holder != null) {
                mProVider.getImageContainer().removeView(holder.view);
                if (holder.isVideo) {
                    mUploadUtil.stop();
                }
                mProVider.getIvAdd().setVisibility(View.VISIBLE);
            }

        }
    };

    public ImageContainerHelper(Provider provider, ActionListener listener) {
        mProVider = provider;
        mActionListener = listener;
        mImageSize = getImageSize();
        ViewGroup.LayoutParams layoutParams = mProVider.getIvAdd().getLayoutParams();
        layoutParams.width = mImageSize;
        layoutParams.height = mImageSize;
        mProVider.getIvAdd().setLayoutParams(layoutParams);
        mHttpModel = new HttpModel(null);
        mUploadUtil = VideoUploadUtil.getInstance();
    }


    public void handleSend() {
        if (mProVider.getImageContainer().getChildCount() > 1) {
            for (int i = 0; i < mProVider.getImageContainer().getChildCount() - 1; i++) {
                View child = mProVider.getImageContainer().getChildAt(i);
                Holder holder = (Holder) child.getTag(INDEX);
                if (!holder.isVideo && holder.url == null) {
                    mActionListener.showProgress(true);
                    final int finalI = i;
                    mHttpModel.upLoadFile(holder.path, new HttpModel.OnUploadListener() {
                        @Override
                        public void onProgress(float progress, long total, int id) {

                        }

                        @Override
                        public void onUploadFinish(String url) {
                            Holder holder1 = (Holder) mProVider.getImageContainer().getChildAt(finalI).getTag(INDEX);
                            holder1.url = url;
                            handleSend();
                        }

                        @Override
                        public void onUploadFaild(String e) {
                            mActionListener.showProgress(false);
                            ToastUtils.showToast("图片上传失败，请重试");
                        }
                    });
                    return;
                }
            }
            mActionListener.actionFinish();
        } else {
            mActionListener.actionFinish();
        }
    }


    private int getImageSize() {
        int screenWidth = ScreenUtils.getScreenWidth(mProVider.getContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProVider.getImageContainer().getLayoutParams();
        screenWidth -= layoutParams.rightMargin + layoutParams.leftMargin +
                mProVider.getImageContainer().getPaddingLeft() + mProVider.getImageContainer().getPaddingRight();
        return (screenWidth - Dip2PxUtils.dip2px(mProVider.getContext(), 10) * 3) / 4;
    }

    public void addAndCreateView(String path) {
        int index = mProVider.getImageContainer().getChildCount() - 1;
        View view = createImageView(index, path);
        mProVider.getImageContainer().addView(view, index);
    }

    public void addAndCreateView(ArrayList<String> pathses) {
        if (pathses != null && pathses.size() > 0) {
            for (String path : pathses) {
                addAndCreateView(path);
            }
        }
    }

    public void handleVideo(Intent data) {
        if (data != null) {
            final Uri uri = data.getData();
            System.out.println("uri: " + uri.getPath());
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mProVider.getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();
                cursor = null;
                System.out.println("video path: " + path);
                if (path == null) {
                    ToastUtils.showToast("找不到该视频");
                    return;
                }
                View videoItem;
                Bitmap localVideoThumbnail = getLocalVideoThumbnail(path);

                int index = mProVider.getImageContainer().getChildCount() - 1;
                videoItem = createVideoItem(index);
                mProVider.getImageContainer().addView(videoItem, index);
                mProVider.getIvAdd().setVisibility(View.GONE);
                final Holder holder = (Holder) videoItem.getTag(INDEX);
                holder.image.setImageBitmap(localVideoThumbnail);
                mUploadUtil.setOnUploadListener(new VideoUploadUtil.OnUploadListener() {
                    @Override
                    public void onUploadFaild() {
                        mProVider.getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.icon.setVisibility(View.INVISIBLE);
                                holder.tv_progress.setText("上传失败");
                                holder.tv_progress.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mUploadUtil.reUpLoad();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onUploadSuccess() {
                        mProVider.getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.icon.setVisibility(View.VISIBLE);
                                holder.tv_progress.setVisibility(View.INVISIBLE);
                                holder.vidoId = mUploadUtil.getVideoId();
                            }
                        });
                    }

                    @Override
                    public void onProgress(final long uploadedSize, final long totalSize) {

                        mProVider.getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.icon.setVisibility(View.INVISIBLE);
                                holder.tv_progress.setText(uploadedSize * 100 / totalSize + "%");
                            }
                        });

                    }

                    @Override
                    public void onStart() {
                        holder.icon.setVisibility(View.INVISIBLE);
                        holder.tv_progress.setText("0%");
                    }
                }).upload(path);
            }
        }
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    private Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    public View createImageView(int index, String path) {
        View item = View.inflate(mProVider.getContext(), R.layout.item_upload_image, null);
        View delete = item.findViewById(R.id.delete);
        RoundImageView1_1W roundImageView = (RoundImageView1_1W) item.findViewById(R.id.iv_image);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mImageSize, mImageSize);
//        layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(this, 10), 0);
        item.setLayoutParams(layoutParams);
        roundImageView.setBorderRadius(3);
        Holder holder = new Holder(roundImageView);
        holder.index = index;
        holder.path = path;
        holder.view = item;
        item.setTag(INDEX, holder);
        delete.setTag(INDEX, holder);
        delete.setOnClickListener(mDeleteListener);
        GlideUtil.getInstance().display(roundImageView, path);
        return item;
    }

    private View createVideoItem(int index) {
        ViewGroup inflate = (ViewGroup) View.inflate(mProVider.getContext(), R.layout.item_video_priv, null);
        inflate.setLayoutParams(new RelativeLayout.LayoutParams(mImageSize, mImageSize));
        View delete = inflate.findViewById(R.id.delete);

        Holder holder = new Holder(inflate);
        holder.view = inflate;
        holder.isVideo = true;
        holder.index = index;
        inflate.setTag(INDEX, holder);
        delete.setTag(INDEX, holder);
        delete.setOnClickListener(mDeleteListener);
        return inflate;
    }

    public void onDestroy() {
        mUploadUtil.stop();
    }

    public static class Holder {
        public ImageView image;
        public ImageView icon;
        public TextView tv_progress;
        public boolean isVideo;

        public String url;
        public String path;
        public int index;
        public String vidoId;
        public View view;

        public Holder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            icon = (ImageView) view.findViewById(R.id.icon);
            tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        }
    }


    public interface Provider {
        Activity getContext();

        ViewGroup getImageContainer();

        View getIvAdd();
    }

    public interface ActionListener {
        void showProgress(boolean flag);

        void actionFinish();
    }
}
