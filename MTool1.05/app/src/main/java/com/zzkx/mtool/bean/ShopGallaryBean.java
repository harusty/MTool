package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/10/16.
 */

public class ShopGallaryBean extends BaseBean {


    public DataBean data;

    public static class DataBean {
        public List<ImageBean> merchantRestaurantsSurroundingsImg;

        public static class ImageBean {

            public String id;
            public String shopId;
            public String imgUrl;
            public String sort;
            public long createTime;
            public long updateTime;
        }
    }
}
