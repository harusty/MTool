package com.zzkx.mtool.chat.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.BlackListBean;
import com.zzkx.mtool.chat.adapter.BlackListAdapter;
import com.zzkx.mtool.presenter.BlackListPresenter;
import com.zzkx.mtool.presenter.DeleteBlackListPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IBlackListView;
import com.zzkx.mtool.view.iview.IDelBlackListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Blacklist screen
 */
public class BlacklistActivity extends BaseActivity implements IBlackListView, View.OnClickListener, IDelBlackListView {

    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.ic_title_check)
    View mTitleCheck;
    @BindView(R.id.header_container)
    LinearLayout mHeaderLayout;


    private BlackListPresenter mBlackListPresenter;
    private BlackListAdapter mAdapter;
    private DeleteBlackListPresenter mDeleteBlackListPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_friend_select;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("屏蔽管理");
        mBlackListPresenter = new BlackListPresenter(this);
        mDeleteBlackListPresenter = new DeleteBlackListPresenter(this);
        mTitleCheck.setOnClickListener(this);
    }

    @Override
    public void initNet() {
        mBlackListPresenter.getBlackList();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showBlackList(List<BlackListBean.DataBean> bean) {
        mAdapter = new BlackListAdapter(bean);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlackListAdapter.ViewHolder holder = (BlackListAdapter.ViewHolder) view.getTag();
                BlackListBean.DataBean item = mAdapter.getItem(position);
                item.cusSelected = !item.cusSelected;
                holder.chekcbox.setChecked(item.cusSelected);
                setBottomContainer(item, position);
            }
        });
    }

    private Map<Integer, View> mViewCache = new HashMap<>();

    private void setBottomContainer(BlackListBean.DataBean item, int position) {
        if (item.cusSelected) {
            if (mViewCache.get(position) != null) {
                mHeaderLayout.removeView(mViewCache.get(position));
                mViewCache.remove(position);
            }
            RoundImageView1_1W roundImageView1_1W = new RoundImageView1_1W(BlacklistActivity.this);
            int size = Dip2PxUtils.dip2px(BlacklistActivity.this, 45);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(BlacklistActivity.this, 5), 0);
            roundImageView1_1W.setLayoutParams(layoutParams);
            GlideUtil.getInstance().display(roundImageView1_1W, item.picUrl);
            roundImageView1_1W.setTag(R.id.child_index, position);
            roundImageView1_1W.setTag(item.nickname);
            mViewCache.put(position, roundImageView1_1W);
            mHeaderLayout.addView(roundImageView1_1W);
        } else {
            Integer index = null;
            for (int i = 0; i < mHeaderLayout.getChildCount(); i++) {
                index = (int) mHeaderLayout.getChildAt(i).getTag(R.id.child_index);
            }
            if (index != null) {
                View view = mViewCache.get(index);
                mHeaderLayout.removeView(view);
                mViewCache.remove(index);
            }
        }
        if (mHeaderLayout.getChildCount() > 0) {
            mTitleCheck.setVisibility(View.VISIBLE);
        } else {
            mTitleCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showEmpty() {
        mStateView.setCurrentState(StateView.ResultState.EMPTY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_title_check:
                List<BlackListBean.DataBean> data = mAdapter.getData();
                List<String> ids = new ArrayList<>();
                for (BlackListBean.DataBean bean : data) {
                    if (bean.cusSelected)
                        ids.add(bean.hxUsername);
                }
                mDeleteBlackListPresenter.deleteFromBlackList(ids);
                break;
        }
    }

    @Override
    public void showDeleteResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("删除成功");
            mViewCache.clear();
            mHeaderLayout.removeAllViews();
            mTitleCheck.setVisibility(View.INVISIBLE);
            initNet();
        } else {
            ToastUtils.showToast("删除失败，请重试");
        }
    }
}
