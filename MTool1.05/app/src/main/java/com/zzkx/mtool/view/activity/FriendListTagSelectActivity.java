package com.zzkx.mtool.view.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.GroupChatExtBean;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddTagMemberPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.SelectContactAdapter;
import com.zzkx.mtool.view.customview.RectChekBox;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;
import com.zzkx.mtool.view.iview.IAddTagMemberView;

import java.util.ArrayList;
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

public class FriendListTagSelectActivity extends BaseActivity implements View.OnClickListener, IAddTagMemberView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.header_container)
    LinearLayout mHeaderLayout;
    @BindView(R.id.ic_title_check)
    View mTitleCheck;
    private List<EaseUser> mContactList = new ArrayList<>();
    private SelectContactAdapter mAdapter;
    private AddTagMemberPresenter mAddTagMemberPresenter;
    private int mAction;
    public static final int ACTION_CREATE_GROUP = 1;

    @Override
    public int getContentRes() {
        return R.layout.activity_friend_select;
    }

    private Map<Integer, View> mViewCache = new HashMap<>();

    @Override
    public void initViews() {
        setMainMenuEnable();
        mAction = getIntent().getIntExtra(Const.ACTION, 0);
        if (mAction == ACTION_CREATE_GROUP)
            setMainTitle("创建群聊");
        else
            setMainTitle("添加好友到新标签");
        getContactList();

        mAddTagMemberPresenter = new AddTagMemberPresenter(this);
        mTitleCheck.setOnClickListener(this);
        mAdapter = new SelectContactAdapter(mContactList);
        mAdapter.setOnCheckChangeListener(new RectChekBox.OnCheckChangeListener() {
            @Override
            public void onChange(RectChekBox chekBox, boolean b) {
                int position = (int) chekBox.getTag();
                EaseUser item = mAdapter.getItem(position);
                item.cusSelected = b;
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectContactAdapter.ViewHolder holder = (SelectContactAdapter.ViewHolder) view.getTag();
                EaseUser item = mAdapter.getItem(position);
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
                    RoundImageView1_1W roundImageView1_1W = new RoundImageView1_1W(FriendListTagSelectActivity.this);
                    int size = Dip2PxUtils.dip2px(FriendListTagSelectActivity.this, 45);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                    layoutParams.setMargins(0, 0, Dip2PxUtils.dip2px(FriendListTagSelectActivity.this, 5), 0);
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

    protected void getContactList() {
        mContactList.clear();
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
                    mContactList.add(user);
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
                String groupName = "";
                for (int i = 0; i < mContactList.size(); i++) {
                    EaseUser user = mContactList.get(i);
                    if (user.cusSelected) {
                        ids.add(user.getUsername());
                        groupName += user.getNick() + "、";
                    }
                }
                if (mAction == ACTION_CREATE_GROUP) {
                    createGrouop(groupName, ids);
                } else {
                    String tagId = getIntent().getStringExtra(Const.ID);
                    mAddTagMemberPresenter.addMember(tagId, ids);
                }
                break;
        }
    }

    private void createGrouop(String groupName, final ArrayList<String> ids) {
        showProgress(true);
        if (groupName.endsWith("、"))
            groupName = groupName.substring(0, groupName.length() - 1);
        final String finalGroupName = groupName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] strings = ids.toArray(new String[ids.size()]);
                EMGroupOptions option = new EMGroupOptions();
                option.maxUsers = 200;
                option.inviteNeedConfirm = false;
                String reason = getString(R.string.invite_join_group);
                reason = SPUtil.getString(Const.USER_NICK, "") + reason + finalGroupName;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                GroupChatExtBean extBean = new GroupChatExtBean();
                extBean.groupType = EaseConstant.NORMAL_CHAT;
                extBean.inviterHead = SPUtil.getString(Const.USER_HEADER, "");
                extBean.inviterNick = SPUtil.getString(Const.USER_NICK, "");
                try {
                    EMClient.getInstance().groupManager().createGroup(finalGroupName, Json_U.toJson(extBean), strings, reason, option);
                    FriendListTagSelectActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                showProgress(false);
                                ToastUtils.showToast("创建成功");
                                setResult(Const.RESULT_SUCESS_CODE);
                                finish();
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ToastUtils.showToast("创建失败，请重试");
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
}
