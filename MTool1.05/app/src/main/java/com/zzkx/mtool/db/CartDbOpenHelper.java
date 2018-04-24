package com.zzkx.mtool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sshss on 2018/2/6.
 */

public class CartDbOpenHelper extends SQLiteOpenHelper {
    private static CartDbOpenHelper instance;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "cart_cach.db";
    public static final String TABLE_MENU = "tb_menu";
    public static final String TABLE_SKU = "tb_sku";

    //share
    public static final String COLUM_ID_MENU = "menu_id";
    public static final String COLUM_NAME = "name";
    public static final String COLUM_COUNT_OUT = "count_out";
    public static final String COLUM_COUNT_IN = "count_in";
    public static final String COLUM_PRICE_OUT = "price_out";
    public static final String COLUM_PRICE_IN = "price_in";

    //tb_menu
    public static final String COLUM_ID_USER = "user_id";
    public static final String COLUM_ID_SHOP = "shop_id";

    //tb_sku
    public static final String COLUM_ID_SKU = "id";
    public static final String COLUM_ID_MENU_NAME = "menu_name";


    private CartDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static CartDbOpenHelper getInstance(Context context) {
        if (instance == null)
            instance = new CartDbOpenHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_MENU + " ("
                + COLUM_ID_USER + " TEXT, "
                + COLUM_ID_SHOP + " TEXT, "
                + COLUM_ID_MENU + " TEXT, "
                + COLUM_NAME + " TEXT, "
                + COLUM_COUNT_OUT + " INTEGER, "
                + COLUM_COUNT_IN + " INTEGER, "
                + COLUM_PRICE_OUT + " DOUBLE, "
                + COLUM_PRICE_IN + " DOUBLE); "
        );
        db.execSQL("CREATE TABLE "
                + TABLE_SKU + " ("
                + COLUM_ID_SKU + " TEXT, "
                + COLUM_ID_SHOP + " TEXT, "
                + COLUM_ID_MENU + " TEXT, "
                + COLUM_ID_MENU_NAME + " TEXT, "
                + COLUM_NAME + " TEXT, "
                + COLUM_COUNT_OUT + " INTEGER, "
                + COLUM_COUNT_IN + " INTEGER, "
                + COLUM_PRICE_OUT + " DOUBLE, "
                + COLUM_PRICE_IN + " DOUBLE); "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
