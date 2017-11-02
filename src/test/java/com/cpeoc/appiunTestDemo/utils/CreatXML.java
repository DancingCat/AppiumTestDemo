package com.cpeoc.appiunTestDemo.utils;

import io.qameta.allure.Description;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;

/**
 * TestNGXML文件生成
 * @author ken
 * 生成到tmp目录下，以便mvn clean test使用
 */
public class CreatXML {

	boolean isIOS = AppiumServerConfig.client.toLowerCase().equals("ios");
	List<Device> deviceList = new ArrayList<Device>();
	
	@Test
	public void creatRuntimeXML(){
		
		if(!isIOS){
			//获取所有安卓设备
			deviceList = DeviceUtil.getAndroidDevicesList();
		}else{
			//获取所有iOS设备
			deviceList = DeviceUtil.getIOSDevicesList();
		}
		//生成testNGXML配置文件
		XmlUtil.creatTestNGXML(deviceList);
		
	}
	
}
