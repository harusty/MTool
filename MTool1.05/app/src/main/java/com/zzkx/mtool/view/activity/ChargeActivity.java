package com.zzkx.mtool.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ChargeAliPresenter;
import com.zzkx.mtool.presenter.ChargeWxPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.util.pay.ali.AliPay;
import com.zzkx.mtool.util.pay.wx.WxPay;
import com.zzkx.mtool.view.iview.IChargeAliView;
import com.zzkx.mtool.view.iview.IChargeWxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/19.
 */

public class ChargeActivity extends BaseActivity implements View.OnClickListener, IChargeWxView, IChargeAliView {

    @BindView(R.id.rb_ali)
    RadioButton rb_ali;
    @BindView(R.id.rb_wechat)
    RadioButton rb_wechat;
    @BindView(R.id.et_money)
    EditText et_money;
    private ChargeWxPresenter mChargeWxPresenter;
    private ChargeAliPresenter mChargeAliPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void payReuslt(BusPayBean payBean) {
        if (payBean.mCode == payBean.SUCESS) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_charge;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("钱  包");
        findViewById(R.id.pay_ali).setOnClickListener(this);
        findViewById(R.id.pay_wechat).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.pay_ali).performClick();
        mChargeWxPresenter = new ChargeWxPresenter(this);
        mChargeAliPresenter = new ChargeAliPresenter(this);

    }

    @Override
    public void onReload() {

    }

    private int payType = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_ali:
                rb_ali.setChecked(true);
                rb_wechat.setChecked(false);
                payType = 1;
                break;
            case R.id.pay_wechat:
                rb_ali.setChecked(false);
                rb_wechat.setChecked(true);
                payType = 0;
                break;
            case R.id.tv_next:
                String s = et_money.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("请输入充值金额");
                    return;
                }
                if (Long.parseLong(s) == 0) {
                    ToastUtils.showToast("请输入正确的充值金额");
                    return;
                }
                RequestBean requestBean = new RequestBean();
                requestBean.totalPrice = s;
                requestBean.pathType = payType;
                if (payType == 0) {
                    requestBean.pathName = "微信支付";
                    mChargeWxPresenter.getPayInfo(requestBean);
                } else {
                    requestBean.pathName = "支付宝支付";
                    mChargeAliPresenter.getPayInfo(requestBean);
                }
                break;
        }
    }

    @Override
    public void showWxChargeInfo(OrderPayResultBean bean) {
        new WxPay(MyApplication.getContext()).parseWxPayInfo(bean);
    }

    @Override
    public void showAliChargeInfo(OrderPayResultBean bean) {
        new AliPay().parseAli(this,bean);
    }
}
