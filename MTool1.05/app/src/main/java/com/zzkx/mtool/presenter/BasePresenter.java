package com.zzkx.mtool.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.LoginBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.http.MyCallBack;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.UserInfoCachUtil;
import com.zzkx.mtool.view.activity.LoginActivity;
import com.zzkx.mtool.view.iview.IView;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sshss on 2017/6/23.
 * <p>
 * 这里抽取封装对网络请求反馈的预处理，更具体的业务逻辑有下级实现。
 * <p>
 * 1.对网络请求失败onFaild做预处理,下级presenter复写onError()需要注意，该回调在非UI线程。
 * 2.已处理自动登陆，按往前的套路，自动登陆需要具体id、pwd，具体获取方法以及自动登陆失败的情况处理按具体情况补上。
 */

public abstract class BasePresenter<E extends IView, T extends BaseBean> implements IPresenter {
    private E mView;
    private HttpModel mHttpModel;
    private static final int ERROR = 0;
    private static final int CONNECT_ERROR = 3;
    private static final int PROGRESS = 1;
    private static final int DATA = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR:
                    onResponseErrorM((ErrorBean) msg.obj);
                    break;
                case CONNECT_ERROR:
                    onConnectFaildM((ErrorBean) msg.obj);
                    break;
                case PROGRESS:
                    mView.showProgress((Boolean) msg.obj);
                    break;
                case DATA:
                    onSuccessM((T) msg.obj);
                    break;
            }
        }
    };

    /**
     * 给下级presenter用
     *
     * @param bean
     */
    public abstract void onSuccessM(T bean);

    /**
     * 非主线程请求回调，可以做一些耗时操作
     *
     * @param bean
     */
    public void onSuccessWorkThread(T bean) {

    }

    public void onConnectFaildM(ErrorBean errorBean) {
        mView.showError(errorBean);
    }

    public void onResponseErrorM(ErrorBean errorBean) {
        mView.showError(errorBean);
    }

    public BasePresenter(E view) {
        mView = view;
        mHttpModel = new HttpModel(this);
    }

    public E getView() {
        return mView;
    }

    @Override
    public void onSuccess(String resultJson, String url, Object tag) {
        Gson gson = new Gson();
        Class<T> entityClass;
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            entityClass = (Class<T>) type;
        } else {
            Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            /**
             *  这里的p[1]指的是第二个泛型的class
             */
            entityClass = (Class<T>) p[1];
        }
        T t = gson.fromJson(resultJson, entityClass);
        t.cusTag = tag;
        if (t.status == 2) {
            handleReLogin();
        } else {
//            Message msg = Message.obtain();
//            msg.what = PROGRESS;
//            msg.obj = false;
//            mHandler.sendMessage(msg);
            t.reqUrl = url;
            Message msg2 = Message.obtain();
            msg2.what = DATA;
            msg2.obj = t;
            onSuccessWorkThread(t);
            mHandler.sendMessage(msg2);
        }
    }

    private void handleReLogin() {

        Call call = getHttpModel().getLastCall();
        String phone = SPUtil.getString(Const.USER_PHONE, "");
//        String pwd = SPUtil.getString(Const.PWD, "");
        if (TextUtils.isEmpty(phone)) {
            goLoginAct();
            return;
        }
        final RequestBean loginBean = new RequestBean();
        loginBean.phone = phone;
//        loginBean.username = phone;
//        loginBean.password = pwd;

        mHttpModel.request(API.NO_PWD_LOGIN, loginBean, null, new MyCallBack(call.request()) {
            @Override
            public void onFailure(Call call, IOException e) {
                onConnectFaild(new ErrorBean(MyApplication.getContext().getString(R.string.net_err), call.request().url().toString()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String resJson = response.body().string();
                    LoginBean bean = Json_U.fromJson(resJson, LoginBean.class);
                    if (bean.status == 1) {
                        UserInfoCachUtil.cachInfo(bean);
                        mHttpModel.request(null, null, getRequest(), null, null);
                    } else {
                        //go loginAct
                        goLoginAct();
                    }
                } else {
                    onResponseError(new ErrorBean(response.code(), call.request().url().toString()));
                }
            }
        }, null);
    }

    public void goLoginAct() {
        SPUtil.putBoolean(Const.IS_LOGIN,false);
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        MyApplication.getContext().startActivity(intent);
        MyApplication.getInstance().clearActivity();
    }

    @Override
    public void onConnectFaild(ErrorBean bean) {
        Message msg = Message.obtain();
        msg.what = CONNECT_ERROR;
        msg.obj = bean;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onResponseError(ErrorBean bean) {
        Message msg = Message.obtain();
        msg.what = ERROR;
        msg.obj = bean;
        mHandler.sendMessage(msg);
    }

    public void onDestroy() {
        if (mHttpModel != null)
            mHttpModel.cancleAll();
//        mView = null;
    }

    public HttpModel getHttpModel() {
        return mHttpModel;
    }


}