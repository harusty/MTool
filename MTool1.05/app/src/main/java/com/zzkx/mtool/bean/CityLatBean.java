package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/11/28.
 */

public class CityLatBean extends BaseBean {


    public DataBean data;

    public static class DataBean {
        public String id;
        public String name;
        public String charAlpha;
        public String longtitude;
    }
}
