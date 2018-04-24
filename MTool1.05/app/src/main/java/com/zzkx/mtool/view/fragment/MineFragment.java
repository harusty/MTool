package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.view.activity.BusinessPartner;
import com.zzkx.mtool.view.activity.CollectActivity;
import com.zzkx.mtool.view.activity.HistorOrderListActivity;
import com.zzkx.mtool.view.activity.MyStateActivity;
import com.zzkx.mtool.view.activity.MyWalleteActivity;
import com.zzkx.mtool.view.activity.ServiceCenterActivity;
import com.zzkx.mtool.view.activity.SupposedAvtivity;

/**
 * Created by sshss on 2017/8/1.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initNet() {

    }

    @Override
    public void initViews() {
        setTitleDisable();
        mBaseView.findViewById(R.id.layout_order_history).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_collect).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_supposed).setOnClickListener(this);
        mBaseView.findViewById(R.id.lo_my_state).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_be_partner).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_wallete).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_service).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_order_history:
                startActivity(new Intent(getActivity(), HistorOrderListActivity.class));
                break;
            case R.id.layout_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.layout_supposed:
                startActivity(new Intent(getActivity(), SupposedAvtivity.class));
                break;
            case R.id.lo_my_state:
                startActivity(new Intent(getActivity(), MyStateActivity.class));
                break;
            case R.id.layout_be_partner:
                startActivity(new Intent(getActivity(), BusinessPartner.class));
                break;
            case R.id.layout_wallete:
                startActivity(new Intent(getActivity(), MyWalleteActivity.class));
                break;
            case R.id.layout_service:
                startActivity(new Intent(getActivity(), ServiceCenterActivity.class));
                break;
        }
    }
}
