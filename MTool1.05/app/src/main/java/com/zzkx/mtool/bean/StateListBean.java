package com.zzkx.mtool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateListBean extends BaseListBean implements Serializable {

    public List<DataBean> data;

    @Override
    public List getData() {
        return data;
    }

    public static class DataBean implements Serializable {

        public String id;
        public String memId;
        public String firstId;//原文动态id
        public long createTime;
        public String content;
        public String share;
        public int supports;
        public int opposes;
        public String parentId;
        public String firstName;
        public int collects;
        public int shareType;  //是否是分享的动态0否，1是
        public UserMemberBean userMember;
        public long now;
        public int suppoppType;
        public int followType;
        public ArrayList<ResData> forumThreadResources;
        public int collectType;
        public List<StateListBean.UserMemberBean> userMemberList;
        public List<StateListBean.UserMemberBean> shareuserMemberList;

    }

    public static class ResData implements Serializable {

        public String resourceUrl;
        public int type;
        public String videoId;
        public String coverUrl;
        public int cusResIndex;
    }

    public static class UserMemberBean implements Serializable {
        public String id;
        public String nickname;
        public String picUrl;
    }
}
