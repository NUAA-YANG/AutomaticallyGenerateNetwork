package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import com.nuaa.automaticallygeneratenetwork.service.IptablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
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
    IptablesService iptablesService;
    @Autowired
    CreateRuleBash createRuleBash;


    //统一调用
    public List<String> finalConversion(String aclPathName) throws IOException {
        List<String> cmds = new ArrayList<>();
        //获得acl路径下转化完毕的iptables类，其中键是容器名称
        Map<String, List<Iptables>> map = findIptables(aclPathName);
        Set<String> keySet = map.keySet();
        //对每个容器都创建防火墙相关脚本
        for (String lxdName:keySet){
            List<Iptables> iptablesList = map.get(lxdName);
            List<String> allBash = createAllBash(lxdName, iptablesList);
            cmds.addAll(allBash);
        }
        return cmds;
    }

    //在数据库中找到配置文件描述的iptables类
    public Map<String,List<Iptables>> findIptables(String aclPathName) throws IOException {
        Map<String,List<Iptables>> result = new HashMap<>();
        //获取目录下的所有文件名称
        String[] fileArray = new File(aclPathName).list();
        for (String name:fileArray){
            String lxdName = name.split("_")[0];
            //如果map中没有这个容器对应的iptables集合，那么就存入map中
            if (!result.containsKey(lxdName)){
                List<Iptables> findList = iptablesService.getListByLxdName(lxdName);
                result.put(lxdName,findList);
            }
        }
        return result;
    }


    //对于每个容器的防火墙，都来生成bash文件
    public List<String> createAllBash(String lxdName,List<Iptables> iptablesList){
        List<String> cmds = new ArrayList<>();
        //1.创建防火墙命令
        String iptablesRule = createRuleBash.createIptablesRule(iptablesList);
        //1.1 生成防火墙配置文件
        cmds.add("echo \""+iptablesRule+"\" >> /root/AutoNetwork/"+lxdName+"/iptables.rules;");
        //2.创建保存防火墙的脚本文件
        String RcLocal = createRuleBash.createRcLocal();
        //2.2 生成保存防火墙的脚本文件
        cmds.add("echo \""+RcLocal+"\" >> /root/AutoNetwork/"+lxdName+"/rc.local;");
        //2.3 为防火墙的脚本文件设置权限
        cmds.add("chmod 777 /root/AutoNetwork/"+lxdName+"/rc.local");
        //3. 创建开机自启脚本
        String bash = createRuleBash.createBash();
        //3.1 生成开机自启脚本文件
        cmds.add("echo \""+bash+"\" >> /root/AutoNetwork/"+lxdName+"/startUpIptables.sh;");
        return cmds;
    }



}
