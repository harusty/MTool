package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/11/22.
 */

public class UserDetailBean extends BaseListBean {


    public DataBean data;

    @Override
    public List getData() {
        return data.forumPostDo;
    }

    public static class DataBean {

        public UserMemberDoBean userMemberDo;
        public int type;
        public int sayType;
        public int attentionFlag;//1已关注，0未关注
        public List<StateListBean.DataBean> forumPostDo;

        public static class UserMemberDoBean {
            public String id;
            public String nickname;
            public String picUrl;
            public int sex;
            public String introduction;
            public String idolcount;
            public String fanscount;
            public String age;
            public String hxUsername;
            public List<?> forumPostDo;
        }


    }
}
