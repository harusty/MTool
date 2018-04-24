package com.zzkx.mtool.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.R;
import com.zzkx.mtool.view.adapter.base.BaseViewHolder;
import com.zzkx.mtool.view.adapter.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by sshss on 2017/9/11.
 */

public class LocateNeayByAdapter extends MyBaseAdapter<PoiItem, LocateNeayByAdapter.ViewHolder> {

    public LocateNeayByAdapter(Context context, List<PoiItem> data) {
        super(context, data);
    }

    @Override
    public int getItemRes() {
        return R.layout.item_add_info3;
    }

    @Override
    public void setView(ViewHolder holder, PoiItem object) {
        holder.tv_info1.setText(object.getTitle());
        holder.tv_info2.setText(object.getSnippet());
    }

    @Override
    public ViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    static class ViewHolder extends BaseViewHolder {
        TextView tv_info1;
        TextView tv_info2;

        public ViewHolder(View convertView) {
            super(convertView);
            tv_info1 = (TextView) convertView.findViewById(R.id.tv_info1);
            tv_info2 = (TextView) convertView.findViewById(R.id.tv_info2);
        }
    }
}
