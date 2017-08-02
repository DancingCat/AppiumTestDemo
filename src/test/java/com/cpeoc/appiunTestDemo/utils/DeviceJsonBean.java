package com.cpeoc.appiunTestDemo.utils;

import java.util.List;

/**
 * device  json实体类
 * @author ken
 * @date 2017-7-19
 * 
 */
public class DeviceJsonBean {
	
	List<Capabilities> capabilities;
	Configuration configuration;
	public List<Capabilities> getCapabilities() {
		return capabilities;
	}
	public void setCapabilities(List<Capabilities> capabilities) {
		this.capabilities = capabilities;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	
}
