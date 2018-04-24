package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/28.
 */

public class ShopDetailBean extends BaseListBean {


    public DataBean data;

    @Override
    public List getData() {
        return data.forumPostDo;
    }

    public static class DataBean {

        public int flag;
        public int attentionFlag;
        public List<StateListBean.DataBean> forumPostDo;
        public MerchantRestaurantsDoBean merchantRestaurantsDo;
    }

//    public static class ForumPostDoBean {
//
//        public String id;
//        public String forumId;
//        public String threadId;
//        public String memId;
//        public long createTime;
//        public String content;
//        public int supports;
//        public UserMemberBean userMember;
//        public String now;
//        public int commNum;
//
//        public static class UserMemberBean {
//            public String nickname;
//            public String picUrl;
//        }
//    }


    public static class MerchantRestaurantsDoBean {

        public String name;
        public String storeId;
        public String linkman;
        public String hotline;
        public String cityName;
        public String address;
        public String logoUrl;
        public String description;
        public int priceScore;
        public int serviceScore;
        public String avgConsume;
        public int deliveryModel;
        public int takeOutSwitch;
        public int takeOutService;// 外卖服务是否有   1有 0无
        public int takeInService;//到店服务是否有   1有 0无
        public double latitude;
        public double longitude;
        public double deliverAmount;//起送价
        public String toHomeTip;//配送费

    }
}
