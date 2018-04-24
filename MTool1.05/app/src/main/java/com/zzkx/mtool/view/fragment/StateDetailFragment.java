package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.StateDetailBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.chat.ui.ComplainActivity;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.StateCollectPresenter;
import com.zzkx.mtool.presenter.StateDetailPresenter;
import com.zzkx.mtool.presenter.SupportPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ShowAtUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.activity.AllSupportedUserActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.activity.VideoPlayerAcitivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogMoreMenu;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.IStateCollectView;
import com.zzkx.mtool.view.iview.IStateDetailView;
import com.zzkx.mtool.view.iview.ISupportView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.view.View.inflate;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateDetailFragment extends BaseFragment implements IStateDetailView, View.OnClickListener, IStateCollectView, ICancleCollectionView {
    @BindView(R.id.state_layout)
    ViewGroup mStateLayout;
    @BindView(R.id.header_container)
    ViewGroup mHeaderContainer;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    private SupportPresenter mSupportPresenter;
    //    private StateListBean.DataBean item;
    private StateListAdapter.ViewHolder viewHolder;
    private StateDetailPresenter mPresenter;
    private StateDetailBean.DataBean mDetailData;
    private String mId;
    private StateCollectPresenter mStateCollectPresenter;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    private StateDetailActivity mActivity;
    private View.OnClickListener mResClickListener;
    private DialogMoreMenu mDialogMoreMenu;
    private DialogState mDialogState;

    @Override
    public int getContentRes() {
        return R.layout.activity_state_detail;
    }

    @Override
    public void initViews() {
        mPresenter = new StateDetailPresenter(this);
        setTitleDisable();
        mSupportPresenter = new SupportPresenter(new ISupportView() {


            @Override
            public void onSuppotedSuccess(BaseBean bean) {
                if (viewHolder != null) {
                    viewHolder.ic_support2.setImageResource(R.mipmap.ic_good_red);
                    viewHolder.tv_supports2.setText(mDetailData.supports + 1 + "");
                }
            }

            @Override
            public void showProgress(boolean toShow) {
                StateDetailFragment.this.showProgress(toShow);
            }

            @Override
            public void showError(ErrorBean errorBean) {
                StateDetailFragment.this.showError(errorBean);
            }
        });

        mBaseView.findViewById(R.id.iv_dot).setOnClickListener(this);
        mId = getActivity().getIntent().getStringExtra(Const.ID);
        mStateCollectPresenter = new StateCollectPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        mActivity = (StateDetailActivity) getActivity();
        mDialogState = new DialogState(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatShareBean shareBean = new ChatShareBean();
                ArrayList<StateListBean.ResData> forumThreadResources = mDetailData.forumThreadResources;
                if (mDetailData.shareType == 1) {
                    shareBean.id = mDetailData.firstId;
                } else {
                    shareBean.id = mDetailData.id;
                }
                shareBean.title = TextUtils.isEmpty(mDetailData.content) ? "来自MTool的分享" : mDetailData.content;
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
                    mDialogState.dismiss();
                } else {
                    mDialogState.onShare(v.getId(), shareBean);
                    mDialogState.dismiss();
                }
            }
        });
        mDialogState.hideActionMenu();
    }

    private void initItem() {
        mResClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childIndex = (int) v.getTag(R.id.child_index);
                ArrayList<StateListBean.ResData> forumThreadResources = mDetailData.forumThreadResources;
                StateListBean.ResData resData = forumThreadResources.get(childIndex);
                if (resData.type == 1) {
                    startActivity(new Intent(getActivity(), VideoPlayerAcitivity.class)
                            .putExtra(Const.URL, resData.resourceUrl)
                            .putExtra(Const.COVER_URL, resData.coverUrl)
                    );
                } else {
                    startActivity(new Intent(getActivity(), StateImgActivity.class)
                            .putExtra(Const.RES, forumThreadResources)
                            .putExtra(Const.INDEX, childIndex)
                    );
                }
            }
        };
        final int type = getType(mDetailData.forumThreadResources);
        GlideUtil glideInstance = GlideUtil.getInstance();
        View child = null;
        switch (type) {
            case 0:
                child = inflate(mActivity, R.layout.item_state_txt, null);
                break;
            case 1:
                child = inflate(mActivity, R.layout.item_state_single_image, null);
                break;
            case 2:
                child = inflate(mActivity, R.layout.item_state_multi_image_1, null);
                break;
            case 3:
                child = inflate(mActivity, R.layout.item_state_multi_image_2, null);
                break;
            case 4:
                child = inflate(mActivity, R.layout.item_state_multi_image_3, null);
                break;
            case 5:
                child = inflate(mActivity, R.layout.item_state_video, null);
                break;
        }
        mStateLayout.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
        mStateLayout.findViewById(R.id.layout_support).setVisibility(View.VISIBLE);
        View shareType = inflate(mActivity, R.layout.item_state_txt_share, null);
        shareType.setVisibility(View.GONE);
        mStateLayout.addView(shareType, 1);
        mStateLayout.addView(child, 2);

        viewHolder = new StateListAdapter.ViewHolder(mStateLayout);
        if (mDetailData.shareType == 1) {
            viewHolder.layout_share_type.setVisibility(View.VISIBLE);
            viewHolder.tv_name_orgin.setText("引用/原著：" + mDetailData.firstName + "/");
            viewHolder.tv_show_orgin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, StateDetailActivity.class)
                            .putExtra(Const.ID, mDetailData.firstId)
                            .putExtra(Const.TYPE, type)
                    );
                }
            });
            ShowAtUtil.handleAtUsers(viewHolder.tv_share_content, mDetailData.share, mDetailData.shareuserMemberList);
            ShowAtUtil.handleAtUsers(viewHolder.tv_content, mDetailData.content, mDetailData.userMemberList);
        } else {
            ShowAtUtil.handleAtUsers(viewHolder.tv_content, mDetailData.content, mDetailData.userMemberList);
            viewHolder.layout_share_type.setVisibility(View.GONE);
        }
        viewHolder.tv_supports2.setText(mDetailData.supports + "");
        viewHolder.ic_more.setVisibility(View.GONE);
        if (viewHolder.image_container != null) {
            viewHolder.image_container.setGridMode();
            viewHolder.image_container.setHorizontalSpacing(16);
            viewHolder.image_container.setVerticalSpacing(16);
        }
        if (mDetailData.suppoppType == 1) {
            viewHolder.ic_support2.setImageResource(R.mipmap.ic_good_red);
        } else {
            viewHolder.ic_support2.setOnClickListener(this);
        }
        final StateDetailBean.DataBean.UserMemberBean userMember = mDetailData.userMember;
        if (userMember != null) {
            glideInstance.display(viewHolder.iv_user_header, userMember.picUrl);
            viewHolder.tv_user_name.setText(userMember.nickname);
            viewHolder.iv_user_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HeadClickUtil.handleClick(getContext(), userMember.id, null);
                }
            });
        }

        viewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(mDetailData.createTime)));
        switch (type) {
            case 0:
                break;
            case 1:
                viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, mDetailData.forumThreadResources.get(0).resourceUrl);
                break;
            case 5:
                viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, mDetailData.forumThreadResources.get(0).coverUrl);
                break;
            default:
                for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
                    ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
                    if (i < mDetailData.forumThreadResources.size()) {
                        StateListBean.ResData resData = mDetailData.forumThreadResources.get(i);
                        imageview.setTag(R.id.child_index, i);
                        imageview.setOnClickListener(mResClickListener);
                        glideInstance.display(imageview, resData.resourceUrl);
                    } else {
                        imageview.destroyDrawingCache();
                    }
                }
        }
    }

    private int getType(ArrayList<StateListBean.ResData> resources) {
        if (resources != null) {
            if (resources.size() == 0) {
                return 0;
            } else if (resources.size() == 1) {
                StateListBean.ResData resData = resources.get(0);
                if (resData.type == 0)
                    return 1;
                else
                    return 5;//视频
            } else if (resources.size() > 1 && resources.size() <= 3) {
                return 2;
            } else if (resources.size() > 3 && resources.size() <= 6) {
                return 3;
            } else if (resources.size() > 6) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void initNet() {
        mPresenter.getDetailInfo(mId);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_support2:
                mSupportPresenter.support(mId, 0);
                break;
            case R.id.iv_dot:
                if (mDetailData != null) {
                    startActivity(new Intent(getContext(), AllSupportedUserActivity.class).putExtra(Const.OBJ, mDetailData)
                    );
                }
                break;
        }
    }

    @Override
    public void showDate(StateDetailBean bean) {
        StateDetailBean.DataBean data = bean.data;
        if (data != null) {
            mDetailData = data;
            setSecMenu();

            List<StateDetailBean.DataBean.UserMemberDosBean> userMemberDos = mDetailData.userMemberDos;
            GlideUtil instance = GlideUtil.getInstance();
            for (int i = 0; i < userMemberDos.size(); i++) {
                if (i < mHeaderContainer.getChildCount()) {
                    StateDetailBean.DataBean.UserMemberDosBean userMemberDosBean = userMemberDos.get(i);
                    ImageView child = (ImageView) mHeaderContainer.getChildAt(i);
                    instance.display(child, userMemberDosBean.picUrl);
                }
            }
            initItem();
        }
    }

    private void setSecMenu() {
        int[] imageRes;
        String[] title;
        final String uid = SPUtil.getString(Const.U_ID, "");
        if (mDetailData.memId.equals(uid)) {
            imageRes = new int[]{R.mipmap.ic_backto_top, R.mipmap.ic_share,};
            title = new String[]{"返回顶部", "分        享"};
        } else {
            imageRes = new int[]{R.mipmap.ic_backto_top
                    , R.mipmap.ic_star_gray_empty, R.mipmap.ic_share, R.mipmap.ic_1, R.mipmap.ic_4};
            title = new String[]{"返回顶部", "收        藏", "分        享", "不再看", "举        报"};
        }
        mActivity.setSecMenu(imageRes, title
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        switch (position) {
                            case 0:
                                mScrollView.scrollTo(0, 0);
                                break;
                            case 1:
                                if (mDetailData == null)
                                    return;

                                if (mDetailData.memId.equals(uid)) {
                                    share();
                                } else {
                                    if (mDetailData.collectType == 1) {
                                        mCancleCollectionPresenter.cancleCollection(mDetailData.id, 2);
                                    } else {
                                        mStateCollectPresenter.collectState(mDetailData.id, 0);
                                    }
                                }
                                break;
                            case 2://分享
                                share();
                                break;
                            case 3://不再看

                                break;
                            case 4://举报
                                startActivity(new Intent(mActivity, ComplainActivity.class)
                                        .putExtra(Const.TYPE, 0)
                                        .putExtra(Const.ID, mDetailData.id)
                                );
                                break;
                        }
                        mActivity.secMenuDismiss();
                    }
                });
        if (!mDetailData.memId.equals(uid))
            mActivity.setCollected(mDetailData.collectType);
    }

    private void share() {
        mDialogState.show("", 0);
    }

    @Override
    public void showCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            mActivity.setCollected(1);
            mDetailData.collectType = 1;
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showCancleCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            mActivity.setCollected(0);
            mDetailData.collectType = 0;
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }
}
