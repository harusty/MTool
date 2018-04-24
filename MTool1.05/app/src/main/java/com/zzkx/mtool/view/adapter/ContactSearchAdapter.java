package com.zzkx.mtool.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.SearchContactBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2017/11/15.
 */

public class ContactSearchAdapter extends BaseAdapter {
    private final List<Object> mData;
    private onMoreListener mListener;

    public ContactSearchAdapter(List<Object> searchResult) {
        mData = searchResult;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof String) {
            return 0;
        } else {
            return ((SearchContactBean.FanListBean) mData.get(position)).cusType;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int itemViewType = getItemViewType(position);
        if (convertView == null) {
            if (itemViewType == 0) {
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_message_search_title, null);
            } else {
                convertView = View.inflate(MyApplication.getContext(), R.layout.ease_row_contact, null);
            }
            holder = new ViewHolder(convertView);
            if (holder.layout_more != null)
                holder.layout_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onMoreClick(itemViewType);
                    }
                });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (itemViewType == 0) {
            if (position == 0)
                holder.layout_more.setVisibility(View.VISIBLE);
            else
                holder.layout_more.setVisibility(View.INVISIBLE);
            holder.title.setText(((String) mData.get(position)));
        } else {
            SearchContactBean.FanListBean contactBaseInfoBean = (SearchContactBean.FanListBean) mData.get(position);

            switch (itemViewType) {
                case Const.FRIEND:
                case Const.FAN:
                case Const.IDOLE:
                    SearchContactBean.UserMemberBean userMember = contactBaseInfoBean.userMember;
                    if (userMember != null) {
                        holder.name.setText(userMember.nickname);
                        if(!TextUtils.isEmpty(userMember.picUrl)){
                            GlideUtil.getInstance().display(holder.avatar,userMember.picUrl);
                        }
                    }

                    break;
                case Const.GROUP:
                    SearchContactBean.UserMemberBean chatGroup = contactBaseInfoBean.chatGroup;
                    if (chatGroup != null) {
                        holder.name.setText(chatGroup.name);
                    }
                    holder.layout_more.setVisibility(View.INVISIBLE);
                    break;
            }
//            EaseUserUtils.setUserAvatar(MyApplication.getContext(), conversation.conversationId(), holder.avatar);
//            EaseUserUtils.setUserNick(conversation.conversationId(), holder.name);
        }
        return convertView;
    }

    public void setOnMoreListener(onMoreListener listener) {
        mListener = listener;
    }

    public interface onMoreListener {
        void onMoreClick(int itemViewType);
    }

    private static class ViewHolder {
        public TextView title;
        public TextView name;
        public ImageView avatar;
        public View layout_more;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            title = (TextView) convertView.findViewById(R.id.tv_title);
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            layout_more = convertView.findViewById(R.id.layout_more);
        }
    }
}
