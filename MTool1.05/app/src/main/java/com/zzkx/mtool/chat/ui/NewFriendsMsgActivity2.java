/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzkx.mtool.chat.ui;

import android.view.View;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.adapter.NewFriendsMsgAdapter;
import com.zzkx.mtool.chat.db.InviteMessgeDao;
import com.zzkx.mtool.chat.domain.InviteMessage;
import com.zzkx.mtool.view.activity.BaseActivity;

import java.util.Collections;
import java.util.List;

/**
 * Application and notification
 *
 */
public class NewFriendsMsgActivity2 extends BaseActivity {

	@Override
	public int getContentRes() {
		return R.layout.em_activity_new_friends_msg;
	}

	@Override
	public void initViews() {
		setMainMenuEnable();
		setMainTitle(getString(R.string.Application_and_notify));
		ListView listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		Collections.reverse(msgs);

		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
	}

	@Override
	public void onReload() {

	}

	public void back(View view) {
		finish();
	}
}
