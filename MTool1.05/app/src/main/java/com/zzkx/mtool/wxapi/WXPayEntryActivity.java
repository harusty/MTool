package com.zzkx.mtool.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.util.pay.wx.WxPay;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by ShenChengRi on 2016/5/16.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq baseReq) {

    }


//    @Override
//    public int getContentRes() {
//        return R.layout.activity_wxpay_entry;
//
//    }
//
//    @Override
//    public void initViews() {
//        api = WXAPIFactory.createWXAPI(this, WxPay.APP_ID);
//        api.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReload() {
//
//    }

    private IWXAPI api;
    private static final int SUCESS = 0;
    private static final int CANCLE = -2;
    private static final int FAILD = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WxPay.APP_ID);
        api.handleIntent(getIntent(), this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), this);
    }


    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        System.out.println("baseResp   " + errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (errCode) {
                case SUCESS:
                    ToastUtils.showToast("支付成功");
                    break;
                case CANCLE:
                    ToastUtils.showToast("支付取消");
                    break;
                case FAILD:
                    ToastUtils.showToast("支付失败");
                    break;
            }
            BusPayBean busPayBean = new BusPayBean(errCode);
            EventBus.getDefault().post(busPayBean);
        }
        finish();
    }
}
