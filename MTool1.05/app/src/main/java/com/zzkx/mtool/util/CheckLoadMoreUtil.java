package com.zzkx.mtool.util;

import android.widget.ListView;

import com.zzkx.mtool.bean.BaseListBean;
import com.zzkx.mtool.view.customview.LoadMoreListView;

import java.util.List;

/**
 * Created by ShenChengRi on 2016/8/15.
 * 对于三种情况的封装
 * 1.menuList == 0，total==0 Empty 该接口无记录
 * 2.menuList == 0,total>0  NoMore 已滑倒最底部（没有更多数据）
 */
public abstract class CheckLoadMoreUtil {

    private LoadMoreListView listView;
    private int pageCount = 10;
    private boolean isRefresh = true;

    public abstract void updateAdapter();

    public abstract void onEmpty();

    public CheckLoadMoreUtil(ListView listView) {
        this(listView, 10);
    }

    public CheckLoadMoreUtil(ListView listView, int numPerPage) {
        if (listView instanceof LoadMoreListView)
            this.listView = (LoadMoreListView) listView;
        this.pageCount = numPerPage;
    }

    public int check(BaseListBean bean, List total, boolean isRefresh) {
        this.isRefresh = isRefresh;
        int pageNum = -1;
        int plainPageNum = 0;
        List data = bean.getData();
        if (bean.page != null)
            plainPageNum = bean.page.plainPageNum;
        if (data != null && data.size() > 0) {
            if (isRefresh) {
                total.clear();
                total.addAll(data);
            } else {
                if (!total.contains(data)) {
                    total.addAll(data);
                }
            }
            loadingSuccess(data);
        } else {
            if (isRefresh)
                total.clear();
            loadingEmpty();
        }
        pageNum = plainPageNum + 1;
        updateAdapter();
        return pageNum;
    }


    public void onNoMore() {
        if (listView != null)
            listView.setLoading(false);
    }

    private void loadingEmpty() {
        if (isRefresh) {
            if (listView != null)
                listView.setLoading(false);
            onEmpty();
        } else {
            onNoMore();
        }
    }

    private void loadingSuccess(List data) {
        if (data.size() < pageCount) {
            if (listView != null)
                listView.setLoading(false);
        } else {
            if (listView != null)
                listView.setLoading(true);
        }
    }
}
