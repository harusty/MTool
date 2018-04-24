package com.zzkx.mtool.view.activity;

import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.Formatter;
import com.zzkx.mtool.util.GlideUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/29.
 */

public class VideoPlayerAcitivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.surface)
    SurfaceView mSurfaceView;
    @BindView(R.id.first_image)
    ImageView mFirstImage;
    @BindView(R.id.layout_tmp)
    View mLayoutTmp;
    @BindView(R.id.btn_center)
    View mBtnCenter;
    @BindView(R.id.centerProgress)
    ProgressBar mCenterProgress;

    @BindView(R.id.currentPosition)
    TextView mTvCurrentTime;
    @BindView(R.id.totalDuration)
    TextView mTotalTime;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.progress)
    SeekBar mPlayerProgress;
    @BindView(R.id.player_layout)
    View mPlayerLayout;

    private AliVcMediaPlayer mPlayer;
    private String mUrl;
    private boolean isCompleted;
    private boolean inSeek;

    @Override
    public int getContentRes() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mUrl = getIntent().getStringExtra(Const.URL);
        String coverUrl = getIntent().getStringExtra(Const.COVER_URL);
        GlideUtil.getInstance().display(mFirstImage, coverUrl);
        mBtnCenter.setOnClickListener(this);
        mPlayerLayout.setVisibility(View.INVISIBLE);
        mSurfaceView.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mLayoutTmp.setOnClickListener(this);
        findViewById(R.id.layout_back).setOnClickListener(this);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
//                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
                holder.setKeepScreenOn(true);
                // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                if (mPlayer != null) {
                    mPlayer.setVideoSurface(holder.getSurface());
                }

            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

                if (mPlayer != null) {
                    mPlayer.setSurfaceChanged();
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mPlayer != null) {
                    mPlayer.releaseVideoSurface();
                }
            }
        });
        mPlayerProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null) {
                    mPlayer.seekTo(seekBar.getProgress());
                    if (isCompleted) {
                        inSeek = false;
                    } else {
                        inSeek = true;
                    }
                }
            }
        });
        initVodPlayer();
        if (getIntent().getBooleanExtra(Const.PLAY, false)) {
            play();
        }
    }

    private void initVodPlayer() {
        mPlayer = new AliVcMediaPlayer(this, mSurfaceView);
        mPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
                mLayoutTmp.setVisibility(View.INVISIBLE);
                mPlayerLayout.setVisibility(View.VISIBLE);
                mPlayer.play();
            }
        });
        mPlayer.setPcmDataListener(new MediaPlayer.MediaPlayerPcmDataListener() {
            @Override
            public void onPcmData(byte[] bytes, int i) {

            }
        });
        mPlayer.setFrameInfoListener(new MyFrameInfoListener(this));
        mPlayer.setErrorListener(new MediaPlayer.MediaPlayerErrorListener() {
            @Override
            public void onError(int i, String msg) {
                mPlayer.stop();
            }
        });
        mPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                isCompleted = true;
                showVideoProgressInfo();
                stopUpdateTimer();
                resetPlayer();
            }
        });
        mPlayer.setSeekCompleteListener(new MediaPlayer.MediaPlayerSeekCompleteListener() {
            @Override
            public void onSeekCompleted() {
                inSeek = false;
            }
        });
        mPlayer.setStoppedListener(new MediaPlayer.MediaPlayerStoppedListener() {
            @Override
            public void onStopped() {
            }
        });
        mPlayer.setBufferingUpdateListener(new MediaPlayer.MediaPlayerBufferingUpdateListener() {
            @Override
            public void onBufferingUpdateListener(int percent) {
                updateBufferingProgress(percent);
            }
        });
        mPlayer.enableNativeLog();
