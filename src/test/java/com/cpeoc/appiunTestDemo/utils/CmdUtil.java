package com.cpeoc.appiunTestDemo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * cmd命令工具类
 * @author ken
 * @date 2017-7-17
 * 
 */

public class CmdUtil {

    private static CmdUtil cmdUtil;

    private Runtime runtime = Runtime.getRuntime();
    /**
     * CmdUtil 单例模式
     * @return CmdUtil对象实例
     */
    public static  CmdUtil getInstance(){
        if(cmdUtil == null){
            cmdUtil = new CmdUtil();
        }

        return cmdUtil;
    }


    /**
     * 运行cmd，并且返回结果
     *
     * @param command 要运行的命令
     * @return List 命令行运行结果逐行保存在List中
     * @see
     * 	1.根据运行系统执行不同命令</br>
     * 	2.考虑到某些命令有管道、重定向等操作，使用exec(String [] cmdArray)</br>
     * 
     */
    public List<String> execCmd(String command) {
        if (!command.isEmpty()) {

            BufferedReader br = null;
            try {
                
                Process process = null;
                	String os = System.getProperty("os.name");
                	String []cmdArray = new String[]{ "/bin/bash", "-c", command};
//                	Map<String, String> envMap = System.getenv();
//            		String[] envp = new String[envMap.size()];            		
//            		try{
//            		
//            			//System.arraycopy(envMap, 0, envp, 0, envMap.size());
//            			Iterator<Entry<String, String>> iterator = envMap.entrySet().iterator();
//            			int i= 0;
//            			while(true){
//            				
//            				if(iterator.hasNext()){
//            					Entry<String, String> next = iterator.next();
//            					envp[i] = next.getKey()+"="+next.getValue();
//            					i++;
//            				}else{
//            					break;
//            				}
//            			}
//            			
//            		}catch(Exception e){
//            			System.out.println(e.getMessage());
//            		}
            		if(os.contains("Windows")){
//                		process = runtime.exec("cmd /c " + command,envp);
                		process = runtime.exec("cmd /c " + command);
                }else{
//                		process = runtime.exec(cmdArray,envp);
                		process = runtime.exec(cmdArray);
                }
                	
                br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
                String line = "";

                List<String> content = new ArrayList<String>();

                while ((line = br.readLine()) != null){
                    if (!line.isEmpty()) {
                        content.add(line);
                    }
                }

                //process.destroy();
                return content;
            } catch (Exception e) {
                System.out.println("execCmd执行命令错误!" + e.getMessage());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;
    }
    
    /**
     * 获取项目 目录下libs文件夹下的selenium-server-standalone-xxx.jar
     * @return selenium-server-standalone-xxx.jar
     */
    public String getSeleniumServerStandAloneJar(){
    	String cmd = System.getProperty("os.name").contains("Windows")?"dir libs":"ls libs";
    	List<String> list = CmdUtil.getInstance().execCmd(cmd);
    	String[] split = new String[]{};
    	for (String line : list) {
			if(line.contains("selenium-server-standalone")){
				split = line.split(" ");
			}
		}
    	
    	for (String string : split) {
    		if(string.contains("selenium-server-standalone")){
    			return string;
    		}
		}
    	return "";
    }
    

}
