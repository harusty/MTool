package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.LoginBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.SSO_UserBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.LoginPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ShareMethodUtils;
import com.zzkx.mtool.util.StatusBarUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.ILoginView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/8.
 */

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    public static final int ACTION_REGIST = 1;
    public static final int ACTION_MSG_LOGIN = 2;
    public static final int ACTION_REST_PWD = 3;
    public static final int ACTION_BIND_PWD = 4;
    public static final int ACTION_BIND_PHONE = 5;
    @BindView(R.id.et_phone)
    EditText mEtPhon;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.tv_bottom)
    TextView mTvBottom;
    @BindView(R.id.tv_forget)
    TextView mTvForgot;
    @BindView(R.id.tv_msg_login)
    TextView mTvMsgLogin;
    @BindView(R.id.iv_head)
    ImageView mIvHead;

    private LoginPresenter mLoginPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        StatusBarUtil.setStatuBarColor(this, Color.TRANSPARENT, 0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTitleDisable();

        mLoginPresenter = new LoginPresenter(this);
        findViewById(R.id.bt_login).setOnClickListener(this);
        findViewById(R.id.bt_regist).setOnClickListener(this);
        findViewById(R.id.layout_wechat_login).setOnClickListener(this);
        findViewById(R.id.layout_qq_login).setOnClickListener(this);
        findViewById(R.id.layout_weibo_login).setOnClickListener(this);

        String s = mTvBottom.getText().toString();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                s.length() - 6, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), s.length() - 6, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvBottom.setText(spannableString);

        mTvForgot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvForgot.setOnClickListener(this);
        mTvMsgLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvMsgLogin.setOnClickListener(this);
        findViewById(R.id.tv_bottom).setOnClickListener(this);


        mEtPhon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1) || !SPUtil.getString(Const.USER_PHONE, "").equals(s1)) {
                    mIvHead.setVisibility(View.INVISIBLE);
                } else if (SPUtil.getString(Const.USER_PHONE, "").equals(s1)) {
                    mIvHead.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initLastAccount();
    }

    private void initLastAccount() {
        mEtPhon.setText(SPUtil.getString(Const.USER_PHONE, ""));
        mEtPwd.setText(SPUtil.getString(Const.PWD, ""));
        GlideUtil.getInstance().display(mIvHead, SPUtil.getString(Const.USER_HEADER, ""));
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showLoginSuccess(LoginBean bean) {

//        DemoModel demoModel = new DemoModel(this);
//        EaseUser easeUser = new EaseUser("hx1505308274022");
//        easeUser.setNickname("兔子");
//        easeUser.setAvatar("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1302382899,1761475552&fm=27&gp=0.jpg");
//        demoModel.saveContact(easeUser);

        LoginBean.Data data = bean.data;
        if (API.OTHER_LOGIN.equals(bean.reqUrl)) {
            if (TextUtils.isEmpty(data.phone)) {
                startActivity(new Intent(this, RegisteActivity.class)
                        .putExtra(Const.ACTION, LoginActivity.ACTION_BIND_PHONE));
            }
        } else {
            System.out.println("showLoginSuccess:" + bean.reqUrl);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void showLoginFaild(String msg) {
        ToastUtils.showToast(msg);
        showProgress(false);
    }

    @Override
    public void showError(ErrorBean errorBean) {
        mStateView.setCurrentState(StateView.ResultState.SUCESS);
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regist:
                startActivity(new Intent(this, RegisteActivity.class)
                        .putExtra(Const.ACTION, ACTION_REGIST)
                );
                break;
            case R.id.bt_login:
                String phone = mEtPhon.getText().toString();
                String pwd = mEtPwd.getText().toString();
                mLoginPresenter.login(phone, pwd);
                break;
            case R.id.tv_msg_login:
                startActivity(new Intent(this, RegisteActivity.class)
                        .putExtra(Const.ACTION, ACTION_MSG_LOGIN)
                );
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this, RegisteActivity.class)
                        .putExtra(Const.ACTION, ACTION_REST_PWD)
                );
                break;
            case R.id.layout_wechat_login:
                showProgress(true);
                ShareMethodUtils.login_wechat(new ShareMethodUtils.OnShareListener() {
                    @Override
                    public void onComplete(final SSO_UserBean bean) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                RequestBean requestBean = new RequestBean();
                                requestBean.wxOpenid = bean.id;
                                System.out.println(bean.id);
                                mLoginPresenter.login(requestBean);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        showProgress(false);
                    }

                    @Override
                    public void onCancel() {
                        showProgress(false);
                    }
                });

                break;
            case R.id.layout_qq_login:
                showProgress(true);
                ShareMethodUtils.login_qq(new ShareMethodUtils.OnShareListener() {
                    @Override
                    public void onComplete(final SSO_UserBean bean) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                RequestBean requestBean = new RequestBean();
                                requestBean.qqOpenid = bean.id;
                                System.out.println(bean.id);
                                mLoginPresenter.login(requestBean);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        showProgress(false);
                    }

                    @Override
                    public void onCancel() {
                        showProgress(false);
                    }
                });
                break;
            case R.id.layout_weibo_login:
                showProgress(true);
                ShareMethodUtils.login_weibo(new ShareMethodUtils.OnShareListener() {
                    @Override
                    public void onComplete(final SSO_UserBean bean) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                RequestBean requestBean = new RequestBean();
                                requestBean.qqOpenid = bean.id;
                                System.out.println(bean.id);
                                mLoginPresenter.login(requestBean);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        showProgress(false);
                    }

                    @Override
                    public void onCancel() {
                        showProgress(false);
                    }
                });
                break;
            case R.id.tv_bottom:
                startActivity(new Intent(this, ArticleActivity.class)
                        .putExtra(Const.TITLE, "服务条款")
                        .putExtra(Const.ID, "115193568214599")
                );
                break;
        }
    }
}