//        mPlayer.disableNativeLog();
    }

    private void resetPlayer() {
        mLayoutTmp.setVisibility(View.VISIBLE);
        mBtnCenter.setVisibility(View.VISIBLE);
        mCenterProgress.setVisibility(View.INVISIBLE);
        mPlayerLayout.setVisibility(View.INVISIBLE);
        showPlayer = false;
        mIvPlay.setImageResource(R.mipmap.ic_15);

        mTvCurrentTime.setText("00:00");
        mPlayerProgress.setProgress(0);

    }

    private void updateBufferingProgress(int percent) {
        int duration = (int) mPlayer.getDuration();
        int secondaryProgress = (int) (duration * percent * 1.0f / 100);
        mPlayerProgress.setSecondaryProgress(secondaryProgress);
    }

    @Override
    public void onReload() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying()) {
            pause();
        }
    }

    @Override
    protected void onDestroy() {
        stop();
        destroy();
        stopUpdateTimer();
        progressUpdateTimer = null;
        super.onDestroy();
    }

    //继续播放
    private void resume() {
        if (mPlayer != null) {
            mPlayer.play();
            mIvPlay.setImageResource(R.mipmap.ic_15);
        }
    }

    //暂停播放
    private void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            mIvPlay.setImageResource(R.mipmap.ic_14);
        }
    }

    //停止播放
    private void stop() {

        if (mPlayer != null) {
            mPlayer.stop();
        }
    }

    private void destroy() {
        if (mPlayer != null) {
            mPlayer.releaseVideoSurface();
            mPlayer.stop();
            mPlayer.destroy();
        }
    }

    private void replay() {
        stop();
        start();
    }

    private void start() {
        if (mPlayer != null) {
            mPlayer.prepareToPlay(mUrl);
        }
    }

    private boolean showPlayer;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_center:
                play();
                break;
            case R.id.iv_play:
                if (mPlayer.isPlaying()) {
                    pause();
                } else {
                    resume();
                }
                break;
            case R.id.surface:

                if (showPlayer) {
                    mPlayerLayout.setVisibility(View.VISIBLE);
                } else {
                    mPlayerLayout.setVisibility(View.INVISIBLE);
                }
                showPlayer = !showPlayer;
                break;
        }
    }

    public void play() {
        mBtnCenter.setVisibility(View.INVISIBLE);
        mCenterProgress.setVisibility(View.VISIBLE);

        if (mPlayer != null) {
            mPlayer.prepareToPlay(mUrl);
        }
    }


    private void onFrameShow() {
        inSeek = false;
        showVideoProgressInfo();
//        showVideoSizeInfo();
//        updateLogInfo();
    }

    private Handler progressUpdateTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            showVideoProgressInfo();
        }
    };

    private void showVideoProgressInfo() {
        int curPosition = (int) mPlayer.getCurrentPosition();
        int duration = (int) mPlayer.getDuration();
        int bufferPosition = mPlayer.getBufferPosition();
        if ((mPlayer.isPlaying())
                && !inSeek) {

            mTvCurrentTime.setText(Formatter.formatTime(curPosition));
            mTotalTime.setText(Formatter.formatTime(duration));
            mPlayerProgress.setMax(duration);
            mPlayerProgress.setSecondaryProgress(bufferPosition);
            mPlayerProgress.setProgress(curPosition);
        }

        startUpdateTimer();
    }

    private void startUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
        progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
    }

    private void stopUpdateTimer() {
        progressUpdateTimer.removeMessages(0);
    }

    private static class MyFrameInfoListener implements MediaPlayer.MediaPlayerFrameInfoListener {

        private WeakReference<VideoPlayerAcitivity> vodModeActivityWeakReference;

        public MyFrameInfoListener(VideoPlayerAcitivity vodModeActivity) {
            vodModeActivityWeakReference = new WeakReference<VideoPlayerAcitivity>(vodModeActivity);
        }

        @Override
        public void onFrameInfoListener() {
            VideoPlayerAcitivity vodModeActivity = vodModeActivityWeakReference.get();
            if (vodModeActivity != null) {
                vodModeActivity.onFrameShow();
            }
        }
    }
}
