package com.zzkx.mtool.bean;

// FIXME generate failure  field _$Data141

import java.io.Serializable;
import java.util.List;

/**
 * Created by sshss on 2017/8/28.
 */

public class MenuListBean extends BaseBean {


    public List<DataBean> data;
    public double cusTotalPrice;

    public static class DataBean extends BaseIdBean {

        public long restaurantsId;
        public String groupName;
        public String sortNo;
        public long createTime;
        public List<FoodInfoListBean> foodInfoList;
        public int cusGroupCount;

        public String logoUrl;
        public String name;
        public int cusSelectCount;
        public int cusOriginSelectCount;
        public double cusShopOrderPrice;
        public Allofee allofee;

        public double deliverAmount;
        public int activeTime;//有效时间
        public String storeId;
        public String merchantUserId;
        public String linkman;
        public String hotline;
        public double latitude;
        public double longitude;
        public String address;
        public int auditStatus;
        public int priceScore;
        public int serviceScore;
        public String avgConsume;
        public String startingPrice;
        public String description;
        public String distance;
        public double cusPeisong;
        public int cusMenuCount;
    }

    public static class Allofee {
        public String id;
        public String startScope;
        public double startPrice;
        public String stepPrice;
        public String defaultValue;
    }

    public static class MenuOpiton implements Serializable {
        public String id;
        public String foodId;
        public String spec;
        public double priceOut;
        public double priceIn;
        public int cusCount;
        public int cusOutCount;
        public String cusShopId;
        public String cusMenuName;
    }

    public static class FoodInfoListBean extends BaseIdBean {
        public int cusCount;//自用字段
        public int cusOutCount; //外送数量
        public boolean cusIsChecked = true;
        public double cusMenuTotalPrice;


        public String restaurantsId;
        public String name;
        public double priceIn;
        public double priceOut;
        public int makeTime;
        public int type;
        public int monthlySales;
        public int praises;
        public int comments;
        public int grossScore;
        public int isFeatured;
        public int isNew;
        public int isFormat;
        public int stock;
        public int boxPrice;
        public String unit;
        public String description;
        public int status;
        public long createTime;
        public List<MenuOpiton> foodSkuList;//规格集合
        public List<MenuOpiton> foodSkus;//规格集合
        public List<FoodImage> foodImageDos;
        public FoodImage foodImages;
        public DataBean cusParentBean;
        public int collectType;

        public String goodsId;
    }

    public static class FoodImage implements Serializable {
        public String imgUrl;
    }
}
