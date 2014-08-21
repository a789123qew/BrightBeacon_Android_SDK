package com.bright.examples.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 演示功能列表
 */
public class AllDemosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.all_demos);

		findViewById(R.id.distance_demo_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AllDemosActivity.this,
								ListBeaconsActivity.class);
						intent.putExtra(
								ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
								DistanceBeaconActivity.class.getName());
						startActivity(intent);
					}
				});
		findViewById(R.id.notify_demo_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AllDemosActivity.this,
								ListBeaconsActivity.class);
						intent.putExtra(
								ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
								NotifyDemoActivity.class.getName());
						startActivity(intent);
					}
				});
		findViewById(R.id.characteristics_demo_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AllDemosActivity.this,
								ListBeaconsActivity.class);
						intent.putExtra(
								ListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
								CharacteristicsDemoActivity.class.getName());
						startActivity(intent);
					}
				});
		findViewById(R.id.config_demo_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AllDemosActivity.this,
								ConfigDemoActivity.class);
						startActivity(intent);
					}
				});

	}

}
