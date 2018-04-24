package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IDeleteTagUserView;

import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public class DeleteTagUserPresenter extends BasePresenter<IDeleteTagUserView,BaseBean> {
    public DeleteTagUserPresenter(IDeleteTagUserView view) {
        super(view);
    }


    public void deleteTagUsers(String labelId,List<String> ids){
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memId = ids;
        requestBean.labelId = labelId;
        getHttpModel().request(API.DEL_TAG_USER,requestBean);

    }
    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showDelResult(bean);
    }
}
