package com.zzkx.mtool.presenter;

import android.text.TextUtils;

import com.zzkx.mtool.bean.BlackListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.InitialUtil;
import com.zzkx.mtool.view.iview.IBlackListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public class BlackListPresenter extends BasePresenter<IBlackListView, BlackListBean> {
    private HashMap<String, Integer> mSetionIndexes = new HashMap<>();
    private List<BlackListBean.DataBean> mData;

    public BlackListPresenter(IBlackListView view) {
        super(view);
    }

    public void getBlackList() {
        getView().showProgress(true);
        getHttpModel().request(API.BLACK_LIST, null);
    }

    @Override
    public void onSuccessM(BlackListBean bean) {
        getView().showProgress(false);
        if (bean.data != null) {
            mData = bean.data;
            getView().showBlackList(bean.data);
        } else {
            getView().showEmpty();
        }
    }

    @Override
    public void onSuccessWorkThread(BlackListBean bean) {
        mSetionIndexes.clear();
        List<BlackListBean.DataBean> data = bean.data;
        if (data != null && data.size() > 0) {
            for (BlackListBean.DataBean dataBean : data) {
                dataBean.cusInitial = InitialUtil.getInitial(dataBean.nickname);
            }
        }
        Collections.sort(data, new Comparator<BlackListBean.DataBean>() {
            @Override
            public int compare(BlackListBean.DataBean lhs, BlackListBean.DataBean rhs) {
                if (lhs.cusInitial.equals(rhs.cusInitial)) {
                    return lhs.nickname.compareTo(rhs.nickname);
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
            BlackListBean.DataBean dataBean = data.get(position);
            String header = dataBean.cusInitial;
            if ((position == 0 || header != null && !header.equals(data.get(position - 1).cusInitial))) {
                if (TextUtils.isEmpty(header)) {

                } else {
                    mSetionIndexes.put(header, position);
                }
            }
        }
    }

    public Integer getSectionIndex(String alpah) {
        return mSetionIndexes.get(alpah);
    }

    public BlackListBean.DataBean getData(int position) {
        return mData.get(position);
    }
}
