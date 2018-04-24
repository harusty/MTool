package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.CachePathUtil;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ImageContainerHelper;
import com.zzkx.mtool.util.ImageContainerHelper.Holder;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.SimpleDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/24.
 */

public class FoodSingleEnvaluateActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_food)
    ImageView mIvFood;
    @BindView(R.id.et_comment)
    EditText mComment;
    @BindView(R.id.image_container)
    CategoryLayout mImageContainer;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.tv_good_name)
    TextView mTvGoodName;
    private static final int INDEX = R.id.indicator;
    private SimpleDialog mImageDialog;
    private File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_LOCAL_VIDEO = 4;
    private ImageContainerHelper mImageContainerHelper;

    @Override
    public int getContentRes() {
        return R.layout.activity_food_single_envaluate;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("单品评价");
        findViewById(R.id.tv_save).setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mImageContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(this, 10));

        GlideUtil.getInstance().display(mIvFood, getIntent().getStringExtra(Const.URL));
        mTvGoodName.setText(getIntent().getStringExtra(Const.NAME));
        initImageDialog();
        mImageContainerHelper = new ImageContainerHelper(new ImageContainerHelper.Provider() {
            @Override
            public Activity getContext() {
                return FoodSingleEnvaluateActivity.this;
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
                FoodSingleEnvaluateActivity.this.showProgress(flag);
            }

            @Override
            public void actionFinish() {
                handleFinish();
            }
        });
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
                            startActivityForResult(new Intent(FoodSingleEnvaluateActivity.this, PicturePreviewActivity.class)
                                    .putExtra("size", MAX_SIZE - (mImageContainer.getChildCount() - 1)), REQUEST_CODE_LOCAL);
                            break;
                    }
                    mImageDialog.dismiss();
                }
            };
            mImageDialog = new SimpleDialog(this, R.layout.dialog_img_select);
            mImageDialog.getView().findViewById(R.id.tv_camera).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_gallery).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.title).setVisibility(View.GONE);
        }
    }

    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

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

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                mImageContainerHelper.handleSend();
                break;
            case R.id.iv_add:
                selectImage();
                break;
        }
    }

    public void handleFinish() {
        String s = mComment.getText().toString();
        if (s.length() > 80) {
            ToastUtils.showToast("评价字数最多80字");
            return;
        }
        String iamges = "";
        for (int i = 0; i < mImageContainer.getChildCount(); i++) {
            View view = mImageContainer.getChildAt(i);
            Holder holder = (Holder) view.getTag(INDEX);
            if (holder != null) {
                iamges += "," + holder.url;
            }
        }
        Intent intent = new Intent();
        intent.putExtra(Const.IMAGES, iamges);
        intent.putExtra(Const.COMMENT, s);
        setResult(Const.RESULT_SUCESS_CODE, intent);
        finish();
    }

    private void selectImage() {
//        startActivityForResult(new Intent(this, PicturePreviewActivity.class), 99);
        mImageDialog.show();
    }


    private static int MAX_SIZE = 9;

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

}
