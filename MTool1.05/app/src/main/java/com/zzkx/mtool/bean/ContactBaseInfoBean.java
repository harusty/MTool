package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/11/20.
 */

public class ContactBaseInfoBean extends BaseBean {


    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String nickname;
        public String picUrl;
        public String hxUsername;
    }
}
