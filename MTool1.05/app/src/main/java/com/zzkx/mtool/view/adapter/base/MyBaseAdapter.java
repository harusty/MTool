package com.zzkx.mtool.view.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sshss on 2017/6/28.
 * T 展示数据类型
 * H Holder类型
 */

public abstract class MyBaseAdapter<T, H> extends BaseAdapter {
    public List<T> mData;
    public Context mContext;

    public abstract int getItemRes();

    public abstract void setView(H holder, T t);

    public abstract H getViewHolder(View convertView);

    public MyBaseAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, getItemRes(), null);
            holder = getViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (H) convertView.getTag();
        setView(holder, getItem(position));
        return convertView;
    }

}
