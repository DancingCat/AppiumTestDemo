package com.cpeoc.appiunTestDemo.test.mine;

public class Test {
	public static void main(String[] args) {
		String a="iPhone 6:8.3";
		String[] split = a.split(",");
		for (String string : split) {
			System.out.println(string);
		}
	}
}
