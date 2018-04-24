package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.amap.api.maps.model.LatLng;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.adapter.CollectShopAdapter;

import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/18.
 */

public class ShopCollectionListActivity extends CollectListActivity {

    private CollectShopAdapter mAdapter;
    private CollectionBean.MerchantRestaurantsListBean mClickItem;

    @Override
    public void initViews() {
        super.initViews();
        setMainTitle("收藏的店铺");
        final boolean isSelectAction = getIntent().getBooleanExtra(Const.SELECT_ACTION, false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position -= mListView.getHeaderViewsCount();
                if (position < 0)
                    return;
                CollectionBean.MerchantRestaurantsListBean item = (CollectionBean.MerchantRestaurantsListBean) mAdapter.getItem(position);
                if (isSelectAction) {
                    ChatShareBean shareBean = new ChatShareBean();
                    Intent intent = new Intent();
                    shareBean.title = item.name;
                    shareBean.picUrl = item.logoUrl;
                    shareBean.type = 2;
                    shareBean.id = item.id;
                    shareBean.content = item.description;
                    intent.putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Const.CUS_SERV_SCORE, item.serviceScore + "");
                    map.put(Const.CUS_USER_SCORE, item.priceScore + "");
                    CollectionBean.AllofeeBean allofee = item.allofee;
                    if (allofee != null)
                        map.put(Const.CUS_PEISONG, allofee.startPrice);
                    map.put(Const.CUS_RENJUN, item.avgConsume);
                    map.put(Const.CUS_QISONG, item.deliverAmount + "");
                    map.put(Const.CUS_SHOP_ID, item.id);
                    map.put(Const.CUS_INTRO, item.description);

                    startActivity(new Intent(ShopCollectionListActivity.this, ShopActivity.class)
                            .putExtra(Const.ID, item.id));
                }
            }
        });
    }

    @Override
    protected StickyListHeadersAdapter getListAdapter(CusCollectBean cusCollectBean) {
        mAdapter = new CollectShopAdapter(cusCollectBean,
                new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude())
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickItem = (CollectionBean.MerchantRestaurantsListBean) v.getTag();
                mDialogCollectionMore.show(mClickItem);
            }
        });
        return mAdapter;

    }

    @Override
    public int getCollectType() {
        return 0;
    }

    @Override
    public ChatShareBean getShareBean() {
        ChatShareBean shareBean = new ChatShareBean();
        shareBean.type = 2;
        if (mClickItem != null) {
            shareBean.picUrl = mClickItem.logoUrl;
            shareBean.title = mClickItem.name;
            shareBean.content = mClickItem.description;
            shareBean.id = mClickItem.id;
        }
        return shareBean;
    }
}
