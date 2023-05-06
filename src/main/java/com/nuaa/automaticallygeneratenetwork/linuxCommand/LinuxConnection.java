package com.nuaa.automaticallygeneratenetwork.linuxCommand;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author YZX
 * @Create 2023-05-04 11:04
 * @Java-version jdk1.8
 */
@Service
@Component
//获取服务器的连接
public class LinuxConnection {

    /***
     * @description  jsch创建连接
     * @date 2023/5/4 14:56
     * @params [ip地址, port端口号, userName用户名, password密码]
     * @returns Session 对象
     */
    public Session getJSchSession(String ip , int port, String userName, String password){
        JSch jSch = new JSch();
        Session session = null;
        try {
            //创建连接
            session = jSch.getSession(userName,ip,port);
            session.setPassword(password);
            //是否使用密钥登录，一般默认为no
            session.setConfig("StrictHostKeyChecking", "no");
            //启用连接
            session.connect();
            //System.out.println("======================服务器连接成功=======================");
        }catch (Exception e){
            throw new RuntimeException("======================服务器连接失败=======================");
        }
        return session;
    }

    //jsch关闭连接
    public void closeJSchSession(Session session){
        if (session != null) {
            try {
                session.disconnect();
                System.out.println("====================服务器连接关闭成功======================");
            }catch (Exception e){
                throw new RuntimeException("======================服务器连接关闭失败=======================");
            }
        }
    }
}
