package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/10/24.
 */

public class StateSearchResultBean extends BaseBean{

    public List<DataBean> data;

    public static class DataBean {

        public ForumPostBean forumPost;
        public UserMemberBean userMember;

        public static class ForumPostBean {

            public String id;
            public String forumId;
            public String memId;
            public String userId;
            public long createTime;
            public String content;
        }

        public static class UserMemberBean {

            public String nickname;
            public String picUrl;
        }
    }
}
