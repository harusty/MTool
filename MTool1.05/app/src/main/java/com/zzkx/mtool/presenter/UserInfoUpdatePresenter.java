package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IMineView;

/**
 * Created by sshss on 2017/9/29.
 */

public class UserInfoUpdatePresenter extends BasePresenter<IMineView, BaseBean> {

    private RequestBean requestBean;

    public UserInfoUpdatePresenter(IMineView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showUpdateInfo(requestBean);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    public void update(String nickname, String picUrl, Integer sex, Long birthday, String introduction, String userAdd) {
        getView().showProgress(true);
        requestBean = new RequestBean();
        requestBean.id = SPUtil.getString(Const.U_ID, "");
        requestBean.nickname = nickname;
        requestBean.picUrl = picUrl;
        requestBean.sex = sex;
        requestBean.birthday = birthday;
        requestBean.introduction = introduction;
        requestBean.userAddr = userAdd;
        getHttpModel().request(API.UPDATE_USER_INFO, requestBean);
    }
}
