package com.cpeoc.appiunTestDemo.conf;
/**
 * iOS 设备配置文件
 * @author ken
 * @date 2017-7-20
 * @see
 * 	1.因为iOS通过命令行获取连接的真实设备的系统版本信息 </br>
 * 	2.模拟器Simulator通过配置的形式多机串行测试		 </br>
 *  3.如果是window操作系统  配置的运行iOS 直接退出	 </br>
 *  4.iOS真机无须手动配置						 </br>
 * 
 */
public interface IOSDeviceInfoConfig {
	/** 运行测试的模拟器  deviceName:platformVersion,多个设备之间以逗号分隔 */
	String iOSSimulatorDevices = "iPhone 8:9.3,iPhone 9:10.3";
}
