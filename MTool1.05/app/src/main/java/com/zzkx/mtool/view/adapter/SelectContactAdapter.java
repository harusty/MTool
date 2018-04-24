package com.zzkx.mtool.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseImageView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class SelectContactAdapter extends BaseAdapter {

    private List<EaseUser> mData;
    private RectChekBox.OnCheckChangeListener mListener;
    private String mAction;

    public SelectContactAdapter(List<EaseUser> contactList) {
        mData = contactList;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public EaseUser getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnCheckChangeListener(RectChekBox.OnCheckChangeListener listener) {
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_contact_select, null);
            holder = new ViewHolder(convertView);
            holder.chekcbox.setTouchable(false);
            if (mAction != null && mAction.equals("remove"))
                holder.chekcbox.setBoxRes(R.mipmap.ic_22, R.mipmap.ic_21);
            else if (mAction != null && mAction.equals("single_select"))
                holder.chekcbox.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        EaseUser user = getItem(position);

        String username = user.getUsername();
        String header = user.getInitialLetter();

        holder.chekcbox.setChecked(user.cusSelected);

        if ((position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter()))) {
            if (TextUtils.isEmpty(header)) {
                holder.headerView.setVisibility(View.GONE);
            } else {
                holder.headerView.setVisibility(View.VISIBLE);
                holder.headerView.setText(header);
                holder.headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            holder.headerView.setVisibility(View.GONE);
        }
        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if (avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
//                avatarView.setShapeType(avatarOptions.getAvatarShape());
                if (avatarOptions.getAvatarBorderWidth() != 0)
                    avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }

        if (user.getNick() == null)
            EaseUserUtils.setUserNick(username, holder.nameView);
        else
            holder.nameView.setText(user.getNick());

        if (user.getAvatar() == null)
            EaseUserUtils.setUserAvatar(MyApplication.getContext(), username, holder.avatar);
        else
            GlideUtil.getInstance().display(holder.avatar, user.getAvatar());

        return convertView;
    }

    public void setAction(String action) {
        mAction = action;
    }


    public static class ViewHolder {
        public ImageView avatar;
        public TextView nameView;
        public TextView headerView;
        public RectChekBox chekcbox;
        int position;

        public ViewHolder(View convertView) {
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            nameView = (TextView) convertView.findViewById(R.id.name);
            headerView = (TextView) convertView.findViewById(R.id.header);
            chekcbox = (RectChekBox) convertView.findViewById(R.id.chekcbox);
        }
    }
}
