package com.sprd.quickwindow;

import com.cg.quickcircle.CGAnimation;
import com.cg.quickcircle.CircleLayout;
import com.cg.quickcircle.CircleViewBase;
import com.cg.quickcircle.TimeStatusView;
import com.cg.quickcircle.CircleLayout.OnCenterClickListener;
import com.cg.quickcircle.CircleLayout.OnItemClickListener;
import com.cg.quickcircle.CircleViewBase.OnBackButtonClickListener;
import com.cg.quickcircle.camera.CircleCamera;
import com.cg.quickcircle.mmssms.CircleSMS;
import com.cg.quickcircle.music.CircleMusic;
import com.cg.quickcircle.phone.CirclePhone;
import com.cg.quickcircle.setting.CircleSetting;

import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class QuickWindowService extends Service implements TimeStatusView.OnDoDump,OnItemClickListener,OnCenterClickListener,OnBackButtonClickListener {

    private View mMainView;
    private  WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    
	FrameLayout mFrameLayout  =  null;
	
	TimeStatusView mTimeStatusView = null;
	CircleLayout mCircleLayout = null;
	View mLauncher = null;
	
	CircleViewBase[] appviews = new CircleViewBase[]{new CircleSetting(), new CirclePhone(), new CircleCamera(), new CircleSMS(), new CircleMusic()};
	CircleViewBase currentCircleViewBase = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("QuickWindowService", "onCreate");

        mLayoutInflater = LayoutInflater.from(this);

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.flags = /*WindowManager.LayoutParams.FLAG_FULLSCREEN |*/
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        mMainView = mLayoutInflater.inflate(R.layout.activity_main, null);
        mWindowManager.addView(mMainView, mLayoutParams);
        
		mFrameLayout = (FrameLayout) mMainView.findViewById(R.id.quick_circle_fragment);
		mLayoutInflater = LayoutInflater.from(this);
		if(mTimeStatusView == null) {
			mTimeStatusView = (TimeStatusView)mLayoutInflater.inflate(R.layout.time_fragment, null);
		}
		mFrameLayout.addView(mTimeStatusView);
		mTimeStatusView.setOnDoDumpCallBack(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("QuickWindowService", "onDestroy");
        mWindowManager.removeView(mMainView);
    }
    

	@Override
	public void dodump(boolean lr) {
		// TODO Auto-generated method stub
		if(mLauncher == null) {
			mLauncher = mLayoutInflater.inflate(R.layout.launcher_fragment, null);
			mCircleLayout = (CircleLayout)mLauncher.findViewById(R.id.launcher_circle_layout);
			mCircleLayout.setOnItemClickListener(this);
			mCircleLayout.setOnCenterClickListener(this);
		}
		mFrameLayout.removeAllViews();
		mFrameLayout.addView(mLauncher);
		
		float cX = mFrameLayout.getWidth() / 2.0f;
        float cY = mFrameLayout.getHeight() / 2.0f;
        Log.d("xtest", "dodump:" + lr);
        CGAnimation rotateAnim = new CGAnimation(cX, cY, lr ? CGAnimation.ROTATE_DECREASE : CGAnimation.ROTATE_INCREASE);
		mLauncher.startAnimation(rotateAnim);
	}

	@Override
	public void onCenterClick() {
		// TODO Auto-generated method stub
		if(mTimeStatusView != null) {
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(mTimeStatusView);
		}
	}

	@Override
	public void onItemClick(View view) {
		// TODO Auto-generated method stub
		int viewid = view.getId();
		switch (viewid) {
		case R.id.main_phone_image:
			Log.d("xtest", "main_phone_image clicked");
			break;
		case R.id.main_sms_image:
			Log.d("xtest", "main_sms_image clicked");
			break;
		case R.id.main_camera_image:
			Log.d("xtest", "main_camera_image clicked");
			break;
		case R.id.main_music_image:
			Log.d("xtest", "main_music_image clicked");
			break;
		case R.id.main_setting_image:
			Log.d("xtest", "main_setting_image clicked");
			break;
		default:
			break;
		}
		
		for (CircleViewBase mCircleViewBase : appviews) {
			if(mCircleViewBase.getID() == viewid) {
				currentCircleViewBase = mCircleViewBase;
				break;
			}
		}
		
		if(currentCircleViewBase != null) {
			if(!currentCircleViewBase.isContextAttached()) {
				currentCircleViewBase.attachContext(this);
				currentCircleViewBase.setOnBackButtonClickListener(this);
			}
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(currentCircleViewBase.getView());
			currentCircleViewBase.onAdd();
		}
		
	}

	@Override
	public void onBackButtionClick() {
		// TODO Auto-generated method stub
		if(currentCircleViewBase != null) {
			currentCircleViewBase.onRemove();
		}
		mFrameLayout.removeAllViews();
		mFrameLayout.addView(mLauncher);
	}

}
