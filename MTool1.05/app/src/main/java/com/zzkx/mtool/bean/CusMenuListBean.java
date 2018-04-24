package com.zzkx.mtool.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sshss on 2017/8/31.
 */

public class CusMenuListBean {
    public List<MenuListBean.FoodInfoListBean> menuList = new ArrayList();
    public List<MenuListBean.DataBean> headerList = new ArrayList();
    public List<Integer> headerIndices = new ArrayList<>();
    public Map<String,Double> peisongMap = new HashMap<>();
    public double cusTotalPrice;
    public int totalMenuNum;
    public double totalPeisong;


}
