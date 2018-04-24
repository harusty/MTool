package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.geocoder.RegeocodeAddress;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.PathUtil;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.presenter.UserInfoUpdatePresenter;
import com.zzkx.mtool.util.GetImgUtil;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogQr;
import com.zzkx.mtool.view.customview.RectChekBox;
import com.zzkx.mtool.view.customview.SimpleDialog;
import com.zzkx.mtool.view.iview.IMineView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;

import static java.lang.System.currentTimeMillis;

/**
 * Created by sshss on 2017/9/29.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, IMineView {
    @BindView(R.id.iv_user_header)
    ImageView mIvUserHeader;
    @BindView(R.id.iv_edit_header)
    ImageView mIvEditHeader;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_intro)
    TextView mTvIntro;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_birth)
    TextView mTvBirth;
    @BindView(R.id.tv_locate)
    TextView mTvLocate;
    @BindView(R.id.iv_edit_nick)
    ImageView mIvEditNick;
    @BindView(R.id.iv_edit_intro)
    ImageView mIvEditIntro;
    @BindView(R.id.iv_edit_sex)
    ImageView mIvEditSex;
    @BindView(R.id.iv_edit_birth)
    ImageView mIvEditBirth;
    @BindView(R.id.iv_edit_locate)
    ImageView mIvEditLocate;

    private SimpleDialog mImageDialog;
    private UserInfoUpdatePresenter mPresenter;
    private SimpleDialog mNickDialog;
    private SimpleDialog mIntroDialog;
    private SimpleDateFormat mFormater;
    private SimpleDialog mSexDialog;
    private SimpleDialog mBirthDialog;
    private DatePicker mDateView;
    private DialogQr mDialogQr;

    @Override
    public int getContentRes() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("个人信息");
        findViewById(R.id.layout_user_qr).setOnClickListener(this);
        mPresenter = new UserInfoUpdatePresenter(this);
        mFormater = new SimpleDateFormat("yyyy-MM-dd");
        mIvEditHeader.setOnClickListener(this);
        mIvEditHeader.setTag(false);
        mIvEditNick.setOnClickListener(this);
        mIvEditNick.setTag(false);
        mIvEditIntro.setOnClickListener(this);
        mIvEditIntro.setTag(false);
        mIvEditSex.setOnClickListener(this);
        mIvEditSex.setTag(false);
        mIvEditBirth.setOnClickListener(this);
        mIvEditBirth.setTag(false);
        mIvEditLocate.setOnClickListener(this);
        mIvEditLocate.setTag(false);
        setUerInfo();
    }

    private void setUerInfo() {
        GlideUtil.getInstance().display(mIvUserHeader, SPUtil.getString(Const.USER_HEADER, ""));
        mTvNick.setText(SPUtil.getString(Const.USER_NICK, ""));
        mTvIntro.setText(SPUtil.getString(Const.USER_INTRO, ""));

        int sex = SPUtil.getInt(Const.USER_SEX, 0);
        if (sex == 1) {
            mTvSex.setText("男");
        } else {
            mTvSex.setText("女");
        }
        mTvSex.setTag(sex);
        long birth = SPUtil.getLong(Const.USER_BIRTH, 0);
        if (birth > 0) {
            mTvBirth.setText(mFormater.format(birth));
        }
        String userAdd = SPUtil.getString(Const.USER_ADD, null);
        if (userAdd != null) {
            mTvLocate.setText(userAdd);
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_header:
                if ((boolean) v.getTag()) {
                    mPresenter.update(null, ((String) mIvUserHeader.getTag(R.id.tag)), null, null, null, null);
                } else {
                    initImageDialog();
                    mImageDialog.show();
                }
                break;
            case R.id.iv_edit_nick:
                if ((boolean) v.getTag()) {
                    mPresenter.update(((String) mTvNick.getTag(R.id.tag)), null, null, null, null, null);
                } else {
                    initNickDialog();
                    EditText input = ((EditText) mNickDialog.getView().findViewById(R.id.et_input));
                    String nick = SPUtil.getString(Const.USER_NICK, "");
                    input.setText(nick);
                    input.setSelection(nick.length());
                    mNickDialog.show();
                }
                break;
            case R.id.iv_edit_intro:
                if ((boolean) v.getTag()) {
                    mPresenter.update(null, null, null, null, ((String) mTvIntro.getTag(R.id.tag)), null);
                } else {
                    initIntroDialog();
                    EditText input = ((EditText) mIntroDialog.getView().findViewById(R.id.et_input));
                    String intro = SPUtil.getString(Const.USER_INTRO, "");
                    input.setText(intro);
                    input.setSelection(intro.length());
                    mIntroDialog.show();
                }
                break;
            case R.id.iv_edit_sex:
                if ((boolean) v.getTag()) {
                    mPresenter.update(null, null, ((Integer) mTvSex.getTag()), null, null, null);
                } else {
                    initSexDialog();
                    mSexDialog.show();
                }
                break;
            case R.id.iv_edit_birth:
                if ((boolean) v.getTag()) {
                    mPresenter.update(null, null, null, ((Long) mTvBirth.getTag()), null, null);
                } else {
                    initBirthDialog();
                    mBirthDialog.show();
                }
                break;
            case R.id.iv_edit_locate:
                if ((boolean) v.getTag()) {
                    mPresenter.update(null, null, null, null, null, mTvLocate.getText().toString());
                } else {
                    startActivityForResult(new Intent(this, AddressLocateActivity.class).putExtra(Const.NO_BOUND, true), 999);
                }
                break;
            case R.id.layout_user_qr:
                if (mDialogQr == null) {
                    mDialogQr = new DialogQr(this);
                    mDialogQr.setImgRes(SPUtil.getString(Const.USER_QR, ""));
                }
                mDialogQr.show();
                break;

        }
    }

    private void initBirthDialog() {
        mBirthDialog = new SimpleDialog(this, R.layout.dialog_date_picker);

        Calendar instance = Calendar.getInstance();
        mDateView = (DatePicker) mBirthDialog.getView().findViewById(R.id.date_view);
        mBirthDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = mDateView.getYear();
                int month = mDateView.getMonth();
                int dayOfMonth = mDateView.getDayOfMonth();
                calendar.set(year, month, dayOfMonth);
                calendar.getTimeInMillis();
                mTvBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                mTvBirth.setTag(calendar.getTimeInMillis());
                resetEditButton(mIvEditBirth, true);
                mBirthDialog.dismiss();
            }
        });
        mDateView.init(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), null);
        mDateView.setMaxDate(currentTimeMillis());
    }

    private void initSexDialog() {
        if (mSexDialog == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Checkable mCb1 = (Checkable) mSexDialog.getView().findViewById(R.id.cb1);
                    Checkable mCb2 = (Checkable) mSexDialog.getView().findViewById(R.id.cb2);
                    switch (v.getId()) {
                        case R.id.cb1:
                            if (!mCb1.isChecked()) {
                                mCb1.toggle();
                                mCb2.setChecked(!mCb1.isChecked());
                            }
                            break;
                        case R.id.cb2:
                            if (!mCb2.isChecked()) {
                                mCb2.toggle();
                                mCb1.setChecked(!mCb2.isChecked());
                            }
                            break;
                        case R.id.tv_confirm:
                            int sex = mCb1.isChecked() ? 1 : 0;
                            if (sex == 1) {
                                mTvSex.setText("男");
                            } else {
                                mTvSex.setText("女");
                            }
                            mTvSex.setTag(sex);
                            mSexDialog.dismiss();
                            resetEditButton(mIvEditSex, true);
                            break;
                        default:
                            mSexDialog.dismiss();
                    }
                }
            };

            mSexDialog = new SimpleDialog(this, R.layout.dialog_sex);
            RectChekBox cb1 = (RectChekBox) mSexDialog.getView().findViewById(R.id.cb1);
            RectChekBox cb2 = (RectChekBox) mSexDialog.getView().findViewById(R.id.cb2);
            cb1.setOnClickListener(clickListener);
            cb2.setOnClickListener(clickListener);
            mSexDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mSexDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);

            if ((int) mTvSex.getTag() == 1) {
                cb1.setChecked(true);
            } else {
                cb2.setChecked(true);
            }
        }
    }

    private void initIntroDialog() {
        if (mIntroDialog == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_confirm:
                            String s = ((EditText) mIntroDialog.getView().findViewById(R.id.et_input)).getText().toString();
                            if (TextUtils.isEmpty(s)) {
                                ToastUtils.showToast("简介不能为空");
                                return;
                            }
                            mTvIntro.setTag(R.id.tag, s);
                            mTvIntro.setText(s);
                            resetEditButton(mIvEditIntro, true);
                            break;
                    }
                    mIntroDialog.dismiss();
                }
            };
            mIntroDialog = new SimpleDialog(this, R.layout.dialog_intro);
            mIntroDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mIntroDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
        }
    }

    private void initNickDialog() {
        if (mNickDialog == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_confirm:
                            String s = ((EditText) mNickDialog.getView().findViewById(R.id.et_input)).getText().toString();
                            if (TextUtils.isEmpty(s)) {
                                ToastUtils.showToast("昵称不能为空");
                                return;
                            }
                            s = s.trim();
                            String[] split = s.split(" ");
                            if (split.length > 1) {
                                s = "";
                                for (String str : split) {
                                    s += str;
                                }
                            }
                            mTvNick.setTag(R.id.tag, s);
                            mTvNick.setText(s);
                            resetEditButton(mIvEditNick, true);
                            break;
                    }
                    mNickDialog.dismiss();
                }
            };
            mNickDialog = new SimpleDialog(this, R.layout.dialog_nick);
            mNickDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mNickDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
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
                            selectPicFromLocal();
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

    private File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(this, R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists()) {
                    GetImgUtil.cropPhoto(this, Uri.fromFile(cameraFile), 1, 1);
                }

            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    GetImgUtil.cropPhoto(this, selectedImage, 1, 1);
                }
            } else if (requestCode == GetImgUtil.CROP_FINISH) {

                new HttpModel(null).upLoadFile(GetImgUtil.CROP_IMG_DIR.getAbsolutePath(), new HttpModel.OnUploadListener() {
                    @Override
                    public void onProgress(float progress, long total, int id) {

                    }

                    @Override
                    public void onUploadFinish(String url) {
                        GlideUtil.getInstance().display(mIvUserHeader, url);
                        mIvUserHeader.setTag(R.id.tag, url);
                        resetEditButton(mIvEditHeader, true);
                    }

                    @Override
                    public void onUploadFaild(String e) {
                        showProgress(false);
                        ToastUtils.showToast("上传失败，请重新上传");
                    }
                });
            }
        } else if (resultCode == Const.RESULT_SUCESS_CODE) {
            RegeocodeAddress item = data.getParcelableExtra(Const.LOC_INFO);
            String districtName = item.getDistrict();
            String province = handleProvinceName(item.getProvince());
            String cityName = item.getCity();
            String locate = province + " " + cityName + " " + districtName;
            mTvLocate.setText(locate);
            resetEditButton(mIvEditLocate, true);
        }
    }

    private String handleProvinceName(String provinceName) {
        if (!TextUtils.isEmpty(provinceName)) {
            if (provinceName.contains("北京")) {
                return "北京";
            } else if (provinceName.contains("上海")) {
                return "上海";
            } else if (provinceName.contains("天津")) {
                return "天津";
            } else if (provinceName.contains("重庆")) {
                return "重庆";
            }
        }
        return provinceName;
    }


    @Override
    public void showError(ErrorBean errorBean) {
        ToastUtils.showToast(getString(R.string.netErroRetry));
    }

    @Override
    public void showUpdateInfo(RequestBean requestBean) {
        if (requestBean.picUrl != null) {
            resetEditButton(mIvEditHeader, false);
            SPUtil.putString(Const.USER_HEADER, requestBean.picUrl);

            DemoHelper instance = DemoHelper.getInstance();
            instance.getUserProfileManager().setCurrentUserAvatar(requestBean.picUrl);

        } else if (requestBean.nickname != null) {
            resetEditButton(mIvEditNick, false);
            EMClient.getInstance().updateCurrentUserNick(requestBean.nickname);
            SPUtil.putString(Const.USER_NICK, requestBean.nickname);
            DemoHelper instance = DemoHelper.getInstance();
            instance.getUserProfileManager().updateCurrentUserNickName(requestBean.nickname);
        } else if (requestBean.introduction != null) {
            resetEditButton(mIvEditIntro, false);
            SPUtil.putString(Const.USER_INTRO, requestBean.introduction);
        } else if (requestBean.sex != null) {
            resetEditButton(mIvEditSex, false);
            SPUtil.putInt(Const.USER_SEX, requestBean.sex);
        } else if (requestBean.birthday != null) {
            resetEditButton(mIvEditBirth, false);
            SPUtil.putLong(Const.USER_BIRTH, requestBean.birthday);
        } else if (requestBean.userAddr != null) {
            resetEditButton(mIvEditLocate, false);
            SPUtil.putString(Const.USER_ADD, requestBean.userAddr);
        }
    }

    private void resetEditButton(ImageView button, boolean isCheck) {
        if (isCheck) {
            button.setImageResource(R.mipmap.ic_check_red);
        } else {
            button.setImageResource(R.mipmap.ic_edit);
        }
        button.setTag(isCheck);
    }
}
