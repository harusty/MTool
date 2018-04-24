package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zzkx.mtool.R;


/**
 * Created by L.K.X on 2016/8/31.
 */
public abstract class StateView extends FrameLayout implements View.OnTouchListener {

    private ResultState currentState = ResultState.LOADING;
    private View loadView;
    private View errorView;
    private View emptyView;
    private boolean toIntercept;

    public abstract void onReload();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return toIntercept;
    }

    public enum ResultState {
        LOADING,
        SUCESS,
        ERROR,
        EMPTY,
    }


    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        errorView = View.inflate(getContext(), R.layout.view_error, null);
        errorView.findViewById(R.id.bt_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onReload();
            }
        });
        addView(errorView, layoutParams);

        emptyView = View.inflate(getContext(), R.layout.view_empty, null);
        addView(emptyView, layoutParams);

        loadView = View.inflate(getContext(), R.layout.view_loading, null);
        addView(loadView, layoutParams);

        loadView.setVisibility(INVISIBLE);
        errorView.setVisibility(INVISIBLE);
        emptyView.setVisibility(INVISIBLE);

        setOnTouchListener(this);
    }


    //控制该显示哪个view
    public void setCurrentState(ResultState currentState) {
        this.currentState = currentState;
        switch (currentState) {
            case LOADING:
                errorView.setVisibility(INVISIBLE);
                emptyView.setVisibility(INVISIBLE);
                loadView.setVisibility(VISIBLE);
                toIntercept = true;
                break;
            case SUCESS:
                loadView.setVisibility(INVISIBLE);
                errorView.setVisibility(INVISIBLE);
                emptyView.setVisibility(INVISIBLE);
                toIntercept = false;
                break;
            case ERROR:
                loadView.setVisibility(INVISIBLE);
                emptyView.setVisibility(INVISIBLE);
                errorView.setVisibility(VISIBLE);
                toIntercept = true;
                break;
            case EMPTY:
                loadView.setVisibility(INVISIBLE);
                errorView.setVisibility(INVISIBLE);
                emptyView.setVisibility(VISIBLE);
                toIntercept = false;
                break;
        }
    }


}
