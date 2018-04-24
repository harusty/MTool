package com.zzkx.mtool.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CommentListBean;
import com.zzkx.mtool.bean.EaseUserListBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CommentSendPresenter;
import com.zzkx.mtool.presenter.StateCommentListPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.FriendListActivity;
import com.zzkx.mtool.view.adapter.CommentAdapter;
import com.zzkx.mtool.view.customview.LoadMoreListView;
import com.zzkx.mtool.view.iview.ICommentListView;
import com.zzkx.mtool.view.iview.ISendCommentView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/29.
 */

public class StateCommentFragment extends BaseFragment implements ICommentListView,
        View.OnClickListener, ISendCommentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.lv_list)
    LoadMoreListView mListView;
    @BindView(R.id.layout_input)
    View mLayoutInput;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.iv_bottom_edit)
    View mBottom;
    @BindView(R.id.iv_async)
    ImageView iv_async;
    @BindView(R.id.iv_at)
    ImageView iv_at;

    private StateCommentListPresenter mPreseter;
    private CommentAdapter mAdapter;
    private CommentSendPresenter mSendPresenter;
    private boolean mReplyFlag;
    private String mCommentHeader;
    private String mId;
    private List<EaseUser> mAtList;
    private View.OnLayoutChangeListener mListener;

    @Override
    public int getContentRes() {
        return R.layout.fragment_comment_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setFooterGone();
        Bundle arguments = getArguments();
        mId = arguments.getString(Const.ID);
        mPreseter = new StateCommentListPresenter(this);
        mBottom.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        mListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (oldBottom != 0 && bottom != 0) {
                    if (oldBottom - bottom > 0) {
                        System.out.println("监听到软键盘弹起...");
                        mBottom.setVisibility(View.INVISIBLE);
                        mLayoutInput.setVisibility(View.VISIBLE);
                    } else if (bottom - oldBottom > 0) {
                        mListView.requestLayout();
                        mBottom.setVisibility(View.VISIBLE);
                        mLayoutInput.setVisibility(View.GONE);
                        System.out.println("键盘收起...");
                    }
                }
            }
        };
        mBaseView.addOnLayoutChangeListener(mListener);
        View view = new View(getActivity());
        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dip2PxUtils.dip2px(getActivity(), 10)));
        mListView.addHeaderView(view);
        mListView.setHeaderDividersEnabled(false);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getContext(), 10));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position -= mListView.getHeaderViewsCount();
            }
        });
        mReplyFlag = arguments.getBoolean(Const.TO_REPLY);
        iv_async.setOnClickListener(this);
        iv_async.setTag(0);
        iv_at.setOnClickListener(this);
    }


    @Override
    public void initNet() {
        mPreseter.getCommentData(mId);
    }

    @Override
    public void onReload() {
        showProgress(false);
        initNet();
    }


    @Override
    public void showProgress(boolean toShow) {
        if (toShow && mRefreshLayout.isShown())
            return;
        mRefreshLayout.setRefreshing(toShow);
        if (mReplyFlag) {
            mReplyFlag = false;
            mCommentHeader = null;
            showInput();
        }
    }

    @Override
    public void showSendSuccess(RequestBean lastRequest) {
        InputUtils.hideInput(getActivity(), mEtComment);
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bottom_edit:
                iv_async.setVisibility(View.VISIBLE);
                mEtComment.setText("");
                mCommentHeader = null;
                RequestBean requestBean1 = new RequestBean();
                requestBean1.postId = mId;
                mEtComment.setTag(requestBean1);
                showInput();
                break;
            case R.id.tv_send:
                RequestBean requestBean = (RequestBean) mEtComment.getTag();
                if(requestBean == null) {
                    requestBean = new RequestBean();
                    requestBean.postId = mId;
                }
                String s = mEtComment.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("评论内容不能为空");
                    return;
                }
                if (mCommentHeader != null && s.contains(mCommentHeader) && s.startsWith(mCommentHeader)) {
                    s = s.substring(mCommentHeader.length(), s.length());
                }
                if (mSendPresenter == null) {
                    mSendPresenter = new CommentSendPresenter(this);
                }
                if (mAtList != null) {
                    String atIds = "";
                    for (EaseUser user : mAtList) {
                        atIds += user.getMtoolId() + ",";
                    }
                    if (atIds.endsWith(",")) {
                        atIds = atIds.substring(0, atIds.length() - 1);
                    }
                    requestBean.remindId = atIds;
                }
                requestBean.shareType = (int) iv_async.getTag();
                requestBean.content = s;
                mSendPresenter.send(requestBean);
                mEtComment.setTag(null);
                break;
            case R.id.iv_async:
                if (((int) iv_async.getTag()) == 1) {
                    iv_async.setTag(0);
                    iv_async.setImageResource(R.mipmap.ic_circle_share);
                } else {
                    iv_async.setTag(1);
                    iv_async.setImageResource(R.mipmap.ic_62);
                }
                break;
            case R.id.iv_at:
                InputUtils.hideInput(getActivity(), mEtComment);
                startActivityForResult(new Intent(getActivity(), FriendListActivity.class)
                        .putExtra(Const.ACTION, FriendListActivity.ACTION_MULTI_SELECT), 99);
                break;
        }
    }

    private boolean haveAtList;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getStringExtra("data") != null) {
                haveAtList = true;
                EaseUserListBean data1 = Json_U.fromJson(data.getStringExtra("data"), EaseUserListBean.class);
                mAtList = data1.data;
                String atUserNick = " ";
                for (EaseUser user : mAtList) {
                    atUserNick += "@" + user.getNickname() + " ";
                }
                mEtComment.setText(mEtComment.getText().toString() + atUserNick);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInput();
                    }
                }, 300);

            }
        }
    }


    private void showInput() {
//        mBottom.setVisibility(View.GONE);
//        mLayoutInput.setVisibility(View.VISIBLE);
        InputUtils.showInput(getActivity(), mEtComment);
    }

    @Override
    public void showData(List<Object> data) {
        if (data != null && data.size() > 0) {
            mAdapter = new CommentAdapter(data, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    int position;
                    if (tag instanceof Integer) {
                        position = (int) tag;
                    } else {
                        position = (int) v.getTag(R.id.child_index);
                    }
                    CommentListBean.DataBean item = (CommentListBean.DataBean) mAdapter.getItem(position);
                    CommentListBean.DataBean.UserMemberBean userMember = item.userMember;
                    switch (v.getId()) {
                        case R.id.ic_msg2:
                            if (userMember != null) {
                                mCommentHeader = "回复@" + userMember.nickname + "：";
                                mEtComment.setText(mCommentHeader);
                                mEtComment.setSelection(mCommentHeader.length());
                                RequestBean requestBean = new RequestBean();
                                requestBean.postId = mId;
                                requestBean.parentId = userMember.id;
                                requestBean.fatherId = item.fatherId == null ? item.id : item.fatherId;
                                mEtComment.setTag(requestBean);
                                iv_async.setVisibility(View.INVISIBLE);
                                showInput();
                            }
                            break;
                        case R.id.iv_user_header:
                            if (userMember != null) {
                                HeadClickUtil.handleClick(getActivity(), userMember.id, null);
                            }
                            break;
                    }
                }
            });
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }
}
