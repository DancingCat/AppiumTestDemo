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
 *  	5.若想运行所有模拟器且不嫌时间长的话，可手动修改模拟器名称  </br>
 * 
 */
public interface IOSSimulatorConfig {
	/** 运行测试的模拟器  deviceName:platformVersion,多个设备之间以逗号分隔 */
	/** 该项与下面两项二选一，配置了这项下面两项可不配置，反之一样 */
	/** 此项优先,若为空则自动获取系统上可用模拟器 */
	String iOSSimulatorDevices = "";
	
	/** 当模拟器名称一样时，true取最新系统版本的模拟器，false取最旧版本模拟器 */
	boolean cover = false;
	
	/** 模拟器数量,取前n个,必须>=1 */
	int deviceAmount = 6;
}
