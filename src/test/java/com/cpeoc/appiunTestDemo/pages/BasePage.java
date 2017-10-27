package com.cpeoc.appiunTestDemo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
/**
 * 页面基类
 * @author ken
 * @date 2017-10-27
 *	封装基本操作
 */
public class BasePage {

	AndroidDriver  driver;
	WebDriverWait wait;
	
//	
//	public void waitForElementPresent(WebElement element){
//		wait.until(ExpectedConditions.visibilityOf(element));
//		
//	}
//	
}
