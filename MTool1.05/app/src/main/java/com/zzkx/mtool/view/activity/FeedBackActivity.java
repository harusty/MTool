package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.view.customview.CategoryLayout;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/19.
 */

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tag_container)
    CategoryLayout mTagContainer;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_content_leng)
    TextView mTvContentLen;
    @BindView(R.id.image_container)
    LinearLayout mImageContainer;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;

    private static final int MAX_CONTENT_LEN = 200;
    private static final int MAX_IMAGE_NUM = 3;

    @Override
    public int getContentRes() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("反馈问题");
        mTagContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(this, 10));
        mTagContainer.setVerticalSpacing(Dip2PxUtils.dip2px(this, 10));
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    mTvContentLen.setText(0 + "/" + MAX_CONTENT_LEN);
                } else {
                    mTvContentLen.setText(str.length() + "/" + MAX_CONTENT_LEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mIvAdd.setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
//                startActivity(new Intent(this, ImageGridActivity.class));
                startActivity(new Intent(this, PicturePreviewActivity.class));
                break;
        }
    }
}
