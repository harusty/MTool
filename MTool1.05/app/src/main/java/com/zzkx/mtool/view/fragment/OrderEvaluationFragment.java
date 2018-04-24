package com.zzkx.mtool.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.imple.ISuccessListener;
import com.zzkx.mtool.presenter.OrderDetailPresenter;
import com.zzkx.mtool.util.CachePathUtil;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ImageContainerHelper;
import com.zzkx.mtool.util.ImageContainerHelper.Holder;
import com.zzkx.mtool.util.ScreenUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.FoodSingleEnvaluateActivity;
import com.zzkx.mtool.view.activity.PicturePreviewActivity;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.MtoolRatingBar;
import com.zzkx.mtool.view.customview.RoundImageView;
import com.zzkx.mtool.view.customview.SimpleDialog;
import com.zzkx.mtool.view.iview.IOrderDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/20.
 */

public class OrderEvaluationFragment extends BaseFragment implements View.OnClickListener, IOrderDetailView {

    @BindView(R.id.iv_shop_logo)
    ImageView mIvShopLogo;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.rating_user)
    MtoolRatingBar mRatingUser;
    @BindView(R.id.rating_serv)
    MtoolRatingBar mRatingServ;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.rating_deliver)
    MtoolRatingBar mRatingDeliver;
    @BindView(R.id.delever_tag_container)
    CategoryLayout mTagContainer;
    @BindView(R.id.image_container)
    CategoryLayout mImageContainer;
    @BindView(R.id.single_container)
    LinearLayout mSingleContainer;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.iv_deliver_log)
    ImageView mIvDeliverLogo;
    @BindView(R.id.tv_deliver_name)
    TextView mTvDeliverName;
    @BindView(R.id.tv_deliver_company)
    TextView mTvDeliverCompany;

    private File cameraFile;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_LOCAL_VIDEO = 4;
    private int mImageSize;
    private OrderDetailPresenter mPresenter;
    private String mOrderId;
    private OrderDetailBean.DataBean mOrderDetailData;
    public List<RequestBean.OrderRequestBean> merchantRestaurantsCommentImgs = new ArrayList<>();
    private SimpleDialog mImageDialog;
    private ImageContainerHelper mImageContainerHelper;
    private static int MAX_SIZE = 9;
    private static final int INDEX = R.id.indicator;

    @Override
    public int getContentRes() {
        return R.layout.fragment_order_evaluation;
    }

    @Override
    public void initViews() {
        mPresenter = new OrderDetailPresenter(this, getActivity());
        mOrderId = getArguments().getString(Const.ID);
        mBaseView.findViewById(R.id.tv_submit).setOnClickListener(this);
        setTitleDisable();
        mRatingUser.setStarOption(Dip2PxUtils.dip2px(getActivity(), 18), Dip2PxUtils.dip2px(getActivity(), 20));
        mRatingServ.setStarOption(Dip2PxUtils.dip2px(getActivity(), 18), Dip2PxUtils.dip2px(getActivity(), 20));
        mRatingDeliver.setStarOption(Dip2PxUtils.dip2px(getActivity(), 18), Dip2PxUtils.dip2px(getActivity(), 20));
        mRatingUser.setTouchable();
        mRatingServ.setTouchable();
        mRatingDeliver.setTouchable();
        mIvAdd.setOnClickListener(this);
        mImageSize = getImageSize();
        mImageContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(getContext(), 10));
        mIvAdd.getLayoutParams().height = mImageSize;
        mIvAdd.getLayoutParams().width = mImageSize;
        initImageDialog();
        mImageContainerHelper = new ImageContainerHelper(new ImageContainerHelper.Provider() {
            @Override
            public Activity getContext() {
                return getActivity();
            }

            @Override
            public ViewGroup getImageContainer() {
                return mImageContainer;
            }

            @Override
            public View getIvAdd() {
                return mIvAdd;
            }

        }, new ImageContainerHelper.ActionListener() {
            @Override
            public void showProgress(boolean flag) {
                OrderEvaluationFragment.this.showProgress(flag);
            }

            @Override
            public void actionFinish() {
                handleFinish();
            }
        });
    }

    private void initImageDialog() {
        if (mImageDialog == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_camera:
                            selectPicFromCamera();
                            break;
                        case R.id.tv_gallery:
                            startActivityForResult(new Intent(getActivity(), PicturePreviewActivity.class)
                                            .putExtra("size", MAX_SIZE - (mImageContainer.getChildCount() - 1))
                                    , REQUEST_CODE_LOCAL);
                            break;
                    }
                    mImageDialog.dismiss();
                }
            };
            mImageDialog = new SimpleDialog(getActivity(), R.layout.dialog_img_select);
            mImageDialog.getView().findViewById(R.id.tv_camera).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_gallery).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_back).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
            mImageDialog.getView().findViewById(R.id.title).setVisibility(View.GONE);
        }
    }

    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getContext(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(CachePathUtil.getTmpImagePath(getActivity()),
                System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
    }


    @Override
    public void initNet() {
        mPresenter.getOrderInfo(mOrderId);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                selectImage();
                break;
            case R.id.tv_submit:
                handleFinish();
                break;
        }
    }

    public void handleFinish() {
        if (mOrderDetailData != null) {
            RequestBean requestBean = new RequestBean();
            requestBean.merchantRestaurantsComment = new RequestBean.OrderRequestBean();
            requestBean.merchantRestaurantsComment.restaurantsId = mOrderDetailData.shopId;
            requestBean.merchantRestaurantsComment.orderId = mOrderId;
            requestBean.merchantRestaurantsComment.score = mRatingUser.getRating() + "";
            requestBean.merchantRestaurantsComment.serverScore = mRatingServ.getRating() + "";
            String s = mEtComment.getText().toString();
            if (!TextUtils.isEmpty(s) && s.length() > 80) {
                ToastUtils.showToast("评价字数最多80字");
                return;
            }
            requestBean.merchantRestaurantsComment.content = s;
            requestBean.merchantRestaurantsCommentImgs = new ArrayList<>();
            for (int i = 0; i < mImageContainer.getChildCount(); i++) {
                View view = mImageContainer.getChildAt(i);
                Holder holder = (Holder) view.getTag(INDEX);
                if (holder != null) {
                    RequestBean.OrderRequestBean orderRequestBean = new RequestBean.OrderRequestBean();
                    orderRequestBean.url = holder.url;
                    requestBean.merchantRestaurantsCommentImgs.add(orderRequestBean);
                }

            }
            OrderDetailBean.DataBean.ExpressageCourier expressageCourier = mOrderDetailData.expressageCourier;
            if (expressageCourier != null) {
                requestBean.expressageCourierComment = new RequestBean.OrderRequestBean();
                requestBean.expressageCourierComment.courierId = expressageCourier.id;
                String content = "";
                for (int i = 0; i < mTagContainer.getChildCount(); i++) {
                    CheckBox childAt = (CheckBox) mTagContainer.getChildAt(i);
                    if (childAt.isChecked()) {
                        content += childAt.getText().toString() + ",";
                    }
                }
                requestBean.expressageCourierComment.content = content;
                requestBean.expressageCourierComment.score = mRatingDeliver.getRating() + "";
            } else {
                ToastUtils.showToast("ExpressageCourier null");
//                        return;
            }
            requestBean.foodComment = foodComment;

            mPresenter.submit(requestBean, new ISuccessListener() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    if (baseBean.status == 1) {
                        ToastUtils.showToast("提交成功");
                        getActivity().setResult(Const.RESULT_SUCESS_CODE);
                        getActivity().finish();
                    } else {
                        ToastUtils.showToast(baseBean.msg);
                    }
                }
            });
        } else {
            ToastUtils.showToast("mOrderDetailData null");
        }
    }

    private void selectImage() {
        mImageDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                String path = cameraFile.getAbsolutePath();
                mImageContainerHelper.addAndCreateView(path);
                if (mImageContainer.getChildCount() - 1 == MAX_SIZE) {
                    mIvAdd.setVisibility(View.GONE);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) {
                ArrayList<String> pathses = (ArrayList<String>) data.getSerializableExtra("data");
                mImageContainerHelper.addAndCreateView(pathses);
                if (mImageContainer.getChildCount() - 1 == MAX_SIZE) {
                    mIvAdd.setVisibility(View.GONE);
                }
            }
        }

        if (resultCode == Const.RESULT_SUCESS_CODE && requestCode == TO_SINGLE_MENU) {
            String comment = data.getStringExtra(Const.COMMENT);
            String images = data.getStringExtra(Const.IMAGES);
            RequestBean.OrderRequestBean orderRequestBean = foodComment.get(mClickMenu);
            orderRequestBean.content = comment;
            orderRequestBean.imagesUrl = images;
            View childAt = mSingleContainer.getChildAt(mClickMenu);
            childAt.findViewById(R.id.tv_envaluate_state).setVisibility(View.VISIBLE);
        }
    }

    private ImageView createIamgeView(int index, String url) {
        RoundImageView roundImageView = new RoundImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mImageSize, mImageSize);
        layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(getContext(), 10), 0);
        roundImageView.setLayoutParams(layoutParams);
        roundImageView.setBorderRadius(3);
        Holder holder = new Holder(roundImageView);
        holder.index = index;
        holder.url = url;
        roundImageView.setTag(INDEX, holder);
