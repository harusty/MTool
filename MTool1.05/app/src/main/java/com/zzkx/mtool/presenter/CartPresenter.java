package com.zzkx.mtool.presenter;

import android.view.View;
import android.widget.Checkable;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.MenuOptionBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.db.MenuBean;
import com.zzkx.mtool.imple.OptionConfrimListenenr;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ICartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/9/2.
 */

public class CartPresenter extends BasePresenter<ICartView, MenuListBean> {
    private int mType;
    private CusMenuListBean mCusMenuListBean;
    private View.OnClickListener mOptionCtrlListener;
    private OptionConfrimListenenr mOptionConfrimListener;

    public CartPresenter(ICartView view, int type) {
        super(view);
        mType = type;
        initOptionCtrlListener();
        initOptionConfirmListener();
    }

    public void initTopOrderInfo() {
        mCusMenuListBean.cusTotalPrice = 0;
        mCusMenuListBean.totalMenuNum = 0;
        for (MenuListBean.DataBean dataBean : mCusMenuListBean.headerList) {
            dataBean.cusMenuCount = 0;
            dataBean.cusShopOrderPrice = 0;
            List<MenuListBean.FoodInfoListBean> foodInfoList = dataBean.foodInfoList;
            if (foodInfoList != null) {
                for (MenuListBean.FoodInfoListBean foodBean : foodInfoList) {
                    if (foodBean.cusIsChecked) {
                        mCusMenuListBean.cusTotalPrice += foodBean.cusMenuTotalPrice;
                        dataBean.cusShopOrderPrice += foodBean.cusMenuTotalPrice;
                        mCusMenuListBean.totalMenuNum += foodBean.cusCount;
                        dataBean.cusMenuCount += foodBean.cusCount;
                    }
                }
                if (dataBean.cusMenuCount > 0)
                    mCusMenuListBean.cusTotalPrice += dataBean.cusPeisong;
            }
        }
        getView().showTopOrderInfo(mCusMenuListBean.cusTotalPrice, mCusMenuListBean.totalMenuNum, mType);
    }

