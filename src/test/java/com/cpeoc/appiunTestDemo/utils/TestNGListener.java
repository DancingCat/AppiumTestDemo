package com.cpeoc.appiunTestDemo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;



import io.appium.java_client.AppiumDriver;
/**
 * 失败截图监听类
 * @author ken
 * @date
 * 	2017-8-11
 *
 */
public class TestNGListener extends TestListenerAdapter {
	
	public static AppiumDriver<WebElement> driver;

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        takeScreenShot(tr);
    }

	public void takeScreenShot(ITestResult tr) {

		File screenshotFile = driver.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(screenshotFile, new File("tmp"+File.separator+tr.getName()+".png"));
		} catch (IOException e) {
			System.out.println("截图失败！用例名称：" + tr.getTestName());
		}
	}

}
