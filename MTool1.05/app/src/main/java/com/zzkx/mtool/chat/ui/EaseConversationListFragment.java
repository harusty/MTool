package com.zzkx.mtool.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.GroupChatExtBean;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ConversationExtBean;
import com.zzkx.mtool.chat.db.InviteMessgeDao;
import com.zzkx.mtool.chat.util.ContactUtil;
import com.zzkx.mtool.chat.widget.EaseConversationList;
import com.zzkx.mtool.util.SPUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.hyphenate.easeui.utils.Json_U.fromJson;

/**
 * conversation list fragment
 * <p>
 * 加载本地会话缓存
 * 环信服务器连接断开监听
 */
public class EaseConversationListFragment extends EaseBaseFragment {
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    public static final int MAIN_CONVER_LIST = 0;
    public static final int ORDER_CONVER_LIST = 1;
    public int mConverType = 0;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }
    };
    private InviteMessgeDao inviteMessgeDao;
    private ArrayList<String> conversationIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        query = (EditText) getView().findViewById(R.id.query);

        // button to clear content in searchMain bar
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }


    public void setConversationListType(int type) {
        mConverType = type;
    }

    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        asyncConverHeader();
        conversationListView.init(conversationList);

        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    System.out.println("conversationList out fragment :" + conversationList.size());
                    if (conversationListView == null)
                        startActivity(new Intent(getContext(), EaseMainActivity.class));
                    else
                        conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }

    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        conversationIds = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new searchMessage during sorting
         * so use synchronized to make sure timestamp of last searchMessage won't change.
         */
        synchronized (conversations) {
            EMGroupManager emGroupManager = EMClient.getInstance().groupManager();

            long lastTime = 0;
            EMConversation orderLastConverSation = null;
            int orderUnreadCount = 0;
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() == 0)
                    continue;
                if (TextUtils.isEmpty(conversation.getExtField())) {
                    conversation.setExtField("{}");
                }
                if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                    if (mConverType == ORDER_CONVER_LIST)
                        continue;
                    conversationIds.add(conversation.conversationId());
                    long msgTime = conversation.getLastMessage() != null ? conversation.getLastMessage().getMsgTime() : 0;
                    sortList.add(new Pair<Long, EMConversation>(msgTime, conversation));
                } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    EMGroup group = emGroupManager.getGroup(conversation.conversationId());
                    if (group != null) {
                        String description = group.getDescription();
                        System.out.println("group description:" + description);
                        try {
                            GroupChatExtBean groupChatExtBean = fromJson(description, GroupChatExtBean.class);
                            switch (groupChatExtBean.groupType) {
                                case EaseConstant.ORDER_CHAT:
                                    long msgTime = conversation.getLastMessage().getMsgTime();
                                    orderUnreadCount += conversation.getUnreadMsgCount();
                                    if (msgTime > lastTime) {
                                        lastTime = msgTime;
                                        orderLastConverSation = conversation;
                                    }
                                    if (mConverType == MAIN_CONVER_LIST) {
                                        continue;
                                    }
                                    break;
                                case EaseConstant.NORMAL_CHAT:
                                    if (mConverType == ORDER_CONVER_LIST)
                                        continue;
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }

                        long msgTime = conversation.getLastMessage() != null ? conversation.getLastMessage().getMsgTime() : 0;
                        sortList.add(new Pair<Long, EMConversation>(msgTime, conversation));
                    }
                } else if (mConverType == MAIN_CONVER_LIST) {
                    long msgTime = conversation.getLastMessage().getMsgTime();
                    sortList.add(new Pair<Long, EMConversation>(msgTime, conversation));
                }
            }

            if (mConverType == MAIN_CONVER_LIST) {
                addOrderHelper(sortList, orderLastConverSation, orderUnreadCount);
                addStateHelper(sortList);
                adNewFriend(sortList);
                addSystemNoti(sortList);
            }
        }

        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        return list;
    }

    private void asyncConverHeader() {
        if (conversationIds.size() > 0)
            ContactUtil.getInstance(new ContactUtil.CallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Map<String, EaseUser> contactList) {
                    refresh();
                }

                @Override
                public void onFiald() {

                }
            }).updateConversationHeader(conversationIds);
    }


    private void addSystemNoti(List<Pair<Long, EMConversation>> sortList) {
        long inviteTime = 0;
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(EaseConstant.SYSTEM_NOTI_NAME,
                EMConversation.EMConversationType.GroupChat, true);
        String json = SPUtil.getString(EaseConstant.SYSTEM_NOTI_NAME, "{}");
        ConversationExtBean extBean = Json_U.fromJson(json, ConversationExtBean.class);
        extBean.conversationType = EaseConstant.SYSTEM_NOTI;
        conversation.setExtField(Json_U.toJson(extBean));
        sortList.add(new Pair<Long, EMConversation>(inviteTime, conversation));
    }
    private void adNewFriend(List<Pair<Long, EMConversation>> sortList) {

//        if (inviteMessgeDao == null) {
//            inviteMessgeDao = new InviteMessgeDao(getActivity());
//        }
//
//        List<InviteMessage> messagesList = inviteMessgeDao.getMessagesList();
//        if (messagesList != null && messagesList.size() > 0) {
//            InviteMessage inviteMessage = messagesList.get(messagesList.size() - 1);
//            inviteMessage.getTime();
//            long inviteTime = inviteMessage.getTime();
//
//            EMConversation newFriendConversation = EMClient.getInstance().chatManager().getConversation(EaseConstant.NEW_FRIEND_NAME,
//                    EMConversation.EMConversationType.GroupChat, true);
//            ConversationExtBean extBean = new ConversationExtBean();
//            extBean.conversationType = EaseConstant.NEW_FRIEND;
//            extBean.unReadCount = inviteMessgeDao.getUnreadMessagesCount();
//            newFriendConversation.setExtField(Json_U.toJson(extBean));
//            sortList.add(new Pair<Long, EMConversation>(inviteTime, newFriendConversation));
//        }
        long inviteTime = 0;
        EMConversation newFriendConversation = EMClient.getInstance().chatManager().getConversation(EaseConstant.NEW_FRIEND_NAME,
                EMConversation.EMConversationType.GroupChat, true);
        String json = SPUtil.getString(EaseConstant.NEW_FRIEND_NAME, "{}");
        ConversationExtBean extBean = Json_U.fromJson(json, ConversationExtBean.class);
        extBean.conversationType = EaseConstant.NEW_FRIEND;
        newFriendConversation.setExtField(Json_U.toJson(extBean));
        sortList.add(new Pair<Long, EMConversation>(inviteTime, newFriendConversation));
    }

    private void addStateHelper(List<Pair<Long, EMConversation>> sortList) {
        EMConversation stateHelperConver = EMClient.getInstance().chatManager().getConversation(EaseConstant.STATE_HELPER_NAME,
                EMConversation.EMConversationType.GroupChat, true);
        long msgTime = 0;
//        StateHelperUtil stateHelperUtil = StateHelperUtil.getInstance();
//        int unreadCount = stateHelperUtil.getUnreadCount();
//        MyEMMessage cmdMessage = stateHelperUtil.getLastNotifyMessage();
//        if (cmdMessage != null) {
//            String msg = cmdMessage.msgContent;
//            long msgTime = cmdMessage.msgTime;
//            stateHelperConver.setExtField(EaseConstant.STATE_HELPER + ";" + msg + ";" + unreadCount);
//            sortList.add(new Pair<Long, EMConversation>(msgTime, stateHelperConver));
//        }
        String json = SPUtil.getString(EaseConstant.STATE_HELPER_NAME, "{}");
        ConversationExtBean extBean = Json_U.fromJson(json, ConversationExtBean.class);
        extBean.conversationType = EaseConstant.STATE_HELPER;
        stateHelperConver.setExtField(Json_U.toJson(extBean));
        sortList.add(new Pair<Long, EMConversation>(msgTime, stateHelperConver));

    }

    private void addOrderHelper(List<Pair<Long, EMConversation>> sortList, EMConversation lastConverSation, int orderUnreadCount) {
        EMConversation orderConversation = EMClient.getInstance().chatManager().getConversation(EaseConstant.ORDER_HELPER_NAME,
                EMConversation.EMConversationType.GroupChat, true);
        long msgTime = 0;
        String message = "";
        if (lastConverSation != null) {
            EMMessage lastMessage = lastConverSation.getLastMessage();
            msgTime = lastMessage.getMsgTime();
            EMMessageBody body = lastMessage.getBody();
            if (body instanceof EMTextMessageBody) {
                message = ((EMTextMessageBody) body).getMessage();
            } else if (body instanceof EMImageMessageBody) {
                message = "[图片]";
            } else if (body instanceof EMVoiceMessageBody) {
                message = "[语音]";
            } else if (body instanceof EMVideoMessageBody) {
                message = "[视频]";
            }
        }
        String json = SPUtil.getString(EaseConstant.ORDER_HELPER_NAME, "{}");
        ConversationExtBean extBean = Json_U.fromJson(json, ConversationExtBean.class);
        extBean.conversationType = EaseConstant.ORDER_HELPER;
        extBean.message = message;
        extBean.unReadCount = orderUnreadCount;
        orderConversation.setExtField(Json_U.toJson(extBean));
        sortList.add(new Pair<Long, EMConversation>(msgTime, orderConversation));
    }

    /**
     * sort conversations according time stamp of last searchMessage
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                ConversationExtBean extBean1 = Json_U.fromJson(con1.second.getExtField(), ConversationExtBean.class);
                ConversationExtBean extBean2 = Json_U.fromJson(con2.second.getExtField(), ConversationExtBean.class);
                if (extBean1.isTop && extBean2.isTop) {
                    if (extBean2.topTime > extBean1.topTime)
                        return 1;
                    else
                        return -1;
                } else if (extBean2.isTop) {
                    return 1;
                } else if (extBean1.isTop) {
                    return -1;
                } else if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}
