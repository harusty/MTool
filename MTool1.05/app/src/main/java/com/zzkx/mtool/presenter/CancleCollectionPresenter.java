package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICancleCollectionView;

/**
 * Created by sshss on 2017/10/18.
 */

public class CancleCollectionPresenter extends BasePresenter<ICancleCollectionView, BaseBean> {
    public CancleCollectionPresenter(ICancleCollectionView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showCancleCollectResult(bean);
    }
    public void cancleCollection(String id, int type) {
        cancleCollection(id,type,0);
    }
    public void cancleCollection(String id, int type,int tag) {
        getView().showProgress(true);
        String url = null;
        RequestBean requestBean = new RequestBean();
        switch (type) {
            case 0://店铺
                url = API.DEL_SHOP_COLLECTION;
                requestBean.memberShopCollect = new RequestBean();
                requestBean.memberShopCollect.shopId = id;
                break;
            case 1://商品
                url = API.DEL_GOODS_COLLECTION;
                requestBean.memberGoodsCollect = new RequestBean();
                requestBean.memberGoodsCollect.goodsId = id;
                break;
            case 2://动态.
                url = API.DEL_STATE_COLLECTION;
                requestBean.forumPostCollect = new RequestBean();
                requestBean.forumPostCollect.postId = id;
                break;
        }
        getHttpModel().request(url, requestBean,tag);
    }
}