//        roundImageView.setOnClickListener(imgClickListner);
        return roundImageView;
    }

    private int getImageSize() {
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mImageContainer.getLayoutParams();
        screenWidth -= layoutParams.rightMargin + layoutParams.leftMargin +
                mImageContainer.getPaddingLeft() + mImageContainer.getPaddingRight();
        return (screenWidth - Dip2PxUtils.dip2px(getActivity(), 10) * 3) / 4;
    }

    @Override
    public void showData(OrderDetailBean bean) {
        mOrderDetailData = bean.data;
        if (mOrderDetailData != null) {
            OrderDetailBean.DataBean.MerchantRestaurantsBean merchantRestaurants = mOrderDetailData.merchantRestaurants;
            if (merchantRestaurants != null) {
                mTvShopName.setText(merchantRestaurants.name);
                GlideUtil.getInstance().display(mIvShopLogo, merchantRestaurants.logoUrl);
            }
            OrderDetailBean.DataBean.ExpressageCourier expressageCourier = mOrderDetailData.expressageCourier;
            if (expressageCourier != null) {
                GlideUtil.getInstance().display(mIvDeliverLogo, expressageCourier.picUrl);
                mTvDeliverCompany.setText(expressageCourier.nickname);
            }
            mTvDeliverName.setText(mOrderDetailData.courierName);
            setMenuData(mOrderDetailData.orderDetailSkuList);
        }
    }

    private List<RequestBean.OrderRequestBean> foodComment = new ArrayList<>();
    private int mClickMenu;
    private static final int TO_SINGLE_MENU = 10;
    View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mClickMenu = (int) v.getTag();
            String goodsUrl = mOrderDetailData.orderDetailSkuList.get(mClickMenu).goodsUrl;
            String goodsName = mOrderDetailData.orderDetailSkuList.get(mClickMenu).goodsName;
            startActivityForResult(new Intent(getActivity(), FoodSingleEnvaluateActivity.class)
                            .putExtra(Const.NAME, goodsName)
                            .putExtra(Const.URL, goodsUrl)
                    , TO_SINGLE_MENU);
        }
    };

    private void setMenuData(List<OrderDetailBean.DataBean.OrderDetailListBean> orderDetailList) {
        mSingleContainer.removeAllViews();

        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetailBean.DataBean.OrderDetailListBean bean = orderDetailList.get(i);
            View simpleView = View.inflate(MyApplication.getContext(), R.layout.item_order_simple2, null);
            ImageView iv_food = (ImageView) simpleView.findViewById(R.id.iv_food);
            TextView tv_name = (TextView) simpleView.findViewById(R.id.tv_section);
            tv_name.setText(bean.goodsName);
            GlideUtil.getInstance().display(iv_food, bean.goodsUrl);
            simpleView.setTag(i);
            simpleView.setOnClickListener(menuClickListener);
            RequestBean.OrderRequestBean orderRequestBean = new RequestBean.OrderRequestBean();
            orderRequestBean.foodId = bean.goodsId;
            orderRequestBean.orderId = mOrderId;
            orderRequestBean.score = 5 + "";
            foodComment.add(orderRequestBean);
            mSingleContainer.addView(simpleView);
        }
    }
}
