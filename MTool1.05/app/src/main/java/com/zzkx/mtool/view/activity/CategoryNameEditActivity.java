package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CatNameEditPreseter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ICatNameEditView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/17.
 */

public class CategoryNameEditActivity extends BaseActivity implements ICatNameEditView {
    public static final int ACTION_CREATE = 0;
    public static final int ACTION_EDIT = 1;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    private CatNameEditPreseter mCatNameEditPresenter;
    private int mType;

    @Override
    public int getContentRes() {
        return R.layout.activity_catname_edit;
    }

    @Override
    public void initViews() {
        setMainTitle("编辑名称");
        setMainMenuEnable();
        String name = getIntent().getStringExtra(Const.NAME);
        mEtName.setText(name);
        mType = getIntent().getIntExtra(Const.TYPE, ACTION_CREATE);
        mCatNameEditPresenter = new CatNameEditPreseter(this);

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showToast("分类名称不能为空");
                    return;
                }

                switch (mType) {
                    case ACTION_CREATE:
                        Intent intent = new Intent();
                        intent.putExtra(Const.NAME, mEtName.getText().toString());
                        intent.putExtra(Const.TYPE, ACTION_CREATE);
                        setResult(Const.RESULT_SUCESS_CODE, intent);
                        finish();
                        break;
                    case ACTION_EDIT:
                        String id = getIntent().getStringExtra(Const.ID);
                        String url = getIntent().getStringExtra(Const.URL);
                        if (url != null)
                            mCatNameEditPresenter.editName(id, name, url);
                        else
                            mCatNameEditPresenter.editName(id, name);
                        break;
                }
            }
        });
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showError(ErrorBean errorBean) {
        ToastUtils.showToast(getString(R.string.netErroRetry));
    }

    @Override
    public void showEditResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("修改成功");
            Intent intent = new Intent();
            intent.putExtra(Const.NAME, mEtName.getText().toString());
            intent.putExtra(Const.TYPE, ACTION_EDIT);
            setResult(Const.RESULT_SUCESS_CODE, intent);
            finish();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

}
