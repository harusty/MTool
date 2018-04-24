package com.zzkx.mtool.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.imple.OptionConfrimListenenr;
import com.zzkx.mtool.util.CartCacheUtil;

import java.util.List;

import static com.zzkx.mtool.MyApplication.getContext;
import static com.zzkx.mtool.R.id.tv_money;

/**
 * Created by sshss on 2017/8/29.
 */

public class DialogMenuOption implements View.OnClickListener {

    private final boolean mIsFromCart;
    private TextView mTvTitle;
    private int mType;
    private OptionConfrimListenenr mConfirmListener;
    private Context mContext;
    private ViewGroup mOptionContainer;
    private AlertDialog alertDialog;
    private View mContentView;
    private SparseArray<View> mViewCache;
    private List<MenuListBean.MenuOpiton> mOptionList;
    private MenuListBean.FoodInfoListBean mMenuBean;
    private MenuListBean.DataBean mDataBean;
    private boolean mNoOption;
    private ScrollView mScrollView;

    public DialogMenuOption(Context context, OptionConfrimListenenr confrimListener, int type) {
        this(context, confrimListener, type, false);
    }

    public DialogMenuOption(Context context, OptionConfrimListenenr confrimListener, int type, boolean isFromCart) {
        mIsFromCart = isFromCart;
        mType = type;
        mConfirmListener = confrimListener;
        mContext = context;
        mViewCache = new SparseArray<>();
        mContentView = View.inflate(getContext(), R.layout.dialog_option_menu, null);
        mTvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        mOptionContainer = (ViewGroup) mContentView.findViewById(R.id.option_container);
        mContentView.findViewById(R.id.tv_cancle).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_confirm).setOnClickListener(this);
        mScrollView = (ScrollView) mContentView.findViewById(R.id.scroll_view);
        initDialog(context);
    }

    public void show(MenuListBean.FoodInfoListBean menubean, MenuListBean.DataBean dataBean) {
        show(menubean, dataBean, false);
    }

    public void show(MenuListBean.FoodInfoListBean menubean, MenuListBean.DataBean dataBean, boolean noOption) {
        mDataBean = dataBean;
        mMenuBean = menubean;
        mTvTitle.setText(menubean.name);
        alertDialog.show();
        alertDialog.setContentView(mContentView);
        mNoOption = noOption;
        if (noOption) {
            mOptionCounts = new int[1];
            setMenuOptionNoOption();
        } else {
            mOptionList = mMenuBean.foodSkuList;
            mOptionCounts = new int[mOptionList.size()];
            setMenuOption();
        }
    }

    private void setMenuOptionNoOption() {
        mOptionContainer.removeAllViews();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = mOptionContainer.getChildAt(0);
                TextView tv_count = (TextView) child.findViewById(R.id.tv_count);
                int count = Integer.parseInt(tv_count.getText().toString());
                switch (v.getId()) {
                    case R.id.iv_plus:
                        count++;
                        break;
                    case R.id.iv_minus:
                        count--;
                        if (count < 0)
                            count = 0;
                        break;
                }
                mOptionCounts[0] = count;
                tv_count.setText(String.valueOf(count));
            }
        };
        View view = View.inflate(mContext, R.layout.item_menu_option, null);
        View minus = view.findViewById(R.id.iv_minus);
        View plus = view.findViewById(R.id.iv_plus);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        ((TextView) view.findViewById(R.id.tv_section)).setText(getContext().getString(R.string.default_spec));
        ((TextView) view.findViewById(R.id.tv_count)).setText(mMenuBean.cusCount + "");
        if (mType == CartCacheUtil.TYPE_OUT) {
            tv_money.setText(mMenuBean.priceOut + "");
        } else {
            tv_money.setText(mMenuBean.priceIn + "");
        }
        minus.setOnClickListener(listener);
        plus.setOnClickListener(listener);
