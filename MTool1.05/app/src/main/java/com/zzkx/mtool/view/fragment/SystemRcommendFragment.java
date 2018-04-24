package com.zzkx.mtool.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.SystemRecommendListBean;
import com.zzkx.mtool.presenter.AddAttenionPresenter;
import com.zzkx.mtool.presenter.SystemRecommendPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.SystemRecommendAdapter;
import com.zzkx.mtool.view.iview.IAddAttentionView;
import com.zzkx.mtool.view.iview.IRecommentListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/12.
 */

public class SystemRcommendFragment extends BaseFragment implements IRecommentListView, SwipeRefreshLayout.OnRefreshListener, IAddAttentionView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private SystemRecommendPresenter mSystemRecommendPresenter;
    private AddAttenionPresenter mAddAttenionPresenter;
    private SystemRecommendAdapter mAdapter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mSystemRecommendPresenter = new SystemRecommendPresenter(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mAddAttenionPresenter = new AddAttenionPresenter(this);
    }

    @Override
    public void initNet() {
        mSystemRecommendPresenter.getRecommendUser();
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
    public void showRecommendList(SystemRecommendListBean bean) {
        List<SystemRecommendListBean.DataBean> data = bean.data;
        if (data == null || data.size() == 0) {
//            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        } else {
            mAdapter = new SystemRecommendAdapter(data, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    SystemRecommendListBean.DataBean item = (SystemRecommendListBean.DataBean) mListView.getAdapter().getItem(position);
                    mAddAttenionPresenter.add(item.id, position);
                }
            });
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
    @Override
    public void showAttentionAddSuccess(BaseBean bean) {
        int position = (int) bean.cusTag;
        mAdapter.getItem(position).idolType = 1;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDelAttentionFaild(BaseBean bean) {
        ToastUtils.showToast(bean.msg);
    }
}
