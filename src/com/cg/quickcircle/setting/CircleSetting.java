package com.cg.quickcircle.setting;

import com.cg.quickcircle.CircleViewBase;
import com.sprd.quickwindow.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CircleSetting extends CircleViewBase implements OnItemClickListener,OnClickListener {
	
	public final static int CLOCKSTYLE_BLUE = 0;
	public final static int CLOCKSTYLE_CHAMPAGNE = 1;
	public final static int CLOCKSTYLE_WHITE = 2;
	
	View mCircleSettingView = null;
	View mAudioProfile = null;
	View mClockStyle = null;
	View mScreenTimeout = null;
	ViewGroup contioner = null;
	
	public static final int SETTINGFUNCTION_USRPROFILE = 1;
	public static final int SETTINGFUNCTION_CLOCKBACKGROUD = 2;
	public static final int SETTINGFUNCTION_TIMEOUT = 3;
	
	int[][] settingitem = new int[][]{
			{R.drawable.user_profile_icon,R.string.user_profile,SETTINGFUNCTION_USRPROFILE},
			{R.drawable.clock_backgroud_icon,R.string.clock_style,SETTINGFUNCTION_CLOCKBACKGROUD},
			{R.drawable.timeout_backgroud_icon,R.string.screen_timeout,SETTINGFUNCTION_TIMEOUT}
	};
	
	@Override
	public View createView() {
		// TODO Auto-generated method stub
		if(contioner == null) {
			contioner = (ViewGroup)mLayoutInflater.inflate(R.layout.empty_layout, null);
		    inited(contioner);
		}
		return contioner;
	}
	
	private void inited(View view) {
		
		contioner.removeAllViews();
		if(null == mCircleSettingView) {
			mCircleSettingView = mLayoutInflater.inflate(R.layout.setting_list_view, null);
			ListView mListView = (ListView)mCircleSettingView.findViewById(R.id.setting_list);
			ImageButton back = (ImageButton) mCircleSettingView.findViewById(R.id.back_img);
			back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					goBackLauncher();
				}
			});
			initSettingListview(mListView);
		}
		contioner.addView(mCircleSettingView);
		
	}
	
	public void initSettingListview(ListView listview) {
		listview.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if(arg1 == null) {
					arg1 = mLayoutInflater.inflate(R.layout.setting_item, null);
				}
				ImageView setting_image = (ImageView)arg1.findViewById(R.id.setting_image);
				TextView setting_name = (TextView)arg1.findViewById(R.id.setting_name);
				setting_image.setBackgroundResource(settingitem[arg0][0]);
				setting_name.setText(settingitem[arg0][1]);
				return arg1;
			}
			
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return settingitem.length;
			}
		});
		
		listview.setOnItemClickListener(this);
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
	public int getID() {
		// TODO Auto-generated method stub
		return R.id.main_setting_image;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		contioner.removeAllViews();
		switch (settingitem[arg2][2]) {
		case SETTINGFUNCTION_USRPROFILE:
			Log.d("xtest", "CircleSetting SETTINGFUNCTION_USRPROFILE onItemClick");
			
			if(mAudioProfile == null) {
				mAudioProfile = mLayoutInflater.inflate(R.layout.user_profile_view, null);
				((ImageButton)mAudioProfile.findViewById(R.id.back_img)).setOnClickListener(this);
				initUserProfile(mAudioProfile);
			}
			contioner.addView(mAudioProfile);
			
			break;
		case SETTINGFUNCTION_CLOCKBACKGROUD:
			Log.d("xtest", "CircleSetting SETTINGFUNCTION_CLOCKBACKGROUD onItemClick");
			
			if(mClockStyle == null) {
				mClockStyle = mLayoutInflater.inflate(R.layout.circle_backgroud_view, null);
				((ImageButton) mClockStyle.findViewById(R.id.back_img)).setOnClickListener(this);
				initClockBackage(mClockStyle);
			}
			contioner.addView(mClockStyle);
			break;
		case SETTINGFUNCTION_TIMEOUT:
			Log.d("xtest", "CircleSetting SETTINGFUNCTION_TIMEOUT onItemClick");
			
			if(mScreenTimeout == null) {
				mScreenTimeout = mLayoutInflater.inflate(R.layout.screen_timeout_view, null);
				((ImageButton) mScreenTimeout.findViewById(R.id.back_img)).setOnClickListener(this);
				initTimeOut(mScreenTimeout);
			}
			contioner.addView(mScreenTimeout);
			break;
		default:
			break;
		}
	}
	
	public void initUserProfile(View view) {
		ContentResolver resolver = mContext.getContentResolver();
		int originSelectId = Settings.System.getInt(resolver, "currentAudioProfileId", -1);
		
		final RadioButton user_profile_general_box = (RadioButton) view.findViewById(R.id.user_profile_general_box);
		final RadioButton user_profile_silent_box = (RadioButton) view.findViewById(R.id.user_profile_silent_box);
		final RadioButton user_profile_meeting_box = (RadioButton) view.findViewById(R.id.user_profile_meeting_box);
		final RadioButton user_profile_outdoor_box = (RadioButton) view.findViewById(R.id.user_profile_outdoor_box);
		
		user_profile_general_box.setChecked(false);
		user_profile_silent_box.setChecked(false);
		user_profile_meeting_box.setChecked(false);
		user_profile_outdoor_box.setChecked(false);
		
		switch (originSelectId) {
		case 1:
			user_profile_general_box.setChecked(true);
			break;
		case 2:
			user_profile_silent_box.setChecked(true);
			break;
		case 3:
			user_profile_meeting_box.setChecked(true);
			break;
		case 4:
			user_profile_outdoor_box.setChecked(true);
			break;
		default:
			break;
		}
		
		user_profile_general_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 1);
					user_profile_silent_box.setChecked(false);
					user_profile_meeting_box.setChecked(false);
					user_profile_outdoor_box.setChecked(false);
					mContext.sendBroadcast(new Intent("android.cg.RINGER_MODE_CHANGED"));
				}
			}
		});
		
		user_profile_silent_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 2);
					user_profile_general_box.setChecked(false);
					user_profile_meeting_box.setChecked(false);
					user_profile_outdoor_box.setChecked(false);
					mContext.sendBroadcast(new Intent("android.cg.RINGER_MODE_CHANGED"));
				}
			}
		});
		
		user_profile_meeting_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 3);
					user_profile_general_box.setChecked(false);
					user_profile_silent_box.setChecked(false);
					user_profile_outdoor_box.setChecked(false);
					mContext.sendBroadcast(new Intent("android.cg.RINGER_MODE_CHANGED"));
				}
			}
		});
		
		user_profile_outdoor_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 4);
					user_profile_general_box.setChecked(false);
					user_profile_silent_box.setChecked(false);
					user_profile_meeting_box.setChecked(false);
					mContext.sendBroadcast(new Intent("android.cg.RINGER_MODE_CHANGED"));
				}
			}
		});
		
	}
	
	public void initClockBackage(View view) {
		
		final RadioButton blue_box = (RadioButton)view.findViewById(R.id.blue_box);
		final RadioButton champagne_box = (RadioButton)view.findViewById(R.id.champagne_box);
		final RadioButton white_box = (RadioButton)view.findViewById(R.id.white_box);
		blue_box.setChecked(false);
		champagne_box.setChecked(false);
		white_box.setChecked(false);
		
		int style = mContext.getSharedPreferences("function", Context.MODE_MULTI_PROCESS).getInt("clockstyle", CLOCKSTYLE_BLUE);
		
		switch (style) {
		case CLOCKSTYLE_BLUE:
			blue_box.setChecked(true);
			break;
		case CLOCKSTYLE_CHAMPAGNE:
			champagne_box.setChecked(true);
			break;
		case CLOCKSTYLE_WHITE:
			white_box.setChecked(true);
			break;
		default:
			break;
		}
		
		blue_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					mContext.getSharedPreferences("function", Context.MODE_MULTI_PROCESS).edit().putInt("clockstyle", CLOCKSTYLE_BLUE).commit();
					champagne_box.setChecked(false);
					white_box.setChecked(false);
				}
			}
		});
		
		champagne_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					mContext.getSharedPreferences("function", Context.MODE_MULTI_PROCESS).edit().putInt("clockstyle", CLOCKSTYLE_CHAMPAGNE).commit();
					blue_box.setChecked(false);
					white_box.setChecked(false);
				}
			}
		});
		
		white_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1) {
					mContext.getSharedPreferences("function", Context.MODE_MULTI_PROCESS).edit().putInt("clockstyle", CLOCKSTYLE_WHITE).commit();
					champagne_box.setChecked(false);
					blue_box.setChecked(false);
				}
			}
		});
		
	}
	
	public void initTimeOut(View view) {
		ContentResolver resolver = mContext.getContentResolver();
		long currentTimeout = Settings.System.getLong(resolver, android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 30000);
		CharSequence[] values =  mContext.getResources().getTextArray(R.array.screen_timeout_values);
		int best = 0;
        for (int i = 0; i < values.length; i++) {
            long timeout = Long.parseLong(values[i].toString());
            if (currentTimeout >= timeout) {
                best = i;
            }
        }
		RadioGroup mRadioGroup = (RadioGroup)view.findViewById(R.id.screen_timeout_radiogroup);
		
	    CharSequence[] entries =  mContext.getResources().getTextArray(R.array.screen_timeout_entries);
	    
	    for (int i = 0; i < entries.length; i++) {
			RadioButton tmp = new RadioButton(mContext);
			tmp.setText(entries[i]);
			Log.d("xtest", "" + entries[i]);
			tmp.setTextColor(mContext.getResources().getColor(R.color.black));
			tmp.setPadding(10, 0, 0, 0);
			tmp.setId(i);
			mRadioGroup.addView(tmp, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
			if(best == i) {
				tmp.setChecked(true);
			}
		}
	    mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				
				CharSequence[] entries =  mContext.getResources().getTextArray(R.array.screen_timeout_entries);
				CharSequence[] values =  mContext.getResources().getTextArray(R.array.screen_timeout_values);
				int value = Integer.parseInt((String) values[arg0.getCheckedRadioButtonId()]);
				
	            try {
	                Settings.System.putInt(mContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, value);
	            } catch (NumberFormatException e) {
	                Log.e("xtest", "could not persist screen timeout setting", e);
	            }
			}
		});
	    
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		contioner.removeAllViews();
		contioner.addView(mCircleSettingView);
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
