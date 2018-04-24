package com.zzkx.mtool.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.BaseListBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.ShopDetailBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddAttentionShopPresenter;
import com.zzkx.mtool.presenter.CancleAttentionShopPresenter;
import com.zzkx.mtool.presenter.CollectPresenter;
import com.zzkx.mtool.presenter.ShopDetailPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.OrderDetailMapActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.activity.ShopStateActivity;
import com.zzkx.mtool.view.customview.MtoolRatingBar;
import com.zzkx.mtool.view.iview.IAddAttentionShopView;
import com.zzkx.mtool.view.iview.ICollectView;
import com.zzkx.mtool.view.iview.IShopDetailView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/13.
 */

public class ShopInfoFragment extends BaseFragment
        implements View.OnClickListener, ICollectView, IShopDetailView, IAddAttentionShopView {

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
    private CollectPresenter mCollectPresenter;
    private ShopDetailPresenter mShopDetailPresenter;
    private String mShopId;
    private ShopDetailBean.MerchantRestaurantsDoBean mMerchantRestaurantsDo;
    private AddAttentionShopPresenter mAddAttentionShopPresenter;
    private CancleAttentionShopPresenter mCancleAttentionShopPresenter;
    private ImageView mIvShopLogo;
    @BindView(R.id.tv_cancle_attention)
    View mCancleAttention;
    @BindView(R.id.tv_to_shop)
    View mToShop;
    @BindView(R.id.tv_attention)
    View mAttention;

    @Override
    public int getContentRes() {
        return R.layout.fragment_shop_detail;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mShopId = getActivity().getIntent().getStringExtra(Const.ID);
        mShopDetailPresenter = new ShopDetailPresenter(this, mShopId);

        mAddAttentionShopPresenter = new AddAttentionShopPresenter(this);
        mCancleAttentionShopPresenter = new CancleAttentionShopPresenter(this);

        mIvCollect = (ImageView) mBaseView.findViewById(R.id.iv_collect);
        mTvShopName = (TextView) mBaseView.findViewById(R.id.tv_shop_name);
        mTvRatingServ = (TextView) mBaseView.findViewById(R.id.tv_rating_serv);
        mTvRatingUser = (TextView) mBaseView.findViewById(R.id.tv_rating_user);
        mShopIntro = (TextView) mBaseView.findViewById(R.id.tv_intro);
        mTvPhone = (TextView) mBaseView.findViewById(R.id.tv_phone);
        mTvAddress = (TextView) mBaseView.findViewById(R.id.tv_address);
        mRatingServ = (MtoolRatingBar) mBaseView.findViewById(R.id.rating_serv);
        mRatingUser = (MtoolRatingBar) mBaseView.findViewById(R.id.rating_user);
        mLayoutCollect = mBaseView.findViewById(R.id.layout_collect);
        mIvShopLogo = (ImageView) mBaseView.findViewById(R.id.iv_shop_logo);

        mBaseView.findViewById(R.id.layout_phone).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_shop_map).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_state).setOnClickListener(this);

        mCancleAttention.setOnClickListener(this);
        mToShop.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mCancleAttention.setVisibility(View.INVISIBLE);
        mToShop.setVisibility(View.INVISIBLE);
        mAttention.setVisibility(View.INVISIBLE);
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
            mAttention.setVisibility(View.GONE);
            mCancleAttention.setVisibility(View.VISIBLE);
            mToShop.setVisibility(View.VISIBLE);
        } else {
            mAttention.setVisibility(View.VISIBLE);
            mCancleAttention.setVisibility(View.GONE);
            mToShop.setVisibility(View.GONE);
        }

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
//                LatLonPoint serializable = new LatLonPoint(mMerchantRestaurantsDo.latitude, mMerchantRestaurantsDo.longitude);
//                startActivity(new Intent(getContext(), OrderDetailMapActivity.class)
//                        .putExtra(Const.LOC_INFO, serializable.getLongitude() + "," + serializable.getLatitude())
//                        .putExtra(Const.ID, mShopId)
//                        .putExtra(Const.URL, mMerchantRestaurantsDo.logoUrl)
//                        .putExtra(Const.TITLE, mMerchantRestaurantsDo.name)
//                        .putExtra(Const.CUS_ADDRESS, getActivity().getIntent().getStringExtra(Const.CUS_ADDRESS))
//                );

                startActivity(new Intent(getContext(), OrderDetailMapActivity.class)
                        .putExtra(Const.SHOP_ID, mMerchantRestaurantsDo.storeId)
                        .putExtra(Const.TITLE, mMerchantRestaurantsDo.name)
                        .putExtra(Const.CUS_ADDRESS, mMerchantRestaurantsDo.address)
                );
                break;
            case R.id.tv_cancle_attention:
                mCancleAttentionShopPresenter.cancleShopAttention(mShopId);
                break;
            case R.id.tv_attention:
                mAddAttentionShopPresenter.addShopAttention(mShopId);
                break;
            case R.id.tv_to_shop:
                if (ShopActivity.sInstance != null)
                    ShopActivity.sInstance.finish();
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                intent.putExtra(Const.ID, mShopId);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.layout_state:
                startActivityForResult(new Intent(getContext(), ShopStateActivity.class)
                        .putExtra(Const.ID, mShopId), 999
                );
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.RESULT_SUCESS_CODE)
            initNet();
    }

    @Override
    public void collectSuccess(BaseBean bean) {
        mIvCollect.setImageResource(R.mipmap.ic_red_star_empty);
        mLayoutCollect.setEnabled(false);
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
            setAttention(1);
        } else {
            ToastUtils.showToast("添加关注失败，请重试");
        }
    }

    @Override
    public void showCancleShopAttentionResult(BaseBean bean) {
        if (bean.status == 1) {
            setAttention(0);
        } else {
            ToastUtils.showToast("取消关注失败，请重试");
        }
    }

    @Override
    public void showRefreshComplete() {

    }

    @Override
    public void showList(BaseListBean baseListBean) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showReload() {

    }

}
