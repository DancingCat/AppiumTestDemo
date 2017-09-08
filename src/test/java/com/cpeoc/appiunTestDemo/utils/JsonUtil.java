package com.cpeoc.appiunTestDemo.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.google.gson.Gson;

/**
 * json工具类
 * 
 * @author ken
 * @date 2017-7-19
 * @see 1.主要用于生产nodeDevice.json </br>
 *      2.利用gson </br>
 *      3.格式参考：</br>
 *      https://github.com/appium/appium/blob/51285550eadb907e5cf90931a0c72ec7bc4a004d/docs/en/advanced-concepts/grid.md</br>
 */
public class JsonUtil {

	/**
	 * 生成device json字符串
	 * 
	 * @return device json toString
	 */
	public static String createDeviceJsonStr(DeviceJsonBean djb) {

		Gson g = new Gson();	
		// 返回json
		return g.toJson(djb);

	}
	/**
	 * 在tmp目录下创建所有设备的json文件
	 * @return List<DeviceJsonBean> 设备json实体类列表
	 * @see
	 * 	1.创建tmp临时目录，如果已有删除已有tmp再创建 </br>
	 * 	2.读取配置文件client参数，获取设备列表以及对应系统版本 </br>
	 * 	3.获取可用端口列表 </br>
	 * 	4.在tmp目录下生成deviceName.json文件 </br>
	 */
	public static List<DeviceJsonBean> createAllDeviceJson() {
		
		List<DeviceJsonBean> deviceJsonBeanList  = new ArrayList<DeviceJsonBean>();
		//platForm
		String platForm = AppiumServerConfig.client.toLowerCase();
		
		// 创建临时文件夹tmp
		File tmp = new File("tmp");
		if (tmp.exists()) {
			FileUtil fu = new FileUtil();
			fu.deleteFile(tmp);
		}
		
		tmp.mkdir();
		
		// 
		List<String> deviceList = null;
		Map<String, String> deviceMap = null;
		// 根据配置文件指定的client，获取对应设备系统版本
		// 安卓设备直接获取可用设备
		if (platForm.equals("android")) {
			deviceList = DeviceUtil.getAvailableAndroidDeviceList();
			deviceMap = DeviceUtil.getAvailableAndroidDeviceMap();
			// 苹果设备需要根据是否真机运行，模拟器则读取IOSDeviceInfoConfig配置文件
		} else  {
			deviceList = DeviceUtil.getIOSDevices();
			deviceMap = DeviceUtil.getIOSDevicesMap();
		}

		// 获取可用端口列表 +hubport 一个
		List<Integer> availablePortList = PortUtil.getAvailablePortList(deviceList.size() + 1);
		int hubPort = availablePortList.get(0);
		//device json 字符串
		
		int portIndex = 1;
		for (String deviceName : deviceList) {			
			// 创建json文件  防止有些模拟器名称带有:
			File deviceJson = new File(tmp, deviceName.replaceAll(":", ".") + ".json");
			try {
				deviceJson.createNewFile();
			} catch (IOException e) {
				System.out.println("创建" + deviceName + ".json失败！" + e.getMessage());
			}
			// 往json文件写入内容
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;
			try {
				
				osw = new OutputStreamWriter(new FileOutputStream(deviceJson));
				bw = new BufferedWriter(osw);
				//生产device json 字符串
				String ver = deviceMap.get(deviceName);
				int port = availablePortList.get(portIndex);
				DeviceJsonBean createDeviceJsonBean = createDeviceJsonBean(deviceName, platForm, ver, hubPort, port);			
				
				//将bean 添加到结果返回的list中
				deviceJsonBeanList.add(createDeviceJsonBean);
				
				String jsonStr = createDeviceJsonStr(createDeviceJsonBean);
				portIndex++;
				//System.out.println(jsonStr);
				bw.write(jsonStr);
				
				
			} catch (Exception e) {
				System.out.println(deviceName + ".json文件写入出错！" + e.getMessage());
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					System.out.println("RandomAccessFile写入文件操作流关闭出错！" + e.getMessage());
				}
			}

		}
		return deviceJsonBeanList;

	}
	/**
	 * 构造DeviceJsonBean
	 * @param deviceName
	 *            设备名
	 * @param platForm
	 *            系统类型Android iOS
	 * @param version
	 *            系统版本
	 * @param hubPort
	 *            hub端口
	 * @param port
	 *            node节点端口
	 * @return DeviceJsonBean
	 */
	public static DeviceJsonBean createDeviceJsonBean(String deviceName, String platForm, String version, int hubPort, int port){
		// 构造 device json 数据
		Capabilities c = new Capabilities();
		Configuration co = new Configuration();
		c.setBrowserName(deviceName);
		c.setPlatform(platForm);
		c.setVersion(version);
		co.setHost(AppiumServerConfig.serverHost);
		co.setHubPort(hubPort);
		co.setHubHost(AppiumServerConfig.serverHost);
		co.setPort(port);
		co.setUrl("http://"+AppiumServerConfig.serverHost + ":"+port + "/wd/hub");

		List<Capabilities> caList = new ArrayList<Capabilities>();
		caList.add(c);

		DeviceJsonBean djb = new DeviceJsonBean();
		djb.setCapabilities(caList);
		djb.setConfiguration(co);
		return djb;
	}	
	
}
