package com.zzkx.mtool.bean;

import java.util.List;

/**
 * Created by sshss on 2017/11/23.
 */

public class CityDataBean extends BaseBean {
    public List<Data> data;
    public List<Object> cusData;

    public static class Data {
        public String id;
        public String name;
        public String charAlpha;
        public double latitude;
        public double longitude;
        public String citycode;
    }
}
