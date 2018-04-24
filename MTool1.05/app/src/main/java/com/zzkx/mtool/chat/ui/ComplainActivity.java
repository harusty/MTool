package com.zzkx.mtool.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ComplainPresenter;
import com.zzkx.mtool.util.CachePathUtil;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.ImageContainerHelper;
import com.zzkx.mtool.util.ImageContainerHelper.Holder;
import com.zzkx.mtool.util.ScreenUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.activity.PicturePreviewActivity;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.SimpleDialog;
import com.zzkx.mtool.view.iview.IComplainView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/11.
 */

public class ComplainActivity extends BaseActivity implements View.OnClickListener, IComplainView {
    @BindView(R.id.image_container)
    CategoryLayout mImageContainer;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.et_input)
    EditText mComment;
    private int mImageSize;
    private SimpleDialog mImageDialog;
    private int lastIndex;
    private static final int INDEX = R.id.indicator;
    private ImageContainerHelper mImageContainerHelper;
    private ComplainPresenter mComplainPresenter;


    @Override
    public int getContentRes() {
        return R.layout.activity_jubao_group;
    }

    @Override
    public void initViews() {
        setMainTitle("举  报");
        setMainMenuEnable();
        mImageSize = getImageSize();
        mImageContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(this, 10));
        mIvAdd.getLayoutParams().height = mImageSize;
        mIvAdd.getLayoutParams().width = mImageSize;
        mIvAdd.setOnClickListener(this);
        findViewById(R.id.tv_publish).setOnClickListener(this);
        initImageDialog();
        mImageContainerHelper = new ImageContainerHelper(new ImageContainerHelper.Provider() {
            @Override
            public Activity getContext() {
                return ComplainActivity.this;
            }

            @Override
            public ViewGroup getImageContainer() {
                return mImageContainer;
            }

            @Override
            public View getIvAdd() {
                return mIvAdd;
            }

        }, new ImageContainerHelper.ActionListener() {
            @Override
            public void showProgress(boolean flag) {
                ComplainActivity.this.showProgress(flag);
            }

            @Override
            public void actionFinish() {
                handleFinish();
            }
        });
        mComplainPresenter = new ComplainPresenter(this);
    }

    private static final int MAX_SIZE = 9;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                String path = cameraFile.getAbsolutePath();
                mImageContainerHelper.addAndCreateView(path);
                if (mImageContainer.getChildCount() - 1 == MAX_SIZE) {
                    mIvAdd.setVisibility(View.GONE);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) {
                ArrayList<String> pathses = (ArrayList<String>) data.getSerializableExtra("data");
                mImageContainerHelper.addAndCreateView(pathses);
                if (mImageContainer.getChildCount() - 1 == MAX_SIZE) {
                    mIvAdd.setVisibility(View.GONE);
                }
            }
        }
    }


    private void initImageDialog() {
        if (mImageDialog == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_camera:
                            selectPicFromCamera();
                            break;
                        case R.id.tv_gallery:
                            startActivityForResult(new Intent(ComplainActivity.this, PicturePreviewActivity.class)
                                            .putExtra("size", MAX_SIZE - (mImageContainer.getChildCount() - 1))
                                    , REQUEST_CODE_LOCAL);
                            break;
                    }
                    mImageDialog.dismiss();
                }
            };

            mImageDialog = new SimpleDialog(this, R.layout.dialog_img_select);
            mImageDialog.getView().findViewById(R.id.tv_camera).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_gallery).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_video).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.title).setVisibility(View.GONE);
        }
    }


    private File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_LOCAL_VIDEO = 4;

    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(this, R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(CachePathUtil.getTmpImagePath(this),
                System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
    }

    private int getImageSize() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mImageContainer.getLayoutParams();
        screenWidth -= layoutParams.rightMargin + layoutParams.leftMargin +
                mImageContainer.getPaddingLeft() + mImageContainer.getPaddingRight();
        return (screenWidth - Dip2PxUtils.dip2px(this, 10) * 3) / 4;
    }


    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                lastIndex = -1;
                mImageDialog.show();
                break;
            case R.id.tv_publish:
                mImageContainerHelper.handleSend();
                break;
        }
    }

    public void handleFinish() {
        String s = mComment.getText().toString();
        if (TextUtils.isEmpty(s)) {
            ToastUtils.showToast("请输入举报内容");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mImageContainer.getChildCount(); i++) {
            View view = mImageContainer.getChildAt(i);
            Holder holder = (Holder) view.getTag(INDEX);
            if (holder != null && holder.url != null)
                sb.append(holder.url + ",");
        }
        String pics = sb.toString();
        if (pics.endsWith(","))
            pics = pics.substring(0, pics.length() - 1);
        //0动态 1 餐饮(商家) 2群 3订单
        int type = getIntent().getIntExtra(Const.TYPE, 0);
        String id = getIntent().getStringExtra(Const.ID);
        mComplainPresenter.complain(s, getIntent().getIntExtra(Const.TYPE, 0), pics,id);

    }



    @Override
    public void showComplainResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("举报成功");
            finish();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }
}
