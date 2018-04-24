package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CodeBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.LoginBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.LoginMsgCodePresenter;
import com.zzkx.mtool.presenter.LoginPresenter;
import com.zzkx.mtool.presenter.RegistPresenter;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ILoginMsgView;
import com.zzkx.mtool.view.iview.ILoginView;
import com.zzkx.mtool.view.iview.IRegistView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/29.
 */

public class MessageCodeActivity extends BaseActivity implements View.OnClickListener, IRegistView, ILoginView, ILoginMsgView {
    @BindView(R.id.text_phone)
    TextView mTvPhone;
    @BindView(R.id.editText)
    EditText mCodeInfput;
    @BindView(R.id.code_container)
    ViewGroup mLayoutCodeContainer;
    @BindView(R.id.tv_resend)
    TextView mTvResend;
    @BindView(R.id.tv_to_login)
    TextView mTvTologin;
    private CountDownTimer mTimer;
    private RegistPresenter mPresenter;
    private String mPhone;
    private boolean mIsLogin;
    private int mAction;
    private String mCode;
    private String mId;
    private LoginPresenter mLoginPresenter;
    private LoginMsgCodePresenter mLoginMsgCodePresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_message_code;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mId = getIntent().getStringExtra(Const.ID);
        mAction = getIntent().getIntExtra(Const.ACTION, 1);
        mLoginPresenter = new LoginPresenter(this);
        switch (mAction) {
            case LoginActivity.ACTION_REGIST:
                setMainTitle("快捷注册");
                mTvTologin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCode)) {
                            ToastUtils.showToast("请输入验证码");
                            return;
                        }
                        mPresenter.regist(mCode, mPhone);
                    }
                });
                break;
            case LoginActivity.ACTION_MSG_LOGIN:
                setMainTitle("快捷登录");
                mTvTologin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCode)) {
                            ToastUtils.showToast("请输入验证码");
                            return;
                        }
                        mLoginPresenter.msgLogin(mPhone, mCode);
                    }
                });
                break;
            case LoginActivity.ACTION_REST_PWD:
            case LoginActivity.ACTION_BIND_PWD:
                setMainTitle("安全认证");
                mTvTologin.setText("更改密码");
                mTvTologin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCode)) {
                            ToastUtils.showToast("请输入验证码");
                            return;
                        }
                        mPresenter.validateRestCode(mCode, mPhone);
                    }
                });
                break;
            case LoginActivity.ACTION_BIND_PHONE:
                setMainTitle("绑定手机");
                mTvTologin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCode)) {
                            ToastUtils.showToast("请输入验证码");
                            return;
                        }
                        mPresenter.bindPhone(mCode, mPhone);
                    }
                });
                break;
        }

        mPresenter = new RegistPresenter(this, this);
        mPhone = getIntent().getStringExtra(Const.PHONE);
        String phoneInfo = "我们已给手机号码+86 " + mPhone + "发送了已给6位数验证码。";
        SpannableString spannableString = new SpannableString(phoneInfo);
        if (mPhone != null)
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 8, 12 + mPhone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPhone.setText(spannableString);

        mTvResend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvResend.setOnClickListener(this);
        mLayoutCodeContainer.setOnClickListener(this);
        initTimer();
        mCodeInfput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initTimer() {
        if (mTimer != null)
            mTimer.cancel();
        mTimer = null;
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvResend.setClickable(false);
                int time = (int) (millisUntilFinished / 1000);
                System.out.println("time:  " + time);
                mTvResend.setText("重新发送（" + time + "）");
            }

            @Override
            public void onFinish() {
                mTvResend.setClickable(true);
                mTvResend.setText("重新发送");

            }
        };
        mTimer.start();
    }

    private void setCode(String s) {
        mCode = s;
        for (int i = 0; i < mLayoutCodeContainer.getChildCount(); i++) {
            TextView code = (TextView) mLayoutCodeContainer.getChildAt(i);
            if (i < s.length()) {
                char c = s.charAt(i);
                code.setText(String.valueOf(c));
            } else {
                code.setText("");
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_container:
                InputUtils.showInput(this, mCodeInfput);
                break;
            case R.id.tv_resend:
                switch (mAction) {
                    case LoginActivity.ACTION_REST_PWD:
                        mPresenter.getResetCode(mPhone);
                        break;
                    case LoginActivity.ACTION_REGIST:
                        mPresenter.getRegistCode(mPhone);
                        break;
                    case LoginActivity.ACTION_MSG_LOGIN:
                        if (mLoginMsgCodePresenter == null)
                            mLoginMsgCodePresenter = new LoginMsgCodePresenter(this);
                        mLoginMsgCodePresenter.getLoginMsgCode(mPhone);
                        break;
                    case LoginActivity.ACTION_BIND_PHONE:
                        mPresenter.getRegistCode(mPhone);
                        break;
                }
                break;
        }
    }


    @Override
    public void showReceiveCode(CodeBean bean) {
        initTimer();
    }

    @Override
    public void validateSuccess(BaseBean id) {
        startActivity(new Intent(this, PasswordRestActivity.class).putExtra(Const.ID, mId));
    }

    @Override
    public void registSuccess(BaseBean baseBean) {
        mLoginPresenter.loginNoPwd(mPhone);
    }

    @Override
    public void bindPhoneResult(BaseBean baseBean) {
        mLoginPresenter.loginNoPwd(mPhone);
    }

    @Override
    public void showMsgCodeSuccess() {

    }

    @Override
    public void showLoginSuccess(LoginBean bean) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void resetSuccess(BaseBean baseBean) {

    }


    @Override
    public void showLoginFaild(String msg) {

    }


}
