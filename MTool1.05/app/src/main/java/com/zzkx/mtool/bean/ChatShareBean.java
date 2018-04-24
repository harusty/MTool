package com.zzkx.mtool.bean;

import java.io.Serializable;

/**
 * Created by sshss on 2018/1/10.
 */

public class ChatShareBean implements Serializable {
    //名片0，动态1，店铺2，商品3
    public int type;
    public String picUrl;
    public String title;
    public String content;

    public String id;//可能是环信id，也是mtoolid
    public String extraId;//扩展id，如果id是hxId,扩展id可以是mtoolid
}
