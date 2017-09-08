package com.cpeoc.appiunTestDemo.test.yy;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.cpeoc.appiunTestDemo.utils.TestNGListener;

@Listeners({TestNGListener.class})
public class TestBaseDriver extends BaseDriver {
	
	
	@Test
	public void test(){
		System.out.println(driver);
		System.out.println(".....");
		Assert.fail();
	}
}
