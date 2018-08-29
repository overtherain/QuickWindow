package com.cg.quickcircle.music;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cg.quickcircle.CircleViewBase;
import com.sprd.quickwindow.R;

//add begin
import android.media.AudioManager;
import android.media.RemoteController;
import android.media.RemoteController.MetadataEditor;
import android.media.RemoteController.OnClientUpdateListener;

//add end



public class CircleMusic extends CircleViewBase implements OnBackClick{

	ViewGroup contioner = null;
	MusicControlView mMusicControlView = null; 
	
	//add begin
	public static String TAG = "CircleMusic";
	public static boolean mStartMusicService = true;
	
	
	//add end
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return R.id.main_music_image;
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

	private void inited(View mcontioner) {
		// TODO Auto-generated method stub
		Log.d("xtest", "" + this + " inited");
		if(mMusicControlView == null) {
			mMusicControlView = (MusicControlView)mLayoutInflater.inflate(R.layout.music_control_view, null);
			mMusicControlView.setOnBackClickListener(this);
		}
		contioner.removeAllViews();
		contioner.addView(mMusicControlView);
	}

	@Override
	public void onBackClick() {
		// TODO Auto-generated method stub
		goBackLauncher();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	//music data set
	public class Metadata {
		private String albumTitle;
		private String artist;
		private Bitmap bitmap;
		private long duration;
		private String trackTitle;

		Metadata() {
		}

		public void clear() {
			artist = null;
			trackTitle = null;
			albumTitle = null;
			bitmap = null;
			duration = -1L;
		}

		public String toString() {
			return "Metadata[artist=" + artist + " trackTitle="
					+ trackTitle + " albumTitle=" + albumTitle
					+ " duration=" + duration + "]";
		}
	}

	//add music data
	private void populateMetadata() {
		
	}

	//update music data
	public void updateMetadata(RemoteController.MetadataEditor mMetadataEditor) {
	
	}

	void cancelResetToMetadata() {
		removeCallbacks(mResetToMetadata);
	}

	void clearMetadata() {
		mPopulateMetadataWhenAttached = null;
		mMetadata.clear();
		populateMetadata();
	}

	void delayResetToMetadata() {
		removeCallbacks(mResetToMetadata);
		postDelayed(mResetToMetadata, 5000L);
	}

	private boolean isAttachedToWindow() {
		// TODO Auto-generated method stub
		return false;
	};
	
	
	//button click
	public RemoteController.OnClientUpdateListener mRCCUpdateListener = new RemoteController.OnClientUpdateListener() {
		
		public void onClientChange(boolean paramAnonymousBoolean) {
			if (paramAnonymousBoolean)
				clearMetadata();
			MusicControlView.(this, false);
		}

		public void onClientMetadataUpdate(
				RemoteController.MetadataEditor paramAnonymousMetadataEditor) {
			updateMetadata(paramAnonymousMetadataEditor);
			MusicControlView(this, false);
		}

		public void onClientPlaybackStateUpdate(int paramAnonymousInt) {
			updatePlayPauseState(paramAnonymousInt);
			MusicControlView(this, false);
		}

		public void onClientPlaybackStateUpdate(int paramAnonymousInt,
				long paramAnonymousLong1, long paramAnonymousLong2,
				float paramAnonymousFloat) {
			updatePlayPauseState(paramAnonymousInt);
			MusicControlView(this, false);
		}

		public void onClientTransportControlUpdate(int paramAnonymousInt) {
			MusicControlView(this, false);
		}
	};
	
	
	public static void sendMediaButtonClick(int paramInt) {
		mRemoteController.sendMediaKeyEvent(new KeyEvent(0, paramInt));
		mRemoteController.sendMediaKeyEvent(new KeyEvent(1, paramInt));
	}
	
	
	private void updatePlayPauseState(int paramInt) {

	}
	
	

}
