package com.zzkx.mtool.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.presenter.AddAttenionPresenter;
import com.zzkx.mtool.presenter.AttentionUserPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.view.iview.IAddAttentionView;
import com.zzkx.mtool.view.iview.IAttentionUserView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/12.
 */

public class NewFriendFragment extends BaseFragment implements IAttentionUserView, IAddAttentionView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private AttentionUserPresenter mAttentionUserPresenter;
    private TmpAdapter mTmpAdapter;
    private AddAttenionPresenter mAddAttenionPresenter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        HeaderUtil.addHeader(getActivity(), mListView, 10);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getActivity(), 10));
        mListView.setHeaderDividersEnabled(false);

        mAttentionUserPresenter = new AttentionUserPresenter(this);
        mAddAttenionPresenter = new AddAttenionPresenter(this);
    }

    @Override
    public void initNet() {
        super.initNet();
        mAttentionUserPresenter.getUserList(true);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showUserData(List<AttentionUserBean.DataBean> data) {
        mTmpAdapter = new TmpAdapter(data);
        mListView.setAdapter(mTmpAdapter);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void showAttentionAddSuccess(BaseBean bean) {
        initNet();
    }

    @Override
    public void showDelAttentionFaild(BaseBean bean) {

    }

    @Override
    public void onRefresh() {
        initNet();
    }


    private class TmpAdapter extends BaseAdapter {

        private List<AttentionUserBean.DataBean> mData;

        public TmpAdapter(List<AttentionUserBean.DataBean> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public AttentionUserBean.DataBean getItem(int position) {

            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AttentionUserBean.DataBean item = getItem(position);
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_new_fri_attention, null);
            ImageView head = (ImageView) convertView.findViewById(R.id.iv_head);
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            final AttentionUserBean.UserMemberBean userMember = item.userMember;
            if (userMember != null) {
                GlideUtil.getInstance().display(head, userMember.picUrl);
                name.setText(userMember.nickname);
                convertView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddAttenionPresenter.add(userMember.id);
                    }
                });
            }
            return convertView;
        }
    }
}
