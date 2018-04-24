package com.zzkx.mtool.chat.domain;

/**
 * Created by sshss on 2018/1/27.
 */

public class AtSupportMessage {

    public String stateId;//动态id
    public String picUrl;//头像
    public String nickName;//昵称

    public int id;
    public int actionType;//0@，1赞
    public int atType;//0发表动态中@，1动态评论中，2评论对话中（回复，或者回复中@）
    public int supportType;//0赞了我的动态，1赞了我的评论

    public int readFlag;//已读，未读
    public long msgTime;
    public String reason;

}
