package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BankCardBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICardListView;

import java.util.List;

/**
 * Created by sshss on 2018/1/4.
 */

public class CardListPresenter extends BasePresenter<ICardListView, BankCardBean> {
    public CardListPresenter(ICardListView view) {
        super(view);
    }


    public void getCardList() {
        getView().showProgress(true);
        getHttpModel().request(API.BANK_CARD_LIST, null);
    }

    @Override
    public void onSuccessM(BankCardBean bean) {
        getView().showProgress(false);
        getView().showCardList(bean);
    }

    @Override
    public void onSuccessWorkThread(BankCardBean bean) {
        List<BankCardBean.Data> data = bean.data;
        if (data != null) {
            for (BankCardBean.Data dataBean : data) {
                if(dataBean.defaultValue == 1){
                    getView().showDefCardInfo(dataBean);
                }
            }
        }
    }
}
