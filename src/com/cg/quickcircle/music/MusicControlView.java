package com.cg.quickcircle.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.RemoteController;
import android.media.RemoteController.MetadataEditor;
import android.media.RemoteController.OnClientUpdateListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicControlView extends FrameLayout {
	private static final boolean ANIMATE_TRANSITIONS = false;
	protected static final boolean DEBUG = false;
	private static final int DISPLAY_TIMEOUT_MS = 5000;
	private static final int RESET_TO_METADATA_DELAY = 5000;
	protected static final String TAG = "TransportControlView";
	private AudioManager mAudioManager;
	private ImageView mBackLauncherCircle;
	private ImageView mBtnNext;
	private ImageView mBtnPlay;
	private ImageView mBtnPrev;
	private Context mContext;
	private int mCurrentPlayState;
	private ViewGroup mInfoContainer;
	private Metadata mMetadata = new Metadata();
	
	private RemoteController.MetadataEditor mPopulateMetadataWhenAttached = null;
	private RemoteController.OnClientUpdateListener mRCClientUpdateListener = new RemoteController.OnClientUpdateListener() {
		public void onClientChange(boolean paramAnonymousBoolean) {
			if (paramAnonymousBoolean)
				MusicControlView.this.clearMetadata();
			MusicControlView.access$002(MusicControlView.this, false);
		}

		public void onClientMetadataUpdate(
				RemoteController.MetadataEditor paramAnonymousMetadataEditor) {
			MusicControlView.this.updateMetadata(paramAnonymousMetadataEditor);
			MusicControlView.access$002(MusicControlView.this, false);
		}

		public void onClientPlaybackStateUpdate(int paramAnonymousInt) {
			MusicControlView.this.updatePlayPauseState(paramAnonymousInt);
			MusicControlView.access$002(MusicControlView.this, false);
		}

		public void onClientPlaybackStateUpdate(int paramAnonymousInt,
				long paramAnonymousLong1, long paramAnonymousLong2,
				float paramAnonymousFloat) {
			MusicControlView.this.updatePlayPauseState(paramAnonymousInt);
			MusicControlView.access$002(MusicControlView.this, false);
		}

		public void onClientTransportControlUpdate(int paramAnonymousInt) {
			MusicControlView.access$002(MusicControlView.this, false);
		}
	};
	private RemoteController mRemoteController;
	private final Runnable mResetToMetadata = new Runnable() {
		public void run() {
			MusicControlView.this.resetToMetadata();
		}
	};
	private boolean mStartMusicService = true;
	private TextView mTrackTitle;
	
	private final View.OnClickListener mTransportCommandListener = new View.OnClickListener() {
		public void onClick(View paramAnonymousView) {
			int i = -1;
			if (paramAnonymousView == MusicControlView.this.mBtnPrev)
				i = 88;
			while (true) {
				if (i != -1)
					MusicControlView.this.sendMediaButtonClick(i);
				return;
				if (paramAnonymousView == MusicControlView.this.mBtnNext) {
					i = 87;
				} else if (paramAnonymousView == MusicControlView.this.mBtnPlay) {
					i = 85;
					if (MusicControlView.this.mStartMusicService) {
						Intent localIntent = new Intent("test.music");
						MusicControlView.this.mContext
								.sendBroadcast(localIntent);
					}
				} else if (paramAnonymousView == MusicControlView.this.mBackLauncherCircle) {
					Circle.getUiHandler().sendEmptyMessage(0);
				}
			}
		}
	};

	@SuppressLint({ "NewApi" })
	public MusicControlView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;
		this.mAudioManager = ((AudioManager) paramContext
				.getSystemService("audio"));
		this.mCurrentPlayState = 9;
		this.mRemoteController = new RemoteController(paramContext,
				this.mRCClientUpdateListener);
		DisplayMetrics localDisplayMetrics = paramContext.getResources()
				.getDisplayMetrics();
		int i = Math.max(localDisplayMetrics.widthPixels,
				localDisplayMetrics.heightPixels);
		this.mRemoteController.setArtworkConfiguration(i, i);
		this.mAudioManager.registerRemoteController(this.mRemoteController);
	}

	@SuppressLint({ "NewApi" })
	private void populateMetadata() {
		DisplayMetrics localDisplayMetrics = getContext().getResources()
				.getDisplayMetrics();
		Bitmap localBitmap1 = this.mMetadata.bitmap;
		Bitmap localBitmap2 = null;
		if (localBitmap1 != null)
			localBitmap2 = QuickCircleUtils.drawCirclePicture(this.mContext,
					this, localDisplayMetrics.widthPixels,
					localDisplayMetrics.heightPixels, this.mMetadata.bitmap);
		if (localBitmap2 != null)
			setBackground(new BitmapDrawable(localBitmap2));
		while (true) {
			StringBuilder localStringBuilder = new StringBuilder();
			if (!TextUtils.isEmpty(this.mMetadata.artist)) {
				if (localStringBuilder.length() != 0)
					localStringBuilder.append(" - ");
				localStringBuilder.append(this.mMetadata.artist);
			}
			if (!TextUtils.isEmpty(this.mMetadata.albumTitle)) {
				if (localStringBuilder.length() != 0)
					localStringBuilder.append(" - ");
				localStringBuilder.append(this.mMetadata.albumTitle);
			}
			if (!TextUtils.isEmpty(this.mMetadata.trackTitle))
				localStringBuilder.append(" <" + this.mMetadata.trackTitle
						+ ">");
			this.mTrackTitle.setText(localStringBuilder.toString());
			updatePlayPauseState(this.mCurrentPlayState);
			return;
			setBackgroundResource(2130837556);
		}
	}

	@SuppressLint({ "NewApi" })
	private void sendMediaButtonClick(int paramInt) {
		this.mRemoteController.sendMediaKeyEvent(new KeyEvent(0, paramInt));
		this.mRemoteController.sendMediaKeyEvent(new KeyEvent(1, paramInt));
	}

	private static void setVisibilityBasedOnFlag(View paramView, int paramInt1,
			int paramInt2) {
		if ((paramInt1 & paramInt2) != 0) {
			paramView.setVisibility(0);
			return;
		}
		paramView.setVisibility(4);
	}

	@SuppressLint({ "NewApi" })
	private void updatePlayPauseState(int paramInt) {
		if (paramInt == this.mCurrentPlayState)
			return;
		int i;
		int j;
		switch (paramInt) {
		default:
			i = 2130837537;
			j = 2131099668;
		case 9:
		case 3:
		case 8:
		}
		while (true) {
			this.mBtnPlay.setBackgroundResource(i);
			this.mBtnPlay.setContentDescription(getResources().getString(j));
			this.mCurrentPlayState = paramInt;
			return;
			i = 2130837588;
			j = 2131099668;
			continue;
			i = 2130837536;
			j = 2131099667;
			continue;
			i = 2130837589;
			j = 2131099669;
		}
	}

	void cancelResetToMetadata() {
		removeCallbacks(this.mResetToMetadata);
	}

	void clearMetadata() {
		this.mPopulateMetadataWhenAttached = null;
		this.mMetadata.clear();
		populateMetadata();
	}

	void delayResetToMetadata() {
		removeCallbacks(this.mResetToMetadata);
		postDelayed(this.mResetToMetadata, 5000L);
	}

	@SuppressLint({ "NewApi" })
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (this.mPopulateMetadataWhenAttached != null) {
			updateMetadata(this.mPopulateMetadataWhenAttached);
			this.mPopulateMetadataWhenAttached = null;
		}
		this.mAudioManager.registerRemoteController(this.mRemoteController);
	}

	@SuppressLint({ "NewApi" })
	protected void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
		DisplayMetrics localDisplayMetrics = getContext().getResources()
				.getDisplayMetrics();
		int i = Math.max(localDisplayMetrics.widthPixels,
				localDisplayMetrics.heightPixels);
		this.mRemoteController.setArtworkConfiguration(i, i);
	}

	@SuppressLint({ "NewApi" })
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.mAudioManager.unregisterRemoteController(this.mRemoteController);
	}

	public void onFinishInflate() {
		super.onFinishInflate();
		this.mInfoContainer = ((ViewGroup) findViewById(2131361829));
		this.mTrackTitle = ((TextView) findViewById(2131361830));
		this.mBtnPrev = ((ImageView) findViewById(2131361831));
		this.mBtnPlay = ((ImageView) findViewById(2131361832));
		this.mBtnNext = ((ImageView) findViewById(2131361833));
		this.mBackLauncherCircle = ((ImageView) findViewById(2131361809));
		View[] arrayOfView = new View[4];
		arrayOfView[0] = this.mBtnPrev;
		arrayOfView[1] = this.mBtnPlay;
		arrayOfView[2] = this.mBtnNext;
		arrayOfView[3] = this.mBackLauncherCircle;
		int i = arrayOfView.length;
		for (int j = 0; j < i; j++)
			arrayOfView[j].setOnClickListener(this.mTransportCommandListener);
	}

	void resetToMetadata() {
	}

	@SuppressLint({ "NewApi" })
	void scrubTo(int paramInt) {
		this.mRemoteController.seekTo(paramInt);
	}

	@SuppressLint({ "NewApi" })
	void updateMetadata(RemoteController.MetadataEditor paramMetadataEditor) {
		if (isAttachedToWindow()) {
			Metadata.access$802(this.mMetadata,
					paramMetadataEditor.getString(13, this.mMetadata.artist));
			Metadata.access$902(this.mMetadata,
					paramMetadataEditor.getString(7, this.mMetadata.trackTitle));
			Metadata.access$1002(this.mMetadata,
					paramMetadataEditor.getString(1, this.mMetadata.albumTitle));
			Metadata.access$1102(this.mMetadata,
					paramMetadataEditor.getLong(9, -1L));
			Metadata.access$1202(this.mMetadata,
					paramMetadataEditor.getBitmap(100, this.mMetadata.bitmap));
			populateMetadata();
		}
		while (true) {
			this.mPopulateMetadataWhenAttached = paramMetadataEditor;
			return;
			this.mPopulateMetadataWhenAttached = paramMetadataEditor;
		}
	}

	class Metadata {
		private String albumTitle;
		private String artist;
		private Bitmap bitmap;
		private long duration;
		private String trackTitle;

		Metadata() {
		}

		public void clear() {
			this.artist = null;
			this.trackTitle = null;
			this.albumTitle = null;
			this.bitmap = null;
			this.duration = -1L;
		}

		public String toString() {
			return "Metadata[artist=" + this.artist + " trackTitle="
					+ this.trackTitle + " albumTitle=" + this.albumTitle
					+ " duration=" + this.duration + "]";
		}
	}
}