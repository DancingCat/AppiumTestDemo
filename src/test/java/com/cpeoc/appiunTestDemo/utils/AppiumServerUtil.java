package com.cpeoc.appiunTestDemo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;

/**
 * AppiumServer工具类
 * 
 * @author ken
 * @date 2017-7-18
 * 
 */
public class AppiumServerUtil {

	// hubPID nodeServerPID list
	static List<Integer> serverPID = new ArrayList<Integer>();
	static boolean isIOS = AppiumServerConfig.client.toLowerCase().equals("ios");
	static List<Device> deviceList = new ArrayList<Device>();
	/**
	 * 启动所有相关的appium server（包括selenium hub和appium node）
	 * 
	 * @return true 启动成功 false 启动失败
	 * @throws InterruptedException
	 * 
	 * @see 
	 * 	1.Android</br> 
	 * 	
	 * 	1.1 获取所有设备 deviceList,生成临时文件夹tmp </br> 
	 * 	1.2 生成testng.xml文件
	 * 	1.3 启动appium > tmp\deviceNameAppiumServer.log,通过端口是否占用判断启动情况并保存PID </br>
	 *  1.4 将PID保存到serverPID </br>
	 *  
	 *  2.iOS </br>
	 *  2.1 获取端口，启动一个appium server
	 *  
	 */
	public static boolean startAppiumServer() throws InterruptedException {
		
		String serverhost = AppiumServerConfig.serverHost;
//		转到XMLUtil		
//		// 创建临时文件夹tmp
//		File tmp = new File("tmp");
//		if (tmp.exists()) {
//			FileUtil fu = new FileUtil();
//			fu.deleteFile(tmp);
//		}
//		tmp.mkdir();
		
		//删除allure-results文件夹
		File allureresults = new File("allure-results");
		if (allureresults.exists()) {
			FileUtil fu = new FileUtil();
			fu.deleteFile(allureresults);
		}		
		
		if(!isIOS){
			//获取所有安卓设备
			deviceList = DeviceUtil.getAndroidDevicesList();
			//配置文件基础校验
			if(!ConfigFileCheckUtil.checkAllConfigAvialable(deviceList)){
				return false;
			}
			if (deviceList.isEmpty()) {
				System.out.println("系统无可用移动设备，请检查配置文件和设备连接情况！！");
				return false;
			}
			
			//生成testNGXML配置文件
			//XmlUtil.creatTestNGXML(deviceList);
									
			// 分别启动appium server 保存log 并返回PID
			for (Device device : deviceList) {	
				System.out.println("开始启动appium server ");
				String deviceName = device.getDeviceName();
				int servertPort = device.getServerPort();
				int bsPort = device.getBoostrapPort();
				String cmd = "appium -a " + serverhost + " -U \""
						+ deviceName + "\" -p " + servertPort + " -bp "+ bsPort + " > tmp"
						+ File.separator
						+ deviceName.replaceAll(":", ".")
						+ "AppiumServer.log";
				//CmdUtil.getInstance().execCmd(cmd);
				Server s = new Server();
				s.cmd=cmd;
				s.start();
				
				System.out.println("运行命令："+cmd);
				System.out.println("appium server启动中~~~~");				
				long startTime = System.currentTimeMillis();
				while(true){
					Thread.sleep(2000);
					if (PortUtil.isPortUsed(servertPort)) {
						// 获取PID
						int hub = PortUtil.getPidByPort(servertPort);
						serverPID.add(hub);
						System.out.println(deviceName + " appium server启动成功！");
						break ;
					}
					long endTime = System.currentTimeMillis();
					if((endTime-startTime)>AppiumServerConfig.serverStartUpTimeOut){
						System.out.println("appium server 启动失败！设备名：" + deviceName.replaceAll(":", ".")
								+ "端口号：" + servertPort);
						stopAppiumServer();	
						//break;
						return false;
					}
				}
			}

		}else{
			
			//获取所有iOS设备
			deviceList = DeviceUtil.getIOSDevicesList();
			//配置文件基础校验
			if(!ConfigFileCheckUtil.checkAllConfigAvialable(deviceList)){				
				return false;
			}
			if (deviceList.isEmpty()) {
				System.out.println("系统无可用移动设备，请检查配置文件和设备连接情况！！");
				return false;
			}
			//生成testNGXML配置文件
			//XmlUtil.creatTestNGXML(deviceList);
			
			Device de = deviceList.get(0);
			int serverPort = de.getServerPort();
			int boostrapPort = de.getBoostrapPort();
			
			String cmd = "appium -a " + serverhost +" -p " + serverPort +" -bp "+boostrapPort +" > tmp"+ File.separator+ "AppiumServer.log";
			//CmdUtil.getInstance().execCmd(cmd);
			Server s = new Server();
			s.cmd=cmd;
			s.start();
			
			System.out.println("运行命令："+cmd);
			System.out.println("appium server启动中~~~~");
			
			Thread.sleep(AppiumServerConfig.serverStartUpTimeOut);
			if (PortUtil.isPortUsed(serverPort)) {
				// 获取PID
				int pid = PortUtil.getPidByPort(serverPort);
				serverPID.add(pid);
				System.out.println(" appium server启动成功！");
			} else {
				System.out.println("appium server 启动失败！" + "端口号：" + serverPort+",请查看tmp文件夹下相关日志！");
				return false;
			}
		}
		return true;
	}

	/**
	 * 停止所有appium相关服务（selenium hub和appium server）
	 * 
	 * @return true 全部关闭成功 false 存在关闭异常
	 * @see 1.根据PID杀掉进程 </br> Windows:taskkill -f -pid pid </br> mac os:kill -9
	 *      pid </br>
	 */
	public static boolean stopAppiumServer() {
		String os = System.getProperty("os.name");
		for (int port : serverPID) {
			String cmd = os.contains("Windows") ? "taskkill -f -pid " + port
					: "kill -9 " + port;
			System.out.println("运行命令："+cmd);
			CmdUtil.getInstance().execCmd(cmd);
		}

		for (int port : serverPID) {
			if (PortUtil.isPortUsed(port)) {
				return false;
			}
		}
		return true;	
	}	

}
/***
 * 服务类
 * @author ken
 * @date 2017-7-25
 * @see	
 * 	1.继续Thread实现多线程启动不同服务 </br>
 *
 */
class Server extends Thread {
	String cmd;

	@Override
	public void run() {
		CmdUtil cu = new CmdUtil();
		cu.execCmd(cmd);
	}

}
