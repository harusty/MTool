package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/12/14.
 */

public class UserPrivacyBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
//         "id": 11111,						//隐私id
//         "memId": 15053080898020,			//用户id
//         "disturbStatus": 0,					//勿扰开关	0:关 1:开
//         "voiceSwich": 0,					//声音设置开关	0:关 1:开
//         "shakeSwich": 0,					//震动设置开关	0:关 1:开
//         "passivityChat": 0,					//是否允许粉丝发起聊天	0:否 1:是
//         "autoAddFans": 0,					//自动关注粉丝			0:否 1:是
//         "strangerSeeStatus": 0,				//是否让陌生人查看我的动态  0:否 1:是
//         "fansSeeStatus": 0,					//是否让粉丝看我的动态 	0:否 1:是
//         "togetherStatus": 0					//订单评价同步到动态		0:否 1:是

        public String id;
        public String memId; //用户id
        public int disturbStatus;
        public int voiceSwich;
        public int shakeSwich;
        public int passivityChat;
        public int autoAddFans;
        public int strangerSeeStatus;
        public int fansSeeStatus;
        public int togetherStatus;
    }
}
