package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/5.
 */

public class UserSearchBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String username;
        public String nickname;
        public String picUrl;
        public String phone;
        public long createTime;
        public long modifyTime;
        public int type;
        public String hxUsername;
        public String hxPassword;
        public int sex;
        public long birthday;
        public String introduction;
        public int isFix;
    }
}
