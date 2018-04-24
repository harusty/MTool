package com.zzkx.mtool.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.ShopCommentDetailBean;
import com.zzkx.mtool.bean.StateDetailBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ShopCommentDetailPresenter;
import com.zzkx.mtool.presenter.ShopCommentSupportCanclePresenter;
import com.zzkx.mtool.presenter.ShopCommentSupportPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.view.activity.AllSupportedUserActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.iview.IShopCommentDetailView;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/26.
 */

public class ShopCommentDetailFragment extends BaseFragment
        implements View.OnClickListener, IShopCommentDetailView, ISupportCancleView {
    @BindView(R.id.state_layout)
    ViewGroup mStateLayout;
    @BindView(R.id.header_container)
    ViewGroup mHeaderContainer;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    private ShopCommentSupportPresenter mSupportPresenter;
    //    private StateListBean.DataBean item;
    private StateListAdapter.ViewHolder viewHolder;
    private ShopCommentDetailPresenter mPresenter;
    private ShopCommentDetailBean.DataBean mDetailData;
    private String mId;
    private Activity mActivity;
    private View.OnClickListener mResClickListener;
    private ShopCommentSupportCanclePresenter mShopCommentSupportCanclePresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_state_detail;
    }


    @Override
    public void initViews() {
        mPresenter = new ShopCommentDetailPresenter(this);
        mShopCommentSupportCanclePresenter = new ShopCommentSupportCanclePresenter(this);
        setTitleDisable();
        mSupportPresenter = new ShopCommentSupportPresenter(new ISupportView() {
            @Override
            public void onSuppotedSuccess(BaseBean bean) {
                if (viewHolder != null) {
                    mDetailData.suppoppType = 1;
                    viewHolder.ic_support2.setImageResource(R.mipmap.ic_good_red);
                    mDetailData.praises += 1;
                    viewHolder.tv_supports2.setText(mDetailData.praises + "");
                }
            }

            @Override
            public void showProgress(boolean toShow) {
                ShopCommentDetailFragment.this.showProgress(toShow);
            }

            @Override
            public void showError(ErrorBean errorBean) {
                ShopCommentDetailFragment.this.showError(errorBean);
            }
        });

        mBaseView.findViewById(R.id.iv_dot).setOnClickListener(this);
        mId = getActivity().getIntent().getStringExtra(Const.ID);
        mActivity = getActivity();
    }

    private void initItem() {
        mResClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childIndex = (int) v.getTag(R.id.child_index);
                ArrayList<StateListBean.ResData> forumThreadResources = transStateListBean();
                startActivity(new Intent(getActivity(), StateImgActivity.class)
                        .putExtra(Const.RES, forumThreadResources)
                        .putExtra(Const.INDEX, childIndex)
                );
            }
        };
        int type = getArguments().getInt(Const.TYPE);
        GlideUtil glideInstance = GlideUtil.getInstance();
        View child = null;
        switch (type) {
            case 0:
                child = View.inflate(mActivity, R.layout.item_state_txt, null);
                break;
            case 1:
                child = View.inflate(mActivity, R.layout.item_state_single_image, null);
                break;
            case 2:
                child = View.inflate(mActivity, R.layout.item_state_multi_image_1, null);
                break;
            case 3:
                child = View.inflate(mActivity, R.layout.item_state_multi_image_2, null);
                break;
            case 4:
                child = View.inflate(mActivity, R.layout.item_state_multi_image_3, null);
                break;
            case 5:
                child = View.inflate(mActivity, R.layout.item_state_video, null);
                break;
        }
        mStateLayout.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
        mStateLayout.findViewById(R.id.layout_support).setVisibility(View.VISIBLE);
        mStateLayout.addView(child, 1);
        viewHolder = new StateListAdapter.ViewHolder(mStateLayout);
        viewHolder.tv_content.setText(mDetailData.content);
        viewHolder.tv_supports2.setText(mDetailData.praises + "");
        viewHolder.ic_more.setVisibility(View.GONE);
        if (viewHolder.image_container != null) {
            viewHolder.image_container.setGridMode();
            viewHolder.image_container.setHorizontalSpacing(16);
            viewHolder.image_container.setVerticalSpacing(16);
        }
        viewHolder.ic_support2.setOnClickListener(this);
        if (mDetailData.suppoppType == 1) {
            viewHolder.ic_support2.setImageResource(R.mipmap.ic_good_red);
        } else {
            viewHolder.ic_support2.setImageResource(R.mipmap.ic_good);
        }


        glideInstance.display(viewHolder.iv_user_header, mDetailData.memPicUrl);
        viewHolder.tv_user_name.setText(mDetailData.memNickname);
        viewHolder.iv_user_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeadClickUtil.handleClick(getContext(), mDetailData.memId, null);
            }
        });

        viewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(mDetailData.createTime)));
        switch (type) {
            case 0:
                break;
            case 1:
                viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, mDetailData.merchantRestaurantsCommentImgs.get(0).url);
                break;

            default:
                for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
                    ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
                    if (i < mDetailData.merchantRestaurantsCommentImgs.size()) {
                        ShopCommentDetailBean.ResBean resData = mDetailData.merchantRestaurantsCommentImgs.get(i);
                        imageview.setTag(R.id.child_index, i);
                        imageview.setOnClickListener(mResClickListener);
                        glideInstance.display(imageview, resData.url);
                    } else {
                        imageview.destroyDrawingCache();
                    }
                }
        }
    }

    private ArrayList<StateListBean.ResData> transStateListBean() {
        ArrayList<StateListBean.ResData> list = new ArrayList<>();
        List<ShopCommentDetailBean.ResBean> resList = mDetailData.merchantRestaurantsCommentImgs;
        for (ShopCommentDetailBean.ResBean bean : resList) {
            StateListBean.ResData resData = new StateListBean.ResData();
            resData.resourceUrl = bean.url;
            list.add(resData);
        }
        return list;
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
                if (mDetailData != null) {
                    if (mDetailData.suppoppType == 0)
                        mSupportPresenter.support(mId, 0);
                    else
                        mShopCommentSupportCanclePresenter.cancleSupport(mId, 0);
                }

                break;
            case R.id.iv_dot:
                if (mDetailData != null) {
                    startActivity(new Intent(getContext(), AllSupportedUserActivity.class).putExtra(Const.OBJ, getTranseStateData()));
                }
                break;
        }
    }

    private StateDetailBean.DataBean getTranseStateData() {
        StateDetailBean.DataBean dataBean = new StateDetailBean.DataBean();
        dataBean.userMemberDos = new ArrayList<>();
        List<ShopCommentDetailBean.SupportUserBean> userMemberDos = mDetailData.restaurantsCommentSuppopps;
        for (int i = 0; i < userMemberDos.size(); i++) {
            StateDetailBean.DataBean.UserMemberDosBean userMemberDosBean1 = new StateDetailBean.DataBean.UserMemberDosBean();
            ShopCommentDetailBean.SupportUserBean userMemberDosBean = userMemberDos.get(i);
            userMemberDosBean1.picUrl = userMemberDosBean.memPicUrl;
            userMemberDosBean1.nickname = userMemberDosBean.memNickname;
            dataBean.userMemberDos.add(userMemberDosBean1);

        }
        return dataBean;
    }


    @Override
    public void showDetailInfo(ShopCommentDetailBean bean) {
        ShopCommentDetailBean.DataBean data = bean.data;
        if (data != null) {
            mDetailData = data;
            List<ShopCommentDetailBean.SupportUserBean> userMemberDos = mDetailData.restaurantsCommentSuppopps;
            GlideUtil instance = GlideUtil.getInstance();
            for (int i = 0; i < userMemberDos.size(); i++) {
                if (i < mHeaderContainer.getChildCount()) {
                    ShopCommentDetailBean.SupportUserBean userMemberDosBean = userMemberDos.get(i);
                    ImageView child = (ImageView) mHeaderContainer.getChildAt(i);
                    instance.display(child, userMemberDosBean.memPicUrl);
                }
            }
            initItem();
        }
    }

    @Override
    public void showCancleSupportResult(BaseBean bean) {
        if (bean.status == 1) {
            mDetailData.suppoppType = 0;
            viewHolder.ic_support2.setImageResource(R.mipmap.ic_good);
            mDetailData.praises -= 1;
            viewHolder.tv_supports2.setText(mDetailData.praises + "");
        }
    }
}
