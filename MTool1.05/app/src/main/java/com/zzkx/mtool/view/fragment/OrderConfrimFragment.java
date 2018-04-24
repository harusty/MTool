package com.zzkx.mtool.view.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.MyMoneyBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddressListPresnter;
import com.zzkx.mtool.presenter.MyMoneyPresenter;
import com.zzkx.mtool.presenter.OrderConfirmPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.util.pay.ali.AliPay;
import com.zzkx.mtool.util.pay.wx.WxPay;
import com.zzkx.mtool.view.activity.AddressEditActivity;
import com.zzkx.mtool.view.activity.AddressListActivity;
import com.zzkx.mtool.view.activity.CartActivity;
import com.zzkx.mtool.view.activity.OrderRemarkActivity;
import com.zzkx.mtool.view.customview.CartChekBox;
import com.zzkx.mtool.view.customview.ExpandableView;
import com.zzkx.mtool.view.customview.SimpleDialog;
import com.zzkx.mtool.view.iview.IMyMoneyView;
import com.zzkx.mtool.view.iview.IOrderConfrimView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

import static java.lang.System.currentTimeMillis;

/**
 * Created by sshss on 2017/9/2.
 */

public class OrderConfrimFragment extends BaseFragment implements IOrderConfrimView, View.OnClickListener, IMyMoneyView {
    @BindView(R.id.layout_select_add_info)
    View mAddInfo;
    @BindView(R.id.add_info_container)
    ExpandableView mAddContainer;
    @BindView(R.id.layout_paytype)
    View mLayoutPayType;
    @BindView(R.id.paytype_container)
    ExpandableView mPayTypeContainer;
    @BindView(R.id.order_menu_container)
    ViewGroup mOrderContainer;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_order_info)
    TextView mTvOrderInfo;
    @BindView(R.id.tv_yue)
    TextView mTvYue;
    @BindView(R.id.tv_pay_name)
    TextView mTvPayName;

    private OrderConfirmPresenter mOrderConfirmPresenter;
    private CartActivity mActivity;
    private SimpleDialog mDialogTimePicker;
    private CusTimeSetListener mCusTimeSetListener;
    private RequestBean mRequestBean;
    private AddressListPresnter mAddressPresenter;
    private static final int REQUEST_ADDRESS = 100;
    private static final int REQUEST_REMARK = 101;
    private MyMoneyPresenter mMyMoneyPresenter;

    @Override
    public void onPageSelected() {
        showProgress(false);
        mOrderConfirmPresenter.initRequestBean();
        mRequestBean = mOrderConfirmPresenter.getRequestBean();
        showOrder(mActivity.mShopsTypeIn, mActivity.mShopsTypeOut);
        mAddressPresenter.getAddress();
    }

    private void showOrder(List<MenuListBean.DataBean> shopsTypeIn, List<MenuListBean.DataBean> shopsTypeOut) {
        initOrderTopInfo();
        mOrderContainer.removeAllViews();
        for (int i = 0; i < shopsTypeOut.size() + shopsTypeIn.size(); i++) {
            MenuListBean.DataBean dataBean;
            int type;
            if (i < shopsTypeOut.size()) {
                dataBean = shopsTypeOut.get(i);
                type = CartCacheUtil.TYPE_OUT;
            } else {
                dataBean = shopsTypeIn.get(i - shopsTypeOut.size());
                type = CartCacheUtil.TYPE_IN;
            }

            if (dataBean.cusSelectCount == 0 || dataBean.cusMenuCount == 0)
                continue;
            RequestBean.OrderDining orderDining = new RequestBean.OrderDining();
            mRequestBean.orderDiningList.add(orderDining);
            mRequestBean.payInfo.orders++;
            setOrderShopRequestInfo(dataBean, orderDining, type);

            View child;
            if (type == CartCacheUtil.TYPE_OUT) {
                child = View.inflate(getContext(), R.layout.item_confirm_order_out, null);
            } else {
                child = View.inflate(getContext(), R.layout.item_confirm_order_in, null);
            }
            OrderHolder holder = new OrderHolder(child, orderDining);
            final int finalI = i;
            child.findViewById(R.id.layout_remark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getContext(), OrderRemarkActivity.class)
                                    .putExtra(Const.INDEX, finalI)
                            , REQUEST_REMARK);
                }
            });
            setBaseViewInfo(dataBean, holder, type);

            List<MenuListBean.FoodInfoListBean> foodInfoList = dataBean.foodInfoList;
            if (foodInfoList != null && foodInfoList.size() > 0) {
                holder.order_list_container.removeAllViews();

                for (int j = 0; j < foodInfoList.size(); j++) {
                    MenuListBean.FoodInfoListBean menuBean = foodInfoList.get(j);

                    if (menuBean.cusCount == 0 || !menuBean.cusIsChecked)
                        continue;


                    List<MenuListBean.MenuOpiton> foodSkuList = menuBean.foodSkuList;
                    if (foodSkuList != null && foodSkuList.size() > 0) {
                        for (int k = 0; k < foodSkuList.size(); k++) {
                            MenuListBean.MenuOpiton menuOpiton = foodSkuList.get(k);
                            if (menuOpiton.cusCount == 0)
                                continue;

                            double price;
                            if (type == CartCacheUtil.TYPE_IN)
                                price = menuOpiton.priceIn;
                            else
                                price = menuOpiton.priceOut;

                            RequestBean.ConfirmMenuBean confirmMenuBean = new RequestBean.ConfirmMenuBean();
                            setMenuOrderInfo(menuBean, confirmMenuBean, type);
                            confirmMenuBean.quantity = menuOpiton.cusCount;
                            confirmMenuBean.price = price;
                            confirmMenuBean.skuId = menuOpiton.id;
                            confirmMenuBean.spec = menuOpiton.spec;
                            orderDining.orderDetailSkuList.add(confirmMenuBean);

                            View simpleView = View.inflate(getContext(), R.layout.item_order_simple, null);
                            TextView tv_name = (TextView) simpleView.findViewById(R.id.tv_section);
                            TextView tv_spec = (TextView) simpleView.findViewById(R.id.tv_spec);
                            TextView tv_price_info = (TextView) simpleView.findViewById(R.id.tv_price_info);
                            tv_name.setText(menuBean.name);
                            tv_spec.setText(menuOpiton.spec);
                            tv_price_info.setText("x" + menuOpiton.cusCount + "/￥" + price);
                            holder.order_list_container.addView(simpleView);
                        }

                    } else {
                        View simpleView = View.inflate(getContext(), R.layout.item_order_simple, null);
                        TextView tv_name = (TextView) simpleView.findViewById(R.id.tv_section);
                        TextView tv_price_info = (TextView) simpleView.findViewById(R.id.tv_price_info);
                        tv_name.setText(menuBean.name);
                        double price;
                        if (type == CartCacheUtil.TYPE_IN)
                            price = menuBean.priceIn;
                        else
                            price = menuBean.priceOut;
                        RequestBean.ConfirmMenuBean confirmMenuBean = new RequestBean.ConfirmMenuBean();
                        orderDining.orderDetailSkuList.add(confirmMenuBean);
                        setMenuOrderInfo(menuBean, confirmMenuBean, type);
                        confirmMenuBean.quantity = menuBean.cusCount;
                        confirmMenuBean.price = price;
                        tv_price_info.setText("x" + menuBean.cusCount + "/￥" + price);
                        holder.order_list_container.addView(simpleView);
                    }
                }
                holder.order_list_container.setExpand(false);
            }
            mOrderContainer.addView(child);
        }
    }

    private void setBaseViewInfo(MenuListBean.DataBean dataBean, OrderHolder holder, int type) {
        if (type == CartCacheUtil.TYPE_OUT) {
            holder.tv_title.setText("外送：" + dataBean.name);
            if (dataBean.allofee != null) {
                holder.tv_option_info.setText("立即：配送费" + dataBean.allofee.startPrice);
                holder.tv_option_1.setText("配送费" + dataBean.allofee.startPrice);
                holder.tv_alarm_time.setText(formatDate(mCusTimeSetListener.getSelectTimeStemp()));
                holder.tv_pick_time.setText(formatDate(mCusTimeSetListener.getSelectTimeStemp()));
            }
        } else {
            String usefalTime = getUsefalTime(dataBean.activeTime);
            holder.tv_option_info.setText("有效期：" + usefalTime);
            holder.tv_option_1.setText(usefalTime);
            holder.tv_alarm_time.setText(formatDate(mCusTimeSetListener.getSelectTimeStemp()));
            holder.tv_title.setText("到店：" + dataBean.name);
        }
        holder.iv_alarm_setting.setTag(holder);
        if (holder.iv_pick_time_setting != null)
            holder.iv_pick_time_setting.setTag(holder);
        holder.tv_shop_price.setText("合计：" + dataBean.cusShopOrderPrice + "元");
    }

    private String getUsefalTime(int activeTime) {
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);
        instance.set(year, month, day + activeTime);
        return new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
    }

    private String formatDate(long selectTimeStemp) {
        SimpleDateFormat simpleDateFormat = getDateFormater();
        return simpleDateFormat.format(selectTimeStemp);
    }

    @NonNull
    private SimpleDateFormat getDateFormater() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    private void setOrderShopRequestInfo(MenuListBean.DataBean dataBean, RequestBean.OrderDining orderDining, int type) {
        orderDining.shopId = dataBean.id;
        orderDining.shopName = dataBean.name;
        double peisong = dataBean.allofee == null ? null : dataBean.allofee.startPrice;
        if (type == CartCacheUtil.TYPE_IN)
            peisong = 0;
        orderDining.payMoney = orderDining.totalPrice = dataBean.cusShopOrderPrice + peisong;
        orderDining.diningType = type;
        orderDining.deliverFee = dataBean.allofee == null ? 0 : dataBean.allofee.startPrice;
        orderDining.appointTime = mCusTimeSetListener.getSelectTimeStemp();
        //外送：立即，定时时间，自提时间
        //到店：有效期时间，预约时间

        //备注
        //地址id
    }

    private void setMenuOrderInfo(MenuListBean.FoodInfoListBean menuBean, RequestBean.ConfirmMenuBean confirmMenuBean, int type) {
        confirmMenuBean.goodsId = menuBean.id;
        confirmMenuBean.goodsName = menuBean.name;
        confirmMenuBean.goodsUrl = menuBean.foodImages == null ? null : menuBean.foodImages.imgUrl;
    }


    private void initOrderTopInfo() {
        mTvTotalPrice.setText(String.valueOf(mActivity.mTotalPrice));
        mTvOrderInfo.setText("外送：" + mActivity.mTypeOutPrice +
                "（" + mActivity.mTypeOutNum + "份）/到店：" + mActivity.mTypeInPrice +
                "（" + mActivity.mTypeInNum + "份）");

        mRequestBean.payInfo.totalPrice = mActivity.mTotalPrice;
    }

    @Override
    public void showAddList(List<AddressListBean.AddressBean> data) {
        View.OnClickListener addClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHolder holder = (AddHolder) v.getTag();
                AddHolder holder2 = (AddHolder) mAddInfo.getTag();
                holder2.tv_info1.setText(holder.tv_info1.getText().toString());
                holder2.tv_info2.setText(holder.tv_info2.getText().toString());
                mRequestBean.consigneeId = holder.addressBean.id;
                mAddContainer.packUp();
            }
        };

        if (data != null && data.size() > 0) {
            mAddContainer.removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                AddressListBean.AddressBean addressBean = data.get(i);
                int defaultValue = addressBean.defaultValue;
                String info1 = addressBean.name + "  " + addressBean.phone;
                String info2 = addressBean.addrDetail;
                if (defaultValue == 1) {
                    mRequestBean.consigneeId = addressBean.id;
                    AddHolder addHolder = new AddHolder(mAddInfo, addressBean);
                    mAddInfo.setTag(addHolder);
                    addHolder.tv_info1.setText(info1);
                    addHolder.tv_info2.setText(info2);
                }
                AddHolder addHolder = new AddHolder(View.inflate(getContext(), R.layout.item_add_info, null), addressBean);
                addHolder.view.setOnClickListener(addClickListener);
                addHolder.view.setTag(addHolder);
                addHolder.tv_info1.setText(info1);
                addHolder.tv_info2.setText(info2);
                mAddContainer.addView(addHolder.view);
            }

            View footer = View.inflate(getContext(), R.layout.footer_address_manage, null);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getContext(), AddressListActivity.class), REQUEST_ADDRESS);
                }
            });
            mAddContainer.addView(footer);
        } else {
            mAddInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AddressEditActivity.class));
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            if (requestCode == REQUEST_ADDRESS) {
                showProgress(true);
                mAddressPresenter.getAddress();
            } else {
                String remark = data.getStringExtra("remark");
                int index = data.getIntExtra(Const.INDEX, 0);
                RequestBean.OrderDining orderDining = mRequestBean.orderDiningList.get(index);
                orderDining.remark = remark;
            }
        }
    }


    @Override
    public void showPay(OrderPayResultBean bean) {
        if (mRequestBean.payInfo.pathType == 0) {
            new WxPay(MyApplication.getContext()).parseWxPayInfo(bean);
        } else if (mRequestBean.payInfo.pathType == 1) {
            new AliPay().parseAli(mActivity, bean);
        }else {
            if(bean.status == 1){
                EventBus.getDefault().post(new BusPayBean(BusPayBean.SUCESS));
            }else{
                EventBus.getDefault().post(new BusPayBean(BusPayBean.FAILD));
            }
        }
    }

    @Override
    public void showParentProgress(boolean b) {
        showProgress(b);
        mActivity.getViewPager().setTouchEnable(!b);
    }

    @Override
    public void showPayInfoError() {
        mActivity.resetFragmentSate(false);
    }

    @Override
    public void showError(ErrorBean errorBean) {
        showProgress(false);
        mActivity.resetFragmentSate(false);
    }

    //从缓存清空已支付的缓存
    public void clearCartCache() {

    }

    @Override
    public void showWalletyInfo(MyMoneyBean bean) {
        mTvYue.setText("￥" + bean.data.account);
    }

    private class AddHolder {
        private AddressListBean.AddressBean addressBean;
        TextView tv_info1;
        TextView tv_info2;
        View view;

        public AddHolder(View view, AddressListBean.AddressBean addressBean) {
            this.addressBean = addressBean;
            this.view = view;
            tv_info1 = (TextView) view.findViewById(R.id.tv_info1);
            tv_info2 = (TextView) view.findViewById(R.id.tv_info2);
        }
    }

    private class OrderHolder {
        RequestBean.OrderDining mOrderDining;
        TextView tv_title;
        TextView tv_option_1;
        TextView tv_alarm_time;
        TextView tv_pick_time;
        View iv_alarm_setting;
        View iv_pick_time_setting;
        CartChekBox cb1;
        CartChekBox cb2;
        CartChekBox cb3;

        TextView tv_shop_price;
        ExpandableView order_list_container;

        boolean isAlarmSet;
        TextView tv_option_info;

        CartChekBox lastCb;

        View.OnClickListener timeEditListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderHolder holder = (OrderHolder) v.getTag();
                switch (v.getId()) {
                    case R.id.iv_alarm_setting:
                        holder.isAlarmSet = true;
                        break;
                    case R.id.iv_pick_time_setting:
                        holder.isAlarmSet = false;
                        break;
                }
                showDatePicker(holder);
            }
        };
        View.OnClickListener cbListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartChekBox cb = (CartChekBox) v;
                if (!cb.isChecked()) {
                    cb.setChecked(true);
                    lastCb.setChecked(false);
                    lastCb = cb;
                } else {
                    return;
                }

                switch (v.getId()) {
                    case R.id.cb1:
                        if (mOrderDining.diningType == CartCacheUtil.TYPE_OUT) {
                            tv_option_info.setText("立即：" + tv_option_1.getText().toString());
                        } else {
                            tv_option_info.setText("有效期：" + tv_option_1.getText().toString());
                        }
                        mOrderDining.deliveryType = 0;
                        break;
                    case R.id.cb2:
                        if (mOrderDining.diningType == CartCacheUtil.TYPE_OUT) {
                            tv_option_info.setText("定时：" + tv_alarm_time.getText().toString());
                        } else {
                            tv_option_info.setText("预约：" + tv_alarm_time.getText().toString());
                        }
                        mOrderDining.appointTime = mCusTimeSetListener.getSelectTimeStemp();
                        mOrderDining.deliveryType = 1;
                        break;
                    case R.id.cb3:
                        tv_option_info.setText("自提：" + tv_pick_time.getText().toString());
                        mOrderDining.deliveryType = 2;
                        mOrderDining.appointTime = mCusTimeSetListener.getSelectTimeStemp();
                        break;
                }
            }
        };

        private OrderHolder(View child, RequestBean.OrderDining orderDining) {
            mOrderDining = orderDining;

            tv_title = (TextView) child.findViewById(R.id.tv_title);

            View layout_distribute_option = child.findViewById(R.id.layout_option);
            tv_option_info = (TextView) layout_distribute_option.findViewById(R.id.tv_option_info);

            ExpandableView layout_distribute_container = (ExpandableView) child.findViewById(R.id.layout_option_container);
            layout_distribute_container.setExpand(false);
            layout_distribute_container.setTriggerView(layout_distribute_option);

            tv_option_1 = (TextView) layout_distribute_container.findViewById(R.id.tv_option_1);
            tv_alarm_time = (TextView) layout_distribute_container.findViewById(R.id.tv_alarm_time);
            tv_pick_time = (TextView) layout_distribute_container.findViewById(R.id.tv_pick_time);
            iv_alarm_setting = layout_distribute_container.findViewById(R.id.iv_alarm_setting);
            iv_pick_time_setting = layout_distribute_container.findViewById(R.id.iv_pick_time_setting);

            iv_alarm_setting.setOnClickListener(timeEditListener);
            if (iv_pick_time_setting != null)
                iv_pick_time_setting.setOnClickListener(timeEditListener);

            cb1 = (CartChekBox) layout_distribute_container.findViewById(R.id.cb1);
            cb1.setChecked(true);
            lastCb = cb1;
            cb1.setOnClickListener(cbListener);
            cb2 = (CartChekBox) layout_distribute_container.findViewById(R.id.cb2);
            cb2.setOnClickListener(cbListener);
            cb3 = (CartChekBox) layout_distribute_container.findViewById(R.id.cb3);
            if (cb3 != null)
                cb3.setOnClickListener(cbListener);


            View layout_order_detail = child.findViewById(R.id.layout_order_detail);
            tv_shop_price = (TextView) layout_order_detail.findViewById(R.id.tv_shop_price);
            order_list_container = (ExpandableView) child.findViewById(R.id.order_list_container);
            order_list_container.setTriggerView(layout_order_detail);

        }
    }


    @Override
    public int getContentRes() {
        return R.layout.fragment_order_confirm;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = ((CartActivity) getActivity());
    }


    @Override
    public void initViews() {
        showProgress(true);
        setTitleDisable();
        mOrderConfirmPresenter = new OrderConfirmPresenter(this);
        mAddressPresenter = new AddressListPresnter(this);
        mRequestBean = mOrderConfirmPresenter.getRequestBean();
        mAddContainer.setTriggerView(mAddInfo);
        mPayTypeContainer.setTriggerView(mLayoutPayType);
        mPayTypeContainer.setExpand(false);
        initPayType();
//        mPayTypeContainer.setEnabled(false);
        mBaseView.findViewById(R.id.tv_go_pay).setOnClickListener(this);
        mAddContainer.setExpand(false);
        mMyMoneyPresenter = new MyMoneyPresenter(this);
        initDatePicker();
    }


    private ViewGroup lastPayView;

    private void initPayType() {
//        private TextView tv_pay_name = (TextView) mPayTypeContainer.findViewById(R.id.tv_pay_name);

        View.OnClickListener payClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lastPayView != null && lastPayView.getId() == v.getId())
                    return;
                int payType = -1;
                String payName = null;
                switch (v.getId()) {
                    case R.id.layout_yue:
                        payType = 2;
                        payName = "余额支付";
                        break;
                    case R.id.layout_wechat:
                        payType = 0;
                        payName = "微信支付";
                        break;
                    case R.id.layout_ali:
                        payType = 1;
                        payName = "支付宝支付";
                        break;
                }

                mRequestBean.payInfo.pathName = payName;
                mRequestBean.payInfo.pathType = payType;
                mTvPayName.setText(payName);
                ViewGroup group = (ViewGroup) v;
                CartChekBox chekBox = (CartChekBox) group.getChildAt(group.getChildCount() - 1);
                chekBox.setChecked(!chekBox.isChecked());
                if (lastPayView != null) {
                    CartChekBox lastChekBox = (CartChekBox) lastPayView.getChildAt(lastPayView.getChildCount() - 1);
                    lastChekBox.setChecked(false);
                }
                lastPayView = (ViewGroup) v;
