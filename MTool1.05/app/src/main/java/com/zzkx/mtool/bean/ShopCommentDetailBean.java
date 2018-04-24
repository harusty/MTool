package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/1/26.
 */

public class ShopCommentDetailBean extends BaseBean {


    public DataBean data;

    public static class DataBean {

        public String id;
        public String restaurantsId;
        public String memId;
        public String orderId;
        public String memPicUrl;
        public String score;
        public String serverScore;
        public String memNickname;
        public String content;
        public long createTime;
        public int praises;
        public int opposes;
        public long now;
        public int suppoppType;
        public List<SupportUserBean> restaurantsCommentSuppopps;
        public List<ResBean> merchantRestaurantsCommentImgs;


    }
    public static class SupportUserBean {

        public String id;
        public String memId;
        public String memPicUrl;
        public String memNickname;
    }

    public static class ResBean {

        public String id;
        public String commentId;
        public String url;
        public long createTime;
    }
}
