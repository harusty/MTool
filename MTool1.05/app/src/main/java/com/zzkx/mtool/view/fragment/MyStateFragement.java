package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MyStatePresenter;
import com.zzkx.mtool.presenter.StateDeletePresenter;
import com.zzkx.mtool.presenter.SupportCanclePresenter;
import com.zzkx.mtool.presenter.SupportPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IMyStateView;
import com.zzkx.mtool.view.iview.IStateList;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/11.
 */

public class MyStateFragement extends BaseFragment implements IMyStateView, ISupportCancleView, ISupportView, IStateList, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private List<StateListBean.DataBean> mTotalData;
    private StateListAdapter mStateListAdapter;
    private MyStatePresenter mMyStatePresenter;
    private int mClickPosition;
    private SupportCanclePresenter mSupportCanclePresenter;
    private SupportPresenter mSupportPresenter;
    private DialogState mDialogStateMore;
    private StateDeletePresenter mStateDeletePresenter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getActivity(), 10));
        HeaderUtil.addHeader(getActivity(), mListView, 10);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mMyStatePresenter = new MyStatePresenter(this);

        mSupportCanclePresenter = new SupportCanclePresenter(this);
        mSupportPresenter = new SupportPresenter(this);
        mStateDeletePresenter = new StateDeletePresenter(this);
        mDialogStateMore = new DialogState(getContext(), new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                StateListBean.DataBean dataBean = mTotalData.get(mClickPosition);
                switch (v.getId()) {
                    case R.id.delete:

                        mStateDeletePresenter.delete(dataBean.id, mClickPosition);
                        mDialogStateMore.dismiss();
                        break;
                }

            }
        });
    }

    @Override
    public void initNet() {
        mMyStatePresenter.getMyState();
    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void showMyState(StateListBean bean) {
        mTotalData = bean.data;
        if (mTotalData != null && mTotalData.size() > 0) {
            if (mStateListAdapter == null) {
                mStateListAdapter = new StateListAdapter(getActivity(), mTotalData, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Object tag = v.getTag();
                        if(tag instanceof Integer) {
                            mClickPosition = (int) tag;
                        }else {
                            mClickPosition = (int) v.getTag(R.id.child_index);
                        }
                        StateListBean.DataBean item = mStateListAdapter.getItem(mClickPosition);

                        switch (v.getId()) {

                            case R.id.ic_msg:
                                startActivityForResult(new Intent(getActivity(), StateDetailActivity.class)
                                        .putExtra(Const.OBJ, item)
                                        .putExtra(Const.TO_REPLY, true), 9
                                );
                                break;
                            case R.id.ic_support:
                                if (item.suppoppType == 1) {
                                    mSupportCanclePresenter.cancleSupport(item.id, mClickPosition);
                                } else {
                                    mSupportPresenter.support(item.id, mClickPosition);
                                }
                                break;
                            case R.id.iv_more:
                                StateListBean.UserMemberBean userMember = item.userMember;
                                if (userMember != null)
                                    mDialogStateMore.show(userMember.id, item.collectType);
                                break;
                        }
                    }
                });
//                mStateListAdapter.setType(StateListAdapter.TYPE_SUPPOSED);
                mListView.setAdapter(mStateListAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        position = position - mListView.getHeaderViewsCount();
                        if (position < 0)
                            return;

                        if (position < mTotalData.size()) {
                            StateListBean.DataBean dataBean = mTotalData.get(position);
                            int itemViewType = mStateListAdapter.getItemViewType(position);
                            Intent intent = new Intent(getActivity(), StateDetailActivity.class);
                            intent.putExtra(Const.TYPE, itemViewType);
                            intent.putExtra(Const.ID, dataBean.id);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                mStateListAdapter.notifyDataSetChanged();
            }
        } else {
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
    }

    @Override
    public void onReload() {
        initNet();
    }


    @Override
    public void showCancleSupportResult(BaseBean bean) {
        if (bean.status == 1) {
            int cusTag = (int) bean.cusTag;
            StateListBean.DataBean dataBean = mTotalData.get(cusTag);
            dataBean.supports--;
            dataBean.suppoppType = 0;
            mStateListAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void onSuppotedSuccess(BaseBean bean) {
        if (bean.status == 1) {
            String cusTag = (String) bean.cusTag;
            if (!TextUtils.isEmpty(cusTag)) {
                StateListBean.DataBean dataBean = mTotalData.get(Integer.parseInt(cusTag));
                dataBean.supports++;
                dataBean.suppoppType = 1;
                mStateListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showDelete(BaseBean bean) {
        if (bean.status == 1) {
            mTotalData.remove(((int) bean.cusTag));
            mStateListAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
