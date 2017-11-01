package com.cpeoc.appiunTestDemo.utils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.utils.AppiumServerUtil;
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
public class BaseDriver {
	public AppiumDriver<WebElement> driver;
	public WebDriverWait wait;
	public String client = AppiumServerConfig.client.trim().toLowerCase();
	public boolean realDevice = AppiumServerConfig.realDevice;
		
	public  AppiumDriver<WebElement> getDriver(){
		return this.driver;
	}
	
	
	@BeforeSuite
	public void startAppiumServer() {
		try {
			boolean startAppiumServer = AppiumServerUtil.startAppiumServer();
			if(!startAppiumServer){
				Assert.assertTrue(false,"appium Server启动失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false,"appium Server启动失败！");
		}		
	}
	
	@AfterSuite(alwaysRun = true)
	public void stopAppiumServer(){
		try {
			boolean stopAppiumServer = AppiumServerUtil.stopAppiumServer();
			if(!stopAppiumServer){
				Assert.assertTrue(false,"appium Server停止失败！");
			}
		} catch (Exception e) {
			Assert.assertTrue(false,"appium Server停止失败！");
		}	
	}
	
	
	@Parameters({"deviceName","platformVersion","port"})
	@BeforeTest
	public void setUp(String deviceName,String platformVersion,String port) throws Exception {
		
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "app");
		File app = new File(appDir, "Shop.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", deviceName); // "Android Emulator"
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appPackage", "cn.swiftpass.wftpay");
		
		// capabilities.setCapability("noReset", true);
		//iOS配置 realDevice
		if(client.equals("ios")){
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("automationName", "XCUITest");
			if(realDevice){
				capabilities.setCapability("udid", DeviceUtil.getIOSRealDeviceUdidByDeviceName(deviceName));
			}
		}else{
			capabilities.setCapability("platformName", "Android");
		}	
		driver = new AppiumDriver<>(new URL("http://"+AppiumServerConfig.serverHost+":"+port+"/wd/hub"),
				capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//设置等待时间30s
		wait = new WebDriverWait(driver,30);	
		//TestNGListener.driver = driver;
	}

	@AfterTest(alwaysRun = true)
	public void tearDown() throws Exception {
		if (driver != null) {
			driver.quit();
		}
	}

}
