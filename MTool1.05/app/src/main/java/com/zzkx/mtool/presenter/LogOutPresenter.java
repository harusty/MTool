package com.zzkx.mtool.presenter;

import com.hyphenate.EMCallBack;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.http.PersistentCookieStore;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ILogOutView;

/**
 * Created by sshss on 2017/10/24.
 */

public class LogOutPresenter extends BasePresenter<ILogOutView, BaseBean> {

    public LogOutPresenter(ILogOutView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        if (bean.status == 1) {
            DemoHelper.getInstance().logout(false, new EMCallBack() {
                @Override
                public void onSuccess() {

                    SPUtil.putBoolean(Const.IS_LOGIN, false);
                    new PersistentCookieStore(MyApplication.getContext()).removeAll();
                    getView().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showLogOutSuccess();
                        }
                    });
//                    DemoHelper.getInstance().r
                }

                @Override
                public void onError(int i, String s) {
                    ToastUtils.showToast("退出登录失败：" + i + "  " + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        } else {
            getView().showProgress(false);
            ToastUtils.showToast("退出登录失败，请重试");
        }
    }

    public void logOut() {
        getView().showProgress(true);
        getHttpModel().request(API.LOG_OUT, null);
    }
}
