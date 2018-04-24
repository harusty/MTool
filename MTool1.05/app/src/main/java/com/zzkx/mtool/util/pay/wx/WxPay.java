package com.zzkx.mtool.util.pay.wx;

import android.content.Context;
import android.view.View;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;
import com.zzkx.mtool.util.ToastUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by L.K.X on 2016/8/24.
 */
public class WxPay {
    public static final String APP_ID = "wx0104661b1a8f102d";//开放平台的appid
    private static final String MCH_ID = "1483844352";//商户号
    private static final String API_KEY = "mtool17688080667mtool17688080667";//商户号上设置的秘钥
    private static final String WXPAYINFO = "wxpayinfo";
    private String prepared_id;
    private String nonceStr;
    private String timeStamp;

    private Context context;
    private IWXAPI msgApi;
    private PayReq payRequest;
    private View progress_reading_trans;
    private HttpModel mHttpModel;

    public WxPay(Context context) {
        this.context = context;
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(APP_ID);//注册appid
        payRequest = new PayReq();//初始化req
    }

    public void pay(Object payRequestBean, View progress_reading_trans, String Payurl) {
        this.progress_reading_trans = progress_reading_trans;
        mHttpModel = new HttpModel(new IPresenter() {
            @Override
            public void onSuccess(String json, String url, Object tag) {
//                parseWxPayInfo(json);
            }

            @Override
            public void onConnectFaild(ErrorBean bean) {

            }

            @Override
            public void onResponseError(ErrorBean bean) {

            }
        });
        mHttpModel.request(Payurl, payRequestBean);
    }

    //根据signParams生成app签名
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);
        System.out.println("--" + sb.toString());
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }


    public void parseWxPayInfo(OrderPayResultBean wxPayResultBean) {
        if (progress_reading_trans != null) {
            progress_reading_trans.setVisibility(View.INVISIBLE);
        }

        if (wxPayResultBean != null) {
            int status = wxPayResultBean.status;
            if (status == 1) {
                OrderPayResultBean.DataEntity data = wxPayResultBean.data;
                prepared_id = data.prepayid;
                nonceStr = data.noncestr;
                timeStamp = data.timestamp;
//                ConstantValues.ORDER_ID = data.out_trade_no;

                payRequest.appId = APP_ID;
                payRequest.partnerId = MCH_ID;//商户号
                payRequest.prepayId = prepared_id;//第一步通过微信统一接口获得的预订单号
                payRequest.packageValue = "Sign=WXPay";//固定值
                payRequest.nonceStr = nonceStr;//nonce_str:构成签名的时候需要你自己设置随机字符串
                payRequest.timeStamp = timeStamp;//当前的时间戳

                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                signParams.add(new BasicNameValuePair("appid", payRequest.appId));
                signParams.add(new BasicNameValuePair("noncestr", payRequest.nonceStr));
                signParams.add(new BasicNameValuePair("package", payRequest.packageValue));
                signParams.add(new BasicNameValuePair("partnerid", payRequest.partnerId));
                signParams.add(new BasicNameValuePair("prepayid", payRequest.prepayId));
                signParams.add(new BasicNameValuePair("timestamp", payRequest.timeStamp));
                String appSign = genAppSign(signParams);
                String sign = data.sign;
                System.out.println("appSign:**" + appSign + "-------sign:**" + sign);
                if (sign != null && sign.equals(appSign)) {
                    payRequest.sign = appSign;
                    msgApi.sendReq(payRequest);
                } else {
                    ToastUtils.showToast("签名错误");
                    EventBus.getDefault().post(new BusPayBean(BusPayBean.FAILD));
                }

            } else {
                ToastUtils.showToast("下单失败");
                EventBus.getDefault().post(new BusPayBean(BusPayBean.FAILD));
            }
        }
    }
}
