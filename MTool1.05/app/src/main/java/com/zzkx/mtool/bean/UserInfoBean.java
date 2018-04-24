package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/11/2.
 */

public class UserInfoBean extends BaseBean {


    public DataBean data;

    public static class DataBean {

        public String id;
        public String phone;
        public String password;
    }
}
