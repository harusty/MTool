package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.view.activity.UserDetailActivity;
import com.zzkx.mtool.view.customview.DialogInitialNab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/19.
 */

public class FriendFragment extends ContactSubFragment implements View.OnClickListener {
    @BindView(R.id.contact_list)
    EaseContactList mContactList;

    private ListView mListView;
    private List<EaseUser> contactList = new ArrayList<>();
    private DemoHelper.DataSyncListener mDataSyncListener;
    private Map<String, EaseUser> mContactsMap;
    private DialogInitialNab mDialogInitialNab;
    private FragmentActivity mActivity;
    private EMContactListener mContactlistener;

    @Override
    public int getContentRes() {
        return R.layout.fragment_friend;
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = getActivity();
        mListView = mContactList.getListView();
        mContactList.init(contactList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    EaseUser easeUser = contactList.get(position);
                    startActivity(new Intent(getActivity(), UserDetailActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, easeUser.getUsername())
                    );
                }
            }
        });

        mDataSyncListener = new DemoHelper.DataSyncListener() {
            @Override
            public void onSyncComplete(boolean success) {
                if (success) {
                    refresh();
                }
            }
        };
        DemoHelper.getInstance().addSyncContactListener(mDataSyncListener);

        mContactlistener = new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                initNet();
            }

            @Override
            public void onContactDeleted(String s) {
                initNet();
            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        };
        EMClient.getInstance().contactManager().setContactListener(mContactlistener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDataSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(mDataSyncListener);
            mDataSyncListener = null;
        }
        if (mContactlistener != null)
            EMClient.getInstance().contactManager().removeContactListener(mContactlistener);
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
//                getActivity().runOnUiThread(new Runnable() {
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
            Iterator<Entry<String, EaseUser>> iterator = mContactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Entry<String, EaseUser> entry = iterator.next();
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

    }

    @Override
    public void onInitialClick(View view) {
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
    }
}
