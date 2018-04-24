package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/2/24.
 */

public class QuesDetailListBean extends BaseBean{

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public int isUser;
        public int isShop;
        public int isDistribute;
        public String contentInfo;
        public long createTime;
        public long updateTime;
        public int isDelete;
        public String title;
        public int viewCount;
        public int sortNo;
        public int showPosition;
        public long catalogId;
    }
}
