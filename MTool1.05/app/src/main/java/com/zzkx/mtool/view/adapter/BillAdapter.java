package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BillBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sshss on 2017/12/19.
 */

public class BillAdapter extends BaseAdapter {
    private SimpleDateFormat mFormat;
    private SimpleDateFormat mWeekFormater;
    private List<BillBean.DataBean> mData;

    public BillAdapter(List<BillBean.DataBean> data) {
        mData = data;
        mFormat = new SimpleDateFormat("MM-dd");
        mWeekFormater = new SimpleDateFormat("EEEE");
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BillBean.DataBean getItem(int position) {
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_bill, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BillBean.DataBean item = getItem(position);

        if (position == 0)
            holder.tv_section.setVisibility(View.VISIBLE);
        else
            holder.tv_section.setVisibility(View.GONE);

        holder.tv_reson.setText(item.changeReason);
        if (item.changeDirection == 1) {
            holder.tv_money.setText("-" + item.quantity);
            holder.tv_money.setTextColor(MyApplication.getContext().getResources().getColor(R.color.ligthBlue));
        } else {
            holder.tv_money.setText("+" + item.quantity);
            holder.tv_money.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
        }
        holder.tv_week.setText(mWeekFormater.format(item.createTime));
        holder.tv_date.setText(mFormat.format(item.createTime));
        return convertView;
    }

    private static class ViewHolder {

        TextView tv_section;
        TextView tv_week;
        TextView tv_date;
        TextView tv_money;
        TextView tv_reson;

        public ViewHolder(View view) {
            tv_section = (TextView) view.findViewById(R.id.tv_section);
            tv_week = (TextView) view.findViewById(R.id.tv_week);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_reson = (TextView) view.findViewById(R.id.tv_reson);
        }
    }
}
