package com.zzkx.mtool.http;

import okhttp3.Callback;
import okhttp3.Request;

/**
 * Created by sshss on 2017/6/26.
 */

public abstract class MyCallBack implements Callback {
    private Request request;

    public MyCallBack(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
