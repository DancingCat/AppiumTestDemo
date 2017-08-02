package com.cpeoc.appiunTestDemo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.conf.IOSDeviceInfoConfig;
/**
 * 手机设备工具类
 * @author ken
 * @date 2017-7-18
 *
 */
public class DeviceUtil {
	
	static String os = System.getProperty("os.name");
	
	/**
	 * 获取可用的安卓真机设备Map
	 * @return 可用安卓真机设备deviceName的Map
	 * @see
	 * 	key deviceName  value Version
	 */
	public static Map<String,String> getAvailableAndroidDevicesMap(){
		List<String> devicesList = getAvailableAndroidDevicesList();
		
		Map<String, String> deviceMap = new HashedMap();
		for (String device : devicesList) {
			String androidSysVersionByDeviceName = getAndroidSysVersionByDeviceName(device);
			deviceMap.put(device, androidSysVersionByDeviceName);
		}	
		
		return deviceMap;
		
	}
	
	/**
	 * 获取可用的安卓真机设备List
	 * @return 可用安卓真机设备deviceName的List
	 * @see
	 * 	deviceName  List
	 */
	public static List<String> getAvailableAndroidDevicesList(){
		List<String> deviceList =new ArrayList<>();
		String cmd = "";
		if(os.contains("Windows")){
			cmd ="adb devices";
		}else{
			cmd = AppiumServerConfig.adbPath+" devices ";
		}
		
		List<String> adbList = CmdUtil.getInstance().execCmd(cmd);
		
		for (String device : adbList) {
			//包含device表示设备在线
			if(device.contains("device") & !device.contains("devices")){
				deviceList.add(device.split("\t")[0].trim());
			}
		}
		
		return deviceList;
		
	}
	
	/**
	 * 获取iOS真机Map
	 * @return iOS真机Map
	 * @see
	 * 	key udid value SysVersion
	 */
	public static Map<String,String> getAvailableiOSDevicesMap(){	
		List<String> udidList = getiAvailableOSDevicesList();
		Map<String, String> deviceMap = new HashedMap();
		for (String udid : udidList) {
			String getiOSSysVersionByUdid = getiOSSysVersionByUdid(udid);
			deviceMap.put(udid, getiOSSysVersionByUdid);
		}
		return deviceMap;
	}
	
	/**
	 * 获取iOS真机List
	 * @return iOS真机List
	 * @see
	 * 	udid List
	 */
	public static List<String> getiAvailableOSDevicesList(){			
		return CmdUtil.getInstance().execCmd("idevice_id -l");
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
	 */
	public static String getiOSSysVersionByUdid(String udid){
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
	 * 获取iOS模拟器List
	 * @return  List
	 */
	public static List<String> getIOSimulatorList(){
		String[] simDevices = IOSDeviceInfoConfig.iOSSimulatorDevices.trim().split(",");
		
		List<String> deviceList = new ArrayList<>();
		for (String device : simDevices) {
			String[] deviceInfo = device.split(":");
			String deviceName = deviceInfo[0].trim();
			//String value = deviceInfo[1].trim();
			deviceList.add(deviceName);
		}
		return deviceList;
	}
	
	/**
	 * 获取iOS模拟器Map
	 * @return  Map
	 * @see
	 * 	key deviceName value SysVersion
	 */
	public static Map<String,String> getIOSimulatorMap(){
		String[] simDevices = IOSDeviceInfoConfig.iOSSimulatorDevices.split(",");
			
		Map<String,String> deviceMap = new HashedMap();
		for (String device : simDevices) {
			String[] deviceInfo = device.split(":");
			String key = deviceInfo[0].trim();
			String value = deviceInfo[1].trim();
			deviceMap.put(key, value);
		}
		
		return deviceMap;
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
	 * 从DeviceJsonBeanList中获取deviceList
	 * @param list
	 * @return
	 */
	public static List<String> getDeviceListFromDeviceJsonBeanList(List<DeviceJsonBean> list){
		List<String> deviceList = new ArrayList<>();
		for (DeviceJsonBean deviceJson : list) {
			List<Capabilities> capabilities = deviceJson.getCapabilities();
			deviceList.add(capabilities.get(0).getBrowserName());
		}		
		return deviceList;
		
		
		
	}
}
