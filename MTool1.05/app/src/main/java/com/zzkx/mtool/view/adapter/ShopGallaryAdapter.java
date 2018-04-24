package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2017/10/16.
 */

public class ShopGallaryAdapter extends BaseAdapter {
    private List<StateListBean.ResData> mData;

    public ShopGallaryAdapter(List<StateListBean.ResData> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_shop_gallary,null);
        }
        StateListBean.ResData imageBean = mData.get(position);
        GlideUtil.getInstance().display(((ImageView)convertView),imageBean.resourceUrl);
        return convertView;
    }
}
