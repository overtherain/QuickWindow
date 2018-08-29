package com.cg.quickcircle.mmssms;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.cg.quickcircle.CircleViewBase;
import com.sprd.quickwindow.R;

public class CircleSMS extends CircleViewBase {
	
	ViewGroup contioner = null;
	ViewGroup smscheckview = null;

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return R.id.main_sms_image;
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
		if(smscheckview == null) {
			smscheckview = (ViewGroup)mLayoutInflater.inflate(R.layout.sms_check_view, null);
			((ImageButton)smscheckview.findViewById(R.id.back_img)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					goBackLauncher();
				}
			});
		}
		mcontioner.removeAllViews();
		mcontioner.addView(smscheckview);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

}
