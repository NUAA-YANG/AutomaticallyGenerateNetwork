package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-12 16:27
 * @Java-version jdk1.8
 */
//替换配置文件
@Service
@Component
public class FileRulePush {
    //将生成的配置文件替换
    public List<String> pushRule(String aclPathName){
        List<String> cmds = new ArrayList<>();
        //获取目录下的所有文件名称
        String[] fileArray = new File(aclPathName).list();
        for (String name:fileArray){
            String lxdName = name.split("\\.")[0];
            cmds.add("lxc file push /root/AutoNetwork/"+lxdName+"/iptables.rules "+lxdName+"/etc/");
        }
        return cmds;
    }
}
