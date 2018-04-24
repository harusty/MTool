package com.zzkx.mtool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by sshss on 2017/12/16.
 */

public class CrashInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_info);
        TextView info = (TextView) findViewById(R.id.tv_info);
        info.setText(getIntent().getStringExtra("crash_info"));
    }
}
