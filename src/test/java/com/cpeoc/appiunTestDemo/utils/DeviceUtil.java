package com.cpeoc.appiunTestDemo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.conf.IOSSimulatorConfig;
/**
 * 手机设备工具类
 * @author ken
 * @date 2017-7-18
 *
 */
public class DeviceUtil {
	
	static String os = System.getProperty("os.name");
	
	
	/**
	 * 获取可用的安卓真机设备List
	 * @return 可用安卓真机设备deviList 可能为null
	 * @see
	 * 	Device  List
	 */
	public static List<Device> getAndroidDevicesList(){
		List<Device> deviceList =new ArrayList<>();
		String cmd = "";
		if(os.contains("Windows")){
			cmd ="adb devices";
		}else{
			cmd = AppiumServerConfig.adbPath+" devices ";
		}
		
		List<String> adbList = CmdUtil.getInstance().execCmd(cmd);
		
		for (String line : adbList) {
			//包含device表示设备在线
			if(line.contains("device") & !line.contains("devices")){
				Device device = new Device();
				String deviceName = line.split("\t")[0].trim();
				device.setDeviceName(deviceName);
				device.setSysVersion(getAndroidSysVersionByDeviceName(deviceName));	
				//将设备添加到list
				deviceList.add(device);
			}
		}
		if(!deviceList.isEmpty()){
			//获取可用端口列表
			int deviceSum = deviceList.size();
			List<Integer> availablePortList = PortUtil.getAvailablePortList(deviceSum*2);
			for (int i=0;i<deviceSum;i++) {
				Device de = deviceList.get(i);
				int serverPort = availablePortList.get(i);
				int bsPort = availablePortList.get(deviceSum+i);
				de.setServerPort(serverPort);
				de.setBoostrapPort(bsPort);
			}
		}
		
		return deviceList;
		
	}
	
	
	/**
	 * 获取iOS真机deviceName-SysVersion Map
	 * @return iOS真机Map
	 * @see
	 * 	key deviceName value SysVersion
	 */
	public static Map<String,String> getIOSRealDeviceMap(){	
		List<String> udidList = getIOSRealDeviceUdidList();
		Map<String, String> deviceMap = new HashedMap();
		for (String udid : udidList) {
			String getiOSSysVersionByUdid = getIOSRealDeviceSysVersionByUdid(udid);
			deviceMap.put(udid, getiOSSysVersionByUdid);
		}
		return deviceMap;
	}
	
	/**
	 * 获取iOS真机List
	 * @return iOS真机List
	 * @see
	 * 	deviceName List
	 * @update
	 * 	2017-8-10  更新获取方式，因为idevice_id -l在只有一台设备连接的情况下会出现3个值的情况
	 * 	使用system_profiler SPUSBDataType | grep "Serial Number" 获取udid列表
	 * 	再使用instruments -s devices | grep udid获取deviceName
	 */
	public static List<Device> getIOSRealDeviceList(){			
		List<Device> iOSRealDeviceList = new ArrayList<>();
		List<String> iosRealDeviceUdidList = getIOSRealDeviceUdidList();
		List<Integer> availablePortList = PortUtil.getAvailablePortList(2);
		if(!iosRealDeviceUdidList.isEmpty()){
			for (String udid : iosRealDeviceUdidList) {
				List<String> deviceInfo = CmdUtil.getInstance().execCmd("instruments -s devices | grep " + udid);
				if(!deviceInfo.isEmpty()){
					Device de = new Device();
					//deviceName
					de.setDeviceName(deviceInfo.get(0).split(" ")[0]);
					//udid
					de.setUdid(udid);
					//sysVersion
					String sysVersion = getIOSRealDeviceSysVersionByUdid(udid);
					de.setSysVersion(sysVersion);
					de.setServerPort(availablePortList.get(0));
					de.setBoostrapPort(availablePortList.get(1));
					//将设备添加到list
					iOSRealDeviceList.add(de);
				}
			}
		}
		return iOSRealDeviceList;
	}
	
