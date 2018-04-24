package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2018/1/3.
 */

public class MyMoneyBean extends BaseBean {


    public DataBean data;

    public static class DataBean {

        public long id;
        public long memId;
        public String account;
        public long lastTime;
        public int fixDeposit;
        public int cashPledge;
    }
}
