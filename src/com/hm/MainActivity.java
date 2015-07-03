package com.hm;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {
	private static final int REFRESH_PROGRESS = 0x10;
	private LoadingView mLeafLoadingView;
	private int mProgress;
	
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    if (mProgress < 40) {
                        mProgress += 1;
                        // 随机800ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(800));
                        mLeafLoadingView.setProgress(mProgress);
                    } else {
                        mProgress += 1;
                        // 随机1200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(1200));
                        mLeafLoadingView.setProgress(mProgress);

                    }
                    break;

                default:
                    break;
            }
        };
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLeafLoadingView = (LoadingView) findViewById(R.id.leaf_loading);
		mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);
	}

}
