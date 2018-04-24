package com.zzkx.mtool.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.GroupMemberBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddTagMemberPresenter;
import com.zzkx.mtool.presenter.GroupMemberPresenter;
import com.zzkx.mtool.presenter.GroupOwnerChangePresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.SelectContactAdapter;
import com.zzkx.mtool.view.customview.RectChekBox;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IAddTagMemberView;
import com.zzkx.mtool.view.iview.IGroupMemberView;
import com.zzkx.mtool.view.iview.IOWnerChangeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/4.
 */

public class GroupMemberManageActivity extends BaseActivity implements View.OnClickListener, IAddTagMemberView, IGroupMemberView, IOWnerChangeView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.header_container)
    LinearLayout mHeaderLayout;
    @BindView(R.id.ic_title_check)
    View mTitleCheck;
    private List<EaseUser> mContactList = new ArrayList<>();
    private SelectContactAdapter mAddAdatpter;
    private List<String> mHxIds;
    private String mId;
    private boolean mIsOwner;
    private String mAction;
    private List<GroupMemberBean.DataBean> mMembers;
    private AddTagMemberPresenter mAddTagMemberPresenter;
    private GroupMemberPresenter mGroupMemberPresenter;
    private GroupOwnerChangePresenter mGroupOwnerChangePresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_friend_select;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("群组管理");
        mId = getIntent().getStringExtra("groupId");
        mIsOwner = getIntent().getBooleanExtra("isOwner", false);
        mAction = getIntent().getStringExtra("action");
        GroupMemberBean memberBean = (GroupMemberBean) getIntent().getSerializableExtra("friend");
        if (memberBean != null)
            formatMembers(memberBean);
        mTitleCheck.setOnClickListener(this);
        mAddTagMemberPresenter = new AddTagMemberPresenter(this);
        mGroupMemberPresenter = new GroupMemberPresenter(this);
        mGroupOwnerChangePresenter = new GroupOwnerChangePresenter(this);
        getContactList();
        if (mHxIds != null)
            initListView();
    }

    public void formatMembers(GroupMemberBean memberBean) {
        mMembers = memberBean.data;
        for (GroupMemberBean.DataBean dataBean : mMembers) {
            if (dataBean.ownerType == 1) {
                mMembers.remove(dataBean);
                break;
            }
        }
        if (mMembers != null) {
            mMembers = memberBean.data;
            mHxIds = new ArrayList<>();
            for (GroupMemberBean.DataBean databean : mMembers) {
                mHxIds.add(databean.hxUsername);
            }
        }
    }


    private Map<Integer, View> mViewCache = new HashMap<>();

    public void initListView() {
        mViewCache.clear();
        mAddAdatpter = new SelectContactAdapter(mContactList);
        mAddAdatpter.setAction(mAction);
        mAddAdatpter.setOnCheckChangeListener(new RectChekBox.OnCheckChangeListener() {
            @Override
            public void onChange(RectChekBox chekBox, boolean b) {
                int position = (int) chekBox.getTag();
                EaseUser item = mAddAdatpter.getItem(position);
                item.cusSelected = b;
            }
        });

        mListView.setAdapter(mAddAdatpter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectContactAdapter.ViewHolder holder = (SelectContactAdapter.ViewHolder) view.getTag();
                EaseUser item = mAddAdatpter.getItem(position);
                if ("single_select".equals(mAction)) {
                    handleChangeOwner(item);
                    return;
                }
                if ("add".equals(mAction) && item.cusSelected && mHxIds != null && mHxIds.contains(item.getUsername())) {
                    return;
                }
                item.cusSelected = !item.cusSelected;
                holder.chekcbox.setChecked(item.cusSelected);
                setBottomContainer(item, position);
            }

            private void setBottomContainer(EaseUser item, int position) {
                if (item.cusSelected) {
                    if (mViewCache.get(position) != null) {
                        mHeaderLayout.removeView(mViewCache.get(position));
                        mViewCache.remove(position);
                    }
                    RoundImageView1_1W roundImageView1_1W = new RoundImageView1_1W(GroupMemberManageActivity.this);
                    int size = Dip2PxUtils.dip2px(GroupMemberManageActivity.this, 45);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                    layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(GroupMemberManageActivity.this, 5), 0);
                    roundImageView1_1W.setLayoutParams(layoutParams);
                    GlideUtil.getInstance().display(roundImageView1_1W, item.getAvatar());
                    roundImageView1_1W.setTag(R.id.child_index, position);
                    roundImageView1_1W.setTag(item.getUsername());
                    mViewCache.put(position, roundImageView1_1W);
                    mHeaderLayout.addView(roundImageView1_1W);
                } else {
                    Integer index = null;
                    for (int i = 0; i < mHeaderLayout.getChildCount(); i++) {
                        index = (int) mHeaderLayout.getChildAt(i).getTag(R.id.child_index);
                    }
                    if (index != null) {
                        View view = mViewCache.get(index);
                        mHeaderLayout.removeView(view);
                        mViewCache.remove(index);
                    }
                }
                if (mHeaderLayout.getChildCount() > 0) {
                    mTitleCheck.setVisibility(View.VISIBLE);
                } else {
                    mTitleCheck.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void handleChangeOwner(final EaseUser item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("确定转让给：" + item.getNickname());
        builder.setPositiveButton(getString(R.string.confrim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGroupOwnerChangePresenter.change(item.getUsername(), mId);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected void getContactList() {
        mContactList.clear();
        if ("remove".equals(mAction) || "add_2_tag".equals(mAction) || "single_select".equals(mAction)) {

            if (mHxIds == null) {
                String groupId = getIntent().getStringExtra("userId");
                mGroupMemberPresenter.getMembers(groupId);
                return;
            }

            for (GroupMemberBean.DataBean memberBean : mMembers) {
                EaseUser easeUser = new EaseUser(memberBean.hxUsername);
                easeUser.setNickname(memberBean.nickname);
                easeUser.setAvatar(memberBean.picUrl);
                mContactList.add(easeUser);
            }

        } else {
            Map<String, EaseUser> contactsMap = DemoHelper.getInstance().getContactList();

            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")) {
                    if (!blackList.contains(entry.getKey())) {
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        if (mHxIds != null && mHxIds.contains(user.getUsername()))
                            user.cusSelected = true;
                        mContactList.add(user);
                    }
                }
            }
        }

        // sorting
        Collections.sort(mContactList, new Comparator<EaseUser>() {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_title_check:
                ArrayList<String> ids = new ArrayList<>();
                for (EaseUser user : mContactList) {
                    if (user.cusSelected)
                        ids.add(user.getUsername());
                }
                if (ids.size() > 0) {
                    final String[] strings = ids.toArray(new String[ids.size()]);
                    if ("add".equals(mAction)) {
                        handleAddGroup(strings);
                    } else if ("remove".equals(mAction)) {
                        handleRemove(strings);
                    } else if ("add_2_tag".equals(mAction)) {
                        String labelId = getIntent().getStringExtra(Const.ID);
                        mAddTagMemberPresenter.addMember(labelId, Arrays.asList(strings));
                    }
                }
                break;
        }
    }

    private void handleRemove(final String[] strings) {
        mStateView.setCurrentState(StateView.ResultState.LOADING);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isException = false;
                for (int i = 0; i < strings.length; i++) {
                    try {
                        EMClient.getInstance().groupManager().removeUserFromGroup(mId, strings[i]);
                    } catch (HyphenateException e) {
                        isException = true;
                        e.printStackTrace();
                        ToastUtils.showToast("删除失败");
                    }
                }

                if (isException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                mStateView.setCurrentState(StateView.ResultState.SUCESS);
                            }
                        }
                    });
                } else {
                    setResult(Const.RESULT_SUCESS_CODE);
                    finish();
                }
            }
        }).start();

    }

    public void handleAddGroup(final String[] strings) {
        mStateView.setCurrentState(StateView.ResultState.LOADING);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mIsOwner)
                        EMClient.getInstance().groupManager().addUsersToGroup(mId, strings);
                    else
                        EMClient.getInstance().groupManager().inviteUser(mId, strings, null);//第三个参数：理由
                    setResult(Const.RESULT_SUCESS_CODE);
                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast("添加失败");
                            mStateView.setCurrentState(StateView.ResultState.SUCESS);
                        }
                    });

                }
            }
        }).start();
    }

    @Override
    public void showError(ErrorBean errorBean) {
        ToastUtils.showToast("添加失败，请重试");
    }

    @Override
    protected void onDestroy() {
        if (mContactList != null)
            for (EaseUser user : mContactList) {
                user.cusSelected = false;
            }
        super.onDestroy();
    }

    @Override
    public void showAddResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("添加成功");
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        } else {
            ToastUtils.showToast("添加失败，请重试");
        }
    }

    @Override
    public void showMembers(GroupMemberBean bean) {
        formatMembers(bean);
        getContactList();
        initListView();
    }

    @Override
    public void showResult(BaseBean bean) {

        if (bean.status == 1) {
            ToastUtils.showToast("转让成功");
            setResult(Const.RESULT_SUCESS_CODE);
            finish();
        }else{
            ToastUtils.showToast(bean.msg);
        }
    }
}
