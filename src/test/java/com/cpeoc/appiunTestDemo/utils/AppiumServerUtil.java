package com.cpeoc.appiunTestDemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.google.gson.Gson;

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

	/**
	 * 启动所有相关的appium server（包括selenium hub和appium node）
	 * 
	 * @return true 启动成功 false 启动失败
	 * @throws InterruptedException
	 * 
	 * @see 
	 * 	1.Android</br> 
	 * 	
	 * 	1.1 创建所有设备的devicejson </br> 
	 * 	1.2 启动hub > tmp\hub.log,通过端口是否占用判断启动情况并保存PID</br> 
	 * 	1.3 启动appium node > tmp\deviceNameAppiumServer.log,通过端口是否占用判断启动情况并保存PID </br>
	 *  1.4 将PID保存到serverPID </br>
	 *  
	 *  2.iOS </br>
	 *  2.1 获取端口，启动一个appium server
	 *  
	 */
	public static boolean startAppiumServer() throws InterruptedException {
		
		if(AppiumServerConfig.client.endsWith("Android")||AppiumServerConfig.client.endsWith("android")){
			// 生产node.json 获取返回的DeviceJsonBeanList
			//List<DeviceJsonBean> allDeviceJson = JsonUtil.createAllDeviceJson();
			List<DeviceJsonBean> allDeviceJson = getDeviceJsonBeanListFromTmp();
			
			if (allDeviceJson.isEmpty()) {
				System.out.println("系统无可用移动设备，请检查配置文件和设备连接情况！！");
				return false;
			}
	
			// 启动hub 启动node 保存log 并返回PID
			String hubHost = "127.0.0.1";
			int hubPort = 8888;
			boolean hubServerRunFlag = false;
			for (DeviceJsonBean deviceJsonBean : allDeviceJson) {
	
				Configuration configuration = deviceJsonBean.getConfiguration();
	
				hubHost = configuration.getHubHost();
				hubPort = configuration.getHubPort();
				String seleniumServerStandalone = CmdUtil.getInstance()
						.getSeleniumServerStandAloneJar();
				if (!hubServerRunFlag) {
					String cmd = "java -jar libs" + File.separator
							+ seleniumServerStandalone + " -role hub -host "
							+ hubHost + " -port " + hubPort + " -log tmp"
							+ File.separator + "hub.log";
					// CmdUtil.getInstance().execCmd(cmd);
					Server hub = new Server();
					hub.cmd = cmd;
					hub.start();
					
					System.out.println("运行命令："+cmd);
					System.out.println("selenium hub启动中~~~");
					
					
					long startTime = System.currentTimeMillis();
					while(true){
						Thread.sleep(2000);
						if (PortUtil.isPortUsed(hubPort)) {
							hubServerRunFlag = true;
							// 获取PID
							int hubPID = PortUtil.getPidByPort(hubPort);
							serverPID.add(hubPID);
							System.out.println("selenium hub启动成功！");
							break;
						}
						long endTime = System.currentTimeMillis();
						if((endTime-startTime)>AppiumServerConfig.serverStartUpTimeOut){
							System.out.println("selenium hub 启动超时！端口号：" + hubPort
									+ ",请检查端口以及日志~~");
							break;
						}
					}			
					
				}
				//若hub启动失败  退出
				if(!hubServerRunFlag){
					return false;
				}
				List<Capabilities> capabilities = deviceJsonBean.getCapabilities();
				Capabilities capabilitie = capabilities.get(0);
				String deviceName = capabilitie.getBrowserName();
				int port = configuration.getPort();
				String cmd = "appium -a " + configuration.getHost() + " -U \""
						+ deviceName + "\" -p " + port + " --nodeconfig tmp"
						+ File.separator + deviceName.replaceAll(":", ".") + ".json" + " > tmp"
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
					if (PortUtil.isPortUsed(port)) {
						// 获取PID
						int hub = PortUtil.getPidByPort(port);
						serverPID.add(hub);
						System.out.println(deviceName + " appium server启动成功！");
						break;
					}
					long endTime = System.currentTimeMillis();
					if((endTime-startTime)>AppiumServerConfig.serverStartUpTimeOut){
						System.out.println("appium server 启动失败！设备名：" + deviceName.replaceAll(":", ".")
								+ "端口号：" + port);
						stopAppiumServer();
						break;
					}
				}
			}

		}else{
			List<Integer> portList = PortUtil.getAvailablePortList(1);
			int port = portList.get(0);
			
			// 创建临时文件夹tmp
			File tmp = new File("tmp");
			if (tmp.exists()) {
				FileUtil fu = new FileUtil();
				fu.deleteFile(tmp);
			}
			tmp.mkdir();
			
			String cmd = "appium -a " + AppiumServerConfig.serverHost+" -p " + port + " > tmp"+ File.separator+ "AppiumServer.log";
			//CmdUtil.getInstance().execCmd(cmd);
			Server s = new Server();
			s.cmd=cmd;
			s.start();
			
			System.out.println("运行命令："+cmd);
			System.out.println("appium server启动中~~~~");
			
			Thread.sleep(AppiumServerConfig.serverStartUpTimeOut);
			if (PortUtil.isPortUsed(port)) {
				// 获取PID
				int pid = PortUtil.getPidByPort(port);
				serverPID.add(pid);
				System.out.println(" appium server启动成功！");
			} else {
				System.out.println("appium server 启动失败！" + "端口号：" + port+",请查看tmp文件夹下相关日志！");
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
	/**
	 * 读取tmp文件夹下所有json文件 并转换成DeviceJsonBean实体对象
	 * @return  List<DeviceJsonBean>
	 */
	public static List<DeviceJsonBean> getDeviceJsonBeanListFromTmp(){
		List<DeviceJsonBean> list = new ArrayList<>();
		File tmp = new File("tmp");
		File[] files = tmp.listFiles();
		for (File file : files) {
			if(file.isFile() && file.getName().contains("json")){
				File jsonFile = new File("tmp"+File.separator+file.getName());
				try {
					InputStreamReader read = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(read);
					String json = "";
					String lineText = "";
					while((lineText = bufferedReader.readLine()) != null){
						json+=lineText;
					}
					Gson g = new Gson();
					list.add(g.fromJson(json, DeviceJsonBean.class));
				} catch (Exception e) {
					System.out.println("读取json文件出错！"+e.getMessage());
				}
			}
		}
		
		return list;
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
