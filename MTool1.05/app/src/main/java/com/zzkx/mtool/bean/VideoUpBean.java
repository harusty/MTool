package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/11/28.
 */

public class VideoUpBean extends BaseBean {
    public Data data;
    public String path;

    public static class Data {
        public String videoId;
        public String uploadAddress;
        public String uploadAuth;
    }
}
