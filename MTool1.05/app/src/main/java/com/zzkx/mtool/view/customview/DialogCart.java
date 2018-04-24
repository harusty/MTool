package com.zzkx.mtool.view.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MenuOptionBean;
import com.zzkx.mtool.db.MenuBean;
import com.zzkx.mtool.imple.BottomCartCtrlListener;
import com.zzkx.mtool.util.CartCacheUtil;

import java.util.List;

/**
 * Created by sshss on 2017/8/29.
 */

public class DialogCart implements View.OnClickListener {
    private String mShopId;
    private Activity mActivity;
    private BottomCartCtrlListener mCtrlListener;
    private AlphaAnimation mAlphaAnimation;
    private AlphaAnimation mAlphaAnimation2;
    private ViewGroup mOptionContainer;
    private MainMenu mFakeMenu;
    private MainMenu mMainMenu;
    private View mTransView;
    private View mContentView;
    private Animation mSlideUp;
    private Animation mSlideDown;
    private AlertDialog mClearDialog;
    private int mType;


    /**
     * @param activity
     * @param view      规格展示内容呢区域
     * @param transView 黑色半透明背景
     * @param mainMenu  主导航按钮
     * @param fakeMenu  临时主导航（主导航在整个content区的上面，用fakeview制造主导航在transView下面的假象）
     */
    public DialogCart(Activity activity, View view,
                      View transView, MainMenu mainMenu,
                      MainMenu fakeMenu, String shopId) {
        mShopId = shopId;
        mActivity = activity;
        mMainMenu = mainMenu;
        mFakeMenu = fakeMenu;
        mTransView = transView;
        mContentView = view;
        mOptionContainer = (ViewGroup) mContentView.findViewById(R.id.option_container);


        mTransView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSlideUp = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.slide_up);
        mSlideDown = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.slide_down);
        mAlphaAnimation = new AlphaAnimation(0, 1);
        mAlphaAnimation2 = new AlphaAnimation(1, 0);
        mAlphaAnimation2.setDuration(200);
        mAlphaAnimation.setDuration(200);
    }

    public void setContrlListener(BottomCartCtrlListener ctrlListener, int type) {
        mType = type;
        mCtrlListener = ctrlListener;
        initClearDialog(mActivity);
    }


    private void initClearDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Resources resources = activity.getResources();
        builder.setMessage(resources.getString(R.string.confrim_clear_cart));
        builder.setPositiveButton(resources.getString(R.string.confrim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCtrlListener.onClearShopCache();
                dismiss();
                mOptionContainer.removeAllViews();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(resources.getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mClearDialog = builder.create();
    }


    private void setMenuItem() {
        mOptionContainer.removeAllViews();
        List<MenuBean> menus = CartCacheUtil.getInstance().getMenus(mShopId);
        if (menus != null && menus.size() > 0) {
            for (MenuBean menuBean : menus) {
                int count = 0;
                double price = 0;
                if (mType == CartCacheUtil.TYPE_OUT) {
                    count = menuBean.outCount;
                    price = menuBean.priceOut;
                } else {
                    count = menuBean.inCount;
                    price = menuBean.priceIn;
                }
                if (count == 0)
                    continue;
                View view = createItem();
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.menuBean = menuBean;
                holder.tv_name.setText(menuBean.name);
                holder.tv_money.setText(String.valueOf(price));
                holder.tv_count.setText(String.valueOf(count));
                holder.iv_minus.setOnClickListener(this);
                holder.iv_plus.setOnClickListener(this);
                holder.iv_minus.setTag(holder);
                holder.iv_plus.setTag(holder);
                mOptionContainer.addView(view);
                mViewCache.put(position, view);
                holder.position = position;
                position++;
            }
        }
        List<MenuOptionBean> options = CartCacheUtil.getInstance().getTotalOptions(mShopId);
        if (options != null && options.size() > 0) {
            for (MenuOptionBean optionBean : options) {
                int count = 0;
                double price = 0;
                if (mType == CartCacheUtil.TYPE_OUT) {
                    count = optionBean.outCount;
                    price = optionBean.priceOut;
                } else {
                    count = optionBean.inCount;
                    price = optionBean.priceIn;
                }

                if (count == 0)
                    continue;
                View view = createItem();
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.tv_spec.setVisibility(View.VISIBLE);
                holder.tv_spec.setText(optionBean.name);
                holder.tv_money.setText(String.valueOf(price));
                holder.tv_count.setText(String.valueOf(count));
                holder.tv_name.setText(optionBean.menuName);
                holder.iv_minus.setOnClickListener(this);
                holder.iv_plus.setOnClickListener(this);
                holder.optionBean = optionBean;
                holder.iv_minus.setTag(holder);
                holder.iv_plus.setTag(holder);
                mOptionContainer.addView(view);
                mViewCache.put(position, view);
                holder.position = position;
                position++;
            }
        }
    }

    private View createItem() {
        View child = View.inflate(MyApplication.getContext(), R.layout.item_menu_option, null);
        ViewHolder holder = new ViewHolder(child);
        child.setTag(holder);
        return child;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        int count = 0;
        Object object = null;
        ViewHolder holder = null;
        if (tag != null) {
            holder = (ViewHolder) tag;
            count = Integer.parseInt(holder.tv_count.getText().toString());
            object = holder.optionBean == null ? holder.menuBean : holder.optionBean;
        }
        switch (v.getId()) {
            case R.id.iv_plus:
                mCtrlListener.onAdd(object);
                count++;
                holder.tv_count.setText(String.valueOf(count));
                break;
            case R.id.iv_minus:
                count--;
                if (count < 0) {
                    return;
                }
                holder.tv_count.setText(String.valueOf(count));
                mCtrlListener.onSubtract(object);
                break;
            case R.id.layout_tras:
                mClearDialog.show();
                break;
        }
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_money;
        TextView tv_count;
        TextView tv_spec;
        View iv_minus;
        View iv_plus;
        public MenuBean menuBean;
        public MenuOptionBean optionBean;
        public int position;

        public ViewHolder(View child) {
            tv_name = (TextView) child.findViewById(R.id.tv_section);
            tv_money = (TextView) child.findViewById(R.id.tv_money);
            tv_count = (TextView) child.findViewById(R.id.tv_count);
            tv_spec = (TextView) child.findViewById(R.id.tv_spec);
            iv_minus = child.findViewById(R.id.iv_minus);
            iv_plus = child.findViewById(R.id.iv_plus);
        }
    }

    public void toggle() {
        if (mContentView.getVisibility() == View.VISIBLE) {
            dismiss();
        } else {
            show();
        }
    }

    public void dismiss() {
        if (mContentView.getVisibility() != View.VISIBLE) {
            return;
        }
        mMainMenu.setVisibility(View.VISIBLE);
        mFakeMenu.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.GONE);
        mContentView.setAnimation(mSlideDown);
        mTransView.setAnimation(mAlphaAnimation2);
        mSlideDown.start();
        mAlphaAnimation2.start();
        mTransView.setVisibility(View.GONE);
    }

    int position = 0;
    private SparseArray<View> mViewCache = new SparseArray<>();

    private void show() {
        /**
         * layout_tras两个dialog的共享部分，每次show都要重新绑定点击监听
         */
        mContentView.findViewById(R.id.layout_tras).setOnClickListener(this);
        position = 0;
        mViewCache.clear();
        setMenuItem();
        mMainMenu.setVisibility(View.INVISIBLE);
        mFakeMenu.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.VISIBLE);
        mContentView.setAnimation(mSlideUp);
        mTransView.setAnimation(mAlphaAnimation);
        mSlideUp.start();
        mAlphaAnimation.start();
        mTransView.setVisibility(View.VISIBLE);
    }
}
