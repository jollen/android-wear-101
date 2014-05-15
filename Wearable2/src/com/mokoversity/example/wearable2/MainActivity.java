package com.mokoversity.example.wearable2;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import android.preview.support.wearable.notifications.*;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;

public class MainActivity extends Activity {

	private static IntentFilter mFilterWearableReply;
	private static BroadcastReceiver mWearableReceiver;
	
	private static final String ACTION_RESPONSE = "com.mokoversity.example.wearable2.REPLY";
	
	private MainActivity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// Broadcast receiver
		mFilterWearableReply = new IntentFilter(ACTION_RESPONSE);
		mWearableReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				Toast.makeText(mContext, "Someone wants to get a coupon.", Toast.LENGTH_LONG).show();
			}
		};
		registerReceiver(mWearableReceiver, mFilterWearableReply);
		
		appTwo();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(mWearableReceiver);
		super.onStop();
	}
	
	private void appTwo() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setContentTitle("80% Discount")
			.setContentText("Now products are there...")
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bg_mountain));

		Intent intent = new Intent(ACTION_RESPONSE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);
		
		// 1. Create a big text style for the second page
		BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
		secondPageStyle.setBigContentTitle("New Product")
		               .bigText("A lot of text...");

		// 2. Create second page notification with big text style
		Notification secondPageNotification = new NotificationCompat.Builder(this)
		        .setStyle(secondPageStyle)
		        .build();
		
		Notification notification = new WearableNotifications.Builder(builder)
			.setMinPriority()
			.setHintHideIcon(true)
			// 3. Add the second page to notification
			.addPage(secondPageNotification)
			.build();
		
		NotificationManagerCompat.from(this).notify(0x01, notification);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
