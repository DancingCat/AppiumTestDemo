package com.cpeoc.appiunTestDemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.cpeoc.appiunTestDemo.conf.TestCaseConfig;

/**
 * 文件操作工具类
 * @author ken
 * @date 2017-7-21
 *
 */
public class FileUtil {
	
	List<String> javaFileList = new ArrayList<String>();
	
	/**
	 * 删除文件夹
	 * @param file 
	 */
	public static void deleteFile(File file){
		if(file.isDirectory()){
			for (File f : file.listFiles()) {
				deleteFile(f);
			}
		}
		file.delete();
	}
	/**
	 * 获取测试用例的类全路径名
	 * @return  TestCaseQualifiedName List
	 * @see
	 * 	1.类似com.cpeoc.appiunTestDemo.test.XXX </br>
	 */
	public  List<String> getAllTestCaseQualifiedName(){
		//1.确定文件夹路径
		String userDir = System.getProperty("user.dir");
		String pkgPath = TestCaseConfig.defaultPackage.replaceAll("\\.", "\\"+File.separator);
		String path = userDir +File.separator+"src"+File.separator+TestCaseConfig.whereIsTestCode+File.separator+"java"+File.separator+pkgPath;
		
		
		List<String> qualifiedNameList = new ArrayList<>();		
		try {
			//2.获取java文件
			getAllJavaFile(path);				
			//3.读取类全路径			
			for (String javaFile : javaFileList) {
				//4.判断是否包含@Test
				File jFile = new File(javaFile);
				String readFileToString = FileUtils.readFileToString(jFile, "UTF-8");
				//5.判断是否包含excludePackages 和excludeGroups
				String[] splitGroups = TestCaseConfig.excludeGroups.split(",");
				String[] splitPkgs = TestCaseConfig.excludePackages.split(",");
				boolean containExcludeGroups = false;
				boolean containExcludePkgs = false;
				//不等于空			
				if(!StringUtils.isAnyEmpty(splitPkgs)){
					for (String pkg : splitPkgs) {
						if(readFileToString.contains(pkg)){
							containExcludePkgs = true;
						}
					}
				}
				if(!StringUtils.isAnyEmpty(splitGroups)){
					for (String group : splitGroups) {
						if(readFileToString.contains(group)){
							containExcludeGroups = true;
						}
					}
				}
			
				if(readFileToString.contains("@Test") && !containExcludeGroups && !containExcludePkgs){
					InputStreamReader read = new InputStreamReader(new FileInputStream(jFile), "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(read);
					//获取包名 读取第一行  类似package com.cpeoc.appiunTestDemo.utils;
					String readLine = bufferedReader.readLine();
					String[] splits = readLine.split(" ");
					String pkg = splits[splits.length-1];
					pkg  = pkg.substring(0,pkg.length()-1);
					
					//获取类名
					String className[] = javaFile.split( "\\"+File.separator);
					String cName[] =className[className.length-1].split("\\.");
					qualifiedNameList.add(pkg+"."+cName[0]);//类名
				}					
			}
		} catch (IOException e) {
			System.out.println("读取目录下java文件出错！"+e.getMessage());
		}
		return qualifiedNameList;	
	}
	
	/**
	 * 获取指定路径下所有java文件
	 * @return  Java文件全路径 列表
	 * @throws IOException  
	 */
	public  void getAllJavaFile(String path) throws IOException{
		
		File directory = new File(path);
		File[] listFiles = directory.listFiles();
		
		for (File file : listFiles) {
			if(file.isFile() && file.getName().contains("java")){
				javaFileList.add(file.getCanonicalPath());
			}else{
				getAllJavaFile(file.getCanonicalPath());
			}
		}
		
	}
	
}
