package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.bean.UserDetailBean;
import com.zzkx.mtool.chat.ui.ChatActivity;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddAttenionPresenter;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.DeleteAttenionPresenter;
import com.zzkx.mtool.presenter.StateCollectPresenter;
import com.zzkx.mtool.presenter.SupportCanclePresenter;
import com.zzkx.mtool.presenter.SupportPresenter;
import com.zzkx.mtool.presenter.UserDetailPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.activity.TagsActivity;
import com.zzkx.mtool.view.activity.UserSettingActvity;
import com.zzkx.mtool.view.activity.VideoPlayerAcitivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.iview.IAddAttentionView;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.IStateCollectView;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;
import com.zzkx.mtool.view.iview.IUserDetailView;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/9/22.
 */

public class UserDetailFragment extends BaseListFragment<StateListBean.DataBean> implements IUserDetailView, View.OnClickListener, ISupportView,
        ICancleCollectionView, ISupportCancleView, IStateCollectView, IAddAttentionView {

    private UserDetailPresenter mPresenter;
    private StateListAdapter mStateListAdapter;
    private int mClickPosition;
    private SupportPresenter mSupportPresenter;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    private StateCollectPresenter mStateCollectPresenter;
    private SupportCanclePresenter mSupportCanclePresenter;
    private DialogState mDialogStateMore;
    private View mHeader;
    private String mUserId;
    private String mHxUsername;
    private View mLayoutChat;
    private UserDetailBean.DataBean.UserMemberDoBean userMemberDo;

    @Override
    public BaseAdapter getAdapter() {
        mStateListAdapter = new StateListAdapter(getActivity(), mTotalData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag instanceof Integer) {
                    mClickPosition = (int) tag;
                } else {
                    mClickPosition = (int) v.getTag(R.id.child_index);
                }
                StateListBean.DataBean item = mStateListAdapter.getItem(mClickPosition);

                switch (v.getId()) {
                    case R.id.iv_user_header:
                        HeadClickUtil.handleClick(getContext(), item.memId, null);
                        break;
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
                        if (mDialogStateMore == null)
                            mDialogStateMore = new DialogState(getActivity(), new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    StateListBean.DataBean dataBean = mTotalData.get(mClickPosition);
                                    switch (v.getId()) {
                                        case R.id.collect:
                                            boolean isCollected = (boolean) v.getTag();
                                            if (isCollected) {
                                                mCancleCollectionPresenter.cancleCollection(dataBean.id, 2, mClickPosition);
                                            } else {
                                                mStateCollectPresenter.collectState(dataBean.id, mClickPosition);
                                            }
                                            break;
                                        default:
                                            ChatShareBean shareBean = new ChatShareBean();
                                            ArrayList<StateListBean.ResData> forumThreadResources = dataBean.forumThreadResources;
                                            if (dataBean.shareType == 1) {
                                                shareBean.id = dataBean.firstId;
                                            } else {
                                                shareBean.id = dataBean.id;
                                            }
                                            shareBean.title = TextUtils.isEmpty(dataBean.content) ? "来自MTool的分享" : dataBean.content;
                                            shareBean.type = 1;
                                            if (forumThreadResources != null) {
                                                if (forumThreadResources.size() == 1) {
                                                    StateListBean.ResData resData = forumThreadResources.get(0);
                                                    if (resData.type == 0)
                                                        shareBean.picUrl = resData.resourceUrl;
                                                } else if (forumThreadResources.size() > 1) {
                                                    StateListBean.ResData resData = forumThreadResources.get(0);
                                                    shareBean.picUrl = resData.resourceUrl;
                                                }
                                            }
                                            if (v.getId() == R.id.layout_share_mtool_friend) {
                                                startActivity(new Intent(getActivity(), MToolShareActivity.class)
                                                        .putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean)));
                                                mDialogStateMore.dismiss();
                                            } else {
                                                mDialogStateMore.onShare(v.getId(), shareBean);
                                                mDialogStateMore.dismiss();
                                            }
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
        mStateListAdapter.setOnResClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int parentIndex = (int) v.getTag(R.id.parent_index);
                int childIndex = (int) v.getTag(R.id.child_index);
                StateListBean.DataBean item = mStateListAdapter.getItem(parentIndex);
                StateListBean.ResData resData = item.forumThreadResources.get(childIndex);
                if (resData.type == 1) {
                    startActivity(new Intent(getActivity(), VideoPlayerAcitivity.class)
                            .putExtra(Const.URL, resData.resourceUrl)
                            .putExtra(Const.COVER_URL, resData.coverUrl)
                    );
                } else {
                    startActivity(new Intent(getActivity(), StateImgActivity.class)
                            .putExtra(Const.RES, item.forumThreadResources)
                            .putExtra(Const.INDEX, childIndex)
                    );
                }
            }
        });
        return mStateListAdapter;
    }

    @Override
    public BaseListPresenter getPresenter() {
        String userId = getActivity().getIntent().getStringExtra(Const.ID);
        String hxUserName = getActivity().getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        if (userId != null)
            mPresenter = new UserDetailPresenter(this, userId, null);
        else if (hxUserName != null)
            mPresenter = new UserDetailPresenter(this, null, hxUserName);
        else
            throw new IllegalStateException("id null!~!!!!!!!!!!!!!!!!!!!!!!!");
        return mPresenter;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        mHeader = View.inflate(getContext(), R.layout.header_user_detaill, null);
        mLayoutChat = mHeader.findViewById(R.id.layout_send_chat);
        mLayoutChat.setOnClickListener(this);
        mHeader.findViewById(R.id.layout_set_tag).setOnClickListener(this);
        mHeader.findViewById(R.id.layout_user_setting).setOnClickListener(this);
        mListView.addHeaderView(mHeader);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getActivity(), 10));
        mListView.setHeaderDividersEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                position -= mListView.getHeaderViewsCount();
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
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        mStateCollectPresenter = new StateCollectPresenter(this);
        mSupportCanclePresenter = new SupportCanclePresenter(this);
        mSupportPresenter = new SupportPresenter(this);
