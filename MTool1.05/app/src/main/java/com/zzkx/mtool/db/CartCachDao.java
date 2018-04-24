package com.zzkx.mtool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zzkx.mtool.bean.MenuOptionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2018/2/6.
 */

public class CartCachDao {

    private Context mContext;

    public CartCachDao(Context context) {
        mContext = context;
    }


    public MenuBean getMenu(String menuId) {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_MENU, null, CartDbOpenHelper.COLUM_ID_MENU + " = ?", new String[]{String.valueOf(menuId)}, null, null, null);
        MenuBean menuBean = null;
        if (query.moveToNext()) {
            menuBean = parseMenu(query);
        }
        query.close();
        return menuBean;
    }

    public List<MenuBean> getMenus() {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_MENU, null, null, null, null, null, null);
        List<MenuBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            list.add(parseMenu(query));
        }
        query.close();
        return list;
    }

    public List<MenuBean> getMenus(String shopId) {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_MENU, null, CartDbOpenHelper.COLUM_ID_SHOP + " = ?", new String[]{String.valueOf(shopId)}, null, null, null);
        List<MenuBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            list.add(parseMenu(query));
        }
        query.close();
        return list;
    }

    public MenuOptionBean getMenuOption(String optionId) {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_SKU, null, CartDbOpenHelper.COLUM_ID_SKU + " = ?", new String[]{optionId}, null, null, null);
        MenuOptionBean menuOptionBean = null;
        if (query.moveToNext()) {
            menuOptionBean = parseOption(query);
        }
        query.close();
        return menuOptionBean;
    }

    public List<MenuOptionBean> getTotalMenuOptions(String shopId) {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_SKU, null, CartDbOpenHelper.COLUM_ID_SHOP + " = ?", new String[]{String.valueOf(shopId)}, null, null, null);
        List<MenuOptionBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            list.add(parseOption(query));
        }
        return list;
    }

    public List<MenuOptionBean> getMenuOptions() {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_SKU, null, null, null, null, null, null);
        List<MenuOptionBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            list.add(parseOption(query));
        }
        return list;
    }

    public List<MenuOptionBean> getMenuOptions(String menuId) {
        SQLiteDatabase db = getReadableDb();
        Cursor query = db.query(CartDbOpenHelper.TABLE_SKU, null, CartDbOpenHelper.COLUM_ID_MENU + " = ?", new String[]{String.valueOf(menuId)}, null, null, null);
        List<MenuOptionBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            list.add(parseOption(query));
        }
        return list;
    }

    private MenuOptionBean parseOption(Cursor query) {
        MenuOptionBean menuOptionBean = new MenuOptionBean();
        String optionId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_SKU));
        String menuId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_MENU));
        String menuName = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_MENU_NAME));
        String name = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_NAME));
        int outCount = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_COUNT_OUT));
        int inCount = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_COUNT_IN));
        double priceOut = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_PRICE_OUT));
        double priceIn = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_PRICE_IN));
        menuOptionBean.id = optionId;
        menuOptionBean.menuId = menuId;
        menuOptionBean.menuName = menuName;
        menuOptionBean.name = name;
        menuOptionBean.outCount = outCount;
        menuOptionBean.inCount = inCount;
        menuOptionBean.priceOut = priceOut;
        menuOptionBean.priceIn = priceIn;
        return menuOptionBean;
    }

    private MenuBean parseMenu(Cursor query) {
        MenuBean menuBean = new MenuBean();
        String shopId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_SHOP));
        String menuId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_MENU));
        String name = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_NAME));
        int outCount = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_COUNT_OUT));
        int inCount = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_COUNT_IN));
        double priceOut = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_PRICE_OUT));
        double priceIn = query.getInt(query.getColumnIndex(CartDbOpenHelper.COLUM_PRICE_IN));
        menuBean.shopId = shopId;
        menuBean.menuId = menuId;
        menuBean.name = name;
        menuBean.outCount = outCount;
        menuBean.inCount = inCount;
        menuBean.priceOut = priceOut;
        menuBean.priceIn = priceIn;
        return menuBean;
    }

    public void updateMenu(String menuid, ContentValues values) {
        SQLiteDatabase db = getWriteableDb();
        if (db.isOpen()) {
            db.update(CartDbOpenHelper.TABLE_MENU, values, CartDbOpenHelper.COLUM_ID_MENU + " = ?", new String[]{String.valueOf(menuid)});
        }
    }

    public void updateOption(String optionId, ContentValues values) {
        SQLiteDatabase db = getWriteableDb();
        if (db.isOpen()) {
            db.update(CartDbOpenHelper.TABLE_SKU, values, CartDbOpenHelper.COLUM_ID_SKU + " = ?", new String[]{String.valueOf(optionId)});
        }
    }

    public void saveOption(String shopId, String menuId, String skuId, String name, double priceOut, double priceIn, int outCount, int inCount) {
        SQLiteDatabase db = getWriteableDb();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(CartDbOpenHelper.COLUM_ID_SKU, skuId);
            values.put(CartDbOpenHelper.COLUM_ID_SHOP, shopId);
            values.put(CartDbOpenHelper.COLUM_ID_MENU, menuId);
            values.put(CartDbOpenHelper.COLUM_NAME, name);
            values.put(CartDbOpenHelper.COLUM_PRICE_OUT, priceOut);
            values.put(CartDbOpenHelper.COLUM_PRICE_IN, priceIn);
            values.put(CartDbOpenHelper.COLUM_COUNT_OUT, outCount);
            values.put(CartDbOpenHelper.COLUM_COUNT_IN, inCount);
            db.insert(CartDbOpenHelper.TABLE_SKU, null, values);
        }
    }

    public void saveMenu(String shopId, String menuId, String name, double priceOut, double priceIn, int outCount, int inCount) {
        SQLiteDatabase db = getWriteableDb();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(CartDbOpenHelper.COLUM_ID_SHOP, shopId);
            values.put(CartDbOpenHelper.COLUM_ID_MENU, menuId);
            values.put(CartDbOpenHelper.COLUM_NAME, name);
            values.put(CartDbOpenHelper.COLUM_PRICE_OUT, priceOut);
            values.put(CartDbOpenHelper.COLUM_PRICE_IN, priceIn);
            values.put(CartDbOpenHelper.COLUM_COUNT_OUT, outCount);
            values.put(CartDbOpenHelper.COLUM_COUNT_IN, inCount);
            db.insert(CartDbOpenHelper.TABLE_MENU, null, values);
        }
    }

    private SQLiteDatabase getReadableDb() {
        return CartDbOpenHelper.getInstance(mContext).getReadableDatabase();
    }

    private SQLiteDatabase getWriteableDb() {
        return CartDbOpenHelper.getInstance(mContext).getWritableDatabase();
    }

    public void resetMenuNum(String tableName, String shopId, String colum) {
        SQLiteDatabase db = getWriteableDb();
        Cursor query = db.query(tableName, null, CartDbOpenHelper.COLUM_ID_SHOP + " = ?", new String[]{shopId}, null, null, null);
        while (query.moveToNext()) {
            String menuId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_MENU));
            String skuId = null;
            try {
                skuId = query.getString(query.getColumnIndex(CartDbOpenHelper.COLUM_ID_SKU));
            }catch (Exception e){
                e.printStackTrace();
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(colum, 0);
            if (!TextUtils.isEmpty(skuId))
                updateOption(skuId, contentValues);
            else
                updateMenu(menuId, contentValues);
        }
        query.close();
    }
    public void clearDb(String tableName){
        SQLiteDatabase db = getWriteableDb();
        db.delete(tableName,null,null);
    }


}
