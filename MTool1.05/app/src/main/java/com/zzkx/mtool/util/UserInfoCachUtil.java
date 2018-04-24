package com.zzkx.mtool.util;

import com.zzkx.mtool.bean.LoginBean;
import com.zzkx.mtool.config.Const;

/**
 * Created by sshss on 2017/10/25.
 */

public class UserInfoCachUtil {
    public static void cachInfo(LoginBean bean) {
        SPUtil.putString(Const.U_ID, bean.data.id);
        SPUtil.putString(Const.HX_ID, bean.data.hxUsername);
        SPUtil.putString(Const.HX_PWD, bean.data.hxPassword);
        SPUtil.putString(Const.USER_PHONE, bean.data.username);
        SPUtil.putString(Const.USER_HEADER, bean.data.picUrl);
        SPUtil.putString(Const.USER_NICK, bean.data.nickname);
        SPUtil.putString(Const.USER_INTRO, bean.data.introduction);
        SPUtil.putInt(Const.USER_SEX, bean.data.sex);
        SPUtil.putLong(Const.USER_BIRTH, bean.data.birthday);
        SPUtil.putString(Const.USER_ADD, bean.data.userAddr);
        SPUtil.putString(Const.USER_QR, bean.data.qrCode);
    }
}
