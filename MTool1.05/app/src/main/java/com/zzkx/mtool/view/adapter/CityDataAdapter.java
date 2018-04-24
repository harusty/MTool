package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CityDataBean;

import java.util.List;

/**
 * Created by sshss on 2017/11/23.
 */

public class CityDataAdapter extends BaseAdapter {
    private List<Object> mData;

    public CityDataAdapter(List<Object> cusData) {
        mData = cusData;
    }

    @Override
    public int getCount() {
        return mData.size();
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
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            if (itemViewType == 0) {
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_title2, null);
            } else {
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_city_name, null);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Object item = getItem(position);
        if (itemViewType == 0) {
            holder.tv_section.setText(item.toString());
        } else {
//            holder.tv_name.setText(((CityDataBean.Data) getItem(position)).name);
            if (item != null)
                holder.tv_name.setText(((CityDataBean.Data) getItem(position)).name);
            else
                holder.tv_name.setText("null nullnullnullnullnullnullnullnull");
        }
        return convertView;
    }

    private static class ViewHolder {

        TextView tv_name;
        TextView tv_section;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_section = (TextView) convertView.findViewById(R.id.tv_section);
        }
    }
}
