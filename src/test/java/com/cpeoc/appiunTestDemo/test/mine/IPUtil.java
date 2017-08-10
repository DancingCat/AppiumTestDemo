package com.cpeoc.appiunTestDemo.test.mine;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IP地址获取工具类
 * 
 * @author ken
 * @date 2017-7-20
 */
public class IPUtil {
	/**
	 * 获取本机本地IP地址
	 * @return  IP地址列表
	 * @see
	 * 	1.获取所有网卡 </br>
	 * 	2.获取网卡IP </br>
	 */
	public static List<String> getLocalIpAddress(){
		List<String> IPList = new ArrayList<String>();
		try {

			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				// System.out.println("网卡名称"+netInterface.getName());
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						// System.out.println("本机的IP = " + ip.getHostAddress());
						IPList.add(ip.getHostAddress());
					}
				}
			}			
		} catch (Exception e) {
			System.out.println("获取本机IP出错！");
			System.out.println(e.getMessage());
		}
		return IPList;	
	}
}
