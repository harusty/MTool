package com.zzkx.mtool.view.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BillBean;
import com.zzkx.mtool.presenter.BillPresenter;
import com.zzkx.mtool.view.adapter.BillAdapter;
import com.zzkx.mtool.view.iview.IBillView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/19.
 */

public class BillActivity extends BaseActivity implements IBillView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private BillPresenter mBillPresenter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("账单明细");
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mBillPresenter = new BillPresenter(this);
    }

    @Override
    public void initNet() {
        mBillPresenter.getBillList();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void shoBillList(List<BillBean.DataBean> data) {
        if (data != null) {
            mListView.setAdapter(new BillAdapter(data));
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
