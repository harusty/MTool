package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.adapter.CollectStateAdapter;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/18.
 */

public class StateCollectionListActivity extends CollectListActivity {

    private CollectStateAdapter mCollectStateAdapter;
    private CollectionBean.ForumPostDos mClickItem;

    @Override
    public void initViews() {
        super.initViews();
        setMainTitle("收藏的动态");
        final boolean isSelectAction = getIntent().getBooleanExtra(Const.SELECT_ACTION, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                position -= mListView.getHeaderViewsCount();
                CollectionBean.ForumPostDos item = (CollectionBean.ForumPostDos) mCollectStateAdapter.getItem(position);
                if (isSelectAction) {
                    ChatShareBean shareBean = new ChatShareBean();
                    Intent intent = new Intent();
                    ArrayList<StateListBean.ResData> forumThreadResources = item.forumThreadResources;
                    if (forumThreadResources != null) {
                        if (forumThreadResources.size() == 1) {
                            StateListBean.ResData resData = forumThreadResources.get(0);
                            if (resData.type == 0)
                                shareBean.picUrl = resData.resourceUrl;
                        } else if (forumThreadResources.size() > 1) {
                            StateListBean.ResData resData = forumThreadResources.get(0);
                            shareBean.picUrl = resData.resourceUrl;
                        }
                    }
                    shareBean.title = TextUtils.isEmpty(item.content) ? "来自MTool的分享" : item.content;
                    shareBean.type = 1;
                    shareBean.id = item.id;
                    intent.putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    startActivity(new Intent(StateCollectionListActivity.this, StateDetailActivity.class)
                            .putExtra(Const.TYPE, mCollectStateAdapter.getItemViewType(position))
                            .putExtra(Const.ID, item.id)
                    );
                }
            }
        });
    }

    @Override
    protected StickyListHeadersAdapter getListAdapter(CusCollectBean cusCollectBean) {
        mCollectStateAdapter = new CollectStateAdapter(this, cusCollectBean, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickItem = (CollectionBean.ForumPostDos) v.getTag();
                mDialogCollectionMore.show(mClickItem);
            }
        });
        mCollectStateAdapter.setOnResClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int parentIndex = (int) v.getTag(R.id.parent_index);
                int childIndex = (int) v.getTag(R.id.child_index);
                CollectionBean.ForumPostDos item = (CollectionBean.ForumPostDos) mCollectStateAdapter.getItem(parentIndex);
                StateListBean.ResData resData = item.forumThreadResources.get(childIndex);
                if (resData.type == 1) {
                    startActivity(new Intent(StateCollectionListActivity.this, VideoPlayerAcitivity.class)
                            .putExtra(Const.URL, resData.resourceUrl)
                            .putExtra(Const.COVER_URL, resData.coverUrl)
                    );
                } else {
                    startActivity(new Intent(StateCollectionListActivity.this, StateImgActivity.class)
                            .putExtra(Const.RES, item.forumThreadResources)
                            .putExtra(Const.INDEX, childIndex)
                    );
                }
            }
        });
        return mCollectStateAdapter;
    }

    @Override
    public int getCollectType() {
        return 2;
    }

    @Override
    public ChatShareBean getShareBean() {
        ChatShareBean shareBean = new ChatShareBean();
        shareBean.type = 1;
        if (mClickItem != null) {
            if(mClickItem.shareType == 1){
                shareBean.id = mClickItem.firstId;
            }else{
                shareBean.id = mClickItem.id;
            }
            shareBean.title = mClickItem.content == null ? "来自MTool的分享!" : mClickItem.content;
            ArrayList<StateListBean.ResData> forumThreadResources = mClickItem.forumThreadResources;
            if (forumThreadResources != null) {
                if (forumThreadResources.size() == 1) {
                    StateListBean.ResData resData = forumThreadResources.get(0);
                    if (resData.type == 0)
                        shareBean.picUrl = resData.resourceUrl;
                } else if (forumThreadResources.size() > 1) {
                    StateListBean.ResData resData = forumThreadResources.get(0);
                    shareBean.picUrl = resData.resourceUrl;
                }
            }
        }
        return shareBean;
    }
}
