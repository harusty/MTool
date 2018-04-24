package com.zzkx.mtool.bean;

import java.io.Serializable;

/**
 * Created by sshss on 2017/11/1.
 */

public class MyEMMessage implements Serializable {

    public long msgTime;
    public String msgContent;
    public String fromNicName;
    public String userHead;
    public boolean isRead;
}
