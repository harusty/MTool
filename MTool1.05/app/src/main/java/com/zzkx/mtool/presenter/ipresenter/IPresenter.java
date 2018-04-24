package com.zzkx.mtool.presenter.ipresenter;

import com.zzkx.mtool.bean.ErrorBean;

/**
 * Created by sshss on 2017/6/23.
 * presenter作为model和view的中间人，处理系统的业务逻辑，根据model的变化更新view
 * 这里主要是三个监听model的回调
 */

public interface IPresenter {
    void onSuccess(String json, String url, Object tag);

    void onConnectFaild(ErrorBean bean);

    void onResponseError(ErrorBean bean);

}