//                mPayTypeContainer.packUp();
            }
        };
        mPayTypeContainer.findViewById(R.id.layout_yue).setOnClickListener(payClickListener);
        mPayTypeContainer.findViewById(R.id.layout_wechat).setOnClickListener(payClickListener);
        mPayTypeContainer.findViewById(R.id.layout_ali).setOnClickListener(payClickListener);
        mPayTypeContainer.findViewById(R.id.layout_wechat).performClick();
    }

    View timeContent;
    AlertDialog mDialogDatePicker;
    DatePicker dateView;

    private void initDatePicker() {
        Calendar instance = Calendar.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mDialogDatePicker = builder.create();
        timeContent = View.inflate(getContext(), R.layout.dialog_date_picker, null);
        dateView = (DatePicker) timeContent.findViewById(R.id.date_view);
        timeContent.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogDatePicker.dismiss();
                if (mDialogTimePicker == null) {
                    initTimePicker();
                } else {
                    mDialogTimePicker.show();
                }
            }
        });
        dateView.init(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                mDialogDatePicker.dismiss();
//                if (mDialogTimePicker == null) {
//                    initTimePicker();
//                } else {
//                    mDialogTimePicker.show();
//                }
            }
        });
        initTimePicker();
        dateView.setMinDate(currentTimeMillis());
        dateView.setMaxDate(currentTimeMillis() + 1000 * 60 * 60 * 24 * 10);

    }

    private TimePicker mTimePicker;

    private Calendar initTimePicker() {
        Calendar instance = Calendar.getInstance();
        mCusTimeSetListener = new CusTimeSetListener();
//        mDialogTimePicker = new TimePickerDialog(getContext(), mCusTimeSetListener, instance.get(Calendar.HOUR), instance.get(Calendar.MINUTE), false);

        mDialogTimePicker = new SimpleDialog(getActivity(), R.layout.dialog_time_picker);
        mTimePicker = (TimePicker) mDialogTimePicker.getView().findViewById(R.id.time_view);
        mTimePicker.setOnTimeChangedListener(mCusTimeSetListener);
        mDialogTimePicker.getView().findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCusTimeSetListener.getSelectTimeStemp() < System.currentTimeMillis()) {
                    ToastUtils.showToast("不能早于当前时间");
                } else {
                    mCusTimeSetListener.setTime();
                    mDialogTimePicker.dismiss();
                }
            }
        });
        return instance;
    }

    private void showDatePicker(OrderHolder holder) {
        if (holder.mOrderDining.diningType == CartCacheUtil.TYPE_OUT) {
            mDialogTimePicker.show();
        } else {
            mDialogDatePicker.show();
            mDialogDatePicker.setContentView(timeContent);
        }
        mDialogDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCusTimeSetListener.setData(holder);
    }

    private class CusTimeSetListener implements TimePickerDialog.OnTimeSetListener, TimePicker.OnTimeChangedListener {
        private OrderHolder mHolder;
        private long timeStemp;

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            handleTimeChange(hourOfDay, minute);
        }

        private void handleTimeChange(int hourOfDay, int minute) {
            int year = dateView.getYear();
            int month = dateView.getMonth();
            int day = dateView.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hourOfDay, minute);
            timeStemp = calendar.getTimeInMillis();
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            handleTimeSet(hourOfDay, minute);
        }

        private void handleTimeSet(int hourOfDay, int minute) {
            int year = dateView.getYear();
            int month = dateView.getMonth();
            int day = dateView.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hourOfDay, minute);
            timeStemp = calendar.getTimeInMillis();
            String formatDate = formatDate(timeStemp);
            if (mHolder != null) {
                if (mHolder.isAlarmSet)
                    mHolder.tv_alarm_time.setText(formatDate);
                else
                    mHolder.tv_pick_time.setText(formatDate);
            }
        }

        public void setData(OrderHolder data) {
            mHolder = data;
        }

        public OrderHolder getHolder() {
            return mHolder;
        }

        public long getSelectTimeStemp() {
            if (timeStemp == 0)
                timeStemp = System.currentTimeMillis();
            return timeStemp;
        }


        public void setTime() {
            String formatDate = formatDate(timeStemp);
            if (mHolder != null) {
                if (mHolder.isAlarmSet)
                    mHolder.tv_alarm_time.setText(formatDate);
                else
                    mHolder.tv_pick_time.setText(formatDate);
            }
        }
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void initNet() {
        mMyMoneyPresenter.getWalleteInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_pay:
                submitOrder();
                break;
            case R.id.layout_remark:
                startActivityForResult(new Intent(getContext(), OrderRemarkActivity.class), REQUEST_REMARK);
                break;
        }
    }

    public void submitOrder() {
        mOrderConfirmPresenter.submint();
    }
}
