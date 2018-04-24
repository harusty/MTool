package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/10/26.
 */

public class CollectionSearchBean extends BaseBean {

    public List<Object> cusData;
    public DataBean data;

    public static class DataBean {
        public List<ShopListBean> shopList;
        public List<GoodsListBean> goodsList;
        public List<ForumPostCollectListBean> forumPostCollectList;


    }
    public static class ShopListBean {

        public String shopId;
        public String shopName;
        public int shopStatus;
        public MerchantRestaurantsDoBean merchantRestaurantsDo;

    }
    public static class MerchantRestaurantsDoBean {

        public String id;
        public String name;
        public String logoUrl;
        public String description;
        public int priceScore;
        public int serviceScore;
        public double latitude;
        public double longitude;
        public int status;
        public String type;
        public String deposit;
        public String avgConsume;
        public String deliverAmount;
        public String toHomeTip;
    }

    public static class GoodsListBean {

        public long id;
        public String goodsImg;
        public String goodsName;
        public int goodsPrice;
        public int type;
        public String goodsId;
        public FoodInfoBean foodInfo;
        public String shopName;


    }
    public static class FoodInfoBean {

        public int monthlySales;
        public int praises;
    }

    public static class ForumPostCollectListBean {
        public StateListBean.DataBean forumPostDo;
    }
}
