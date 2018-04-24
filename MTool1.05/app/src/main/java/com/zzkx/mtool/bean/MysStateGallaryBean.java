package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/10/21.
 */

public class MysStateGallaryBean extends BaseBean {

    public List<DataBean> data;
    public List<Object> cusData;

    public static class DataBean {

        public String month;
        public List<StateListBean.ResData> forumThreadResources;
    }
//    public static class Res {
//        public String id;
//        public String resourceUrl;
//        public long createTime;
//        public int cusResIndex;
//    }
}
