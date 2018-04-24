package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.TagMemberBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.DeleteTagUserPresenter;
import com.zzkx.mtool.presenter.TagMemberListPresetner;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.TagMemberListAdapter;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.iview.IDeleteTagUserView;
import com.zzkx.mtool.view.iview.ITagMemberListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/4.
 */

public class ContactTagDetailActivity extends BaseActivity implements ITagMemberListView, IDeleteTagUserView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.ic_title_check)
    View mTitleCheck;
    @BindView(R.id.header_container)
    ViewGroup mHeaderLayout;
    private TagMemberListPresetner mTagMemberListPresetner;
    private String mId;
    private TagMemberListAdapter mAdapter;
    private List<TagMemberBean.Data> mData = new ArrayList<TagMemberBean.Data>();
    private DeleteTagUserPresenter mDeleteTagUserPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_friend_select;
    }

    @Override
    public void initViews() {
        setMainTitle("编辑标签");
        setMainMenuEnable();
        View header = View.inflate(this, R.layout.header_tag_add_member, null);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ContactTagDetailActivity.this, ContactSelectActivity.class)
                                .putExtra(Const.ID, mId)
                        , 99);
            }
        });
        mListView.addHeaderView(header);
        notifyAdapter();
        mTagMemberListPresetner = new TagMemberListPresetner(this);
        mDeleteTagUserPresenter = new DeleteTagUserPresenter(this);
        mId = getIntent().getStringExtra(Const.ID);
        mTitleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ids = new ArrayList<String>();
                for (TagMemberBean.Data bean : mData) {
                    if (bean.cusSelected)
                        ids.add(bean.id);
                }
                mDeleteTagUserPresenter.deleteTagUsers(mId, ids);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            initNet();
        }
    }

    private void notifyAdapter() {
        if (mAdapter == null) {
            mAdapter = new TagMemberListAdapter(mData);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initNet() {
        mTagMemberListPresetner.getMemberList(mId);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showMemberList(TagMemberBean bean) {
        List<TagMemberBean.Data> data = bean.data;
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyAdapter();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position -= mListView.getHeaderViewsCount();
                    if (position < 0)
                        return;
                    TagMemberListAdapter.ViewHolder holder = (TagMemberListAdapter.ViewHolder) view.getTag();
                    TagMemberBean.Data item = mAdapter.getItem(position);

                    item.cusSelected = !item.cusSelected;
                    holder.chekcbox.setChecked(item.cusSelected);
                    setBottomContainer(item, position);
                }
            });
        }
    }

    private Map<Integer, View> mViewCache = new HashMap<>();

    private void setBottomContainer(TagMemberBean.Data item, int position) {
        if (item.cusSelected) {
            if (mViewCache.get(position) != null) {
                mHeaderLayout.removeView(mViewCache.get(position));
                mViewCache.remove(position);
            }
            RoundImageView1_1W roundImageView1_1W = new RoundImageView1_1W(ContactTagDetailActivity.this);
            int size = Dip2PxUtils.dip2px(ContactTagDetailActivity.this, 45);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(ContactTagDetailActivity.this, 5), 0);
            roundImageView1_1W.setLayoutParams(layoutParams);
            GlideUtil.getInstance().display(roundImageView1_1W, item.picUrl);
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
    public void showDelResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("删除成功");
            mViewCache.clear();
            mHeaderLayout.removeAllViews();
            mTitleCheck.setVisibility(View.INVISIBLE);
            initNet();
        } else
            ToastUtils.showToast("伤处失败，请重试");
    }
}
