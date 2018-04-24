package com.zzkx.mtool.presenter;

import android.app.Activity;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CodeBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IRegistView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sshss on 2017/9/29.
 */

public class RegistPresenter extends BasePresenter<IRegistView, CodeBean> {
    private Activity mActivity;

    public RegistPresenter(IRegistView view, Activity activity) {

        super(view);
        mActivity = activity;
    }

    @Override
    public void onSuccessM(CodeBean bean) {
        getView().showProgress(false);
        getView().showReceiveCode(bean);
    }

    public void getRegistCode(String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        getHttpModel().request(API.REGIST_CODE, requestBean);
    }

    public void getResetCode(String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        getHttpModel().request(API.PWD_RESET_CODE, requestBean);
    }

    public void validateRestCode(String code, String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        requestBean.code = code;
        getHttpModel().request(API.PUBLISH_REST_INFO, requestBean, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showError(new ErrorBean());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);

                            try {
                                String string = response.body().string();
                                BaseBean baseBean = Json_U.fromJson(string, BaseBean.class);
                                if (baseBean.status == 1) {
                                    getView().validateSuccess(baseBean);
                                }
                                ToastUtils.showToast(baseBean.msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        }, null);
    }
    public void regist(String code, String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        requestBean.code = code;
        getHttpModel().request(API.REGIST, requestBean, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showError(new ErrorBean());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);

                            try {
                                String string = response.body().string();
                                BaseBean baseBean = Json_U.fromJson(string, BaseBean.class);
                                if (baseBean.status == 1) {
                                    getView().registSuccess(baseBean);
                                }
                                ToastUtils.showToast(baseBean.msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        }, null);
    }
    public void bindPhone(String code, String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        requestBean.code = code;
        getHttpModel().request(API.BIND_PHONE, requestBean, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showError(new ErrorBean());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);

                            try {
                                String string = response.body().string();
                                BaseBean baseBean = Json_U.fromJson(string, BaseBean.class);
                                if (baseBean.status == 1) {
                                    getView().bindPhoneResult(baseBean);
                                }
                                ToastUtils.showToast(baseBean.msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        }, null);
    }
    public void toReset(String id, String password) {
        RequestBean requestBean = new RequestBean();
        requestBean.id = id;
        requestBean.newPassword = password;

        getHttpModel().request(API.UPDATE_PASSWORD, requestBean, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showError(new ErrorBean());
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);

                            try {
                                String string = response.body().string();
                                BaseBean baseBean = Json_U.fromJson(string, BaseBean.class);
                                if (baseBean.status == 1) {
                                    getView().resetSuccess(baseBean);
                                }else {
                                    ToastUtils.showToast(baseBean.msg);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        }, null);
    }



}
