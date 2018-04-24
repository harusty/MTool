package com.zzkx.mtool.view;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.easeui.widget.EaseContactList;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.ui.GroupsActivity;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.customview.DialogInitialNab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/15.
 */

public class MToolShareActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.contact_list)
    EaseContactList mContactList;
    private ListView mListView;
    private View mHeader;
    private List<EaseUser> contactList = new ArrayList<>();
    private Map<String, EaseUser> mContactsMap;
    private DialogInitialNab mDialogInitialNab;
    private String mShareJson;

    @Override
    public int getContentRes() {
        return R.layout.activity_mtoool_share;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("选  择");
        mShareJson = getIntent().getStringExtra(Const.SHARE_INFO);

        mHeader = View.inflate(this, R.layout.header_select_group, null);
        mListView = mContactList.getListView();
        mContactList.init(contactList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - mListView.getHeaderViewsCount();
                if (position >= 0) {
                    EaseUser easeUser = contactList.get(position);
                    share(easeUser.getUsername(), EMMessage.ChatType.Chat);
                }
            }
        });
        HeaderUtil.addHeader(this, mListView, 0);
        mListView.addHeaderView(mHeader);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MToolShareActivity.this, GroupsActivity.class).putExtra(Const.ACTION, GroupsActivity.ACTION_SELECT2), 99);
            }
        });

    }

    public void share(String to, EMMessage.ChatType chatType) {

        ChatShareBean shareBean = Json_U.fromJson(mShareJson, ChatShareBean.class);
        String content = "";
        switch (shareBean.type) {
            case 0:
                content = "[名片]";
                break;
            case 1:
                content = "[动态]";
                break;
            case 2:
                content = "[店铺]";
                break;
            case 3:
                content = "[商品]";
                break;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, to);
        message.setAttribute(EaseConstantSub.MESSAGE_TYPE_SHARE, true);
        message.setAttribute(Const.SHARE_INFO, mShareJson);
        message.setAttribute(EaseConstant.USER_NICK, SPUtil.getString(Const.USER_NICK, ""));
        message.setAttribute(EaseConstant.USER_HEAD, SPUtil.getString(Const.USER_HEADER, ""));
        message.setChatType(chatType);
        //send searchMessage
        EMClient.getInstance().chatManager().sendMessage(message);
        ToastUtils.showToast("分享成功");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String to = data.getStringExtra(Const.ID);
            share(to, EMMessage.ChatType.GroupChat);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void refresh() {
        initNet();
    }

    @Override
    public void initNet() {
        synchronized (this) {
            mContactsMap = DemoHelper.getInstance().getContactList();
            getContactList();
            mContactList.refresh();
        }

//        ContactUtil.getInstance(new ContactUtil.CallBack() {
//            @Override
//            public void onStart() {
//                showProgress(true);
//            }
//
//            @Override
//            public void onSuccess(Map<String, EaseUser> contactsMap) {
//                mContactsMap = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) contactsMap).clone();
//                getContactList();
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!mActivity.isFinishing()) {
//                            mContactList.refresh();
//                            showProgress(false);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFiald() {
//                ShareActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showError(null);
//                    }
//                });
//            }
//        }).getContact();
    }

    protected void getContactList() {
        contactList.clear();
        if (mContactsMap == null) {
            return;
        }
        synchronized (mContactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = mContactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")) {
//                    if (!blackList.contains(entry.getKey())) {
                    //filter out users in blacklist
                    String id = SPUtil.getString(Const.HX_ID, "");
                    if (!TextUtils.isEmpty(id) && entry.getKey().contains(id)) {
                        continue;
                    }
                    EaseUser user = entry.getValue();
                    EaseCommonUtils.setUserInitialLetter(user);
                    contactList.add(user);
//                    }
                }
            }
        }

        // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    return lhs.getNick().compareTo(rhs.getNick());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });

    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_indicator:
                if (mDialogInitialNab == null) {
                    mDialogInitialNab = new DialogInitialNab(MToolShareActivity.this);
                    mDialogInitialNab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String headerString = (String) view.getTag();
                            ListView listView = mContactList.getListView();
                            SectionIndexer indexer = mContactList.getAdapter();
                            String[] adapterSections = (String[]) indexer.getSections();
                            try {
                                for (int i = adapterSections.length - 1; i > -1; i--) {
                                    if (adapterSections[i].equals(headerString)) {
                                        listView.setSelection(indexer.getPositionForSection(i));
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("setHeaderTextAndScroll", e.getMessage());
                            }
                            mDialogInitialNab.dismiss();
                        }
                    });
                }
                break;
        }
    }
}
