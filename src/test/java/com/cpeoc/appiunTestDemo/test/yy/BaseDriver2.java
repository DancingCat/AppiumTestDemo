package com.cpeoc.appiunTestDemo.test.yy;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.utils.DeviceUtil;

import io.appium.java_client.AppiumDriver;
/**
 * 测试基类
 * @author ken
 * @date
 * 	2017-8-11
 *@see
 *	1.测试基础类，包含公共方法封装 </br>
 */
public class BaseDriver2 {
	public AppiumDriver<WebElement> driver;
	public WebDriverWait wait;
	public String client = AppiumServerConfig.client.trim().toLowerCase();
	public boolean realDevice = AppiumServerConfig.realDevice;
		
	//@BeforeTest
	public void setUp() throws Exception {
		
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "app");
		File app = new File(appDir, "Album_netease.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "fe3123d2"); // "Android Emulator"
		capabilities.setCapability("platformVersion", 6.0);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appPackage", "com.netease.cloudalbum");
		capabilities.setCapability("platformName", "Android");
		// capabilities.setCapability("noReset", true);
		//iOS配置 realDevice
		if(client.equals("ios")){
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("automationName", "XCUITest");
			if(realDevice){
				capabilities.setCapability("udid", DeviceUtil.getIOSRealDeviceUdidByDeviceName("fe3123d2"));
			}
		}
		
		driver = new AppiumDriver<>(new URL("http://127.0.0.1:"+9000+"/wd/hub"),
				capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//设置等待时间30s
		wait = new WebDriverWait(driver,30);
	}

	//@AfterTest(alwaysRun = true)
	public void tearDown() throws Exception {
		if (driver != null) {
			driver.quit();
		}
	}

}
