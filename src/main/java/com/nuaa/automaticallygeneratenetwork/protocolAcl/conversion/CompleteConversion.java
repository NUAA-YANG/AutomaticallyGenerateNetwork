package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Author YZX
 * @Create 2023-05-11 18:42
 * @Java-version jdk1.8
 */
//完整的转化，将ACL文本命令转化为类存入数据库并且生成cmd命令提供执行
@Service
@Component
public class CompleteConversion {
    @Autowired
    AclToIptables aclToIptables;
    @Autowired
    IptablesToLine iptablesToLine;
    @Autowired
    CreateOrExecRule createOrExecRule;

    //完整转化实现
    public List<String> finalConversion(String aclPathName) throws IOException {
        List<String> cmds = new ArrayList<>();
        //1. 存入数据库，并且获得数据库的存储类
        Map<String, List<Iptables>> map = aclToIptables.turnToIptables(aclPathName);
        //2. 遍历获得的存储类，生成命令行语句
        Set<String> keySet = map.keySet();
        for (String name:keySet){
            //2.1 根据不同的容器名称，获得不同的命令转化
            List<Iptables> iptablesList = map.get(name);
            List<String> turnToLine = createOrExecRule.execIptablesRule(name,iptablesList);
            //2.2 添加iptables命令行
            cmds.addAll(turnToLine);
        }
        return cmds;
    }
}
