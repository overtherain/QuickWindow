package com.cg.quickcircle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public abstract class CircleViewBase {

	OnBackButtonClickListener mOnBackButtonClickListener = null;
	public LayoutInflater mLayoutInflater;
	public Context mContext;
	public View currentview = null;

	public interface OnBackButtonClickListener {
		public void onBackButtionClick();
	}

	public View getView() {
		if (currentview != null) {
			return currentview;
		}
		currentview = createView();
		return currentview;
	}

	public void setOnBackButtonClickListener(
			OnBackButtonClickListener mOnBackButtonClickListener) {
		this.mOnBackButtonClickListener = mOnBackButtonClickListener;
	}

	public void attachContext(Context context) {
		mContext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	public boolean isContextAttached() {
		return mLayoutInflater != null;
	}

	public void goBackLauncher() {
		if (mOnBackButtonClickListener != null) {
			Log.d("xtest", "goBackLauncher");
			mOnBackButtonClickListener.onBackButtionClick();
		}
	}

	abstract public int getID();

	abstract public void onAdd();

	abstract public void onRemove();

	abstract public View createView();

	abstract public void onResume();

	abstract public void onPause();

}
