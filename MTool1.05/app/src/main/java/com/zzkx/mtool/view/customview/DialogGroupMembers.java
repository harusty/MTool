package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.GroupMemberBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.presenter.GroupMemberPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IGroupMemberView;

import java.util.List;

/**
 * Created by sshss on 2017/12/27.
 */

public class DialogGroupMembers extends SimpleDialog implements View.OnClickListener, IGroupMemberView {

    private View mProgress;
    private OnMemberSelecteListener mListener;
    private Context mContext;
    private GroupMemberPresenter mGroupMemberPresenter;
    private ListView mListView;
    private MemberAdapter mAdapter;
    private List<GroupMemberBean.DataBean> mData;
    private int mLastCheckPosition = -1;

    public DialogGroupMembers(Context context, OnMemberSelecteListener listener) {
        super(context, R.layout.dialog_group_members);
        mListener = listener;
        mContext = context;
        mListView = (ListView) mDialogView.findViewById(R.id.lv_list);
        mDialogView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mDialogView.findViewById(R.id.tv_confirm).setOnClickListener(this);
        mProgress = mDialogView.findViewById(R.id.progress);
        mGroupMemberPresenter = new GroupMemberPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                if (mLastCheckPosition != -1) {
                    String hxUsername = mData.get(mLastCheckPosition).hxUsername;
                    mListener.onMemberSelect(hxUsername);
                    dismiss();
                } else {
                    ToastUtils.showToast("请选择语音通话成员");
                }
                break;
        }
    }

    public void show(String groupId) {
        super.show();
        if (mAdapter == null)
            mGroupMemberPresenter.getMembers(groupId);
    }

    @Override
    public void showProgress(boolean toShow) {
        if (toShow)
            mProgress.setVisibility(View.VISIBLE);
        else
            mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(ErrorBean errorBean) {
        ToastUtils.showToast("网络错误，请重试");
        dismiss();
    }

    @Override
    public void showMembers(GroupMemberBean bean) {
        mData = bean.data;
        if (mData != null) {
            mAdapter = new MemberAdapter();
            for (GroupMemberBean.DataBean dataBean : mData) {
                if(DemoHelper.getInstance().getCurrentUsernName().equals(dataBean.hxUsername)) {
                    mData.remove(dataBean);
                    break;
                }
            }
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GroupMemberBean.DataBean dataBean = mData.get(position);
                    dataBean.cusChecked = !dataBean.cusChecked;
                    if (mLastCheckPosition != -1)
                        mData.get(mLastCheckPosition).cusChecked = false;
                    if (dataBean.cusChecked)
                        mLastCheckPosition = position;
                    else if (!dataBean.cusChecked && mLastCheckPosition == position) {
                        mLastCheckPosition = -1;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class MemberAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = View.inflate(mContext, R.layout.item_group_member, null);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            ImageView iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            RectChekBox checkbox = (RectChekBox) convertView.findViewById(R.id.checkbox);
            GroupMemberBean.DataBean dataBean = mData.get(position);
            tv_name.setText(dataBean.nickname);
            GlideUtil.getInstance().display(iv_head, dataBean.picUrl);
            checkbox.setChecked(dataBean.cusChecked);
            checkbox.setTag(position);
            return convertView;
        }
    }

    public interface OnMemberSelecteListener {
        void onMemberSelect(String hxId);
    }
}
