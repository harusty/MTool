package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.OrderStatus;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.activity.OrderDetailMapActivity;
import com.zzkx.mtool.view.activity.OrderEnvaluateActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.customview.DialogQr;
import com.zzkx.mtool.view.iview.IOrderDetailView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/14.
 */

public class InOrderDetailFragment extends BaseFragment implements IOrderDetailView, View.OnClickListener {
    @BindView(R.id.tv_date_time)
    TextView mTvDateTime;
    @BindView(R.id.tv_qr_num)
    TextView mTvQrNum;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.tv_shop_phone)
    TextView mTvShopPhone;
    @BindView(R.id.tv_shop_address)
    TextView mTvShopAddress;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.tv_pay_type)
    TextView mTvPayType;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.layout_order_container)
    ViewGroup mOrderContainer;
    @BindView(R.id.tv_order_status)
    TextView mTvOrderSatus;
    @BindView(R.id.layout_to_envaluate)
    View mLayoutToEnvaluate;
    private FragmentActivity mActivity;
    private String mOrderId;
    private SimpleDateFormat mFormat;
    private String mDeliveryGeo;
    private String mStoreId;
    private OrderDetailBean.DataBean mData;
    private List<OrderDetailBean.DataBean.OrderDetailListBean> mOrderDetailSkuList;
    private DialogQr mDialogQr;

    @Override
    public int getContentRes() {
        return R.layout.fragment_in_order_detail;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        setMainTitle(getString(R.string.orderDetail2));
        mFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
        mActivity = getActivity();
        mOrderId = getArguments().getString(Const.ID);
        mLayoutToEnvaluate.setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_qr).setOnClickListener(this);
        mBaseView.findViewById(R.id.tv_one_more_time).setOnClickListener(this);
        mBaseView.findViewById(R.id.iv_shop_map).setOnClickListener(this);
        mDialogQr = new DialogQr(getContext());
    }


    @Override
    public void showData(OrderDetailBean bean) {
        OrderDetailBean.DataBean data = bean.data;
        if (data != null) {
            mData = data;
            mOrderDetailSkuList = data.orderDetailSkuList;
            mTvDateTime.setText("预约订座：" + mFormat.format(data.appointTime));
            mTvOrderTime.setText("订单时间：" + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(data.createTime));
            OrderDetailBean.DataBean.MerchantRestaurantsBean merchantRestaurants = data.merchantRestaurants;
            if (merchantRestaurants != null) {
                mTvShopName.setText("商家：" + merchantRestaurants.name);
                mTvShopPhone.setText("电话：" + merchantRestaurants.hotline);
                mTvShopAddress.setText("地址：" + merchantRestaurants.address);
                mTvOrderNum.setText("订单号码：" + mOrderId);
                mTvQrNum.setText("识别码：" + mData.headingCode);
                mDialogQr.setCode("识别码：" + mData.headingCode);
                mTvPayType.setText("支付方式：" + data.pathName);
                mTvTotalPrice.setText("合计：" + data.totalPrice + "元");
                mStoreId = merchantRestaurants.storeId;
                mDialogQr.setImgRes(mData.qrCode);
                setMenuData(data.orderDetailSkuList);
            }
            switch (data.status) {
                case OrderStatus.DAISHOULI://待受理
                    mTvOrderSatus.setText("订单状态：待受理");
                    break;
                case OrderStatus.DAIQUCAN://待取餐，待使用
                    mTvOrderSatus.setText("订单状态：待使用");
                    break;
                case OrderStatus.YIWANCHENG://配送中 已完成
                    mTvOrderSatus.setText("订单状态：已完成");
                    mLayoutToEnvaluate.setVisibility(View.VISIBLE);
                    break;
                case OrderStatus.YIPINGJIA:
                    mTvOrderSatus.setText("订单状态：已评价");
                    break;
                case OrderStatus.TUIDAN:
                    mTvOrderSatus.setText("订单状态：用户退单");
                    break;
                case OrderStatus.JUDAN:
                case 7:
                case 8:
                    mTvOrderSatus.setText("订单状态：商家超时拒单");
                    break;

            }
            mDeliveryGeo = data.deliveryGeo;
        }
    }

    private void setMenuData(List<OrderDetailBean.DataBean.OrderDetailListBean> orderDetailList) {
        mOrderContainer.removeAllViews();
        for (OrderDetailBean.DataBean.OrderDetailListBean bean : orderDetailList) {
            View simpleView = View.inflate(getContext(), R.layout.item_order_simple, null);
            TextView tv_name = (TextView) simpleView.findViewById(R.id.tv_section);
            TextView tv_spec = (TextView) simpleView.findViewById(R.id.tv_spec);
            TextView tv_price_info = (TextView) simpleView.findViewById(R.id.tv_price_info);
            tv_name.setText(bean.goodsName);
            if (bean.foodSku != null && bean.foodSku.spec != null)
                tv_spec.setText(bean.foodSku.spec);
            tv_price_info.setText("x" + bean.quantity + "/￥" + bean.price);
            mOrderContainer.addView(simpleView);
        }

    }

    @Override
    public void onReload() {
        showProgress(true);
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_shop_map:
                OrderDetailBean.DataBean.MerchantRestaurantsBean merchantRestaurants = mData.merchantRestaurants;
                if (merchantRestaurants != null)
                    startActivity(new Intent(getContext(), OrderDetailMapActivity.class)
                            .putExtra(Const.SHOP_ID, mStoreId)
                            .putExtra(Const.TITLE, merchantRestaurants.name)
                            .putExtra(Const.CUS_ADDRESS, merchantRestaurants.address)
                    );
                break;
            case R.id.layout_to_envaluate:
                startActivity(new Intent(getActivity(), OrderEnvaluateActivity.class)
                        .putExtra(Const.ID, mOrderId));
                break;
            case R.id.tv_one_more_time:
                if (mData != null) {
                    CartCacheUtil.getInstance().clearCache();
                    for (OrderDetailBean.DataBean.OrderDetailListBean bean : mOrderDetailSkuList) {
                        MenuListBean.FoodInfoListBean foodInfoListBean = new MenuListBean.FoodInfoListBean();
                        foodInfoListBean.id = bean.goodsId;
                        foodInfoListBean.restaurantsId = mData.shopId;
                        foodInfoListBean.name = bean.goodsName;
                        if (bean.foodSku != null && bean.foodSku.spec != null) {
                            MenuListBean.MenuOpiton menuOpiton = new MenuListBean.MenuOpiton();
                            menuOpiton.id = bean.foodSku.id;
                            menuOpiton.spec = bean.foodSku.spec;
                            menuOpiton.cusCount = bean.quantity;
                            menuOpiton.cusShopId = mData.shopId;
                            menuOpiton.foodId = bean.goodsId;
                            CartCacheUtil.getInstance().saveOption(menuOpiton, CartCacheUtil.TYPE_IN);
                        } else {
                            foodInfoListBean.cusCount = bean.quantity;
                            CartCacheUtil.getInstance().saveMenu(foodInfoListBean, CartCacheUtil.TYPE_IN);
                        }
                    }
                    startActivity(new Intent(getActivity(), ShopActivity.class)
                            .putExtra(Const.ID, mData.shopId)
                            .putExtra(Const.INDEX, 1)
                    );
                }
                break;
            case R.id.layout_qr:
                if (mData != null)
                    mDialogQr.show();
                break;
        }
    }
}
