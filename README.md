# Bright SDK for Android #

## Overview ##

Bright SDK for Android is a library to allow interaction with iBeacons. The SDK system requirements are Android 4.3 or above and Bluetooth Low Energy.

It mimics [Bright SDK for iOS](https://github.com/BrightBeacon/iOS-SDK.git). All naming conventions come from the iBeacon library for iOS and the Bright iOS library.

It allows for:
- beacon ranging (scan beacons and optionally filters them by their values)
- beacon monitoring (monitors regions for those devices that have entered/exited a region)
- beacon characteristic reading and writing (proximity UUID, major & minor values, broadcasting power, advertising interval), see [BeaconConnection] (https://github.com/BrightBeacon/Android-SDK/tree/master/Documents/com/brtbeacon/sdk/connection/BeaconConnection.html) class and [demos](https://github.com/BrightBeacon/Android-SDK/tree/master/Examples) in the SDK

Docs: 
 - [Current JavaDoc documentation](http://brightbeacon.github.io/BrightBeacon_Android_SDK)
 - [Bright Community Portal](http://www.brtbeacon.com)

**What is ranging?**

Ranging allows apps to know the relative distance between a device and beacons. This can be very valuable. Consider for example of an indoor location app of department store. The app can determine which department (such footwear, clothing, accessories etc) is closest by. Information about this proximity can be employed by the app to show fitting guides or offer discounts.

As Bluetooth Low Energy ranging depends on detecting radio signals, results will vary depending on the placement of Bright beacons and whether a user's mobile device is in-hand, in a bag or a pocket. Clear line of sight between a mobile device and a beacon will yield better results so it is recommended that Bright beacons not be hidden between shelves.

To enjoy consistent ranging it is good practice to use the app in the foreground while the user is holding the device in-hand (which means the app is on and running).

Apps can use `startRanging` method of `BRTBeaconManager` class to determine relative proximity of beacons in the region and can be updated when this distance changes. Ranging updates come every second to listeners registered with `setRangingListener` method of `BRTBeaconManager` class. Ranging updates contain a list of currently found beacons. If a beacon goes out of range it will not be presented on this list.

Ranging is designed to be used in apps in foreground.

**What is monitoring?**

Region monitoring is a term used to describe a Bluetooth device's usage and  detect when a user is in the vicinity of beacons. You can use this functionality to show alerts or provide contextual aware information as a user enters or exits  a beacon's region. Beacon's regions are defined by beacon's values:

- proximity UUID: 128-bit unique identifier,
- major: 16-bit unsigned integer to differentiate between beacons within the same proximity UUID,
- minor: 16-bit unsigned integer to differentiate between beacons with the same proximity UUID and major value.

Note that all of those values are optional. That means that single region can contain multiple beacons which creates interesting use cases. Consider for example a department store that is identified by a particular proximity UUID and major value. Different sections of the store are differentiated further by a different minor value. An app can monitor region defined by their proximity UUID and major value to provide location-relevant information by distinguishing minor values.

Apps can use `startMonitoring` method of `BRTBeaconManager` class to start monitoring regions. Monitoring updates come to listeners registered with `setMonitoringListener` method of `BRTBeaconsManager` class.

Monitoring is designed to perform periodic scans in the background. By default it scans for 5 seconds and sleeps 25 seconds. That means that it can take by default up to 30 seconds to detect entering or exiting a region. Default behaviour can be changed via `BRTBeaconManager#setBackgroundScanPeriod`.

## Installation ##

1. Copy [brightbeacon-sdk-1.0.5.jar](https://github.com/BrightBeacon/BrightBeacon_Android_SDK/blob/master/BrightSDK/brightbeacon-sdk-1.0.5.jar) to your `libs` directory.
2. Add following permissions and service declaration to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```

```xml
<service android:name="com.brightbeacon.sdk.service.BRTBeaconService"
         android:exported="false"/>
```
(optional) You can enable debug logging of the Bright SDK by calling `com.brightbeacon.sdk.utils.L.enableDebugLogging(true)`.

## Usage and demos ##

Demos are located in [Demos](https://github.com/BrightBeacon/Android-SDK/tree/master/Examples) directory. 

Demos include samples for ranging beacons, monitoring beacons, calculating distance between beacon and the device and also changing minor value of the beacon.

Quick start with ranging:

```java
  private static final String BRIGHT_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
  private static final BRTRegion ALL_BRIGHT_BEACONS = new BRTRegion("regionId", BRIGHT_PROXIMITY_UUID, null, null);

  private BRTBeaconManager beaconManager = new BRTBeaconManager(context);

  // Should be invoked in #onCreate.
  beaconManager.setRangingListener(new RangingListener() {
    @Override public void onBeaconsDiscovered(RangingResult rangingResult) {
      Log.d(TAG, "Ranged beacons: " + rangingResult.beacons);
    }
  });

  // Should be invoked in #onStart.
  beaconManager.connect(new erviceReadyCallback() {
    @Override public void onServiceReady() {
      try {
        beaconManager.startRanging(ALL_BRIGHT_BEACONS);
      } catch (RemoteException e) {
        Log.e(TAG, "Cannot start ranging", e);
      }
    }
  });

  // Should be invoked in #onStop.
  try {
    beaconManager.stopRanging(ALL_BRIGHT_BEACONS);
  } catch (RemoteException e) {
    Log.e(TAG, "Cannot stop but it does not matter now", e);
  }

  // When no longer needed. Should be invoked in #onDestroy.
  beaconManager.disconnect();
```


