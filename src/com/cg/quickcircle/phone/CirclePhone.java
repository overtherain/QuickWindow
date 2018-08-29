package com.cg.quickcircle.phone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cg.quickcircle.CircleViewBase;
import com.sprd.quickwindow.R;

public class CirclePhone extends CircleViewBase {
	
	ViewGroup contioner = null;
	ViewGroup calllogview = null;
	
	private final String TAG = "CirclePhone";

	MyAsyncQueryHandler asyncQuery;
	ListView lv;
	TextView tv;
	CallLogAdapter adapter = new CallLogAdapter();
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return R.id.main_phone_image;
	}

	@Override
	public void onAdd() {
		// TODO Auto-generated method stub
		asyncQuery = new MyAsyncQueryHandler(mContext.getContentResolver());
		
		Uri uri = CallLog.Calls.CONTENT_URI;

		String[] projection = { CallLog.Calls.DATE, CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME,
				CallLog.Calls._ID };

		asyncQuery.startQuery(0, null, uri, projection, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
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

	private void inited(View view) {
		// TODO Auto-generated method stub
		contioner.removeAllViews();
		
		if(calllogview == null) {
			calllogview = (ViewGroup)mLayoutInflater.inflate(R.layout.constant_list_view, null);
			tv = (TextView)calllogview.findViewById(R.id.no_call_log_tip_text);
			lv = (ListView)calllogview.findViewById(R.id.contant_list);
			((ImageButton)calllogview.findViewById(R.id.back_img)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					goBackLauncher();
				}
			});
		}
		Log.d(TAG,"----------"+adapter.getCount());
		lv.setAdapter(adapter);
		
		contioner.addView(calllogview);
	}
	
	class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			Log.d(TAG,"Query Start");
			if (cursor != null && cursor.getCount() > 0) {
				ArrayList<CallLogBean> tmplist = new ArrayList<CallLogBean>();
				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd HH:mm");
				Date date;
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					date = new Date(cursor.getLong(cursor
							.getColumnIndex(CallLog.Calls.DATE)));
					// String date =
					// cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
					String number = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.NUMBER));
					int type = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls.TYPE));
					String cachedName = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.CACHED_NAME));
					int id = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls._ID));

					CallLogBean clb = new CallLogBean(sfd.format(date),number,cachedName);
					
					tmplist.add(clb);
				}
				Log.d(TAG,"Query Done --" + tmplist.size());
				if(tmplist.size()>0){
					Log.d(TAG,"change visibility");
					tv.setVisibility(View.INVISIBLE);
					lv.setVisibility(View.VISIBLE);
				}
				
				adapter.updateList(tmplist);
				adapter.notifyDataSetChanged();
			}
			
		}
		
	}

	class CallLogBean{
		String date,number,name;
		CallLogBean(String date,String number,String name){
			this.date = date;
			this.number = number;
			this.name = name;
			if (null == name || "".equals(name)) {
				this.name = number;
			}
		}
	}
	
	public class CallLogAdapter extends BaseAdapter {
		ArrayList<CallLogBean> list = new ArrayList<CallLogBean>();
		
		void updateList(ArrayList<CallLogBean> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d(TAG,"***" + list.size());
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg1 == null) {
				arg1 = mLayoutInflater.inflate(R.layout.contant_item, null);
			}
			TextView name = (TextView) arg1.findViewById(R.id.phone_name);
			TextView date = (TextView) arg1.findViewById(R.id.date);
			TextView number = (TextView) arg1.findViewById(R.id.phone_number);
			ImageView imgBtn = (ImageView) arg1.findViewById(R.id.callling_image);
			final CallLogBean clb = (CallLogBean) list.get(arg0);
			name.setText(clb.name);
			date.setText(clb.date);
			number.setText(clb.number);
			Log.d(TAG,"name" + clb.name + "date" + clb.date +  "number" + clb.number);
			
			imgBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    intent.setAction(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:" + clb.number));
						
					    mContext.startActivity(intent);
				}});
			
			return arg1;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

}
