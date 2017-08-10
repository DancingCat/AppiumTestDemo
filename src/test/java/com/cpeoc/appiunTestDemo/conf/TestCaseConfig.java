package com.cpeoc.appiunTestDemo.conf;
/**
 * 测试用例配置文件
 * @author ken
 * @date 2017-7-19
 * @see
 * 	1.用例默认运行test包下所有class，如：com.cpeoc.appiunTestDemo.test     </br>
 * 	2.可配置运行测试的组/包和不运行的组/包   </br>
 * 	3.不控制到method，有需要请在代码中控制 </br>
 * 	4.请务必严格参照配置说明进行配置！！！！！ </br>
 */
public interface TestCaseConfig {
	
	/** 代码所在文件夹，可选main 或 test */
	String whereIsTestCode = "test";
	
	/** 默认运行组，可为空，多个组之间以逗号分隔 */
	String defaultExcGroup= "smoke,wode";
	
	/** 不包含的组，可为空，多个组之间以逗号分隔 */
	String excludeGroups="shangcheng";
	
	/** 默认运行的包，必填;不支持多个包；此包应包含excludePackages */
	String defaultPackage = "com.cpeoc.appiunTestDemo.test";
	
	/** 不包含的包，可为空，多个包之间以逗号分隔 */	
	String excludePackages="com.cpeoc.appiunTestDemo.test.mine";
	
	
}
