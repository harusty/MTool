package com.zzkx.mtool.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseIdBean;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static com.zzkx.mtool.MyApplication.getContext;
import static com.zzkx.mtool.R.id.bean;

/**
 * Created by sshss on 2017/8/31.
 */

public class MenuAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private int mType;
    private List<MenuListBean.FoodInfoListBean> mMenuData;
    private List<Integer> mHeaderIndices;
    private List<MenuListBean.DataBean> mHeaderData;
    private View.OnClickListener mOptionCtrListener;
    private Activity mContext;

    public MenuAdapter(Activity activity, CusMenuListBean data,
                       View.OnClickListener optionCtrlListener, int type) {
        mType = type;
        mMenuData = data.menuList;
        mHeaderData = data.headerList;
        mHeaderIndices = data.headerIndices;
        mContext = activity;
        mOptionCtrListener = optionCtrlListener;
    }

    @Override
    public int getCount() {
        return mMenuData.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_menu_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        MenuListBean.DataBean dataBean = mHeaderData.get(mMenuData.get(position).cusGroupPotision);
        if (dataBean != null) {
            if (dataBean.cusGroupCount > 0) {
                holder.tv_group_cont.setVisibility(View.VISIBLE);
                holder.tv_group_cont.setText(dataBean.cusGroupCount + "");
            } else {
                holder.tv_group_cont.setVisibility(View.GONE);
            }
            holder.tv_name.setText(dataBean.groupName);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.iv_add.setOnClickListener(mOptionCtrListener);
            holder.iv_minus.setOnClickListener(mOptionCtrListener);
//            convertView.setOnClickListener(mChildClickListener);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuListBean.FoodInfoListBean foodInfoListBean = mMenuData.get(position);
        if (foodInfoListBean.stock == 0) {
            holder.layout_ctr_button.setVisibility(View.GONE);
            holder.tv_empty.setVisibility(View.VISIBLE);
        } else {
            holder.layout_ctr_button.setVisibility(View.VISIBLE);
            holder.tv_empty.setVisibility(View.GONE);
        }
        convertView.setTag(bean, foodInfoListBean);
        String url = foodInfoListBean.foodImages == null ? null : foodInfoListBean.foodImages.imgUrl;
        GlideUtil.getInstance().display(holder.iv_image, url);
        holder.tv_name.setText(foodInfoListBean.name);
        holder.iv_add.setTag(foodInfoListBean);
        holder.iv_minus.setTag(foodInfoListBean);
        holder.tv_info.setText("好评：" + foodInfoListBean.praises + "%/月销量：" + foodInfoListBean.monthlySales);
        if (foodInfoListBean.type == CartCacheUtil.TYPE_OUT)
            holder.tv_price.setText(foodInfoListBean.priceOut + "");
        else
            holder.tv_price.setText(foodInfoListBean.priceIn + "");
        if (foodInfoListBean.cusCount > 0) {
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(foodInfoListBean.cusCount + "");
            holder.iv_minus.setVisibility(View.VISIBLE);
        } else {
            holder.iv_minus.setVisibility(View.INVISIBLE);
            holder.tv_count.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return ((BaseIdBean) mMenuData.get(position)).cusGroupPotision;
    }

    private class GroupHolder {
        TextView tv_name;
        TextView tv_group_cont;

        public GroupHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_group_cont = (TextView) convertView.findViewById(R.id.tv_group_cont);
            if (mType == CartCacheUtil.TYPE_IN) {
                tv_group_cont.setBackgroundResource(R.drawable.oval_light_blue);
                ((ImageView) convertView.findViewById(R.id.iv_arrow)).setImageResource(R.mipmap.ic_arrow_down_lightblue);
            }
        }
    }

    @Override
    public Object[] getSections() {
        return new Integer[]{0, 4};
    }

    @Override
    public int getPositionForSection(int section) {
        if (section >= mHeaderIndices.size()) {
            section = mHeaderIndices.size() - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mHeaderIndices.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mHeaderIndices.size(); i++) {
            if (position < mHeaderIndices.get(i)) {
                return i - 1;
            }
        }
        return mHeaderIndices.size() - 1;
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_count;
        TextView tv_info;
        TextView tv_price;
        TextView tv_empty;
        View layout_ctr_button;
        ImageView iv_add;
        ImageView iv_minus;
        ImageView iv_image;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            tv_empty = (TextView) convertView.findViewById(R.id.tv_empty);
            layout_ctr_button = convertView.findViewById(R.id.layout_ctr_button);
            iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            iv_minus = (ImageView) convertView.findViewById(R.id.iv_minus);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            if (mType == CartCacheUtil.TYPE_IN) {
                tv_count.setTextColor(getContext().getResources().getColor(R.color.ligthBlue));
                iv_minus.setImageResource(R.mipmap.ic_ligntblue_minus);
                iv_add.setImageResource(R.mipmap.ic_lightblue_add);
            }
        }
    }
}
