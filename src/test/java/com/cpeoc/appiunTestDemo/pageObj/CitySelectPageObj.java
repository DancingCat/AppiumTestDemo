package com.cpeoc.appiunTestDemo.pageObj;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

/**
 * 城市选择页
 * @author ken
 * @date 2017-10-27
 *
 */
public class CitySelectPageObj {

	@AndroidFindBy(id = "et_search_bar")
	public MobileElement searchBar;
	
	@AndroidFindBy(id = "message")
	public MobileElement message;
	
}
