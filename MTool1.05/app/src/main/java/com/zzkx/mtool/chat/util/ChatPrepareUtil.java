package com.zzkx.mtool.chat.util;

import android.app.Activity;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EasyUtils;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.ui.VideoCallActivity;
import com.zzkx.mtool.chat.ui.VoiceCallActivity;
import com.zzkx.mtool.view.activity.MainActivity;

/**
 * Created by sshss on 2018/1/17.
 */

public class ChatPrepareUtil {
    public static void prepare(final int sleepTime, final Activity activity) {
        new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
                    if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName()) || topActivityName.equals(VoiceCallActivity.class.getName()))) {
                        // nop
                        // avoid main screen overlap Calling Activity
                    } else {
                        //enter main screen
                        activity.startActivity(new Intent(activity, MainActivity.class));
                    }
                    activity.finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
            }
        }).start();
    }
}
