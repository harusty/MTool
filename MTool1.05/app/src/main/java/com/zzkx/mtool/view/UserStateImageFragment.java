package com.zzkx.mtool.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.hyphenate.easeui.EaseConstant;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MysStateGallaryBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.UserGallaryPresenter;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.adapter.MyStateGallaryAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.iview.IMyStateGallaryView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/11.
 */

public class UserStateImageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IMyStateGallaryView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private UserGallaryPresenter mMyStateGallaryPresenter;
    private View.OnClickListener mOnClickListener;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        String id = getActivity().getIntent().getStringExtra(Const.ID);
        String hxId = getActivity().getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        if (id == null && hxId == null)
            id = SPUtil.getString(Const.U_ID, "");
        mMyStateGallaryPresenter = new UserGallaryPresenter(this, id, hxId);
    }

    @Override
    public void initNet() {
        mMyStateGallaryPresenter.getStateGallary();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onRefresh() {
        initNet();
    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void showMyGallary(MysStateGallaryBean bean) {
        MyStateGallaryAdapter myStateGallaryAdapter = new MyStateGallaryAdapter(bean);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag(R.id.child_index);
                startActivity(new Intent(getActivity(), StateImgActivity.class)
                        .putExtra(Const.RES, mMyStateGallaryPresenter.getOnlyResData())
                        .putExtra(Const.INDEX, index)
                );
            }
        };
        myStateGallaryAdapter.setOnResClickListener(mOnClickListener);
        mListView.setAdapter(myStateGallaryAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnClickListener = null;
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

}
