
package com.cpeoc.appiunTestDemo.pageObj;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.WithTimeout;


/**
 * 引导页
 * @author ken
 * @date 2017-10-27
 * 
 */
public class GuidePageObj {
	
	@WithTimeout(time = 20, unit = TimeUnit.SECONDS)
	@AndroidFindBy(id="guide_pager")
	public MobileElement guide;
	
	@AndroidFindBy(xpath = "//*[contains(@text,'开启邮惠之旅')]")
	public MobileElement startBtn;
	
	
}
