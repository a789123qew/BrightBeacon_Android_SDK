package com.bright.examples.demos;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTBeaconPower;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.ConfigBeacon;
import com.brtbeacon.sdk.RangingListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.brtbeacon.sdk.connection.BeaconCharacteristics;
import com.brtbeacon.sdk.connection.BRTBeaconConnection;
import com.brtbeacon.sdk.connection.ConnectionCallback;
import com.brtbeacon.sdk.connection.WriteCallback;
import com.brtbeacon.sdk.service.RangingResult;

/**
 * 演示批量修改周边Beacon
 */
public class ConfigDemoActivity extends Activity implements OnClickListener {
	private TextView			alertTextView;
	private EditText			uuidEditText;
	private EditText			measuredpowerEditText;
	private EditText			txpowerEditText;
	private EditText			advertisingEditText;
	private Button				startButton;
	private Switch				majorSwitch;
	private Switch				minorSwitch;
	private BRTBeaconManager	beaconManager;
	private BRTBeaconConnection	beaconConnection;
	private BRTRegion			brtRegion	= new BRTRegion("rid", null, null,
													null);
	private String				uuid;
	private String				measuredpower;
	private String				txpower;
	private String				advertising;
	private boolean				isConfig	= false;
	private boolean				isConn		= false;
	private boolean				major		= true;
	private boolean				minor		= true;
	private boolean				isRun		= false;
	private int					index		= 0;
	private List<BRTBeacon>		beacons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_demo);

		alertTextView = (TextView) findViewById(R.id.config_alert);
		uuidEditText = (EditText) findViewById(R.id.config_uuid);
		measuredpowerEditText = (EditText) findViewById(R.id.config_measured_power);
		txpowerEditText = (EditText) findViewById(R.id.config_tx_power);
		advertisingEditText = (EditText) findViewById(R.id.config_advertising);
		startButton = (Button) findViewById(R.id.config_start);
		startButton.setOnClickListener(this);
		majorSwitch = (Switch) findViewById(R.id.config_major);
		majorSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				major = isChecked;
			}
		});
		minorSwitch = (Switch) findViewById(R.id.config_minor);
		minorSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				minor = isChecked;
			}
		});
		beaconManager = new BRTBeaconManager(this);
		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(final RangingResult rangingResult) {
				alertTextView.setText("发现周边的Beacon数量："
						+ rangingResult.beacons.size());

				if (isRun && !isConfig) {
					try {
						beaconManager.stopRanging(brtRegion);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					beaconManager.disconnect();
					config(rangingResult.beacons);
					isConfig = true;

				}

			}

		});
	}

	private Handler	handler	= new Handler() {
								public void handleMessage(android.os.Message msg) {
									config(beacons);
								};
							};

	/**
	 * 配置Beacon
	 * 
	 * @param beacons
	 */
	private void config(List<BRTBeacon> beacons) {
		this.beacons = beacons;

		if (!isConn && index < beacons.size()) {
			if (beacons.get(index).getMajor() == 0
					&& beacons.get(index).getMinor() == 0) {
				alertTextView.setText("当前正在操作的Beacon名字:"
						+ beacons.get(index).getName());
				beaconConnection = new BRTBeaconConnection(this,
						beacons.get(index), new beaconConnectionCallback());
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						beaconConnection.authenticate();
					}
				}, 1500);
				isConn = true;
				index++;
			}

		}
	}

	class beaconConnectionCallback implements ConnectionCallback {

		@Override
		public void onAuthenticated(
				BeaconCharacteristics paramBeaconCharacteristics) {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(ConfigDemoActivity.this, "onAuthenticated",
							Toast.LENGTH_SHORT).show();
				}
			});

			ConfigBeacon configBeacon = new ConfigBeacon();
			configBeacon.setUuid(uuid);
			configBeacon.setMajor(index);
			configBeacon.setMinor(index);
			configBeacon.setIntervalMillis(Integer.parseInt(advertising));
			configBeacon.setMeasuredPower(Integer.parseInt(measuredpower));
			configBeacon.setTxPower(BRTBeaconPower.BRTBeaconPowerLevelDefault);
			beaconConnection.setBeaconCharacteristic(configBeacon,
					new WriteCallback() {

						@Override
						public void onSuccess() {
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(ConfigDemoActivity.this,
											"successful", Toast.LENGTH_SHORT)
											.show();
								}
							});
							isConn = false;
							beaconConnection.close();
							handler.sendEmptyMessage(1);
						}

						@Override
						public void onError() {
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(ConfigDemoActivity.this,
											"failed", Toast.LENGTH_SHORT)
											.show();
								}
							});
							isConn = false;
							beaconConnection.close();
							handler.sendEmptyMessage(1);
						}
					});
		}

		@Override
		public void onAuthenticationError() {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(ConfigDemoActivity.this, "AuthenticationError",
							Toast.LENGTH_SHORT).show();
				}
			});
			isConn = false;
			beaconConnection.close();
			handler.sendEmptyMessage(1);
		}

		@Override
		public void onDisconnected() {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(ConfigDemoActivity.this, "Disconnected",
							Toast.LENGTH_SHORT).show();
				}
			});
			isConn = false;
			beaconConnection.close();
			handler.sendEmptyMessage(1);
		}

		@Override
		public void onServicesDiscoveredError() {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(ConfigDemoActivity.this,
							"ServicesDiscoveredError", Toast.LENGTH_SHORT)
							.show();
				}
			});
			isConn = false;
			beaconConnection.close();
			handler.sendEmptyMessage(1);
		}

	}

	@Override
	protected void onResume() {
		beaconManager.connect(new ServiceReadyCallback() {

			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(brtRegion);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		super.onResume();
	}

	@Override
	protected void onStop() {
		// try {
		// beaconManager.stopRanging(brtRegion);
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		beaconManager.disconnect();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config_start:
			uuid = uuidEditText.getText().toString();
			measuredpower = measuredpowerEditText.getText().toString();
			txpower = txpowerEditText.getText().toString();
			advertising = advertisingEditText.getText().toString();
			isRun = true;

			break;

		default:
			break;
		}
	}
}
