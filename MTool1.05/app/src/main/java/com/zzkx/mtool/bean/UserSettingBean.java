package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/11/22.
 */

public class UserSettingBean extends BaseBean {

    public DataBean data;

    public static class DataBean {

        public String id;
        public String friend1Id;
        public String friend2Id;

        public int friend1Switch;//是否查看TA的动态:0-否，1-是
        public int friend2Switch;//是否让TA查看我的动态  0-否，1-是
        public int shield;//是否屏蔽 0-否，1-是
        public int speak;//向我发言 0-禁止，1-允许
        public long createTime;
    }
}
