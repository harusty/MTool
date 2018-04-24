package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/24.
 */

public class OrderListBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public double totalPrice;
        public double payMoney;
        public long createTime;
        public String shopId;
        public String memUsername;
        public String memId;
        public String remark;
        public String payId;
        public long payTime;
        public int diningType;
        public int source;
        public int deliveryType;
        public int totalProcessTime;
        public int deliverFee;
        public String originAddr;
        public String originGeo;
        public String consignee;
        public String deliveryGeo;
        public String consigneePhone;
        public String consigneeAddr;
        public int deliveryMethod;
        public UserMemberBean userMember;
        public long appointTime;
        public List<?> orderDetailSkuList;
        public String orderName;

        public static class UserMemberBean {

            public String picUrl;
            public String phone;
        }
    }
}
