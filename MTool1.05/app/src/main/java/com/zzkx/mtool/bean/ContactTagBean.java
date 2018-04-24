package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class ContactTagBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String memId;
        public String name;
        public long createTime;
        public boolean cusSlected;
    }
}
