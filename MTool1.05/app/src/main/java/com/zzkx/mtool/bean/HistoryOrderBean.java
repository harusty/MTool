package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public class HistoryOrderBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String shopName;
        public DiningType orderDining;
        public long createTime;



    }
    public static class DiningType {
        public int diningType;
    }
}
