package com.zzkx.mtool.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by sshss on 2017/10/17.
 */

public class CollectionCatBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean implements Serializable {

        public String id;
        public String memId;
        public String name;
        public int type;
        public long createTime;
        public boolean cusSlected;
    }
}
