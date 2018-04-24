package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.UserInfoBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.LogOutPresenter;
import com.zzkx.mtool.presenter.UserInfoPresenter;
import com.zzkx.mtool.view.iview.ILogOutView;
import com.zzkx.mtool.view.iview.IUserInfoView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/24.
 */

public class AccountSafetyActivity extends BaseActivity implements View.OnClickListener, ILogOutView, IUserInfoView {
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    private LogOutPresenter mLogOutPresenter;
    private UserInfoPresenter mUserInfoPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_accoutn_safety;
    }

    @Override
    public void initViews() {
        setMainTitle("账号与安全");
        setMainMenuEnable();
        findViewById(R.id.tv_log_out).setOnClickListener(this);
        mLogOutPresenter = new LogOutPresenter(this);
        mUserInfoPresenter = new UserInfoPresenter(this);
    }

    @Override
    public void initNet() {
        mUserInfoPresenter.getUserInfo();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_log_out:
                mLogOutPresenter.logOut();
                break;
            case R.id.tv_status:
                startActivity(new Intent(this, RegisteActivity.class)
                        .putExtra(Const.ACTION, LoginActivity.ACTION_BIND_PWD));
                break;
        }
    }

    @Override
    public void showError(ErrorBean errorBean) {
       super.showError(errorBean);
    }

    @Override
    public void showLogOutSuccess() {
        MyApplication.getInstance().clearActivity();
        Intent intent = new Intent(this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showUserInfo(UserInfoBean bean) {
        UserInfoBean.DataBean data = bean.data;
        if (data != null) {
            mTvPhone.setText(data.phone);
            if (TextUtils.isEmpty(data.password)) {
                mTvStatus.setText("未设置");
                mTvStatus.setOnClickListener(this);
            } else {
                mTvStatus.setText("已设置");
                mTvStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AccountSafetyActivity.this, RegisteActivity.class)
                                .putExtra(Const.ACTION, LoginActivity.ACTION_REST_PWD));
                    }
                });
            }
        }
    }
}
