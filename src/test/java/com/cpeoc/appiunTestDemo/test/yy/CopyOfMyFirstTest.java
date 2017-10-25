package com.cpeoc.appiunTestDemo.test.yy;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CopyOfMyFirstTest {
	
	
	@Test
	public void test004(){
		System.out.println("test004测试运行成功，请查看测试报告");
		Assert.assertTrue(true);
	}
	
	@Test
	public void test005(){
		System.out.println("test005测试运行成功，请查看测试报告");
		Assert.assertTrue(true);
	}
	
	@Test
	public void test006(){
		System.out.println("test006测试运行成功，请查看测试报告");
		Assert.assertTrue(false);
	}
}
