package com.zzkx.mtool.presenter;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.MenuOptionBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.db.MenuBean;
import com.zzkx.mtool.imple.BottomCartCtrlListener;
import com.zzkx.mtool.imple.OptionConfrimListenenr;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.MenuDetailActivity;
import com.zzkx.mtool.view.fragment.ShopFragment;
import com.zzkx.mtool.view.iview.IShopView;

import java.util.List;

/**
 * Created by sshss on 2017/8/28.
 */

public class ShopPresenter extends BasePresenter<IShopView, MenuListBean> {


    private String mShopId;
    private View.OnClickListener mOnClickListener;
    private OptionConfrimListenenr mOptionConfrimListenenr;
    private CusMenuListBean mCusMenuListBean;
    private BottomCartCtrlListener mBottomCartCtrlListener;
    private int mType;

    public ShopPresenter(IShopView view) {
        super(view);
        //规格弹框加减
        initOptionCtrlListener();
        //规格弹框确定
        initOptionConfirmListener();
        initBottomCartCtrl();
    }

    private void initBottomCartCtrl() {
        mBottomCartCtrlListener = new BottomCartCtrlListener() {
            @Override
            public void onAdd(Object tag) {
                handleBottomCartChange(tag, true);
            }

            @Override
            public void onSubtract(Object tag) {
                handleBottomCartChange(tag, false);
            }

            @Override
            public void onClearShopCache() {
                CartCacheUtil.getInstance().clearShopMenus(mShopId, mType);
                mCusMenuListBean.cusTotalPrice = 0;
                for (MenuListBean.DataBean bean : mCusMenuListBean.headerList) {
                    bean.cusGroupCount = 0;
                    bean.cusShopOrderPrice = 0;
                }
                for (MenuListBean.FoodInfoListBean bean : mCusMenuListBean.menuList) {
                    bean.cusCount = 0;
                    bean.cusMenuTotalPrice = 0;
                    List<MenuListBean.MenuOpiton> foodSkuList = bean.foodSkuList;
                    for (MenuListBean.MenuOpiton optionBea : foodSkuList) {
                        optionBea.cusCount = 0;
                    }
                }
                getView().notifyDataSetChanged();
                initCartPriceInfo();
            }
        };
    }

    private void handleBottomCartChange(Object tag, boolean onAdd) {
        List<MenuListBean.DataBean> headerList = mCusMenuListBean.headerList;
        List<MenuListBean.FoodInfoListBean> menuList = mCusMenuListBean.menuList;
        int offset = onAdd ? 1 : -1;
        if (tag instanceof MenuBean) {
            String id = ((MenuBean) tag).menuId;
            MenuListBean.DataBean databean = null;
            for (MenuListBean.FoodInfoListBean bean : menuList) {
                if (id.equals(bean.id)) {
                    if (databean == null) {
                        databean = headerList.get(bean.cusGroupPotision);
                        databean.cusGroupCount += offset;
                    }
                    bean.cusCount += offset;
                    if (bean.cusCount < 0) {
                        bean.cusCount = 0;
                        break;
                    }
                    if (mType == CartCacheUtil.TYPE_OUT)
                        mCusMenuListBean.cusTotalPrice += bean.priceOut * offset;
                    else
                        mCusMenuListBean.cusTotalPrice += bean.priceIn * offset;
                    CartCacheUtil.getInstance().saveMenu(bean, mType);
                    initCartPriceInfo();
                    getView().notifyDataSetChanged();
                    break;
                }
            }
        } else {
            String id = ((MenuOptionBean) tag).id;
            flag:
            for (MenuListBean.FoodInfoListBean bean : menuList) {
                MenuListBean.DataBean databean = headerList.get(bean.cusGroupPotision);

                List<MenuListBean.MenuOpiton> foodSkuList = bean.foodSkuList;
                if (foodSkuList != null && foodSkuList.size() > 0) {
                    for (MenuListBean.MenuOpiton optionBean : foodSkuList) {
                        if (id.equals(optionBean.id)) {

                            optionBean.cusCount += offset;
                            if (optionBean.cusCount < 0) {
                                optionBean.cusCount = 0;
                                break flag;
                            }
                            databean.cusGroupCount += offset;
                            bean.cusCount += offset;
                            if (mType == CartCacheUtil.TYPE_OUT)
                                mCusMenuListBean.cusTotalPrice += optionBean.priceOut * offset;
                            else
                                mCusMenuListBean.cusTotalPrice += optionBean.priceIn * offset;
                            CartCacheUtil.getInstance().saveOption(optionBean, mType);
                            initCartPriceInfo();
                            getView().notifyDataSetChanged();
                            break flag;
                        }
                    }
                }
            }
        }
    }

