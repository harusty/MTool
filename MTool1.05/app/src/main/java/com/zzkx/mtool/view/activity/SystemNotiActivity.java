package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.zzkx.mtool.bean.SystemNotiBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.SystemNotiListPresenter;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.adapter.SystemNotiListAdapter;

/**
 * Created by sshss on 2018/2/23.
 */

public class SystemNotiActivity extends BaseListActivity<SystemNotiBean.DataBean> {
    @Override
    public BaseAdapter getAdapter() {
        return new SystemNotiListAdapter(mTotalData);
    }

    @Override
    public BaseListPresenter getPresenter() {
        return new SystemNotiListPresenter(this);
    }

    @Override
    public void initNet() {
        getPresenter().getListData(1);
    }

    @Override
    public void initViews() {
        super.initViews();
        setMainMenuEnable();
        setMainTitle("系统公告");
        HeaderUtil.addHeader(this, mListView, 0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= mListView.getHeaderViewsCount();
                if (position < 0)
                    return;
                SystemNotiBean.DataBean dataBean = (SystemNotiBean.DataBean) mAdapter.getItem(position);
                startActivity(new Intent(SystemNotiActivity.this, H5ShowActivity.class)
                        .putExtra(Const.CONTENT, dataBean.contentinfo));
            }
        });
    }
}
