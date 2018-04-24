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
package com.zzkx.mtool.chat.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BusBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.db.InviteMessgeDao;
import com.zzkx.mtool.chat.runtimepermissions.PermissionsManager;
import com.zzkx.mtool.chat.runtimepermissions.PermissionsResultAction;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.activity.LoginActivity;
import com.zzkx.mtool.view.activity.MessageSearchActivity;
import com.zzkx.mtool.view.fragment.ContactFragment;
import com.zzkx.mtool.view.fragment.StateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

@SuppressLint("NewApi")
public class EaseMainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity";
    @BindView(R.id.shop_top_tab)
    ViewGroup mTopTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private SparseArray<Fragment> mCache;
    private ContactFragment contactListFragment;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private InviteMessgeDao inviteMessgeDao;
    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(EaseMainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new searchMessage
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };
    private TabHelper mTabHelper;
    public boolean isConflict;
    private boolean isCurrentAccountRemoved;
    private int curPage;
    private MyMultiDeviceListener mMyMultiDeviceListener;
    private MyContactListener mMyContactListener;

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (mViewPager.getCurrentItem() == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

//        AtSupportMessage atSupportMessage = new AtSupportMessage();
//        atSupportMessage.stateId = "15130662497243";
//        atSupportMessage.picUrl = "http://59.110.1.160:8081/fileService/uploads/2017/12/12/15130653617171.image";
//        atSupportMessage.nickName = "小可爱";
//        atSupportMessage.atType = 0;
//        atSupportMessage.supportType = 1;
//        sendActionMessage(EaseConstant.ACTION_SUPPORT, Json_U.toJson(atSupportMessage));
    }

    private void sendActionMessage(String aciton, String extraJson) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMsg.setAttribute("action_info", extraJson);
        String action = aciton;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = "hx1511837384922";//发送给某个人
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    @Subscribe
    public void busEvent(BusBean bus) {
        if (bus.action == Const.ACTION_CLEAR_CHAT) {
            updateUnreadLabel();
        }
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {
        initChat();
        mTopTab.setVisibility(View.VISIBLE);
        setMainMenuEnable();
        setMainTitleGone();
        initPager();
        mTabHelper = new TabHelper();
        mTabHelper.bind(mViewPager, mTopTab, new int[]{R.mipmap.ic_top_msg, R.mipmap.ic_active, R.mipmap.ic_contact}
                , new int[]{R.mipmap.ic_top_msg_gray, R.mipmap.ic_active_gray, R.mipmap.ic_contact_gray});

        setSecMenu(new int[]{R.mipmap.ic_add_friend, R.mipmap.ic_zoom_menu}
                , new String[]{"添加好友", "搜        索"}
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        switch (position) {
                            case 0:
                                startActivity(new Intent(EaseMainActivity.this, AddContactActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(EaseMainActivity.this, MessageSearchActivity.class));
                                break;
                        }
                    }
                });
//        if (!DemoHelper.getInstance().isLoggedIn()) {
//            HXLoginUtil.login(this, mStateView);
//        }

    }

    private void initPager() {
        mCache = new SparseArray<>();
        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactFragment();
//        SettingsFragment settingFragment = new SettingsFragment();
        mCache.put(0, conversationListFragment);
        mCache.put(1, new StateFragment());
        mCache.put(2, contactListFragment);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCache.get(position);
            }

            @Override
            public int getCount() {
                return mCache.size();
            }
        });
    }

    private void initChat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so del try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        }
        //make sure activity will not in background if user is logged into another device or removed
//        if (getIntent() != null &&
//                (getIntent().getBooleanExtra(EaseConstantSub.ACCOUNT_REMOVED, false) ||
//                        getIntent().getBooleanExtra(EaseConstantSub.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
//                        getIntent().getBooleanExtra(EaseConstantSub.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
//            DemoHelper.getInstance().logout(false, null);
//            finish();
//            startActivity(new Intent(this, ChatLoginActivity.class));
//            return;
//        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
//            finish();
//            startActivity(new Intent(this, ChatLoginActivity.class));
//            return;
//        }
        // runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();

        showExceptionDialogFromIntent(getIntent());

        inviteMessgeDao = new InviteMessgeDao(this);


        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();


        mMyContactListener = new MyContactListener();
        EMClient.getInstance().contactManager().setContactListener(mMyContactListener);
        EMClient.getInstance().addClientListener(clientListener);
        mMyMultiDeviceListener = new MyMultiDeviceListener();
        EMClient.getInstance().addMultiDeviceListener(mMyMultiDeviceListener);
        //debug purpose only
        registerInternalDebugReceiver();
    }

    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(EaseMainActivity.this, ChatLoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (internalDebugReceiver != null)
            unregisterReceiver(internalDebugReceiver);
        EventBus.getDefault().unregister(this);
        if (broadcastManager != null && broadcastReceiver != null)
            broadcastManager.unregisterReceiver(broadcastReceiver);
        if(mMyMultiDeviceListener != null)
            EMClient.getInstance().removeMultiDeviceListener(mMyMultiDeviceListener);
        if(mMyContactListener!= null)
            EMClient.getInstance().contactManager().removeContactListener(mMyContactListener);

        mCache.clear();

    }

    /**
     * update unread searchMessage count
     */
    public void updateUnreadLabel() {
        //未读消息总数量
        int count = getUnreadMsgCountTotal();
    }

    /**
     * get unread searchMessage count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EaseConstantSub.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(EaseConstantSub.ACTION_GROUP_CHANAGED);

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                updateUnreadAddressLable();
                updateUnreadLabel();
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();


                    String action = intent.getAction();
                    if (action.equals(EaseConstantSub.ACTION_GROUP_CHANAGED)) {
                        if (EaseCommonUtils.getTopActivity(EaseMainActivity.this).equals(GroupsActivity.class.getName())) {
                            GroupsActivity.instance.onResume();
                        }
                    }
                }
                if (contactListFragment != null)
                    contactListFragment.initNet();
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * update the total unread count
     */

    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     * 好友申请等数量
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
        mViewPager.setCurrentItem(0, true);
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(EaseConstantSub.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(EaseConstantSub.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(EaseConstantSub.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(EaseConstantSub.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(EaseConstantSub.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(EaseConstantSub.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(EaseConstantSub.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(EaseConstantSub.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!EaseMainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(EaseMainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(EaseMainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(EaseConstantSub.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(EaseConstantSub.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(EaseConstantSub.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onReload() {

    }

    public void goQrCard(View view) {

    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(EaseMainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);

    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(EaseConstantSub.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }
}
