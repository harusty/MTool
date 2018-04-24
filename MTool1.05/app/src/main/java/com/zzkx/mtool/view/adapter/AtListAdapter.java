package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.domain.AtSupportMessage;
import com.zzkx.mtool.util.GlideUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2017/11/1.
 */

public class AtListAdapter extends BaseAdapter {
    private List<AtSupportMessage> mData;

    public AtListAdapter(List<AtSupportMessage> allMsg) {
        mData = allMsg;
    }

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_at, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AtSupportMessage myEMMessage = mData.get(position);
        holder.tv_name.setText(myEMMessage.nickName);
        holder.tv_content.setText(myEMMessage.reason);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(myEMMessage.msgTime)));
        GlideUtil.getInstance().display(holder.iv_head, myEMMessage.picUrl);
        if (myEMMessage.readFlag == 1) {
            holder.pot.setVisibility(View.INVISIBLE);
        } else {
            holder.pot.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder {

        public ImageView iv_head;
        public TextView tv_name;
        public TextView tv_content;
        public TextView tv_time;
        public View pot;

        public ViewHolder(View convertView) {
            iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            pot = convertView.findViewById(R.id.pot);
        }
    }
}
