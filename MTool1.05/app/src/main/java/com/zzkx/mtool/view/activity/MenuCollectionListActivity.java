package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.adapter.CollectMenuAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/18.
 */

public class MenuCollectionListActivity extends CollectListActivity {

    private CollectMenuAdapter mAdapter;
    private CollectionBean.MemberGoodsCollectDoBean mClickItem;

    @Override
    public void initViews() {
        super.initViews();
        setMainTitle("收藏的商品");
        final boolean isSelectAction = getIntent().getBooleanExtra(Const.SELECT_ACTION, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                position -= mListView.getHeaderViewsCount();
                CollectionBean.MemberGoodsCollectDoBean item = (CollectionBean.MemberGoodsCollectDoBean) mAdapter.getItem(position);
                if (isSelectAction) {
                    ChatShareBean shareBean = new ChatShareBean();
                    shareBean.picUrl = item.goodsImg;
                    shareBean.id = item.goodsId;
                    shareBean.type = 3;
                    shareBean.title = item.goodsName;
                    shareBean.content = "外送：" + item.goodsPriceOut + "元/到店：" + item.goodsPrice + "元";
                    Intent intent = new Intent();
                    intent.putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    startActivity(new Intent(MenuCollectionListActivity.this, MenuDetailActivity.class)
                            .putExtra(Const.ID, item.goodsId)
                    );
                }
            }
        });
    }

    @Override
    protected StickyListHeadersAdapter getListAdapter(CusCollectBean cusCollectBean) {
        mAdapter = new CollectMenuAdapter(cusCollectBean, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickItem = (CollectionBean.MemberGoodsCollectDoBean) v.getTag();
                mDialogCollectionMore.show(mClickItem);
            }
        });
        return mAdapter;
    }

    @Override
    public int getCollectType() {
        return 1;
    }

    @Override
    public ChatShareBean getShareBean() {
        ChatShareBean shareBean = new ChatShareBean();
        shareBean.type = 3;
        if (mClickItem != null) {
            shareBean.id = mClickItem.goodsId;
            shareBean.picUrl = mClickItem.goodsImg;
            shareBean.title = mClickItem.goodsName;
            shareBean.content = "外送：" + mClickItem.goodsPriceOut + "元/到店：" + mClickItem.goodsPrice + "元";
        }
        return shareBean;
    }
}
