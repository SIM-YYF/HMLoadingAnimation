package com.hm;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

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
		ImageView  iv_fengshan = (ImageView) this.findViewById(R.id.iv_fengshan);
		RotateAnimation  ra = new RotateAnimation(
				0, 
				360, 
				Animation.RELATIVE_TO_SELF,//相对于X轴的伸缩模式 
				0.5f, //相对于X轴的坐标值    
				Animation.RELATIVE_TO_SELF,  //相对于Y轴的伸缩模式
				0.5f);//相对于Y轴的坐标值
		
		ra.setInterpolator(new LinearInterpolator());
		ra.setFillAfter(false);
		ra.setRepeatMode(Animation.RESTART);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setDuration(1500);
		ra.start();
		iv_fengshan.setAnimation(ra);
		
		mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);
	}

}
