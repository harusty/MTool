package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/16.
 */

public class HistoryBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String keyword;
        public long createTime;
        public long updateTime;
    }
}
