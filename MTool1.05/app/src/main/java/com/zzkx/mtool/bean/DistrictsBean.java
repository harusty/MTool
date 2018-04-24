package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/11/28.
 */

public class DistrictsBean extends BaseBean {
    public List<DataBean> data;

    public static class DataBean {

        public String id;
        public String name;
        public String citycode;
        public String adcode;
        public double latitude;
        public double longitude;
    }
}
