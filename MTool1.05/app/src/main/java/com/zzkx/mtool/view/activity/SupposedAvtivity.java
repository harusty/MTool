package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.StateCollectPresenter;
import com.zzkx.mtool.presenter.StateDeletePresenter;
import com.zzkx.mtool.presenter.SupportCanclePresenter;
import com.zzkx.mtool.presenter.SupposedStatePresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.IMySupposedStateView;
import com.zzkx.mtool.view.iview.IStateCollectView;
import com.zzkx.mtool.view.iview.IStateList;
import com.zzkx.mtool.view.iview.ISupportCancleView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/10.
 */

public class SupposedAvtivity extends BaseActivity implements IMySupposedStateView, IStateList, IStateCollectView, ICancleCollectionView, ISupportCancleView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private StateListAdapter mAdapter;
    private List<StateListBean.DataBean> mTotalData;
    private SupposedStatePresenter mSupposedStatePresenter;
    private StateCollectPresenter mStateCollectPresenter;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    private SupportCanclePresenter mSupportCanclePresenter;
    private StateDeletePresenter mStateDeletePresenter;
    public DialogState mDialogStateMore;
    public int mClickPosition;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("我赞过的");
        HeaderUtil.addHeader(this, mListView, 10);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(this, 10));
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSupposedStatePresenter = new SupposedStatePresenter(this);
        mRefreshLayout.setOnRefreshListener(this);
        mStateCollectPresenter = new StateCollectPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        mSupportCanclePresenter = new SupportCanclePresenter(this);
        mStateDeletePresenter = new StateDeletePresenter(this);
    }

    @Override
    public void initNet() {
        mSupposedStatePresenter.getData();
    }

    @Override
    public void showProgress(boolean toShow) {
        mRefreshLayout.setRefreshing(toShow);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showData(StateListBean bean) {
        mTotalData = bean.data;
        if (mTotalData != null && mTotalData.size() > 0) {
            if (mAdapter == null) {
                mAdapter = new StateListAdapter(this, mTotalData, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Object tag = v.getTag();
                        if (tag instanceof Integer) {
                            mClickPosition = (int) tag;
                        } else {
                            mClickPosition = (int) v.getTag(R.id.child_index);
                        }

                        StateListBean.DataBean item = mAdapter.getItem(mClickPosition);
                        switch (v.getId()) {
                            case R.id.iv_user_header:
                                HeadClickUtil.handleClick(SupposedAvtivity.this,item.memId,null);
                                break;
                            case R.id.ic_msg:
                                startActivityForResult(new Intent(SupposedAvtivity.this, StateDetailActivity.class)
                                        .putExtra(Const.OBJ, item)
                                        .putExtra(Const.TO_REPLY, true), 9
                                );
                                break;
                            case R.id.ic_support:
                                mSupportCanclePresenter.cancleSupport(item.id, mClickPosition);
                                break;
                            case R.id.iv_more:
                                if (mDialogStateMore == null)
                                    mDialogStateMore = new DialogState(SupposedAvtivity.this, new View.OnClickListener() {


                                        @Override
                                        public void onClick(View v) {
                                            StateListBean.DataBean dataBean = mTotalData.get(mClickPosition);
                                            switch (v.getId()) {
                                                case R.id.delete:
                                                    mStateDeletePresenter.delete(dataBean.id, mClickPosition);
                                                    mDialogStateMore.dismiss();
                                                    break;
                                                case R.id.collect:
                                                    boolean isCollected = (boolean) v.getTag();
                                                    if (isCollected) {
                                                        mCancleCollectionPresenter.cancleCollection(dataBean.id, 2, mClickPosition);
                                                    } else {
                                                        mStateCollectPresenter.collectState(dataBean.id, mClickPosition);
                                                    }
                                                    break;
                                            }

                                        }
                                    });
                                StateListBean.UserMemberBean userMember = item.userMember;
                                if (userMember != null)
                                    mDialogStateMore.show(userMember.id, item.collectType);
                                break;
                        }
                    }
                });
                mAdapter.setType(StateListAdapter.TYPE_SUPPOSED);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        position = position - mListView.getHeaderViewsCount();
                        if (position > -1) {
                            StateListBean.DataBean dataBean = mTotalData.get(position);
                            int itemViewType = mAdapter.getItemViewType(position);
                            Intent intent = new Intent(SupposedAvtivity.this, StateDetailActivity.class);
                            intent.putExtra(Const.TYPE, itemViewType);
                            intent.putExtra(Const.OBJ, dataBean);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
    }

    @Override
    public void showDelete(BaseBean bean) {
        if (bean.status == 1) {
            mTotalData.remove(((int) bean.cusTag));
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("收藏成功");
            int tag = (int) bean.cusTag;
            mAdapter.getItem(tag).collectType = 1;
            mDialogStateMore.setCollected(1);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCancleCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            int tag = (int) bean.cusTag;
            mAdapter.getItem(tag).collectType = 0;
            mDialogStateMore.setCollected(0);
            ToastUtils.showToast("取消成功");
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCancleSupportResult(BaseBean bean) {
        if (bean.status == 1) {
            int cusTag = (int) bean.cusTag;
            mTotalData.remove(cusTag);
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
