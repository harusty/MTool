package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/11/15.
 */

public class ConversationExtBean  {

    //搜索聊天记录
    public String searchName;
    public String searchMessage;
    public int searchType;
    //会话列表
    public int conversationType = -1;
    public int unReadCount;//订单未读消息，好友申请数量
    public String message;
    public String header;
    public String nick;
    public boolean isTop;
    public long topTime;

    public ConversationExtBean() {
    }
}
