package com.zzkx.mtool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sshss on 2017/10/14.
 */

public class ShopCommentListBean extends BaseListBean {

    public List<DataBean> data;

    @Override
    public List getData() {
        return data;
    }

    public static class DataBean implements Serializable {

        public String id;
        public String restaurantsId;
        public String memId;
        public String memPicUrl;
        public int score;
        public int serverScore;
        public String memNickname;
        public String content;
        public long createTime;
        public int praises;
        public String now;
        public List<MerchantRes> merchantRestaurantsCommentImgs;
        public int opposes;
        public int suppoppType;

        public static class MerchantRes implements Serializable {
            public String id;
            public String commentId;
            public String url;
            public long createTime;
        }
    }
}
