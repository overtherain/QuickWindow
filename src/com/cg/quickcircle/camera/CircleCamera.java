package com.cg.quickcircle.camera;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cg.quickcircle.CircleViewBase;
import com.sprd.quickwindow.R;

public class CircleCamera extends CircleViewBase {
	
	ViewGroup contioner = null;
	ViewGroup circle_camera = null;

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return R.id.main_camera_image;
	}

	@Override
	public void onAdd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemove() {
		// TODO Auto-generated method stub

	}

	@Override
	public View createView() {
		// TODO Auto-generated method stub
		
		if(contioner == null) {
			contioner = (ViewGroup)mLayoutInflater.inflate(R.layout.empty_layout, null);
		    inited(contioner);
		}
		return contioner;
	}

	private void inited(ViewGroup mcontioner) {
		// TODO Auto-generated method stub
		if(circle_camera == null) {
			circle_camera = (ViewGroup)mLayoutInflater.inflate(R.layout.circle_camera, null);
			((ImageButton)circle_camera.findViewById(R.id.back_img)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					goBackLauncher();
				}
			});
			
		}
		contioner.removeAllViews();
		contioner.addView(circle_camera);
	}


	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}
}
