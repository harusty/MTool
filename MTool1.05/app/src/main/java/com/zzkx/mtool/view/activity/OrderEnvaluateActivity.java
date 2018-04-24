package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.view.fragment.OrderEvaluationFragment;
import com.zzkx.mtool.view.iview.IOrderDetailView;

/**
 * Created by sshss on 2017/9/21.
 */

public class OrderEnvaluateActivity extends BaseActivity implements IOrderDetailView {
    private OrderEvaluationFragment mOrderEvaluationFragment;

    @Override
    public int getContentRes() {
        return R.layout.layout_container;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("评价订单");
        mOrderEvaluationFragment = new OrderEvaluationFragment();
        mOrderEvaluationFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mOrderEvaluationFragment).commit();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showData(OrderDetailBean bean) {

    }
}