//        if(mOptionContainer.getChildCount() == 5){
//            ViewGroup.LayoutParams layoutParams = mOptionContainer.getLayoutParams();
//            ViewGroup.LayoutParams layoutParams1 = mScrollView.getLayoutParams();
//            layoutParams1.height = layoutParams.height;
//            mScrollView.setLayoutParams(layoutParams1);
//        }

        mOptionContainer.addView(view);
    }


    private int[] mOptionCounts;

    private void setMenuOption() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                View child = mOptionContainer.getChildAt(position);
                MenuListBean.MenuOpiton menuOpiton = mOptionList.get(position);
                TextView tv_count = (TextView) child.findViewById(R.id.tv_count);
                int count = mOptionCounts[position];
                switch (v.getId()) {
                    case R.id.iv_plus:
                        count++;
                        break;
                    case R.id.iv_minus:
                        count--;
                        if (count < 0)
                            count = 0;
                        break;
                }
                mOptionCounts[position] = count;
                tv_count.setText(count + "");
            }
        };

        mOptionContainer.removeAllViews();
        for (int i = 0; i < mOptionList.size(); i++) {
            MenuListBean.MenuOpiton menuOpiton = mOptionList.get(i);
            mOptionCounts[i] = menuOpiton.cusCount;
            View view = mViewCache.get(i);
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_menu_option, null);
                View minus = view.findViewById(R.id.iv_minus);
                View plus = view.findViewById(R.id.iv_plus);
                minus.setOnClickListener(listener);
                plus.setOnClickListener(listener);
                minus.setTag(i);
                plus.setTag(i);
                mViewCache.put(i, view);
            }
            ((TextView) view.findViewById(R.id.tv_section)).setText(menuOpiton.spec);
            if (mType == CartCacheUtil.TYPE_OUT) {
                ((TextView) view.findViewById(tv_money)).setText(menuOpiton.priceOut + "");
            } else {
                ((TextView) view.findViewById(tv_money)).setText(menuOpiton.priceIn + "");
            }
            ((TextView) view.findViewById(R.id.tv_count)).setText(menuOpiton.cusCount + "");
            mOptionContainer.addView(view);
        }
    }

    private void initDialog(Context context) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;
        alertDialog.onWindowAttributesChanged(attributes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                alertDialog.dismiss();
                break;
            case R.id.tv_confirm:
                if (mNoOption) {
                    handleNoOptionInCart();
                } else {
                    handleOption();
                }
                alertDialog.dismiss();
                break;
        }
    }

    private void handleNoOptionInCart() {
        int optionCount = mOptionCounts[0];
        double priceOffset;
        int countsOffset = optionCount - mMenuBean.cusCount;
        if (mType == CartCacheUtil.TYPE_OUT) {
            priceOffset = countsOffset * mMenuBean.priceOut;
        } else {
            priceOffset = countsOffset * mMenuBean.priceIn;
        }
        mDataBean.cusShopOrderPrice += priceOffset;
        mMenuBean.cusMenuTotalPrice += priceOffset;
        mDataBean.cusMenuCount += countsOffset;
        mMenuBean.cusCount += countsOffset;
        mConfirmListener.onCofirm(priceOffset, countsOffset);
    }

    private void handleOption() {
        int menuCount = 0;
        double priceOffset = 0;
        for (int i = 0; i < mOptionList.size(); i++) {
            MenuListBean.MenuOpiton menuOpiton = mOptionList.get(i);
            int offset = mOptionCounts[i] - menuOpiton.cusCount;
            menuOpiton.cusCount = mOptionCounts[i];
            menuCount += mOptionCounts[i];
            if (mType == CartCacheUtil.TYPE_OUT)
                priceOffset += menuOpiton.priceOut * offset;
            else
                priceOffset += menuOpiton.priceIn * offset;
            if (!mIsFromCart) {
                CartCacheUtil.getInstance().saveOption(menuOpiton, mType);
            }
        }
        int offset = menuCount - mMenuBean.cusCount;

        mDataBean.cusGroupCount += offset;
        mDataBean.cusMenuCount += offset;
        mDataBean.cusShopOrderPrice += priceOffset;
        mMenuBean.cusCount = menuCount;
        mConfirmListener.onCofirm(priceOffset, offset);//初始化总价
    }
}
