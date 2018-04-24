package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/1/23.
 */

public class ShopComentReplyBean extends BaseListBean {
    public List<DataBean> data;

    @Override
    public List getData() {
        return data;
    }
    public static class DataBean {

        public String id;
        public String memId;
        public String commentId;
        public String parentId;
        public String memPicUrl;
        public String content;
        public String memNickname;
        public long createTime;
    }
}
