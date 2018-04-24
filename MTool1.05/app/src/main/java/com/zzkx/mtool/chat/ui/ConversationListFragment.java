package com.zzkx.mtool.chat.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.util.NetUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ConversationExtBean;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.db.InviteMessgeDao;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.view.activity.OrderHelperActivity;
import com.zzkx.mtool.view.activity.StateHelperActivity;
import com.zzkx.mtool.view.activity.SystemNotiActivity;
import com.zzkx.mtool.view.customview.DialogMoreMenu;
import com.zzkx.mtool.view.iview.IBaseFragment;

public class ConversationListFragment extends EaseConversationListFragment implements IBaseFragment {

    private TextView errorText;
    private DialogMoreMenu mDialogMoreMenu;

    @Override
    protected void initView() {
        super.initView();
        titleBar.setVisibility(View.GONE);
        ((ViewGroup) query.getParent()).setVisibility(View.GONE);
        final View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorItemContainer.setVisibility(View.GONE);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);

        mDialogMoreMenu = new DialogMoreMenu(getActivity(), new DialogMoreMenu.OnMenuClickListener() {
            @Override
            public void onMenuClick(int menuPosition, int listPosition) {
                EMConversation conversation = conversationListView.getItem(listPosition);
                String extField = conversation.getExtField();

                if (!TextUtils.isEmpty(extField)) {
                    switch (menuPosition) {
                        case 0:
                            try {
                                // delete conversation
                                EMClient.getInstance().chatManager().deleteConversation(conversation.conversationId(), true);
                                InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                                inviteMessgeDao.deleteMessage(conversation.conversationId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            refresh();
                            // update unread count
                            if (getActivity() instanceof EaseMainActivity)
                                ((EaseMainActivity) getActivity()).updateUnreadLabel();
                            break;
                        case 1:
                            ConversationExtBean extBean = Json_U.fromJson(extField, ConversationExtBean.class);
                            extBean.isTop = !extBean.isTop;
                            extBean.topTime = System.currentTimeMillis();
                            if (conversation.conversationId().equals(EaseConstant.ORDER_HELPER_NAME)
                                    || conversation.conversationId().equals(EaseConstant.STATE_HELPER_NAME)
                                    || conversation.conversationId().equals(EaseConstant.NEW_FRIEND_NAME)
                                    || conversation.conversationId().equals(EaseConstant.SYSTEM_NOTI_NAME)) {

                                SPUtil.putString(conversation.conversationId(), Json_U.toJson(extBean));
                            } else {
                                conversation.setExtField(Json_U.toJson(extBean));
                            }
                            refresh();
                            break;
                    }
                }
                mDialogMoreMenu.dismiss();
            }
        });
        mDialogMoreMenu.addMenu(R.mipmap.ic_tras_can, "删  除");
        mDialogMoreMenu.addMenu(R.mipmap.ic_43, "置  顶");
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
//        registerForContextMenu(conversationListView);
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mConverType == ORDER_CONVER_LIST)
                    return true;
                EMConversation conversation = conversationListView.getItem(position);
                String extField = conversation.getExtField();

                ConversationExtBean extBean = Json_U.fromJson(extField, ConversationExtBean.class);
                if (extBean.isTop)
                    mDialogMoreMenu.setMenu(R.mipmap.ic_44, "取消置顶", 1);
                else
                    mDialogMoreMenu.setMenu(R.mipmap.ic_43, "置  顶", 1);
                if (conversation.conversationId().equals(EaseConstant.ORDER_HELPER_NAME)
                        || conversation.conversationId().equals(EaseConstant.STATE_HELPER_NAME)
                        || conversation.conversationId().equals(EaseConstant.NEW_FRIEND_NAME)
                        || conversation.conversationId().equals(EaseConstant.SYSTEM_NOTI_NAME)
                        ) {
                    mDialogMoreMenu.setVisiblity(0,View.GONE);
                }else{
                    mDialogMoreMenu.setVisiblity(0,View.VISIBLE);
                }
                mDialogMoreMenu.show(position);
                return true;
            }
        });
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (mConverType == ORDER_CONVER_LIST)
                        intent.putExtra(EaseConstantSub.IS_ORDER_GROUP_CHAT, true);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(EaseConstantSub.EXTRA_CHAT_TYPE, EaseConstantSub.CHATTYPE_CHATROOM);
                        } else if (conversation.getType() == EMConversationType.GroupChat) {
                            intent.putExtra(EaseConstantSub.EXTRA_CHAT_TYPE, EaseConstantSub.CHATTYPE_GROUP);
                        }
                    }
                    // it's single chat
                    String extField = conversation.getExtField();

                    if (!TextUtils.isEmpty(extField)) {
                        intent.putExtra(EaseConstant.EXT_FILDS, extField);
                        ConversationExtBean extBean = Json_U.fromJson(extField, ConversationExtBean.class);
                        if (extBean != null) {
                            switch (extBean.conversationType) {
                                case EaseConstant.ORDER_HELPER:
                                    startActivity(new Intent(getActivity(), OrderHelperActivity.class));
                                    break;
                                case EaseConstant.STATE_HELPER:
                                    startActivity(new Intent(getActivity(), StateHelperActivity.class));
                                    break;
                                case EaseConstant.NEW_FRIEND:
                                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                                    break;
                                case EaseConstant.SYSTEM_NOTI:
                                    startActivity(new Intent(getActivity(), SystemNotiActivity.class));
                                    break;
                                default:
                                    intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                                    startActivity(intent);
                            }
                        } else {
                            intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                            startActivity(intent);
                        }
                    } else {
                        intent.putExtra(EaseConstantSub.EXTRA_USER_ID, username);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        if (getActivity() instanceof EaseMainActivity)
            ((EaseMainActivity) getActivity()).updateUnreadLabel();
        return true;
    }
}
