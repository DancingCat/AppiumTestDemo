package com.cpeoc.appiunTestDemo.utils;
/**
 * node  json实体类相关类
 * @author ken
 * @date 2017-7-19
 * 
 */
public class Configuration {
	int cleanUpCycle=2000;
	int timeout=30000;
	String proxy="org.openqa.grid.selenium.proxy.DefaultRemoteProxy";
	/** webdriver url */
	String url;
	/** webdriver host */
	String host;
	/** webdriver port */
	int port;
	int maxSession=1;
	boolean register =true;
	int registerCycle = 5000;
	/** selenium grid  hubport */
	int hubPort;
	/** selenium grid  hubhost */
	String hubHost;
	
	public int getCleanUpCycle() {
		return cleanUpCycle;
	}
	public void setCleanUpCycle(int cleanUpCycle) {
		this.cleanUpCycle = cleanUpCycle;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMaxSession() {
		return maxSession;
	}
	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}
	public boolean isRegister() {
		return register;
	}
	public void setRegister(boolean register) {
		this.register = register;
	}
	public int getRegisterCycle() {
		return registerCycle;
	}
	public void setRegisterCycle(int registerCycle) {
		this.registerCycle = registerCycle;
	}
	public int getHubPort() {
		return hubPort;
	}
	public void setHubPort(int hubPort) {
		this.hubPort = hubPort;
	}
	public String getHubHost() {
		return hubHost;
	}
	public void setHubHost(String hubHost) {
		this.hubHost = hubHost;
	}
	
	
	
}
