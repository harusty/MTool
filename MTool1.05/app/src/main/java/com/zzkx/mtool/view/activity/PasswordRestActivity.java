package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CodeBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.RegistPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IRegistView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/29.
 */

public class PasswordRestActivity extends BaseActivity implements IRegistView {
    @BindView(R.id.et_password1)
    EditText mEtPwd1;
    @BindView(R.id.et_password2)
    EditText mEtPwd2;
    private RegistPresenter mPresenter;
    private String mId;

    @Override
    public int getContentRes() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("更改密码");
        mPresenter = new RegistPresenter(this, this);
        mId = getIntent().getStringExtra(Const.ID);
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = mEtPwd1.getText().toString();
                String s2 = mEtPwd2.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    ToastUtils.showToast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(s2)) {
                    ToastUtils.showToast("请再次输入确认密码");
                    return;
                }
                if (!TextUtils.equals(s1, s2)) {
                    ToastUtils.showToast("两次输入密码不一致，请重新输入");
                    return;
                }
                mPresenter.toReset(mId, s1);
            }
        });

    }

    @Override
    public void resetSuccess(BaseBean baseBean) {
        if (TextUtils.isEmpty(baseBean.msg))
            baseBean.msg = "重置密码成功";

        ToastUtils.showToast(baseBean.msg);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void registSuccess(BaseBean baseBean) {

    }

    @Override
    public void bindPhoneResult(BaseBean baseBean) {

    }

    @Override
    public void onReload() {

    }

    @Override
    public void showReceiveCode(CodeBean bean) {

    }

    @Override
    public void validateSuccess(BaseBean baseBean) {

    }
}
