package com.zzkx.mtool.chat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.GroupMemberBean;
import com.zzkx.mtool.chat.util.ChatNotifySoundSettingUtil;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.GroupMemberPresenter;
import com.zzkx.mtool.presenter.GroupOwnerChangePresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.activity.GroupMemberManageActivity;
import com.zzkx.mtool.view.customview.CustomSwitch;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.customview.SimpleDialog;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IGroupMemberView;
import com.zzkx.mtool.view.iview.IOWnerChangeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/6.
 */

public class GroupDetailActivity extends BaseActivity implements View.OnClickListener, IGroupMemberView, IOWnerChangeView {
    @BindView(R.id.layout_group_detail_user)
    View mViewUser;
    @BindView(R.id.layout_group_detail_creater)
    View mViewCreater;
    @BindView(R.id.tv_announcement)
    TextView mTvGroupAnnounce;
    @BindView(R.id.tv_group_name)
    TextView mTvGroupName;
    @BindView(R.id.grid)
    GridView mGridView;
    @BindView(R.id.tv_info)
    TextView mTvInfo;

    private String groupId;
    private EMGroup group;
    private CustomSwitch mSwitchNotice;
    private SimpleDialog mDilogNick;
    private SimpleDialog mIntroDialog;
    private AlertDialog mDissorveDialog;
    private GroupMemberAdapter groupMemberAdapter;
    private static final int REQUEST_ADD = 10;
    private static final int REQUEST_REMOVE = 11;
    private static final int REQUEST_SINGLE_SELECT = 12;
    private GroupMemberPresenter mGroupMemberPresenter;
    private GroupOwnerChangePresenter mGroupOwnerChangePresenter;
    private GroupMemberBean mGroupMembersBean;

    @Override
    public int getContentRes() {
        return R.layout.activity_group_detail;
    }


    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("群聊管理");

