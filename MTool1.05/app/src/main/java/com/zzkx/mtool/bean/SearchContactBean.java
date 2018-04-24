package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/11/20.
 */

public class SearchContactBean extends BaseBean {


    public DataBean data;
    public List<Object> cusData;

    public static class DataBean {
        public List<FanListBean> fanList;
        public List<FanListBean> idolList;
        public List<FanListBean> friendList;
        public List<FanListBean> groupList;
    }
    public static class FanListBean {
        public String idolId;
        public String fansId;
        public String groupId;
        public String memId;
        public UserMemberBean userMember;
        public UserMemberBean chatGroup;
        public int cusType;
    }
    public static class UserMemberBean {
        public String id;
        public String nickname;
        public String picUrl;

        public String groupid;
        public String name;
    }
}
