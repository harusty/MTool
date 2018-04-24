package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CodeBean;
import com.zzkx.mtool.bean.CountrySelecBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.LoginMsgCodePresenter;
import com.zzkx.mtool.presenter.RegistPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogCountry;
import com.zzkx.mtool.view.iview.ILoginMsgView;
import com.zzkx.mtool.view.iview.IRegistView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/8.
 */

public class RegisteActivity extends BaseActivity implements IRegistView, View.OnClickListener, ILoginMsgView {
    @BindView(R.id.tv_agreement1)
    TextView mTvAgree1;
    @BindView(R.id.tv_agreement2)
    TextView mTvAgree2;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.tv_send_msg)
    TextView mTvSend;
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_country_num)
    TextView tv_country_num;

    private RegistPresenter mPresenter;
    private String mPhone;
    public static RegisteActivity istance;
    private int mAction;
    private LoginMsgCodePresenter mLoginMsgCodePresenter;
    private DialogCountry mDialogCountry;

    @Override
    public int getContentRes() {
        return R.layout.activity_regist;
    }

    @Override
    public void initViews() {
        istance = this;
        mPresenter = new RegistPresenter(this, this);
        setMainMenuEnable();
        mAction = getIntent().getIntExtra(Const.ACTION, 1);
        mTvAgree1.setOnClickListener(this);
        mTvAgree2.setOnClickListener(this);
        switch (mAction) {
            case LoginActivity.ACTION_REGIST:
                setMainTitle("快捷注册");
                mTvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhone = mEtPhone.getText().toString();
                        if (TextUtils.isEmpty(mPhone)) {
                            ToastUtils.showToast("请输入手机号");
                            return;
                        }
                        mPresenter.getRegistCode(mPhone);
                    }
                });
                break;
            case LoginActivity.ACTION_MSG_LOGIN:
                setMainTitle("快捷登录");
                mTvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhone = mEtPhone.getText().toString();
                        if (TextUtils.isEmpty(mPhone)) {
                            ToastUtils.showToast("请输入手机号");
                            return;
                        }
                        if (mLoginMsgCodePresenter == null)
                            mLoginMsgCodePresenter = new LoginMsgCodePresenter(RegisteActivity.this);
                        mLoginMsgCodePresenter.getLoginMsgCode(mPhone);
                    }
                });
                break;
            case LoginActivity.ACTION_REST_PWD:
            case LoginActivity.ACTION_BIND_PWD:
                setMainTitle("安全认证");
                mTvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhone = mEtPhone.getText().toString();
                        if (TextUtils.isEmpty(mPhone)) {
                            ToastUtils.showToast("请输入手机号");
                            return;
                        }
                        mPresenter.getResetCode(mPhone);
                    }
                });
                break;
            case LoginActivity.ACTION_BIND_PHONE:
                setMainTitle("绑定手机");
                mTvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhone = mEtPhone.getText().toString();
                        if (TextUtils.isEmpty(mPhone)) {
                            ToastUtils.showToast("请输入手机号");
                            return;
                        }
                        mPresenter.getRegistCode(mPhone);
                    }
                });
                break;
        }
        mTvAgree1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvAgree2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.layout_country).setOnClickListener(this);
        mDialogCountry = new DialogCountry(this, new DialogCountry.OnSelectListener() {
            @Override
            public void onSelect(CountrySelecBean bean) {
                tv_country.setText(bean.name);
                tv_country_num.setText(bean.phone);
            }
        });
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showError(ErrorBean errorBean) {
        showProgress(false);
        ToastUtils.showToast(getString(R.string.netErroRetry));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_country:
                mDialogCountry.show();
                break;
            case R.id.tv_agreement1:
                startActivity(new Intent(this,ArticleActivity.class)
                        .putExtra(Const.TITLE,"使用条款")
                        .putExtra(Const.ID,"115193569803173")
                );
                break;
            case R.id.tv_agreement2:
                startActivity(new Intent(this,ArticleActivity.class)
                        .putExtra(Const.TITLE,"隐私政策")
                        .putExtra(Const.ID,"115193569121761")
                );
                break;
        }
    }

    @Override
    public void showReceiveCode(CodeBean bean) {
        if (bean.status == 1) {
            Intent intent = getIntent().setClass(this, MessageCodeActivity.class);
            intent.putExtra(Const.PHONE, mPhone);
            intent.putExtra(Const.ID, bean.data);
            intent.putExtra(Const.ACTION, mAction);
            startActivity(intent);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showMsgCodeSuccess() {
        Intent intent = getIntent().setClass(this, MessageCodeActivity.class);
        intent.putExtra(Const.PHONE, mPhone);
        intent.putExtra(Const.ACTION, mAction);
        startActivity(intent);
    }

    @Override
    public void validateSuccess(BaseBean baseBean) {

    }

    @Override
    public void resetSuccess(BaseBean baseBean) {

    }

    @Override
    public void registSuccess(BaseBean baseBean) {

    }

    @Override
    public void bindPhoneResult(BaseBean baseBean) {

    }


}
