package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BankCardBean;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CardListPresenter;
import com.zzkx.mtool.presenter.WithdrawPresenter;
import com.zzkx.mtool.util.Amount;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogCard;
import com.zzkx.mtool.view.iview.ICardListView;
import com.zzkx.mtool.view.iview.IWithdrawView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/19.
 */

public class WithDrawActivity extends BaseActivity implements View.OnClickListener, ICardListView, IWithdrawView {
    private CardListPresenter mCardListPresenter;
    @BindView(R.id.tv_def_info)
    TextView tv_def_info;
    @BindView(R.id.layout_card)
    View layout_card;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_extra)
    TextView tv_extra;

    private DialogCard mDialogCard;
    private BankCardBean.Data mCurrentCard;
    private WithdrawPresenter mWithdrawPresenter;


    @Override
    public int getContentRes() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("提现");
        mCardListPresenter = new CardListPresenter(this);
        mWithdrawPresenter = new WithdrawPresenter(this);
        findViewById(R.id.tv_withdraw).setOnClickListener(this);
        mDialogCard = new DialogCard(this, new DialogCard.OnAddListener() {
            @Override
            public void OnAdd() {
                addAction();
                mDialogCard.dismiss();
            }

            @Override
            public void onCardClick(BankCardBean.Data dataBean) {
                mCurrentCard = dataBean;
                tv_def_info.setText(dataBean.bankName + "(" + dataBean.bankCarNo.substring(dataBean.bankCarNo.length() - 4, dataBean.bankCarNo.length()) + ")");
                mDialogCard.dismiss();
            }
        });
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    long l = Long.parseLong(content);
                    if (l > 0) {
                        double extra = new Amount(l).mul(l, 0.001);
                        tv_extra.setText("额外扣除￥" + extra + "（服务费）");
                    } else {
                        tv_extra.setText("额外扣除￥0.0（服务费）");
                    }
                } else {
                    tv_extra.setText("额外扣除￥0.0（服务费）");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addAction() {
        startActivityForResult(new Intent(this, AddCardActivity.class), 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            initNet();
        }
    }

    @Override
    public void initNet() {
        mCardListPresenter.getCardList();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_withdraw:
                String s = et_money.getText().toString();
                if (!TextUtils.isEmpty(s) || Long.parseLong(s) > 0) {
                    RequestBean requestBean = new RequestBean();
                    requestBean.bankId = mCurrentCard.id;
                    requestBean.withdrawalMoney = s;
                    mWithdrawPresenter.withDraw(requestBean);
                } else {
                    ToastUtils.showToast("请输入正确提现金额");
                }
                break;
        }
    }

    @Override
    public void showCardList(BankCardBean bean) {
        List<BankCardBean.Data> data = bean.data;
        if (data == null || data.size() == 0) {
            tv_def_info.setText("添加银行卡");
            layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAction();
                }
            });
        } else {
            mDialogCard.setData(data);
            layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogCard.show();
                }
            });
        }
    }

    @Override
    public void showDefCardInfo(final BankCardBean.Data dataBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCurrentCard = dataBean;
                tv_def_info.setText(dataBean.bankName + "(" + dataBean.bankCarNo.substring(dataBean.bankCarNo.length() - 4, dataBean.bankCarNo.length()) + ")");
            }
        });
    }

    @Override
    public void showWithDrawResult(BaseBean bean) {
        ToastUtils.showToast(bean.msg);
        if (bean.status == 1) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }
}
