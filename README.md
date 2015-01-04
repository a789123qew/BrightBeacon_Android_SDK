# Bright SDK for Android #

## Overview ##

Bright SDK for Android是一个库，以便与iBeacons互动。该SDK系统要求Android4.3或以上，蓝牙低功耗（BLE）。

[Bright SDK for iOS](https://github.com/BrightBeacon/iOS-SDK.git)这是IOS SDK链接地址。

SDK包括的功能:

- Beacon 广播扫描
- Beacon 范围监听
- Beacon 特征值读取写入
-  详细功能查看[API文档](http://brightbeacon.github.io/BrightBeacon_Android_SDK)和演示[demos](https://github.com/BrightBeacon/Android-SDK/tree/master/Examples) in the SDK

SDK API文档: 

 - [API文档](http://brightbeacon.github.io/BrightBeacon_Android_SDK)

公司官网:

 - [重庆智石网络科技有限公司](http://www.brtbeacon.com)
 
更新日志:


- 2014.1.4:（brightbeacon-sdk-1.3.0.jar）
- 1.所有涉及测量功率的方法都使用枚举BRTBeaconPower;
- 2.BRTBeaconManager类新增startMonitoringForRegion()开启后台服务扫描Beacon;
- 2014.12.15:（brightbeacon-sdk-1.2.3.jar）修复1.2.2中固件检测bug；
- 2014.12.11:（brightbeacon-sdk-1.2.2.jar）修复红米，小米手机扫描bug。更改BRTBeaconConnection类中authenticate()方法名为connect();
- 2014.12.6:1.固件更新v5，修复电量计算bug；2.修复固件更新bug；3.修复离开监听范围bug（BRTBeacon对象构造方法添加一个macAddress参数）；
- 2014.11.26:固件升级为V4；


 




