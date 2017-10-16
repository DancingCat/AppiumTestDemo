package com.cpeoc.appiunTestDemo.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cpeoc.appiunTestDemo.conf.AppiumServerConfig;
import com.cpeoc.appiunTestDemo.conf.TestCaseConfig;

/**
 * XML工具类
 * 
 * @author ken
 * @date 2017-7-19
 * @see 1.主要用来生成testng.xml配置文件 </br>
 */
public class XmlUtil {
	
	static boolean parallel = AppiumServerConfig.parallel ;
	static boolean isIOS = AppiumServerConfig.client.toLowerCase().equals("ios");
	
	// 读取配置文件 client iOS铁定串行 Android另行判断
	@SuppressWarnings("unused")
	public static void creatTestNGXML(List<Device> deviceList) {
		// 创建空实例
		Document document = DocumentHelper.createDocument();

		// 添加 DocType <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
		document.addDocType("suite", null, "http://testng.org/testng-1.0.dtd");

		// 获取工程名
		String userDir = System.getProperty("user.dir");
		String projectName = userDir.substring(
				userDir.lastIndexOf(File.separator) + 1, userDir.length());

		// 添加suite 节点
		Element suite = document.addElement("suite");
		// 属性 name = "AppiumTestDemo"
		suite.addAttribute("name", projectName);
		
		// 判断parallel
		if (parallel && !isIOS ) {
			suite.addAttribute("parallel", "tests");
			suite.addAttribute("thread-count", deviceList.size() + "");
		}

		// 用例分配 如果模块数量>= 设备数量 按模块分配；否则按class分配
		// 用例分配采用平均分配算法
		// test下建议使用模块划分用例

		List<String> moduleList = getAllModule();
		
		int moduleSize = moduleList.size();
		int deviceSize = deviceList.size();
		if (moduleSize >= deviceSize || isIOS) {
			document = creatXMLByModule(deviceList, suite, document);
		} else {
			document = creatXMLByClass(deviceList, suite, document);
		}
		
		// 文档中含有中文,设置编码格式写入的形式.
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8"); // 指定XML编码
		try {
			XMLWriter writer = new XMLWriter(new FileWriter("tmp"
							+ File.separator + "testng.xml"), format);
					writer.write(document);
					writer.close();

		} catch (Exception e) {
			System.out.println("生成testng.xml配置文件出错~~，错误信息：" + e.getMessage());
		}
	}

	/**
	 * 根据class分配生成testng.xml
	 * 
	 * @param allDeviceJson
	 * @param suite
	 * @param document
	 *            文档
	 * @see
	 * 	1.读取配置defaultPackage下所有Class，返回List QualifiedName  </br>
	 */
	public static Document creatXMLByClass(List<Device> deviceList,
			Element suite, Document document) {
		
		FileUtil fu = new FileUtil();
		List<String> allTestCaseQualifiedNameList = fu.getAllTestCaseQualifiedName();
		
		//有一种极端情况  class数少于设备数
		if(deviceList.size()>allTestCaseQualifiedNameList.size() && !parallel){
			//以数量少的为单位循环
			for (int i=0;i<allTestCaseQualifiedNameList.size();i++) {
				String testCaseQualifiedName = allTestCaseQualifiedNameList.get(i);
				Device device = deviceList.get(i);
				//新增test  新增device info parameter
				Element test = deviceIntoTestElement(device,suite);
				
				//添加classes
				Element classes = test.addElement("classes");
				Element cla = classes.addElement("class");
				cla.addAttribute("name", testCaseQualifiedName);
	
			}
			return document;
		}
		
		// suite下添加子节点
		for (Device device : deviceList) {
			//新增test  新增device info parameter
			Element test = deviceIntoTestElement(device,suite);
			
			//添加classes
			Element classes = test.addElement("classes");
			
			//获取平均分配				
			//如果parallel=true，每个设备用例都一样，false时才采用平均分配算法
			if(parallel){
				for (String testCaseQualifiedName : allTestCaseQualifiedNameList) {
					Element cla = classes.addElement("class");
					cla.addAttribute("name", testCaseQualifiedName);
				}
			}else{
				List<String> deviceNameList = DeviceUtil.getDeviceNameList(deviceList);
				Map<String, List<String>> deviceClassMap = allotOfAverage(deviceNameList,allTestCaseQualifiedNameList);
	
				String deviceName = device.getDeviceName();
				List<String> testClassList = deviceClassMap.get(deviceName);			
//				//防止class少于设备数时，部分设备没有分到class导致空指针异常
//				if(null!=testClassList){
					//添加class
					for (String testClass : testClassList) {
						Element cla = classes.addElement("class");
						cla.addAttribute("name", testClass);
					}
//				}				
			}

		}

		return document;
	}

