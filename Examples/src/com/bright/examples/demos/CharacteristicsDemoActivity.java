package com.bright.examples.demos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconPower;
import com.brtbeacon.sdk.ConfigBeacon;
import com.brtbeacon.sdk.connection.BRTBeaconConnection;
import com.brtbeacon.sdk.connection.BeaconCharacteristics;
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
	private Switch				modeSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.characteristics_demo);
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
		modeSwitch = (Switch) findViewById(R.id.mode);
		modeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int state = 0;
				if (isChecked) {
					state = 1;
				} else {
					state = 0;
				}
				connection.writeDevolMode(state, new WriteCallback() {

					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新开发模式成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新开发模式失败");
							}
						});
					}
				});
			}
		});
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

		findViewById(R.id.btn_temperature).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (connection.isConnected()) {
							connection.getTemperature();
						}
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!connection.isConnected()) {
			statusView.setText("状态: 连接中...");

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					connection.connect();
				}
			}, 3000);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			connection.close();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connection.close();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		connection.close();
		super.onStop();
	}

	private OnClickListener createResetButtonListener() {

		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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
						showToast("重置设备成功");
					}
				});

			}

			@Override
			public void onError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToast("重置设备失败");
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
								showToast("更新主标识成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新主标识失败");
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
								showToast("更新副标识成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新副标识失败");
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
								showToast("更新测量功率成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新测量功率失败");
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
								showToast("更新发射功率成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新发射功率失败");
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
								showToast("更新发射频率成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新发射频率失败");
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
								showToast("更新名称成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新名称失败");
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
								showToast("更新UUID成功");
							}
						});
					}

					@Override
					public void onError() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showToast("更新UUID失败");
							}
						});
					}
				});
	}

	/**
	 * 匹配发射功率
	 * 
	 * @param tx
	 * @return
	 */
	private String matchtxvalue(String tx) {
		int txvalues = Integer.parseInt(tx);
		String txvalue = "BRTBeaconPowerLevelDefault";
		switch (txvalues) {
		case 0:
			txvalue = "BRTBeaconPowerLevelMinus23";
			break;
		case 1:
			txvalue = "BRTBeaconPowerLevelMinus6";
			break;
		case 2:
			txvalue = "BRTBeaconPowerLevelDefault";
			break;
		case 3:
			txvalue = "BRTBeaconPowerLevelPlus4";
			break;
		}
		return txvalue;
	}

	private com.brtbeacon.sdk.connection.ConnectionCallback createConnectionCallback() {
		return new ConnectionCallback() {

			@Override
			public void onAuthenticated(final BeaconCharacteristics beaconChars) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						statusView.setText("状态: 连接成功");
						StringBuilder sb = new StringBuilder()
								.append("名称: ")
								.append(beaconChars.getName())
								.append("\n")
								.append("区域标识: ")
								.append(beaconChars.getUUID())
								.append("\n")
								.append("主标识: ")
								.append(beaconChars.getMajor())
								.append("\n")
								.append("副标识: ")
								.append(beaconChars.getMinor())
								.append("\n")
								.append("测量功率: ")
								.append(beaconChars.getMeasuredPower())
								.append("\n")
								.append("发射频率: ")
								.append(beaconChars
										.getAdvertisingIntervalMillis())
								.append("\n").append("电量: ")
								.append(beaconChars.getBattery()).append("\n")
								.append("固件版本: ")
								.append(beaconChars.getVersion()).append("\n")
								.append("温度: ")
								.append(beaconChars.getTemperature())
								.append("\n");
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

						txTextView.setText(matchtxvalue(String
								.valueOf(beaconChars.getTX())));
						nameEditView.setText(new String(beaconChars.getName()));
						afterConnectedView.setVisibility(View.VISIBLE);
						if (beaconChars.getDevolMode() == 1) {
							modeSwitch.setChecked(true);
						} else {
							modeSwitch.setChecked(false);
						}
					}
				});
			}

			@Override
			public void onAuthenticationError() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusView.setText("状态: 验证失败，请检查KEY是否正确.");
					}
				});
			}

			@Override
			public void onDisconnected() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusView.setText("状态: 连接断开");
					}
				});
			}

		};
	}

	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
}
