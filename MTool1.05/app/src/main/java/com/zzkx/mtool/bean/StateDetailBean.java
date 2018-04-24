package com.zzkx.mtool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateDetailBean extends BaseBean implements Serializable{


    public DataBean data;

    public static class DataBean implements Serializable{

        public String id;
        public String memId;
        public long createTime;
        public String content;
        public int supports;
        public int opposes;
        public int collectType;
        public int collects;
        public int suppoppType;
        public UserMemberBean userMember;
        public List<UserMemberDosBean> userMemberDos;
        public ArrayList<StateListBean.ResData> forumThreadResources;
        public int shareType;
        public String firstName;
        public String share;
        public String firstId;
        public List<StateListBean.UserMemberBean> shareuserMemberList;
        public List<StateListBean.UserMemberBean> userMemberList;

        public static class UserMemberBean implements Serializable{

            public String id;
            public String nickname;
            public String picUrl;
        }

        public static class UserMemberDosBean implements Serializable{

            public long id;
            public String nickname;
            public String picUrl;
        }
    }
}
