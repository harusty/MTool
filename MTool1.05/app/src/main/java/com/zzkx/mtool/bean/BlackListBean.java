package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public class BlackListBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String nickname;
        public String picUrl;
        public String hxUsername;
        public String cusInitial;
        public boolean cusSelected;
    }
}