    public void getFoodMenu(String id, int type) {
        mShopId = id;
        mType = type;
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.foodInfo = new RequestBean.FoodInfo();
        requestBean.foodInfo.restaurantsId = id;
        requestBean.foodInfo.type = type;
        getHttpModel().request(API.MENU_LIST, requestBean);


    }

    private void initOptionConfirmListener() {
        mOptionConfrimListenenr = new OptionConfrimListenenr() {
            @Override
            public void onCofirm(Object object, int count) {
                double price = (double) object;
//                mCusMenuListBean.cusTotalPrice += price;
                initCartPriceInfo();
                getView().notifyDataSetChanged();
            }
        };
    }

    public OptionConfrimListenenr getOptionConfrimListener() {
        return mOptionConfrimListenenr;
    }

    private void initOptionCtrlListener() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                MenuListBean.DataBean dataBean;
                MenuListBean.FoodInfoListBean bean;
                List<MenuListBean.MenuOpiton> foodSkuList;
                if (tag != null) {
                    bean = (MenuListBean.FoodInfoListBean) v.getTag();
                    dataBean = mCusMenuListBean.headerList.get(bean.cusGroupPotision);
                    foodSkuList = bean.foodSkuList;
                } else {
                    ToastUtils.showToast("Null");
                    return;
                }
                MenuBean menu = CartCacheUtil.getInstance().getMenu(bean.id);
                switch (v.getId()) {
                    case R.id.iv_add:
                        if (foodSkuList == null || foodSkuList.size() == 0) {
                            bean.cusCount++;
                            CartCacheUtil.getInstance().saveMenu(bean, mType);
                            dataBean.cusGroupCount++;
                            if (mType == CartCacheUtil.TYPE_OUT)
                                mCusMenuListBean.cusTotalPrice += bean.priceOut;
                            else
                                mCusMenuListBean.cusTotalPrice += bean.priceIn;
                            getView().notifyDataSetChanged();
                            initCartPriceInfo();
                        } else {
                            getView().showOption(bean, dataBean);
                        }

                        break;
                    case R.id.iv_minus:
                        if (foodSkuList == null || foodSkuList.size() == 0) {
                            if (bean.cusCount > 0) {
                                bean.cusCount--;
                                dataBean.cusGroupCount--;
                                CartCacheUtil.getInstance().saveMenu(bean, mType);
                                if (mType == CartCacheUtil.TYPE_OUT)
                                    mCusMenuListBean.cusTotalPrice -= bean.priceOut;
                                else
                                    mCusMenuListBean.cusTotalPrice -= bean.priceIn;
                                initCartPriceInfo();
                            }
                        } else {
                            getView().showOption(bean, dataBean);
                        }
                        getView().notifyDataSetChanged();
                        break;
                }
            }
        };
    }


    private void initCartPriceInfo() {
        double price = mCusMenuListBean.cusTotalPrice;
        int count = 0;
        for (MenuListBean.DataBean databen : mCusMenuListBean.headerList) {
            count += databen.cusGroupCount;
            price += databen.cusShopOrderPrice;
        }
        getView().showCartInfo(count, price, mType);
    }

    @Override
    public void onSuccessM(MenuListBean bean) {
        getView().showProgress(false);
//        if (mCusMenuListBean.headerList.size() == 0)
//            return;
        initCartPriceInfo();
        getView().shoMenulist(mCusMenuListBean, mOnClickListener);
        getView().setBottomCtrlListener(mBottomCartCtrlListener, mType);
    }

    /**
     * 添加gropPosition
     * 初始化缓存
     *
     * @param bean
     */
    @Override
    public void onSuccessWorkThread(MenuListBean bean) {
        if (mCusMenuListBean != null) {
            mCusMenuListBean.headerList.clear();
            mCusMenuListBean.headerIndices.clear();
            mCusMenuListBean.menuList.clear();
            mCusMenuListBean.totalMenuNum = 0;
            mCusMenuListBean.cusTotalPrice = 0;
        } else {
            mCusMenuListBean = new CusMenuListBean();
        }
        if (bean != null) {
            List<MenuListBean.DataBean> listData = bean.data;
            double totalCartPrice = 0;
            for (int i = 0; i < listData.size(); i++) {
                int groupCount = 0;
                MenuListBean.DataBean dataBean = listData.get(i);
                List<MenuListBean.FoodInfoListBean> foodInfoList = dataBean.foodInfoList;
                mCusMenuListBean.headerList.add(dataBean);
                if (foodInfoList != null) {
                    if (i == 0) {
                        mCusMenuListBean.headerIndices.add(0);
                        mCusMenuListBean.headerIndices.add(foodInfoList.size());
                    } else if (i < listData.size() - 1) {
                        Integer value = mCusMenuListBean.headerIndices.get(i - 1);
                        mCusMenuListBean.headerIndices.add(foodInfoList.size() + value);
                    }
                    dataBean.cusGroupPotision = i;
                    for (int j = 0; j < foodInfoList.size(); j++) {
                        MenuListBean.FoodInfoListBean foodBean = foodInfoList.get(j);
                        mCusMenuListBean.menuList.add(foodBean);
                        foodBean.cusGroupPotision = i;
                        MenuBean cachMenu = CartCacheUtil.getInstance().getMenu(foodBean.id);
                        if (cachMenu != null) {
                            if (mType == CartCacheUtil.TYPE_OUT) {
                                foodBean.cusCount = cachMenu.outCount;
                            } else {
                                foodBean.cusCount = cachMenu.inCount;
                            }
                            CartCacheUtil.getInstance().updateMenuInfo(foodBean.id, foodBean.name, foodBean.priceOut, foodBean.priceIn);
                        }

                        List<MenuListBean.MenuOpiton> foodSkuList = foodBean.foodSkuList;
                        if (foodSkuList != null && foodSkuList.size() != 0) {
                            foodBean.cusCount = 0;
                            for (int k = 0; k < foodSkuList.size(); k++) {
                                MenuListBean.MenuOpiton menuOpiton = foodSkuList.get(k);
                                menuOpiton.cusShopId = mShopId;
                                menuOpiton.cusMenuName = foodBean.name;
                                MenuOptionBean cacheOptionBean = CartCacheUtil.getInstance().getOption(menuOpiton.id);
                                if (cacheOptionBean != null) {
                                    double m;
                                    if (mType == CartCacheUtil.TYPE_OUT) {
                                        foodBean.cusCount += cacheOptionBean.outCount;
                                        menuOpiton.cusCount = cacheOptionBean.outCount;
                                        m = menuOpiton.priceOut * cacheOptionBean.outCount;
                                    } else {
                                        foodBean.cusCount += cacheOptionBean.inCount;
                                        menuOpiton.cusCount = cacheOptionBean.inCount;
                                        m = menuOpiton.priceIn * cacheOptionBean.inCount;
                                    }
                                    totalCartPrice += m;
                                    foodBean.cusMenuTotalPrice += m;
                                    CartCacheUtil.getInstance().updateOptionInfo(menuOpiton.id, menuOpiton.spec, menuOpiton.priceOut, menuOpiton.priceIn);
                                }
                            }
                        } else if (cachMenu != null) {
                            double menuPrice;
                            if (mType == CartCacheUtil.TYPE_OUT)
                                menuPrice = cachMenu.outCount * foodBean.priceOut;
                            else
                                menuPrice = cachMenu.inCount * foodBean.priceIn;

                            totalCartPrice += menuPrice;
                            foodBean.cusMenuTotalPrice += menuPrice;

                        }
                        groupCount += foodBean.cusCount;
                    }
                }
                dataBean.cusGroupCount = groupCount;
            }
            mCusMenuListBean.cusTotalPrice = totalCartPrice;
        }
    }


    public void onDestroy() {
        getHttpModel().cancleAll();
    }

    private int mClickPosition;

    public void onItemClick(ShopFragment shopFragment, int position) {
        mClickPosition = position;
        MenuListBean.FoodInfoListBean foodInfoListBean = mCusMenuListBean.menuList.get(position);
        shopFragment.startActivityForResult(new Intent(getView().getContext(), MenuDetailActivity.class)
                        .putExtra(Const.ID, foodInfoListBean.id)
                        .putExtra(Const.TYPE, mType),
                1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            MenuListBean.FoodInfoListBean menuBean = (MenuListBean.FoodInfoListBean) data.getSerializableExtra(Const.MENU_BEAN);
            MenuListBean.FoodInfoListBean foodInfoListBean = mCusMenuListBean.menuList.get(mClickPosition);
            double prePrice = foodInfoListBean.cusMenuTotalPrice;
            int preCount = foodInfoListBean.cusCount;
            mCusMenuListBean.headerList.get(foodInfoListBean.cusGroupPotision).cusGroupCount += menuBean.cusCount - preCount;
            menuBean.foodImages = foodInfoListBean.foodImages;
            menuBean.foodSkuList = menuBean.foodSkus;
            menuBean.foodSkus = null;
            mCusMenuListBean.menuList.remove(mClickPosition);
            mCusMenuListBean.menuList.add(mClickPosition, menuBean);
            double offsetPrice = menuBean.cusMenuTotalPrice;
            menuBean.cusMenuTotalPrice += offsetPrice;
            mCusMenuListBean.cusTotalPrice += offsetPrice;
            getView().notifyDataSetChanged();
            initCartPriceInfo();
        }
    }
}
