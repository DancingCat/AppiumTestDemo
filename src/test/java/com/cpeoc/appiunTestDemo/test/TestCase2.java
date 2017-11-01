package com.cpeoc.appiunTestDemo.test;

import java.io.File;
import java.io.IOException;

import io.qameta.allure.Attachment;
import io.qameta.allure.Description;

import org.openqa.selenium.OutputType;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.cpeoc.appiunTestDemo.pages.CitySelectPage;
import com.cpeoc.appiunTestDemo.pages.GuidePage;
import com.cpeoc.appiunTestDemo.utils.BaseDriver;

/**
 * 本地相册的大图浏览 
 * 前置条件：登录账号
 * 测试点：查看照片信息
 * 		  上传到云端相册
 * 
 * 步骤：登陆  获取云相册相片数量   上传   再次获取云相册相片数量  断言 
 */
public class TestCase2 extends BaseDriver{

	@Test
	@Description("滑动进入首页")
	@Attachment
	public void swipeIntoHome() {
		CitySelectPage csp = new CitySelectPage(driver);	
		GuidePage gp = new GuidePage(driver);
		gp.swipeIntoHome();
		
//		try {
//			takeScreenShot("ssss");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		Assert.assertTrue(csp.isMessageDisplay());
	}

	
//	 @Attachment(value = "Failure ScreenShot", type = "image/png")
//	 private byte[] takeScreenShot(String methodName) throws IOException {
//	       return driver.getScreenshotAs(OutputType.BYTES);
//	 }
}
