# Bright SDK for Android #

## Overview ##

Bright SDK for Android是一个库，以便与iBeacons互动。该SDK系统要求Android4.3或以上，蓝牙低功耗（BLE）。

[Bright SDK for iOS](https://github.com/BrightBeacon/iOS-SDK.git)这是IOS SDK链接地址。

SDK包括的功能:

- Beacon 广播扫描
- Beacon 范围监听
- Beacon 特征值读取写入
-  详细功能查看[API文档](http://brightbeacon.github.io/BrightBeacon_Android_SDK)和演示[示例工程](https://github.com/BrightBeacon/Android-SDK/tree/master/Examples) in the SDK

SDK API文档: 

 - [API文档](http://brightbeacon.github.io/BrightBeacon_Android_SDK)

公司官网:

 - [智石科技](http://www.brtbeacon.com)
 
更新日志:


#2.3.0(2015.3.16)#
- 2.3.0SDK迭代主要内容：[br/]
1.添加异常信息类BRTThrowable，BRTBeaconManager类中添加方法registerApp，getAppKey；具体API使用请看文档说明。

#2.2.0(2015.3.4)#
- 1.扫描代码优化

#2.1.0(2015.2.25)#
- 1.修复部分机型扫描bug；
- 2.修复固件升级bug
 
#2.0.3(2015.2.10)#
- 1.修复MeasuredPower读取值错误bug

#2.0.2(2015.2.3)#
- 1.优化配置Beacon

#2.0.1(2015.1.29)#
- 1.兼容固件2.0;
- 2.一些方法名称的改动,详见sdk使用文档

#1.3.1(2015.1.5)#
- 1.MonitoringListener接口中的onExitedRegion()方法新增返回参数(List<.BRTBeacon>);
- 2.判断一个Beacon离开范围的时间设置为5s。

#1.3.0(2015.1.4)#
- 1.所有涉及测量功率的方法都使用枚举BRTBeaconPower;
- 2.BRTBeaconManager类新增startMonitoringForRegion()开启后台服务扫描Beacon。

#1.2.3(2014.12.15)#
- 1.修复1.2.2中固件检测bug；

#1.2.2(2014.12.11)#
- 1.修复红米，小米手机扫描bug。
- 2.更改BRTBeaconConnection类中authenticate()方法名为connect()。

#1.2.1(2014.12.6)#
- 1.固件更新v5，修复电量计算bug；
- 2.修复固件更新bug；
- 3.修复离开监听范围bug（BRTBeacon对象构造方法添加macAddress参数）。

#1.2.0(2014.11.26)#
- 1.固件升级为V4。


 




