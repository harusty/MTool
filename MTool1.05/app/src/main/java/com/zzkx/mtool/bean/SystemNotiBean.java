package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/2/23.
 */

public class SystemNotiBean extends BaseListBean {
    public List<DataBean> data;

    @Override
    public List getData() {
        return data;
    }

    public static class DataBean {
        public long id;
        public String title;
        public String name;
        public String pic;
        public String contentinfo;
        public long createTime;
        public int type;
        public long userId;
        public int noticeType;
    }
}
