package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.CollectionSearchBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICollectionSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/26.
 */

public class CollectionSearchPresenter
        extends BasePresenter<ICollectionSearchView, CollectionSearchBean> {

    public CollectionSearchPresenter(ICollectionSearchView view) {
        super(view);
    }

    @Override
    public void onSuccessM(CollectionSearchBean bean) {
        if (bean.cusData.size() == 0){
            getView().showEmpty();}
        else {
            getView().showProgress(false);
            getView().showData(bean);
        }
    }

    @Override
    public void onSuccessWorkThread(CollectionSearchBean bean) {
        bean.cusData = new ArrayList<>();
        CollectionSearchBean.DataBean data = bean.data;
        if (data != null) {
            List<CollectionSearchBean.ForumPostCollectListBean> stateList = data.forumPostCollectList;
            List<CollectionSearchBean.GoodsListBean> goodsList = data.goodsList;
            List<CollectionSearchBean.ShopListBean> shopList = data.shopList;

            if (shopList != null && shopList.size() > 0) {
                bean.cusData.add("店  铺");
                bean.cusData.addAll(shopList);
            }

            if (goodsList != null && goodsList.size() > 0) {
                bean.cusData.add("商  品");
                bean.cusData.addAll(goodsList);
            }

            if (stateList != null && stateList.size() > 0) {
                bean.cusData.add("资  讯");
                bean.cusData.addAll(stateList);
            }
        }
    }

    public void search(String key) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.keyWord = key;
        getHttpModel().request(API.SEARCH_COLLECTION, requestBean);
    }
}
