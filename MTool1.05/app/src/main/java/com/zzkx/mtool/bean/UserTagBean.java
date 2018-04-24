package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/14.
 */

public class UserTagBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String memId;
        public String name;
    }
}
