package com.zzkx.mtool.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.GroupChatExtBean;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ConversationExtBean;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.ui.ChatActivity;
import com.zzkx.mtool.chat.ui.NewFriendsMsgActivity;
import com.zzkx.mtool.view.activity.OrderHelperActivity;
import com.zzkx.mtool.view.activity.StateHelperActivity;
import com.zzkx.mtool.view.activity.SystemNotiActivity;
import com.zzkx.mtool.view.iview.IMessageSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by sshss on 2017/11/15.
 */

public class MsgSearchPresenter extends BasePresenter<IMessageSearchView, BaseBean> {
    private List<EMConversation> mEMConversations;
    private List<Object> mSearchResult;

    private static final int SORT_FINISH = 1;
    private static final int SEARCH_FINISH = 2;
    public static final int TYPE_RECENT = 3;
    public static final int TYPE_GROUP = 4;
    public static final int TYPE_SINGLE = 5;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SORT_FINISH:
                    getView().showProgress(false);
                    break;
                case SEARCH_FINISH:
                    getView().showProgress(false);
                    if (mSearchResult.size() > 0)
                        getView().showSearchResult(mSearchResult);
                    else
                        getView().showEmpty();
                    break;
            }
        }
    };

    public MsgSearchPresenter(IMessageSearchView view) {
        super(view);
    }

    public void searchMain(final String key, final int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mEMConversations == null)
                    mEMConversations = sortConversations();

                List<EMConversation> recentConver = new ArrayList<>();
                List<EMConversation> singleConver = new ArrayList<>();
                List<EMConversation> groupConver = new ArrayList<>();

                EMGroupManager emGroupManager = EMClient.getInstance().groupManager();
                for (EMConversation conversation : mEMConversations) {
                    if (conversation.getAllMsgCount() == 0)
                        continue;

                    if (recentConver.size() < num) {
                        setExtBean(emGroupManager, conversation, recentConver, key, TYPE_RECENT);
                    }

                    if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                        if (groupConver.size() < num) {
                            setExtBean(emGroupManager, conversation, groupConver, key, TYPE_GROUP);
                        }
                    } else if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                        if (singleConver.size() < num) {
                            setExtBean(emGroupManager, conversation, singleConver, key, TYPE_SINGLE);
                        }
                    }
                }
                if (mSearchResult == null)
                    mSearchResult = new ArrayList<>();
                else
                    mSearchResult.clear();

                if (recentConver.size() > 0) {
                    ConversationExtBean extBean1 = new ConversationExtBean();
                    extBean1.searchType = TYPE_RECENT;
                    extBean1.message = "最近使用过的";
                    mSearchResult.add(extBean1);
                    mSearchResult.addAll(recentConver);
                }
                if (singleConver.size() > 0) {
                    ConversationExtBean extBean2 = new ConversationExtBean();
                    extBean2.message = "聊天记录";
                    extBean2.searchType = TYPE_SINGLE;
                    mSearchResult.add(extBean2);
                    mSearchResult.addAll(singleConver);
                }
                if (groupConver.size() > 0) {
                    ConversationExtBean extBean3 = new ConversationExtBean();
                    extBean3.message = "群  聊";
                    extBean3.searchType = TYPE_GROUP;
                    mSearchResult.add(extBean3);
                    mSearchResult.addAll(groupConver);
                }

