package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionShopBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean extends InitialBean {

        public String id;
        public String shopId;
        public String memId;
        public int type;
        public String shopName;
        public String shopLogo;
        public int shopStatus;
        public long createTime;
    }
}
