package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AttentionShopBean;
import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddTagMemberPresenter;
import com.zzkx.mtool.presenter.AttentionShopPresenter;
import com.zzkx.mtool.presenter.AttentionUserPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.AttentionShopAdapter;
import com.zzkx.mtool.view.adapter.AttentionUserAdapter;
import com.zzkx.mtool.view.customview.DialogInitialNab;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IAddTagMemberView;
import com.zzkx.mtool.view.iview.IAttentionShopView;
import com.zzkx.mtool.view.iview.IAttentionUserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionListActivity extends BaseActivity implements IAttentionShopView, IAttentionUserView, View.OnClickListener, IAddTagMemberView {

    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.icon_red_arrow)
    View mArrow;
    @BindView(R.id.header_container)
    ViewGroup mHeaderLayout;
    @BindView(R.id.ic_title_check)
    View mTitleCheck;
    private int mType;
    private AttentionShopPresenter mAttentionShopPresenter;
    private DialogInitialNab mDialog;
    private AttentionUserPresenter mAttentionUserPresenter;
    public static final int ACTION_SELECT = 2;
    private int mAction;
    private AttentionUserAdapter mAttentionUserAdapter;
    private List<AttentionUserBean.DataBean> mUserDataList;
    private AddTagMemberPresenter mAddTagMemberPresenter;
    public static final int TYPE_USER = 1;
    public static final int TYPE_FANS = 2;

    @Override
    public int getContentRes() {
        return R.layout.activity_attention_list;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mType = getIntent().getIntExtra(Const.TYPE, 0);

        if (mType == TYPE_USER)
            setMainTitle("关注的用户");
        else if (mType == 2)
            setMainTitle("关注我的用户");
        else
            setMainTitle("关注的店铺");


        mAction = getIntent().getIntExtra(Const.ACTION, 0);

        mAttentionShopPresenter = new AttentionShopPresenter(this);
        mAttentionUserPresenter = new AttentionUserPresenter(this);
        mAddTagMemberPresenter = new AddTagMemberPresenter(this);
        HeaderUtil.addHeader(this, mListView, 20);
        if (mAction == ACTION_SELECT) {
            mArrow.setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_bottom).setVisibility(View.GONE);
            mArrow.setOnClickListener(this);
            initInitialDialog();
        }
        mTitleCheck.setOnClickListener(this);
    }

    private void initInitialDialog() {
        mDialog = new DialogInitialNab(this);
        mDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String alpah = (String) view.getTag();
                Integer sectionIndex;
                if (mType == 0) {
                    sectionIndex = mAttentionShopPresenter.getSectionIndex(alpah);
                } else {
                    sectionIndex = mAttentionUserPresenter.getSectionIndex(alpah);
                }
                if (sectionIndex != null) {
                    mListView.setSelection(sectionIndex);
                }
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void initNet() {
        if (mType == TYPE_USER) {
            mAttentionUserPresenter.getUserList(false);
        } else if (mType == TYPE_FANS) {
            mAttentionUserPresenter.getUserList(true);
        } else {
            mAttentionShopPresenter.getShopList();
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showShopData(final List<AttentionShopBean.DataBean> data) {
        mListView.setAdapter(new AttentionShopAdapter(data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= position;
                if (position < 0)
                    return;
                AttentionShopBean.DataBean data1 = mAttentionShopPresenter.getData(position);
                startActivity(new Intent(AttentionListActivity.this, ShopDetailActivity.class).putExtra(Const.ID, data1.shopId));
            }
        });
    }

    @Override
    public void showUserData(List<AttentionUserBean.DataBean> data) {
        mUserDataList = data;
        mAttentionUserAdapter = new AttentionUserAdapter(data, mAction);
        mListView.setAdapter(mAttentionUserAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= position;
                if (position < 0)
                    return;
                if (mAction == ACTION_SELECT) {
                    AttentionUserAdapter.ViewHolder holder = (AttentionUserAdapter.ViewHolder) view.getTag();
                    AttentionUserBean.DataBean item = mAttentionUserAdapter.getItem(position);
                    item.cusSelected = !item.cusSelected;
                    holder.chekcbox.setChecked(item.cusSelected);
                    setBottomContainer(item, position);
                } else {
                    AttentionUserBean.DataBean data1 = mAttentionUserPresenter.getData(position);
                    AttentionUserBean.UserMemberBean userMember = data1.userMember;
                    if (userMember != null)
                        startActivity(new Intent(AttentionListActivity.this, UserDetailActivity.class)
                                .putExtra(Const.ID, userMember.id));
                }

            }
        });
    }

    private Map<Integer, View> mViewCache = new HashMap<>();

    private void setBottomContainer(AttentionUserBean.DataBean item, int position) {
        AttentionUserBean.UserMemberBean userMember = item.userMember;
        if (item.cusSelected) {
            if (mViewCache.get(position) != null) {
                mHeaderLayout.removeView(mViewCache.get(position));
                mViewCache.remove(position);
            }
            RoundImageView1_1W roundImageView1_1W = new RoundImageView1_1W(this);
            int size = Dip2PxUtils.dip2px(this, 45);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(this, 5), 0);
            roundImageView1_1W.setLayoutParams(layoutParams);
            GlideUtil.getInstance().display(roundImageView1_1W, userMember.picUrl);
            roundImageView1_1W.setTag(R.id.child_index, position);
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
            case R.id.icon_red_arrow:
                mDialog.show(mArrow);
                break;
            case R.id.ic_title_check:
                List<String> ids = new ArrayList<>();
                for (AttentionUserBean.DataBean bean : mUserDataList) {
                    if (bean.cusSelected)
                        ids.add(bean.userMember.hxUsername);
                }
                mAddTagMemberPresenter.addMember(getIntent().getStringExtra(Const.ID), ids);
                break;
        }
    }

    @Override
    public void showAddResult(BaseBean bean) {
        if (bean.status == 1) {
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        } else {
            ToastUtils.showToast("添加失败，请重试");
        }
    }
}
