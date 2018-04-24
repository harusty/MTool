package com.zzkx.mtool.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.ShopDetailBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddAttentionShopPresenter;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.CancleAttentionShopPresenter;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.CollectPresenter;
import com.zzkx.mtool.presenter.ShopDetailPresenter;
import com.zzkx.mtool.presenter.StateCollectPresenter;
import com.zzkx.mtool.presenter.SupportCanclePresenter;
import com.zzkx.mtool.presenter.SupportPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.OrderDetailMapActivity;
import com.zzkx.mtool.view.activity.ShopDetailActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.activity.VideoPlayerAcitivity;
import com.zzkx.mtool.view.adapter.StateListAdapter;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.MtoolRatingBar;
import com.zzkx.mtool.view.iview.IAddAttentionShopView;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.ICollectView;
import com.zzkx.mtool.view.iview.IShopDetailView;
import com.zzkx.mtool.view.iview.IStateCollectView;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;

/**
 * Created by sshss on 2017/9/13.
 */

public class ShopInfoFragment2 extends BaseListFragment<StateListBean.DataBean>
        implements View.OnClickListener, ICollectView, IShopDetailView, ISupportView, ICancleCollectionView, IStateCollectView, ISupportCancleView, IAddAttentionShopView {

    ImageView mIvShopLogo;
    TextView mTvShopName;
    TextView mTvRatingServ;
    TextView mTvRatingUser;
    MtoolRatingBar mRatingServ;
    MtoolRatingBar mRatingUser;
    TextView mShopIntro;
    ImageView mIvCollect;
    View mLayoutCollect;
    TextView mTvPhone;
    TextView mTvAddress;
    private ShopDetailActivity mActivity;
    private CollectPresenter mCollectPresenter;
    private ShopDetailPresenter mShopDetailPresenter;
    private String mShopId;
    private ShopDetailBean.MerchantRestaurantsDoBean mMerchantRestaurantsDo;
    private int mClickPosition;
    private StateListAdapter mStateListAdapter;
    private SupportPresenter mSupportPresenter;
    private DialogState mDialogStateMore;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    private StateCollectPresenter mStateCollectPresenter;
    private SupportCanclePresenter mSupportCanclePresenter;
    private AddAttentionShopPresenter mAddAttentionShopPresenter;
    private CancleAttentionShopPresenter mCancleAttentionShopPresenter;
    private ImageView mIvAttention;
    private View mHeader;

    @Override
    public BaseAdapter getAdapter() {

        mStateListAdapter = new StateListAdapter(getActivity(), mTotalData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickPosition = (int) v.getTag();
                StateListBean.DataBean item = mStateListAdapter.getItem(mClickPosition);

                switch (v.getId()) {
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
                            mDialogStateMore = new DialogState(getContext(), new View.OnClickListener() {

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
        mShopId = getActivity().getIntent().getStringExtra(Const.ID);
        mShopDetailPresenter = new ShopDetailPresenter(this, mShopId);
        return mShopDetailPresenter;
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        mActivity = (ShopDetailActivity) getActivity();

        mAddAttentionShopPresenter = new AddAttentionShopPresenter(this);
        mCancleAttentionShopPresenter = new CancleAttentionShopPresenter(this);

        mHeader = View.inflate(getContext(), R.layout.header_shop_info, null);
        mIvShopLogo = (ImageView) mHeader.findViewById(R.id.iv_shop_logo);
        mIvCollect = (ImageView) mHeader.findViewById(R.id.iv_collect);
        mTvShopName = (TextView) mHeader.findViewById(R.id.tv_shop_name);
        mTvRatingServ = (TextView) mHeader.findViewById(R.id.tv_rating_serv);
        mTvRatingUser = (TextView) mHeader.findViewById(R.id.tv_rating_user);
        mShopIntro = (TextView) mHeader.findViewById(R.id.tv_intro);
        mTvPhone = (TextView) mHeader.findViewById(R.id.tv_phone);
        mTvAddress = (TextView) mHeader.findViewById(R.id.tv_address);
        mRatingServ = (MtoolRatingBar) mHeader.findViewById(R.id.rating_serv);
        mRatingUser = (MtoolRatingBar) mHeader.findViewById(R.id.rating_user);
        mLayoutCollect = mHeader.findViewById(R.id.layout_collect);
        mIvAttention = (ImageView) mHeader.findViewById(R.id.iv_attention);

        mHeader.findViewById(R.id.layout_phone).setOnClickListener(this);
        mHeader.findViewById(R.id.layout_shop_map).setOnClickListener(this);
        mHeader.findViewById(R.id.layout_attention).setOnClickListener(this);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getActivity(), 10));
        mListView.setHeaderDividersEnabled(false);
        if (mListView.getHeaderViewsCount() == 0)
            mListView.addHeaderView(mHeader);

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

        mSupportPresenter = new SupportPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        mStateCollectPresenter = new StateCollectPresenter(this);
        mSupportCanclePresenter = new SupportCanclePresenter(this);
        mDialogStateMore = new DialogState(getContext(), new View.OnClickListener() {

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
                }

            }
        });
    }

    private void setHeaderData() {
        mTvShopName.setText(mMerchantRestaurantsDo.name);
        GlideUtil.getInstance().display(mIvShopLogo, mMerchantRestaurantsDo.logoUrl);
        int scre_serv = mMerchantRestaurantsDo.serviceScore;
        mRatingServ.setCount(scre_serv);
        mTvRatingServ.setText(scre_serv + "");

        int scre_user = mMerchantRestaurantsDo.priceScore;
        mRatingUser.setCount(scre_user);
        mTvRatingUser.setText(scre_user + "");

        mShopIntro.setText(mMerchantRestaurantsDo.description);
        mTvPhone.setText("电话：" + mMerchantRestaurantsDo.hotline);
        mTvAddress.setText("地址：" + mMerchantRestaurantsDo.address);

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

    private View.OnClickListener getDeleteAttentionListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCancleAttentionShopPresenter.cancleShopAttention(mShopId);
            }
        };
    }

    private View.OnClickListener getAddAttentionListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddAttentionShopPresenter.addShopAttention(mShopId);
            }
        };
    }

    @Override
    public void initNet() {
        mShopDetailPresenter.getListData(1);
    }

    @Override
    public void showData(ShopDetailBean bean) {
        ShopDetailBean.DataBean data = bean.data;
        if (data != null) {
            if (data.flag == 1) {
                mIvCollect.setImageResource(R.mipmap.ic_red_star_empty);
                mLayoutCollect.setEnabled(false);
            } else {
                mLayoutCollect.setOnClickListener(this);
            }
            mMerchantRestaurantsDo = data.merchantRestaurantsDo;
            setAttention(data.attentionFlag);
            setHeaderData();
        }
    }

    @Override
    public void showEmpty() {
        mListView.setFooterGone();
        mListView.setAdapter(getAdapter());
    }


    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_collect:
                if (mCollectPresenter == null)
                    mCollectPresenter = new CollectPresenter(this);
                mCollectPresenter.collectShop(mShopId);
                break;
            case R.id.layout_phone:
                Resources resources = getResources();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("呼叫：" + mMerchantRestaurantsDo.hotline);
                builder.setPositiveButton(resources.getString(R.string.confrim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mMerchantRestaurantsDo.hotline));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(resources.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.layout_shop_map:
                LatLonPoint serializable = new LatLonPoint(mMerchantRestaurantsDo.latitude, mMerchantRestaurantsDo.longitude);
                startActivity(new Intent(getContext(), OrderDetailMapActivity.class)
                        .putExtra(Const.TYPE, 0)
                        .putExtra(Const.LOC_INFO, serializable.getLongitude() + "," + serializable.getLatitude())
                        .putExtra(Const.ID, mShopId)
                        .putExtra(Const.URL, getActivity().getIntent().getStringExtra(Const.URL))
                        .putExtra(Const.TITLE, getActivity().getIntent().getStringExtra(Const.TITLE))
                        .putExtra(Const.CUS_ADDRESS, getActivity().getIntent().getStringExtra(Const.CUS_ADDRESS))
                );
                break;
            case R.id.layout_attention:

                break;
        }
    }

    @Override
    public void collectSuccess(BaseBean bean) {
        mIvCollect.setImageResource(R.mipmap.ic_red_star_empty);
        mLayoutCollect.setEnabled(false);
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
    public void showError(ErrorBean errorBean) {
        if (API.ADD_SHOP_ATTENTION.equals(errorBean.url) || API.CANCLE_SHOP_ATTENTION.equals(errorBean.url))
            return;
        super.showError(errorBean);
    }

    @Override
    public void showAddShopAttentionResult(BaseBean bean) {
        if (bean.status == 1) {
            mIvAttention.setImageResource(R.mipmap.ic_heart);
            setAttention(1);
        } else {
            ToastUtils.showToast("添加关注失败，请重试");
        }
    }

    @Override
    public void showCancleShopAttentionResult(BaseBean bean) {
        if (bean.status == 1) {
            mIvAttention.setImageResource(R.mipmap.ic_heart_gray);
            setAttention(0);
        } else {
            ToastUtils.showToast("取消关注失败，请重试");
        }
    }
}
