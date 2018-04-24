package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/1/4.
 */

public class BankCardBean extends BaseBean {
    public List<Data> data;

    public static class Data {
        public long id;
        public long merchantUserId;
        public String bankName;
        public String bankCarNo;
        public String name;
        public int defaultValue;
        public long createTime;
    }
}