	/**
	 * 根据模块分配生成testng.xml
	 * 
	 * @param allDeviceJson
	 * @param suite
	 * @param document
	 *            文档
	 */
	public static Document creatXMLByModule(List<Device> deviceList,
			Element suite, Document document) {
		//获取平均分配
		List<String> allModule = getAllModule();
		List<String> deviceNameList = DeviceUtil.getDeviceNameList(deviceList);
		Map<String, List<String>> deviceModuleMap = allotOfAverage(deviceNameList,allModule);
		// suite下添加子节点
		for (Device device : deviceList) {
			//新增test  新增device info parameter
			Element test = deviceIntoTestElement(device,suite);

			// 添加groups
			String defaultexcgroup = TestCaseConfig.defaultExcGroup.trim();
			String excludegroups = TestCaseConfig.excludeGroups.trim();
			if (defaultexcgroup != "" || excludegroups != "") {

				Element groups = test.addElement("groups");
				Element run = groups.addElement("run");

				// 获取 defaultExcGroup List 和 excludeGroups List
				if (defaultexcgroup != "") {
					String[] gps = defaultexcgroup.split(",");
					for (String gp : gps) {
						Element include = run.addElement("include");
						include.addAttribute("name", gp);
					}

				}

				if (excludegroups != "") {
					String[] gps = excludegroups.split(",");
					for (String gp : gps) {
						Element exclude = run.addElement("exclude");
						exclude.addAttribute("name", gp);
					}
				}
			}
			// 添加packages
			Element packages = test.addElement("packages");
			
			
			//当parallel=false时，采用平均分配算法		
			if(!parallel && !isIOS){				
				String deviceName = device.getDeviceName();
				List<String> muduleList = deviceModuleMap.get(deviceName);
				for (String module : muduleList) {
					Element pkg = packages.addElement("package");
					pkg.addAttribute("name", module);
					writeExcludePakage(pkg,module);
				}
			}else{
				for (String module : allModule) {
					Element pkg = packages.addElement("package");
					pkg.addAttribute("name", module);
					writeExcludePakage(pkg,module);
				}
			}
			
			

		}
		return document;
	}
	/**
	 * 写入package的exclude
	 * @param pkg
	 * @param moduleName
	 * @see	
	 * 	1.如果excludePkg包含了模块，则在模块下写入exclude
	 */
	public static void writeExcludePakage(Element pkg,String moduleName){
		String excludepackages = TestCaseConfig.excludePackages.trim();
		if (excludepackages != "") {
			String[] pkgs = excludepackages.split(",");
			for (String pk : pkgs) {
				if(pk.contains(moduleName)){
					Element exclude = pkg.addElement("exclude");
					exclude.addAttribute("name", pk);
				}
				
			}
		}
	}
	
