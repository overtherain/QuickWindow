package com.cg.quickcircle;

import java.util.Calendar;

import com.cg.quickcircle.setting.CircleSetting;
import com.sprd.quickwindow.R;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class TimeStatusView extends RelativeLayout {
	
	public static final float DO_DUMP_VALUE = 70;
	
	TextView am_pm_view = null;
	TextView date_view = null;
	TextView week_view = null;
	TextClock time_view = null;
	ImageView phone_view = null;
	ImageView sms_view = null;
	View keyguard_status_view_face_palm = null;
	Context mContext = null;
	
	TextView battery_view = null;
	
	String lastAMPM = "";
	String lastWEEK = "";
	String lastDATE = "";
	
	float xondown = 0,yondown = 0;
	
    private static final String ACTION_UNREAD_MESSAGE_COUNT = "com.android.mms.NEW_MASSAGE_RECEVICE_COUNT";
    private static final String EXTRA_MESSAGE_COUNT = "newMessagecount";
    private static final String ACTION_MISSED_CALL_COUNT = "com.android.call.MISSED_CALL";
    private static final String EXTRA_MISSED_CALL_KEY = "EXTRA_MISSED_CALL_KEY";
	
    private DateFormatObserver mDateFormatObserver;
    
    boolean dodump = false;
    
    public static interface OnDoDump {
    	public void dodump(boolean lr);
    }
    
    int[] mouthres = new int[]{
    		R.string.january
    		,R.string.february
    		,R.string.march
    		,R.string.april
    		,R.string.may 
    		,R.string.june 
    		,R.string.july
    		,R.string.august 
    		,R.string.september 
    		,R.string.october 
    		,R.string.november
    		,R.string.december
    };
    
    int[] weekres = new int[]{
    		R.string.sunday
    		,R.string.monday
    		,R.string.tuesday
    		,R.string.wednesday
    		,R.string.thursday 
    		,R.string.friday
    		,R.string.saturday
    };
    
    int[] ampmres = new int[]{
    		R.string.morning
    		,R.string.afternoon
    };
    
    private OnDoDump mOnDoDump = null;
    
    public void setOnDoDumpCallBack(OnDoDump onDoDump) {
    	this.mOnDoDump = onDoDump;
    }
    
    private void doDumpAction(boolean lr) {
    	dodump = true;
    	Log.d("xtest", "doDumpAction");
    	if(mOnDoDump != null) {
    		mOnDoDump.dodump(lr);
    	}
    }
    
    protected void updateClock(boolean force) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int datofmonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int dayofweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
        String date = getResources().getString(mouthres[month]) + " " + datofmonth;
        String week = getResources().getString(weekres[dayofweek - 1]);
        String ampm = getResources().getString(ampmres[am_pm]);
        
        if(!lastAMPM.equals(ampm)) {
        	lastAMPM = ampm;
        	am_pm_view.setText(lastAMPM);
        }
        if(!lastDATE.equals(date)) {
        	lastDATE = date;
        	date_view.setText(lastDATE);
        }
        if(!lastWEEK.equals(week)) {
        	lastWEEK = week;
        	week_view.setText(lastWEEK);
        }
    }

	public TimeStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		oninited(context);
	}
	
	private void oninited(Context context) {
		mContext = context;
	}
	
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        mContext.registerReceiver(mIntentReceiver, filter, null, null);
        
        IntentFilter uncountfilter = new IntentFilter();
        uncountfilter.addAction(ACTION_UNREAD_MESSAGE_COUNT);
        uncountfilter.addAction(ACTION_MISSED_CALL_COUNT);
        uncountfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext. registerReceiver(mUncountReceiver, uncountfilter);

        updateClock(false);
        mDateFormatObserver = new DateFormatObserver(getHandler());
        mDateFormatObserver.startObserving();
        
        int style = mContext.getSharedPreferences("function", Context.MODE_MULTI_PROCESS).getInt("clockstyle", CircleSetting.CLOCKSTYLE_BLUE);
        switch (style) {
		case CircleSetting.CLOCKSTYLE_BLUE:
			keyguard_status_view_face_palm.setBackgroundResource(R.drawable.main_circle_backgroud_blue);
			am_pm_view.setTextColor(getResources().getColor(R.color.clock_gray));
			date_view.setTextColor(getResources().getColor(R.color.clock_gray));
			week_view.setTextColor(getResources().getColor(R.color.clock_gray));
			time_view.setTextColor(getResources().getColor(R.color.clock_gray));
			break;
        case CircleSetting.CLOCKSTYLE_CHAMPAGNE:
        	keyguard_status_view_face_palm.setBackgroundResource(R.drawable.main_circle_backgroud_champagne);
			am_pm_view.setTextColor(getResources().getColor(R.color.clock_bg));
			date_view.setTextColor(getResources().getColor(R.color.clock_bg));
			week_view.setTextColor(getResources().getColor(R.color.clock_bg));
			time_view.setTextColor(getResources().getColor(R.color.clock_bg));
			break;
        case CircleSetting.CLOCKSTYLE_WHITE:
		default:
			keyguard_status_view_face_palm.setBackgroundResource(R.drawable.main_circle_backgroud_white);
			am_pm_view.setTextColor(getResources().getColor(R.color.clock_white));
			date_view.setTextColor(getResources().getColor(R.color.clock_white));
			week_view.setTextColor(getResources().getColor(R.color.clock_white));
			time_view.setTextColor(getResources().getColor(R.color.clock_white));
			break;
		}
        
        updateUnreadMessageCount();
        updateMissingCallCount();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(mUncountReceiver);
        mContext.unregisterReceiver(mIntentReceiver);
        mDateFormatObserver.stopObserving();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dodump = false;
			xondown = event.getX();
			yondown = event.getY();
			return true;
        case MotionEvent.ACTION_MOVE:
        	if(!dodump) {
        	    float curx = event.getX();
        	    float cury =  event.getY();
        	    float res = Math.max(Math.abs(curx - xondown),Math.abs(cury - yondown));
        	    if(res > DO_DUMP_VALUE) {
        	    	doDumpAction(curx > xondown);
        	    	xondown = 0;
        	    	yondown = 0;
        	    }
        	}
        	return true;
		default:
			dodump = false;
			break;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		am_pm_view = (TextView)findViewById(R.id.am_pm_view);
		date_view = (TextView)findViewById(R.id.date_view);
		week_view = (TextView)findViewById(R.id.week_view);
		time_view = (TextClock)findViewById(R.id.time_view);
		battery_view = (TextView)findViewById(R.id.battery_view);
		phone_view = (ImageView)findViewById(R.id.phone_view);
		sms_view = (ImageView)findViewById(R.id.sms_view);
		keyguard_status_view_face_palm = findViewById(R.id.keyguard_status_view_face_palm);
	}
	
    private void updateUnreadMessageCount() {
        final Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(uri, null, "read = 0", null, null);
        // NOTE: update unread message count
        if (cursor != null) {
            int mUnreadMessageCount = cursor.getCount();
            Log.d("QuickWindowActivity", "unread message:" + mUnreadMessageCount);
            cursor.close();

            if(mUnreadMessageCount > 0) {
            	sms_view.setVisibility(View.VISIBLE);
            } else {
            	sms_view.setVisibility(View.GONE);
            }
        }
    }
    
    public void changeChargeState(int chargestate) {
    	if(BatteryManager.BATTERY_STATUS_CHARGING  == chargestate ) {
    		battery_view.setText(R.string.charging);
    		battery_view.setVisibility(View.VISIBLE);
    	} else if(BatteryManager.BATTERY_STATUS_FULL == chargestate) {
    		battery_view.setText("100%");
    		battery_view.setVisibility(View.VISIBLE);
    	} else {
    		battery_view.setVisibility(View.GONE);
    	}
    }

    private void updateMissingCallCount() {
        final Uri uri = Calls.CONTENT_URI;
        final String[] projection = new String[] {Calls.TYPE};
        final String selection = Calls.TYPE + " = " + Calls.MISSED_TYPE +
                " AND " + Calls.IS_READ + " = 0";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, null, null);
        // NOTE: update missing call count
        if (cursor != null) {
            int mMissingCallCount = cursor.getCount();
            Log.d("QuickWindowActivity", "missing call:" + mMissingCallCount);
            cursor.close();

            if(mMissingCallCount > 0){
            	phone_view.setVisibility(View.VISIBLE);  
            } else {
            	phone_view.setVisibility(View.GONE);  
            }
        }
    }
	
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)
                    || Intent.ACTION_TIME_CHANGED.equals(action)
                    || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
                    || Intent.ACTION_LOCALE_CHANGED.equals(action)) {
                updateClock(false);
            }
        }
    };
    
    private BroadcastReceiver mUncountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_UNREAD_MESSAGE_COUNT.equals(intent.getAction())) {
                // NOTE: unread message count change
                int mUnreadMessageCount = intent.getIntExtra(EXTRA_MESSAGE_COUNT, 0);
                if(mUnreadMessageCount > 0) {
                	sms_view.setVisibility(View.VISIBLE);
                } else {
                	sms_view.setVisibility(View.GONE);
                }
            } else if(ACTION_MISSED_CALL_COUNT.equals(intent.getAction())){
                // NOTE: missing call count change
                int mMissingCallCount = intent.getIntExtra(EXTRA_MISSED_CALL_KEY, 0);
                if(mMissingCallCount > 0){
                	phone_view.setVisibility(View.VISIBLE);  
                } else {
                	phone_view.setVisibility(View.GONE);  
                }
            } else if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            	int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            	changeChargeState(status);
            }
        }
    };
    
    private class DateFormatObserver extends ContentObserver {
        public DateFormatObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateClock(false);
        }

        // resolve resources leak when changing theme
        public void stopObserving() {
            final ContentResolver cr = mContext.getContentResolver();
            cr.unregisterContentObserver(this);
        }

        public void startObserving() {
            final ContentResolver cr = mContext.getContentResolver();
            cr.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.DATE_FORMAT), false, this);
        }
    }
	
}
