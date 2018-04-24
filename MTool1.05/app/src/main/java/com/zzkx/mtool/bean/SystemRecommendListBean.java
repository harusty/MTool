package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2018/2/1.
 */

public class SystemRecommendListBean extends BaseBean {

    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String nickname;
        public String picUrl;
        public String introduction;
        public int fansType;//我是否关注0-否，1-是
        public int idolType;//是否关注我0-否，1-是
    }
}
