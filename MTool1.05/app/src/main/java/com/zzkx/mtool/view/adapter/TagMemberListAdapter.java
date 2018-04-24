package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.TagMemberBean;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class TagMemberListAdapter extends BaseAdapter {
    private List<TagMemberBean.Data> mData;

    public TagMemberListAdapter(List<TagMemberBean.Data> datas) {
        mData = datas;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TagMemberBean.Data getItem(int position) {
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_attention, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.tv_initial.setVisibility(View.GONE);
            holder.chekcbox.setVisibility(View.VISIBLE);
            holder.chekcbox.setTouchable(false);
            holder.chekcbox.setBoxRes(R.mipmap.ic_22,R.mipmap.ic_21);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TagMemberBean.Data item = getItem(position);
        GlideUtil.getInstance().display(holder.iv_header,item.picUrl);
        holder.tv_name.setText(item.nickname);
        holder.chekcbox.setChecked(item.cusSelected);
        return convertView;
    }

    public static class ViewHolder {

        public TextView tv_initial;
        public ImageView iv_header;
        public TextView tv_name;
        public RectChekBox chekcbox;

        public ViewHolder(View convertView) {
            tv_initial = (TextView) convertView.findViewById(R.id.tv_initial);
            iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            chekcbox = (RectChekBox) convertView.findViewById(R.id.chekcbox);
        }
    }
}
