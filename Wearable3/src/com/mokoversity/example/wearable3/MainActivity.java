package com.mokoversity.example.wearable3;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.preview.support.wearable.notifications.*;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;

public class MainActivity extends Activity {

	private static IntentFilter mFilterWearableReply;
	private static BroadcastReceiver mWearableReceiver;
	
	private static final String ACTION_RESPONSE = "com.mokoversity.example.wearable3.REPLY";
	
	private MainActivity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		appThree();
	}

	private void appThree() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setContentTitle("Coupon")
			.setContentText("Limited. 30 mins from now !")
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bg_mountain));

		Intent intent = new Intent(ACTION_RESPONSE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);
		
		Notification notification = new WearableNotifications.Builder(builder)
			.setMinPriority()
			.setHintHideIcon(true)
			.addRemoteInputForContentIntent(
				new RemoteInput.Builder("GET !")
				.setLabel("Reply")
				.setChoices(getResources().getStringArray(R.array.reply_choices))
				.build())
			.build();
		
		NotificationManagerCompat.from(this).notify(0x02, notification);
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
