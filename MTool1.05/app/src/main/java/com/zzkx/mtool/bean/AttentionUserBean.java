package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionUserBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean extends InitialBean {

        public String fansId;
        public long createTime;
        public AttentionUserBean.UserMemberBean userMember;

        public boolean cusSelected;
    }

    public static class UserMemberBean {
        public String id;
        public String nickname;
        public String picUrl;
        public String hxUsername;
    }
}
