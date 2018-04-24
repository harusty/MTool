/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzkx.mtool.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.easeui.utils.StrangersCachUtil;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.VoiceCallExtBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.ui.VideoCallActivity;
import com.zzkx.mtool.chat.ui.VoiceCallActivity;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DemoHelper.getInstance().isLoggedIn())
            return;
        //username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");
        String extJson = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
        String groupId = null;
        System.out.println("CallReceiver ext: " + extJson);
        if (!TextUtils.isEmpty(extJson)) {
            VoiceCallExtBean extBean = Json_U.fromJson(extJson, VoiceCallExtBean.class);
            groupId = extBean.groupId;
            StrangersCachUtil.putUser(MyApplication.getContext(), from, extBean.usernicknamehx, extBean.userpicurlhx);
        }
        if ("video".equals(type)) { //video call
            context.startActivity(new Intent(context, VideoCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    putExtra("group_id", groupId).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {//voice call
            context.startActivity(new Intent(context, VoiceCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    putExtra("group_id", groupId).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


    }

}
