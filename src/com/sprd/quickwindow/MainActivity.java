package com.sprd.quickwindow;

import com.cg.quickcircle.TimeStatusView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cg.quickcircle.CGAnimation;
import com.cg.quickcircle.CircleLayout;
import com.cg.quickcircle.CircleLayout.OnItemClickListener;
import com.cg.quickcircle.CircleLayout.OnCenterClickListener; 
import com.cg.quickcircle.CircleViewBase;
import com.cg.quickcircle.CircleViewBase.OnBackButtonClickListener;
import com.cg.quickcircle.camera.CircleCamera;
import com.cg.quickcircle.mmssms.CircleSMS;
import com.cg.quickcircle.music.CircleMusic;
import com.cg.quickcircle.phone.CirclePhone;
import com.cg.quickcircle.setting.CircleSetting;

public class MainActivity extends Activity implements TimeStatusView.OnDoDump,OnItemClickListener,OnCenterClickListener,OnBackButtonClickListener {
	
	FrameLayout mFrameLayout  =  null;
	LayoutInflater mLayoutInflater = null;
	
	TimeStatusView mTimeStatusView = null;
	CircleLayout mCircleLayout = null;
	View mLauncher = null;
	
	CircleViewBase[] appviews = new CircleViewBase[]{new CircleSetting(), new CirclePhone(), new CircleCamera(), new CircleSMS(), new CircleMusic()};
	CircleViewBase currentCircleViewBase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFrameLayout = (FrameLayout) findViewById(R.id.quick_circle_fragment);
		mLayoutInflater = LayoutInflater.from(this);
		if(mTimeStatusView == null) {
			mTimeStatusView = (TimeStatusView)mLayoutInflater.inflate(R.layout.time_fragment, null);
		}
		mFrameLayout.addView(mTimeStatusView);
		mTimeStatusView.setOnDoDumpCallBack(this);
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
		
		Log.d("xtest", "" + currentCircleViewBase);
		
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

	@Override
	protected void onResume() {
		if(currentCircleViewBase != null) {
			currentCircleViewBase.onResume();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(currentCircleViewBase != null) {
			currentCircleViewBase.onPause();
		}
		super.onPause();
	}
}
