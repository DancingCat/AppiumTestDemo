package com.cpeoc.appiunTestDemo.test.yy;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MyFirstTest {
	
	
	@Test
	public void test001(){
		System.out.println("test001测试运行成功，请查看测试报告");
		Assert.assertTrue(true);
	}
	
	@Test
	public void test002(){
		System.out.println("test002测试运行成功，请查看测试报告");
		Assert.assertTrue(true);
	}
	
	@Test
	public void test003(){
		System.out.println("test003测试运行成功，请查看测试报告");
		Assert.assertTrue(true);
	}
}