	/**
	 * 新增test节点  新增device info parameter
	 * 
	 * @param deviceJsonBean  
	 * @param suite  suite节点Element
	 * @return  test 节点Element
	 */
	public static Element deviceIntoTestElement(Device device,Element suite){

		String deviceName = device.getDeviceName();
		// 添加test节点
		Element test = suite.addElement("test");
		test.addAttribute("name", deviceName);
		// 添加parameter deviceName
		Element parameter1 = test.addElement("parameter");
		parameter1.addAttribute("name", "deviceName");
		parameter1.addAttribute("value", deviceName);
		// 添加parameter platformVersion
		Element parameter2 = test.addElement("parameter");
		parameter2.addAttribute("name", "platformVersion");
		parameter2.addAttribute("value", device.getSysVersion());
		// 添加parameter port  iOS端口写hubport
		Element parameter3 = test.addElement("parameter");
		parameter3.addAttribute("name", "port");
		if(!isIOS){
			parameter3.addAttribute("value", device.getServerPort() + "");
		}else{
			parameter3.addAttribute("value", device.getBoostrapPort() + "");
		}

		return test;
	}	
	
	/**
	 * 获取TestCaseConfig.defaultPackage 下级目录
	 * 
	 * @return 所有下级目录List </br> 
	 * 			如：默认包名 + 模块名 com.xxxx.xxx.text.moduleName
	 * @date 2017-7-28
	 * @see 
	 * 	1.获取配置文件配置的测试默认包名 </br> 
	 * 	2.获取配置文件配置的所在目录 </br>
	 *  3.返回目录（模块）列表，建议测试用例以模块划分 </br>
	 */
	public static List<String> getAllModule() {
		String currentPath = System.getProperty("user.dir");
		String path = TestCaseConfig.defaultPackage.replaceAll("\\.", "\\"
				+ File.separator);
		currentPath = currentPath + File.separator + "src" + File.separator
				+ TestCaseConfig.whereIsTestCode + File.separator + "java"
				+ File.separator + path;
		String sysName = System.getProperty("os.name");
		String cmd = sysName.contains("Windows") ? "dir " + currentPath
				: "ls -l " + currentPath;
		List<String> dirs = CmdUtil.getInstance().execCmd(cmd);

		List<String> moduleList = new ArrayList<String>();
		// windows下取包含<DIR>字符的行 linux下取d开头的行
		if (sysName.contains("Windows")) {
			//第一行和第二行不取
			for (String line : dirs) {
				if (line.contains("DIR")) {
					String[] directorys = line.split(" ");
					if(!directorys[directorys.length-1].contains(".")){
						// 默认包名 + 模块名 com.xxxx.xxx.text.moduleName
						moduleList.add(TestCaseConfig.defaultPackage+"."
								+ directorys[directorys.length-1]);
					}					
				}
			}
		} else {
			for (String line : dirs) {
				if (line.startsWith("d")) {
					String[] directorys = line.split(" ");
					// 默认包名 + 模块名 com.xxxx.xxx.text.moduleName
					moduleList.add(TestCaseConfig.defaultPackage+"."
							+ directorys[directorys.length-1]);
				}
			}
		}
		//删除excludePackage
		String excludepackages = TestCaseConfig.excludePackages.trim();
		String[] excludepkgs = excludepackages.split(",");
		for (String module : moduleList) {
			for (String pkg : excludepkgs) {
				if(module.equals(pkg)){
					moduleList.remove(module);
				}
			}
		}
		
		return moduleList;
	}

	/**
	 * 平均分配
	 * 
	 * @param users
	 *            用户
	 * @param tasks
	 *            任务
	 * @return Map key是用户 value是task List
	 * @see 1.利用tasks.size()%users.size() 轮询task 将task保存在对应的user上 </br>
	 *      2.如果已经包含则取出，新增task，如果未包含新增list </br>
	 */
	public static Map<String, List<String>> allotOfAverage(List<String> users,
			List<String> tasks) {
		// 保存分配的信息
		Map<String, List<String>> allot = new ConcurrentHashMap<String, List<String>>(); 
		if (users != null && users.size() > 0 && tasks != null
				&& tasks.size() > 0) {
			for (int i = 0; i < tasks.size(); i++) {
				int j = i % users.size();
				if (allot.containsKey(users.get(j))) {
					List<String> list = allot.get(users.get(j));
					list.add(tasks.get(i));
					allot.put(users.get(j), list);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(tasks.get(i));
					allot.put(users.get(j), list);
				}
			}
		}
		return allot;
	}

}