	/**
	 * 获取已连接iOS真机udid list
	 * @return iOS真机udid list
	 * @date 
	 *  2017-8-10
	 * @see
	 * 	1.更新获取方式，因为idevice_id -l在只有一台设备连接的情况下会出现3个值的情况 </br>
	 */
	public static List<String> getIOSRealDeviceUdidList(){
		List<String> iOSRealDeviceUdidList = new ArrayList<>();
		List<String> lines = CmdUtil.getInstance().execCmd("system_profiler SPUSBDataType | grep 'Serial Number'");
		if(!lines.isEmpty()){
			for (String line : lines) {
				String deviceNameLine = line.trim();
				if(deviceNameLine.length()==55){
					String[] split = deviceNameLine.split(":");
					iOSRealDeviceUdidList.add(split[1].trim());				
				}
			}
		}
		return iOSRealDeviceUdidList;
	}
	
	/**
	 * 通过deviceName 获取安卓系统版本
	 * @param deviceName
	 * @return 安卓系统版本
	 * @see
	 * 	1.如果获取不到则返回null </br>
	 * 	2.只取系统版本号两位 @update 2017-7-21 </br>
	 */
	public static String getAndroidSysVersionByDeviceName(String deviceName){
		String cmd = "";
		if(os.contains("Windows")){
			cmd = " adb -s "+deviceName+" shell cat /system/build.prop";
		}else{
			cmd = AppiumServerConfig.adbPath+" -s "+deviceName+" shell cat /system/build.prop";
		}
		List<String> outPutStr = CmdUtil.getInstance().execCmd(cmd);
		for (String line : outPutStr) {
			if(line.contains("ro.build.version.release")){
				String v = line.split("=")[1].trim();
				return v.substring(0, v.length()-2);
			}
		}
		return null;
	}
	/**
	 * 根据udid获取真机系统版本
	 * @param udid
	 * @return 系统版本，如果为null表示获取失败
	 * @update
	 * 	
	 */
	public static String getIOSRealDeviceSysVersionByUdid(String udid){
		List<String> op = CmdUtil.getInstance().execCmd("ideviceinfo -u "+udid);
		for (String line : op) {
			if(line.contains("ProductVersion")){
				String v = line.split(":")[1].trim();
				return v.substring(0, v.length()-2);
			}
		}
		return null;		
	}
	
