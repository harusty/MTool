package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class TagMemberBean extends BaseBean {
    public List<Data> data;
    public static class Data{
        public String id;
        public String nickname;
        public String picUrl;
        public boolean cusSelected;
    }
}
