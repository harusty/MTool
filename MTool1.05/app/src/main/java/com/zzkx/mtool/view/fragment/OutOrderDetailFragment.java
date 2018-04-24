package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.OrderStatus;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.activity.OrderDetailMapActivity;
import com.zzkx.mtool.view.activity.OrderDetailMapDelivActivity;
import com.zzkx.mtool.view.activity.OrderEnvaluateActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.iview.IOrderDetailView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/14.
 */

public class OutOrderDetailFragment extends BaseFragment implements IOrderDetailView, View.OnClickListener {
    @BindView(R.id.tv_delivere_info)
    TextView mTvDeliverInfo;
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
    @BindView(R.id.tv_receive_name)
    TextView mTvReceiveName;
    @BindView(R.id.tv_address)
    TextView mTvReceiveAddress;
    @BindView(R.id.tv_order_status)
    TextView mTvOrderSatus;
    @BindView(R.id.layout_to_envaluate)
    View mLayoutToEnvaluate;
    @BindView(R.id.layout_deliver)
    View mLayoutDeliver;
    @BindView(R.id.iv_map_deliver)
    View mIvMapDeliver;
    private String mOrderId;
    private String mDeliveryGeo;
    private String mStoreId;
    private int mOrderStatus;
    private List<OrderDetailBean.DataBean.OrderDetailListBean> mOrderDetailSkuList;
    private OrderDetailBean.DataBean mData;

    @Override
    public int getContentRes() {
        return R.layout.fragment_out_order_detail;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        setMainTitle(getString(R.string.orderDetail2));
        mLayoutToEnvaluate.setOnClickListener(this);
        mOrderId = getArguments().getString(Const.ID);
        mBaseView.findViewById(R.id.iv_shop_map).setOnClickListener(this);
        mBaseView.findViewById(R.id.tv_one_more_time).setOnClickListener(this);
        mIvMapDeliver.setOnClickListener(this);
        mLayoutDeliver.setVisibility(View.GONE);
    }


    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showData(OrderDetailBean bean) {
        OrderDetailBean.DataBean data = bean.data;
        System.out.println("isAdded: "+isAdded()+" isDetached:"+isDetached());

        if (data != null) {
            mData = data;
            mOrderStatus = data.status;
            if (mOrderStatus == 0) {

            } else {
                if (data.courierName != null)
                    mTvDeliverInfo.setText("配送员：" + data.courierName + "/" + data.consigneePhone);
                else {
                    mTvDeliverInfo.setText("配送员：等待骑手接单");
                    mIvMapDeliver.setVisibility(View.INVISIBLE);
                }
            }
            OrderDetailBean.DataBean.MerchantRestaurantsBean merchantRestaurants = data.merchantRestaurants;
            mTvTotalPrice.setText("合计：" + data.totalPrice + "元");
            mTvOrderTime.setText("订单时间：" + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(data.createTime));

            mOrderDetailSkuList = data.orderDetailSkuList;
            if (merchantRestaurants != null) {
                mTvShopName.setText("商家：" + merchantRestaurants.name);
                mTvShopPhone.setText("电话：" + merchantRestaurants.hotline);
                mTvShopAddress.setText("地址：" + merchantRestaurants.address);
                mStoreId = merchantRestaurants.storeId;
                mTvOrderNum.setText("订单号码：" + mOrderId);
                mTvPayType.setText("支付方式：" + data.pathName);
                setMenuData(data.orderDetailSkuList);
            }
            switch (mOrderStatus) {
                case OrderStatus.DAISHOULI://待受理
                    mTvOrderSatus.setText("订单状态：待受理");
                    break;
                case OrderStatus.DAIQUCAN://待取餐，待使用
                    mTvOrderSatus.setText("订单状态：待取餐");
                    mLayoutDeliver.setVisibility(View.VISIBLE);
                    break;
                case OrderStatus.PEISONGZHONG://配送中
                    mTvOrderSatus.setText("订单状态：配送中");
                    mLayoutDeliver.setVisibility(View.VISIBLE);
                    break;
                case OrderStatus.YIWANCHENG://已完成
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
            mTvReceiveName.setText(data.consignee + "/" + data.consigneePhone);
            mTvReceiveAddress.setText(data.consigneeAddr);
        }
    }

    private void setMenuData(List<OrderDetailBean.DataBean.OrderDetailListBean> orderDetailList) {
        mOrderContainer.removeAllViews();
        for (OrderDetailBean.DataBean.OrderDetailListBean bean : orderDetailList) {
            View simpleView = View.inflate(MyApplication.getContext(), R.layout.item_order_simple, null);
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
                            CartCacheUtil.getInstance().saveOption(menuOpiton, CartCacheUtil.TYPE_OUT);
                        } else {
                            foodInfoListBean.cusCount = bean.quantity;
                            CartCacheUtil.getInstance().saveMenu(foodInfoListBean, CartCacheUtil.TYPE_OUT);
                        }
                    }
                    startActivity(new Intent(getActivity(), ShopActivity.class).putExtra(Const.ID, mData.shopId));
                }
                break;
            case R.id.iv_map_deliver:
                if (mData != null) {
                    startActivity(new Intent(getContext(), OrderDetailMapDelivActivity.class)
                            .putExtra(Const.ID, mData.courierId)
                            .putExtra(Const.SHOP_ID, mStoreId)
                            .putExtra(Const.ORDER_ID, mOrderId)
                            .putExtra("order_status", mOrderStatus)
                    );
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
