package com.cpeoc.appiunTestDemo.utils;

import java.util.ArrayList;
import java.util.List;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;

/**
 * 端口工具类
 * 
 * @author ken
 * @date 2017-7-17
 * 
 */
public class PortUtil {
	/**
	 * 判断端口是否被占用
	 * 
	 * @param portNum
	 *            端口号
	 * @return 返回布尔值 true表示被占用或异常 false表示端口空闲
	 */
	public static Boolean isPortUsed(int portNum) {
		List<String> ports = new ArrayList<String>();
		try {
			// 不严谨，但是可用
			String cmdStr = System.getProperty("os.name").contains("Windows") ? "findstr"
					: "grep";
			ports = CmdUtil.getInstance().execCmd(
					"netstat -ano|" + cmdStr + " " + portNum);
			return ports.size() > 0 ? true : false;
		} catch (Exception e) {
			System.out.println("获取端口占用情况失败!=");
			return true;
		}
	}

	/**
	 * 获取指定数量的可用端口号
	 * 
	 * @param portAmount
	 *            所需端口总数
	 * @return 可用端口列表
	 */
	public static List<Integer> getAvailablePortList(int portAmount) {
		List<Integer> avPortList = new ArrayList<>();
		int i = AppiumServerConfig.startPortNum;
		int j = 1;
		while (i < 65536) {
			if (!isPortUsed(i)) {
				avPortList.add(i);
				if (j == portAmount) {
					break;
				}
				j++;
			}
			i++;
		}
		return avPortList;
	}

	/**
	 * 根据端口号获取进程ID
	 * 
	 * @param port
	 *            端口号
	 * @return PID 进程ID
	 * @see 1.mac os确保lsof已安装 </br>
	 */
	public static int getPidByPort(int port) {
		String os = System.getProperty("os.name");
		String cmd = os.contains("Windows") ? "netstat -ano | findstr " + port
				: "lsof -i:" + port;
		List<String> lines = CmdUtil.getInstance().execCmd(cmd);
		// mac os 使用lsof -i:port
		// 结果取第二行 第五个

		if (os.contains("Windows")) {
			String[] split = lines.get(0).split(" ");
			return Integer.parseInt(split[split.length - 1]);
		} else {
			String[] split = lines.get(1).split(" ");
			return Integer.parseInt(split[4]);
		}

	}

}
