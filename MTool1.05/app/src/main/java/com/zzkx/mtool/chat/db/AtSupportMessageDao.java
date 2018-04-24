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
package com.zzkx.mtool.chat.db;

import android.content.ContentValues;
import android.content.Context;

import com.zzkx.mtool.chat.domain.AtSupportMessage;

import java.util.List;


public class AtSupportMessageDao {
    static final String TABLE_NAME = "notify_msgs";
    static final String COLUMN_NAME_ID = "id";
    static final String COLUMN_NAME_STATE_ID = "state_id";
    static final String COLUMN_NAME_FROM = "username";
    static final String COLUMN_NAME_TIME = "time";
    static final String COLUMN_NAME_REASON = "reason";

    static final String COLUMN_NAME_PIC_URL = "pic_url";
    static final String COLUMN_NAME_NICK = "nick";
    static final String COLUMN_NAME_ACTION_TYPE = "action_type";
    static final String COLUMN_NAME_AT_TYPE = "at_type";
    static final String COLUMN_NAME_SUPPORT_TYPE = "support_type";
    public static final String COLUMN_NAME_READ_FLAG = "read_flag";

    public AtSupportMessageDao(Context context) {

    }

    /**
     * save searchMessage
     *
     * @param message
     * @return return cursor of the searchMessage
     */
    public Integer saveActionMessage(AtSupportMessage message) {
        return DemoDBManager.getInstance().saveActionMessage(message);
    }

    /**
     * update searchMessage
     *
     * @param msgId
     * @param values
     */
    public void updateActionMessage(int msgId, ContentValues values) {
        DemoDBManager.getInstance().updateActionMessage(msgId, values);
    }

    /**
     * get messges
     *
     * @return
     */
    public List<AtSupportMessage> getActionMessagesList() {
        return DemoDBManager.getInstance().getActionMessages();
    }

    public List<AtSupportMessage> getAtMessagesList() {
        return DemoDBManager.getInstance().getAtMessagesList();
    }

    public List<AtSupportMessage> getSupportMessageList() {
        return DemoDBManager.getInstance().getSupportMessagesList();
    }


    public void deleteMessage(String from) {
        DemoDBManager.getInstance().deleteMessage(from);
    }


}
