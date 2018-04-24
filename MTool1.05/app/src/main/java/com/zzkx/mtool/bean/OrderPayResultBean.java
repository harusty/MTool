package com.zzkx.mtool.bean;

/**
 * Created by L.K.X on 2016/8/24.
 */
public class OrderPayResultBean extends BaseBean {
    public DataEntity data;

    public class DataEntity {
        //微信
        public String sign;
        public String timestamp;
        public String noncestr;
        public String prepayid;
        public String out_trade_no;//总订单号
        //支付宝
        public String body;
    }
}
