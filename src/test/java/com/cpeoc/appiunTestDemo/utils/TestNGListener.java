package com.cpeoc.appiunTestDemo.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;





import io.qameta.allure.Attachment;
/**
 * 失败截图监听类
 * @author ken
 * @date
 * 	2017-8-11
 *
 */
public class TestNGListener extends TestListenerAdapter {
	
	//public static AppiumDriver<WebElement> driver;

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        takeScreenShot(tr.getTestName(),tr);
    }
    @Attachment(value = "Failure ScreenShot", type = "image/png")
	public  byte[]  takeScreenShot(String methodName,ITestResult tr) {
		BaseDriver driver = (BaseDriver) tr.getInstance();
		return driver.getDriver().getScreenshotAs(OutputType.BYTES);	
	}
}
