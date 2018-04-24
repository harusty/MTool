package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/16.
 */

public class ResultShopFood extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String storeId;
        public String merchantUserId;
        public String name;
        public String linkman;
        public String hotline;
        public double latitude;
        public double longitude;
        public String address;
        public int auditStatus;
        public double priceScore;
        public double serviceScore;
        public long createTime;
        public List<MenuListBean.FoodInfoListBean> foodInfoList;
    }
}
