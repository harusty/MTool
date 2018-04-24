package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/14.
 */

public class OrderDetailBean extends BaseBean {


    public DataBean data;

    public static class DataBean {

        public double totalPrice;
        public long appointTime;
        public long createTime;
        public String consignee;
        public String consigneeAddr;
        public String deliveryGeo;
        public String consigneePhone;
        public String courierId;
        public String courierName;
        public String courierPhone;
        public int status;
        public int deliveryMethod;//0平台，1商家自己

        public MerchantRestaurantsBean merchantRestaurants;
        public List<OrderDetailListBean> orderDetailSkuList;
        public ExpressageCourier expressageCourier;
        public String shopId;
        public String pathName;
        public String id;
        public String qrCode;
        public String headingCode;

        public static class ExpressageCourier {
            public String id;
            public String picUrl;
            public String nickname;
        }

        public static class MerchantRestaurantsBean {

            public String storeId;
            public String name;
            public String hotline;
            public String address;
            public String logoUrl;
        }

        public static class OrderDetailListBean {

            public String goodsId;
            public String goodsUrl;
            public String goodsName;
            public int price;
            public int quantity;
            public FoodSku foodSku;

        }

        public static class FoodSku {
            public String spec;
            public String id;
        }
    }
}
