package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddEditPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.RectChekBox;
import com.zzkx.mtool.view.iview.IAddressEditView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddressEditActivity extends BaseActivity implements IAddressEditView, View.OnClickListener {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_add_detail)
    EditText mEtAddDetail;
    @BindView(R.id.go_locate)
    TextView mTvAddInfo;
    @BindView(R.id.cb_1)
    RectChekBox mCb1;
    @BindView(R.id.cb_2)
    RectChekBox mCb2;
    @BindView(R.id.cb_default_add)
    RectChekBox mCbDeaf;
    @BindView(R.id.bt_delete)
    TextView mDelete;
    @BindView(R.id.bt_add)
    TextView mAdd;


    private AddEditPresenter mPresenter;
    private boolean mIsAdd;
    private AddressListBean.AddressBean mUpdateBean;
    private AlertDialog mDeleteDialog;

    @Override
    public int getContentRes() {
        return R.layout.activity_address_edit;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mPresenter = new AddEditPresenter(this);
        findViewById(R.id.layout_locate).setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mCb1.setOnClickListener(this);
        mCb2.setOnClickListener(this);
        mIsAdd = getIntent().getBooleanExtra(Const.IS_ADD, true);

        if (!mIsAdd) {
            setMainTitle(getString(R.string.editAddInfo));
            mDelete.setVisibility(View.VISIBLE);
            mAdd.setText(getString(R.string.save));
            mDelete.setOnClickListener(this);
            mUpdateBean = (AddressListBean.AddressBean) getIntent().getSerializableExtra(Const.LOC_INFO);
            if (mUpdateBean == null) {
                ToastUtils.showToast("bean null");
            } else {
                mEtName.setText(mUpdateBean.name);
                mEtPhone.setText(mUpdateBean.phone);
                if (mUpdateBean.sex == 0)
                    mCb2.setChecked(true);
                else
                    mCb1.setChecked(true);
                mEtAddDetail.setText(mUpdateBean.addrDetail);
                if (mUpdateBean.defaultValue == 1) {
                    mCbDeaf.setChecked(true);
                    mCbDeaf.setEnabled(false);
                }
                mPresenter.setLocationInfo(mUpdateBean.latitude, mUpdateBean.longitude, mUpdateBean.addrDetail);
            }
            initDeleteDialog();
        } else {
            setMainTitle(getString(R.string.addAddInfo));
            mCb1.setChecked(true);
        }


    }

    private void initDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.confirm_delete));
        builder.setPositiveButton(getString(R.string.confrim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.delete(mUpdateBean);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDeleteDialog = builder.create();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_locate:
                startActivityForResult(new Intent(this, AddressLocateActivity.class), 1);
                break;
            case R.id.bt_add:
                String name = mEtName.getText().toString();
                String phone = mEtPhone.getText().toString();
                int sex = getSex();
                String addDetail = mEtAddDetail.getText().toString();
                int deaf = mCbDeaf.isChecked() ? 1 : 0;
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showToast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showToast("请输入手机号");
                    return;
                }

                mPresenter.submit(name, phone, sex, addDetail, deaf, mUpdateBean);
                break;
            case R.id.cb_1:
                if (!mCb1.isChecked()) {
                    mCb1.toggle();
                    mCb2.setChecked(!mCb1.isChecked());
                }
                break;
            case R.id.cb_2:
                if (!mCb2.isChecked()) {
                    mCb2.toggle();
                    mCb1.setChecked(!mCb1.isChecked());
                }
                break;
            case R.id.bt_delete:
                mDeleteDialog.show();
                break;
        }
    }

    @Override
    public void showAdd(boolean isSuccess) {
        if (isSuccess) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }

    @Override
    public void showDelete(BaseBean baseBean) {
        ToastUtils.showToast(baseBean.msg);
        if (baseBean.status == 1) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }

    @Override
    public Activity getContext() {
        return this;
    }

    private int getSex() {
        int sex;
        if (mCb1.isChecked())
            sex = 1;
        else
            sex = 0;
        return sex;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            PoiItem item = data.getParcelableExtra(Const.LOC_INFO);
            mTvAddInfo.setText(item.getTitle() + "\r\n" + item.getSnippet());
            mPresenter.setLocationInfo(item.getLatLonPoint().getLatitude(),
                    item.getLatLonPoint().getLongitude(), item.getSnippet());
        }
    }


}
