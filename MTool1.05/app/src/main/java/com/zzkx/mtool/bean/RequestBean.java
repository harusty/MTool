package com.zzkx.mtool.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/6/23.
 */

public class RequestBean {

    public String username;
    public String password;
    public Integer pageNum;
    public Integer numPerPage;
    public FoodInfo foodInfo;
    public List<String> ids;
    public Integer type;
    public String id;

    public PayInfoBean payInfo;
    public List<OrderDining> orderDiningList;
    public AddressListBean.AddressBean memberReceiveAddr;
    public String consigneeId;//收货地址id
    public RequestBean orderDining;
    public RequestBean forumPost;

    public MerchantRestaurants merchantRestaurants;
    public OrderRequestBean merchantRestaurantsComment;
    public OrderRequestBean expressageCourierComment;
    public List<OrderRequestBean> merchantRestaurantsCommentImgs;
    public List<OrderRequestBean> foodComment;
    public List<String> resourceUrls;
    public String content;
    public String parentId;
    public String postId;
    public RequestBean forumPostComment;
    public RequestBean memberShopCollect;
    public String shopId;
    public String phone;
    public String code;
    public String newPassword;
    public String nickname;
    public String picUrl;
    public Integer sex;
    public Long birthday;
    public String introduction;
    public String goodsId;
    public String goodsImage;
    public Double goodsPrice;
    public RequestBean memberGoodsCollect;
    public Integer page;
    public RequestBean memberCollectCatalog;
    public String name;
    public RequestBean memberCollectDetail;
    public String collectId;
    public List<String> catalogIds;
    public RequestBean forumPostCollect;
    public RequestBean forumPostSuppopp;
    public String userAddr;
    public String keyWord;
    public Integer follow;
    public List<String> hxNames;
    public Object keyword;
    public RequestBean userMember;
    public String idolId;
    public String hxUsername;
    public String friend2Id;
    public Integer speak;
    public Integer friend2Switch;
    public Integer friend1Switch;
    public Integer shield;
    public String fileName;
    public String title;
    public String fatherId;
    public String videoId;
    public String labelId;
    public List<String> memId;
    public List<String> hxUsernames;
    public String groupId;
    public Integer autoAddFans;
    public Integer passivityChat;
    public Integer strangerSeeStatus;
    public Integer fansSeeStatus;
    public Integer togetherStatus;
    public Integer tmp10;
    public RequestBean userMemberSeting;
    public List<String> labelIds;
    public String memberId;
    public String totalPrice;
    public Integer pathType;
    public String pathName;
    public String bankName;
    public String bankCarNo;
    public Long bankId;
    public String withdrawalMoney;
    public String wxOpenid;
    public String qqOpenid;
    public String citycode;
    public String commentId;
    public RequestBean merchantRestaurantsCommentReply;
    public String share;
    public Integer shareType;
    public String remindId;
    public String newGroupOwnId;
    public String groupid;
    public String contentInfo;
    public String pics;
    public String typeId;
    public String catalogId;
    public String keyCustom;

    public static class OrderRequestBean{
        public String restaurantsId;
        public String orderId;
        public String score;
        public String serverScore;
        public String content;

        public  String url;

        public  String courierId;

        public String foodId;
        public String imagesUrl;
    }
    public static class MerchantRestaurants{
        public String name;
        public String id;
        public Double latitude;
        public Double longitude;
    }


    public static class PayInfoBean {
        public double totalPrice;
        public int pathType = 0;//支付方式：0微信支付，1支付宝支付,2余额支付
        public String pathName = "微信支付";//支付名称??
        public int orders;//订单总数??
    }

    public static class OrderDining {
        public String  id;
        public Integer source = 0;//订单来源,0:app，1微信小程序
        public String shopId;
        public String shopName;
        public Integer type = 0;//订单类型:0餐饮
        public Double totalPrice;//店铺订单总价
        public Double payMoney;//店铺订单总价
        public String remark;//备注
        public Integer payType = 2;//1：货到付款，2：在线支付
        public Integer diningType;//0：外送，1到店
        public Integer deliveryType = 0;//外送时：（0立即，1定时，2自提）；到店时：（0立即，1预约）
        public Double deliverFee;//配送费
        public Long appointTime;//预约时间
        public List<ConfirmMenuBean> orderDetailSkuList = new ArrayList<>();

    }

    public static class ConfirmMenuBean {
        public String goodsId;
        public String goodsUrl;
        public String goodsName;
        public String skuId;
        public String spec;
        public double price;
        public int quantity;

    }


    public static class FoodInfo {
        public String id;
        public String restaurantsId;
        public int type;
    }
}
