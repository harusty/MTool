package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.hyphenate.easeui.domain.EaseUser;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.DemoModel;
import com.zzkx.mtool.chat.util.ContactUtil;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MainMapPresenter;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.RightMenuHelper;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogFilter;
import com.zzkx.mtool.view.iview.IMainView;

import java.util.Collection;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/1.
 */

public class MainActivity extends BaseActivity implements IMainView, View.OnClickListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener {

    @BindView(R.id.swip_city_name)
    TextView mTvCityName;
    @BindView(R.id.search_main)
    EditText mEtSearchMain;
    @BindView(R.id.icon_locate)
    View mLocateView;
    @BindView(R.id.locate_pb)
    ProgressBar mPbLocate;
    @BindView(R.id.shop_pager)
    ViewPager mShopPager;
    @BindView(R.id.right_container)
    View mRightContainer;
    @BindView(R.id.icon_eye)
    View mIcEye;
    private MainMapPresenter mMainMapPresenter;
    private MapView mapView;
    private RightMenuHelper mRightMenuHelper;
    private DialogFilter mDialogFilter;
    private long currentBackPressedTime;

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public int getContentRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
//        StatusBarUtil.setStatusBarTranslucent(this,127,true);
        SPUtil.putBoolean(Const.IS_FIRST, false);
        setTitleDisable();
        mEtSearchMain.setOnClickListener(this);
        findViewById(R.id.layout_swip_city).setOnClickListener(this);
        mIcEye.setOnClickListener(this);
        findViewById(R.id.icon_list).setOnClickListener(this);
        findViewById(R.id.icon_locate).setOnClickListener(this);
        setMainMenuEnable();
        mRightMenuHelper = new RightMenuHelper(mRightContainer);
        initRightFilterView();
        //        if (SPUtil.getBoolean(Const.IS_LOGIN, false) && !DemoHelper.getInstance().isLoggedIn()) {
        //            HXLoginUtil.login(this, null);
        //        }
        updateContactHeaderAndNick();
    }

    private void updateContactHeaderAndNick() {
        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            Collection<EaseUser> values = DemoHelper.getInstance().getContactList().values();
            ContactUtil.getInstance(null).updateEase(values);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mMainMapPresenter = new MainMapPresenter(this, mapView);
        mapView.getMap().setOnCameraChangeListener(this);
        mapView.getMap().setOnMapLoadedListener(this);

//        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
//            @Override
//            public void onMessageReceived(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> list) {
//                for(int i = 0 ;i<list.size();i++){
//                    EMMessage emMessage = list.get(i);
//                    EMCmdMessageBody body = (EMCmdMessageBody) emMessage.getBody();
//                    System.out.println("cmd action: "+body.action());
//
//                }
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageRecalled(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage emMessage, Object o) {
//
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CurrentCityInfo currentCity = MyApplication.getInstance().getCurrentCity();
        if (currentCity == null)
            return;
        mTvCityName.setText(currentCity.city);
        mMainMapPresenter.clear();
        mMainMapPresenter.searchByBound("", currentCity.latitude, currentCity.longitude, null);
    }

    @Override
    public void onMapLoaded() {
        mMainMapPresenter.locate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_eye:
                mDialogFilter.toggleRightFilter(mIcEye);
                break;
            case R.id.icon_list:
                if (MyApplication.getInstance().getCurrentCity() == null) {
                    ToastUtils.showToast("请重新定位");
                    return;
                }
                startActivity(new Intent(this, ShopListActivity.class));
                break;
            case R.id.icon_locate:
                mMainMapPresenter.locate();
                break;
            case R.id.layout_swip_city:
                startActivity(new Intent(this, CityChangeActivity.class));
                break;
            case R.id.search_main:
                startActivity(new Intent(this, SearchFoodShopActivity.class));
//                startActivity(new Intent(this, PicturePreviewActivity.class));
//                hx1505354084328
//                EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
//                String action = Const.STATE_ACTION_SUPPORT;//action可以自定义
//                String action = Const.STATE_ACTION_AT;//action可以自定义
//                EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
//                String toUsername = "hx1505354084328";//发送给某个人
//                cmdMsg.setTo(toUsername);
//                cmdMsg.addBody(cmdBody);
//                cmdMsg.setAttribute(Const.MSG, "再发表动态中@到我");
//                cmdMsg.setAttribute(Const.NAME, "帅哥");
//                cmdMsg.setAttribute(Const.URL, "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3762172220,2665402044&fm=27&gp=0.jpg");
//                EMClient.getInstance().chatManager().sendMessage(cmdMsg);

//                UploadInfo loadInfo = new UploadInfo();
//                loadInfo.setCoordType(NearbySearch.AMAP);
//                loadInfo.setPoint(new LatLonPoint(39.89422,116.511058));
//                loadInfo.setUserID("1234");
//                NearbySearch.getInstance(getApplicationContext())
//                        .uploadNearbyInfoAsyn(loadInfo);
//
//                EMMessage message = EMMessage.createTxtSendMessage("aaaa", "testuser1");
//                EMClient.getInstance().chatManager().sendMessage(message);
                break;
        }
    }


    private void initRightFilterView() {
        mDialogFilter = new DialogFilter(this, new DialogFilter.OnMenuClick() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        mMainMapPresenter.searchByBound(null, null);
                        break;
                    case 1:
                        mMainMapPresenter.searchByBound("s_takeOut", "1");
                        break;
                    case 2:
                        mMainMapPresenter.searchByBound("s_takeIn", "1");
                        break;
                }
                mDialogFilter.dismiss();
            }
        });
    }

    @Override
    public void showLocatePb(boolean show) {
        if (show)
            mLocateView.setVisibility(View.INVISIBLE);
        else
            mLocateView.setVisibility(View.VISIBLE);
    }

    @Override
    public String getKeyWord() {
        return mEtSearchMain.getText().toString();
    }

    @Override
    public ViewPager getViewPager() {
        return mShopPager;
    }

    @Override
    public void setCurCity(String city) {
        mTvCityName.setText(city);
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - currentBackPressedTime > 2000) {
            currentBackPressedTime = System.currentTimeMillis();
            ToastUtils.showToast("再按一次返回键退出程序");
        } else {
            // 退出
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocateUtil.getInstance().stopLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMainMapPresenter != null)
            mMainMapPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onDestroy();
        DemoModel demoModel = new DemoModel(this);
        demoModel.setContactSynced(false);
    }

    @Override
    public void showProgress(boolean toShow) {
        super.showProgress(toShow);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mMainMapPresenter.onCameraChangeFinish(cameraPosition);
    }

    @Override
    public void onReload() {

    }

}
