package com.zzkx.mtool.util;

import android.content.ContentValues;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.bean.MenuOptionBean;
import com.zzkx.mtool.db.CartCachDao;
import com.zzkx.mtool.db.CartDbOpenHelper;
import com.zzkx.mtool.db.MenuBean;

import java.util.List;

/**
 * Created by sshss on 2017/8/28.
 * 持久缓存：商品已选数量、id、名字、价格，其中名字和价格在每次请求店铺首页接口时需要重新同步缓存。
 * 清空缓存：需要同步从接口请求下来的数据的分类数量、商品数量、规格数量、购物车总价清零。（重置cusXXXX字段）
 */

public class CartCacheUtil {
    public static final int TYPE_IN = 1;
    public static final int TYPE_OUT = 0;
    private final CartCachDao mCartCachDao;
    private static CartCacheUtil sInstance;

    private CartCacheUtil() {
        mCartCachDao = new CartCachDao(MyApplication.getContext());
    }

    public static CartCacheUtil getInstance() {
        if (sInstance == null)
            sInstance = new CartCacheUtil();
        return sInstance;
    }


    public void saveMenu(MenuListBean.FoodInfoListBean menuBean, int type) {
        MenuBean menu = mCartCachDao.getMenu(menuBean.id);
        int outCount = 0;
        int inCount = 0;
        String colum;
        if (type == TYPE_OUT) {
            colum = CartDbOpenHelper.COLUM_COUNT_OUT;
            outCount = menuBean.cusCount;
        } else {
            colum = CartDbOpenHelper.COLUM_COUNT_IN;
            inCount = menuBean.cusCount;
        }

        if (menu == null) {
            mCartCachDao.saveMenu(menuBean.restaurantsId, menuBean.id, menuBean.name, menuBean.priceOut, menuBean.priceIn, outCount, inCount);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(colum, menuBean.cusCount);
            contentValues.put(CartDbOpenHelper.COLUM_NAME, menuBean.name);
            contentValues.put(CartDbOpenHelper.COLUM_PRICE_OUT, menuBean.priceOut);
            contentValues.put(CartDbOpenHelper.COLUM_PRICE_IN, menuBean.priceIn);
            mCartCachDao.updateMenu(menuBean.id, contentValues);
        }
    }

    public void saveOption(MenuListBean.MenuOpiton menuOpiton, int type) {
        MenuOptionBean optionBean = mCartCachDao.getMenuOption(menuOpiton.id);
        int outCount = 0;
        int inCount = 0;
        String colum;
        if (type == TYPE_OUT) {
            colum = CartDbOpenHelper.COLUM_COUNT_OUT;
            outCount = menuOpiton.cusCount;
        } else {
            colum = CartDbOpenHelper.COLUM_COUNT_IN;
            inCount = menuOpiton.cusCount;
        }

        if (optionBean == null) {
            mCartCachDao.saveOption(menuOpiton.cusShopId, menuOpiton.foodId, menuOpiton.id, menuOpiton.spec, menuOpiton.priceOut, menuOpiton.priceIn, outCount, inCount);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(colum, menuOpiton.cusCount);
            contentValues.put(CartDbOpenHelper.COLUM_NAME, menuOpiton.spec);
            contentValues.put(CartDbOpenHelper.COLUM_ID_MENU_NAME, menuOpiton.cusMenuName);
            contentValues.put(CartDbOpenHelper.COLUM_PRICE_OUT, menuOpiton.priceOut);
            contentValues.put(CartDbOpenHelper.COLUM_PRICE_IN, menuOpiton.priceIn);
            mCartCachDao.updateOption(menuOpiton.id, contentValues);
        }
    }


    public void updateMenuInfo(String menuId, String name, double priceOut, double priceIn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartDbOpenHelper.COLUM_ID_MENU, menuId);
        contentValues.put(CartDbOpenHelper.COLUM_NAME, name);
        contentValues.put(CartDbOpenHelper.COLUM_PRICE_OUT, priceOut);
        contentValues.put(CartDbOpenHelper.COLUM_PRICE_IN, priceIn);
        mCartCachDao.updateMenu(menuId, contentValues);
    }

    public void updateMenuCount(String menuId, int count, int type) {
        ContentValues contentValues = new ContentValues(1);
        String colume;
        if (type == TYPE_OUT) {
            colume = CartDbOpenHelper.COLUM_COUNT_OUT;
        } else {
            colume = CartDbOpenHelper.COLUM_COUNT_IN;
        }
        contentValues.put(colume, count);
        mCartCachDao.updateMenu(menuId, contentValues);
    }

    public void updateOptionInfo(String skuId, String name, double priceOut, double priceIn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartDbOpenHelper.COLUM_NAME, name);
        contentValues.put(CartDbOpenHelper.COLUM_PRICE_OUT, priceOut);
        contentValues.put(CartDbOpenHelper.COLUM_PRICE_IN, priceIn);
        mCartCachDao.updateOption(skuId, contentValues);
    }

    public void updateOptionCount(String skuId, int count, int type) {
        ContentValues contentValues = new ContentValues(1);
        String colume;
        if (type == TYPE_OUT) {
            colume = CartDbOpenHelper.COLUM_COUNT_OUT;
        } else {
            colume = CartDbOpenHelper.COLUM_COUNT_IN;
        }
        contentValues.put(colume, count);
        mCartCachDao.updateOption(skuId, contentValues);
    }

    public List<MenuBean> getMenus() {
        return mCartCachDao.getMenus();
    }
    public List<MenuBean> getMenus(String shopId) {
        return mCartCachDao.getMenus(shopId);
    }

    public MenuBean getMenu(String menuId) {
        return mCartCachDao.getMenu(menuId);
    }

    public List<MenuOptionBean> getTotalOptions(String shopId) {
        return mCartCachDao.getTotalMenuOptions(shopId);
    }

    public List<MenuOptionBean> getOptions() {
        return mCartCachDao.getMenuOptions();
    }
    public List<MenuOptionBean> getOptions(String menuId) {
        return mCartCachDao.getMenuOptions(menuId);
    }

    public MenuOptionBean getOption(String skuId) {
        return mCartCachDao.getMenuOption(skuId);
    }


    public void clearShopMenus(String shopId, int type) {
        if (type == CartCacheUtil.TYPE_OUT) {
            mCartCachDao.resetMenuNum(CartDbOpenHelper.TABLE_MENU, shopId, CartDbOpenHelper.COLUM_COUNT_OUT);
            mCartCachDao.resetMenuNum(CartDbOpenHelper.TABLE_SKU, shopId, CartDbOpenHelper.COLUM_COUNT_OUT);
        } else {
            mCartCachDao.resetMenuNum(CartDbOpenHelper.TABLE_MENU, shopId, CartDbOpenHelper.COLUM_COUNT_IN);
            mCartCachDao.resetMenuNum(CartDbOpenHelper.TABLE_SKU, shopId, CartDbOpenHelper.COLUM_COUNT_IN);
        }
    }
    public void clearCache(){
        mCartCachDao.clearDb(CartDbOpenHelper.TABLE_MENU);
        mCartCachDao.clearDb(CartDbOpenHelper.TABLE_SKU);
    }
}
