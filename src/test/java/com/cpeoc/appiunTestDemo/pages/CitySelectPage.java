package com.cpeoc.appiunTestDemo.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.cpeoc.appiunTestDemo.pageObj.CitySelectPageObj;

public class CitySelectPage {
	
	CitySelectPageObj cspo = new CitySelectPageObj();
	
	AppiumDriver<WebElement> driver;
	
	public CitySelectPage(AppiumDriver<WebElement> driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(this.driver, 5, TimeUnit.SECONDS), cspo);
	}
	
	public boolean isSearchBarDisplay(){
		return cspo.searchBar.isDisplayed();
	}
	
	public boolean isMessageDisplay(){
		return cspo.message.isDisplayed();
	}
	
}