	/**
	 * 自动获取iOS模拟器List
	 * @return  List
	 * 
	 * @update 
	 * 	2017-8-10 </br>
	 * 	使用instruments -s devices 获取 </br>
	 * 	过滤条件：模拟器名称i开头、行内不包含+、包含Simulator字符
	 * 	
	 */
	public static List<Device> getIOSSimulatorList(){		
		List<Device> deviceList = new ArrayList<>();
		Map<String, String> iosSimulatorMap = getIOSSimulatorMap();
		List<Integer> availablePortList = PortUtil.getAvailablePortList(2);
		Iterator<Entry<String, String>> iterator = iosSimulatorMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			Device de = new Device();
			//deviceName
			String deviceName = next.getKey();
			de.setDeviceName(deviceName);
			//sysVersion
			String sysVersion = next.getValue();
			de.setSysVersion(sysVersion);
			//serverPort
			int serverPort = availablePortList.get(0);
			de.setServerPort(serverPort);
			//bsPort
			int bsPort = availablePortList.get(1);
			de.setBoostrapPort(bsPort);
			
			deviceList.add(de);	
		}
		return deviceList;
	}
	/**
	 * 通过deviceName 获取 udid
	 * @param deviceName  
	 * @return udid
	 * @see
	 * 	1.先获取deviceName - udid Map </br>
	 */
	public static String getIOSRealDeviceUdidByDeviceName(String deviceName){
		//DeviceName: Dancingcat"
		Map<String,String> deviceNameUdidMap = new HashedMap();
		List<String> iosRealDeviceUdidList = getIOSRealDeviceUdidList();
		for (String iosRealDeviceUdid : iosRealDeviceUdidList) {
			List<String> execCmd = CmdUtil.getInstance().execCmd("ideviceinfo -u "+iosRealDeviceUdid);
			for (String line : execCmd) {
				if(line.contains("DeviceName")){
					String dName = line.split(":")[1].trim();
					deviceNameUdidMap.put(dName, iosRealDeviceUdid);
				}
			}	
		}
		
		return deviceNameUdidMap.get(deviceName);
	}
	
	/**
	 * 自动获取系统iOS模拟器列表deviceName-sysVersion Map
	 * @return  Map
	 * @see
	 * 	key deviceName value sysVersion
	 */
	public static Map<String,String> getIOSSimulatorMap(){
		Map<String,String> iOSimulatorMap = new HashedMap();
		List<String> simDevices = CmdUtil.getInstance().execCmd("instruments -s devices");

		for (String device : simDevices) {
			if(device.contains("Simulator") && !device.contains("+") && device.startsWith("iPhone")){
				int start = device.indexOf("(");
				int end = device.indexOf(")");
				String deviceName = device.substring(0,start).trim();
				String sysVersion = device.substring(start+1,end);
				//判断有问题
				if(!(iOSimulatorMap.containsKey(deviceName) && ! IOSSimulatorConfig.cover) ){
					iOSimulatorMap.put(deviceName, sysVersion);
					if(iOSimulatorMap.size()==IOSSimulatorConfig.deviceAmount){
						break;
					}
					
					
				}

			}	
		}
		return iOSimulatorMap;
	}
	
	/**
	 * 通过配置文件获取iOS模拟器List
	 * @return  List
	 */
	public static List<Device> getIOSSimulatorListByConfig(){
		String[] simDevices = IOSSimulatorConfig.iOSSimulatorDevices.trim().split(",");
		
		List<Device> deviceList = new ArrayList<>();
		List<Integer> availablePortList = PortUtil.getAvailablePortList(2);
		for (String device : simDevices) {
			String[] deviceInfo = device.split(":");
			String deviceName = deviceInfo[0].trim();
			String sysVersion = deviceInfo[1].trim();
			//String value = deviceInfo[1].trim();
			Device de = new Device();
			de.setDeviceName(deviceName);
			de.setSysVersion(sysVersion);
			de.setServerPort(availablePortList.get(0));
			de.setBoostrapPort(availablePortList.get(1));
			
			deviceList.add(de);
		}
		return deviceList;
	}
	
	/**
	 * 获取无空格的设备名
	 * @param deviceName 原始设备名称
	 * @return 无空格的设备名
	 */
	public static String  getNotBlankDeviceName(String deviceName){
		String[] names = deviceName.split(" ");
		String notBlankName ="";
		for (String name : names) {
			if(name!=""){
				notBlankName+=name;
			}
		}
		return notBlankName;
	}
	/**
	 * 获取iOS设备列表 
	 */
	public static List<Device> getIOSDevicesList(){
		//真机
		if(AppiumServerConfig.realDevice){
			return getIOSRealDeviceList();
		//模拟器
		}else{
			//手动配置
			if(!IOSSimulatorConfig.iOSSimulatorDevices.isEmpty()){
				return getIOSSimulatorListByConfig();
			//自动获取
			}else{
				return getIOSSimulatorList();
			}			
		}
	}

	/**
	 * 从设备list中获取设备名list
	 * @param deivceList
	 * @return   deviceNameList
	 */
	public static List<String> getDeviceNameList(List<Device> deivceList) {
		List<String>  deviceNameList = new ArrayList<>();
 		for (Device device : deivceList) {
 			deviceNameList.add(device.getDeviceName());
		}
		return deviceNameList;
	}
}
