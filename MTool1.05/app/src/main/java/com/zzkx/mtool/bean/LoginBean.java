package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/6/23.
 */

public class LoginBean extends BaseBean {
    public Data data;

    public static class Data {
        public String id;
        public String picUrl;
        public String qrCode;
        public String phone;
        public String username;
        public String nickname;
        public String introduction;
        public String hxUsername;
        public String hxPassword;
        public String userAddr;
        public int sex;
        public long birthday;
        public int type;
    }
}
