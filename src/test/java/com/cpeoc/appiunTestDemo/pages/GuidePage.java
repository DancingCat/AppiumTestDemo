package com.cpeoc.appiunTestDemo.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cpeoc.appiunTestDemo.pageObj.GuidePageObj;

public class GuidePage {

	GuidePageObj gpo = new GuidePageObj();
	AppiumDriver<WebElement> driver;
	
	public GuidePage(AppiumDriver<WebElement> driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(this.driver, 5, TimeUnit.SECONDS), gpo);
	}

	
	@SuppressWarnings("deprecation")
	public void swipeIntoHome(){	
		System.out.println("swipe begin ...............");
		WebDriverWait wait= new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOf(gpo.guide));		
//		driver.swipe(400, 800, 200, 0, 200);
//		driver.swipe(400, 800, 200, 0, 400);
//		driver.swipe(600, 800, 200, 0, 200);
		
		try {

		TouchAction swipe = new TouchAction(driver).press(400, 800).waitAction(200).moveTo(100, 800).release();	 
		swipe.perform();
		Thread.sleep(200);
		swipe.perform();
		Thread.sleep(200);
		swipe.perform();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("引导页滑动出错");
		}
		gpo.startBtn.click();		
	}
	
	
	
}
