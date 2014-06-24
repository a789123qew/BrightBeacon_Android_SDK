package com.bright.examples.demos;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.widget.TextView;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.MonitoringListener;
import com.brtbeacon.sdk.ServiceReadyCallback;

/**
 * Demo that shows how to use region monitoring. Two important steps are:
 * <ul>
 * <li>start monitoring region, in example in {@link #onResume()}</li>
 * <li>respond to monitoring changes by registering {@link MonitoringListener}
 * in {@link BeaconManager}</li>
 * </ul>
 * 
 * @author
 */
public class NotifyDemoActivity extends Activity {

	private static final String	TAG				= NotifyDemoActivity.class
														.getSimpleName();
	private static final int	NOTIFICATION_ID	= 123;

	private BRTBeaconManager	beaconManager;
	private NotificationManager	notificationManager;
	private BRTRegion			region;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_demo);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		BRTBeacon beacon = getIntent().getParcelableExtra(
				ListBeaconsActivity.EXTRAS_BEACON);
		region = new BRTRegion("regionId", beacon.getProximityUUID(),
				beacon.getMajor(), beacon.getMinor());
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		beaconManager = new BRTBeaconManager(this);

		// Default values are 5s of scanning and 25s of waiting time to save CPU
		// cycles.
		// In order for this demo to be more responsive and immediate we lower
		// down those values.
		beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onEnteredRegion(BRTRegion arg0, List<BRTBeacon> arg1) {
				// TODO Auto-generated method stub
				postNotification("Entered region");
			}

			@Override
			public void onExitedRegion(BRTRegion arg0) {
				// TODO Auto-generated method stub
				postNotification("Exited region");
			}

		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		notificationManager.cancel(NOTIFICATION_ID);
		beaconManager.connect(new ServiceReadyCallback() {
			@Override
			public void onServiceReady() {

				try {
					beaconManager.startMonitoring(region);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		notificationManager.cancel(NOTIFICATION_ID);
		beaconManager.disconnect();
		super.onDestroy();
	}

	private void postNotification(String msg) {
		Intent notifyIntent = new Intent(NotifyDemoActivity.this,
				NotifyDemoActivity.class);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivities(
				NotifyDemoActivity.this, 0, new Intent[] { notifyIntent },
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(
				NotifyDemoActivity.this).setSmallIcon(R.drawable.beacon_gray)
				.setContentTitle("Notify Demo").setContentText(msg)
				.setAutoCancel(true).setContentIntent(pendingIntent).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notificationManager.notify(NOTIFICATION_ID, notification);

		TextView statusTextView = (TextView) findViewById(R.id.status);
		statusTextView.setText(msg);
	}
}
