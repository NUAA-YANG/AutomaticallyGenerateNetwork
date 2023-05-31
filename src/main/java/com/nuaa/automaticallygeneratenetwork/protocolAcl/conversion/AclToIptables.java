package com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion;

import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.GetInfo;
import com.nuaa.automaticallygeneratenetwork.service.IptablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author YZX
 * @Create 2023-05-10 16:03
 * @Java-version jdk1.8
 */
//将ACl语句转化为Iptables类
@Service
@Component
public class AclToIptables {

    @Autowired
    NetmaskUtils netmaskUtils;
    @Autowired
    IptablesService iptablesService;
    @Autowired
    IptablesToLine iptablesToLine;



    /**
     * @description  将不同厂商的单条ACL语句转化为Iptables类
     * @date 2023/5/10 16:21
     * @params [line：单条ACl语句, lxdName：容器名称, manufacturer：厂商名称]
     * @returns Iptables
     */
    public Iptables turnLineToIptables(String line,String lxdName,String manufacturer){
        //中兴匹配规则
        String ZhongXing_Regex = "rule\\s+\\d*\\s*(permit|deny)(?:\\s+(tcp|udp|icmp|ip))?" +
                //匹配源ip地址 或 “any” 关键字
        "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}|\\w+))?" +
                //匹配源子网掩码
        "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}))?" +
                //匹配源端口“range”关键字、起始端口和结束端口  或   端口“eq”关键字和端口值
        "(?:\\s+(range)\\s+(\\d+)-(\\d+)|\\s+(eq|ge|le)\\s+(\\d+))?" +
                //匹配目的ip地址 或 “any” 关键字
        "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}|\\w+))?" +
                //匹配目的子网掩码
        "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}))?" +
                //匹配目的端口“range”关键字、起始端口和结束端口  或   端口“eq”关键字和端口值
        "(?:\\s+(range)\\s+(\\d+)-(\\d+)|\\s+(eq|ge|le)\\s+(\\d+))?" +
                //匹配两个特殊关键字
        "(?:\\s+(established))?" +
        "(?:\\s+precedence\\s+\\d+)?";

        String HuaWei_Regex = "rule\\s+\\d*\\s*(permit|deny)(?:\\s+(tcp|udp|icmp|ip))?" +
                "(?:\\s+source\\s((?:\\d{1,3}\\.){3}\\d{1,3}|any))?" +
                "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}))?" +
                "(?:\\s+destination\\s((?:\\d{1,3}\\.){3}\\d{1,3}|any))?" +
                "(?:\\s+((?:\\d{1,3}\\.){3}\\d{1,3}))?" +
                "(?:\\s+(range)\\s+(\\d+)\\s(\\d+)|\\s+source-port\\s+(eq|ge|le)\\s(\\d+))?" +
                "(?:\\s+(range)\\s+(\\d+)\\s(\\d+)|\\s+destination-port\\s+(eq|ge|le)\\s(\\d+))?" +
                "(?:\\s+(established))?" +//tcp group(17)
                "(?:\\s+precedence\\s+\\d+)?";

        //定义匹配
        Pattern pattern = null;
        if ("ZhongXing".equals(manufacturer)){
            pattern = Pattern.compile(ZhongXing_Regex);
        } else if ("HuaWei".equals(manufacturer)) {
            pattern = Pattern.compile(HuaWei_Regex);
        }else {
            //默认使用中兴的匹配规则
            pattern = Pattern.compile(ZhongXing_Regex);
        }
        Matcher matcher = pattern.matcher(line);
        //用于记录属性
        List<String> proList = new ArrayList<>();
        //起始为null，只有匹配成功才创建类
        Iptables iptables = null;
        //匹配成功才封装属性
        if (matcher.find()){
            iptables = new Iptables();
            //将属性封装到集合当中
            for (int i = 1 ; i<17 ; i++){
                if (matcher.group(i)!=null){
                    proList.add(matcher.group(i));
                }else {
                    proList.add(null);
                }
            }

            //将集合中的属性转化为实体类
            iptables.setId(0);
            iptables.setManufacturer(manufacturer);
            iptables.setLxdName(lxdName);
            iptables.setTableName("filter");
            iptables.setRule("A");
            iptables.setChain("FORWARD");

            if (proList.size()>0){
                iptables.setJudge(proList.get(0));
                iptables.setProtocol(proList.get(1));

                iptables.setSIp(proList.get(2));
                if (proList.get(3)!=null){
                    //将二进制的子网掩码转化为数字
                    iptables.setSNetmask(netmaskUtils.netmaskToNum(proList.get(3)));
                }
                iptables.setSRange(proList.get(4));
                iptables.setSStartPort(proList.get(5));
                iptables.setSDestPort(proList.get(6));
                iptables.setSEq(proList.get(7));
                iptables.setSPort(proList.get(8));

                iptables.setDIp(proList.get(9));
                if (proList.get(10)!=null){
                    //将二进制的子网掩码转化为数字
                    iptables.setDNetmask(netmaskUtils.netmaskToNum(proList.get(10)));
                }
                iptables.setDRange(proList.get(11));
                iptables.setDStartPort(proList.get(12));
                iptables.setDDestPort(proList.get(13));
                iptables.setDEq(proList.get(14));
                iptables.setDPort(proList.get(15));
            }

        }

        return iptables;
    }


    //处理一个ACL文本文件
    public List<Iptables> turnTxtToIptables(File file) throws IOException {
        //用来返回结果
        List<Iptables> iptablesList = new ArrayList<>();
        //获得容器名称
        String[] splitInfo = file.getName().split("_");
        String lxdName = splitInfo[0];
        //获得厂商名称
        String manufacturer = splitInfo[1].split("\\.")[0];
        //创建读取流
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line=reader.readLine())!=null && !"".equals(line)){
            Iptables iptables = turnLineToIptables(line, lxdName, manufacturer);
            if (iptables!=null){
                iptablesList.add(iptables);
            }
        }
        return iptablesList;
    }


    //处理文件夹下面的所有ACL文本
    public Map<String,List<Iptables>> turnToIptables(String aclPathName) throws IOException {
        Map<String,List<Iptables>> map = new HashMap<>();
        //获取目录下的所有文件名称
        String[] fileArray = new File(aclPathName).list();
        for (String name:fileArray){
            File file = new File(aclPathName +"/"+ name);
            //用来返回类对象
            List<Iptables> iptablesList = turnTxtToIptables(file);
            //存入数据库中
            List<Iptables> saveList = iptablesService.saveList(iptablesList);
            //根据容器名称进行存储
            map.put(name.split("_")[0],saveList);
        }
        return map;
    }

}
