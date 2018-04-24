package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.ui.BlacklistActivity;
import com.zzkx.mtool.chat.ui.GroupsActivity;
import com.zzkx.mtool.chat.ui.NewFriendsMsgActivity;
import com.zzkx.mtool.view.activity.AllHelperActivity;
import com.zzkx.mtool.view.activity.TagsActivity;
import com.zzkx.mtool.view.customview.DialogInitialNab;
import com.zzkx.mtool.view.customview.DialogSort;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/19.
 */

public class ContactFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.icon_indicator)
    View mIndicatorView;
    @BindView(R.id.icon_red_arrow)
    View mRedEye;
    @BindView(R.id.scrolable_layout)
    ScrollableLayout mScrollableLayout;

    private DialogInitialNab mDialogInitialNab;
    private DialogSort mDialogType;
    private FragmentActivity mActivity;
    private ContactSubFragment mCurFragment;
    private FanslistFragment mFanslistFragment;
    private FriendFragment mFriendFragment;
    private IdolFragment mIdolFragment;

    @Override
    public int getContentRes() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = getActivity();


        mRedEye.setOnClickListener(this);
        mDialogType = new DialogSort(getActivity(), new int[]{R.mipmap.ic_24_c, R.mipmap.ic_25_c, R.mipmap.ic_26_c, R.mipmap.ic_27_c}
                , new int[]{R.mipmap.ic_24, R.mipmap.ic_25, R.mipmap.ic_26, R.mipmap.ic_27}
                , new String[]{"我的朋友", "我的网友", "我关注的", "关注我的"}
        );
        mDialogType.setOnSortListener(new DialogSort.OnSortListener() {


            @Override
            public boolean onSort(DialogSort.ViewHolder sortKey) {
                if (mCurFragment != null && mCurFragment.isAdded())
                    getFragmentManager().beginTransaction().hide(mCurFragment).commit();
                switch (sortKey.position) {
                    case 0:
                        if (mFriendFragment == null) {
                            mFriendFragment = new FriendFragment();
                            getFragmentManager().beginTransaction().add(R.id.fr_contact_container, mFriendFragment).show(mFriendFragment).commit();
                        } else {
                            getFragmentManager().beginTransaction().show(mFriendFragment).commit();
                        }
                        mCurFragment = mFriendFragment;
                        break;
                    case 1:
                        break;
                    case 2:
                        if (mIdolFragment == null) {
                            mIdolFragment = new IdolFragment();
                            getFragmentManager().beginTransaction().add(R.id.fr_contact_container, mIdolFragment).show(mIdolFragment).commit();
                        } else {
                            getFragmentManager().beginTransaction().show(mIdolFragment).commit();
                        }
                        mCurFragment = mIdolFragment;
                        break;
                    case 3:
                        if (mFanslistFragment == null) {
                            mFanslistFragment = new FanslistFragment();
                            getFragmentManager().beginTransaction().add(R.id.fr_contact_container, mFanslistFragment).show(mFanslistFragment).commit();
                        } else {
                            getFragmentManager().beginTransaction().show(mFanslistFragment).commit();
                        }
                        mCurFragment = mFanslistFragment;
                        break;
                }
                mDialogType.dismiss();
                return true;
            }
        });

        mBaseView.findViewById(R.id.layout_new_friend).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_group).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_black_list).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_all_helper).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_tags).setOnClickListener(this);
        mIndicatorView.setOnClickListener(this);

        mFriendFragment = new FriendFragment();
        mCurFragment = mFriendFragment;
        getFragmentManager().beginTransaction().add(R.id.fr_contact_container, mFriendFragment).commit();
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mFriendFragment);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.layout_new_friend:
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                break;
            case R.id.layout_group:
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            case R.id.layout_black_list:
                startActivity(new Intent(getActivity(), BlacklistActivity.class));
                break;
            case R.id.layout_all_helper:
                startActivity(new Intent(getActivity(), AllHelperActivity.class));
                break;
            case R.id.layout_tags:
                startActivity(new Intent(getActivity(), TagsActivity.class));
                break;
            case R.id.icon_indicator:
                if (mDialogInitialNab == null) {
                    mDialogInitialNab = new DialogInitialNab(getActivity());
                    mDialogInitialNab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mScrollableLayout.scrollTo(0, mScrollableLayout.getHeadHeight());
                            mCurFragment.onInitialClick(view);
                            mDialogInitialNab.dismiss();
                        }
                    });
                }
                mDialogInitialNab.show(mIndicatorView);
                break;
            case R.id.icon_red_arrow:
                mDialogType.toggleRightFilter(mRedEye);
                break;
        }
    }
}
