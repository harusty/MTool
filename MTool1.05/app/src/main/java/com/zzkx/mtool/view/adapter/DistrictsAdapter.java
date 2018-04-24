package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aries.ui.view.radius.RadiusTextView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.DistrictsBean;

import java.util.List;

/**
 * Created by sshss on 2017/11/28.
 */

public class DistrictsAdapter extends BaseAdapter {
    private List<DistrictsBean.DataBean> mData;

    public DistrictsAdapter(List<DistrictsBean.DataBean> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DistrictsBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_district, null);
        RadiusTextView textView = (RadiusTextView) convertView.findViewById(R.id.tv_name);
        textView.setText(mData.get(position).name);
        return convertView;
    }
}
