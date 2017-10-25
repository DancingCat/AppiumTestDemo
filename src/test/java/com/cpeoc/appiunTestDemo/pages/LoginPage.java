package com.cpeoc.appiunTestDemo.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.PageFactory;

import com.cpeoc.appiunTestDemo.utils.BaseDriver;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPage extends BaseDriver {
	
	public LoginPage(){
		PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS), this);
	}
	
	
}
