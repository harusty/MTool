package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/19.
 */

public class BillBean extends BaseBean {
    public List<DataBean> data;


    public static class DataBean {

        public long id;
        public long accountId;
        public int changeDirection;
        public String quantity;
        public long createTime;
        public int type;
        public String changeReason;
        public String orderId;
        public String username;
        public String nickname;
    }
}
