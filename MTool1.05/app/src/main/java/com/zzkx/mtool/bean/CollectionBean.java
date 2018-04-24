package com.zzkx.mtool.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/6/28.
 */

public class CollectionBean extends BaseBean {

    public List<DataBean> data;
    /**
     * id : 111111
     * memId : 15053540843296
     * searchName : 默认分类
     * type : 1
     * createTime : 1508138835000
     * memberGoodsCollectDo : [{"id":15081525263305,"goodsImg":"http://47.93.91.166:8080/fileService/uploads/2016/07/25/214694360003012.png","goodsName":"苹果","goodsPrice":22,"type":0,"goodsId":15053619944511,"foodInfo":{"monthlySales":0,"praises":0,"shopName":"金粥城"}}]
     */

    public int id;
    public long memId;
    public String name;
    public int type;
    public long createTime;
    public List<MemberGoodsCollectDoBean> memberGoodsCollectDo;

    public static class DataBean {

        public String id;
        public String memId;
        public String name;
        public int type;
        public long createTime;
        public List<MerchantRestaurantsListBean> merchantRestaurantsList;
        public List<MemberGoodsCollectDoBean> memberGoodsCollectDo;
        public List<ForumPostDos> forumPostDos;

    }

    public static class ForumPostDos extends BaseCollectBean {
        public long createTime;
        public String content;
        public UserMemberBean userMember;
        public ArrayList<StateListBean.ResData> forumThreadResources;

        public int shareType;
        public String firstName;
        public String firstId;
        public String share;
        public List<StateListBean.UserMemberBean> shareuserMemberList;
        public List<StateListBean.UserMemberBean> userMemberList;
    }

    public static class UserMemberBean {
        public String nickname;
        public String picUrl;
    }

    public static class MerchantRestaurantsListBean extends BaseCollectBean {

        public String name;
        public String logoUrl;
        public int priceScore;
        public int serviceScore;
        public String avgConsume;
        public double latitude;
        public double longitude;
        public int status;
        public int deliverAmount;
        public UserMemberAccountBean userMemberAccount;
        public AllofeeBean allofee;

        public String description;
    }

    public static class MemberGoodsCollectDoBean extends BaseCollectBean {

        public String goodsImg;
        public String goodsName;
        public String goodsPrice;
        public String goodsPriceOut;
        public int type;
        public FoodInfoBean foodInfo;
    }

    public static class UserMemberAccountBean {
        public String cashPledge;
    }

    public static class AllofeeBean {
        public String startPrice;
    }


    public static class FoodInfoBean {

        public String monthlySales;
        public String praises;
        public String shopName;
    }
}
