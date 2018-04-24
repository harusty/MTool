package com.zzkx.mtool.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseIdBean;
import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.customview.CartChekBox;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static com.zzkx.mtool.R.id.bean;

/**
 * Created by sshss on 2017/8/31.
 */

public class CartAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private int mType;
    private List<MenuListBean.FoodInfoListBean> mMenuData;
    private List<Integer> mHeaderIndices;
    private View.OnClickListener mOptionCtrListener;
    private Activity mContext;

    public CartAdapter(Activity activity, CusMenuListBean data,
                       View.OnClickListener optionCtrlListener, int type) {
        this.mType = type;
        mMenuData = data.menuList;
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_cart_menu_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        MenuListBean.DataBean dataBean = mMenuData.get(position).cusParentBean;

        if (dataBean != null) {
            holder.tv_shop_name.setText(dataBean.name);
            GlideUtil.getInstance().display(holder.iv_shop_logo, dataBean.logoUrl);
            if (dataBean.cusSelectCount == dataBean.cusOriginSelectCount) {
                holder.iv_check.setImageResource(R.mipmap.ic_check_red);
            } else if (dataBean.cusSelectCount == 0) {
                holder.iv_check.setImageResource(R.mipmap.ic_check_gray);
            } else {
                holder.iv_check.setImageResource(R.mipmap.ic_check_harf);
            }
            holder.tv_shop_price.setText(dataBean.cusShopOrderPrice + "");
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_cart_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.iv_edit.setOnClickListener(mOptionCtrListener);
            holder.cb_check.setOnClickListener(mOptionCtrListener);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuListBean.FoodInfoListBean foodInfoListBean = mMenuData.get(position);
        convertView.setTag(bean, foodInfoListBean);
        String url = foodInfoListBean.foodImages == null ? null : foodInfoListBean.foodImages.imgUrl;
        GlideUtil.getInstance().display(holder.iv_image, url);
        holder.tv_name.setText(foodInfoListBean.name);
        holder.iv_edit.setTag(foodInfoListBean);
        holder.cb_check.setTag(foodInfoListBean);
        holder.cb_check.setChecked(foodInfoListBean.cusIsChecked);
        holder.tv_info.setText("好评：" + foodInfoListBean.praises + "%/月销量：" + foodInfoListBean.monthlySales);
        if (foodInfoListBean.type == CartCacheUtil.TYPE_OUT)
            holder.tv_price.setText(foodInfoListBean.priceOut + "");
        else
            holder.tv_price.setText(foodInfoListBean.priceIn + "");
        if (foodInfoListBean.cusCount > 0) {
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(foodInfoListBean.cusCount + "");
        } else {
            holder.tv_count.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID becase this is what
        // headers are based upon
        return ((BaseIdBean) mMenuData.get(position)).cusGroupPotision;
    }

    private class GroupHolder {
        TextView tv_shop_name;
        TextView tv_shop_price;
        ImageView iv_shop_logo;
        ImageView iv_check;

        public GroupHolder(View convertView) {
            tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            tv_shop_price = (TextView) convertView.findViewById(R.id.tv_shop_price);
            iv_shop_logo = (ImageView) convertView.findViewById(R.id.iv_shop_logo);
            iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            if (mType == CartCacheUtil.TYPE_IN) {
                ImageView arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
                arrow.setImageResource(R.mipmap.ic_arrow_down_lightblue);
            }
        }
    }

    @Override
    public Object[] getSections() {
        return new Integer[]{0, 4};
    }

    //header在整个集合中的位置
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
        TextView tv_price;
        TextView tv_info;
        ImageView iv_edit;
        ImageView iv_image;
        CartChekBox cb_check;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            cb_check = (CartChekBox) convertView.findViewById(R.id.cb_check);
            if (mType == CartCacheUtil.TYPE_IN) {
                tv_count.setTextColor(MyApplication.getContext().getResources().getColor(R.color.ligthBlue));
            }
        }
    }
}
