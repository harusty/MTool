package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICollectListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/16.
 */

public class CollectListPresenter extends BasePresenter<ICollectListView, CollectionBean> {


    private CusCollectBean mCusCollectBean;
    private int mType;

    public CollectListPresenter(ICollectListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(CollectionBean bean) {
        getView().showProgress(false);
        getView().showCollectList(mCusCollectBean);
    }

    @Override
    public void onSuccessWorkThread(CollectionBean bean) {
        mCusCollectBean = new CusCollectBean();
        mCusCollectBean.mCollectedShops = new ArrayList<>();
        mCusCollectBean.headerIndices = new ArrayList<>();

        List<CollectionBean.DataBean> data = bean.data;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                CollectionBean.DataBean shopCategoryBean = data.get(i);
                List dataList = null;
                switch (mType) {
                    case 0:
                        dataList = shopCategoryBean.merchantRestaurantsList;
                        break;
                    case 1:
                        dataList = shopCategoryBean.memberGoodsCollectDo;
                        break;
                    case 2:
                        dataList = shopCategoryBean.forumPostDos;
                        break;
                }

                if (dataList == null || dataList.size() == 0) {
                    continue;
                } else {
                    mCusCollectBean.mCollectedShops.addAll(dataList);
                    for (int j = 0; j < dataList.size(); j++) {
                        BaseCollectBean shopBean = (BaseCollectBean) dataList.get(j);
                        shopBean.cusParentBean = shopCategoryBean;
                        shopBean.cusGroupPosition = i;
                    }
                }
                if (mCusCollectBean.headerIndices.size() == 0) {
                    mCusCollectBean.headerIndices.add(0);
                    int size = 0;
                    if (dataList.size() > 0)
                        size = dataList.size();
                    mCusCollectBean.headerIndices.add(size);
                } else if (i < data.size() - 1) {
                    Integer value = mCusCollectBean.headerIndices.get(i - 1);
                    mCusCollectBean.headerIndices.add(dataList.size() + value);
                }
            }
        }
    }

    public void getData(int type) {
        mType = type;
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectCatalog = new RequestBean();
        requestBean.memberCollectCatalog.type = type;
        getHttpModel().request(API.USER_COLLECT, requestBean);
    }
}
