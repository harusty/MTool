package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.ui.EaseMainActivity;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.view.activity.CartActivity;
import com.zzkx.mtool.view.activity.LoginActivity;
import com.zzkx.mtool.view.activity.MineActivity;
import com.zzkx.mtool.view.activity.ScanActivity;
import com.zzkx.mtool.view.activity.ShopListActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sshss on 2017/8/22.
 */

public class MainMenu extends FrameLayout implements View.OnClickListener {
    private ViewGroup mLayoutMenuBtton;
    private Context mContext;
    private AlphaAnimation mAlphaAnimation2;
    private View mBgView;
    private ImageView mMenuButton;
    private View mRootView;
    private LinearLayout mVertLayout;
    private LinearLayout mHoriLayout;
    private View mMenuView;
    private AlphaAnimation mAlphaAnimation;
    private ScaleAnimation mScaleAnimation;
    private ScaleAnimation mScaleAnimation2;
    private ViewGroup mSecMenuContainer;
    private HashMap<Integer, View> mSecMenus;

    private static final int MAIN_SEC = 0;
    private static final int BASKET_SEC = 1;
    private static final int SHOPING_SEC = 2;
    private DialogSearch mDialogSearch;
    private boolean isShow;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            resetUnredCount();
        }
    };
    private EMMessageListener mMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            mHandler.sendEmptyMessage(999);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
    private TextView mTvMsgCount;
    private View mMsgTag;

    public boolean isShow() {
        return isShow;
    }

    public MainMenu(@NonNull Context context) {
        this(context, null);
    }

    public MainMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSecMenus = new HashMap<>();
        mRootView = View.inflate(context, R.layout.main_menu_view, null);
        ViewParent parent = mRootView.getParent();
        if (parent != null)
            ((ViewGroup) parent).removeAllViews();
        mMenuView = mRootView.findViewById(R.id.menu_view);

        mSecMenuContainer = (ViewGroup) mRootView.findViewById(R.id.second_menu_content);
        mLayoutMenuBtton = (ViewGroup) mRootView.findViewById(R.id.layout_menu_button);
        mHoriLayout = (LinearLayout) mRootView.findViewById(R.id.hori_layout);
        mVertLayout = (LinearLayout) mRootView.findViewById(R.id.vert_layout);
        mMenuButton = (ImageView) mRootView.findViewById(R.id.locateView);
        mBgView = mRootView.findViewById(R.id.bg_view);

        mMenuView.setVisibility(INVISIBLE);
        mBgView.setVisibility(INVISIBLE);
        mBgView.setOnClickListener(this);
        mMenuButton.setOnClickListener(this);
        View container = mRootView.findViewById(R.id.container);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) container.getLayoutParams();
        int statusBarHeight = 0;
        param.setMargins(0, statusBarHeight, 0, 0);
        mContext = context;
        initAnim();
        initChild(context);
        addView(mRootView);

        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }


    @Override
    protected void onDetachedFromWindow() {
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
        super.onDetachedFromWindow();
    }

    private void initAnim() {

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBgView.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBgView.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        mScaleAnimation = new ScaleAnimation(0.0f, 1f, 0.0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF, 0.1f);
        mScaleAnimation2 = new ScaleAnimation(1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF, 0.1f);
        mAlphaAnimation = new AlphaAnimation(0, 1);
        mAlphaAnimation2 = new AlphaAnimation(1, 0);
        mScaleAnimation.setDuration(200);
        mScaleAnimation2.setDuration(200);
        mAlphaAnimation.setDuration(200);
        mAlphaAnimation2.setDuration(200);
        mAlphaAnimation.setAnimationListener(animationListener);
        mAlphaAnimation2.setAnimationListener(animationListener);
        mScaleAnimation.setAnimationListener(animationListener);
        mScaleAnimation2.setAnimationListener(animationListener);
        mScaleAnimation.setInterpolator(new DecelerateInterpolator());
        mScaleAnimation2.setInterpolator(new DecelerateInterpolator());
        mAlphaAnimation.setInterpolator(new DecelerateInterpolator());
        mAlphaAnimation2.setInterpolator(new DecelerateInterpolator());
    }

    private void initChild(@NonNull Context context) {
        mHoriLayout.removeAllViews();
        mVertLayout.removeAllViews();
        Resources resources = getResources();
        setHoriView(R.mipmap.ic_tag, resources.getString(R.string.event), R.id.event);
        setHoriView(R.mipmap.ic_zoom_menu, resources.getString(R.string.search), R.id.search);
        setVertView(R.mipmap.ic_basket, resources.getString(R.string.shoping), R.id.basket);
        setVertView(R.mipmap.ic_cart, resources.getString(R.string.shopingcar), R.id.cart);
        View view = setVertView(R.mipmap.ic_message, resources.getString(R.string.message), R.id.message);
        mTvMsgCount = (TextView) view.findViewById(R.id.tv_msg);
        mMsgTag = view.findViewById(R.id.iv_tag);
        setVertView(R.mipmap.ic_mine, resources.getString(R.string.mine), R.id.mine);
        setVertView(R.mipmap.ic_scan, resources.getString(R.string.scan), R.id.scan);
    }


    private View setVertView(int img_res, String title, int id) {
        ViewGroup menu = (ViewGroup) View.inflate(getContext(), R.layout.item_main_menu_message, null);
        menu.setId(id);
        ((ImageView) menu.findViewById(R.id.iv_tag)).setImageResource(img_res);
        ((TextView) menu.findViewById(R.id.tv_title)).setText(title);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        menu.setLayoutParams(layoutParams);
        menu.setOnClickListener(this);
        mVertLayout.addView(menu);
        return menu;
    }

    private void setHoriView(int img_res, String title, int id) {
        ViewGroup menu = (ViewGroup) View.inflate(getContext(), R.layout.item_main_menu_message, null);
        menu.setId(id);
        ((ImageView) menu.findViewById(R.id.iv_tag)).setImageResource(img_res);
        ((TextView) menu.findViewById(R.id.tv_title)).setText(title);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        menu.setLayoutParams(layoutParams);
        menu.setOnClickListener(this);
        mHoriLayout.addView(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locateView:
                if (mMenuView.getVisibility() == VISIBLE) {
                    dismiss(true);
                } else {
                    show();
                }
                break;
            case R.id.bg_view:
                dismiss(true);
                break;
            case R.id.event:
                break;
            case R.id.search:
                if (mDialogSearch == null) {
                    mDialogSearch = new DialogSearch(mContext);
                }
                mDialogSearch.show();
                dismiss(true);
                break;
            case R.id.basket:
                clickBasket(v);
                break;
            case R.id.cart:
                if (SPUtil.getBoolean(Const.IS_LOGIN, false)) {
                    getContext().startActivity(new Intent(getContext(), CartActivity.class));
                } else {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));

                }
                dismiss(false);
                break;
            case R.id.message:
                dismiss(false);
                if (SPUtil.getBoolean(Const.IS_LOGIN, false)) {
                    mContext.startActivity(new Intent(mContext, EaseMainActivity.class));
                } else {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.mine:
                if (SPUtil.getBoolean(Const.IS_LOGIN, false)) {
                    getContext().startActivity(new Intent(getContext(), MineActivity.class));
                } else {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }
//                 getContext().startActivity(new Intent(getContext(), MineActivity.class));
                dismiss(false);
                break;
            case R.id.scan:
//                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                getContext().startActivity(new Intent(getContext(), ScanActivity.class));
                break;
        }
    }

    private void clickBasket(View v) {
        Object tag = v.getTag();
        ImageView child = (ImageView) v.findViewById(R.id.iv_tag);
        boolean flag;
        if (tag == null) {
            child.setImageResource(R.mipmap.ic_basket_selected);
            hidePreView();
            createBasketMenu();
            flag = true;
        } else if ((boolean) tag) {
            showSecMenu(mSecMenus.get(MAIN_SEC));
            child.setImageResource(R.mipmap.ic_basket);
            flag = false;
        } else {
            showSecMenu(mSecMenus.get(BASKET_SEC));
            child.setImageResource(R.mipmap.ic_basket_selected);
            flag = true;
        }
        v.setTag(flag);
    }

    private void createBasketMenu() {
        Resources res = getContext().getResources();
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mContext.startActivity(new Intent(mContext, ShopListActivity.class));
                dismiss(false);
            }
        };
