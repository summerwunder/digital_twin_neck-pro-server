
package edu.whut.utils.python;

import edu.whut.constants.ConnectConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 执行python脚本，向远程服务器发送请求
 * 输入参数：力传感器数值
 * 返回值：是否成功运行脚本
 */

@Slf4j
public class PythonHelper {
    /**
     * 执行python脚本发送数据信息
     */
    /*
    public static boolean sendDgramData(Double forceValue){

        String[] arguments=new String[]
                {ConnectConstants.pythonCmdPath,ConnectConstants.pythonFilePath,forceValue.toString()};
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(arguments);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            //输出结果
            while ((line = in.readLine()) != null) {
                log.info("python脚本输出结果为--->{}",line);
                //此处数据应有服务器进行相应
                if(line.equals("数据接收成功")){
                    return true;
                }
            }
            in.close();
            //waitFor是用来显示脚本是否运行成功，1表示失败，0表示成功，还有其他的表示其他错误
            if(proc.waitFor()==0){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    */

}

