package com.cpeoc.appiunTestDemo.utils;
/**
 * device 相关属性封装类
 * @author ken
 * @date 2017-7-21
 *
 */
public class Device {
	/** 设备名deviceName */
	String deviceName;
	/** 系统版本 */
	String sysVersion;
	/** 系统类型如Android iOS */
	//String platform;
	/** appium server端口 */
	int serverPort;
	/** boostrap 端口 */
	int boostrapPort;
	/** iOS真机udid*/
	String udid;
	
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSysVersion() {
		return sysVersion;
	}
	public void setSysVersion(String sysVersion) {
		this.sysVersion = sysVersion;
	}

	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getBoostrapPort() {
		return boostrapPort;
	}
	public void setBoostrapPort(int boostrapPort) {
		this.boostrapPort = boostrapPort;
	}
	


}
