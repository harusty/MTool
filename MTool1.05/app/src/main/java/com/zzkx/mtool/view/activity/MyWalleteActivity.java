package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MyMoneyBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MyMoneyPresenter;
import com.zzkx.mtool.view.iview.IMyMoneyView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/19.
 */

public class MyWalleteActivity extends BaseActivity implements View.OnClickListener, IMyMoneyView {
    @BindView(R.id.tv_money)
    TextView mTvMoeny;
    private MyMoneyPresenter mMyMoneyPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_my_wallete;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("钱  包");
        findViewById(R.id.tv_charge).setOnClickListener(this);
        findViewById(R.id.tv_widthdraw).setOnClickListener(this);
        findViewById(R.id.tv_zhangdan).setOnClickListener(this);
        findViewById(R.id.tv_question).setOnClickListener(this);
        mMyMoneyPresenter = new MyMoneyPresenter(this);
    }

    @Override
    public void initNet() {
        mMyMoneyPresenter.getWalleteInfo();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_charge:
                startActivityForResult(new Intent(this, ChargeActivity.class), 99);
                break;
            case R.id.tv_widthdraw:
                startActivity(new Intent(this, WithDrawActivity.class));
                break;
            case R.id.tv_zhangdan:
                startActivity(new Intent(this, BillActivity.class));
                break;
            case R.id.tv_question:
                startActivity(new Intent(this, H5ShowActivity.class).putExtra(Const.URL, "https://www.baidu.com/"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            initNet();
        }
    }

    @Override
    public void showWalletyInfo(MyMoneyBean bean) {
        MyMoneyBean.DataBean data = bean.data;
        if (data != null) {
            mTvMoeny.setText(data.account);
        }
    }
}
