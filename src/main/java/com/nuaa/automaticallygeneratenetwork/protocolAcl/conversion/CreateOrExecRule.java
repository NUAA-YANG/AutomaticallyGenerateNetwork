package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-12 16:19
 * @Java-version jdk1.8
 */
//创造防火墙的配置文件或直接执行防火墙命令
@Service
@Component
public class CreateOrExecRule {

    @Autowired
    IptablesToLine iptablesToLine;


    //直接执行命令(不需要调用配置文件的替换)
    public List<String> execIptablesRule(String lxdName, List<Iptables> iptablesList){
        List<String> cmds = new ArrayList<>();
        cmds.add("lxc exec "+lxdName+" -- apt-get install iptables -y;");
        for (int i = 0 ; i<iptablesList.size();i++){
            String line = iptablesToLine.turnIptablesToLine(iptablesList.get(i));
            cmds.add("lxc exec "+lxdName+" -- "+line);
        }
        return cmds;
    }

    //将Iptables类集合转化为Iptables语句集合，生成配置文件(需要调用配置文件的替换)
    public String createIptablesRule(List<Iptables> iptablesList){
        StringBuffer rule = new StringBuffer();
        rule.append("*filter\n" +
                ":INPUT ACCEPT [0:0]\n" +
                ":FORWARD ACCEPT [0:0]\n" +
                ":OUTPUT ACCEPT [0:0]\n");
        for (int i = 0; i < iptablesList.size(); i++) {
            //将每个类转化为语句
            String line = iptablesToLine.turnIptablesToLine(iptablesList.get(i));
            rule.append(line.trim()+"\n");
        }
        rule.append("COMMIT\n");
        return new String(rule);
    }

    //创建自启动脚本文件同时设置防火墙读取文件信息
    public String createBash(){
        String resultBash = "#!/bin/bash\n"+
                "chmod 777 /lib/systemd/system/rc-local.service\n"+
                "systemctl enable /lib/systemd/system/rc-local.service\n"+
                "iptables-restore < /etc/iptables.rules;";
        return resultBash;
    }

    //保证每次重启都会读取防火墙配置文件
    public String createRcLocal(){
        String RcLocal = "#!/bin/sh\n" +
                "iptables-restore < /etc/iptables.rules\n" +
                "exit 0";
        return RcLocal;
    }
}
