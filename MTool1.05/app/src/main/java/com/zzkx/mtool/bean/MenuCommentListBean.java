package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/10/14.
 */

public class MenuCommentListBean extends BaseListBean {
    public List<DataBean> data;

    @Override
    public List getData() {
        return data;
    }

    public static class DataBean {
        public String id;
        public String foodId;
        public String memId;
        public String memPic;
        public String memNickname;
        public int score;
        public String content;
        public long createTime;
    }
}
