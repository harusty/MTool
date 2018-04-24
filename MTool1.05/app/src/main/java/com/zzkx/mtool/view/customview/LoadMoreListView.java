package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zzkx.mtool.R;


/**
 * Created by admin on 2016/3/28.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private View mLoadingReload;
    private View mFooter;
    private View mLoadingFalse;
    private View mLoadingTrue;
    private boolean isLoading = true;
    private boolean flag;
    private LoadMoreListener listener;
    private MyScrollChangeListener myListener;
    private MyOnScrollListener onScrollListener;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooter = View.inflate(context, R.layout.footer_loadmore, null);
        mFooter.measure(0, 0);
        mLoadingTrue = mFooter.findViewById(R.id.loading_true);
        mLoadingFalse = mFooter.findViewById(R.id.loading_false);
        mLoadingReload = mFooter.findViewById(R.id.loading_reload);
        mLoadingReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                listener.onLoadMore();
            }
        });
        this.addFooterView(mFooter);
        this.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (getAdapter() != null && (getLastVisiblePosition() >= getAdapter().getCount() - 1)) {
                if (listener != null && isLoading && !flag) {
                    flag = true;
                    listener.onLoadMore();
                }
            }
        }
        if (myListener != null)
            myListener.onMyScrollStateChanged(scrollState);
    }


    public static int getListViewHeght(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return 0;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public void setLoading(boolean b) {
        flag = false;
        isLoading = b;
        mFooter.setVisibility(VISIBLE);
        if (isLoading) {
            mLoadingTrue.setVisibility(VISIBLE);
            mLoadingFalse.setVisibility(INVISIBLE);
            mLoadingReload.setVisibility(INVISIBLE);
        } else {
//            mFooter.setPadding(0, -mFooterHeight, 0, 0);
            mLoadingTrue.setVisibility(INVISIBLE);
            mLoadingReload.setVisibility(INVISIBLE);
            mLoadingFalse.setVisibility(VISIBLE);
//                mLoadingFalse.setVisibility(GONE);
        }
    }

    public void showReload() {
        flag = false;
        isLoading = false;

        mLoadingTrue.setVisibility(INVISIBLE);
        mLoadingFalse.setVisibility(INVISIBLE);
        mLoadingReload.setVisibility(VISIBLE);
    }


    public void setFooterGone() {
        setLoading(false);
        if (mFooter != null)
            mFooter.setVisibility(INVISIBLE);
    }

    public void setFooterViewsible() {
        setLoading(true);
        if (mFooter != null)
            mFooter.setVisibility(VISIBLE);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setOnLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }


    public interface LoadMoreListener {
        void onLoadMore();
    }

    public interface MyScrollChangeListener {
        public void onMyScrollStateChanged(int scrollState);
    }


    public void setOnMyScrollListener(MyOnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    public boolean checkRefresh() {

        return getChildCount() > 0 && getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0 || getChildCount() == 0;
    }

    public interface MyOnScrollListener {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

}
