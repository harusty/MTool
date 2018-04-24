package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2018/1/3.
 */

public class AliPayResultBean extends BaseBean {
    public Data data;
    public static class Data{
        public String payId;
        public String body;
    }
}
