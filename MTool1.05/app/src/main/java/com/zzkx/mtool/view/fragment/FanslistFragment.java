package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AttentionUserPresenter;
import com.zzkx.mtool.view.activity.UserDetailActivity;
import com.zzkx.mtool.view.adapter.AttentionUserAdapter;
import com.zzkx.mtool.view.iview.IAttentionUserView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/16.
 */

public class FanslistFragment extends ContactSubFragment implements IAttentionUserView {
    private AttentionUserPresenter mAttentionUserPresenter;
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private List<AttentionUserBean.DataBean> mUserDataList;
    private AttentionUserAdapter mAttentionUserAdapter;

    @Override
    public void onInitialClick(View view) {
        String alpah = (String) view.getTag();
        Integer sectionIndex;
        sectionIndex = mAttentionUserPresenter.getSectionIndex(alpah);
        if (sectionIndex != null) {
            mListView.setSelection(sectionIndex);
        }
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mAttentionUserPresenter = new AttentionUserPresenter(this);
    }

    @Override
    public void initNet() {
        mAttentionUserPresenter.getUserList(true);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void showUserData(List<AttentionUserBean.DataBean> data) {
        mUserDataList = data;
        mAttentionUserAdapter = new AttentionUserAdapter(data, 0);
        mListView.setAdapter(mAttentionUserAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= position;
                if (position < 0)
                    return;
                AttentionUserBean.DataBean data1 = mAttentionUserPresenter.getData(position);
                AttentionUserBean.UserMemberBean userMember = data1.userMember;
                if (userMember != null)
                    startActivity(new Intent(getActivity(), UserDetailActivity.class)
                            .putExtra(Const.ID, userMember.id));

            }
        });
    }

    @Override
    public void showEmpty() {

    }
}
