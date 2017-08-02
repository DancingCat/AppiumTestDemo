package com.cpeoc.appiunTestDemo.utils;
/**
 * node  json实体类相关类
 * @author ken
 * @date 2017-7-19
 * 
 */
public class Capabilities {
	/** 设备名deviceName */
	String browserName;
	/** 系统版本 */
	String version;
	int maxInstances=1;
	/** 系统类型如Android iOS */
	String platform;
	
	public String getBrowserName() {
		return browserName;
	}
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getMaxInstances() {
		return maxInstances;
	}
	public void setMaxInstances(int maxInstances) {
		this.maxInstances = maxInstances;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
