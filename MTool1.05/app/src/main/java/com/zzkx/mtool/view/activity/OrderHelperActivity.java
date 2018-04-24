package com.zzkx.mtool.view.activity;

import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.ui.ConversationListFragment;
import com.zzkx.mtool.chat.ui.EaseConversationListFragment;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.fragment.NoEnvaluateOrderFragment;
import com.zzkx.mtool.view.iview.IBaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/8.
 */

public class OrderHelperActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTopTab;
    private SparseArray<IBaseFragment> mCache;
    private TabHelper mTabHelper;
    public static OrderHelperActivity instance;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
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
            //red packet code : 处理红包回执透传消息
            //end of red packet code
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

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
            }
        });
    }

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {
        instance = this;
        setMainMenuEnable();
        setMainTitleGone();
        initViewPager();
    }

    private void initViewPager() {
        mCache = new SparseArray<>();
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setConversationListType(EaseConversationListFragment.ORDER_CONVER_LIST);
        mCache.put(0, conversationListFragment);
        mCache.put(1, new NoEnvaluateOrderFragment());
        mTabHelper = new TabHelper();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return (Fragment) mCache.get(position);
            }

            @Override
            public int getCount() {
                return mCache.size();
            }
        });
        mTopTab.getChildAt(2).setVisibility(View.INVISIBLE);
        mTopTab.setVisibility(View.VISIBLE);
        mTabHelper.bind(mViewPager, mTopTab, new int[]{R.mipmap.ic_list_red, R.mipmap.ic_clock_red},
                new int[]{R.mipmap.ic_list_gray, R.mipmap.ic_clock_gray});
    }


    @Override
    public void onReload() {

    }

    @Override
    public void onResume() {
        super.onResume();
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
