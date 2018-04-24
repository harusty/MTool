package com.zzkx.mtool.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.InitialBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class IdolAdapter extends BaseAdapter {
    private List<InitialBean> mData;

    public IdolAdapter(List<InitialBean> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InitialBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(MyApplication.getContext(),R.layout.item_attention,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        InitialBean dataBean = mData.get(position);
        String header = dataBean.cusInitial;
        if ((position == 0 || header != null && !header.equals(getItem(position - 1).cusInitial)) ) {
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
        holder.tv_name.setText(dataBean.cusName);
        GlideUtil.getInstance().display(holder.iv_header,dataBean.cusHead);
        return convertView;
    }

    private static class ViewHolder {

        public TextView tv_initial;
        public ImageView iv_header;
        public TextView tv_name;

        public ViewHolder(View convertView) {
            tv_initial = (TextView) convertView.findViewById(R.id.tv_initial);
            iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        }
    }
}
