package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.chat.ui.ComplainActivity;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.StateCollectPresenter;
import com.zzkx.mtool.presenter.StateDeletePresenter;
import com.zzkx.mtool.presenter.StateListPresenter;
import com.zzkx.mtool.presenter.SupportCanclePresenter;
import com.zzkx.mtool.presenter.SupportPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.activity.PublishStateActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.activity.VideoPlayerAcitivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogSort;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.DialogTagSelect;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.IStateCollectView;
import com.zzkx.mtool.view.iview.IStateList;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/21.
 */

public class StateFragment extends BaseListFragment<StateListBean.DataBean>
        implements View.OnClickListener, IStateList, IStateCollectView,
        ICancleCollectionView, ISupportView, ISupportCancleView {

    @BindView(R.id.ic_eye)
    View mSortView;
    @BindView(R.id.layout_input)
    View mLayoutInput;
    @BindView(R.id.right_container)
    View mRightContainer;

    private DialogSort mDialogSort;
    private SupportPresenter mSupportPresenter;
    private StateListAdapter mStateListAdapter;
    private DialogState mDialogStateMore;
    private View mBottomEdit;
    private StateCollectPresenter mStateCollectPresenter;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    public int mClickPosition;
    private SupportCanclePresenter mSupportCanclePresenter;
    private StateListPresenter mStateListPresenter;
    private DialogTagSelect mDialogTagSelec;

    @Override
    public int getContentRes() {
        return R.layout.fragment_state_index;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        mSortView.setOnClickListener(this);
        mDialogSort = new DialogSort(getActivity(), new int[]{R.mipmap.ic_rect_check_red, R.mipmap.ic_rect_check_red, R.mipmap.ic_rect_check_red}
                , new int[]{R.mipmap.ic_rect_check_gray, R.mipmap.ic_rect_check_gray, R.mipmap.ic_rect_check_gray}
                , new String[]{"全  部", "好  友", "自定义分类"}
        );
        mDialogSort.setOnSortListener(new DialogSort.OnSortListener() {
            @Override
            public boolean onSort(DialogSort.ViewHolder sortKey) {
                switch (sortKey.position) {
                    case 0:
                        mStateListPresenter.setFollow(1);
                        onRefresh();
                        break;
                    case 1:
                        mStateListPresenter.setFollow(0);
                        onRefresh();
                        break;
                    case 2:
                        mDialogTagSelec.show(sortKey);
                        return false;
                }
                return true;
            }
        });
        mDialogTagSelec = new DialogTagSelect(getActivity());
        mDialogTagSelec.setOnConfirmListener(new DialogTagSelect.OnConfirmListener() {
            @Override
            public void onConfirm(String tagId) {
                if (tagId != null) {
                    mDialogSort.handleSelect(mDialogTagSelec.getHolder());
                    mDialogSort.dismiss();
                    mStateListPresenter.setFollow(2, tagId);
                    onRefresh();
                } else {
                    mDialogSort.dismiss();
                }
            }
        });

        mBottomEdit = mBaseView.findViewById(R.id.iv_bottom_edit);
        mBottomEdit.setOnClickListener(this);

        HeaderUtil.addHeader(getContext(), mListView, 10);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getContext(), 10));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= mListView.getHeaderViewsCount();
                if (position > -1) {
                    StateListBean.DataBean dataBean = mTotalData.get(position);
                    int itemViewType = mStateListAdapter.getItemViewType(position);
                    Intent intent = new Intent(getActivity(), StateDetailActivity.class);
                    intent.putExtra(Const.TYPE, itemViewType);
                    intent.putExtra(Const.ID, dataBean.id);
                    startActivity(intent);
                }
            }
        });


        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(Const.IS_MY_SUPPOSED)) {
                mRightContainer.setVisibility(View.INVISIBLE);
                mBottomEdit.setVisibility(View.INVISIBLE);
            }
        }
        mSupportPresenter = new SupportPresenter(this);
        mStateCollectPresenter = new StateCollectPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        mSupportCanclePresenter = new SupportCanclePresenter(this);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mSwipLayout.setRefreshing(true);
        mDialogTagSelec.reset();
    }

    @Override
    public BaseAdapter getAdapter() {
        mStateListAdapter = new StateListAdapter(getActivity(), getTotalData(), new View.OnClickListener() {
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
                                .putExtra(Const.ID, item.id)
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
                            mDialogStateMore = new DialogState(getContext(), new View.OnClickListener() {
                                private StateDeletePresenter mStateDeletePresenter;

                                @Override
                                public void onClick(View v) {
                                    StateListBean.DataBean dataBean = mTotalData.get(mClickPosition);
                                    switch (v.getId()) {
                                        case R.id.delete:
                                            if (mStateDeletePresenter == null)
                                                mStateDeletePresenter = new StateDeletePresenter(StateFragment.this);
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
                                        case R.id.jubao:
                                            startActivity(new Intent(getActivity(), ComplainActivity.class).putExtra(Const.ID,dataBean.id));
                                            mDialogStateMore.dismiss();
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
    public void showError(ErrorBean errorBean) {
        if (isAdded()) {
            mSwipLayout.setRefreshing(false);
            ToastUtils.showToast(getString(R.string.netErroRetry));
            if (API.STATE_LIST.equals(errorBean.url)) {
                super.showError(errorBean);
            }
        }
    }

    @Override
    public BaseListPresenter getPresenter() {
        mStateListPresenter = new StateListPresenter(this);
        return mStateListPresenter;
    }


    @Override
    public void initNet() {
        onRefresh();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_eye:
                mDialogSort.toggleRightFilter(mSortView);
                break;
            case R.id.iv_bottom_edit:
                startActivityForResult(new Intent(getContext(), PublishStateActivity.class), 9);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            initNet();
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
    public void showCancleSupportResult(BaseBean bean) {
        if (bean.status == 1) {
            if (bean.cusTag == null) {
                initNet();
                return;
            }
            int cusTag = (int) bean.cusTag;
            StateListBean.DataBean dataBean = mTotalData.get(cusTag);
            dataBean.supports--;
            dataBean.suppoppType = 0;
            mStateListAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }
}
