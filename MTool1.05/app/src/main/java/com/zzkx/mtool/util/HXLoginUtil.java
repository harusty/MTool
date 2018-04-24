package com.zzkx.mtool.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.activity.LoginActivity;

/**
 * Created by sshss on 2017/10/14.
 */

public class HXLoginUtil {
    public static void login(final Activity activity, final LoginResultListener listener) {
        final String hxUsername = SPUtil.getString(Const.HX_ID, "");
        String hxPassword = SPUtil.getString(Const.HX_PWD, "");
        if (TextUtils.isEmpty(hxUsername) || TextUtils.isEmpty(hxPassword)) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            return;
        }
        EMClient.getInstance().login(hxUsername, hxPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DemoHelper.getInstance().getModel().setAutoAcceptGroupInvitation(true);
                        EMClient.getInstance().getOptions().setAutoAcceptGroupInvitation(true);
                        System.out.println("环信登录成功");
                        // ** manually load all local groups and conversation
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        // update current user's display searchName for APNs
                        boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                                SPUtil.getString(Const.USER_NICK, "").trim());
                        if (!updatenick) {
                            Log.e("LoginActivity", "update current user nick fail");
                        }

                        // get user's info (this should be get from App's server or 3rd party service)
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                        DemoHelper.getInstance().setCurrentUserName(hxUsername);
                        DemoHelper instance = DemoHelper.getInstance();
                        instance.getUserProfileManager().updateCurrentUserNickName(SPUtil.getString(Const.USER_NICK, ""));
                        instance.getUserProfileManager().setCurrentUserAvatar(SPUtil.getString(Const.USER_HEADER, ""));
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }
                }).start();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                System.out.println("环信登录失败：" + code + "  " + message);
                if (listener != null) {
                    listener.onFaild();
                }
            }
        });
    }

    public interface LoginResultListener {
        void onSuccess();

        void onFaild();
    }
}
