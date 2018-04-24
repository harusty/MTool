package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/9/27.
 */

public class CommentListBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String memId;
        public String postId;
        public String content;
        public long createTime;
        public UserMemberBean userMember;
        public String parentId;
        public String fatherId;
        public UserMemberReplyBean userMemberReply;
        public boolean isTopComment;
        public boolean isFirstComment;
        public String opposes;
        public String supports;

        public static class UserMemberBean {
            public String id;
            public String nickname;
            public String picUrl;
        }

        public static class UserMemberReplyBean {

            public String id;
            public String nickname;
        }
    }
}
