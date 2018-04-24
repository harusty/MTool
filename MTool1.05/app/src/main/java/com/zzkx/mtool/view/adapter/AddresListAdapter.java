package com.zzkx.mtool.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.view.adapter.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddresListAdapter extends MyBaseAdapter<AddressListBean.AddressBean, AddresListAdapter.ViewHolder> {
    public AddresListAdapter(Context context, List<AddressListBean.AddressBean> data) {
        super(context, data);
    }

    @Override
    public int getItemRes() {
        return R.layout.item_add_info2;
    }

    @Override
    public void setView(ViewHolder holder, AddressListBean.AddressBean addressBean) {
        holder.tv_info1.setText(addressBean.name+"/"+addressBean.phone);
        holder.tv_info2.setText(addressBean.addrDetail);
    }

    @Override
    public ViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    public static class ViewHolder {
        TextView tv_info1;
        TextView tv_info2;

        public ViewHolder(View convertView) {
            tv_info1 = (TextView) convertView.findViewById(R.id.tv_info1);
            tv_info2 = (TextView) convertView.findViewById(R.id.tv_info2);
        }
    }
}

