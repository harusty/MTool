package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.LocalImageBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GetImgUtil;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.LocalImageFetcher;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/21.
 */

public class PicturePreviewActivity extends BaseActivity {
    @BindView(R.id.gridView)
    GridView mGridView;
    private ArrayList<String> mSelectedList;
    private View mCustomMenuButton;
    private ImgAdapter mAdapter;
    private int mMaxSize;

    @Override
    public int getContentRes() {
        return R.layout.frag_picture_preview;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("选择图片");
        mMaxSize = getIntent().getIntExtra("size", 9);
        mCustomMenuButton = View.inflate(this, R.layout.custom_menu_button, null);
        mCustomMenuButton.setVisibility(View.GONE);
        mCustomMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data",mSelectedList);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        setCustomMenuButton(mCustomMenuButton);
        mSelectedList = new ArrayList<>();
        LocalImageFetcher.getInstance().getAllImage(this, new LocalImageFetcher.OnScanFinishListener() {
            @Override
            public void onScanFinish(List<LocalImageBean> allImgPathes) {
                mPathList = allImgPathes;
                mAdapter = new ImgAdapter(allImgPathes, new RectChekBox.OnCheckChangeListener() {
                    @Override
                    public void onChange(RectChekBox chekBox, boolean b) {
//                        int position = (int) chekBox.getTag();
//                        LocalImageBean localImageBean = mPathList.get(position);
//                        localImageBean.isSelected = b;
//                        if (b) {
//                            mSelectedList.add(localImageBean.path);
//                        } else {
//                            mSelectedList.remove(localImageBean.path);
//                        }
//                        if (mSelectedList.size() > 0) {
//                            mCustomMenuButton.setVisibility(View.VISIBLE);
//                        } else {
//                            mCustomMenuButton.setVisibility(View.GONE);
//                        }
                    }
                });
                mGridView.setAdapter(mAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LocalImageBean localImageBean = mPathList.get(position);
                        if(!localImageBean.isSelected && mSelectedList.size() == mMaxSize) {
                            ToastUtils.showToast("最多选9张");
                            return;
                        }
                        localImageBean.isSelected = !localImageBean.isSelected;
                        Holder tag = (Holder) view.getTag();
                        tag.checkBox.setChecked(localImageBean.isSelected);
                        if (localImageBean.isSelected) {
                            mSelectedList.add(localImageBean.path);
                        } else {
                            mSelectedList.remove(localImageBean.path);
                        }
                        if (mSelectedList.size() > 0) {
                            mCustomMenuButton.setVisibility(View.VISIBLE);
                        } else {
                            mCustomMenuButton.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalImageFetcher.getInstance().onDestroy();
    }

    List<LocalImageBean> mPathList;
    private File mImagePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GetImgUtil.REQUEST_CODE_CAPTURE_CAMERA) {
            Intent intent = new Intent();
            intent.putExtra(Const.URL, mImagePath.getAbsolutePath());
            setResult(requestCode, intent);
            finish();
        }
    }

    class ImgAdapter extends BaseAdapter {

        private RectChekBox.OnCheckChangeListener mListener;
        private List<LocalImageBean> mDataList;

        public ImgAdapter(List<LocalImageBean> mDataList, RectChekBox.OnCheckChangeListener listener) {
            this.mDataList = mDataList;
            mListener = listener;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(PicturePreviewActivity.this, R.layout.grid_item, null);
                holder.img = (ImageView) convertView.findViewById(R.id.id_item_image);
                holder.cam = convertView.findViewById(R.id.ll_cam);
                holder.checkBox = (RectChekBox) convertView.findViewById(R.id.checkbox);
                holder.checkBox.setBoxRes(R.mipmap.ic_58, R.mipmap.ic_59);
                holder.checkBox.setTouchable(false);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.checkBox.setTag(position);
            holder.checkBox.setOnCheckChangeListener(mListener);
            holder.img.setVisibility(View.VISIBLE);
            LocalImageBean localImageBean = mDataList.get(position);
            holder.checkBox.setChecked(localImageBean.isSelected);
            GlideUtil.getInstance().display(holder.img, localImageBean.path);
//                LocalImageLoader.getInstance(ThreadUtil.getCoresNum(), LocalImageLoader.Type.LIFO).loadImage(mDataList.get(position), holder.img);
//                BitmapHelper.getBitmapUtils().display(holder.img, mDataList.get(position));
            holder.cam.setVisibility(View.GONE);
            return convertView;
        }
    }

    class Holder {
        public ImageView img;
        public View cam;
        public RectChekBox checkBox;
    }

    @Override
    public void onReload() {

    }
}
