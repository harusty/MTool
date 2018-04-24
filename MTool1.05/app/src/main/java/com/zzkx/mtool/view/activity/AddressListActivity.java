package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddressListPresnter;
import com.zzkx.mtool.view.adapter.AddresListAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IAddListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddressListActivity extends BaseActivity implements IAddListView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwLayout;
    private AddressListPresnter mPresenter;
    private AddresListAdapter mAdapter;
    private List<AddressListBean.AddressBean> mData = new ArrayList<>();

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle(getString(R.string.acceptInfo));
        mPresenter = new AddressListPresnter(this);
        View footer = View.inflate(this, R.layout.footer_add_add, null);
        View header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.marginBorder)));
        mListView.addHeaderView(header);
        footer.setOnClickListener(this);
        mListView.addFooterView(footer);
        mSwLayout.setOnRefreshListener(this);
        mSwLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initNet() {
        mPresenter.getAddress();
    }

    @Override
    public void showError(ErrorBean errorBean) {
        super.showError(errorBean);
        showProgress(false);
        mSwLayout.setRefreshing(false);

    }

    @Override
    public void showProgress(boolean toShow) {
        if (!toShow)
            mStateView.setCurrentState(StateView.ResultState.SUCESS);
        mSwLayout.setRefreshing(toShow);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(AddressListActivity.this, AddressEditActivity.class)
                .putExtra(Const.IS_ADD, true), 1
        );
    }


    @Override
    public void showAddList(List<AddressListBean.AddressBean> data) {
        mData.clear();
        mData.addAll(data);

        if (mAdapter == null) {
            mAdapter = new AddresListAdapter(this, mData);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position -= mListView.getHeaderViewsCount();
                    if (position < mData.size()) {
                        AddressListBean.AddressBean addressBean = mData.get(position);
                        startActivityForResult(new Intent(AddressListActivity.this, AddressEditActivity.class)
                                .putExtra(Const.IS_ADD, false)
                                .putExtra(Const.LOC_INFO, addressBean), 1
                        );
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            mSwLayout.setRefreshing(true);
            initNet();
            setResult(Const.RESULT_SUCESS_CODE);
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
