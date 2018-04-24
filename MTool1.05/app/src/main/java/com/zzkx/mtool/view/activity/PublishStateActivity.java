package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.EaseUserListBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.StatePresenter;
import com.zzkx.mtool.util.CachePathUtil;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.ImageContainerHelper;
import com.zzkx.mtool.util.ImageContainerHelper.Holder;
import com.zzkx.mtool.util.ScreenUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.DialogImageSelector;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IStateView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/25.
 */

public class PublishStateActivity extends BaseActivity implements View.OnClickListener, IStateView {
    @BindView(R.id.et_comment)
    EditText mComment;
    @BindView(R.id.image_container)
    CategoryLayout mImageContainer;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.iv_collect)
    ImageView iv_collect;
    private int mImageSize;
    private static final int INDEX = R.id.indicator;
    private static int MAX_SIZE = 9;

    private StatePresenter mPresenter;
    private DialogImageSelector mImageDialog;
    public List<EaseUser> mAtList;
    private ImageContainerHelper mImageContainerHelper;

    @Override
    public int getContentRes() {
        return R.layout.activity_publish_state;
    }

    @Override
    public void initViews() {
        mPresenter = new StatePresenter(this);
        setMainMenuEnable();
        setMainTitle("发表动态");
        mImageSize = getImageSize();
        mImageContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(this, 10));
        mIvAdd.getLayoutParams().height = mImageSize;
        mIvAdd.getLayoutParams().width = mImageSize;
        mIvAdd.setOnClickListener(this);
        findViewById(R.id.tv_send).setOnClickListener(this);
        findViewById(R.id.iv_at).setOnClickListener(this);
        findViewById(R.id.iv_show_setting).setOnClickListener(this);
        iv_collect.setOnClickListener(this);
        initImageDialog();
        CachePathUtil.clearTmpImages(this);
        mImageContainerHelper = new ImageContainerHelper(new ImageContainerHelper.Provider() {
            @Override
            public Activity getContext() {
                return PublishStateActivity.this;
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
                PublishStateActivity.this.showProgress(flag);
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
                            if (mImageContainer.getChildCount() > 1) {
                                View view = mImageContainer.getChildAt(0);
                                Holder holder = (Holder) view.getTag(INDEX);
                                if (holder.isVideo) {
                                    ToastUtils.showToast("只能添加一个视频或多张图片");
                                    return;
                                }
                            }
                            selectPicFromCamera();
                            break;
                        case R.id.tv_gallery:
                            if (mImageContainer.getChildCount() > 1) {
                                View view = mImageContainer.getChildAt(0);
                                Holder holder = (Holder) view.getTag(INDEX);
                                if (holder.isVideo) {
                                    ToastUtils.showToast("只能添加一个视频或多张图片");
                                    return;
                                }
                            }
                            startActivityForResult(new Intent(PublishStateActivity.this, PicturePreviewActivity.class)
                                            .putExtra("size", MAX_SIZE - (mImageContainer.getChildCount() - 1))
                                    , REQUEST_CODE_LOCAL);
                            break;
                        case R.id.tv_video:
                            if (mImageContainer.getChildCount() > 1) {
                                ToastUtils.showToast("只能添加一个视频或多张图片");
                                return;
                            }
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("video/*");
                            startActivityForResult(intent, REQUEST_CODE_LOCAL_VIDEO);
                            break;
                    }
                    mImageDialog.dismiss();
                }
            };
            mImageDialog = new DialogImageSelector(this, clickListener);
            mImageDialog.setVideoOptionVisible();
        }
    }

    private File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_LOCAL_VIDEO = 4;
    protected static final int REQUEST_CODE_SELECT_AT_USER = 5;

    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(this, R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(CachePathUtil.getTmpImagePath(this),
                System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
            } else if (requestCode == REQUEST_CODE_LOCAL_VIDEO) {
                mImageContainerHelper.handleVideo(data);
            } else if (requestCode == REQUEST_CODE_SELECT_AT_USER) {
                handleAtUsers(data);
            }
        }
    }

    private void handleAtUsers(Intent data) {
        if (data != null && data.getStringExtra("data") != null) {
            EaseUserListBean data1 = Json_U.fromJson(data.getStringExtra("data"), EaseUserListBean.class);
            mAtList = data1.data;
            String atUserNick = " ";
            for (EaseUser user : mAtList) {
                atUserNick += "@" + user.getNickname() + " ";
            }
            mComment.setText(mComment.getText().toString() + atUserNick);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageContainerHelper.onDestroy();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                mImageDialog.show();
                break;
            case R.id.tv_send:
                handleSend();
                break;
            case R.id.iv_at:
                startActivityForResult(new Intent(this, FriendListActivity.class)
                        .putExtra(Const.ACTION, FriendListActivity.ACTION_MULTI_SELECT), REQUEST_CODE_SELECT_AT_USER);
                break;
            case R.id.iv_show_setting:
                startActivity(new Intent(this, StateShowSettingActivity.class));
                break;
            case R.id.iv_collect:
                startActivity(new Intent(this, CollectActivity.class));
                break;
        }
    }

    public void handleSend() {
        mImageContainerHelper.handleSend();
    }


    public void handleFinish() {
        RequestBean requestBean = new RequestBean();
        requestBean.content = mComment.getText().toString();
        requestBean.resourceUrls = new ArrayList<>();
        for (int i = 0; i < mImageContainer.getChildCount(); i++) {
            View view = mImageContainer.getChildAt(i);
            Holder holder = (Holder) view.getTag(INDEX);
            if (holder != null && holder.url != null)
                requestBean.resourceUrls.add(holder.url);
            if (holder != null && holder.isVideo) {
                requestBean.type = 1;
                requestBean.videoId = holder.vidoId;
            }
        }
        if (mAtList != null) {
            String atIds = "";
            for (EaseUser user : mAtList) {
                atIds += user.getMtoolId() + ",";
            }
            if (atIds.endsWith(",")) {
                atIds = atIds.substring(0, atIds.length() - 1);
            }
            requestBean.parentId = atIds;
        }
        mPresenter.publish(requestBean);
    }


    @Override
    public void showPublishSuccess() {
        setResult(Const.RESULT_SUCESS_CODE);
        finish();
    }

    @Override
    public void showError(ErrorBean errorBean) {
        mStateView.setCurrentState(StateView.ResultState.SUCESS);
        ToastUtils.showToast(getString(R.string.netErroRetry));
    }
}
