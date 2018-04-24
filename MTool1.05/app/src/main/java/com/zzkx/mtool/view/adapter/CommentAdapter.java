package com.zzkx.mtool.view.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CommentListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2017/9/27.
 */

public class CommentAdapter extends BaseAdapter {

    private View.OnClickListener mClickListener;
    private List<Object> mData;

    public CommentAdapter(List<Object> bean, View.OnClickListener suppotClicklistener) {
        mClickListener = suppotClicklistener;
        mData = bean;
    }

    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StateListAdapter.ViewHolder holder;
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            if (itemViewType == 0)
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_title2, null);
            else
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_comment, null);
            holder = new StateListAdapter.ViewHolder(convertView);
            if (itemViewType == 1) {
                convertView.findViewById(R.id.layout_support).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.layout_msgs).setVisibility(View.VISIBLE);
                holder.ic_support2.setOnClickListener(mClickListener);
                holder.ic_msg2.setOnClickListener(mClickListener);
                holder.iv_user_header.setOnClickListener(mClickListener);
                holder.ic_more.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (StateListAdapter.ViewHolder) convertView.getTag();
        }


        if (itemViewType == 0) {
            holder.tv_section.setText(getItem(position).toString());
        } else {
            holder.ic_msg2.setTag(position);
            holder.ic_support2.setTag(position);
            holder.iv_user_header.setTag(R.id.child_index, position);

            CommentListBean.DataBean item = (CommentListBean.DataBean) getItem(position);
            CommentListBean.DataBean.UserMemberBean userMember = item.userMember;
            if (userMember != null) {
                GlideUtil.getInstance().display(holder.iv_user_header, userMember.picUrl);
                holder.tv_user_name.setText(userMember.nickname);
            }
            holder.tv_time.setText(DateUtils.getTimestampString(new Date(item.createTime)));
            if (TextUtils.isEmpty(item.opposes)) {
                holder.tv_msgs2.setText("");
            } else {
                holder.tv_msgs2.setText(item.opposes);
            }

            holder.tv_supports2.setText(item.supports);
            CommentListBean.DataBean.UserMemberReplyBean userMemberReply = item.userMemberReply;
            String aContent = item.content + "";
            if (userMemberReply != null) {
                if (userMemberReply.nickname != null) {
                    String reply = "回复@" + userMemberReply.nickname + "：";
                    SpannableString spannableString = new SpannableString(reply + aContent);
                    spannableString.setSpan(new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.blue))
                            , 2, userMemberReply.nickname.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tv_content.setText(spannableString);
                } else {
                    holder.tv_content.setText(aContent);
                }
            } else {
                holder.tv_content.setText(aContent);
            }
        }
        return convertView;
    }

}
