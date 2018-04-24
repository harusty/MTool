package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopGallaryBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IShopGallaryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/16.
 */

public class ShopGallaryPresenter extends BasePresenter<IShopGallaryView, ShopGallaryBean> {

    private ArrayList<StateListBean.ResData> mResDatas;
    public ShopGallaryPresenter(IShopGallaryView view) {
        super(view);
    }

    @Override
    public void onSuccessM(ShopGallaryBean bean) {
        getView().showProgress(false);
        getView().showData(mResDatas);
    }


    @Override
    public void onSuccessWorkThread(ShopGallaryBean bean) {
        ShopGallaryBean.DataBean data = bean.data;
        if(data!= null) {
            List<ShopGallaryBean.DataBean.ImageBean> imgs = data.merchantRestaurantsSurroundingsImg;
            if(imgs!= null){
                mResDatas = new ArrayList<>();
                for (ShopGallaryBean.DataBean.ImageBean imgBean : imgs) {
                    StateListBean.ResData resData = new StateListBean.ResData();
                    resData.resourceUrl = imgBean.imgUrl;
                    mResDatas.add(resData);
                }
            }
        }

    }

    public void getData(String shopId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.id = shopId;
        getHttpModel().request(API.SHOP_GALLARY, requestBean);
    }
}