//                for (int i = 0; i < recentConver.size(); i++) {
//                    EMConversation conversation = recentConver.get(i);
//                    ConversationExtBean extBean = Json_U.fromJson(conversation.getExtField(), ConversationExtBean.class);
//                    System.out.println("recent " + extBean.searchName + "  " + extBean.searchMessage);
//                }
//
//                for (int i = 0; i < singleConver.size(); i++) {
//                    EMConversation conversation = singleConver.get(i);
//                    ConversationExtBean extBean = Json_U.fromJson(conversation.getExtField(), ConversationExtBean.class);
//                    System.out.println("singleConver " + extBean.searchName + "  " + extBean.searchMessage);
//                }
//                for (int i = 0; i < groupConver.size(); i++) {
//                    EMConversation conversation = groupConver.get(i);
//                    ConversationExtBean extBean = Json_U.fromJson(conversation.getExtField(), ConversationExtBean.class);
//                    System.out.println("groupConver " + extBean.searchName + "  " + extBean.searchMessage);
//                }
                mHandler.sendEmptyMessage(SEARCH_FINISH);

            }
        }).start();
    }

    private int mCurIndex;

    public void searchSingle(final String key, final int pageCount, final int pageNum, final int type) {

        if (pageNum == 1)
            mCurIndex = 0;


        if (mEMConversations != null && mCurIndex == mEMConversations.size() - 1) {
            if (mSearchResult == null)
                mSearchResult = new ArrayList<>();
            else
                mSearchResult.clear();
            mHandler.sendEmptyMessage(SEARCH_FINISH);
            return;
        }

        getView().showProgress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mEMConversations == null)
                    mEMConversations = sortConversations();

                List<EMConversation> result = new ArrayList<>();
                EMGroupManager emGroupManager = EMClient.getInstance().groupManager();
                for (int i = mCurIndex; i < mEMConversations.size(); i++) {
                    mCurIndex = i;
                    EMConversation conversation = mEMConversations.get(i);
                    if (conversation.getAllMsgCount() == 0)
                        continue;

                    if (result.size() < pageCount) {

                        boolean flag = false;
                        if (type == TYPE_GROUP && conversation.getType() == EMConversation.EMConversationType.GroupChat)
                            flag = true;
                        else if (type == TYPE_SINGLE && conversation.getType() == EMConversation.EMConversationType.Chat) {
                            flag = true;
                        } else if (type == TYPE_RECENT) {
                            flag = true;
                        }

                        if (flag)
                            setExtBean(emGroupManager, conversation, result, key, type);
                    } else {
                        break;
                    }
                }

                if (mSearchResult == null)
                    mSearchResult = new ArrayList<>();
                else
                    mSearchResult.clear();
                mSearchResult.addAll(result);
                mHandler.sendEmptyMessage(SEARCH_FINISH);

            }
        }).start();
    }

    private void setExtBean(EMGroupManager emGroupManager, EMConversation conversation,
                            List<EMConversation> conversations, String key, int type) {
        List<EMMessage> messages = conversation.searchMsgFromDB(key, System.currentTimeMillis(), conversation.getAllMsgCount(), null, EMConversation.EMSearchDirection.UP);
        if (messages != null && messages.size() > 0) {
            conversations.add(conversation);
            String extField = conversation.getExtField();
            ConversationExtBean extBean;
            if (!TextUtils.isEmpty(extField)) {
                extBean = Json_U.fromJson(extField, ConversationExtBean.class);
            } else {
                extBean = new ConversationExtBean();
            }
            extBean.searchType = type;
            if (messages.size() == 1) {
                EMMessage message = messages.get(0);
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    String from = message.getFrom();
                    extBean.searchName = emGroupManager.getGroup(conversation.conversationId()).getGroupName();
                    extBean.searchMessage = from + ":" + ((EMTextMessageBody) message.getBody()).getMessage();
                } else {
                    extBean.searchName = conversation.conversationId();
                    extBean.searchMessage = ((EMTextMessageBody) message.getBody()).getMessage();
                }
            } else {
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    extBean.searchName = emGroupManager.getGroup(conversation.conversationId()).getGroupName();
                } else {
                    extBean.searchName = conversation.conversationId();
                }
                extBean.searchMessage = messages.size() + "条相关的聊天记录";
            }
            conversation.setExtField(Json_U.toJson(extBean));
        }
    }

    private List<EMConversation> sortConversations() {

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMsgCount() == 0)
                continue;
            long msgTime = conversation.getLastMessage().getMsgTime();
            sortList.add(new Pair<Long, EMConversation>(msgTime, conversation));
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

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(SORT_FINISH);
    }

    @Override
    public void onSuccessM(BaseBean bean) {

    }

    public void onItemClick(Object item) {
        if (!(item instanceof ConversationExtBean)) {
            Activity activity = getView().getActivity();
            EMConversation conversation = (EMConversation) item;
            String username = conversation.conversationId();
            if (username.equals(EMClient.getInstance().getCurrentUser()))
                Toast.makeText(activity, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(activity, ChatActivity.class);
//                        if (mConverType == ORDER_CONVER_LIST)
//                            intent.putExtra(Constant.IS_ORDER_GROUP_CHAT, true);
                if (conversation.isGroup()) {
                    if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                        intent.putExtra(EaseConstantSub.EXTRA_CHAT_TYPE, EaseConstantSub.CHATTYPE_CHATROOM);
                    } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                        EMGroup group = EMClient.getInstance().groupManager().getGroup(conversation.conversationId());
                        String description = group.getDescription();
                        if (!TextUtils.isEmpty(description)) {
                            GroupChatExtBean groupChatExtBean = Json_U.fromJson(description, GroupChatExtBean.class);
                            if (groupChatExtBean.groupType == EaseConstant.ORDER_CHAT)
                                intent.putExtra(EaseConstantSub.IS_ORDER_GROUP_CHAT, true);
                        }
                        intent.putExtra(EaseConstantSub.EXTRA_CHAT_TYPE, EaseConstantSub.CHATTYPE_GROUP);
                    }
                }
                String extField = conversation.getExtField();
                if (!TextUtils.isEmpty(extField)) {
                    intent.putExtra(EaseConstant.EXT_FILDS, extField);
                    System.out.println("extField: " + extField);
                    ConversationExtBean extBean = Json_U.fromJson(extField, ConversationExtBean.class);
                    if (extBean != null) {
                        int type = extBean.conversationType;
                        switch (type) {
                            case EaseConstant.ORDER_HELPER:
                                activity.startActivity(new Intent(activity, OrderHelperActivity.class));
                                break;
                            case EaseConstant.STATE_HELPER:
                                activity.startActivity(new Intent(activity, StateHelperActivity.class));
                                break;
                            case EaseConstant.NEW_FRIEND:
                                activity.startActivity(new Intent(activity, NewFriendsMsgActivity.class));
                                break;
                            case EaseConstant.SYSTEM_NOTI:
                                activity.startActivity(new Intent(activity, SystemNotiActivity.class));
                            default:
                                intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                                activity.startActivity(intent);
                        }
                    } else {
                        intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                        activity.startActivity(intent);
                    }
                } else {
                    intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                    activity.startActivity(intent);
                }
            }
        }
    }

    public boolean isSearchAll() {
        return mEMConversations == null || mEMConversations.size() == 0 ||
                mCurIndex == mEMConversations.size() - 1;
    }
}
