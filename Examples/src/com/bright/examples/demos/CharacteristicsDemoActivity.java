package com.bright.examples.demos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconPower;
import com.brtbeacon.sdk.ConfigBeacon;
import com.brtbeacon.sdk.connection.BeaconCharacteristics;
import com.brtbeacon.sdk.connection.BRTBeaconConnection;
import com.brtbeacon.sdk.connection.ConnectionCallback;
import com.brtbeacon.sdk.connection.WriteCallback;

/**
 * 演示连接Beacon，读写相关特征
 */
public class CharacteristicsDemoActivity extends Activity {

	private BRTBeacon			beacon;
	private BRTBeaconConnection	connection;
	private TextView			statusView;
	private TextView			beaconDetailsView;
	private EditText			intervalEditView;
	private EditText			minorEditView;
	private EditText			majorEditView;
	private EditText			powerEditView;
	private EditText			nameEditView;
	private EditText			uuidEditView;
	private TextView			txTextView;
	private View				afterConnectedView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.characteristics_demo);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		beacon = getIntent().getParcelableExtra(
				ListBeaconsActivity.EXTRAS_BEACON);
		init();
		connection = new BRTBeaconConnection(this, beacon,
				createConnectionCallback());
	}

	private void init() {
		statusView = (TextView) findViewById(R.id.status);
		beaconDetailsView = (TextView) findViewById(R.id.beacon_details);
		afterConnectedView = findViewById(R.id.after_connected);
		minorEditView = (EditText) findViewById(R.id.minor);
		majorEditView = (EditText) findViewById(R.id.major);
		powerEditView = (EditText) findViewById(R.id.power);
		nameEditView = (EditText) findViewById(R.id.name);
		intervalEditView = (EditText) findViewById(R.id.interval);
		uuidEditView = (EditText) findViewById(R.id.uuid);
		txTextView = (TextView) findViewById(R.id.dbm);

		findViewById(R.id.reset)
				.setOnClickListener(createResetButtonListener());
		findViewById(R.id.updateuuid).setOnClickListener(
				createUpdateUUIDButtonListener());
		findViewById(R.id.updateminor).setOnClickListener(
				createUpdateButtonListener());
		findViewById(R.id.updatemajor).setOnClickListener(
				createUpdateMajorButtonListener());
		findViewById(R.id.updatepower).setOnClickListener(
				createUpdatePowerButtonListener());
		findViewById(R.id.updatename).setOnClickListener(
				createUpdateNameButtonListener());
		findViewById(R.id.updateinterval).setOnClickListener(
				createUpdateIntervalButtonListener());
		findViewById(R.id.update_dbm23)
				.setOnClickListener(
						createUpdateDBMButtonListener(com.brtbeacon.sdk.BRTBeaconPower.BRTBeaconPowerLevelMinus23));
		findViewById(R.id.update_dbm6)
				.setOnClickListener(
						createUpdateDBMButtonListener(BRTBeaconPower.BRTBeaconPowerLevelMinus6));
		findViewById(R.id.update_dbm0)
				.setOnClickListener(
						createUpdateDBMButtonListener(BRTBeaconPower.BRTBeaconPowerLevelDefault));
		findViewById(R.id.update_dbm4)
				.setOnClickListener(
						createUpdateDBMButtonListener(BRTBeaconPower.BRTBeaconPowerLevelPlus4));

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!connection.isConnected()) {
			statusView.setText("Status: Connecting...");

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					connection.authenticate();
				}
			}, 3000);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connection.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener createResetButtonListener() {

		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// connection.setDefault(new WriteCallback() {
				//
				// @Override
				// public void onSuccess() {
				// runOnUiThread(new Runnable() {
				// @Override
				// public void run() {
				// showToast("setdefault successful!");
				// }
				// });
				// }
				//
				// @Override
				// public void onError() {
				// runOnUiThread(new Runnable() {
				// @Override
				// public void run() {
				// showToast("setdefault failed!");
				// }
				// });
				// }
				// });
				setCharacteristics();
			}

		};

	}

	/**
	 * 配置beacon
	 */
	private void setCharacteristics() {
		ConfigBeacon configBeacon = new ConfigBeacon();
		configBeacon.setMajor(0);
		configBeacon.setMinor(0);
		configBeacon.setIntervalMillis(350);
		configBeacon.setLed(1);
		configBeacon.setTxPower(BRTBeaconPower.BRTBeaconPowerLevelDefault);
		configBeacon.setMeasuredPower(-59);
		configBeacon.setName("BrightBeacon");
		configBeacon.setUuid("e2c56db5-dffb-48d2-b060-d0f5a71096e0");
		configBeacon.setdevolMode(0);
		connection.setBeaconCharacteristic(configBeacon, new WriteCallback() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToast("reset successful");
					}
				});

			}

			@Override
			public void onError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToast("reset failed");
					}
				});

			}
		});
	}

	private View.OnClickListener createUpdatePowerButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int power = parsePowerFromEditView();
				updatePower(power);

			}

		};
	}

	private OnClickListener createUpdateDBMButtonListener(
			final com.brtbeacon.sdk.BRTBeaconPower brtBeaconPower) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDBM(brtBeaconPower);

			}

		};

	}

	private OnClickListener createUpdateIntervalButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int power = parseIntervalFromEditView();
				updateInterval(power);

			}

		};
	}

	private OnClickListener createUpdateNameButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = parseNameFromEditView();
				updateName(name);

			}

		};
	}

	private OnClickListener createUpdateUUIDButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String uuid = parseUUIDFromEditView();
				updateUUID(uuid);

			}

		};
	}

	private View.OnClickListener createUpdateMajorButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int major = parseMajorFromEditView();
				updateMajor(major);

			}
		};
	}

	private View.OnClickListener createUpdateButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int minor = parseMinorFromEditView();
				updateMinor(minor);

			}
		};
	}

	private int parseMinorFromEditView() {
		try {
			return Integer.parseInt(String.valueOf(minorEditView.getText()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private int parseMajorFromEditView() {
		try {
			return Integer.parseInt(String.valueOf(majorEditView.getText()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private String parseNameFromEditView() {
		return nameEditView.getText().toString();
	}

	private String parseUUIDFromEditView() {

		return uuidEditView.getText().toString();

	}

	private int parseIntervalFromEditView() {
		try {
			return Integer.parseInt(String.valueOf(intervalEditView.getText()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private int parsePowerFromEditView() {
		try {
			return Integer.parseInt(String.valueOf(powerEditView.getText()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void updateMajor(int major) {

		connection.writeMajor(major,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("Major value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("Major not updated");
							}
						});
					}
				});
	}

	private void updateMinor(int minor) {

		connection.writeMinor(minor,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("Minor value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("Minor not updated");
							}
						});
					}
				});
	}

	private void updatePower(int power) {

		connection.writeMeasuredPower(power,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("power value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("power not updated");
							}
						});
					}
				});
	}

	private void updateDBM(com.brtbeacon.sdk.BRTBeaconPower dbm) {

		connection.writeTXPower(dbm,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("dbm value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("dbm not updated");
							}
						});
					}
				});
	}

	private void updateInterval(int interval) {

		connection.writeAdvertisingIntervalMillis(interval,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("dbm value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("dbm not updated");
							}
						});
					}
				});
	}

	private void updateName(String name) {

		connection.writeName(name,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("name value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("name not updated");
							}
						});
					}
				});
	}

	private void updateUUID(String uuid) {

		connection.writeUUID(uuid,
				new com.brtbeacon.sdk.connection.WriteCallback() {
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("uuid value updated");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("uuid not updated");
							}
						});
					}
				});
	}

	private com.brtbeacon.sdk.connection.ConnectionCallback createConnectionCallback() {
		return new ConnectionCallback() {

			@Override
			public void onAuthenticated(final BeaconCharacteristics beaconChars) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						statusView.setText("Status: Connected to beacon");
						StringBuilder sb = new StringBuilder()
								.append("Name: ")
								.append(beaconChars.getName())
								.append("\n")
								.append("UUID: ")
								.append(beaconChars.getUUID())
								.append("\n")
								.append("Major: ")
								.append(beaconChars.getMajor())
								.append("\n")
								.append("Minor: ")
								.append(beaconChars.getMinor())
								.append("\n")
								.append("Measured Power: ")
								.append(beaconChars.getMeasuredPower())
								.append("\n")
								.append("TX: ")
								.append(beaconChars.getTX())
								.append("\n")
								.append("LED: ")
								.append(beaconChars.getLED())
								.append("\n")
								.append("AdvertisingIntervalMillis: ")
								.append(beaconChars
										.getAdvertisingIntervalMillis());
						// .append("\n").append("Battery: ")
						// .append(beaconChars.getBattery());

						beaconDetailsView.setText(sb.toString());
						majorEditView.setText(String.valueOf(beaconChars
								.getMajor()));
						minorEditView.setText(String.valueOf(beaconChars
								.getMinor()));
						uuidEditView.setText(String.valueOf(beaconChars
								.getUUID()));
						intervalEditView.setText(String.valueOf(beaconChars
								.getAdvertisingIntervalMillis()));
						powerEditView.setText(String.valueOf(beaconChars
								.getMeasuredPower()));
						txTextView.setText(String.valueOf(beaconChars.getTX()));
						nameEditView.setText(new String(beaconChars.getName()));
						afterConnectedView.setVisibility(View.VISIBLE);
					}
				});
			}

			@Override
			public void onAuthenticationError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusView
								.setText("Status: Cannot connect to beacon. Authentication problems.");
					}
				});
			}

			@Override
			public void onDisconnected() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusView.setText("Status: Disconnected from beacon");
					}
				});
			}

			@Override
			public void onServicesDiscoveredError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusView
								.setText("Status: Could not discover services");
					}
				});
			}

		};
	}

	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
}
