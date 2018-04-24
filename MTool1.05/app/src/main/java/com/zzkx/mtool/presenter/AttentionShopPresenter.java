package com.zzkx.mtool.presenter;

import android.text.TextUtils;

import com.zzkx.mtool.bean.AttentionShopBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.InitialUtil;
import com.zzkx.mtool.view.iview.IAttentionShopView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionShopPresenter extends BasePresenter<IAttentionShopView, AttentionShopBean> {

    private boolean mToSort;
    private HashMap<String, Integer> mSetionIndexes = new HashMap<>();
    private List<AttentionShopBean.DataBean> mData;

    public AttentionShopPresenter(IAttentionShopView view) {
        super(view);
    }

    public AttentionShopPresenter(IAttentionShopView view, boolean toSorte) {
        super(view);
        mToSort = toSorte;
    }

    @Override
    public void onSuccessM(AttentionShopBean bean) {
        if (bean.data != null && bean.data.size() > 0) {
            mData = bean.data;
            getView().showShopData(bean.data);
            getView().showProgress(false);
        } else {
            getView().showEmpty();
        }
    }

    public void getShopList() {
        getView().showProgress(true);
        getHttpModel().request(API.ATTENTION_SHOPS, null);
    }

    @Override
    public void onSuccessWorkThread(AttentionShopBean bean) {
        if (mToSort) {
            mSetionIndexes.clear();
            List<AttentionShopBean.DataBean> data = bean.data;
            if (data != null && data.size() > 0) {
                for (AttentionShopBean.DataBean dataBean : data) {
                    dataBean.cusInitial = InitialUtil.getInitial(dataBean.shopName);
                }

                Collections.sort(data, new Comparator<AttentionShopBean.DataBean>() {

                    @Override
                    public int compare(AttentionShopBean.DataBean lhs, AttentionShopBean.DataBean rhs) {
                        if (lhs.cusInitial.equals(rhs.cusInitial)) {
                            return lhs.shopName.compareTo(rhs.shopName);
                        } else {
                            if ("#".equals(lhs.cusInitial)) {
                                return 1;
                            } else if ("#".equals(rhs.cusInitial)) {
                                return -1;
                            }
                            return lhs.cusInitial.compareTo(rhs.cusInitial);
                        }

                    }
                });

                for (int position = 0; position < data.size(); position++) {
                    AttentionShopBean.DataBean dataBean = data.get(position);
                    String header = dataBean.cusInitial;
                    if ((position == 0 || header != null && !header.equals(data.get(position - 1).cusInitial))) {
                        if (TextUtils.isEmpty(header)) {

                        } else {
                            mSetionIndexes.put(header, position);
                        }
                    }
                }
            }
        }
    }


    public Integer getSectionIndex(String alpah) {
        return mSetionIndexes.get(alpah);
    }

    public AttentionShopBean.DataBean getData(int position) {
        return mData.get(position);
    }
}
