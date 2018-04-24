package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ConversationExtBean;

import java.util.List;

/**
 * Created by sshss on 2017/11/15.
 */

public class MessageSearchAdapter extends BaseAdapter {
    private final List<Object> mData;
    private onMoreListener mListener;

    public MessageSearchAdapter(List<Object> searchResult) {
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
        if (mData.get(position) instanceof ConversationExtBean) {
            return 0;
        } else {
            return 1;
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
            if (itemViewType == 1) {
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_my_chat_history, null);
            } else {
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_message_search_title, null);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.layout_more != null)
            holder.layout_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onMoreClick(((ConversationExtBean) mData.get(position)).searchType);
                }
            });

        if (itemViewType == 0) {
            holder.title.setText(((ConversationExtBean) mData.get(position)).message);
        } else {
            EMConversation conversation = (EMConversation) mData.get(position);
            ConversationExtBean extBean = Json_U.fromJson(conversation.getExtField(), ConversationExtBean.class);
            holder.name.setText(extBean.searchName);
            holder.message.setText(extBean.searchMessage);
            if(conversation.isGroup()){
                holder.avatar.setImageResource(R.mipmap.ic_contact_group_chat);
            }else {
                EaseUserUtils.setUserAvatar(MyApplication.getContext(), conversation.conversationId(), holder.avatar);
            }
//            EaseUserUtils.setUserNick(conversation.conversationId(), holder.name);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView name;
        public TextView message;
        public ImageView avatar;
        public View layout_more;

        public ViewHolder(View convertView) {
            title = (TextView) convertView.findViewById(R.id.tv_title);
            name = (TextView) convertView.findViewById(R.id.name);
            message = (TextView) convertView.findViewById(R.id.message);
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            layout_more = convertView.findViewById(R.id.layout_more);
        }
    }

    public void setOnMoreListener(onMoreListener listener) {
        mListener = listener;
    }

    public interface onMoreListener {
        void onMoreClick(int itemViewType);
    }
}
