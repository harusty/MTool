package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/2/24.
 */

public class ServCenterCatBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public long parentId;
        public String name;
        public String description;
        public long createTime;
        public String iconUrl;
    }
}
