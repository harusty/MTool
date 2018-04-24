package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ShopGallaryPresenter;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.adapter.ShopGallaryAdapter;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IShopGallaryView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/13.
 */

public class ShopGallaryFragment extends BaseFragment implements IShopGallaryView {
    @BindView(R.id.grid)
    GridView mGridView;
    private ShopGallaryPresenter mShopGallaryPresenter;
    private ArrayList<StateListBean.ResData> mData;

    @Override
    public int getContentRes() {
        return R.layout.fragment_shop_gallary;
    }

    @Override
    public void initNet() {
        String id = getActivity().getIntent().getStringExtra(Const.ID);
        mShopGallaryPresenter.getData(id);
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mShopGallaryPresenter = new ShopGallaryPresenter(this);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showData(ArrayList<StateListBean.ResData> data) {
        if (data != null) {
            mData = data;
            ShopGallaryAdapter shopGallaryAdapter = new ShopGallaryAdapter(data);
            mGridView.setAdapter(shopGallaryAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(getActivity(), StateImgActivity.class)
                            .putExtra(Const.RES, mData)
                            .putExtra(Const.INDEX, position)
                    );
                }
            });
        } else {
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
    }
}
