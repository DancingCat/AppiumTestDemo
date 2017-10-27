package com.cpeoc.appiunTestDemo.test;

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
	public void swipeIntoHome() {
		CitySelectPage csp = new CitySelectPage(driver);	
		GuidePage gp = new GuidePage(driver);
		gp.swipeIntoHome();
		Assert.assertTrue(csp.isMessageDisplay());
	}

}
