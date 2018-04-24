package com.zzkx.mtool.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ShowAtUtil;
import com.zzkx.mtool.view.activity.StateDetailActivity;

import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/17.
 */

public class CollectStateAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private Activity mActivity;
    private View.OnClickListener mClickListener;
    private List<Integer> mHeaderIndices;
    private List<BaseCollectBean> mData;
    private View.OnClickListener mResClickListener;

    public CollectStateAdapter(Activity activity, CusCollectBean cusCollectBean, View.OnClickListener moreClickListener) {
        mActivity = activity;
        mClickListener = moreClickListener;
        mData = cusCollectBean.mCollectedShops;
        mHeaderIndices = cusCollectBean.headerIndices;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        CollectionBean.ForumPostDos merchantRestaurantsListBean = (CollectionBean.ForumPostDos) mData.get(position);
        CollectionBean.DataBean cusParentBean = merchantRestaurantsListBean.cusParentBean;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_collection_cat, null);
        }
        TextView tvname = (TextView) convertView.findViewById(R.id.tv_section);
        tvname.setText(cusParentBean.name);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mData.get(position).cusGroupPosition;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BaseCollectBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        CollectionBean.ForumPostDos item = (CollectionBean.ForumPostDos) mData.get(position);
        List<StateListBean.ResData> resources = item.forumThreadResources;
        if (resources != null) {
            if (resources.size() == 0) {
                return 0;
            } else if (resources.size() == 1) {
                StateListBean.ResData resData = resources.get(0);
                if (resData.type == 0)
                    return 1;
                else
                    return 5;//视频
            } else if (resources.size() > 1 && resources.size() <= 3) {
                return 2;
            } else if (resources.size() > 3 && resources.size() <= 6) {
                return 3;
            } else if (resources.size() > 6) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StateListAdapter.ViewHolder holder;
        final int itemViewType = getItemViewType(position);
        if (convertView == null) {
            View child = null;


            switch (itemViewType) {
                case 0:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_txt, null);
                    break;
                case 1:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_single_image, null);
                    break;
                case 2:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_multi_image_1, null);
                    break;
                case 3:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_multi_image_2, null);
                    break;
                case 4:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_multi_image_3, null);
                    break;
                case 5:
                    child = View.inflate(MyApplication.getContext(), R.layout.item_state_video, null);
                    break;
            }
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_state, null);
            convertView.setPadding(0, 0, 0, Dip2PxUtils.dip2px(MyApplication.getContext(), 5));
            View shareType = View.inflate(MyApplication.getContext(), R.layout.item_state_txt_share, null);
            shareType.setVisibility(View.GONE);
            ((ViewGroup) convertView).addView(shareType, 1);
            ((ViewGroup) convertView).addView(child, 2);

            holder = new StateListAdapter.ViewHolder(convertView);
            holder.ic_more.setVisibility(View.VISIBLE);
            holder.ic_more.setOnClickListener(mClickListener);
            if (holder.image_container != null) {
                holder.image_container.setGridMode();
                holder.image_container.setHorizontalSpacing(16);
                holder.image_container.setVerticalSpacing(16);
            }
            convertView.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (StateListAdapter.ViewHolder) convertView.getTag();
        }
        final CollectionBean.ForumPostDos item = (CollectionBean.ForumPostDos) mData.get(position);
        holder.ic_more.setTag(item);

        CollectionBean.UserMemberBean userMember = item.userMember;
        if (userMember != null) {
            GlideUtil.getInstance().display(holder.iv_user_header, userMember.picUrl);
            holder.tv_user_name.setText(userMember.nickname);
        }
        if (item.shareType == 1) {
            holder.layout_share_type.setVisibility(View.VISIBLE);
            holder.tv_name_orgin.setText("引用/原著：" + item.firstName + "/");
            holder.tv_show_orgin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, StateDetailActivity.class)
                            .putExtra(Const.ID, item.firstId)
                            .putExtra(Const.TYPE, itemViewType)
                    );
                }
            });
            ShowAtUtil.handleAtUsers(holder.tv_share_content, item.share, item.shareuserMemberList);
            ShowAtUtil.handleAtUsers(holder.tv_content, item.content, item.userMemberList);
        } else {
            ShowAtUtil.handleAtUsers(holder.tv_content, item.content, item.userMemberList);
            holder.layout_share_type.setVisibility(View.GONE);
        }
        holder.tv_content.setText(item.content);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(item.createTime)));
        switch (itemViewType) {
            case 0:
                break;
            case 1:
                if (mResClickListener != null)
                    holder.image.setOnClickListener(mResClickListener);
                holder.image.setTag(R.id.parent_index, position);
                holder.image.setTag(R.id.child_index, 0);
                GlideUtil.getInstance().display(holder.image, item.forumThreadResources.get(0).resourceUrl);
                break;
            case 5:
                if (mResClickListener != null)
                    holder.image.setOnClickListener(mResClickListener);
                holder.image.setTag(R.id.parent_index, position);
                holder.image.setTag(R.id.child_index, 0);
                GlideUtil.getInstance().display(holder.image, item.forumThreadResources.get(0).coverUrl);
                break;
            default:
                handleImages(holder, GlideUtil.getInstance(), item, position);
                break;
        }
        return convertView;
    }

    public void setOnResClickListener(View.OnClickListener clickListener) {
        mResClickListener = clickListener;
    }

    private void handleImages(StateListAdapter.ViewHolder viewHolder, GlideUtil glideInstance, CollectionBean.ForumPostDos item, int position) {
        for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
            ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
            if (i < item.forumThreadResources.size()) {
                StateListBean.ResData resData = item.forumThreadResources.get(i);
                imageview.setTag(R.id.child_index, i);
                imageview.setTag(R.id.parent_index, position);
                if (mResClickListener != null)
                    imageview.setOnClickListener(mResClickListener);
                glideInstance.display(imageview, resData.resourceUrl);
            } else {
                imageview.destroyDrawingCache();
            }
        }
    }

    @Override
    public Object[] getSections() {
        return new Integer[]{1, 2};
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

}
