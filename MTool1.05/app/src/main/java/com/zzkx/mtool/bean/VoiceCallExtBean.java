package com.zzkx.mtool.bean;

import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;

/**
 * Created by sshss on 2017/12/27.
 */

public class VoiceCallExtBean {
    public String groupId;
    public String userpicurlhx;
    public String usernicknamehx;

    public VoiceCallExtBean() {
        userpicurlhx = SPUtil.getString(Const.USER_HEADER, "");
        usernicknamehx = SPUtil.getString(Const.USER_NICK, "");
    }
}
