package com.zzkx.mtool.view.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ShopComentReplyBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2018/1/23.
 */

public class ShopCommentReplyListAdapter extends BaseAdapter {
    private View.OnClickListener mClickListener;
    private List<ShopComentReplyBean.DataBean> mData;
    private FragmentActivity mContext;

    public ShopCommentReplyListAdapter(FragmentActivity activity, List<ShopComentReplyBean.DataBean> totalData, View.OnClickListener clickListener) {
        mClickListener = clickListener;
        mContext = activity;
        mData = totalData;
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
        return position;
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
                holder.ic_support2.setVisibility(View.GONE);
                holder.tv_supports2.setVisibility(View.GONE);
                holder.tv_msgs2.setVisibility(View.GONE);
                holder.ic_msg2.setOnClickListener(mClickListener);
                holder.iv_user_header.setOnClickListener(mClickListener);
                holder.ic_more.setVisibility(View.INVISIBLE);
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

            ShopComentReplyBean.DataBean item = (ShopComentReplyBean.DataBean) getItem(position);
            GlideUtil.getInstance().display(holder.iv_user_header, item.memPicUrl);
            holder.tv_user_name.setText(item.memNickname);
            holder.tv_time.setText(DateUtils.getTimestampString(new Date(item.createTime)));
            String aContent = item.content;
            holder.tv_content.setText(aContent);
        }
        return convertView;
    }
}
