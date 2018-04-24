package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sshss on 2017/11/20.
 */

public class FriendLocalSearchActivity extends SearchAllActivity {
    private EaseContactList contactListLayout;
    private ListView listView;
    private ArrayList<EaseUser> contactList;
    private Map<String, EaseUser> contactsMap;

    @Override
    public int getContentRes() {
        return R.layout.activity_search_all_contact;
    }

    @Override
    public void initViews() {
        super.initViews();
        contactListLayout = (EaseContactList) findViewById(R.id.contact_list);
        setMainTitle("好  友");
        listView = contactListLayout.getListView();
        contactList = new ArrayList<EaseUser>();
        getContactList();
        contactListLayout.init(contactList, true);
        mInput.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });
        contactListLayout.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    startActivity(new Intent(FriendLocalSearchActivity.this, UserDetailActivity.class).putExtra("userId", username));
                }
            }
        });
    }

    @Override
    public void initNet() {
        String s = getIntent().getStringExtra(Const.KEY_WORD);
        mInput.setText(s);
        contactListLayout.filter(s);
    }

    @Override
    public void search(String keyword) {
        if (!TextUtils.isEmpty(keyword))
            contactListLayout.filter(keyword);

    }

    protected void getContactList() {
        contactList.clear();
        contactsMap = DemoHelper.getInstance().getContactList();
        if (contactsMap == null) {
            return;
        }
        synchronized (this.contactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")) {
                    if (!blackList.contains(entry.getKey())) {
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
        }
    }
}