//        mDialogStateMore = new DialogState(getContext(), new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                StateListBean.DataBean dataBean = mTotalData.get(mClickPosition);
//                switch (v.getId()) {
//                    case R.id.collect:
//                        boolean isCollected = (boolean) v.getTag();
//                        if (isCollected) {
//                            mCancleCollectionPresenter.cancleCollection(dataBean.id, 2, mClickPosition);
//                        } else {
//                            mStateCollectPresenter.collectState(dataBean.id, mClickPosition);
//                        }
//                        break;
//                }
//
//            }
//        });
    }

    @Override
    public void initNet() {
        mPresenter.getListData(1);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showProgress(boolean toShow) {
        super.showProgress(toShow);
        mSwipLayout.setRefreshing(toShow);
    }

    @Override
    public void showError(ErrorBean errorBean) {
        super.showError(errorBean);
    }

    @Override
    public void showEmpty() {
        mListView.setFooterGone();
        mListView.setAdapter(getAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_send_chat:
                if (mHxUsername != null)
                    startActivity(new Intent(getContext(), ChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, mHxUsername));
                break;
            case R.id.layout_user_setting:
                if (mUserId != null) {
                    startActivity(new Intent(getContext(), UserSettingActvity.class)
                            .putExtra(Const.ID, mUserId)
                            .putExtra(EaseConstant.EXTRA_USER_ID, mHxUsername)
                    );
                }
                break;
            case R.id.layout_set_tag:
                startActivity(new Intent(getActivity(), TagsActivity.class)
                        .putExtra(Const.ACTION, TagsActivity.ACTION_SELECT)
                        .putExtra(Const.ID, mUserId)
                );
                break;
        }
    }

    @Override
    public void showData(UserDetailBean bean) {

        UserDetailBean.DataBean data = bean.data;
        if (data != null) {
            userMemberDo = data.userMemberDo;
            if (data.sayType == 0)
                mLayoutChat.setVisibility(View.GONE);

            if (userMemberDo != null) {
                mUserId = userMemberDo.id;
                mHxUsername = userMemberDo.hxUsername;
                GlideUtil.getInstance().display(((ImageView) mHeader.findViewById(R.id.iv_header)), userMemberDo.picUrl);
                ((TextView) mHeader.findViewById(R.id.tv_section)).setText(userMemberDo.nickname);
                ((TextView) mHeader.findViewById(R.id.tv_age)).setText(userMemberDo.age);
                ImageView sex = (ImageView) mHeader.findViewById(R.id.iv_sex);
                if (userMemberDo.sex == 1) {
                    sex.setImageResource(R.mipmap.ic_man);
                } else {
                    sex.setImageResource(R.mipmap.ic_women);
                }
                ((TextView) mHeader.findViewById(R.id.tv_info)).setText("关注：" + userMemberDo.idolcount + "/粉丝：" + userMemberDo.fanscount);
                ((TextView) mHeader.findViewById(R.id.tv_intro)).setText(userMemberDo.introduction);
            }
            setAttention(data.attentionFlag);

        }
    }

    public void setAttention(int attentionFlag) {
        if (attentionFlag == 1) {
            ((TextView) mHeader.findViewById(R.id.tv_attention)).setText("已关注");
            ((ImageView) mHeader.findViewById(R.id.iv_attention)).setImageResource(R.mipmap.ic_heart);
            mHeader.findViewById(R.id.layout_attention).setOnClickListener(getDeleteAttentionListener());
        } else {
            ((TextView) mHeader.findViewById(R.id.tv_attention)).setText("未关注");
            ((ImageView) mHeader.findViewById(R.id.iv_attention)).setImageResource(R.mipmap.ic_heart_gray);
            mHeader.findViewById(R.id.layout_attention).setOnClickListener(getAddAttentionListener());
        }
    }

    private AddAttenionPresenter mAddAttenionPresenter;
    private DeleteAttenionPresenter mDelAttenionPresenter;

    private View.OnClickListener getDeleteAttentionListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDelAttenionPresenter == null)
                    mDelAttenionPresenter = new DeleteAttenionPresenter(UserDetailFragment.this);
                mDelAttenionPresenter.del(mUserId);
            }
        };
    }

    private View.OnClickListener getAddAttentionListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAddAttenionPresenter == null)
                    mAddAttenionPresenter = new AddAttenionPresenter(UserDetailFragment.this);
                mAddAttenionPresenter.add(mUserId);
            }
        };
    }

    @Override
    public void onSuppotedSuccess(BaseBean bean) {
        showProgress(false);
        if (bean.status == 1) {
            String cusTag = (String) bean.cusTag;
            if (!TextUtils.isEmpty(cusTag)) {

                StateListBean.DataBean dataBean = mTotalData.get(Integer.parseInt(cusTag));
                dataBean.supports++;
                dataBean.suppoppType = 1;
                mStateListAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCancleCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            int tag = (int) bean.cusTag;
            mStateListAdapter.getItem(tag).collectType = 0;
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
            StateListBean.DataBean dataBean = mTotalData.get(cusTag);
            dataBean.supports--;
            dataBean.suppoppType = 0;
            mStateListAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("收藏成功");
            int tag = (int) bean.cusTag;
            mStateListAdapter.getItem(tag).collectType = 1;
            mDialogStateMore.setCollected(1);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showAttentionAddSuccess(BaseBean bean) {
        if (bean.status == 1) {
            setAttention(1);

            EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
            EMCmdMessageBody cmdBody = new EMCmdMessageBody(EaseConstant.ACTION_ATTENTION);
            cmdMsg.setAttribute(EaseConstant.USER_NICK_FROM, SPUtil.getString(Const.USER_NICK, ""));
            String toUsername = mHxUsername;
            cmdMsg.setTo(toUsername);
            cmdMsg.addBody(cmdBody);
            EMClient.getInstance().chatManager().sendMessage(cmdMsg);
        }
    }

    @Override
    public void showDelAttentionFaild(BaseBean bean) {
        if (bean.status == 1) {
            setAttention(0);
        }
    }
}
