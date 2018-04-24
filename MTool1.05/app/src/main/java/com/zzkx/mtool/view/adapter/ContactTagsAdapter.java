package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ContactTagBean;
import com.zzkx.mtool.view.activity.TagsActivity;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class ContactTagsAdapter extends BaseAdapter {
    private List<ContactTagBean.DataBean> mData;
    private int mAction;

    public ContactTagsAdapter(List<ContactTagBean.DataBean> list) {
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ContactTagBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_contact_tag, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            if (mAction == TagsActivity.ACTION_SELECT) {
                viewHolder.ic_arrow.setVisibility(View.GONE);
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                viewHolder.checkBox.setTouchable(false);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactTagBean.DataBean item = getItem(position);
        viewHolder.checkBox.setChecked(item.cusSlected);
        viewHolder.tv_name.setText(item.name);
        return convertView;
    }

    public void setAction(int action) {
        mAction = action;
    }

    public List<String> getSelectId() {
        List<String> ids = new ArrayList<>();
        for (ContactTagBean.DataBean tagBean : mData) {
            if (tagBean.cusSlected)
                ids.add(tagBean.id);
        }
        return ids;
    }

    public static class ViewHolder {
        public TextView tv_name;
        public RectChekBox checkBox;
        public View ic_arrow;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            checkBox = (RectChekBox) convertView.findViewById(R.id.checkbox);
            ic_arrow = convertView.findViewById(R.id.ic_arrow);
        }
    }
}
