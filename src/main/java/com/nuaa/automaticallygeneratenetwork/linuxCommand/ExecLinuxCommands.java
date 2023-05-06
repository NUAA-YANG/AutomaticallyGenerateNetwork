package com.nuaa.automaticallygeneratenetwork.linuxCommand;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-04 11:09
 * @Java-version jdk1.8
 */
@Service
@Component
//执行服务器cmd命令
public class ExecLinuxCommands {

    /**
     * @description  执行Linux命令
     * @date 2023/5/4 14:58
     * @params [session传递连接对话, commands传递命令集合]
     * @returns List<String> 返回命令执行结果
     */
    public List<String> getCmdResult(Session session , List<String> commands) throws InterruptedException {
        //用来存放命令的返回值
        List<String> cmdResult = new ArrayList<>();
        for (String command : commands) {
            Channel channel = null;
            try {
                //创建执行通道
                channel = session.openChannel("exec");
                //设置命令
                ((ChannelExec) channel).setCommand(command);
                //连接通道
                channel.connect();
                //读取通道的输出
                InputStream in = channel.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //存放命令的执行结果，如果结果有很多行，则每次line的值不同
                String line;
                //lines用来拼接line结果
                StringBuffer lines = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    //去除头尾的空格
                    line.trim();
                    lines = lines.append(line);
                }
                //如果命令执行没有返回值，则直接输出没有返回值
                if (String.valueOf(lines).equals("")){
                    cmdResult.add("命令执行成功,但没有返回值");
                    //cmdResult.add("命令["+command+"]执行成功,但没有返回值");
                }else {
                    //否则将每行返回直接存入到list中
                    cmdResult.add(String.valueOf(lines));
                }
                reader.close();
                channel.disconnect();
            } catch (JSchException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //每次运行完一条命令休眠一会儿，让服务器响应
            Thread.sleep(500);
        }
        return cmdResult;
    }
}
