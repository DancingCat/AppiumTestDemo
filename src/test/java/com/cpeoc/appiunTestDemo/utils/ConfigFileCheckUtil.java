package com.cpeoc.appiunTestDemo.utils;

import java.util.List;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.conf.IOSDeviceInfoConfig;
import com.cpeoc.appiunTestDemo.conf.TestCaseConfig;

/**
 * 配置文件配置检查类
 * @author ken
 * @date 2017-8-1
 *
 */
public class ConfigFileCheckUtil {
	/**
	 * 配置文件所有字段校验
	 */
	public static void checkAllConfigAvialable() {
		//************************AppiumServerConfig*************************
		//serverHost ipv4格式
		String serverhost = AppiumServerConfig.serverHost.trim();
		String regex = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"; 
		if(serverhost.isEmpty() || !serverhost.matches(regex)){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的serverHost，ipv4格式！");
			return;
		}
		//client 必填，可选Android和iOS
		String client = AppiumServerConfig.client.trim().toLowerCase();
		boolean isStringTrue = client.equals("android") || client.equals("ios");
		if(client.isEmpty() || !isStringTrue){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的client，可选ios或android！");
			return;
		}
		String os = System.getProperty("os.name");
		if(!os.contains("Mac") && client.equals("ios")){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的client，非Mac os操作系统下无法运行iOS测试！");
			return;
		}
		//startPortNum 1~65535
		int startportnum = AppiumServerConfig.startPortNum;
		if(startportnum <= 0 || startportnum > 65535){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的startPortNum，端口可选范围1~65535！");
			return;
		}
		//adbPath Windows可不填 Mac必填
		String adbpath = AppiumServerConfig.adbPath.trim();
		
		if(!os.contains("Windows") && adbpath.isEmpty()){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的adbpath，非Windows操作系统请配置adbpath并确保其可用！");
			return;
		}
		boolean adbUseable = false;
		String cmd = os.contains("Windows")?" adb version":adbpath +" version";
		List<String> adbVersionCmdList = CmdUtil.getInstance().execCmd(cmd );
		for (String adbVersionCmd : adbVersionCmdList) {
			if(adbVersionCmd.contains("Android Debug Bridge version")){
				adbUseable = true;
			}
		}		
		if(!adbUseable){
			System.out.println("请在AppiumServerConfig配置文件中配置正确的adbpath，非Windows操作系统请配置adbpath并确保其可用！");
			return;
		}
		
		//************************IOSDeviceInfoConfig*************************
		//iOSSimulatorDevices
		String iossimulatordevices = IOSDeviceInfoConfig.iOSSimulatorDevices.trim();
		boolean realdevice = AppiumServerConfig.realDevice;
		if(client.equals("ios") && !realdevice){
			if(iossimulatordevices.isEmpty()){
				System.out.println("请在IOSDeviceInfoConfig配置文件中配置正确的iOSSimulatorDevices，详情请查看配置文件说明！");
				return;
			}
			//有一个设备可用即可
			List<String> ioSimulatorList = DeviceUtil.getIOSimulatorList();
			if(null==ioSimulatorList){
				System.out.println("请在IOSDeviceInfoConfig配置文件中配置正确的iOSSimulatorDevices，详情请查看配置文件说明！");
				return;
			}		
		}
		//************************TestCaseConfig*************************
		//whereIsTestCode 必填，只能选test或main
		String whereistestcode = TestCaseConfig.whereIsTestCode.trim();
		boolean isPathTrue = whereistestcode.equals("main") || whereistestcode.equals("test");
		if(whereistestcode.isEmpty() || !isPathTrue){
			System.out.println("请在TestCaseConfig配置文件中配置正确的whereIsTestCode，必填，只能选test或main,且确保你的工程是标准的maven工程！");
			return;
		}
		
		//defaultPackage  必填，不支持多个包
		String defaultexcgroup = TestCaseConfig.defaultExcGroup.trim();
		if(defaultexcgroup.isEmpty()){
			System.out.println("请在TestCaseConfig配置文件中配置正确的defaultExcGroup，此为必填项！");
			return;
		}
		
		
	}
}