    private void initOptionCtrlListener() {
        mOptionCtrlListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Object tag = v.getTag();
                MenuListBean.DataBean dataBean;
                MenuListBean.FoodInfoListBean menuBean;
                List<MenuListBean.MenuOpiton> foodSkuList;
                if (tag != null) {
                    menuBean = (MenuListBean.FoodInfoListBean) v.getTag();
                    dataBean = mCusMenuListBean.headerList.get(menuBean.cusGroupPotision);
                    foodSkuList = menuBean.foodSkuList;
                } else {
                    ToastUtils.showToast("Null");
                    return;
                }
                switch (v.getId()) {
                    case R.id.iv_edit:
                        if (foodSkuList == null || foodSkuList.size() == 0) {
                            getView().showOption(menuBean, dataBean, true);
                        } else {
                            getView().showOption(menuBean, dataBean, false);
                        }
                        break;
                    case R.id.cb_check:
                        ((Checkable) v).toggle();
                        boolean checked = ((Checkable) v).isChecked();
                        menuBean.cusIsChecked = checked;
                        if (checked) {
                            dataBean.cusSelectCount++;
                            dataBean.cusMenuCount += menuBean.cusCount;
                            dataBean.cusShopOrderPrice += menuBean.cusMenuTotalPrice;
                        } else {
                            dataBean.cusSelectCount--;
                            dataBean.cusMenuCount -= menuBean.cusCount;
                            dataBean.cusShopOrderPrice -= menuBean.cusMenuTotalPrice;
                        }
                        initTopOrderInfo();
                        getView().notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    private List<MenuListBean.DataBean> mToRemoveData;

    @Override
    public void onSuccessM(MenuListBean bean) {
        getView().showProgress(false);
        if (mCusMenuListBean.headerList.size() > 0) {
            getView().showCartInfo(mCusMenuListBean, mOptionCtrlListener);
            initTopOrderInfo();
            getView().recordData(mCusMenuListBean.headerList, mType);
        }
        getView().setDataLoded();
    }

    @Override
    public void onSuccessWorkThread(MenuListBean bean) {
        mToRemoveData = new ArrayList<>();
        mCusMenuListBean = new CusMenuListBean();

        if (bean != null) {
            List<MenuListBean.DataBean> listData = bean.data;
            for (int i = 0; i < listData.size(); i++) {
                int groupCount = 0;
                MenuListBean.DataBean dataBean = listData.get(i);
                List<MenuListBean.FoodInfoListBean> foodInfoList = dataBean.foodInfoList;
                if (foodInfoList != null) {
                    //默认都选中
                    dataBean.cusOriginSelectCount = dataBean.cusSelectCount = dataBean.foodInfoList.size();
                    for (int j = 0; j < foodInfoList.size(); j++) {
                        MenuListBean.FoodInfoListBean foodBean = foodInfoList.get(j);
                        foodBean.cusGroupPotision = i;
//                        if (j == 0)
                        foodBean.cusParentBean = dataBean;
                        MenuBean cachMenu = CartCacheUtil.getInstance().getMenu(foodBean.id);
                        int menuCount = 0;
                        if (cachMenu != null) {
                            if (mType == CartCacheUtil.TYPE_OUT)
                                menuCount = cachMenu.outCount;
                            else
                                menuCount = cachMenu.inCount;
                            foodBean.cusCount = menuCount;
                            groupCount += menuCount;
                        }

                        List<MenuListBean.MenuOpiton> foodSkuList = foodBean.foodSkuList;

                        if (foodSkuList != null && foodSkuList.size() != 0) {
                            int menuOptionCount = 0;
                            foodBean.cusCount = 0;
                            for (int k = 0; k < foodSkuList.size(); k++) {
                                MenuListBean.MenuOpiton menuOpiton = foodSkuList.get(k);
                                MenuOptionBean cacheOptionBean = CartCacheUtil.getInstance().getOption(menuOpiton.id);

                                if (cacheOptionBean != null) {
                                    int count;
                                    double price;
                                    if (mType == CartCacheUtil.TYPE_OUT) {
                                        count = cacheOptionBean.outCount;
                                        price = menuOpiton.priceOut;
                                    } else {
                                        count = cacheOptionBean.inCount;
                                        price = menuOpiton.priceIn;
                                    }

                                    menuOpiton.cusCount = count;
                                    menuOptionCount += count;
                                    double m = price * count;
                                    foodBean.cusMenuTotalPrice += m;
                                }
                            }
                            foodBean.cusCount += menuOptionCount;
                            dataBean.cusMenuCount += menuOptionCount;
                        } else if (cachMenu != null && menuCount > 0) {
                            double menuPrice;
                            if (mType == CartCacheUtil.TYPE_OUT) {
                                menuPrice = menuCount * foodBean.priceOut;
                                cachMenu.priceOut = foodBean.priceOut;
                            } else {
                                menuPrice = menuCount * foodBean.priceIn;
                                cachMenu.priceIn = foodBean.priceIn;
                            }

                            foodBean.cusMenuTotalPrice += menuPrice;
                            dataBean.cusMenuCount += menuCount;
                        }
                        dataBean.cusShopOrderPrice += foodBean.cusMenuTotalPrice;
                    }
                }
                dataBean.cusGroupCount = groupCount;
//                dataBean.cusMenuCount = cachShop.count;
                double peisong = 0;
                if (mType == CartCacheUtil.TYPE_OUT) {
                    peisong = dataBean.allofee == null ? 0 : dataBean.allofee.startPrice;
                    dataBean.cusPeisong = peisong;
                }
                if (dataBean.cusShopOrderPrice >= dataBean.deliverAmount) {
                    mCusMenuListBean.totalMenuNum += dataBean.cusMenuCount;

                    mCusMenuListBean.cusTotalPrice += dataBean.cusShopOrderPrice + peisong;
                    mCusMenuListBean.totalPeisong += peisong;
                    mCusMenuListBean.headerList.add(dataBean);

                    if (foodInfoList != null) {
                        mCusMenuListBean.menuList.addAll(foodInfoList);

                        if (mCusMenuListBean.headerIndices.size() == 0) {
                            mCusMenuListBean.headerIndices.add(0);
                            mCusMenuListBean.headerIndices.add(foodInfoList.size());
                        } else if (i < listData.size() - 1) {
                            Integer value = mCusMenuListBean.headerIndices.get(i - 1);
                            mCusMenuListBean.headerIndices.add(foodInfoList.size() + value);
                        }
                    }
                } else {
                    mToRemoveData.add(dataBean);
                }
            }
            for (MenuListBean.DataBean dataBean : mToRemoveData) {
                mCusMenuListBean.headerList.remove(dataBean);
            }
        }
    }

    public void getListData() {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        List<String> ids = getIds();
        if (ids.size() == 0) {
            getView().setDataLoded();
            getView().showProgress(false);
        } else {
            requestBean.ids = ids;
            requestBean.type = mType;
            getHttpModel().request(API.CART_INFO, requestBean);
        }
    }

    private List<String> getIds() {
        List<String> ids = new ArrayList<>();
        List<MenuBean> menus = CartCacheUtil.getInstance().getMenus();
        for (MenuBean menuBean : menus) {
            if ((mType == CartCacheUtil.TYPE_OUT && menuBean.outCount > 0)
                    || (mType == CartCacheUtil.TYPE_IN && menuBean.inCount > 0))
                if (!ids.contains(menuBean.menuId))
                    ids.add(menuBean.menuId);
        }
        List<MenuOptionBean> options = CartCacheUtil.getInstance().getOptions();
        for (MenuOptionBean menuOptionBean : options) {
            if ((mType == CartCacheUtil.TYPE_OUT && menuOptionBean.outCount > 0)
                    || (mType == CartCacheUtil.TYPE_IN && menuOptionBean.inCount > 0))
                if (!ids.contains(menuOptionBean.menuId))
                    ids.add(menuOptionBean.menuId);
        }
        return ids;
    }

    private void initOptionConfirmListener() {
        mOptionConfrimListener = new OptionConfrimListenenr() {
            @Override
            public void onCofirm(Object object, int count) {
//                double price = (double) object;
//                mCusMenuListBean.cusTotalPrice += price;
//                mCusMenuListBean.totalMenuNum += count;
                getView().notifyDataSetChanged();
                initTopOrderInfo();
            }
        };
    }

    public OptionConfrimListenenr getOptionConfrimListener() {
        return mOptionConfrimListener;
    }
}
