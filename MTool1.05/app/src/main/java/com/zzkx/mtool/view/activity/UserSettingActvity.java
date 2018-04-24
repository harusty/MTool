package com.zzkx.mtool.view.activity;

import android.app.ProgressDialog;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BusBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserSettingBean;
import com.zzkx.mtool.chat.db.InviteMessgeDao;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ReadUserSettingPresenter;
import com.zzkx.mtool.presenter.UserSettingPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.CustomSwitch;
import com.zzkx.mtool.view.iview.IUserSettingView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/22.
 */

public class UserSettingActvity extends BaseActivity implements View.OnClickListener, IUserSettingView, CustomSwitch.OnSwitchChangeListener {
    @BindView(R.id.sw_1)
    CustomSwitch mSw1;
    @BindView(R.id.sw_2)
    CustomSwitch mSw2;
    @BindView(R.id.sw_3)
    CustomSwitch mSw3;
    @BindView(R.id.sw_4)
    CustomSwitch mSw4;

    private String mUserId;
    private ReadUserSettingPresenter mReadUserSettingPresenter;
    private String mHxUserName;
    private RequestBean mRequestBean;
    private UserSettingPresenter mUserSettingPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_user_setting;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("用户设置");
        findViewById(R.id.layout_clear_chat).setOnClickListener(this);

        mUserId = getIntent().getStringExtra(Const.ID);

        mHxUserName = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        mReadUserSettingPresenter = new ReadUserSettingPresenter(this);
        mUserSettingPresenter = new UserSettingPresenter(this);
    }

    @Override
    public void initNet() {
        mReadUserSettingPresenter.getSetting(mUserId);
        List<String> blackListUsernames = EMClient.getInstance().contactManager().getBlackListUsernames();
//        mSw1.setSwitch(blackListUsernames.contains(mHxUserName), true);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_clear_chat:
//                EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//                if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
//                    EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
//                }
                try {
                    // delete conversation
                    EMClient.getInstance().chatManager().deleteConversation(mHxUserName, true);
                    InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(this);
                    inviteMessgeDao.deleteMessage(mHxUserName);
                    ToastUtils.showToast("清空成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast("清空失败");
                }
                EventBus.getDefault().post(new BusBean(Const.ACTION_CLEAR_CHAT));
//                refresh();

                // update unread count
//                if (getActivity() instanceof EaseMainActivity)
//                    ((EaseMainActivity) getActivity()).updateUnreadLabel();

                break;
        }
    }

    @Override
    public void showSetting(UserSettingBean bean) {
        UserSettingBean.DataBean data = bean.data;
        if (data != null) {
            mSw1.setSwitch(data.speak == 0);
            mSw2.setSwitch(data.friend2Switch == 0);
            mSw3.setSwitch(data.friend1Switch == 0);
            mSw4.setSwitch(data.shield == 1);

            mSw1.setOnSwitchChangeListener(this);
            mSw2.setOnSwitchChangeListener(this);
            mSw3.setOnSwitchChangeListener(this);
            mSw4.setOnSwitchChangeListener(this);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomSwitch customSwitch = (CustomSwitch) v;
                    onChange(customSwitch, !customSwitch.isSwitchOn());
                }
            };
            mSw1.setOnClickListener(clickListener);
            mSw2.setOnClickListener(clickListener);
            mSw3.setOnClickListener(clickListener);
            mSw4.setOnClickListener(clickListener);

            mRequestBean = new RequestBean();
            mRequestBean.friend2Id = mUserId;
            mRequestBean.speak = data.speak;
            mRequestBean.friend2Switch = data.friend2Switch;
            mRequestBean.friend1Switch = data.friend1Switch;
            mRequestBean.shield = data.shield;
        }
    }

    @Override
    public void showSettingSuccess(UserSettingBean bean) {
        if (bean.status == 1) {
            mSw1.setSwitch(mRequestBean.speak == 0, true);
            mSw2.setSwitch(mRequestBean.friend2Switch == 0, true);
            mSw3.setSwitch(mRequestBean.friend1Switch == 0, true);
            mSw4.setSwitch(mRequestBean.shield == 1, true);
        }
    }

    @Override
    public void onChange(CustomSwitch customSwitch, boolean isOn) {
        switch (customSwitch.getId()) {
            case R.id.sw_1:
                if (!isOn) {
                    mRequestBean.speak = 1;
                    mRequestBean.shield = 0;
                } else {
                    mRequestBean.speak = 0;
                    mRequestBean.shield = mRequestBean.friend2Switch == 0 && mRequestBean.friend1Switch == 0 ? 1 : 0;
                }
//                if (isOn) {
//                    moveToBlacklist(mHxUserName);
//                } else {
//                    removeOutBlacklist(mHxUserName);
//                }
                break;
            case R.id.sw_2:
                if (!isOn) {
                    mRequestBean.friend2Switch = 1;
                    mRequestBean.shield = 0;
                } else {
                    mRequestBean.friend2Switch = 0;
                    mRequestBean.shield = mSw1.isSwitchOn() && mRequestBean.friend1Switch == 0 ? 1 : 0;
                }
                break;
            case R.id.sw_3:
                if (!isOn) {
                    mRequestBean.friend1Switch = 1;
                    mRequestBean.shield = 0;
                } else {
                    mRequestBean.friend1Switch = 0;
                    mRequestBean.shield = mRequestBean.friend2Switch == 0 && mSw1.isSwitchOn() ? 1 : 0;
                }
                break;
            case R.id.sw_4:
                if (isOn) {
                    mRequestBean.speak = 0;
                    mRequestBean.friend2Switch = 0;
                    mRequestBean.friend1Switch = 0;
                    mRequestBean.shield = 1;
                } else {
                    mRequestBean.speak = 1;
                    mRequestBean.friend2Switch = 1;
                    mRequestBean.friend1Switch = 1;
                    mRequestBean.shield = 0;
                }
                break;
        }
        mUserSettingPresenter.setUser(mRequestBean);
    }

    void removeOutBlacklist(final String tobeRemoveUser) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(tobeRemoveUser);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mSw1.setSwitch(false, true);
                            mRequestBean.shield = 0;
                            mUserSettingPresenter.setUser(mRequestBean);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.showToast("操作失败，请重试");
                        }
                    });
                }
            }
        }).start();
    }

    protected void moveToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(this);
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //move to blacklist
                    EMClient.getInstance().contactManager().addUserToBlackList(username, false);
                    UserSettingActvity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            mSw1.setSwitch(true, true);
                            mRequestBean.shield = mRequestBean.friend1Switch == 0 && mRequestBean.friend2Switch == 0 ? 1 : 0;
                            mUserSettingPresenter.setUser(mRequestBean);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    UserSettingActvity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            ToastUtils.showToast("操作失败请重试");
                        }
                    });
                }
            }
        }).start();

    }
}
