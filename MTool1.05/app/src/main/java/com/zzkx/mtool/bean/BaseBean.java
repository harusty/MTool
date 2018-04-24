package com.zzkx.mtool.bean;

/**
 * Created by sshss on 2017/6/23.
 */

public class BaseBean {
    public String reqUrl;
    public int status;
    public String msg;
    public PageBean page;
    public Object cusTag;

    public static class PageBean {
        public int plainPageNum;
        public int pageNum;
        public int numPerPage;
        public String orderField;
        public String orderDirection;
        public int totalPage;
        public int prePage;
        public int nextPage;
        public int totalCount;
    }
}
