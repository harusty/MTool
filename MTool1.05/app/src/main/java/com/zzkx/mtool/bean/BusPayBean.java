package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/9/13.
 */

public class BusPayBean {
    public static final int SUCESS = 0;
    public static final int CANCLE = -2;
    public static final int FAILD = -1;

    public int mCode;

    public BusPayBean(int errCode) {
        mCode = errCode;
    }
}
