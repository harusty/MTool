package com.zzkx.mtool.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopComentReplyBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.ShopCommentReplyListJingXuanPresenter;
import com.zzkx.mtool.presenter.ShopCommentReplyListPresenter;
import com.zzkx.mtool.presenter.ShopCommentReplySendPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.CommentAdapter;
import com.zzkx.mtool.view.adapter.ShopCommentReplyListAdapter;
import com.zzkx.mtool.view.iview.ISendCommentView;
import com.zzkx.mtool.view.iview.ShopCommentReplyView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/29.
 */

public class ShopCommentReplyFragment extends BaseListFragment<ShopComentReplyBean.DataBean> implements
        View.OnClickListener, ISendCommentView, SwipeRefreshLayout.OnRefreshListener, ShopCommentReplyView {

    @BindView(R.id.layout_input)
    View mLayoutInput;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.iv_bottom_edit)
    View mBottom;
    private CommentAdapter mAdapter;
    private ShopCommentReplySendPresenter mSendPresenter;
    private boolean mReplyFlag;
    private String mCommentHeader;
    private String mId;

    @Override
    public BaseAdapter getAdapter() {
        return new ShopCommentReplyListAdapter(getActivity(), getTotalData(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                int position;
                if (tag instanceof Integer) {
                    position = (int) tag;
                } else {
                    position = (int) v.getTag(R.id.child_index);
                }
                ShopComentReplyBean.DataBean dataBean = getTotalData().get(position);
                switch (v.getId()) {
                    case R.id.ic_msg2:
                        mCommentHeader = "回复@" + dataBean.memNickname + "：";
                        mEtComment.setText(mCommentHeader);
                        mEtComment.setSelection(mCommentHeader.length());
                        RequestBean requestBean = new RequestBean();
                        requestBean.commentId = mId;
                        requestBean.parentId = dataBean.memId;
                        mEtComment.setTag(requestBean);
                        showInput();
                        break;
                    case R.id.iv_user_header:
                        HeadClickUtil.handleClick(getActivity(), dataBean.memId, null);
                        break;
                }
            }
        });
    }

    @Override
    public BaseListPresenter getPresenter() {
        return new ShopCommentReplyListPresenter(this, mId);
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_comment_list;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        new ShopCommentReplyListJingXuanPresenter(this);
        mId = getArguments().getString(Const.ID);
        mBottom.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        HeaderUtil.addHeader(getActivity(), mListView, 0);
        mBaseView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (oldBottom != 0 && bottom != 0) {
                    if (oldBottom - bottom > 0) {
//                        ToastUtils.showToast("监听到软键盘弹起...");
                        mBottom.setVisibility(View.INVISIBLE);
                    } else if (bottom - oldBottom > 0) {
                        mListView.requestLayout();
                        mBottom.setVisibility(View.VISIBLE);
                        mLayoutInput.setVisibility(View.GONE);
                    }
                }
            }
        });
        View view = new View(getActivity());
        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dip2PxUtils.dip2px(getActivity(), 10)));
        mReplyFlag = getArguments().getBoolean(Const.TO_REPLY);
    }

    @Override
    public void initNet() {
        getPresenter().getListData(1);
    }

    @Override
    public void onReload() {
        showProgress(false);
        initNet();
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
                mEtComment.setText("");
                mCommentHeader = null;
                RequestBean requestBean1 = new RequestBean();
                requestBean1.commentId = mId;
                mEtComment.setTag(requestBean1);
                showInput();
                break;
            case R.id.tv_send:
                RequestBean requestBean = (RequestBean) mEtComment.getTag();
                String s = mEtComment.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("评论内容不能为空");
                    return;
                }
                if (mCommentHeader != null && s.contains(mCommentHeader) && s.startsWith(mCommentHeader)) {
                    s = s.substring(mCommentHeader.length(), s.length());
                }
                if (mSendPresenter == null) {
                    mSendPresenter = new ShopCommentReplySendPresenter(this);
                }
                requestBean.content = s;
                mSendPresenter.send(requestBean);
                mEtComment.setTag(null);
                break;
        }
    }

    private void showInput() {
        mBottom.setVisibility(View.GONE);
        mLayoutInput.setVisibility(View.VISIBLE);
        InputUtils.showInput(getActivity(), mEtComment);
    }

    @Override
    public void showJingXuan(ShopComentReplyBean bean) {

    }
}
