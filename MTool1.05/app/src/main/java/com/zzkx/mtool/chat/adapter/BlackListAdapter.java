package com.zzkx.mtool.chat.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BlackListBean;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public class BlackListAdapter extends BaseAdapter {
    private final List<BlackListBean.DataBean> mData;

    public BlackListAdapter(List<BlackListBean.DataBean> bean) {
        mData = bean;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BlackListBean.DataBean getItem(int position) {
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
            holder.chekcbox.setBoxRes(R.mipmap.ic_22, R.mipmap.ic_21);
            holder.chekcbox.setVisibility(View.VISIBLE);
            holder.chekcbox.setTouchable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BlackListBean.DataBean dataBean = mData.get(position);
        holder.chekcbox.setChecked(dataBean.cusSelected);
        String header = dataBean.cusInitial;
        if ((position == 0 || header != null && !header.equals(getItem(position - 1).cusInitial))) {
            if (TextUtils.isEmpty(header)) {
                holder.tv_initial.setVisibility(View.GONE);
            } else {
                holder.tv_initial.setVisibility(View.VISIBLE);
                holder.tv_initial.setText(header);
                holder.tv_initial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            holder.tv_initial.setVisibility(View.GONE);
        }
        holder.tv_name.setText(dataBean.nickname);
        GlideUtil.getInstance().display(holder.iv_header, dataBean.picUrl);
        return convertView;
    }

    public List<BlackListBean.DataBean> getData() {
        return mData;
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