//        View mBasketMenu = createSecondMenu(new int[]{R.mipmap.ic_spon,
//                        R.mipmap.ic_quick
//                        , R.mipmap.ic_service}
//                , new String[]{res.getString(R.string.food2)
//                        , res.getString(R.string.quick)
//                        , res.getString(R.string.city_serv)}, clickListener);
        View mBasketMenu = createSecondMenu(new int[]{R.mipmap.ic_spon,}
                , new String[]{res.getString(R.string.food2)}, clickListener);
        mSecMenus.put(BASKET_SEC, mBasketMenu);
    }

    private void showSecMenu(View nextView) {
        hidePreView();
        if (nextView != null)
            nextView.setVisibility(VISIBLE);
    }

    private void hidePreView() {
        Set<Map.Entry<Integer, View>> entries = mSecMenus.entrySet();
        for (Map.Entry<Integer, View> entry : entries) {
            View value = entry.getValue();
            if (value.getVisibility() == View.VISIBLE)
                value.setVisibility(INVISIBLE);
        }
    }


    public void dismiss(boolean anim) {
        isShow = false;
        mMenuView.setVisibility(GONE);
        mBgView.setVisibility(INVISIBLE);
        if (anim) {
            mBgView.setAnimation(mAlphaAnimation2);
            mMenuView.setAnimation(mScaleAnimation2);
            mAlphaAnimation2.start();
            mScaleAnimation2.start();
        }
        mMenuButton.setImageResource(R.mipmap.ic_menu);
    }

    public void show() {
        isShow = true;
        mMenuView.setVisibility(VISIBLE);
        mBgView.setVisibility(VISIBLE);
        mBgView.setAnimation(mAlphaAnimation);
        mMenuView.setAnimation(mScaleAnimation);
        mAlphaAnimation.start();
        mScaleAnimation.start();
        mMenuButton.setImageResource(R.mipmap.ic_main_button_cancle);
        resetUnredCount();
    }

    private void resetUnredCount() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (unreadMessageCount > 0) {
            mTvMsgCount.setVisibility(VISIBLE);
            mMsgTag.setVisibility(INVISIBLE);
            if (unreadMessageCount > 99)
                mTvMsgCount.setText("...");
            else
                mTvMsgCount.setText(unreadMessageCount + "");
            invalidate();
        } else {
            mTvMsgCount.setVisibility(INVISIBLE);
            mMsgTag.setVisibility(VISIBLE);
        }
    }


    public void setSecondMenu(int[] imgRes, String[] titles, View.OnClickListener clickListener) {
        View mainMenu = createSecondMenu(imgRes, titles, clickListener);
        mSecMenus.put(MAIN_SEC, mainMenu);
    }

    public void hideMainSecMenu() {
        View view = mSecMenus.get(MAIN_SEC);
        if (view != null) {
            view.setVisibility(INVISIBLE);
        }
    }

    public void showMianSecMenu() {
        View view = mSecMenus.get(MAIN_SEC);
        if (view != null) {
            view.setVisibility(VISIBLE);
        }
    }

    private View createSecondMenu(int[] imgRes, String[] titles, View.OnClickListener clickListener) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < imgRes.length; i++) {
            ViewGroup viewRoot = (ViewGroup) View.inflate(getContext(), R.layout.item_sec_menu, null);
            ViewGroup view = (ViewGroup) viewRoot.getChildAt(0);
            view.setTag(i);
            ((ImageView) view.getChildAt(0)).setImageResource(imgRes[i]);
            ((TextView) view.getChildAt(1)).setText(titles[i]);
            view.setOnClickListener(clickListener);
            linearLayout.addView(viewRoot);
        }
        mSecMenuContainer.addView(linearLayout);
        return linearLayout;
    }

    public ViewGroup getSecMenuItem(int position) {
        ViewGroup view = (ViewGroup) mSecMenus.get(MAIN_SEC);
        return (ViewGroup) view.getChildAt(position);
    }

    public void changeSecMenuItem(int position, int res, String title) {

        ViewGroup secMenuItem = getSecMenuItem(position);
        ImageView imageView = (ImageView) secMenuItem.findViewById(R.id.image);
        TextView textView = (TextView) secMenuItem.findViewById(R.id.text);
        if (res > 0)
            imageView.setImageResource(res);
        if (!TextUtils.isEmpty(title))
            textView.setText(title);
    }

    public void setCustomMenuButton(View view){
        mLayoutMenuBtton.addView(view);
    }
}
