# 一、简介
## use: appium+maven+testng+Allure2
## support:
	1.Android、iOS
	2.支持安卓并行运行（即支持多机同时执行相同或不同的测试用例）
	3.不支持iOS并行
	4.支持Mac OS和Windows上运行测试用例
	
# 二、工程结构说明
	projectName
	|----------src/test/java
	|------------packageName      
	|--------------conf              配置文件  + 运行测试入口
	|--------------pageObj			  页面
	|--------------pages			  页面功能
	|--------------test				  测试用例
	|----------------MouduleName1	  模块1
	|----------------MouduleName2	  模块2
	|--------------utils             框架工具类
	|----------app                   测试app
	|----------pom.xml  
	             
# 三、使用说明
	1.务必保证是标准的maven工程（可直接在本项目中修改）
	2.务必严格参考配置文件示例进行配置
	3.appium需要支持命令行,建议使用1.6以上版本
	4.需要安装的xxx
	
	
