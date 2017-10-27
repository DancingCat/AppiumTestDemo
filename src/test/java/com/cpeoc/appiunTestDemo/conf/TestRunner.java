package com.cpeoc.appiunTestDemo.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.cpeoc.appiunTestDemo.utils.AppiumServerUtil;
/**
 * 
 * 运行/测试入口类
 * 
 * @author ken
 * @date 2017-10-25
 *
 */
public class TestRunner {
	
	/**
	 * 启动appiumserver
	 * @throws InterruptedException
	 */
	@BeforeTest
	public void startServer() throws InterruptedException{
		
		boolean startAppiumServer = AppiumServerUtil.startAppiumServer();
		if(!startAppiumServer){
			Assert.fail("服务端启动失败！");
		}
		
	}
	
	/**
	 * 运行tesng.xml
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void excTestngXml() throws ClassNotFoundException{
		
		TestNG testNG = new TestNG();
		List<String> suites = new ArrayList<String>();
		suites.add(System.getProperty("user.dir")+File.separator+"tmp"+File.separator+"testng.xml");
		testNG.setTestSuites(suites);

		testNG.setOutputDirectory("tmp"+File.separator+"test-output");
		testNG.run();

	}
	/**
	 * 停止appium server
	 */
	@AfterTest
	public void stopServer(){
		boolean stopAppiumServer = AppiumServerUtil.stopAppiumServer();
		if(!stopAppiumServer){
			Assert.fail("服务端关闭失败！");
		}
	}

	
}
