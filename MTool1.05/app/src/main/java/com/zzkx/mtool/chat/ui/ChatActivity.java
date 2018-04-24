package com.zzkx.mtool.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.GroupChatExtBean;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.util.EasyUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ConversationExtBean;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.chat.EaseConstantSub;
import com.zzkx.mtool.chat.runtimepermissions.PermissionsManager;
import com.zzkx.mtool.chat.util.ChatNotifySoundSettingUtil;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.OrderDetailPresenter;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.fragment.InOrderDetailFragment;
import com.zzkx.mtool.view.fragment.OutOrderDetailFragment;
import com.zzkx.mtool.view.iview.IOrderDetailView;

import butterknife.BindView;

/**
 * chat activity，EaseChatFragment was used {@link #}
 */
public class ChatActivity extends BaseActivity implements IOrderDetailView {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTab;
    private SparseArray<Fragment> mCache;
    public static ChatActivity activityInstance;
    private ChatFragment chatFragment;
    String toChatUsername;
    private OrderDetailPresenter mPresenter;
    private String orderId;

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {

        setMainMenuEnable();
        mPresenter = new OrderDetailPresenter(this, this);
        activityInstance = this;
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        mTab.setVisibility(View.VISIBLE);
        mTab.getChildAt(mTab.getChildCount() - 1).setVisibility(View.INVISIBLE);

        initPager();
        TabHelper tabHelper = new TabHelper();
        tabHelper.bind(mViewPager, mTab, new int[]{R.mipmap.ic_top_msg, R.mipmap.ic_top_orderdetail}
                , new int[]{R.mipmap.ic_top_msg_gray, R.mipmap.ic_top_orderdetail_gray});
        initSecMenu();
    }

    public void initSecMenu() {
        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        EMConversation.EMConversationType converType;
        if (chatType == EaseConstant.CHATTYPE_SINGLE)
            converType = EMConversation.EMConversationType.Chat;
        else
            converType = EMConversation.EMConversationType.GroupChat;

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, converType, true);
        String extField = conversation.getExtField();
        if (TextUtils.isEmpty(extField))
            extField = "{}";
        ConversationExtBean extBean = Json_U.fromJson(extField, ConversationExtBean.class);
        String[] titles = new String[3];
        int[] res = new int[3];
        if (extBean.isTop) {
            titles[0] = "取消置顶";
            res[0] = R.mipmap.ic_44;
        } else {
            titles[0] = "置顶";
            res[0] = R.mipmap.ic_43;
        }
        titles[1] = "详细资料";
        res[1] = R.mipmap.ic_38;

        final boolean silentMode = ChatNotifySoundSettingUtil.getSilentMode(toChatUsername);
        if (silentMode) {
            titles[2] = "开启提示音";
            res[2] = R.mipmap.ic_46;
        } else {
            titles[2] = "关闭提示音";
            res[2] = R.mipmap.ic_45;
        }

        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
            setMainTitle(user.getNick());
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            setMainTitle(group.getGroupName());
        }
        setSecMenu(res, titles, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                switch (index) {
                    case 0:
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
                        ConversationExtBean extBean = Json_U.fromJson(conversation.getExtField(), ConversationExtBean.class);
                        extBean.isTop = !extBean.isTop;
                        extBean.topTime = System.currentTimeMillis();
                        conversation.setExtField(Json_U.toJson(extBean));
                        initSecMenu();
                        mMainMenu.dismiss(false);
                        break;
                    case 1:
                        chatFragment.onRightClick();
                        mMainMenu.dismiss(false);
                        break;
                    case 2:
                        ChatNotifySoundSettingUtil.setSilentMode(toChatUsername, !silentMode);
                        initSecMenu();
                        mMainMenu.dismiss(false);
                        break;
                }
//                startActivity(new Intent(ChatActivity.this, OrderEnvaluateActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSecMenu();
    }

    @Override
    public void initNet() {
        if (orderId != null) {
            mPresenter.getOrderInfo(orderId);
        }
    }

    private void initPager() {
        mCache = new SparseArray<>();

        Fragment orderFragment = null;
         orderId = null;
        if (getIntent().getIntExtra("chatType", EaseConstantSub.CHATTYPE_SINGLE) == EaseConstantSub.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            String description = group.getDescription();
            if (description != null) {
                GroupChatExtBean extBean = Json_U.fromJson(description, GroupChatExtBean.class);
                if (extBean.groupType == EaseConstant.ORDER_CHAT) {
                    if (extBean.orderType == CartCacheUtil.TYPE_OUT) {
                        orderFragment = new OutOrderDetailFragment();
                    } else {
                        orderFragment = new InOrderDetailFragment();
                    }
                    int orderType = extBean.orderType;
                    orderId = extBean.orderId;
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ID, orderId);
                    orderFragment.setArguments(bundle);
                    getIntent().putExtra(Const.TYPE, orderType);
                }
            } else {
                ToastUtils.showToast("GroupError");
            }
        }

        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        mCache.put(0, chatFragment);

        if (orderFragment != null) {
            setMainTitleGone();
            mCache.put(1, orderFragment);
        } else {
            mTab.setVisibility(View.INVISIBLE);
        }

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

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, EaseMainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void showData(OrderDetailBean bean) {
        ((IOrderDetailView) mCache.get(1)).showData(bean);
        chatFragment.showOrderData(bean);
        if (bean.data != null)
            chatFragment.onOrderStateChanged(bean.data.status);
    }
}
