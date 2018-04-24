package com.zzkx.mtool.util.pay.ali;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.zzkx.mtool.bean.BusPayBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sshss on 2018/1/3.
 */

public class AliPay {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AliPayResult aliPayResult = new AliPayResult((String) msg.obj);
            String resultStatus = aliPayResult.getResultStatus();

            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                ToastUtils.showToast("支付成功");
                EventBus.getDefault().post(new BusPayBean(BusPayBean.SUCESS));
            }else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认,最终交易是否成功以服务端异步通知为准（小概率状态)
                if (TextUtils.equals(resultStatus, "8000")) {
                    ToastUtils.showToast("支付确认中");
                } else if (TextUtils.equals(resultStatus, "6001")) {//包括用户主动取消支付
                    ToastUtils.showToast("支付取消");
                    EventBus.getDefault().post(new BusPayBean(BusPayBean.CANCLE));
                } else {
                    ToastUtils.showToast("支付失败");
                    // 其他值就可以判断为支付失败，，或者系统返回的错误
                    EventBus.getDefault().post(new BusPayBean(BusPayBean.FAILD));
                }
            }
        }
    };
    public void parseAli(final Activity activity, OrderPayResultBean bean) {
        final OrderPayResultBean.DataEntity data = bean.data;
        if(data!= null){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    PayTask alipay = new PayTask(activity);
                    // 6.调用支付接口，获取支付结果
                    String result = alipay.pay(data.body, true);
                    System.out.println("aliPay");
                    Message msg = new Message();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }

    }
}
