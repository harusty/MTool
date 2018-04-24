package com.zzkx.mtool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sshss on 2017/12/8.
 */

public class GroupMemberBean extends BaseBean implements Serializable {

    public List<DataBean> data;

    public static class DataBean implements Serializable{

        public String id;
        public String nickname;
        public String picUrl;
        public String hxUsername;
        public int ownerType;
        public boolean cusChecked;
    }
}
