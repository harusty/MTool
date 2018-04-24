package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateDetailBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2017/9/27.
 */

public class SupportAdapter extends BaseAdapter {
    private List<StateDetailBean.DataBean.UserMemberDosBean> mData;

    public SupportAdapter(List<StateDetailBean.DataBean.UserMemberDosBean> userMemberDos) {
        mData = userMemberDos;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public StateDetailBean.DataBean.UserMemberDosBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_suppor_user, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StateDetailBean.DataBean.UserMemberDosBean item = getItem(position);
        holder.tv_user_name.setText(item.nickname);
        GlideUtil.getInstance().display(holder.iv_user_header, item.picUrl);
        return convertView;
    }

    private static class ViewHolder {
        public TextView tv_user_name;
        public ImageView iv_user_header;

        public ViewHolder(View convertView) {
            tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            iv_user_header = (ImageView) convertView.findViewById(R.id.iv_user_header);
        }
    }
}
