package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.SystemNotiBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sshss on 2018/2/23.
 */

public class SystemNotiListAdapter extends BaseAdapter {
    private final SimpleDateFormat mFormat;
    private List<SystemNotiBean.DataBean> mData;

    public SystemNotiListAdapter(List<SystemNotiBean.DataBean> totalData) {
        mData = totalData;
        mFormat = new SimpleDateFormat("yyyy-MM-dd MM:mm:ss");
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SystemNotiBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_system_noti,null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView time = (TextView) convertView.findViewById(R.id.tv_time);
        SystemNotiBean.DataBean item = getItem(position);
        name.setText(item.name);
        time.setText(mFormat.format(item.createTime));
        return convertView;
    }
}