        groupId = getIntent().getStringExtra("groupId");
        EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
        mGroupMemberPresenter = new GroupMemberPresenter(this);
        mGroupOwnerChangePresenter = new GroupOwnerChangePresenter(this);
        setGroupDetailInfo();

    }

    public void initBaseInfo() {
        if (isCurrentOwner()) {
            mViewCreater.setVisibility(View.VISIBLE);
            mViewUser.setVisibility(View.GONE);
            mSwitchNotice = (CustomSwitch) mViewCreater.findViewById(R.id.switch_notification);
            mViewCreater.findViewById(R.id.iv_edit_group_name).setOnClickListener(this);
            mViewCreater.findViewById(R.id.iv_edit_group_notice).setOnClickListener(this);
            mViewCreater.findViewById(R.id.tv_transfer).setOnClickListener(this);
            mViewCreater.findViewById(R.id.tv_delete).setOnClickListener(this);
        } else {
            mViewCreater.setVisibility(View.GONE);
            mViewUser.setVisibility(View.VISIBLE);
            mSwitchNotice = (CustomSwitch) mViewUser.findViewById(R.id.switch_notification);
            mViewUser.findViewById(R.id.tv_out).setOnClickListener(this);
            mViewUser.findViewById(R.id.tv_report).setOnClickListener(this);
        }
        boolean silentMode = ChatNotifySoundSettingUtil.getSilentMode(groupId);
        mSwitchNotice.setSwitch(silentMode);
        //关闭提示音
        mSwitchNotice.setOnSwitchChangeListener(new CustomSwitch.OnSwitchChangeListener() {
            @Override
            public void onChange(CustomSwitch customSwitch, boolean change) {
                ChatNotifySoundSettingUtil.setSilentMode(groupId, change);
            }
        });
    }

    @Override
    public void initNet() {
        mGroupMemberPresenter.getMembers(groupId);
    }

    private void setGroupDetailInfo() {

        mStateView.setCurrentState(StateView.ResultState.LOADING);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //第二个参数 是否获取群组成员信息
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId, false);

                    EMClient.getInstance().groupManager().fetchGroupAnnouncement(groupId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing() && group != null) {
                                mTvGroupName.setText(group.getGroupName());
                                initBaseInfo();
//                                mTvInfo.setText(group.getMemberCount() + "名成员/创建人：" + ownerNick);
                                mTvGroupAnnounce.setText(group.getAnnouncement());
                                mStateView.setCurrentState(StateView.ResultState.SUCESS);
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                ToastUtils.showToast("获取群组详情失败");
                                mStateView.setCurrentState(StateView.ResultState.ERROR);
                            }
                        }
                    });

                }
            }
        }).start();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            setGroupDetailInfo();
            if (requestCode == REQUEST_SINGLE_SELECT) {
                mGroupMemberPresenter.getMembers(group.getGroupId());
            }
        }
    }

    @Override
    public void showMembers(final GroupMemberBean bean) {
        mGroupMembersBean = bean;
        final List<GroupMemberBean.DataBean> members = bean.data;
        List<Object> data = new ArrayList<>();
        if (members != null) {
            for (GroupMemberBean.DataBean dataBean : members) {
                if (dataBean.ownerType == 1) {
                    mTvInfo.setText(members.size() + "名成员/创建人：" + dataBean.nickname);
                    members.remove(dataBean);
                    break;
                }
            }

            data.addAll(members);
            data.add(0, "add");
            if (isCurrentOwner()) {
                data.add(1, "remove");
            }

            groupMemberAdapter = new GroupMemberAdapter(this, data);
            mGridView.setAdapter(groupMemberAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = groupMemberAdapter.getItem(position);
                    if (item instanceof String && "add".equals(item.toString())) {
                        startActivityForResult(new Intent(GroupDetailActivity.this, GroupMemberManageActivity.class)
                                        .putExtra("groupId", groupId)
                                        .putExtra("friend", mGroupMembersBean)
                                        .putExtra("isOwner", isCurrentOwner())
                                        .putExtra("action", "add")
                                , REQUEST_ADD);
                    } else if (item instanceof String && "remove".equals(item.toString())) {
                        startActivityForResult(new Intent(GroupDetailActivity.this, GroupMemberManageActivity.class)
                                        .putExtra("groupId", groupId)
                                        .putExtra("friend", mGroupMembersBean)
                                        .putExtra("isOwner", isCurrentOwner())
                                        .putExtra("action", "remove")
                                , REQUEST_REMOVE);
                    } else {

                    }
                }
            });
        }
    }

    @Override
    public void showResult(BaseBean bean) {

    }

    private class GroupMemberAdapter extends ArrayAdapter<Object> {

        private Context mContext;
        private List<Object> mData;

        public GroupMemberAdapter(@NonNull Context context, List<Object> members) {
            super(context, 0);
            mContext = context;
            mData = members;
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        public List<Object> getData() {
            return mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(GroupDetailActivity.this, R.layout.item_suppor_user, null);
            }

            Object obj = mData.get(position);
            RoundImageView1_1W iv_user_header = (RoundImageView1_1W) convertView.findViewById(R.id.iv_user_header);
            TextView tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            if (obj instanceof String && "add".equals(obj.toString())) {
                iv_user_header.setBorderRadius(0);
                iv_user_header.setStrokeWith(0);
                iv_user_header.setImageResource(R.mipmap.ic_19);
                tv_user_name.setText("添加成员");
            } else if (obj instanceof String && "remove".equals(obj.toString())) {
                iv_user_header.setBorderRadius(0);
                iv_user_header.setStrokeWith(0);
                iv_user_header.setImageResource(R.mipmap.ic_20);
                tv_user_name.setText("删除成员");
            } else {
                GroupMemberBean.DataBean dataBean = (GroupMemberBean.DataBean) obj;
                GlideUtil.getInstance().display(iv_user_header, dataBean.picUrl);
                tv_user_name.setText(dataBean.nickname);
            }
            return convertView;
        }
    }

    boolean isCurrentOwner() {
        if (group == null)
            group = EMClient.getInstance().groupManager().getGroup(groupId);
        String owner = group.getOwner();
        if (owner == null || owner.isEmpty()) {
            return false;
        }
        return owner.equals(EMClient.getInstance().getCurrentUser());
    }

    @Override
    public void onReload() {
        setGroupDetailInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_out://退出群聊
                mStateView.setCurrentState(StateView.ResultState.LOADING);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().leaveGroup(groupId);
                        } catch (HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast("退出失败，请重试");
                                }
                            });

                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mStateView.setCurrentState(StateView.ResultState.SUCESS);
                                if (ChatActivity.activityInstance != null)
                                    ChatActivity.activityInstance.finish();
                                finish();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.tv_report://举报该群
                startActivity(new Intent(this, ComplainActivity.class)
                        .putExtra(Const.ID, groupId)
                        .putExtra(Const.TYPE, 2)
                );
                break;
            case R.id.iv_edit_group_name://群名称

                if (mDilogNick == null) {
                    initNickDialog();
                }

                mDilogNick.show();
                break;
            case R.id.iv_edit_group_notice://群公告设置
                if (mIntroDialog == null) {
                    initIntroDialog();
                }
                mIntroDialog.show();
                break;
            case R.id.tv_transfer://转让
                startActivityForResult(new Intent(GroupDetailActivity.this, GroupMemberManageActivity.class)
                                .putExtra("groupId", groupId)
                                .putExtra("friend", mGroupMembersBean)
                                .putExtra("isOwner", isCurrentOwner())
                                .putExtra("action", "single_select")
                        , REQUEST_SINGLE_SELECT);
                break;
            case R.id.tv_delete://删除

                if (mDissorveDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setMessage("确定解散该群？");
                    builder.setPositiveButton(getString(R.string.confrim), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mStateView.setCurrentState(StateView.ResultState.LOADING);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EMClient.getInstance().groupManager().destroyGroup(groupId);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (ChatActivity.activityInstance != null)
                                                    ChatActivity.activityInstance.finish();
                                                if (!isFinishing()) {
                                                    finish();
                                                }
                                            }
                                        });
                                    } catch (HyphenateException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!isFinishing()) {
                                                    ToastUtils.showToast("删除群聊失败，请重试");
                                                    mStateView.setCurrentState(StateView.ResultState.SUCESS);
                                                }
                                            }
                                        });
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mDissorveDialog = builder.create();
                }
                mDissorveDialog.show();

                break;


        }
    }

    private void initIntroDialog() {
        mIntroDialog = new SimpleDialog(this, R.layout.dialog_group_announcement);
        View view = mIntroDialog.getView();
        Holder holder = new Holder(view);
        view.setTag(holder);
        holder.tv_title.setText("设置群公告");
        holder.et_input.setHint("群聊公告呢容");
        holder.tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroDialog.dismiss();
            }
        });
        holder.tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Holder holder = (Holder) mIntroDialog.getView().getTag();
                final String s = holder.et_input.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("请输入公告内容");
                } else {
                    mStateView.setCurrentState(StateView.ResultState.LOADING);
                    mIntroDialog.dismiss();
                    EMClient.getInstance().groupManager().asyncUpdateGroupAnnouncement(groupId, s, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isFinishing()) {
                                        mStateView.setCurrentState(StateView.ResultState.SUCESS);
                                        mTvGroupAnnounce.setText(s);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onError(final int i, final String s) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isFinishing()) {
                                        mStateView.setCurrentState(StateView.ResultState.SUCESS);
                                        ToastUtils.showToast("修改失败，请重试");
                                        Log.e(GroupDetailActivity.class.getSimpleName(), i + " " + s);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
            }
        });
    }

    private void initNickDialog() {
        mDilogNick = new SimpleDialog(this, R.layout.dialog_nick);
        View view = mDilogNick.getView();
        Holder holder = new Holder(view);
        view.setTag(holder);
        holder.tv_title.setText("群聊名称");
        holder.et_input.setHint("设置群聊名称");
        holder.tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDilogNick.dismiss();
            }
        });
        holder.tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Holder holder = (Holder) mDilogNick.getView().getTag();
                mDilogNick.dismiss();
                final String s = holder.et_input.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("请输入群聊名称");
                } else {
                    mStateView.setCurrentState(StateView.ResultState.LOADING);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().changeGroupName(groupId, s);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isFinishing()) {
                                            mTvGroupName.setText(s);
                                            ToastUtils.showToast("修改成功");
                                            mStateView.setCurrentState(StateView.ResultState.SUCESS);
                                        }
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ToastUtils.showToast("修改名称失败，请重试");
                                mStateView.setCurrentState(StateView.ResultState.SUCESS);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {

        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onInvitationAccepted(String s, String s1, String s2) {

        }

        @Override
        public void onInvitationDeclined(String s, String s1, String s2) {

        }

        @Override
        public void onUserRemoved(String s, String s1) {

        }

        @Override
        public void onGroupDestroyed(String s, String s1) {

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {

        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {

        }

        @Override
        public void onAdminAdded(String s, String s1) {

        }

        @Override
        public void onAdminRemoved(String s, String s1) {

        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String groupId, final String announcement) {
            if (groupId.equals(GroupDetailActivity.this.groupId)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvGroupAnnounce.setText(announcement);
                    }
                });
            }
        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    }

    private class Holder {
        TextView tv_title;
        EditText et_input;
        View tv_back;
        View tv_confirm;

        public Holder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            et_input = (EditText) view.findViewById(R.id.et_input);
            tv_back = view.findViewById(R.id.tv_back);
            tv_confirm = view.findViewById(R.id.tv_confirm);
        }
    }
}
