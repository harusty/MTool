package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2018/2/24.
 */

public class ArticleBean extends BaseBean {


    public DataBean data;

    public static class DataBean {
        public long id;
        public String contentInfo;
        public long createTime;
        public int isDelete;
        public String title;
        public int viewCount;
        public int sortNo;
        public int showPosition;
        public long catalogId;
    }
}
