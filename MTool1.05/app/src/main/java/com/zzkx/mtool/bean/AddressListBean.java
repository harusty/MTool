package com.zzkx.mtool.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddressListBean extends BaseBean {
    public List<AddressBean> data;

    public static class AddressBean implements Serializable {
        public String name;
        public String phone;
        public String addrDetail;
        public int sex;
        public double latitude;
        public double longitude;
        public int defaultValue;
        public String id;
    }
}
