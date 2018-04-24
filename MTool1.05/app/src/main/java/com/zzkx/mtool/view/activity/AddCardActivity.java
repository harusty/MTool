package com.zzkx.mtool.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddCardPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IAddCardVew;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/4.
 */

public class AddCardActivity extends BaseActivity implements IAddCardVew, View.OnClickListener {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_card_num)
    EditText et_card_num;
    @BindView(R.id.et_bankname)
    EditText et_bankname;
    private AddCardPresenter mAddCardPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_add_card;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("添加银行卡");
        mAddCardPresenter = new AddCardPresenter(this);
        findViewById(R.id.tv_add).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showAddResult(BaseBean bean) {
        ToastUtils.showToast(bean.msg);
        if(bean.status== 1){
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        String name = et_name.getText().toString();
        String cardNum = et_card_num.getText().toString();
        String bankName = et_bankname.getText().toString();
        if (TextUtils.isEmpty(bankName)) {
            ToastUtils.showToast("请输入开户行名称");
            return;
        }
        if (TextUtils.isEmpty(cardNum)) {
            ToastUtils.showToast("请输入银行卡号");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast("请输入持卡人姓名");
            return;
        }

        RequestBean requestBean = new RequestBean();
        requestBean.bankName = bankName;
        requestBean.bankCarNo = cardNum;
        requestBean.name = name;
        mAddCardPresenter.addCard(requestBean);
    }
}
