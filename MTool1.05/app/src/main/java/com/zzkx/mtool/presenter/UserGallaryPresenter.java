package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.MysStateGallaryBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMyStateGallaryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/21.
 */

public class UserGallaryPresenter extends BasePresenter<IMyStateGallaryView, MysStateGallaryBean> {

    private String mHxId;
    private String mId;
    private ArrayList<StateListBean.ResData> mOnlyResData;

    public UserGallaryPresenter(IMyStateGallaryView view, String id, String hxId) {
        super(view);
        mId = id;
        mHxId = hxId;
    }


    public void getStateGallary() {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        if (mId != null)
            requestBean.id = mId;
        else if (mHxId != null) {
            requestBean.hxUsername = mHxId;
        } else {
            throw new IllegalStateException("id null!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        getHttpModel().request(API.USER_STATE_GALERY, requestBean);
    }

    @Override
    public void onSuccessM(MysStateGallaryBean bean) {
        getView().showProgress(false);
        if (bean.cusData == null || bean.cusData.size() == 0)
            getView().showEmpty();
        else
            getView().showMyGallary(bean);
    }

    @Override
    public void onSuccessWorkThread(MysStateGallaryBean bean) {
        mOnlyResData = new ArrayList<>();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        List<MysStateGallaryBean.DataBean> data = bean.data;
        if (data != null && data.size() > 0) {
            bean.cusData = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                MysStateGallaryBean.DataBean dataBean = data.get(i);
                bean.cusData.add(dataBean.month);
                bean.cusData.add(new ArrayList<StateListBean.ResData>());

                for (int j = 0; j < dataBean.forumThreadResources.size(); j++) {
                    StateListBean.ResData res = dataBean.forumThreadResources.get(j);
                    res.cusResIndex = mOnlyResData.size();
                    mOnlyResData.add(res);
                    List list = (List) bean.cusData.get(bean.cusData.size() - 1);
                    if (list.size() < 3) {
                        list.add(res);
                    } else if (list.size() == 3) {
                        ArrayList<StateListBean.ResData> list1 = new ArrayList<>();
                        list1.add(res);
                        bean.cusData.add(list1);
                    }
                }
            }
        }

    }

    public ArrayList<StateListBean.ResData> getOnlyResData() {
        return mOnlyResData;
    }
}
